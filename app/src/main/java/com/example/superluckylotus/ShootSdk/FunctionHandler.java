package com.example.superluckylotus.ShootSdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.text.TextUtils;
import android.util.SparseArray;


import androidx.annotation.NonNull;

import com.example.superluckylotus.ShootActivity;
import com.vecore.exception.InvalidArgumentException;
import com.veuisdk.SdkEntry;

import java.util.ArrayList;

import static com.example.superluckylotus.ShootActivity.EDIT_REQUEST_CODE;

/**
 * 因MainActivity已经有太多回调，现把部分功能的回调拆分到此界面
 *
 * @date 2019/06/25
 */
public class FunctionHandler {

    /**
     * 视频转码
     */
    public static final int ALBUM_ALONE_TRANSCODING_REQUEST_CODE = 2000;

    /**
     * 视频裁剪
     */
    public static final int ALBUM_ALONE_CROP_REQUEST_CODE = 2001;

    /**
     * 调色
     */
    public static final int ALBUM_ALONE_TON_REQUEST_CODE = 2002;

    /**
     * 变速
     */
    public static final int ALBUM_ALONE_SPEED_REQUEST_CODE = 2003;
    /**
     * 变声
     */
    public static final int ALBUM_ALONE_SOUND_EFFECT_REQUEST_CODE = 2004;
    /**
     * 配音
     */
    public static final int ALBUM_ALONE_DUBBING_REQUEST_CODE = 2005;
    /**
     * 转场
     */
    public static final int ALBUM_ALONE_TRANSITION_REQUEST_CODE = 2006;
    /**
     * 倒序
     */
    public static final int ALBUM_ALONE_REVERSE_REQUEST_CODE = 2007;
    /**
     * 封面
     */
    public static final int ALBUM_ALONE_COVER_REQUEST_CODE = 2008;
    /**
     * 截取
     */
    public static final int ALBUM_ALONE_INTERCEPT_REQUEST_CODE = 2009;
    /**
     * 字幕
     */
    public static final int ALBUM_ALONE_SUBTITLE_REQUEST_CODE = 2010;
    /**
     * 贴纸
     */
    public static final int ALBUM_ALONE_STICKER_REQUEST_CODE = 2011;
    /**
     * 滤镜
     */
    public static final int ALBUM_ALONE_FILTER_REQUEST_CODE = 2012;
    /**
     * 特效
     */
    public static final int ALBUM_ALONE_EFFECT_REQUEST_CODE = 2013;
    /**
     * 去水印
     */
    public static final int ALBUM_ALONE_OSD_REQUEST_CODE = 2014;
    /**
     * 画中画
     */
    public static final int ALBUM_ALONE_COLLAGE_REQUEST_CODE = 2015;
    /**
     * 涂鸦
     */
    public static final int ALBUM_ALONE_GRAFFITI_REQUEST_CODE = 2016;
    /**
     * 压缩
     */
    public static final int ALBUM_ALONE_COMPRESS_REQUEST_CODE = 2017;

    /**
     * 图片时长
     */
    public static final int ALBUM_ALONE_IMAGE_DURATION_REQUEST_CODE = 2018;

