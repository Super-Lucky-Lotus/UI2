package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * @version: 2.0
 * @author: 黄诗雯
 * @className: ChangePasswordActivity
 * @packageName:com.example.superluckylotus
 * @description: 更改简介界面
 * @data: 2020.07.14 16:38
 **/
public class ChangePasswordActivity extends AppCompatActivity {

    ImageButton mbacktosetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mbacktosetting=(ImageButton)findViewById(R.id.backtosettting);
        setListeners();
    }
    private void setListeners(){
        ChangePasswordActivity.OnClick onClick = new ChangePasswordActivity.OnClick();
        mbacktosetting.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.backtosettting:
                    intent = new Intent(ChangePasswordActivity.this,SettingActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }
}