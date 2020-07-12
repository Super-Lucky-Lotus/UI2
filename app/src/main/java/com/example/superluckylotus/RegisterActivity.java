package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @version: 1.0
 * @author: 黄诗雯
 * @className: ResgisterActivity
 * @packageName:com.example.superluckylotus
 * @description: 注册界面
 * @data: 2020.07.12 02:13
 **/
public class RegisterActivity extends AppCompatActivity {

    Button mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mRegister=(Button)findViewById(R.id.btn_register);
        setListeners();

    }
    private void setListeners(){
        RegisterActivity.OnClick onClick = new RegisterActivity.OnClick();
        mRegister.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.
                        btn_register:
                    intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }
}