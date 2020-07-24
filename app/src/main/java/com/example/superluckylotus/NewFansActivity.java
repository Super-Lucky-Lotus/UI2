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
import android.widget.ImageButton;
import android.widget.ImageView;
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
 * @description: 新增粉丝界面
 * @data: 2020.07.14 16:35
 **/

/**
 * @version: 2.0
 * @author: 黄坤
 * @className: EarthFragment
 * @packageName:com.example.superluckylotus
 * @description: 获取数据库中新增加的粉丝列表
 * @data: 2020.07.20 12:00
 **/

public class NewFansActivity extends AppCompatActivity {

    private Button back_Btn;
    public static Handler uiHandler;
    private ListView list_new_fans;
    private ListView list_old_fans;
    public static MyAdapter adapter_new;
    public static MyAdapter adapter_old;
    public static List<UserInfo> newFans;
    public static List<UserInfo> oldFans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_fans);
        back_Btn=(Button)findViewById(R.id.nf_back);

        list_new_fans = (ListView) findViewById(R.id.list_new_fans);
        list_old_fans = (ListView) findViewById(R.id.list_old_fans);
        getData();
        setListeners();
    }

    private void getData() {
        newFans = new ArrayList<UserInfo>();
        oldFans = new ArrayList<UserInfo>();
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated constructor stub

                Phone phoneObj = (Phone) getApplication();
                final String phone = phoneObj.getPhone();

                String path = "http://139.219.4.34/newfanlist/";
                Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
                userParams.put("phone", phone);

                if (!phone.equals("")) {
                    HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                        @Override
                        public void onSuccess(String result) throws JSONException {
                            JSONObject result_json = new JSONObject(result);
                            String reback = result_json.getString("msg");
                            Log.v("NewFansActivity", result);
                            if (reback.equals("success")) {
                                int num = result_json.getInt("num");
                                for (int i = 1; i < num; i++) {
                                    String username = result_json.getString("Fan" + i + "Name");
                                    String state = result_json.getString("Fan"+i+"State");
                                    UserInfo fan = new UserInfo(username);
                                    if(state.equals(1)) {
                                        newFans.add(fan);
                                    }
                                    else{
                                        oldFans.add(fan);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.v("NewFansActivity", "连接失败！");
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
                    Toast.makeText(NewFansActivity.this, "还没有登录哦，快去登录吧!", Toast.LENGTH_SHORT).show();
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
                        adapter_new = new MyAdapter(newFans);
                        adapter_old = new MyAdapter(oldFans);
                        list_new_fans.setAdapter(adapter_new);
                        list_old_fans.setAdapter(adapter_old);
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
            inflater = (LayoutInflater) NewFansActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    intent = new Intent(NewFansActivity.this, SingleUserActivity.class);
                    intent.putExtra("username",info.getName());
                    startActivity(intent);
                }
            });

            return view;

        }

    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        back_Btn.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.nf_back:
                    finish();
                    break;
            }
        }
    }
}