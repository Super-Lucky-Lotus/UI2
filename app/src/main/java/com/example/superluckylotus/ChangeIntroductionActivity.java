package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @version: 2.0
 * @author: 黄诗雯
 * @className: ChangeIntroductionActivity
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
public class ChangeIntroductionActivity extends AppCompatActivity {

    protected TextView textView1;
    private TextView mPreserve;
    ImageButton mbacktosetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_introduction);
        mbacktosetting=(ImageButton)findViewById(R.id.backtosettting);
        mPreserve = (TextView)findViewById(R.id.btn_preserveintro);
        textView1=(TextView) findViewById(R.id.textView23);
        getData();
        setListeners();
    }

    private void getData() {
        Intent intent = getIntent();
        String description = intent.getStringExtra("description");
        textView1.setText(description);
    }

    private void setListeners(){
        ChangeIntroductionActivity.OnClick onClick = new ChangeIntroductionActivity.OnClick();
        mbacktosetting.setOnClickListener(onClick);
        mPreserve.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            EditText editText1 = (EditText) findViewById(R.id.et_newintroduction);
            String newdescription = editText1.getText().toString();
            Phone phoneObj = (Phone)getApplication();
            final String phone = phoneObj.getPhone();
            String path = "http://139.219.4.34/editdescription/";
            Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
            userParams.put("description", newdescription);
            userParams.put("phone", phone);

            switch (v.getId()){
                case R.id.btn_preserveintro:
                    HttpServer.SuperHttpUtil.post(userParams,path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                        @Override
                        public void onSuccess(String result) throws JSONException {
                            JSONObject result_json=new JSONObject(result);
                            String login=result_json.getString("msg");
                            if(login.equals("success")){
                                Toast.makeText(ChangeIntroductionActivity.this,"修改成功", Toast.LENGTH_SHORT).show();
                                Intent intent = null;
                                intent = new Intent(ChangeIntroductionActivity.this,SettingActivity.class);
                                startActivity(intent);
                            } else{
                                Toast.makeText(ChangeIntroductionActivity.this,"未知错误 ", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(ChangeIntroductionActivity.this,"服务器连接失败",Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onFinish() {
                        }
                    });
                    break;
                case R.id.backtosettting:
                    Intent intent = null;
                    intent = new Intent(ChangeIntroductionActivity.this,SettingActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}