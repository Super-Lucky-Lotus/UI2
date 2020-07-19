package com.example.superluckylotus;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * @version: 1.0
 * @author: 黄诗雯、宋佳容
 * @className: ShootActivity
 * @packageName:com.example.superluckylotus
 * @description: 拍摄界面
 * @data: 2020.07.12 00:48
 **/
public class ShootActivity extends AppCompatActivity {

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
            }

        }
    }

}