package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FansListActivity extends AppCompatActivity {

    private Button fans_list_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans_list);
        fans_list_back_btn = (Button) findViewById(R.id.fans_list_back);
        setListeners();
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        fans_list_back_btn.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.fans_list_back:
                    finish();
                    break;
            }
        }
    }

}