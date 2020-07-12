package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * @version: 1.0
 * @author: 黄诗雯
 * @className: SettingActivity
 * @packageName:com.example.superluckylotus
 * @description: 设置界面
 * @data: 2020.07.11 22:30
 **/
public class SettingActivity extends AppCompatActivity {

    ImageButton mBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mBackBtn=(ImageButton)findViewById(R.id.back);
        setListeners();
    }

    private void setListeners(){
        SettingActivity.OnClick onClick = new SettingActivity.OnClick();
        mBackBtn.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.back:
                    intent = new Intent(SettingActivity.this,MainActivity.class);
                    break;
            }
            startActivity(intent);
        }
}}