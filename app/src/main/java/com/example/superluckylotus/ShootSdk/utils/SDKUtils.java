package com.example.superluckylotus.ShootSdk.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.MediaStore.Video;
import android.text.TextUtils;
import android.util.Log;

import com.example.superluckylotus.Phone;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.example.superluckylotus.R;
import com.vecore.VirtualVideo;
import com.vecore.base.lib.utils.BitmapUtils;
import com.vecore.base.lib.utils.ThreadPoolUtils;
import com.vecore.exception.InvalidArgumentException;
import com.vecore.models.MediaObject;
import com.vecore.models.Scene;
import com.veuisdk.SdkEntry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

public class SDKUtils {

    /**
     * 是否为有效文件
     *
     * @param checkPath 文件
     * @return 返回true文件有效
     */
    public static boolean isValidFile(String checkPath) {
        if (!TextUtils.isEmpty(checkPath)) {
            File file = new File(checkPath);
            return file.exists() && file.length() > 0;
        } else {
            return false;
        }
    }

    /**
     * 将asset文件保存为指定文件
     */
    public static boolean assetRes2File(AssetManager am, String assetFile,
                                        String dstFile) {
        if (isValidFile(dstFile)) {
            return false;
        }
        if (TextUtils.isEmpty(dstFile)) {
            return false;
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(dstFile);
            byte[] pBuffer = new byte[1024];
            int nReadLen;
            if (null == am) {
                return false;
            }
            InputStream is = am.open(assetFile);
            while ((nReadLen = is.read(pBuffer)) != -1) {
                os.write(pBuffer, 0, nReadLen);
            }
            os.flush();
            os.close();
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            new File(dstFile).delete();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            new File(dstFile).delete();
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 绘制片尾图片 并将其保存为临时文件
     *
     * @param author      作者名字
     * @param videoHeight 视频高度
     * @return 图片路径
     */
    public static String createVideoTrailerImage(Activity context,
                                                 String author, int videoHeight, int horiPadding, int dataWidth) {
        File logoFile = new File(((Phone) context.getApplication()).getCustomPath(), "trailer_logo.png");
        String path = logoFile.getAbsolutePath();
        Bitmap bmpLogo = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_launcher);
        Bitmap bmpDate = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.video_trailer_date);
        Bitmap bmpAuthor = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.video_trailer_author);

        Paint bmpPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bmpPaint.setFilterBitmap(true);
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setARGB(255, 175, 171, 170);
        linePaint.setStrokeWidth(2);

        textPaint.setARGB(255, 232, 232, 232);

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String date = sDateFormat.format(new Date());

        int textSize = 150;
        while (true) {
            textPaint.setTextSize(textSize);
            float textHeight = textPaint.getFontMetrics().descent
                    - textPaint.getFontMetrics().ascent;

            if (textHeight > bmpDate.getHeight()) {
                textSize -= 1;
            } else {
                break;
            }
        }

