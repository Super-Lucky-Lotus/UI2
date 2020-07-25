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
 * @className: ChangePasswordActivity
 * @packageName:com.example.superluckylotus
 * @description: 更改简介界面
 * @data: 2020.07.14 16:38
 **/

/**
 * @version: 2.0
 * @author: 黄坤
 * @className: ChangeNameActivity
 * @packageName:com.example.superluckylotus
 * @description: 更改信息到数据库
 * @data: 2020.07.14 16:38
 **/
public class ChangePasswordActivity extends AppCompatActivity {

    private String password;
    private TextView mPreserve;
    ImageButton mbacktosetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mPreserve = (TextView)findViewById(R.id.btn_preservepassword);
        mbacktosetting=(ImageButton)findViewById(R.id.backtosettting);
        getData();
        setListeners();
    }

    private void getData() {
        Intent intent = getIntent();
        password = intent.getStringExtra("password");
    }

    private void setListeners(){
        ChangePasswordActivity.OnClick onClick = new ChangePasswordActivity.OnClick();
        mbacktosetting.setOnClickListener(onClick);
        mPreserve.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            EditText editText1 = (EditText) findViewById(R.id.et_newpassword);
            String newpassword= editText1.getText().toString();
            EditText editText2 = (EditText) findViewById(R.id.et_confrimpassword);
            String confirmpassword= editText2.getText().toString();
            EditText editText3 = (EditText) findViewById(R.id.et_oldpassword);
            String oldpassword= editText3.getText().toString();
            Phone phoneObj = (Phone)getApplication();
            final String phone = phoneObj.getPhone();
            String path = "http://139.219.4.34/editpassword/";
            Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
            userParams.put("password", newpassword);
            userParams.put("phone", phone);
            Log.v("ChangePasswordActivity",newpassword);

            switch (v.getId()){
                case R.id.btn_preservepassword:
                    if(oldpassword.equals(password)) {
                        if (newpassword.equals(confirmpassword)) {
                            HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                                @Override
                                public void onSuccess(String result) throws JSONException {
                                    JSONObject result_json = new JSONObject(result);
                                    String edit = result_json.getString("msg");
                                    if (edit.equals("success")) {
                                        Toast.makeText(ChangePasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                        Intent intent = null;
                                        intent = new Intent(ChangePasswordActivity.this, SettingActivity.class);
                                        startActivity(intent);
                                    }  else {
                                        Toast.makeText(ChangePasswordActivity.this, "未知错误 ", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onError(Exception e) {
                                    Toast.makeText(ChangePasswordActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFinish() {
                                }
                            });
                        }
                        else {
                            Toast.makeText(ChangePasswordActivity.this, "两次密码填写不一致", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(ChangePasswordActivity.this, "原密码错误", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.backtosettting:
                    Intent intent = null;
                    intent = new Intent(ChangePasswordActivity.this,SettingActivity.class);
                    startActivity(intent);
                    break;
            }

        }
    }
}