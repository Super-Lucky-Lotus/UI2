package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * @version: 2.0
 * @author: 宋佳容
 * @className: NewFansActivity
 * @packageName:com.example.superluckylotus
 * @description: 新增粉丝界面
 * @data: 2020.07.14 16:35
 **/

public class NewFansActivity extends AppCompatActivity {

    private Button back_Btn;
    private Button fans_btn1;
    private Button fans_btn2;
    private Button fans_btn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_fans);
        back_Btn=(Button)findViewById(R.id.nf_back);
        fans_btn1=(Button)findViewById(R.id.fansUser_pic);
        fans_btn2=(Button)findViewById(R.id.fansUser_pic1);
        fans_btn3=(Button)findViewById(R.id.fansUser_pic2);
        setListeners();
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        back_Btn.setOnClickListener(onClick);
        fans_btn1.setOnClickListener(onClick);
        fans_btn2.setOnClickListener(onClick);
        fans_btn3.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.nf_back:
                    finish();
                    break;
                case R.id.fansUser_pic:
                case R.id.fansUser_pic1:
                case R.id.fansUser_pic2:
                    intent = new Intent(NewFansActivity.this,SingleUserActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}