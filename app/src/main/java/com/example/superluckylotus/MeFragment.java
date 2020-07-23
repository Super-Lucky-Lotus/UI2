package com.example.superluckylotus;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mob.tools.utils.DeviceHelper.getApplication;


/**
 * @version: 1.0
 * @author: 黄诗雯
 * @className: MeFragment
 * @packageName:com.example.superluckylotus
 * @description: 我的界面
 * @data: 2020.07.12 00:18
 **/

/**
 * @version: 2.0
 * @author: 宋佳容
 * @className: MeFragment
 * @packageName:com.example.superluckylotus
 * @description: 增加好友数 和添加好友按钮的跳转
 * @data: 2020.07.16 16:05
 **/
/**
 * @version: 2.0
 * @author: 黄坤
 * @className: MeFragment
 * @packageName:com.example.superluckylotus
 * @description: 显示对应用户的个人信息
 * @data: 2020.07.16 18:05
 **/

public class MeFragment extends Fragment {

    private static final String TAG = "MeFragment";
    protected String nickname="用户名";
    protected String likes_counts ="0";
    protected String fans_counts = "0";
    protected String follows_counts = "0";
    protected String friends_counts = "0";
    protected String id = "";
    private Button mInfo;
    private Button find_fri_btn;
    private Button fri_num_btn;
    private Button fans_num_btn;
    private Button follow_num_btn;
    protected TextView textView1;
    protected TextView textView2;
    protected TextView textView3;
    protected TextView textView4;
    protected TextView textView5;
    protected TextView textView6;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, null);
        textView1 = (TextView) view.findViewById(R.id.tv_username);
        textView2 = (TextView) view.findViewById(R.id.tv_userID);
        textView3 = (TextView) view.findViewById(R.id.num_like);
        textView4 = (TextView) view.findViewById(R.id.num_follow);
        textView5 = (TextView) view.findViewById(R.id.num_fans);
        textView6 = (TextView) view.findViewById(R.id.num_friends);
        mInfo = (Button) view.findViewById(R.id.btn_setting);
        find_fri_btn = (Button) view.findViewById(R.id.addFri_btn);
        fri_num_btn = (Button) view.findViewById(R.id.num_friend_btn);
        fans_num_btn = (Button) view.findViewById(R.id.num_fans_btn);
        follow_num_btn = (Button) view.findViewById(R.id.num_follow_btn);
        OnClick onclick = new OnClick();
        mInfo.setOnClickListener(onclick);
        find_fri_btn.setOnClickListener(onclick);
        fri_num_btn.setOnClickListener(onclick);
        fans_num_btn.setOnClickListener(onclick);
        follow_num_btn.setOnClickListener(onclick);
        start();
        return view;
    }

    private void start() {
        Phone phoneObj = (Phone)getApplication();
        final String phone = phoneObj.getPhone();
        String path = "http://139.219.4.34/me/";
        Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
        userParams.put("phone", phone);

        if(!phone.equals("")) {
            HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                @Override
                public void onSuccess(String result) throws JSONException {
                    JSONObject result_json = new JSONObject(result);
                    String me = result_json.getString("msg");
                    Log.v(TAG,result);
                    if (me.equals("success")) {
                        nickname = result_json.getString("nickname");
                        id = result_json.getString("id");
                        fans_counts = result_json.getString("fans_counts");
                        follows_counts = result_json.getString("follow_counts");
                        friends_counts = result_json.getString("friends_counts");
                        likes_counts = result_json.getString("receive_like_counts");
                        textView1.setText(nickname);
                        textView2.setText(id);
                        textView3.setText(likes_counts);
                        textView4.setText(follows_counts);
                        textView5.setText(fans_counts);
                        textView6.setText(friends_counts);
                    }
                }

                @Override
                public void onError(Exception e) {
                    Log.v(TAG,"连接失败！");
                }

                @Override
                public void onFinish() {
                }

            });
        }
    }


    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.btn_setting:
                    intent.setClass(getActivity(), SettingActivity.class);
                    break;
                case R.id.addFri_btn:
                    intent.setClass(getActivity(), FindFriendActivity.class);
                    break;
                case R.id.num_friend_btn:
                    intent.setClass(getActivity(), FriendListActivity.class);
                    break;
                case R.id.num_fans_btn:
                    intent.setClass(getActivity(), FansListActivity.class);
                    break;
                case R.id.num_follow_btn:
                    intent.setClass(getActivity(), FollowListActivity.class);
                    break;
            }
            getActivity().startActivity(intent);
        }
    }
}
