package com.example.superluckylotus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.app.FragmentManager;

import android.app.FragmentTransaction;
import android.os.Bundle;

import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity  {

    private RadioGroup mTabRadioGroup;
    private MeFragment meFragment;
    private EarthFragment earthFragment;
    private NearFragment nearFragment;
    private NoticeFragment noticeFragment;

    private FragmentManager fm;

    private Button earth_Btn;
    private Button near_Btn;
    private Button notice_Btn;
    private Button me_Btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //没有标题栏
        setContentView(R.layout.activity_main);

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

}