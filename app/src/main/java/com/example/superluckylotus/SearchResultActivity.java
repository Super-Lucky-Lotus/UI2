package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @version: 2.0
 * @author: 宋佳容
 * @className: SearchResultActivity
 * @packageName:com.example.superluckylotus
 * @description: 搜索结果界面
 * @data: 2020.07.15 20：10
 **/

public class SearchResultActivity extends AppCompatActivity {

    private Button cek_search_btn;
    private Button result_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        cek_search_btn=(Button)findViewById(R.id.check_search_btn);
        result_back_btn=(Button)findViewById(R.id.search_result_back);

        setListeners();
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        cek_search_btn.setOnClickListener(onClick);
        result_back_btn.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.check_search_btn:
                    intent = new Intent(SearchResultActivity.this,SearchResultActivity.class);
                    startActivity(intent);
                    break;
                case R.id.search_result_back:
                    intent = new Intent(SearchResultActivity.this,SearchActivity.class);
                    startActivity(intent);
                    break;
            }

        }



    }

}