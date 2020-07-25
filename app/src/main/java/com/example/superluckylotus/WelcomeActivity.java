package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Timer;
import  android.content.Intent;

import com.example.superluckylotus.Register.LoginActivity;

import java.util.TimerTask;

/**
 * @version: 1.0
 * @author: 黄诗雯
 * @className: WelcomeActivity
 * @packageName:com.example.superluckylotus
 * @description: 欢迎界面
 * @data: 2020.07.10 14:20
 **/
public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{

    private int reclen=5;//跳过倒计时提示5s
    private TextView tv;
    Timer timer=new Timer();
    private Handler handler;
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //定义全屏参数
        int flag=WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗口为全屏显示
        getWindow().setFlags(flag,flag);
        setContentView(R.layout.activity_welcome);
        initView();
        timer.schedule(task,1000,1000);//等待时间1s，停顿时间1s
        /**
         * 正常情况下不点点击跳过
         */
        handler=new Handler();
        handler.postDelayed(runnable=new Runnable() {
            @Override
            public void run() {
                //从闪屏界面跳转到首界面
                Intent intent=new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },5000);//延迟5s后发送handler信息

    }

    private void initView() {
        tv=findViewById(R.id.tv);//跳过
        tv.setOnClickListener(this);//跳过监听
    }
    TimerTask task =new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    reclen--;
                    tv.setText("跳过"+reclen);
                    if(reclen<0){
                        timer.cancel();
                        tv.setVisibility(View.GONE);
                    }
                }
            });
        }
    };

    /**
     * 点击跳过
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv:
                //从闪屏界面跳转到首界面
                Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                if(runnable!=null){
                    handler.removeCallbacks(runnable);
                }
                break;
            default:
                break;
        }

    }

}