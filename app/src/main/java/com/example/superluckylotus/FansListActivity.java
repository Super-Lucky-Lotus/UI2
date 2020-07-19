package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @version: 2.0
 * @author: 宋佳容
 * @className: NewFansActivity
 * @packageName:com.example.superluckylotus
 * @description: 粉丝列表界面
 * @data: 2020.07.17 18:15
 **/

public class FansListActivity extends AppCompatActivity {

    private Button fans_list_back_btn;
    private Button fan_search_btn;
    private EditText search_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans_list);
        fans_list_back_btn = (Button) findViewById(R.id.fans_list_back);
        fan_search_btn= (Button) findViewById(R.id.FansL_search_btn);
        search_name = (EditText) findViewById(R.id.FansL_search_edittext);
        setListeners();
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        fans_list_back_btn.setOnClickListener(onClick);
        fan_search_btn.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.fans_list_back:
                    finish();
                    break;
                case R.id.FansL_search_btn:
                    break;
            }
        }
    }

}