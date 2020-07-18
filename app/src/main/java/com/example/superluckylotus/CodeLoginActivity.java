package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

/**
 * @version: 2.0
 * @author: 黄坤
 * @className: LoginActivity
 * @packageName:com.example.superluckylotus
 * @description: 实现验证码获取、验证、登录功能
 * @data: 2020.07.18 23:18
 **/

public class CodeLoginActivity extends AppCompatActivity {

    private String phone_number;
    private String code_number;
    private EditText editTextPhone;
    private EditText editTextCode;
    Button mGetCode;
    private static final String TAG = "RegisterActivity";
    EventHandler eventHandler;
    private boolean flag=true;
    private TimeCountUtil mTimeCountUtil;
    Button mBacktoLogin;
    Button mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_login);
        mGetCode=(Button)findViewById(R.id.btn_getcode);
        mBacktoLogin=findViewById(R.id.backtologin2);
        mLogin=findViewById(R.id.btn_login2);
        setListeners(this);

        mTimeCountUtil = new TimeCountUtil(mGetCode, 60000, 1000);

        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                Message msg=new Message();
                msg.arg1=event;
                msg.arg2=result;
                msg.obj=data;
                handler.sendMessage(msg);
            }
        };

        SMSSDK.registerEventHandler(eventHandler);
    }

    private void setListeners(Context context){
        CodeLoginActivity.OnClick onClick = new CodeLoginActivity.OnClick();
        mLogin.setOnClickListener(onClick);
        mBacktoLogin.setOnClickListener(onClick);
        mGetCode.setOnClickListener(onClick);
    }

    /**
     * 使用Handler来分发Message对象到主线程中，处理事件
     */
    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event=msg.arg1;
            int result=msg.arg2;
            Object data=msg.obj;

            if(result==SMSSDK.RESULT_COMPLETE)
            {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    Toast.makeText(getApplicationContext(), "验证码输入正确",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CodeLoginActivity.this,MainActivity.class);
                    Phone phoneObj;
                    phoneObj = ((Phone)getApplicationContext());
                    phoneObj.setPhone(phone_number);
                    startActivity(intent);
                }
            }
            else
            {
                if(flag)
                {
                    Toast.makeText(getApplicationContext(),"验证码获取失败请重新获取", Toast.LENGTH_LONG).show();
                    editTextPhone.requestFocus();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"验证码输入错误", Toast.LENGTH_LONG).show();
                    editTextPhone.requestFocus();
                }
            }
        }

    };

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            editTextPhone = (EditText) findViewById(R.id.editTextPhone2);
            phone_number = editTextPhone.getText().toString();
            editTextCode = (EditText) findViewById(R.id.et_number2);
            code_number = editTextCode.getText().toString();
            Intent intent = null;
            switch (v.getId()){
                case R.id.btn_getcode:
                    mTimeCountUtil.start();
                    if(judPhone())//去掉左右空格获取字符串
                    {
                        SMSSDK.getVerificationCode("86",phone_number);
                        editTextCode.requestFocus();
                    }
                    break;
                case R.id.backtologin2:
                    intent = new Intent(CodeLoginActivity.this,LoginActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_login2://MainActivity要改成reg的act
                    if(judCord())
                        SMSSDK.submitVerificationCode("86",phone_number,code_number);
                    flag=false;
                    break;

            }
        }
    }

    //实现倒计时功能
    public class TimeCountUtil extends CountDownTimer {
        private Button mButton;

        public TimeCountUtil(Button button, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.mButton = button;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // 按钮不可用
            mButton.setEnabled(false);
            mButton.setBackgroundColor(Color.parseColor("#dddddd"));
            String showText = millisUntilFinished / 1000 + "秒后可重新发送";
            mButton.setText(showText);
        }

        @Override
        public void onFinish() {
            // 按钮设置可用
            mButton.setEnabled(true);
            mButton.setText("重新获取验证码");
        }
    }

    private boolean judPhone()
    {
        if(TextUtils.isEmpty(editTextPhone.getText().toString().trim()))
        {
            Toast.makeText(CodeLoginActivity.this,"请输入您的电话号码",Toast.LENGTH_LONG).show();
            editTextPhone.requestFocus();
            return false;
        }
        else if(editTextPhone.getText().toString().trim().length()!=11)
        {
            Toast.makeText(CodeLoginActivity.this,"您的电话号码位数不正确",Toast.LENGTH_LONG).show();
            editTextPhone.requestFocus();
            return false;
        }
        else
        {
            phone_number=editTextPhone.getText().toString().trim();
            String num="[1][358]\\d{9}";
            if(phone_number.matches(num))
                return true;
            else
            {
                Toast.makeText(CodeLoginActivity.this,"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

    private boolean judCord() {
        judPhone();
        if (TextUtils.isEmpty(editTextCode.getText().toString().trim())) {
            Toast.makeText(CodeLoginActivity.this, "请输入您的验证码", Toast.LENGTH_LONG).show();
            editTextCode.requestFocus();
            return false;
        } else if (editTextCode.getText().toString().trim().length() != 6) {
            Toast.makeText(CodeLoginActivity.this, "您的验证码位数不正确", Toast.LENGTH_LONG).show();
            editTextCode.requestFocus();

            return false;
        } else {
            code_number = editTextCode.getText().toString().trim();
            return true;
        }

    /*private void setListeners(Context context){
        RegisterActivity.OnClick onClick = new RegisterActivity.OnClick(context);
        mRegister.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{


        public Activity act;
        public String username;
        public String password;
        public String password2;
        public OnClick(Context context) {
            act=(Activity)context;
        }

        @Override
        public void onClick(View v){
            EditText editText1 = (EditText) findViewById(R.id.editTextTextPersonName);
            username = editText1.getText().toString();
            EditText editText2 = (EditText) findViewById(R.id.editTextTextPassword);
            password = editText2.getText().toString();
            EditText editText3 = (EditText) findViewById(R.id.editTextTextPassword2);
            password2 = editText3.getText().toString();
            Log.v(TAG,"username:"+username);
            Log.v(TAG,"password:"+password);
            Log.v(TAG,"password2:"+password2);
            if (password.equals(password2)) {
                try {
                    String path = "http://10.0.2.2:8000/";//

                    Map<String, String> params = new HashMap<String, String>();//将数据放在map里，便于取出传递

                    params.put("username", username);
                    params.put("password", password);

                    //调用get请求
                    if(sendPOSTRequest(path,params)){
                    Intent intent = null;
                    switch (v.getId()) {
                            case R.id.
                                    btn_register:
                                intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                break;
                    }
                    startActivity(intent);
                        Toast.makeText(act,"注册成功",Toast.LENGTH_SHORT).show();}
                    else Toast.makeText(act,"注册失败",Toast.LENGTH_SHORT).show();

                    //调用post请求
                    //sendGETRequest(path,params)
                    //sendPOSTRequest(path,params);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else Toast.makeText(act,"密码输入不一致",Toast.LENGTH_SHORT).show();
        }*/
    }
}