package com.example.superluckylotus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * @version: 1.0
 * @author: 宋佳容
 * @className: ShootActivity
 * @packageName:com.example.superluckylotus
 * @description: 拍摄界面
 * @data: 2020.07.12 00:48
 **/
public class ShootActivity extends AppCompatActivity {

    private Button back_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoot);
        back_Btn = (Button)findViewById(R.id.btn_back);
        setListeners();
    }
    private void setListeners(){
        OnClick onClick = new OnClick();
        back_Btn.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.btn_back:
                    intent = new Intent(ShootActivity.this,MainActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }

}