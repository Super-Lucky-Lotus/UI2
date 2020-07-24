package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.IDNA;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mob.tools.gui.BitmapProcessor.start;

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

/**
 * @version: 3.0
 * @author: 宋佳容
 * @className: FriendListActivity
 * @packageName:com.example.superluckylotus
 * @description: 实现调用通讯录信息
 * @data: 2020.07.21 13:02
 **/

public class FindFriendActivity extends AppCompatActivity {

    private Button find_fri_back_btn;
    private TextView go_fri_list_btn;
    private EditText search_name;
    private Button search_btn;

    public static Handler uiHandler;
    private ListView list_find_friend;
    public static MyAdapter adapter;
    public static List<UserInfo> Users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);
        find_fri_back_btn = (Button) findViewById(R.id.find_fri_back);
        go_fri_list_btn = (TextView) findViewById(R.id.fri_list_btn);
        search_btn = (Button) findViewById(R.id.FindFri_search_btn);
        search_name = (EditText) findViewById(R.id.FindFri_search_edittext);
        list_find_friend=(ListView) findViewById(R.id.list_find_friend);

        setListeners();
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        find_fri_back_btn.setOnClickListener(onClick);
        go_fri_list_btn.setOnClickListener(onClick);
        search_btn.setOnClickListener(onClick);
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
                    new Thread() {
                        @Override
                        public void run() {
                            // TODO Auto-generated constructor stub
                            Users = new ArrayList<UserInfo>();

                            Phone phoneObj = (Phone) getApplication();
                            final String phone = phoneObj.getPhone();
                            String search = search_name.getText().toString();

                            String path = "http://139.219.4.34/getuser/";
                            Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
                            userParams.put("text", search);
                            userParams.put("phone", phone);

                            HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                                @Override
                                public void onSuccess(String result) throws JSONException {
                                    JSONObject result_json = new JSONObject(result);
                                    String msg = result_json.getString("msg");
                                    Log.v("FindFriendActivity", result);
                                    if (msg.equals("success")) {
                                        int num = result_json.getInt("num");
                                        for (int i = 1; i < num; i++) {
                                            String username = result_json.getString("User" + i + "Name");
                                            UserInfo user = new UserInfo(username);
                                            Users.add(user);
                                            Log.v("FindFriendActivity", username);
                                        }
                                    }
                                }

                                @Override
                                public void onError(Exception e) {
                                    Log.v("FindFriendActivity", "连接失败！");
                                }

                                @Override
                                public void onFinish() {
                                    // 子线程执行完毕的地方，利用主线程的handler发送消息
                                    Message msg2 = new Message();
                                    msg2.what = 1;
                                    uiHandler.sendMessage(msg2);
                                }
                            });
                        }
                    }.start();
                    Log.v("GetUsers","Next");
                    uiHandler = new Handler() {
                        // 覆写这个方法，接收并处理消息。
                        @Override
                        public void handleMessage(Message msg) {
                            switch (msg.what) {
                                case 1:
                                    Log.v("Thread","finished");
                                    adapter =new MyAdapter(Users);
                                    list_find_friend.setAdapter(adapter);
                                    break;
                            }
                        }
                    };
                    break;
            }
        }
    }



    public  class MyAdapter extends BaseAdapter {
        private List<UserInfo> infos;
        private LayoutInflater inflater;

        public MyAdapter(List<UserInfo> infos) {
            super();
            this.infos = infos;
            inflater = (LayoutInflater) FindFriendActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public Object getItem(int position) {

            return infos.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            int i=0;
            View view = inflater.inflate(R.layout.item_user, null);
            final TextView username = (TextView) view.findViewById(R.id.username_tv);
            final UserInfo info = infos.get(position);
            username.setText("  "+info.getName());

            Button user_pic = (Button) view.findViewById(R.id.user_pic);

            user_pic.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = null;
                    intent = new Intent(FindFriendActivity.this, SingleUserActivity.class);
                    intent.putExtra("username",info.getName());
                    startActivity(intent);
                }
            });

            return view;
        }
    }
}
