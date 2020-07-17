package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MusicActivity extends AppCompatActivity {
    ImageView mBacktoVideoCut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        mBacktoVideoCut=findViewById(R.id.btn_backtovideocut);
        setListeners();
    }

    private void setListeners(){
        MusicActivity.OnClick onClick = new OnClick();
        mBacktoVideoCut.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.btn_backtovideocut:
                    intent = new Intent(MusicActivity.this,VideoCutActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }
}