package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    Button mRegister;
    private static final String TAG = "RegisterActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mRegister=(Button)findViewById(R.id.btn_register);
        setListeners(this);

    }
    private void setListeners(Context context){
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
                    sendGETRequest(path,params);

                    //调用post请求
                    //sendPOSTRequest(path,params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = null;
                switch (v.getId()) {
                    case R.id.
                            btn_register:
                        intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        break;
                }
                startActivity(intent);
            }else Toast.makeText(act,"注册失败",Toast.LENGTH_LONG).show();
        }

        private void sendGETRequest(String path, Map<String, String> params) throws Exception {

            //生成path路径
            path +="get/";
            final StringBuilder sb = new StringBuilder(path);
            if (params != null && !params.isEmpty()) {
                sb.append("?");
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    //将map数据取出并附在url后面
                    sb.append(entry.getKey()).append("=");
                    sb.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
                    sb.append("&");
                }
                sb.deleteCharAt(sb.length() - 1);
            }

            //启用子线程向服务器发出get请求
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        URL url = new URL(sb.toString());
                        Log.v(TAG, url.toString());//抓包查询生成路径
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setConnectTimeout(3000);//设置响应时间
                        connection.setRequestMethod("GET");
                        connection.setDoInput(true);  //从服务器获得数据

                        //获取服务器响应结果
                        if (connection.getResponseCode() == 200) {

                            InputStream inputStream = connection.getInputStream();
                            int len = 0;
                            ByteArrayOutputStream outStream2 = new ByteArrayOutputStream();
                            byte[] data = new byte[1024];
                            while ((len = inputStream.read(data)) != -1) {
                                outStream2.write(data, 0, len);
                            }
                            outStream2.close();
                            inputStream.close();
                            String responseStr = new String(outStream2.toByteArray());
                            //抓包获取服务端的响应
                            Log.v(TAG, responseStr);

                        }
                    }catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }


    }
}