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

    private static final String[] FRAGMENT_TAG = {"earth_tab","near_tab", "notice_tab","me_tab"};

    private final int show_tab_earth = 0;//附近
    private final int show_tab_near = 1;//广场
    private final int show_tab_notice = 2;//消息
    private final int show_tab_me = 3;//我的
    private int mrIndex = show_tab_earth;//默认选中地图

    private static final String PRV_SELINDEX = "PREV_SELINDEX";
    private int index = -100;// 记录当前的选项
    private FragmentManager fm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //没有标题栏
        setContentView(R.layout.activity_main);
        //防止app闪退后fragment重叠
        if (savedInstanceState != null) {
            //读取上一次界面Save的时候tab选中的状态
            mrIndex = savedInstanceState.getInt(PRV_SELINDEX, mrIndex);
            earthFragment = (EarthFragment) fm.findFragmentByTag(FRAGMENT_TAG[0]);
            nearFragment = (NearFragment) fm.findFragmentByTag(FRAGMENT_TAG[1]);
            noticeFragment = (NoticeFragment) fm.findFragmentByTag(FRAGMENT_TAG[2]);
            meFragment = (MeFragment) fm.findFragmentByTag(FRAGMENT_TAG[3]);

            //初始化
            initView();
        }

    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(PRV_SELINDEX, mrIndex);
        super.onSaveInstanceState(outState);
    }

    private void initView() {
        //获得RadioGroup控件
        mTabRadioGroup=findViewById(R.id.tabs_rg);
        setTabSelection(show_tab_earth);//初始页面为广场
        mTabRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.earth_tab:
                        setTabSelection(show_tab_earth);
                        break;
                    case R.id.near_tab:
                        setTabSelection(show_tab_near);
                        break;
                    case R.id.notice_tab:
                        setTabSelection(show_tab_notice);
                        break;
                    case R.id.me_tab:
                        setTabSelection(show_tab_me);
                        break;
                    default:
                        break;
                }
            }
        });
    }

//根据传入的index参数来设置选中tab页
    private void setTabSelection(int id){
        if(id==index){
            return;
        }
        index=id;
        FragmentTransaction transaction=fm.beginTransaction();
        hideFragments(transaction);
        switch(index){
            case 0:
                mTabRadioGroup.check(R.id.earth_tab);
                if(earthFragment==null){
                    earthFragment=new EarthFragment();
                    transaction.add(R.id.fragment_container,earthFragment,FRAGMENT_TAG[index]);
                }else{
                    transaction.show(earthFragment);
                }
                transaction.commit();
                break;
            case 1:
                mTabRadioGroup.check(R.id.near_tab);
                if(nearFragment==null){
                    nearFragment=new NearFragment();
                    transaction.add(R.id.fragment_container,nearFragment,FRAGMENT_TAG[index]);
                }else{
                    transaction.show(nearFragment);
                }
                transaction.commit();
                break;
            case 2:
                mTabRadioGroup.check(R.id.notice_tab);
                if(noticeFragment==null){
                    noticeFragment=new NoticeFragment();
                    transaction.add(R.id.fragment_container,noticeFragment,FRAGMENT_TAG[index]);
                }else{
                    transaction.show(noticeFragment);
                }
                transaction.commit();
                break;
            case 3:
                mTabRadioGroup.check(R.id.me_tab);
                if(meFragment==null){
                    meFragment=new MeFragment();
                    transaction.add(R.id.fragment_container,meFragment,FRAGMENT_TAG[index]);
                }else{
                    transaction.show(meFragment);
                }
                transaction.commit();
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