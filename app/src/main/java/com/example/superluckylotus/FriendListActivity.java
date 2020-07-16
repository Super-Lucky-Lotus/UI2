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
 * @description: 好友列表
 * @data: 2020.07.16 16:18
 **/

public class FriendListActivity extends AppCompatActivity {

    private Button fri_list_back_btn;
    private TextView go_find_fri_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        fri_list_back_btn = (Button) findViewById(R.id.fri_num_back);
        go_find_fri_btn = (TextView) findViewById(R.id.find_fri_btn);

        setListeners();
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        fri_list_back_btn.setOnClickListener(onClick);
        go_find_fri_btn.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.fri_num_back:
                    intent = new Intent(FriendListActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.find_fri_btn:
                    intent = new Intent(FriendListActivity.this, FindFriendActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}