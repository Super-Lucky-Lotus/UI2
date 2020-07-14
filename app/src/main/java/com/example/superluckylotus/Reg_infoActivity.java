package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Reg_infoActivity extends AppCompatActivity {

    Button mEnter;
    Button mBacktoReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_info);
        mBacktoReg=findViewById(R.id.backtoreg);
        mEnter=findViewById(R.id.btn_enter);
        setListeners();
    }

    private void setListeners() {
        Reg_infoActivity.OnClick onClick = new OnClick();
        mBacktoReg.setOnClickListener(onClick);
        mEnter.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.btn_enter:
                    intent = new Intent(Reg_infoActivity.this,MainActivity.class);
                    break;
                case R.id.backtoreg:
                    intent = new Intent(Reg_infoActivity.this,RegisterActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }
}