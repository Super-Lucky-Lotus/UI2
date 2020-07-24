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
 * @description: 查看评论列表界面
 * @data: 2020.07.14 16:45
 **/

/**
 * @version: 2.0
 * @author: 黄坤
 * @className: EarthFragment
 * @packageName:com.example.superluckylotus
 * @description: 获取数据库中关于该视频的评论
 * @data: 2020.07.19 22:23
 **/

public class CommentActivity extends AppCompatActivity {

    private Button back_Btn;
    public static Handler uiHandler;
    private ListView list_comment;
    public static MyAdapter adapter;
    public static List<MsgInfo> Msgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        back_Btn = (Button) findViewById(R.id.cm_back);

        list_comment=(ListView) findViewById(R.id.list_comment);
        getData();
        setListeners();
    }



    private void getData() {
        Msgs = new ArrayList<MsgInfo>();
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated constructor stub
                Msgs = new ArrayList<MsgInfo>();

                Phone phoneObj = (Phone) getApplication();
                final String phone = phoneObj.getPhone();

                String path = "http://139.219.4.34/newcommentlist/";
                Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
                userParams.put("phone", phone);

                if (!phone.equals("")) {
                    HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                        @Override
                        public void onSuccess(String result) throws JSONException {
                            JSONObject result_json = new JSONObject(result);
                            String reback = result_json.getString("msg");
                            Log.v("CommentActivity", result);
                            if (reback.equals("success")) {
                                int num = result_json.getInt("num");
                                for (int i = 1; i < num; i++) {
                                    String username = result_json.getString("User" + i + "Name");
                                    String time = result_json.getString("Comment" + i + "Time");
                                    String text = result_json.getString("Comment"+i+"Content");
                                    MsgInfo msg = new MsgInfo(username, time, "评论了你:"+text);
                                    Msgs.add(msg);
                                }
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.v("CommentActivity", "连接失败！");
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
                    Toast.makeText(CommentActivity.this, "还没有登录哦，快去登录吧!", Toast.LENGTH_SHORT).show();
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
                        adapter = new CommentActivity.MyAdapter(Msgs);
                        list_comment.setAdapter(adapter);
                        break;
                }

            }
        };
    }

    public  class MyAdapter extends BaseAdapter {
        private List<MsgInfo> infos;
        private LayoutInflater inflater;

        public MyAdapter(List<MsgInfo> infos) {
            super();
            this.infos = infos;
            inflater = (LayoutInflater) CommentActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            View view = inflater.inflate(R.layout.item_info, null);
            final TextView username_tv = (TextView) view.findViewById(R.id.info_name_tv);
            TextView kind_tv = (TextView) view.findViewById(R.id.info_kind_tv);
            TextView time_tv = (TextView) view.findViewById(R.id.info_time_tv);
            Button user_pic = (Button) view.findViewById(R.id.info_user_pic);

            final MsgInfo info = infos.get(position);
            username_tv.setText(info.getName());
            kind_tv.setText(info.getKind());
            time_tv.setText(info.getTime());

            user_pic.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = null;
                    intent = new Intent(CommentActivity.this, SingleUserActivity.class);
                    intent.putExtra("username",info.getName());
                    startActivity(intent);
                }
            });

            return view;

        }

    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        back_Btn.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.cm_back:
                    finish();
                    break;
            }
        }
    }
}