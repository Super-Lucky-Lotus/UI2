package com.example.superluckylotus.Me;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.superluckylotus.Manager.HttpServer;
import com.example.superluckylotus.Phone;
import com.example.superluckylotus.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @version: 2.0
 * @author: 黄诗雯
 * @className: ChangeNameActivity
 * @packageName:com.example.superluckylotus
 * @description: 更改姓名界面
 * @data: 2020.07.14 16:38
 **/

/**
 * @version: 2.0
 * @author: 黄坤
 * @className: ChangeNameActivity
 * @packageName:com.example.superluckylotus
 * @description: 更改姓名信息到数据库
 * @data: 2020.07.14 16:38
 **/

public class ChangeNameActivity extends AppCompatActivity {

    protected TextView textView1;
    private TextView mPreserve;
    ImageButton mbacktosetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        mbacktosetting=(ImageButton)findViewById(R.id.backtosettting);
        mPreserve = (TextView)findViewById(R.id.btn_preservename);
        textView1=(TextView) findViewById(R.id.textView19);
        getData();
        setListeners();
    }

    //接收上个界面跳转过来携带的参数
    private void getData() {
        Intent intent = getIntent();
        String nickname = intent.getStringExtra("nickname");
        textView1.setText("现在的名字："+nickname);
    }

    private void setListeners(){
        ChangeNameActivity.OnClick onClick = new ChangeNameActivity.OnClick();
        mbacktosetting.setOnClickListener(onClick);
        mPreserve.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            EditText editText1 = (EditText) findViewById(R.id.et_newname);
            String newname = editText1.getText().toString();
            Log.v("ChangeNameActivity.this",newname);
            Phone phoneObj = (Phone)getApplication();
            final String phone = phoneObj.getPhone();
            Log.v("ChangeNameActivity.this",phone);
            String path = "http://139.219.4.34/editnickname/";
            Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
            userParams.put("nickname", newname);
            userParams.put("phone", phone);

            switch (v.getId()){
                case R.id.btn_preservename:
                    HttpServer.SuperHttpUtil.post(userParams,path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                        @Override
                        public void onSuccess(String result) throws JSONException {
                            JSONObject result_json=new JSONObject(result);
                            String login=result_json.getString("msg");
                            if(login.equals("success")){
                                Toast.makeText(ChangeNameActivity.this,"修改成功", Toast.LENGTH_SHORT).show();
                                Intent intent = null;
                                intent = new Intent(ChangeNameActivity.this,SettingActivity.class);
                                startActivity(intent);
                            } else{
                                Toast.makeText(ChangeNameActivity.this,"未知错误 ", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(ChangeNameActivity.this,"服务器连接失败",Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onFinish() {
                        }
                    });
                    break;
                case R.id.backtosettting:
                    Intent intent = null;
                    intent = new Intent(ChangeNameActivity.this,SettingActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}