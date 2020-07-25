package com.example.superluckylotus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.facebook.imageutils.BitmapUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoPostActivity extends AppCompatActivity {

    private static final String TAG = "VideoPostActivity";
    Button mChoosePlace;
    ImageView mPost;
    ImageView mBacktocut;
    ImageView mVideoCover;
    Button mChooseVideo;
    Spinner mChooseTag;
    private String pic_path;
    private String video_path;

    private EditText detail_et;

    private Uri upload;//视频路径


    private List<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;

    private static boolean isLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_post);
        detail_et = (EditText) findViewById(R.id.editTextTextPersonName2);
        mChoosePlace=findViewById(R.id.btn_place);
        mBacktocut=findViewById(R.id.btn_backtovideocut);
        mChooseTag = findViewById(R.id.sp_posttype);
        mPost=findViewById(R.id.btn_post);
        mChooseVideo=findViewById(R.id.btn_choosevideo);
        mVideoCover=findViewById(R.id.iv_videocover);
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);//加载数据
        setListeners();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    //如果已创建就不再重新创建子线程了
                    if (thread == null) {

                        Toast.makeText(VideoPostActivity.this, "Begin Parse Data", Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 子线程中解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    Toast.makeText(VideoPostActivity.this, "Parse Succeed", Toast.LENGTH_SHORT).show();
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    Toast.makeText(VideoPostActivity.this, "Parse Failed", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void showPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String opt1tx = options1Items.size() > 0 ?
                        options1Items.get(options1).getPickerViewText() : "";

                String opt2tx = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2) : "";

                String opt3tx = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3) : "";

                String tx = opt1tx + opt2tx + opt3tx;
                Toast.makeText(VideoPostActivity.this, tx, Toast.LENGTH_SHORT).show();
                mChoosePlace.setText(tx);
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }


    //解析数据
    private void initJsonData() {
        /**
         *assets 目录下的Json文件
         * */
        String JsonData = new GetJsonDataUtil_Post().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }*/
                city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }

    //Gson 解析
    public ArrayList<JsonBean> parseData(String result) {
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    private void setListeners(){
        VideoPostActivity.OnClick onClick = new OnClick();
        mPost.setOnClickListener(onClick);
        mChoosePlace.setOnClickListener(onClick);
        mBacktocut.setOnClickListener(onClick);
        mChooseVideo.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.btn_place:
                    if (isLoaded) {
                        showPickerView();
                    } else {
                        Toast.makeText(VideoPostActivity.this, "Please waiting until the data is parsed", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_post:
                    Phone phoneObj = (Phone) getApplication();
                    final String phone = phoneObj.getPhone();
                    Log.v("ChangePhotoActivity", phone);
                    String path = "http://139.219.4.34/upload/";
                    Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
                    userParams.put("phone", phone);
                    userParams.put("city",mChoosePlace.getText().toString());
                    userParams.put("desc",detail_et.getText().toString());
                    userParams.put("label","二次元");
                    Map<String, String> VideoParams = new HashMap<>();
                    VideoParams.put("images", pic_path);
                    VideoParams.put("videos",video_path);


                    HttpServer.SuperHttpUtil.post2( path,userParams, VideoParams,new HttpServer.SuperHttpUtil.HttpCallBack() {
                        @Override
                        public void onSuccess(String result) throws JSONException {
//                            JSONObject result_json = new JSONObject(result);
//                            String login = result_json.getString("msg");
                            Log.v(TAG,"发布123："+result);
//                            if (login.equals("success")) {
//                                Toast.makeText(VideoPostActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
//                                Intent intent = null;
//                                intent = new Intent(VideoPostActivity.this, MainActivity.class);
//                                startActivity(intent);
//                            } else {
//                                Toast.makeText(VideoPostActivity.this, "未知错误 ", Toast.LENGTH_SHORT).show();
//                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(VideoPostActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFinish() {
                        }
                    });
                    intent = new Intent(VideoPostActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_backtovideocut:
                    intent = new Intent(VideoPostActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_choosevideo:
                    Intent intent2=new Intent(Intent.ACTION_GET_CONTENT);
                    intent2.setType("*/*");
                    intent2.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent2,50);
            }

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 50 && resultCode == RESULT_OK) {//从图库选择图片
            String[] proj = {MediaStore.Video.Media.DATA};
            // 获取选中图片的路径
            Cursor cursor = this.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, proj, null, null, null);
            while (cursor.moveToNext()) {
                //视频地址
                video_path = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                Bitmap bitmap = getVideoThumbnail(this,video_path);
                mVideoCover.setImageBitmap(bitmap);
            }
            cursor.close();
        }
    }

    // 获取视频缩略图
    public Bitmap getVideoThumbnail(Context context,String filePath) {
        Bitmap b=null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            b=retriever.getFrameAtTime();
            saveImageToGallery(context,b);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();

        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        //saveImageToGallery(VideoPostActivity.this,b);
        return b;
    }


    public void saveImageToGallery(Context context, Bitmap bmp) {
        //检查有没有存储权限
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "请至权限中心打开应用权限", Toast.LENGTH_SHORT).show();
        } else {
            // 新建目录appDir，并把图片存到其下
            File appDir = new File(context.getExternalFilesDir(null).getPath()+ "BarcodeBitmap");
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            String fileName = System.currentTimeMillis() + ".jpg";
            File file = new File(appDir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                pic_path = file.getPath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 把file里面的图片插入到系统相册中
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        file.getAbsolutePath(), fileName, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Toast.makeText(this, fileName, Toast.LENGTH_LONG);

            // 通知相册更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        }
    }


//    public void saveImageToGallery(Context context, Bitmap bmp) {
//        // 首先保存图片 创建文件夹
//        File appDir = new File(Environment.getExternalStorageDirectory(), "oasystem");
//        if (!appDir.exists()) {
//            appDir.mkdir();
//        }
//        //图片文件名称
//        String fileName = "oa_" + System.currentTimeMillis() + ".jpg";
//        File file = new File(appDir, fileName);
//        try {
//            FileOutputStream fos = new FileOutputStream(file);
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.flush();
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // 其次把文件插入到系统图库
//        String path2 = file.getAbsolutePath();
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(), path2, fileName, null);
//            pic_path = path2+"\\"+fileName;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
}