package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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
    LinearLayout mChangename;
    LinearLayout mChangepassword;
    ImageButton mChangephoto;
    LinearLayout mChangeintro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mBackBtn=(ImageButton)findViewById(R.id.back);
        mChangeintro=(LinearLayout)findViewById(R.id.changeintro);
        mChangename=(LinearLayout)findViewById(R.id.changename);
        mChangepassword=(LinearLayout)findViewById(R.id.changepassword);
        mChangephoto=(ImageButton) findViewById(R.id.changephoto);
        setListeners();
    }

    private void setListeners(){
        SettingActivity.OnClick onClick = new SettingActivity.OnClick();
        mBackBtn.setOnClickListener(onClick);
        mChangeintro.setOnClickListener(onClick);
        mChangephoto.setOnClickListener(onClick);
        mChangename.setOnClickListener(onClick);
        mChangepassword.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.back:
                    intent = new Intent(SettingActivity.this,MainActivity.class);
                    break;
                case R.id.changephoto:
                    intent = new Intent(SettingActivity.this,ChangePhotoActivity.class);
                    break;
                case R.id.changepassword:
                    intent = new Intent(SettingActivity.this,ChangePasswordActivity.class);
                    break;
                case R.id.changename:
                    intent = new Intent(SettingActivity.this,ChangeNameActivity.class);
                    break;
                case R.id.changeintro:
                    intent = new Intent(SettingActivity.this,ChangeIntroductionActivity.class);
                    break;

            }
            startActivity(intent);
        }
     }
}