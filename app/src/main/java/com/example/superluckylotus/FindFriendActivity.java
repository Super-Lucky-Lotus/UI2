package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
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
    private TextView friend_name;
    private EditText search_name;
    private Button search_btn;
    private Button add_friend_btn;
    private String friend_phone;
    public static MyAdapter adapter;
    public static List<PhoneInfo> histories;
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
        histories=new ArrayList<PhoneInfo>();
        String[] permissions=new String[]{Manifest.permission.READ_CONTACTS};
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            requestPermissions(permissions,202);
        }
        adapter =new MyAdapter(getPhoneNumberFromMobile(this));
        final ListView listv=(ListView) findViewById(R.id.listv);



        listv.setAdapter(adapter);

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
    private List<PhoneInfo> list;
    public List<PhoneInfo> getPhoneNumberFromMobile(Context context) {
        // TODO Auto-generated constructor stub
        list = new ArrayList<PhoneInfo>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] { "display_name", "sort_key", "contact_id",
                        "data1" }, null, null, null);
//        moveToNext方法返回的是一个boolean类型的数据
        while (cursor.moveToNext()) {
            //读取通讯录的姓名
            String name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //读取通讯录的号码
            String number = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            int Id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            String Sortkey = getSortkey(cursor.getString(1));
            PhoneInfo phoneInfo = new PhoneInfo(name, number,Sortkey,Id);
            list.add(phoneInfo);
        }
        cursor.close();
        return list;
    }

    private static String getSortkey(String sortKeyString) {
        String key =sortKeyString.substring(0,1).toUpperCase();
        if (key.matches("[A-Z]")){
            return key;
        }else
            return "#";
    }
    public  class MyAdapter extends BaseAdapter {
        private List<PhoneInfo> infos;
        private LayoutInflater inflater;

        public MyAdapter(List<PhoneInfo> infos) {
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

            return 0;
        }

        // "顾客名称","商品编号","顾客地址","数量","价格"};
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int i=0;
            View view = inflater.inflate(R.layout.item_findfri, null);
            TextView tv_number = (TextView) view.findViewById(R.id.FindFri_tv);
            PhoneInfo info = infos.get(position);
            tv_number.setText("  "+info.getName()+"\n"+info.getPhone()

                    +"");


            return view;

        }

    }
}