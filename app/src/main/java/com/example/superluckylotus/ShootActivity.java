package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * @version: 1.0
 * @author: 黄诗雯、宋佳容
 * @className: ShootActivity
 * @packageName:com.example.superluckylotus
 * @description: 拍摄界面
 * @data: 2020.07.12 00:48
 **/
public class ShootActivity extends AppCompatActivity {

    private Button back_Btn;
    private ImageButton mVedio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoot);
        back_Btn = (Button)findViewById(R.id.btn_back);
        mVedio=findViewById(R.id.btn_shoot);
        setListeners();
    }
    private void setListeners(){
        OnClick onClick = new OnClick();
        back_Btn.setOnClickListener(onClick);
        mVedio.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.btn_back:
                    intent = new Intent(ShootActivity.this,MainActivity.class);
                    break;
                case R.id.btn_shoot:
                    intent = new Intent(ShootActivity.this,VideoCutActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }

}