package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @version: 2.0
 * @author: 宋佳容
 * @className: FriendListActivity
 * @packageName:com.example.superluckylotus
 * @description: 寻找用户&添加好友界面
 * @data: 2020.07.16 16:22
 **/

public class FindFriendActivity extends AppCompatActivity {

    private Button find_fri_back_btn;
    private TextView go_fri_list_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);
        find_fri_back_btn = (Button) findViewById(R.id.find_fri_back);
        go_fri_list_btn = (TextView) findViewById(R.id.fri_list_btn);
        setListeners();
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        find_fri_back_btn.setOnClickListener(onClick);
        go_fri_list_btn.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.find_fri_back:
                    finish();
                    break;
                case R.id.fri_list_btn:
                    intent = new Intent(FindFriendActivity.this, FriendListActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

}