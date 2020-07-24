package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version: 2.0
 * @author: 宋佳容
 * @className: NewFansActivity
 * @packageName:com.example.superluckylotus
 * @description: 粉丝列表界面
 * @data: 2020.07.17 18:15
 **/

/**
 * @version: 2.0
 * @author: 黄坤
 * @className: FriendListActivity
 * @packageName:com.example.superluckylotus
 * @description: 获取后端数据库存储的粉丝列表并显示出来
 * @data: 2020.07.18 20:18
 **/

public class FansListActivity extends AppCompatActivity {

    private Button fans_list_back_btn;
    private Button fan_search_btn;
    private EditText search_name;
    public static Handler uiHandler;
    private ListView list_fans;
    public static MyAdapter adapter;
    public static List<UserInfo> Fans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans_list);

        getData();
        fans_list_back_btn = (Button) findViewById(R.id.fans_list_back);
        fan_search_btn= (Button) findViewById(R.id.FansL_search_btn);
        search_name = (EditText) findViewById(R.id.FansL_search_edittext);
        list_fans=(ListView) findViewById(R.id.list_fans);
        setListeners();
    }

    private void getData() {
        Fans = new ArrayList<UserInfo>();
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated constructor stub

                Phone phoneObj = (Phone) getApplication();
                final String phone = phoneObj.getPhone();

                String path = "http://139.219.4.34/fanlist/";
                Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
                userParams.put("phone", phone);

                if (!phone.equals("")) {
                    HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                        @Override
                        public void onSuccess(String result) throws JSONException {
                            JSONObject result_json = new JSONObject(result);
                            String reback = result_json.getString("msg");
                            Log.v("FansListActivity", result);
                            if (reback.equals("success")) {
                                int num = result_json.getInt("num");
                                for (int i = 1; i < num; i++) {
                                    String username = result_json.getString("Follower" + i + "Name");
                                    UserInfo fan = new UserInfo(username);
                                    Fans.add(fan);
                                }
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.v("FansListActivity", "连接失败！");
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
                    Toast.makeText(FansListActivity.this, "还没有登录哦，快去登录吧!", Toast.LENGTH_SHORT).show();
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
                        adapter = new FansListActivity.MyAdapter(Fans);
                        list_fans.setAdapter(adapter);
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
            inflater = (LayoutInflater) FansListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

            return 0;
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
                    intent = new Intent(FansListActivity.this, SingleUserActivity.class);
                    intent.putExtra("username",info.getName());
                    startActivity(intent);
                }
            });

            return view;

        }

    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        fans_list_back_btn.setOnClickListener(onClick);
        fan_search_btn.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.fans_list_back:
                    finish();
                    break;
                case R.id.FansL_search_btn:
                    Fans=new ArrayList<UserInfo>();
                    new Thread() {
                        @Override
                        public void run() {
                            // TODO Auto-generated constructor stub
                            Fans = new ArrayList<UserInfo>();

                            Phone phoneObj = (Phone) getApplication();
                            final String phone = phoneObj.getPhone();
                            String search = search_name.getText().toString();

                            String path = "http://139.219.4.34/getfan/";
                            Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
                            userParams.put("fan_name", search);
                            userParams.put("phone", phone);

                            HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                                @Override
                                public void onSuccess(String result) throws JSONException {
                                    JSONObject result_json = new JSONObject(result);
                                    String msg = result_json.getString("msg");
                                    int num = result_json.getInt("num");
                                    Log.v("FansListActivity", result);
                                    if (msg.equals("success")) {
                                        for (int i = 1; i < num; i++) {
                                            String username = result_json.getString("Fan" + i + "Name");
                                            UserInfo fan = new UserInfo(username);
                                            Fans.add(fan);
                                            Log.v("FansListActivity", username);
                                        }
                                    }
                                }

                                @Override
                                public void onError(Exception e) {
                                    Log.v("FansListActivity", "连接失败！");
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
                                    adapter =new MyAdapter(Fans);
                                    list_fans.setAdapter(adapter);
                                    break;
                            }
                        }
                    };
                    break;
            }
        }
    }

}