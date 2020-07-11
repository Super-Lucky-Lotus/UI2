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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //没有标题栏
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        //获得RadioGroup控件
        mTabRadioGroup=findViewById(R.id.tabs_rg);
        setTabSelection(0);//初始页面为广场
        mTabRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.earth_tab:
                        setTabSelection(0);
                        break;
                    case R.id.near_tab:
                        setTabSelection(1);
                        break;
                    case R.id.notice_tab:
                        setTabSelection(2);
                        break;
                    case R.id.my_tab:
                        setTabSelection(3);
                        break;
                    default:
                        break;
                }
            }
        });
    }

//根据传入的index参数来设置选中tab页
    private void setTabSelection(int id){
        FragmentTransaction transaction=fm.beginTransaction();
        hideFragments(transaction);
        switch(id){
            case 0:
                mTabRadioGroup.check(R.id.earth_tab);
                if(earthFragment==null){
                    earthFragment=new EarthFragment();
                    transaction.add(R.id.fragment_container,earthFragment).commitAllowingStateLoss();
                }else{
                    transaction.show(earthFragment);
                }
                break;
            case 1:
                mTabRadioGroup.check(R.id.near_tab);
                if(nearFragment==null){
                    nearFragment=new NearFragment();
                    transaction.add(R.id.fragment_container,nearFragment).commitAllowingStateLoss();
                }else{
                    transaction.show(nearFragment);
                }
                break;
            case 2:
                mTabRadioGroup.check(R.id.notice_tab);
                if(noticeFragment==null){
                    noticeFragment=new NoticeFragment();
                    transaction.add(R.id.fragment_container,noticeFragment).commitAllowingStateLoss();
                }else{
                    transaction.show(noticeFragment);
                }
                break;
            case 3:
                mTabRadioGroup.check(R.id.my_tab);
                if(meFragment==null){
                    meFragment=new MeFragment();
                    transaction.add(R.id.fragment_container,meFragment).commitAllowingStateLoss();
                }else{
                    transaction.show(meFragment);
                }
                break;
        }
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (earthFragment != null) {
            transaction.hide(earthFragment);
        }
        if (nearFragment != null) {
            transaction.hide(nearFragment);
        }
        if (noticeFragment != null) {
            transaction.hide(noticeFragment);
        }
        if (meFragment != null) {
            transaction.hide(noticeFragment);
        }

    }


}