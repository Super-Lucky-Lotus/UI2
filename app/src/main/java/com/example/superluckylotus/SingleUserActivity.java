package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * @version: 2.0
 * @author: 宋佳容
 * @className: NewFansActivity
 * @packageName:com.example.superluckylotus
 * @description: 查看用户界面
 * @data: 2020.07.17 22:10
 **/

public class SingleUserActivity extends AppCompatActivity {

    private Button single_user_back;
    private ImageView su_video1_btn;
    private ImageView su_video2_btn;
    private ImageView su_video3_btn;
    private ImageView su_video4_btn;
    private ImageView su_video5_btn;
    private ImageView su_video6_btn;
    private ImageView su_video7_btn;
    private ImageView su_video8_btn;
    private ImageView su_video9_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_user);
        single_user_back = (Button) findViewById(R.id.single_user_back);
        su_video1_btn = (ImageView) findViewById(R.id.single_user_video1);
        su_video2_btn = (ImageView) findViewById(R.id.single_user_video2);
        su_video3_btn = (ImageView) findViewById(R.id.single_user_video3);
        su_video4_btn = (ImageView) findViewById(R.id.single_user_video4);
        su_video5_btn = (ImageView) findViewById(R.id.single_user_video5);
        su_video6_btn = (ImageView) findViewById(R.id.single_user_video6);
        su_video7_btn = (ImageView) findViewById(R.id.single_user_video7);
        su_video8_btn = (ImageView) findViewById(R.id.single_user_video8);
        su_video9_btn = (ImageView) findViewById(R.id.single_user_video9);
        setListeners();
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        single_user_back.setOnClickListener(onClick);
        su_video1_btn.setOnClickListener(onClick);
        su_video2_btn.setOnClickListener(onClick);
        su_video3_btn.setOnClickListener(onClick);
        su_video4_btn.setOnClickListener(onClick);
        su_video5_btn.setOnClickListener(onClick);
        su_video6_btn.setOnClickListener(onClick);
        su_video7_btn.setOnClickListener(onClick);
        su_video8_btn.setOnClickListener(onClick);
        su_video9_btn.setOnClickListener(onClick);

    }

    class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.single_user_back:
                    finish();
                    break;
                case R.id.single_user_video1:
                case R.id.single_user_video2:
                case R.id.single_user_video3:
                case R.id.single_user_video4:
                case R.id.single_user_video5:
                case R.id.single_user_video6:
                case R.id.single_user_video7:
                case R.id.single_user_video8:
                case R.id.single_user_video9:
                    intent = new Intent(SingleUserActivity.this, SingleVideoActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

}