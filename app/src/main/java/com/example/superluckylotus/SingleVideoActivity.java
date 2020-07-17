package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @version: 2.0
 * @author: 宋佳容
 * @className: SingleVideoActivity
 * @packageName:com.example.superluckylotus
 * @description: 单个视频界面
 * @data: 2020.07.17 16:05
 **/

public class SingleVideoActivity extends AppCompatActivity {

//    private Button close_sv_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_video);
//        close_sv_btn = (Button) findViewById(R.id.close_sv);
//        setListeners();
    }

//    private void setListeners() {
//        OnClick onClick = new OnClick();
//        close_sv_btn.setOnClickListener(onClick);
//    }
//
//    class OnClick implements View.OnClickListener {
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.close_sv:
//                    finish();
//                    break;
//            }
//        }
//    }
}