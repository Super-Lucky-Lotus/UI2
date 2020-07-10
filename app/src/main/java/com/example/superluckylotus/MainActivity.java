package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends Activity implements View.OnClickListener {

    private EarthFragment earthTabPage=new EarthFragment();
    private NoticeFragment noticeTabPage=new NoticeFragment();
    private MeFragment meTabPage=new MeFragment();

    private FragmentManager fm;

    private LinearLayout mEarthTabButton;
    private LinearLayout mNoticeTabButton;
    private LinearLayout mMyTabButton;

    private ImageButton mEarthImageButton;
    private ImageButton mNoticeImageButton;
    private ImageButton mMyImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //没有标题栏
        setContentView(R.layout.activity_main);
        initView();
        initFragment();
        selectFragment(0);
        initEvent();
    }
    private void initEvent() {
        mMyTabButton.setOnClickListener(this);
        mEarthTabButton.setOnClickListener(this);
        mNoticeTabButton.setOnClickListener(this);
    }

    private void initView() {
        mEarthImageButton=(ImageButton)findViewById(R.id.id_imgButton_earth);
        mNoticeImageButton=(ImageButton)findViewById(R.id.id_imgButton_notice);
        mMyImageButton=(ImageButton)findViewById(R.id.id_imgButton_my);

        mEarthTabButton=(LinearLayout)findViewById(R.id.id_tabButton_earth);
        mNoticeTabButton=(LinearLayout)findViewById(R.id.id_tabButton_notice);
        mMyTabButton = (LinearLayout) findViewById(R.id.id_tabButton_my);

    }

    private void initFragment() {
        fm=getFragmentManager();
        FragmentTransaction transaction=fm.beginTransaction();
        transaction.add(R.id.id_content,earthTabPage).addToBackStack(null).commitAllowingStateLoss();
        transaction.add(R.id.id_content,noticeTabPage).addToBackStack(null).commitAllowingStateLoss();
        transaction.add(R.id.id_content,meTabPage).addToBackStack(null).commitAllowingStateLoss();
        transaction.commit();
    }

    public  void  resetImg() {
        mEarthImageButton.setImageResource(R.drawable.earth);
        mNoticeImageButton.setImageResource(R.drawable.notice);
        mMyImageButton.setImageResource(R.drawable.mine);

    }

    private  void hideFragment(FragmentTransaction transaction) {
        transaction.hide(earthTabPage);
        transaction.hide(meTabPage);
        transaction.hide(noticeTabPage);

    }

    private void selectFragment(int i){
        FragmentTransaction transaction=fm.beginTransaction();
        hideFragment(transaction);

        switch(i){
            case 0:
                transaction.show(earthTabPage);
                mEarthImageButton.setImageResource(R.drawable.earth_clicked);
                break;
            case 1:
                transaction.show(noticeTabPage);
                mNoticeImageButton.setImageResource(R.drawable.notice_clicked);
                break;
            case 2:
                transaction.show(meTabPage);
                mMyImageButton.setImageResource(R.drawable.my_clicked);
            default:
                break;

        }
        transaction.commit();

    }


    @Override
    public void onClick(View view) {
        resetImg();
        switch (view.getId()){
            case R.id.id_tabButton_earth:
                selectFragment(0);
                break;
            case R.id.id_tabButton_notice:
                selectFragment(1);
                break;
            case R.id.id_tabButton_my:
                selectFragment(2);
                break;
            default:
                break;
        }
    }
}