package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * @version: 2.0
 * @author: 宋佳容
 * @className: NewFansActivity
 * @packageName:com.example.superluckylotus
 * @description: 收到喜欢列表界面
 * @data: 2020.07.14 16:42
 **/

public class GetLikesActivity extends AppCompatActivity {

    private Button back_Btn;
    private Button getlikes_video1;
    private Button getlikes_video2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_likes);
        back_Btn=(Button) findViewById(R.id.gl_back);
        getlikes_video1=(Button) findViewById(R.id.likesVedio1_btn);
        getlikes_video2=(Button) findViewById(R.id.likesVedio2_btn);
        setListeners();
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        back_Btn.setOnClickListener(onClick);
        getlikes_video1.setOnClickListener(onClick);
        getlikes_video2.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.gl_back:
                    finish();
                    break;
                case R.id.likesVedio1_btn:
                case R.id.likesVedio2_btn:
                    intent = new Intent(GetLikesActivity.this, SingleVideoActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}