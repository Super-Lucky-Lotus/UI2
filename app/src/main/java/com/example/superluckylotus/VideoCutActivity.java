package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

/**
 * @version: 3.0
 * @author: 黄诗雯
 * @className: VideoCutActivity
 * @packageName:com.example.superluckylotus
 * @description: 调用相册，选取视频
 * @data: 2020.07.20 22:46
 **/

public class VideoCutActivity extends AppCompatActivity {
    ImageView mBacktoShoot;
    private VideoView mVideo;
    ImageView mMusic;
    ImageView mSpeed;
    ImageView mReady;
    static String filepath;
    private SpeedDialog sd;
    File file = new File( filepath);//设置录像存储路径
    Uri uri = Uri.fromFile(file);//文件转成Uri格式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_cut);
        mBacktoShoot=(ImageView)findViewById(R.id.btn_backtoshoot);
        mMusic=(ImageView)findViewById(R.id.btn_music);
        mSpeed=(ImageView)findViewById(R.id.btn_speed);
        mReady=findViewById(R.id.btn_ready);
        mVideo=(VideoView)findViewById(R.id.videoView);
        initVideoPath();
        sd=new SpeedDialog(this);
        setListeners();
    }

    private void initVideoPath() {
        if (ContextCompat.checkSelfPermission(VideoCutActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(VideoCutActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        Toast.makeText(this, "have found video", Toast.LENGTH_SHORT).show();
        mVideo.setVideoURI(uri);
        mVideo.setVideoPath(file.getAbsolutePath());
        MediaController mediaController=new MediaController(this);
        mVideo.setMediaController(mediaController);
        mediaController.setMediaPlayer(mVideo);
        mVideo.requestFocus();
        mVideo.start();
    }

    private void setListeners(){
        VideoCutActivity.OnClick onClick = new VideoCutActivity.OnClick();
        mBacktoShoot.setOnClickListener(onClick);
        mMusic.setOnClickListener(onClick);
        mSpeed.setOnClickListener(onClick);
        mReady.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.btn_backtoshoot:
                    intent = new Intent(VideoCutActivity.this,ShootActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_music:
                    intent = new Intent(VideoCutActivity.this,MusicActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_speed:
                    sd.popupWindowDialog(v);
                    break;
                case R.id.btn_ready:
                    intent = new Intent(VideoCutActivity.this,VideoPostActivity.class);
                    startActivity(intent);
                    break;
            }

        }
    }
}