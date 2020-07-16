package com.example.superluckylotus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.app.FragmentManager;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

/**
 * @version: 1.0
 * @author: 宋佳容
 * @className: MainFragment
 * @packageName:com.example.superluckylotus
 * @description: 主界面
 * @data: 2020.07.11 21:12
 **/

public class MainActivity extends AppCompatActivity  {

    private RadioGroup mTabRadioGroup;
    private MeFragment meFragment;
    private EarthFragment earthFragment;
    private NearFragment nearFragment;
    private NoticeFragment noticeFragment;

    private FragmentManager fm;


    private Button near_Btn;
    private Button notice_Btn;
    private Button me_Btn;
    private Button shoot_Btn;
    private Button earth_Btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //没有标题栏
        setContentView(R.layout.activity_main);
        shoot_Btn = (Button)findViewById(R.id.btn_shoot);
        setListeners();

        //实例化EarthFragment
        earthFragment = new EarthFragment();
        //把EarthFragment添加到Avtivity中
        getFragmentManager().beginTransaction().add(R.id.fragment_container,earthFragment).commitAllowingStateLoss();

        earth_Btn = (Button)findViewById(R.id.earth_tab);
        earth_Btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(earthFragment == null)
                {
                    earthFragment = new EarthFragment();
                }
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,earthFragment).commitAllowingStateLoss();
            }
        });

        near_Btn = (Button)findViewById(R.id.near_tab);
        near_Btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(nearFragment == null)
                {
                    nearFragment = new NearFragment();
                }
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,nearFragment).commitAllowingStateLoss();
            }
        });

        notice_Btn = (Button)findViewById(R.id.notice_tab);
        notice_Btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(noticeFragment == null)
                {
                    noticeFragment = new NoticeFragment();
                }
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,noticeFragment).commitAllowingStateLoss();
            }
        });

        me_Btn = (Button)findViewById(R.id.me_tab);
        me_Btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(meFragment == null)
                {
                    meFragment = new MeFragment();
                }
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,meFragment).commitAllowingStateLoss();
            }
        });


    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        shoot_Btn.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.btn_shoot:
                    intent = new Intent(MainActivity.this,ShootActivity.class);
                    startActivity(intent);
                    break;
            }

        }
    }
}