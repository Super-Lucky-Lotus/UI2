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
    private NearFragment nearTabPage=new NearFragment();

    private FragmentManager fm;

    private LinearLayout mEarthTabButton;
    private LinearLayout mNoticeTabButton;
    private LinearLayout mMyTabButton;
    private LinearLayout mNearTabButton;

    private ImageButton mEarthImageButton;
    private ImageButton mNoticeImageButton;
    private ImageButton mMyImageButton;
    private ImageButton mNearImageButton;

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
        mNearTabButton.setOnClickListener(this);
    }

    private void initView() {
        mEarthImageButton=(ImageButton)findViewById(R.id.id_imgButton_earth);
        mNoticeImageButton=(ImageButton)findViewById(R.id.id_imgButton_notice);
        mMyImageButton=(ImageButton)findViewById(R.id.id_imgButton_my);
        mNearImageButton=(ImageButton)findViewById(R.id.id_imgButton_near);

        mEarthTabButton=(LinearLayout)findViewById(R.id.id_tabButton_earth);
        mNoticeTabButton=(LinearLayout)findViewById(R.id.id_tabButton_notice);
        mMyTabButton = (LinearLayout) findViewById(R.id.id_tabButton_my);
        mNearTabButton=(LinearLayout)findViewById(R.id.id_tabButton_near);

    }

    private void initFragment() {
        fm=getFragmentManager();
        FragmentTransaction transaction=fm.beginTransaction();
        transaction.add(R.id.id_content,earthTabPage).addToBackStack(null).commitAllowingStateLoss();
        transaction.add(R.id.id_content,nearTabPage).addToBackStack(null).commitAllowingStateLoss();
        transaction.add(R.id.id_content,noticeTabPage).addToBackStack(null).commitAllowingStateLoss();
        transaction.add(R.id.id_content,meTabPage).addToBackStack(null).commitAllowingStateLoss();
        transaction.commit();
    }

    public  void  resetImg() {
        mEarthImageButton.setImageResource(R.drawable.earth);
        mNearImageButton.setImageResource(R.drawable.near);
        mNoticeImageButton.setImageResource(R.drawable.notice);
        mMyImageButton.setImageResource(R.drawable.mine);

    }

    private  void hideFragment(FragmentTransaction transaction) {
        transaction.hide(earthTabPage);
        transaction.hide(nearTabPage);
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
                transaction.show(nearTabPage);
                mEarthImageButton.setImageResource(R.drawable.near_clicked);
                break;
            case 2:
                transaction.show(noticeTabPage);
                mNoticeImageButton.setImageResource(R.drawable.notice_clicked);
                break;
            case 3:
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
            case R.id.id_tabButton_near:
                selectFragment(2);
                break;
            case R.id.id_tabButton_notice:
                selectFragment(2);
                break;
            case R.id.id_tabButton_my:
                selectFragment(3);
                break;
            default:
                break;
        }
    }
}