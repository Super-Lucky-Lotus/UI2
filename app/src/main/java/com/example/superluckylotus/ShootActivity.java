package com.example.superluckylotus;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * @version: 1.0
 * @author: 黄诗雯、宋佳容
 * @className: ShootActivity
 * @packageName:com.example.superluckylotus
 * @description: 拍摄界面
 * @data: 2020.07.12 00:48
 **/

/**
 * @version: 3.0
 * @author: 黄诗雯、宋佳容
 * @className: ShootActivity
 * @packageName:com.example.superluckylotus
 * @description: 调用摄像头，拍摄视频
 * @data: 2020.07.20 20:32
 **/


public class ShootActivity extends AppCompatActivity {
    private int SELECT_IMAGE_REQUEST_CODE=201;//判断请求识别码
    private Button back_Btn;
    private ImageButton mVedio;
    Button takeVideo;
    /*用来记录录像存储路径*/
    File file = new File(Environment.getExternalStorageDirectory().getPath() + "/video.mp4");//设置录像存储路径
    Uri uri = Uri.fromFile(file);//文件转成Uri格式


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoot);
        back_Btn = (Button)findViewById(R.id.btn_back);
        mVedio=findViewById(R.id.btn_shoot);
        takeVideo = (Button) findViewById(R.id.take_video);
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        setListeners();
    }


    private void setListeners(){
        OnClick onClick =new OnClick();
        back_Btn.setOnClickListener(onClick);
        mVedio.setOnClickListener(onClick);
        takeVideo.setOnClickListener(onClick);
        ((ImageButton)findViewById(R.id.btn_photo)).setOnClickListener(onClick);
    }


    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.btn_back:
                    intent = new Intent(ShootActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_shoot:
                    VideoCutActivity.filepath=Environment.getExternalStorageDirectory().getPath()+"/video.mp4";
                    intent = new Intent(ShootActivity.this,VideoCutActivity.class);
                    startActivity(intent);
                    break;
                case R.id.take_video:
                    try {
                        if (file.exists()) {
                            file.delete();
                        }
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 激活系统的照相机进行录像，通过Intent激活相机并实现录像功能
                    Intent intent1 = new Intent();
                    intent1.setAction("android.media.action.VIDEO_CAPTURE");
                    intent1.addCategory("android.intent.category.DEFAULT");
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    ActivityCompat.requestPermissions(ShootActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                    startActivityForResult(intent1, 0);
                    break;
                case R.id.btn_photo:
                    //WU.setContext(ShootActivity.this);
                    //WU.toast("hh");
                    Intent innerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");

                    startActivityForResult(wrapperIntent, SELECT_IMAGE_REQUEST_CODE);
                    break;
            }

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode==SELECT_IMAGE_REQUEST_CODE&&resultCode==RESULT_OK){//从图库选择图片
            String[] proj = {MediaStore.Video.Media.DATA};
            // 获取选中图片的路径
            Cursor cursor = this.getContentResolver().query(intent.getData(),proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                String photoPath = cursor.getString(columnIndex);
                //WU.toast(photoPath);
                VideoCutActivity.filepath=photoPath;
                startActivity(new Intent().setClass(ShootActivity.this,VideoCutActivity.class));
            }
            cursor.close();
        }
    }
}