    /**
     * 图片时长
     */
    private ShootActivity.ActivityResultHandler albumImageDurationResultHandler = new ShootActivity.ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data.getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0 && !TextUtils.isEmpty(arrMediaListPath.get(0))) {
                    try {
                        SdkEntry.imageDuration(context, arrMediaListPath.get(0), EDIT_REQUEST_CODE);
                    } catch (InvalidArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };


    /**
     * 视频转码
     */
    private ShootActivity.ActivityResultHandler albumVideoTranscodResultHandler = new ShootActivity.ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data.getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0 && !TextUtils.isEmpty(arrMediaListPath.get(0))) {
                    try {
                        SdkEntry.videoTrans(context, arrMediaListPath.get(0), EDIT_REQUEST_CODE);
                    } catch (InvalidArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
    /**
     * 视频裁剪
     */
    private ShootActivity.ActivityResultHandler albumVideoCropResultHandler = new ShootActivity.ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data.getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0 && !TextUtils.isEmpty(arrMediaListPath.get(0))) {
                    try {
                        SdkEntry.videoCrop(context, arrMediaListPath.get(0), EDIT_REQUEST_CODE);
                    } catch (InvalidArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
    /**
     * 视频调色
     */
    private ShootActivity.ActivityResultHandler albumVideoTonResultHandler = new ShootActivity.ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data.getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0 && !TextUtils.isEmpty(arrMediaListPath.get(0))) {
                    try {
                        SdkEntry.videoTon(context, arrMediaListPath.get(0), EDIT_REQUEST_CODE);
                    } catch (InvalidArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
    /**
     * 视频变速
     */
    private ShootActivity.ActivityResultHandler albumVideoSpeedResultHandler = new ShootActivity.ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data.getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0 && !TextUtils.isEmpty(arrMediaListPath.get(0))) {
                    try {
                        SdkEntry.videoSpeed(context, arrMediaListPath.get(0), EDIT_REQUEST_CODE);
                    } catch (InvalidArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    /**
     * 视频变声
     */
    private ShootActivity.ActivityResultHandler albumVideoSoundResultHandler = new ShootActivity.ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data.getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0 && !TextUtils.isEmpty(arrMediaListPath.get(0))) {
                    try {
                        SdkEntry.videoSoundEffect(context, arrMediaListPath.get(0), EDIT_REQUEST_CODE);
                    } catch (InvalidArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
    /**
     * 视频配音
     */
    private ShootActivity.ActivityResultHandler albumVideoDubbingResultHandler = new ShootActivity.ActivityResultHandler() {
        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data.getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0 && !TextUtils.isEmpty(arrMediaListPath.get(0))) {
                    try {
                        SdkEntry.videoDubbing(context, arrMediaListPath.get(0), EDIT_REQUEST_CODE);
                    } catch (InvalidArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
    /**
     * 视频转场
     */
    private ShootActivity.ActivityResultHandler albumVideoTransitionResultHandler = new ShootActivity.ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data.getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0 && !TextUtils.isEmpty(arrMediaListPath.get(0))) {
                    try {
                        SdkEntry.videoTransition(context, arrMediaListPath, EDIT_REQUEST_CODE);
                    } catch (InvalidArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    /**
     * 视频倒序
     */
    private ShootActivity.ActivityResultHandler albumVideoReverseResultHandler = new ShootActivity.ActivityResultHandler() {
        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data.getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0 && !TextUtils.isEmpty(arrMediaListPath.get(0))) {
                    try {
                        SdkEntry.videoReverse(context, arrMediaListPath.get(0), EDIT_REQUEST_CODE);
                    } catch (InvalidArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
    /**
     * 视频封面
     */
    private ShootActivity.ActivityResultHandler albumVideoCoverResultHandler = new ShootActivity.ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data.getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0) {
                    if (!TextUtils.isEmpty(arrMediaListPath.get(0))) {
                        try {
                            SdkEntry.videoCover(context, arrMediaListPath.get(0), EDIT_REQUEST_CODE);
                        } catch (InvalidArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };
    /**
     * 视频截取
     */
    private ShootActivity.ActivityResultHandler albumVideoInterceptResultHandler = new ShootActivity.ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data.getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0) {
                    if (!TextUtils.isEmpty(arrMediaListPath.get(0))) {
                        try {
                            SdkEntry.videoIntercept(context, arrMediaListPath.get(0), EDIT_REQUEST_CODE);
                        } catch (InvalidArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };
    /**
     * 视频字幕
     */
    private ShootActivity.ActivityResultHandler albumVideoSubtitleResultHandler = new ShootActivity.ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data.getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0) {
                    if (!TextUtils.isEmpty(arrMediaListPath.get(0))) {
                        try {
                            SdkEntry.videoSubtitle(context, arrMediaListPath.get(0), EDIT_REQUEST_CODE);
                        } catch (InvalidArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };
    /**
     * 视频贴纸
     */
    private ShootActivity.ActivityResultHandler albumVideoStickerResultHandler = new ShootActivity.ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data.getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0) {
                    if (!TextUtils.isEmpty(arrMediaListPath.get(0))) {
                        try {
                            SdkEntry.videoStiker(context, arrMediaListPath.get(0), EDIT_REQUEST_CODE);
                        } catch (InvalidArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };
    /**
     * 视频滤镜
     */
    private ShootActivity.ActivityResultHandler albumVideoFilterResultHandler = new ShootActivity.ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data.getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0) {
                    if (!TextUtils.isEmpty(arrMediaListPath.get(0))) {
                        try {
                            SdkEntry.videoFilter(context, arrMediaListPath.get(0), EDIT_REQUEST_CODE);
                        } catch (InvalidArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };
    /**
     * 视频特效
     */
    private ShootActivity.ActivityResultHandler albumVideoEffectResultHandler = new ShootActivity.ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data.getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0) {
                    if (!TextUtils.isEmpty(arrMediaListPath.get(0))) {
                        try {
                            SdkEntry.videoEffect(context, arrMediaListPath.get(0), EDIT_REQUEST_CODE);
                        } catch (InvalidArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };
    /**
     * 视频去水印
     */
    private ShootActivity.ActivityResultHandler albumVideoOSDResultHandler = new ShootActivity.ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data.getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0) {
                    if (!TextUtils.isEmpty(arrMediaListPath.get(0))) {
                        try {
                            SdkEntry.videoOSD(context, arrMediaListPath.get(0), EDIT_REQUEST_CODE);
                        } catch (InvalidArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };
    /**
     * 视频画中画
     */
    private ShootActivity.ActivityResultHandler albumVideoCollageResultHandler = new ShootActivity.ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data.getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0) {
                    if (!TextUtils.isEmpty(arrMediaListPath.get(0))) {
                        try {
                            SdkEntry.videoCollage(context, arrMediaListPath.get(0), EDIT_REQUEST_CODE);
                        } catch (InvalidArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };
    /**
     * 视频涂鸦
     */
    private ShootActivity.ActivityResultHandler albumVideoGraffitiResultHandler = new ShootActivity.ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data.getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0) {
                    if (!TextUtils.isEmpty(arrMediaListPath.get(0))) {
                        try {
                            SdkEntry.videoGraffiti(context, arrMediaListPath.get(0), EDIT_REQUEST_CODE);
                        } catch (InvalidArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };

    /**
     * 视频压缩
     */
    private ShootActivity.ActivityResultHandler albumVideoCompressResultHandler = new ShootActivity.ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data.getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0) {
                    if (!TextUtils.isEmpty(arrMediaListPath.get(0))) {
                        try {
                            SdkEntry.videoCompress(context, arrMediaListPath.get(0), EDIT_REQUEST_CODE);
                        } catch (InvalidArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };

    /**
     * 绑定回调
     *
     * @param registeredActivityResultHandlers
     */
    public void registerActivityResultHandler(@NonNull SparseArray<ShootActivity.ActivityResultHandler> registeredActivityResultHandlers) {

        registeredActivityResultHandlers.put(ALBUM_ALONE_TRANSCODING_REQUEST_CODE, albumVideoTranscodResultHandler);
        registeredActivityResultHandlers.put(ALBUM_ALONE_CROP_REQUEST_CODE, albumVideoCropResultHandler);
        registeredActivityResultHandlers.put(ALBUM_ALONE_TON_REQUEST_CODE, albumVideoTonResultHandler);
        registeredActivityResultHandlers.put(ALBUM_ALONE_SPEED_REQUEST_CODE, albumVideoSpeedResultHandler);
        registeredActivityResultHandlers.put(ALBUM_ALONE_SOUND_EFFECT_REQUEST_CODE, albumVideoSoundResultHandler);
        registeredActivityResultHandlers.put(ALBUM_ALONE_DUBBING_REQUEST_CODE, albumVideoDubbingResultHandler);
        registeredActivityResultHandlers.put(ALBUM_ALONE_TRANSITION_REQUEST_CODE, albumVideoTransitionResultHandler);
        registeredActivityResultHandlers.put(ALBUM_ALONE_REVERSE_REQUEST_CODE, albumVideoReverseResultHandler);
        registeredActivityResultHandlers.put(ALBUM_ALONE_COVER_REQUEST_CODE, albumVideoCoverResultHandler);
        registeredActivityResultHandlers.put(ALBUM_ALONE_INTERCEPT_REQUEST_CODE, albumVideoInterceptResultHandler);
        registeredActivityResultHandlers.put(ALBUM_ALONE_SUBTITLE_REQUEST_CODE, albumVideoSubtitleResultHandler);
        registeredActivityResultHandlers.put(ALBUM_ALONE_STICKER_REQUEST_CODE, albumVideoStickerResultHandler);
        registeredActivityResultHandlers.put(ALBUM_ALONE_FILTER_REQUEST_CODE, albumVideoFilterResultHandler);
        registeredActivityResultHandlers.put(ALBUM_ALONE_EFFECT_REQUEST_CODE, albumVideoEffectResultHandler);
        registeredActivityResultHandlers.put(ALBUM_ALONE_OSD_REQUEST_CODE, albumVideoOSDResultHandler);
        registeredActivityResultHandlers.put(ALBUM_ALONE_COLLAGE_REQUEST_CODE, albumVideoCollageResultHandler);
        registeredActivityResultHandlers.put(ALBUM_ALONE_GRAFFITI_REQUEST_CODE, albumVideoGraffitiResultHandler);
        registeredActivityResultHandlers.put(ALBUM_ALONE_COMPRESS_REQUEST_CODE, albumVideoCompressResultHandler);
        registeredActivityResultHandlers.put(ALBUM_ALONE_IMAGE_DURATION_REQUEST_CODE, albumImageDurationResultHandler);
    }

}
