package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @version: 2.0
 * @author: 宋佳容
 * @className: FriendListActivity
 * @packageName:com.example.superluckylotus
 * @description: 寻找用户&添加好友界面
 * @data: 2020.07.16 16:22
 **/

/**
 * @version: 2.0
 * @author: 黄坤
 * @className: FriendListActivity
 * @packageName:com.example.superluckylotus
 * @description: 从数据库中寻找对应用户并显示出来，完成添加好友功能
 * @data: 2020.07.18 20:22
 **/

public class FindFriendActivity extends AppCompatActivity {

    private Button find_fri_back_btn;
    private TextView go_fri_list_btn;
    private TextView friend_name;
    private EditText search_name;
    private Button search_btn;
    private Button add_friend_btn;
    private String friend_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);
        find_fri_back_btn = (Button) findViewById(R.id.find_fri_back);
        go_fri_list_btn = (TextView) findViewById(R.id.fri_list_btn);
        search_btn = (Button) findViewById(R.id.FindFri_search_btn);
        search_name = (EditText) findViewById(R.id.FindFri_search_edittext);
        add_friend_btn = (Button) findViewById(R.id.addFri1_btn);
        friend_name = (TextView) findViewById(R.id.FindFri1_tv) ;
        setListeners();
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        find_fri_back_btn.setOnClickListener(onClick);
        go_fri_list_btn.setOnClickListener(onClick);
        search_btn.setOnClickListener(onClick);
        add_friend_btn.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.find_fri_back:
                    finish();
                    break;
                case R.id.fri_list_btn:
                    intent = new Intent(FindFriendActivity.this, FriendListActivity.class);
                    startActivity(intent);
                    break;
                case R.id.FindFri_search_btn:
                    String nickname = search_name.getText().toString();
                    String path = "http://139.219.4.34/getuser/";
                    Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
                    userParams.put("nickname", nickname);

                    if(!nickname.equals("")) {
                        HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                            @Override
                            public void onSuccess(String result) throws JSONException {
                                JSONObject result_json = new JSONObject(result);
                                String msg = result_json.getString("msg");
                                friend_phone = result_json.getString("phone");
                                Log.v("FriendListActivity", result);
                                if (msg.equals("success")) {
                                    Log.v("FriendListActivity", friend_phone);
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.v("FriendListActivity","连接失败！");
                            }

                            @Override
                            public void onFinish() {
                            }

                        });
                    }
                    else {
                        Toast.makeText(FindFriendActivity.this,"请输入你要搜索的用户名!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.addFri1_btn:
                    Phone phoneObj = (Phone)getApplication();
                    final String phone = phoneObj.getPhone();
                    String path2 = "http://139.219.4.34/addfriend/";
                    Map<String, String> userParams2 = new HashMap<String, String>();//将数据放在map里，便于取出传递
                    userParams2.put("friendphone", friend_phone);
                    userParams2.put("userphone", phone);

                        HttpServer.SuperHttpUtil.post(userParams2, path2, new HttpServer.SuperHttpUtil.HttpCallBack() {
                            @Override
                            public void onSuccess(String result) throws JSONException {
                                JSONObject result_json = new JSONObject(result);
                                String msg = result_json.getString("msg");
                                friend_phone = result_json.getString("phone");
                                Log.v("FriendListActivity", result);
                                if (msg.equals("success")) {
                                    Toast.makeText(FindFriendActivity.this,"添加成功！", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onError(Exception e) {
                                Log.v("FriendListActivity","连接失败！");
                            }
                            @Override
                            public void onFinish() {
                            }

                        });
                        break;
            }
        }
    }

}