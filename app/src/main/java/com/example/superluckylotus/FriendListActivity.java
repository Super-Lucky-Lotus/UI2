package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.mob.wrappers.UMSSDKWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static Handler uiHandler;
    private ListView list_friend;
    public static MyAdapter adapter;
    public static List<UserInfo> Users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        getData();
        fri_list_back_btn = (Button) findViewById(R.id.fri_num_back);
        go_find_fri_btn = (TextView) findViewById(R.id.find_fri_btn);
        freind_search_btn= (Button) findViewById(R.id.FriL_search_btn);
        search_name = (EditText) findViewById(R.id.FriL_search_edittext);
        list_friend=(ListView) findViewById(R.id.list_friend);

        setListeners();
    }

    private void getData() {
        Users = new ArrayList<UserInfo>();
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated constructor stub
                Users = new ArrayList<UserInfo>();

                Phone phoneObj = (Phone) getApplication();
                final String phone = phoneObj.getPhone();

                String path = "http://139.219.4.34/friendlist/";
                Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
                userParams.put("phone", phone);

                if (!phone.equals("")) {
                    HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                        @Override
                        public void onSuccess(String result) throws JSONException {
                            JSONObject result_json = new JSONObject(result);
                            String reback = result_json.getString("msg");
                            int num = result_json.getInt("num");
                            Log.v("FriendListActivity", result);
                            if (reback.equals("success")) {
                                for (int i = 1; i < num; i++) {
                                    String username = result_json.getString("Friend" + i + "Name");
                                    UserInfo user = new UserInfo(username);
                                    Users.add(user);
                                }
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.v("FriendListActivity", "连接失败！");
                        }

                        @Override
                        public void onFinish() {
                            // 子线程执行完毕的地方，利用主线程的handler发送消息
                            Message msg = new Message();
                            msg.what = 1;
                            uiHandler.sendMessage(msg);
                        }
                    });
                } else {
                    Toast.makeText(FriendListActivity.this, "还没有登录哦，快去登录吧!", Toast.LENGTH_SHORT).show();
                }
            }
        }.start();
        Log.v("GetUsers", "Next");
        uiHandler = new Handler() {
            // 覆写这个方法，接收并处理消息。
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        Log.v("Thread", "finished");
                        adapter = new MyAdapter(Users);
                        list_friend.setAdapter(adapter);
                        break;
                }

            }
        };
    }

    public  class MyAdapter extends BaseAdapter {
        private List<UserInfo> infos;
        private LayoutInflater inflater;

        public MyAdapter(List<UserInfo> infos) {
            super();
            this.infos = infos;
            inflater = (LayoutInflater) FriendListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

            View view = inflater.inflate(R.layout.item_user, null);
            final TextView username = (TextView) view.findViewById(R.id.username_tv);
            final UserInfo info = infos.get(position);
            username.setText("  "+info.getName());

            Button user_pic = (Button) view.findViewById(R.id.user_pic);

            user_pic.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = null;
                    intent = new Intent(FriendListActivity.this, SingleUserActivity.class);
                    intent.putExtra("username",info.getName());
                    startActivity(intent);
                }
            });

            return view;
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
                    Users=new ArrayList<UserInfo>();
                    new Thread() {
                        @Override
                        public void run() {
                            // TODO Auto-generated constructor stub
                            Users = new ArrayList<UserInfo>();

                            Phone phoneObj = (Phone) getApplication();
                            final String phone = phoneObj.getPhone();
                            String search = search_name.getText().toString();

                            String path = "http://139.219.4.34/getfriend/";
                            Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
                            userParams.put("text", search);
                            userParams.put("phone", phone);

                            HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                                @Override
                                public void onSuccess(String result) throws JSONException {
                                    JSONObject result_json = new JSONObject(result);
                                    String msg = result_json.getString("msg");
                                    int num = result_json.getInt("num");
                                    Log.v("FriendListActivity", result);
                                    if (msg.equals("success")) {
                                        for (int i = 1; i < num; i++) {
                                            String username = result_json.getString("User" + i + "Name");
                                            UserInfo user = new UserInfo(username);
                                            Users.add(user);
                                            Log.v("FriendListActivity", username);
                                        }
                                    }
                                }

                                @Override
                                public void onError(Exception e) {
                                    Log.v("FriendListActivity", "连接失败！");
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
                                    list_friend.setAdapter(adapter);
                                    break;
                            }
                        }
                    };
                    break;
            }
        }
    }
}