package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

/**
 * @version: 2.0
 * @author: 宋佳容
 * @className: SearchActivity
 * @packageName:com.example.superluckylotus
 * @description: 搜索界面
 * @data: 2020.07.15 18：00
 **/

public class SearchActivity extends AppCompatActivity {

    private Button search_btn;
    private Button search_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_btn=(Button)findViewById(R.id.search_btn);
        search_back_btn=(Button)findViewById(R.id.search_back);

        setListeners();
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        search_btn.setOnClickListener(onClick);
        search_back_btn.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.search_btn:
                    intent = new Intent(SearchActivity.this,SearchResultActivity.class);
                    startActivity(intent);
                    break;
                case R.id.search_back:
                    intent = new Intent(SearchActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;
            }

        }



    }

}