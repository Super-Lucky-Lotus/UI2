package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @version: 2.0
 * @author: 宋佳容
 * @className: NewFansActivity
 * @packageName:com.example.superluckylotus
 * @description: 关注列表界面
 * @data: 2020.07.17 18:00
 **/

public class FollowListActivity extends AppCompatActivity {

    private Button follow_list_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);
        follow_list_back_btn= (Button) findViewById(R.id.follow_list_back);
        setListeners();
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        follow_list_back_btn.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.follow_list_back:
                    finish();
                    break;
            }
        }
    }
}