package com.example.superluckylotus;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;


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

public class MeFragment extends Fragment {

    private Button mInfo;
    private Button find_fri_btn;
    private Button fri_num_btn;
    private  Button fans_num_btn;
    private  Button follow_num_btn;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me,null);
        mInfo=(Button) view.findViewById(R.id.btn_setting);
        find_fri_btn=(Button)view.findViewById(R.id.addFri_btn) ;
        fri_num_btn=(Button) view.findViewById(R.id.num_friend_btn);
        fans_num_btn=(Button)view.findViewById(R.id.num_fans_btn);
        follow_num_btn=(Button)view.findViewById(R.id.num_follow_btn);
        OnClick onclick=new OnClick();
        mInfo.setOnClickListener(onclick);
        find_fri_btn.setOnClickListener(onclick);
        fri_num_btn.setOnClickListener(onclick);
        fans_num_btn.setOnClickListener(onclick);
        follow_num_btn.setOnClickListener(onclick);
        return view;
    }

    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Intent intent = new Intent();
            switch (v.getId()){
                case R.id.btn_setting:
                    intent.setClass(getActivity(),SettingActivity.class);
                    break;
                case R.id.addFri_btn:
                    intent.setClass(getActivity(),FindFriendActivity.class);
                    break;
                case R.id.num_friend_btn:
                    intent.setClass(getActivity(),FriendListActivity.class);
                    break;
                case R.id.num_fans_btn:
                    intent.setClass(getActivity(),FansListActivity.class);
                    break;
                case R.id.num_follow_btn:
                    intent.setClass(getActivity(),FollowListActivity.class);
                    break;
            }
            getActivity().startActivity(intent);
        }
    }
}
