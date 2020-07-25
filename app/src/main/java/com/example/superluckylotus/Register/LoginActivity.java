package com.example.superluckylotus.Register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.superluckylotus.MainActivity;
import com.example.superluckylotus.Manager.HttpServer;
import com.example.superluckylotus.Phone;
import com.example.superluckylotus.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @version: 1.0
 * @author: 宋佳容
 * @className: LoginActivity
 * @packageName:com.example.superluckylotus
 * @description: 登录界面
 * @data: 2020.07.12 01:18
 **/

/**
 * @version: 2.0
 * @author: 黄诗雯
 * @className: LoginActivity
 * @packageName:com.example.superluckylotus
 * @description: 增加验证码登录
 * @data: 2020.07.12 01:18
 **/

/**
 * @version: 2.0
 * @author: 黄坤
 * @className: LoginActivity
 * @packageName:com.example.superluckylotus
 * @description: 实现手机号和密码登录功能
 * @data: 2020.07.18 16:18
 **/
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private Button login_Btn;
    private Button reg_Btn;
    private Button skip_Btn;
    private TextView mCodeLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_Btn = (Button)findViewById(R.id.btn_login);
        reg_Btn = (Button)findViewById(R.id.btn_reg);
        skip_Btn = (Button)findViewById(R.id.btn_skip);
        mCodeLogin=findViewById(R.id.tv_login);
        setListeners(this);
    }

    private void setListeners(Context context){
        OnClick onClick = new OnClick(context);
        login_Btn.setOnClickListener(onClick);
        reg_Btn.setOnClickListener(onClick);
        skip_Btn.setOnClickListener(onClick);
        mCodeLogin.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{


        public Activity act;
        public String phone;
        public String password;
        public OnClick(Context context) {
            act=(Activity)context;
        }


        @Override
        public void onClick(View v){
            EditText editText1 = (EditText) findViewById(R.id.name);
            phone = editText1.getText().toString();
            EditText editText2 = (EditText) findViewById(R.id.psw);
            password = editText2.getText().toString();
            String path = "http://139.219.4.34/login/";
            Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
            userParams.put("phone", phone);
            userParams.put("password", password);

            switch (v.getId()){
                case R.id.btn_login:
                    try {
                        HttpServer.SuperHttpUtil.post(userParams,path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                            @Override
                            public void onSuccess(String result) throws JSONException {
                                JSONObject result_json=new JSONObject(result);
                                String login=result_json.getString("msg");
                                if(login.equals("success")){
                                    Phone phoneObj = (Phone)getApplication();
                                    phoneObj.setPhone(phone);
                                    Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = null;
                                    intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                else if(login.equals("password failure")){
                                    Toast.makeText(LoginActivity.this,"登录失败，密码错误 ", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(LoginActivity.this,"登录失败，用户不存在 ", Toast.LENGTH_SHORT).show();
                                }
                                Log.v(TAG,result);
                            }
                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(LoginActivity.this,"服务器连接失败",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFinish() {
                                Toast.makeText(LoginActivity.this,"onFinish",Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.btn_reg://MainActivity要改成reg的act
                    Intent intent = null;
                    intent = new Intent(LoginActivity.this,RegisterActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_skip:
                    intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.tv_login:
                    intent = new Intent(LoginActivity.this,CodeLoginActivity.class);
                    startActivity(intent);
                    break;

            }


        }
    }


}