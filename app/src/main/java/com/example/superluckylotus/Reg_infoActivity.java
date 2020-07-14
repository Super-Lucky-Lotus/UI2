package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

public class Reg_infoActivity extends AppCompatActivity {

    Button mEnter;
    Button mBacktoReg;
    Button mBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_info);
        mBacktoReg=findViewById(R.id.backtoreg);
        mEnter=findViewById(R.id.btn_enter);
        mBirthday=findViewById(R.id.btn_birthday);
        setListeners();
    }

    private void setListeners() {
        Reg_infoActivity.OnClick onClick = new OnClick();
        mBacktoReg.setOnClickListener(onClick);
        mEnter.setOnClickListener(onClick);
        mBirthday.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.btn_enter:
                    intent = new Intent(Reg_infoActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.backtoreg:
                    intent = new Intent(Reg_infoActivity.this,RegisterActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_birthday:
                    DatePickerDialog datePickerDialog=new DatePickerDialog(Reg_infoActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Toast.makeText(Reg_infoActivity.this, "您当前选择日期："+year+"年"+(month+1)+"月"+dayOfMonth+"日", Toast.LENGTH_SHORT).show();
                        }
                    }, 2020, 7,14);
                    datePickerDialog.show();//展示日期对话框
                    break;
            }

        }
    }
}