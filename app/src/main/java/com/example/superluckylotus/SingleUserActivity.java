package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @version: 2.0
 * @author: 宋佳容
 * @className: NewFansActivity
 * @packageName:com.example.superluckylotus
 * @description: 查看用户界面
 * @data: 2020.07.17 22:10
 **/

public class SingleUserActivity extends AppCompatActivity {


    private String username="用户名";
    private String likes_counts ="0";
    private String fans_counts = "0";
    private String follows_counts = "0";
    private String friends_counts = "0";
    private String id = "";

    protected TextView textView1;
    protected TextView textView2;
    protected TextView textView3;
    protected TextView textView4;
    protected TextView textView5;
    protected TextView textView6;

    private Button single_user_back;
    private Button single_user_add_friend;
    private Button single_user_follow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_user);

        textView1 = (TextView) findViewById(R.id.single_username);
        textView2 = (TextView) findViewById(R.id.single_userID);
        textView3 = (TextView) findViewById(R.id.single_user_num_like);
        textView4 = (TextView) findViewById(R.id.single_user_num_focus);
        textView5 = (TextView) findViewById(R.id.single_user_num_fans);
        textView6 = (TextView) findViewById(R.id.single_user_num_friends);

        single_user_back = (Button) findViewById(R.id.single_user_back);
        single_user_add_friend = (Button) findViewById(R.id.single_user_add_friend);
        single_user_follow = (Button) findViewById(R.id.single_user_follow);

        getData();
        setListeners();
    }

    //接收上个界面跳转过来携带的参数
    private void getData() {
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        Phone phoneObj = (Phone)getApplication();
        final String phone = phoneObj.getPhone();
        String path = "http://139.219.4.34/userprofile/";
        Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
        userParams.put("username", username);
        userParams.put("phone", phone);

        HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String result) throws JSONException {
                JSONObject result_json = new JSONObject(result);
                String get = result_json.getString("msg");
                Log.v("SingleUserActivity",result);
                if (get.equals("success")) {
                    username = result_json.getString("nickname");
                    id = result_json.getString("id");
                    fans_counts = result_json.getString("fans_counts");
                    follows_counts = result_json.getString("follow_counts");
                    friends_counts = result_json.getString("friends_counts");
                    likes_counts = result_json.getString("receive_like_counts");
                    String follow = result_json.getString("follow");
                    String friend = result_json.getString("friend");
                    if(follow.equals("true")){
                        single_user_follow.setText("取消\n关注");
                    }
                    else{
                        single_user_follow.setText("关注");
                    }
                    if(friend.equals("true")){
                        single_user_add_friend.setText("删除\n好友");
                    }
                    else{
                        single_user_add_friend.setText("好友");
                    }
                    textView1.setText(username);
                    textView2.setText(id);
                    textView3.setText(likes_counts);
                    textView4.setText(follows_counts);
                    textView5.setText(fans_counts);
                    textView6.setText(friends_counts);
                }
            }

            @Override
            public void onError(Exception e) {

                Log.v("SettingActivity", "连接失败！");
            }

            @Override
            public void onFinish() {
            }

        });
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        single_user_add_friend.setOnClickListener(onClick);
        single_user_follow.setOnClickListener(onClick);
        single_user_back.setOnClickListener(onClick);

    }

    class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.single_user_back:
                    finish();
                    break;
                case R.id.single_user_add_friend:
                    if(single_user_add_friend.getText().toString().equals("添加\n好友")) {
                        Phone phoneObj = (Phone) getApplication();
                        final String phone = phoneObj.getPhone();
                        String path2 = "http://139.219.4.34/addfriend/";
                        Map<String, String> userParams2 = new HashMap<String, String>();//将数据放在map里，便于取出传递
                        userParams2.put("friend_nickname", username);
                        userParams2.put("user_phone", phone);

                        HttpServer.SuperHttpUtil.post(userParams2, path2, new HttpServer.SuperHttpUtil.HttpCallBack() {
                            @Override
                            public void onSuccess(String result) throws JSONException {
                                JSONObject result_json = new JSONObject(result);
                                String msg = result_json.getString("msg");
                                Log.v("SingleUserActivity", result);
                                if (msg.equals("success")) {
                                    Toast.makeText(SingleUserActivity.this,"添加成功!", Toast.LENGTH_SHORT).show();
                                }
                                single_user_add_friend.setText("删除\n好友");
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.v("SingleUserActivity", "连接失败！");
                            }

                            @Override
                            public void onFinish() {
                            }

                        });
                    }
                    else{
                        Phone phoneObj = (Phone) getApplication();
                        final String phone = phoneObj.getPhone();
                        String path2 = "http://139.219.4.34/deletefriend/";
                        Map<String, String> userParams2 = new HashMap<String, String>();//将数据放在map里，便于取出传递
                        userParams2.put("friend_nickname", username);
                        userParams2.put("user_phone", phone);

                        HttpServer.SuperHttpUtil.post(userParams2, path2, new HttpServer.SuperHttpUtil.HttpCallBack() {
                            @Override
                            public void onSuccess(String result) throws JSONException {
                                JSONObject result_json = new JSONObject(result);
                                String msg = result_json.getString("msg");
                                Log.v("SingleUserActivity", result);
                                if (msg.equals("success")) {
                                    Toast.makeText(SingleUserActivity.this,"删除成功！", Toast.LENGTH_SHORT).show();
                                }
                                single_user_add_friend.setText("添加\n好友");
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.v("SingleUserActivity", "连接失败！");
                            }

                            @Override
                            public void onFinish() {
                            }

                        });
                    }
                    break;
                case R.id.single_user_follow:
                    if(single_user_follow.getText().toString().equals("关注")) {
                        Phone phoneObj2 = (Phone) getApplication();
                        final String phone2 = phoneObj2.getPhone();
                        String path = "http://139.219.4.34/addfollow/";
                        Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
                        userParams.put("username", username);
                        userParams.put("phone", phone2);

                        HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                            @Override
                            public void onSuccess(String result) throws JSONException {
                                JSONObject result_json = new JSONObject(result);
                                String msg = result_json.getString("msg");
                                Log.v("SingleUserActivity", result);
                                if (msg.equals("success")) {
                                    Toast.makeText(SingleUserActivity.this,"关注成功！", Toast.LENGTH_SHORT).show();
                                }
                                single_user_follow.setText("取消\n关注");
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.v("SingleUserActivity", "连接失败！");
                            }

                            @Override
                            public void onFinish() {
                            }

                        });
                    }
                    else{
                        Phone phoneObj2 = (Phone) getApplication();
                        final String phone2 = phoneObj2.getPhone();
                        String path = "http://139.219.4.34/cancelfollow/";
                        Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
                        userParams.put("username", username);
                        userParams.put("phone", phone2);

                        HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                            @Override
                            public void onSuccess(String result) throws JSONException {
                                JSONObject result_json = new JSONObject(result);
                                String msg = result_json.getString("msg");
                                Log.v("SingleUserActivity", result);
                                if (msg.equals("success")) {
                                    Toast.makeText(SingleUserActivity.this,"取消关注成功！", Toast.LENGTH_SHORT).show();
                                }
                                single_user_follow.setText("关注");
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.v("SingleUserActivity", "连接失败！");
                            }

                            @Override
                            public void onFinish() {
                            }

                        });
                    }
                    break;
            }
        }
    }

}