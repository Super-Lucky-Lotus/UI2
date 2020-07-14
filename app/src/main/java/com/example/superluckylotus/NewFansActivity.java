package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_fans);
        back_Btn=(Button)findViewById(R.id.nf_back);
        setListeners();
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        back_Btn.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.nf_back:
                    intent = new Intent(NewFansActivity.this,MainActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }

}