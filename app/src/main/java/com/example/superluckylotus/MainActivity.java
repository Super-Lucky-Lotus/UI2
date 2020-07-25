package com.example.superluckylotus;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClientOption;


import com.example.superluckylotus.Earth.EarthFragment;
import com.example.superluckylotus.Me.MeFragment;
import com.example.superluckylotus.Near.NearFragment;
import com.example.superluckylotus.Notice.NoticeFragment;
import com.example.superluckylotus.ShootSdk.ShootActivity;

import static com.veuisdk.SdkEntry.editMedia;

/**
 * @version: 1.0
 * @author: 宋佳容
 * @className: MainFragment
 * @packageName:com.example.superluckylotus
 * @description: 主界面
 * @data: 2020.07.11 21:12
 **/

public class MainActivity extends AppCompatActivity  {

    public static String city;
    private RadioGroup mTabRadioGroup;
    private MeFragment meFragment;
    private EarthFragment earthFragment;
    private NearFragment nearFragment;
    private NoticeFragment noticeFragment;
    private final String TAG = "MainActivity";
//    private ShootDialog sd;


    private Button near_Btn;
    private Button notice_Btn;
    private Button me_Btn;
    private Button shoot_Btn;
    private Button earth_Btn;



    //声明LocationClient类，定位服务客户对象
    public LocationClient mLocationClient = null;
    //声明重写的监听类
    private MyLocationListener mLocationListener = new MyLocationListener();
    //是否首次定位
    private boolean isFirstLoc = true;




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //没有标题栏
        setContentView(R.layout.activity_main);
        shoot_Btn = (Button)findViewById(R.id.btn_addshoot);
        //setListeners();

//        sd=new ShootDialog(MainActivity.this);

//        sd=new ShootDialog(MainActivity.this,new ShootDialog.LeaveMyDialogListener(){
//
//            @Override
//            public void onClick(View view) {
//                switch (view.getId()) {
//
//                    case R.id.add_shoot: // 正方形，长方形可切换录制视频，编辑录制后的视频，如果有导出时，导出视频的路径
//                        SdkEntry.registerOSDBuilder(CameraWatermarkBuilder.class);
//                        CameraWatermarkBuilder.setText("好运莲莲");// 可自定义水印显示文本
//                        initCameraConfig(SQUARE_SCREEN_CAN_CHANGE);
//                        SdkEntry.record(view.getContext(), CAMERA_REQUEST_CODE);
//                        break;
//
//                }
//            }
//
//        });

        shoot_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sd.popupWindowDialog(view);
                Intent intent = null;
                intent = new Intent(MainActivity.this, ShootActivity.class);
                startActivity(intent);
//                mContext.startActivity(intent);
            }
        });

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

     //   mLocationClient.start();
        initLocation();
        mLocationClient.start();

    }



    private void initLocation(){
        mLocationClient = new LocationClient((getApplicationContext()));
        mLocationListener = new MyLocationListener();

        //注册监听器
        mLocationClient.registerLocationListener(mLocationListener);

        //设置定位参数
        LocationClientOption option = new LocationClientOption();
        //设置坐标类型
        option.setCoorType("bd09ll");
        //设置是否需要地址信息，默认为无地址
        option.setIsNeedAddress(true);
        //设置是否打开gps进行定位
        option.setOpenGps(true);
        //设置扫描间隔为0
        option.setScanSpan(0);
        //传入设置好的信息
        mLocationClient.setLocOption(option);
    }



    //重写百度地图监听类
    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location){
            Toast.makeText(getApplicationContext(),"定位成功",Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),String.valueOf(location.getCity()),Toast.LENGTH_SHORT).show();
            city=String.valueOf(location.getCity());
        }
    }


}