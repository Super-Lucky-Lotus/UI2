package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class VideoCutActivity extends AppCompatActivity {
    ImageView mBacktoShoot;
    ImageView mMusic;
    ImageView mSpeed;
    ImageView mReady;
    private SpeedDialog sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_cut);
        mBacktoShoot=(ImageView)findViewById(R.id.btn_backtoshoot);
        mMusic=(ImageView)findViewById(R.id.btn_music);
        mSpeed=(ImageView)findViewById(R.id.btn_speed);
        mReady=findViewById(R.id.btn_ready);
        sd=new SpeedDialog(this);
        setListeners();
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