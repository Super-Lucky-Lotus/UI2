package com.example.superluckylotus;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @version: 1.0
 * @author: 黄诗雯
 * @className: SettingActivity
 * @packageName:com.example.superluckylotus
 * @description: 设置界面
 * @data: 2020.07.11 22:30
 **/
public class SettingActivity extends AppCompatActivity {

    ImageButton mBackBtn;
    LinearLayout mChangename;
    LinearLayout mChangepassword;
    ImageButton mChangephoto;
    LinearLayout mChangeintro;
    LinearLayout mChangebirthday;
    DialogManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mBackBtn=(ImageButton)findViewById(R.id.back);
        mChangeintro=(LinearLayout)findViewById(R.id.changeintro);
        mChangename=(LinearLayout)findViewById(R.id.changename);
        mChangepassword=(LinearLayout)findViewById(R.id.changepassword);
        mChangephoto=(ImageButton) findViewById(R.id.changephoto);
        mChangebirthday=(LinearLayout)findViewById(R.id.changebirthday);
        setListeners();
        dm = new DialogManager(this);
    }

    private void setListeners(){
        SettingActivity.OnClick onClick = new SettingActivity.OnClick();
        mBackBtn.setOnClickListener(onClick);
        mChangeintro.setOnClickListener(onClick);
        mChangephoto.setOnClickListener(onClick);
        mChangename.setOnClickListener(onClick);
        mChangepassword.setOnClickListener(onClick);
        mChangebirthday.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.back:
                    intent = new Intent(SettingActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.changephoto:
                    intent = new Intent(SettingActivity.this,ChangePhotoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.changepassword:
                    intent = new Intent(SettingActivity.this,ChangePasswordActivity.class);
                    startActivity(intent);
                    break;
                case R.id.changename:
                    intent = new Intent(SettingActivity.this,ChangeNameActivity.class);
                    startActivity(intent);
                    break;
                case R.id.changeintro:
                    intent = new Intent(SettingActivity.this,ChangeIntroductionActivity.class);
                    startActivity(intent);
                    break;
                case R.id.changebirthday:
                    DatePickerDialog datePickerDialog=new DatePickerDialog(SettingActivity.this, new DatePickerDialog.OnDateSetListener() {
              @Override
                 public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                             Toast.makeText(SettingActivity.this, "您当前选择日期："+year+"年"+(month+1)+"月"+dayOfMonth+"日", Toast.LENGTH_SHORT).show();
                          }
            }, 2020, 7,14);
                             datePickerDialog.show();//展示日期对话框
                     break;
                default:
                    break;

            }

        }







    }
}