        try {
            int authorLength = author.length();
            int authorByte = author.getBytes("GBK").length;
            while (authorByte > 16) {
                authorLength -= 1;
                author = author.substring(0, authorLength) + "...";
                authorByte = author.getBytes("GBK").length;
            }

        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        int logoWidth = bmpLogo.getWidth();
        int messageWidth = (int) (bmpDate.getWidth() + bmpAuthor.getWidth()
                + dataWidth + 40 + textPaint.measureText(date) + textPaint
                .measureText(author));

        int logoDateHeight = (bmpDate.getHeight() + bmpLogo.getHeight() + 80);

        float scale = (float) videoHeight / (logoDateHeight * 2);

        int bmpWidth;
        int logoLeft = 0;
        int top = (int) ((videoHeight - logoDateHeight * scale) / (2 * scale));
        int messageLeft = 0;
        if (logoWidth >= messageWidth) {
            bmpWidth = (int) (logoWidth * scale);
            messageLeft = (logoWidth - messageWidth) / 2;
        } else {
            bmpWidth = (int) (messageWidth * scale);
            logoLeft = (messageWidth - logoWidth) / 2;
        }

        Bitmap bmp = Bitmap.createBitmap(bmpWidth + 2 * horiPadding,
                videoHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bmp);
        canvas.scale(scale, scale);
        logoLeft += horiPadding / scale;
        messageLeft += horiPadding / scale;
        canvas.drawBitmap(bmpLogo, logoLeft, top, bmpPaint);

        top = top + bmpLogo.getHeight() + 40;
        canvas.drawLine(logoLeft, top, logoLeft + bmpLogo.getWidth(), top,
                linePaint);
        top = top + 40;
        int baseline = (int) ((bmpDate.getHeight()
                - textPaint.getFontMetrics().bottom - textPaint
                .getFontMetrics().top) / 2);
        int textTop = top + baseline;

        canvas.drawBitmap(bmpDate, messageLeft, top, bmpPaint);
        messageLeft = messageLeft + bmpDate.getWidth() + 20;

        canvas.drawText(date, messageLeft, textTop, textPaint);

        messageLeft = (int) (messageLeft + textPaint.measureText(date) + dataWidth);
        canvas.drawBitmap(bmpAuthor, messageLeft, top, bmpPaint);

        messageLeft = messageLeft + bmpAuthor.getWidth() + 20;
        canvas.drawText(author, messageLeft, textTop, textPaint);
        File file = new File(path);
        if (file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        bmp.recycle();
        return path;
    }

    /**
     * 播放视频
     */
    public static void onPlayVideo(Context context, String path) {
        SdkEntry.playVideo(context, path);
    }

    /**
     * 响应视频导出<br>
     * 1.读取视频信息并写入系统相册<br>
     * 2.播放该视频
     *
     * @param context   应用上下文
     * @param videoPath 视频路径
     */
    public static void onVideoExport(Context context, String videoPath) {
        if (!TextUtils.isEmpty(videoPath)) {
            // 读取导出视频的媒体信息，如宽度，持续时间等
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                retriever.setDataSource(videoPath);
                int nVideoWidth = Integer
                        .parseInt(retriever
                                .extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                int nVideoHeight = Integer
                        .parseInt(retriever
                                .extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                int duration = Integer
                        .parseInt(retriever
                                .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                // 写入系统相册
                insertToGallery(context, videoPath, duration, nVideoWidth,
                        nVideoHeight);
            } catch (Exception ex) {
            } finally {
                retriever.release();
            }
        } else {
            Log.d(TAG, "获取视频地址失败");
        }
    }

    /**
     * 将视频信息存入相册数据库
     *
     * @param path     视频路径
     * @param duration 视频持续时间
     * @param width    视频宽度
     * @param height   视频高度
     */
    private static void insertToGallery(final Context context, final String path, final int duration,
                                        final int width, final int height) throws InvalidArgumentException {

        ContentValues videoValues = new ContentValues();
        videoValues.put(MediaStore.Video.Media.TITLE, "未定义");
        videoValues.put(Video.Media.MIME_TYPE, "video/mp4");
        videoValues.put(Video.Media.DATA, path);
        videoValues.put(Video.Media.ARTIST,
                context.getString(R.string.app_name));
        videoValues.put(Video.Media.DATE_TAKEN, String.valueOf(System.currentTimeMillis()));
        videoValues.put(Video.Media.DESCRIPTION,
                context.getString(R.string.app_name));
        videoValues.put(Video.Media.DURATION, duration);
        videoValues.put(Video.Media.WIDTH, width);
        videoValues.put(Video.Media.HEIGHT, height);
        final Uri uri = context.getContentResolver().insert(Video.Media.EXTERNAL_CONTENT_URI, videoValues);
        if (null != uri && Build.VERSION.SDK_INT < 29) { //
            Log.e(TAG, "insertToGallery: " + uri);
//            insertToGallery: content://media/external/video/media/92628
            ThreadPoolUtils.executeEx(new Runnable() {
                @Override
                public void run() {
                    updateCover(context, uri, path, width, height);
                }
            });
        }
    }

    /**
     * 修改封面，支持：微信相册里的视频封面与sdk设置的封面一致
     *
     * @param context
     * @param uri
     * @param path
     * @param width
     * @param height
     */
    private static void updateCover(Context context, Uri uri, String path, int width, int height) {
        String enPath = uri.getPath();
        String videoId = enPath.substring(enPath.lastIndexOf("/") + 1, enPath.length());
        {
            //调用之后才缩略图数据库才会有记录
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inDither = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            long imageIdLong = Long.parseLong(videoId);
            Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(), imageIdLong, MediaStore.Video.Thumbnails.MINI_KIND, options);
            if (null != bitmap) {
                bitmap.recycle();
            }
        }

        final VirtualVideo virtualVideo = new VirtualVideo();
        Scene scene = virtualVideo.createScene();
        MediaObject mediaObject = null;
        try {
            mediaObject = new MediaObject(path);
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
        scene.addMedia(mediaObject);
        virtualVideo.addScene(scene);
        Bitmap bitmap = Bitmap.createBitmap(width / 2, height / 2, Bitmap.Config.ARGB_8888);
        if (virtualVideo.getSnapshot(context, 0.2f, bitmap, false)) {
            Cursor cursor = context.getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, null,
                    MediaStore.Video.Thumbnails.VIDEO_ID + " = ?", new String[]{videoId}, null);
            if (null != cursor) {
//                Log.e(TAG, "updateCover: " + cursor.getCount() + " videoId:" + videoId);
                if (cursor.moveToFirst()) {
                    String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
                    //updateCover: /storage/emulated/0/DCIM/.thumbnails/1584952619313.jpg
//                    Log.e(TAG, "updateCover: " + data);
                    try {
                        //强制替换缩略图内容
                        BitmapUtils.saveBitmapToFile(bitmap, 100, data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                cursor.close();
            }
        }
        bitmap.recycle();
        virtualVideo.release();
    }


    /**
     * 设置视频封面
     *
     * @param draweeView
     * @param pathVideoConver
     */
    public static void setCover(SimpleDraweeView draweeView, String pathVideoConver) {
        if (!TextUtils.isEmpty(pathVideoConver)) {
            ImageRequest requestVideoConver;

            String tmp = pathVideoConver;
            if (pathVideoConver.startsWith("/")) {
                //本地图片
                tmp = "file://" + pathVideoConver;
            }
            requestVideoConver = ImageRequestBuilder.newBuilderWithSource(Uri.parse(tmp))
                    .setRotationOptions(RotationOptions.autoRotate())
                    .setLocalThumbnailPreviewsEnabled(true)
                    .setImageDecodeOptions(ImageDecodeOptions.newBuilder().build())
                    .build();


            DraweeController placeHolderDraweeControllerVideoConver = Fresco.newDraweeControllerBuilder()
                    .setOldController(draweeView.getController())
                    .setImageRequest(requestVideoConver)
                    .setAutoPlayAnimations(false)
                    .build();
            draweeView.setController(placeHolderDraweeControllerVideoConver);
        } else {
            draweeView.setImageURI((String) null);
        }
    }

    /**
     * 获取时间
     *
     * @param time
     * @return
     */
    public static String getDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        try {
            return sdf.format(time);
        } catch (Exception e) {
            return sdf.format(System.currentTimeMillis());
        }
    }

}

