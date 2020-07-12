package com.example.superluckylotus;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

/**
 * @version: 1.0
 * @author: 宋佳容
 * @className: LoginActivity
 * @packageName:com.example.superluckylotus
 * @description: 登录界面
 * @data: 2020.07.12 01:18
 **/
public class LoginActivity extends AppCompatActivity {

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
        setListeners();
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        login_Btn.setOnClickListener(onClick);
        reg_Btn.setOnClickListener(onClick);
        skip_Btn.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{
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