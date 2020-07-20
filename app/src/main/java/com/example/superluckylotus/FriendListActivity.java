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

import static com.mob.tools.utils.DeviceHelper.getApplication;

/**
 * @version: 2.0
 * @author: 宋佳容
 * @className: FriendListActivity
 * @packageName:com.example.superluckylotus
 * @description: 好友列表
 * @data: 2020.07.16 16:18
 **/

/**
 * @version: 3.0
 * @author: 宋佳容
 * @className: FriendListActivity
 * @packageName:com.example.superluckylotus
 * @description: 分离对应xml文件
 * @data: 2020.07.20 16:02
 **/

/**
 * @version: 2.0
 * @author: 黄坤
 * @className: FriendListActivity
 * @packageName:com.example.superluckylotus
 * @description: 获取后端数据库存储的好友列表并显示出来
 * @data: 2020.07.18 20:18
 **/

public class FriendListActivity extends AppCompatActivity {

    private Button fri_list_back_btn;
    private TextView go_find_fri_btn;
    private Button freind_search_btn;
    private EditText search_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        getData();
        fri_list_back_btn = (Button) findViewById(R.id.fri_num_back);
        go_find_fri_btn = (TextView) findViewById(R.id.find_fri_btn);
        freind_search_btn= (Button) findViewById(R.id.FriL_search_btn);
        search_name = (EditText) findViewById(R.id.FriL_search_edittext);

        setListeners();
    }

    private void getData() {
        Phone phoneObj = (Phone)getApplication();
        final String phone = phoneObj.getPhone();
        String path = "http://139.219.4.34/friendlist/";
        Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
        userParams.put("phone", phone);

        if(!phone.equals("")) {
            HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                @Override
                public void onSuccess(String result) throws JSONException {
                    JSONObject result_json = new JSONObject(result);
                    String me = result_json.getString("msg");
                    Integer num = result_json.getInt("num");
                    Log.v("FriendListActivity",result);
                    if (me.equals("success")) {
                        if(num != 0) {
                            for (int i = 0; i < num; i++) {
                                String FriendName = result_json.getString("Friend" + i + "Name");
                                Log.v("FriendListActivity", FriendName);
                            }
                        }
                        else{
                            Toast.makeText(FriendListActivity.this,"还没有好友哦，快去添加吧!", Toast.LENGTH_SHORT).show();
                        }
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
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        fri_list_back_btn.setOnClickListener(onClick);
        go_find_fri_btn.setOnClickListener(onClick);
        freind_search_btn.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.fri_num_back:
                    finish();
                    break;
                case R.id.find_fri_btn:
                    intent = new Intent(FriendListActivity.this, FindFriendActivity.class);
                    startActivity(intent);
                    break;
                case R.id.FriL_search_btn:
                    String nickname = search_name.getText().toString();
                    String path = "http://139.219.4.34/getfriend/";
                    Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
                    userParams.put("nickname", nickname);

                    if(!nickname.equals("")) {
                        HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                            @Override
                            public void onSuccess(String result) throws JSONException {
                                JSONObject result_json = new JSONObject(result);
                                String me = result_json.getString("msg");
                                Log.v("FriendListActivity",result);
                                if (me.equals("success")) {
                                    String FriendName = result_json.getString("nickname");
                                    Log.v("FriendListActivity", FriendName);
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
                    else{
                        Toast.makeText(FriendListActivity.this,"请输入你要搜索的用户名!", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
}