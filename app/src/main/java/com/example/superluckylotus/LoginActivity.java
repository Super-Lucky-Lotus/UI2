package com.example.superluckylotus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @version: 1.0
 * @author: 宋佳容
 * @className: LoginActivity
 * @packageName:com.example.superluckylotus
 * @description: 登录界面
 * @data: 2020.07.12 01:18
 **/
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private Button login_Btn;
    private Button reg_Btn;
    private Button skip_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_Btn = (Button)findViewById(R.id.btn_login);
        reg_Btn = (Button)findViewById(R.id.btn_reg);
        skip_Btn = (Button)findViewById(R.id.btn_skip);
        setListeners(this);
    }

    private void setListeners(Context context){
        OnClick onClick = new OnClick(context);
        login_Btn.setOnClickListener(onClick);
        reg_Btn.setOnClickListener(onClick);
        skip_Btn.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{

        private String  username;
        private String  password;

        public OnClick(Context context) {
            Activity act=(Activity)context;
            EditText editText1 = (EditText) findViewById(R.id.input_name);
            username = editText1.getText().toString();
            EditText editText2 = (EditText) findViewById(R.id.input_psw);
            password = editText2.getText().toString();
        }


        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.btn_login:
                    intent = new Intent(LoginActivity.this,MainActivity.class);
                    break;
                case R.id.btn_reg://MainActivity要改成reg的act
                    intent = new Intent(LoginActivity.this,RegisterActivity.class);
                    break;
                case R.id.btn_skip:
                    intent = new Intent(LoginActivity.this,MainActivity.class);
                    break;
            }
            startActivity(intent);

        }
    }


}