package com.example.superluckylotus;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
    private Button fans_num_btn;
    private Button follow_num_btn;
    private ImageView video1_btn;
    private ImageView video2_btn;
    private ImageView video3_btn;
    private ImageView video4_btn;
    private ImageView video5_btn;
    private ImageView video6_btn;
    private ImageView video7_btn;
    private ImageView video8_btn;
    private ImageView video9_btn;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, null);
        mInfo = (Button) view.findViewById(R.id.btn_setting);
        find_fri_btn = (Button) view.findViewById(R.id.addFri_btn);
        fri_num_btn = (Button) view.findViewById(R.id.num_friend_btn);
        fans_num_btn = (Button) view.findViewById(R.id.num_fans_btn);
        follow_num_btn = (Button) view.findViewById(R.id.num_follow_btn);
        video1_btn = (ImageView) view.findViewById(R.id.video1);
        video2_btn = (ImageView) view.findViewById(R.id.video2);
        video3_btn = (ImageView) view.findViewById(R.id.video3);
        video4_btn = (ImageView) view.findViewById(R.id.video4);
        video5_btn = (ImageView) view.findViewById(R.id.video5);
        video6_btn = (ImageView) view.findViewById(R.id.video6);
        video7_btn = (ImageView) view.findViewById(R.id.video7);
        video8_btn = (ImageView) view.findViewById(R.id.video8);
        video9_btn = (ImageView) view.findViewById(R.id.video9);
        OnClick onclick = new OnClick();
        mInfo.setOnClickListener(onclick);
        find_fri_btn.setOnClickListener(onclick);
        fri_num_btn.setOnClickListener(onclick);
        fans_num_btn.setOnClickListener(onclick);
        follow_num_btn.setOnClickListener(onclick);
        video1_btn.setOnClickListener(onclick);
        return view;
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
                case R.id.video1:
                case R.id.video2:
                case R.id.video3:
                case R.id.video4:
                case R.id.video5:
                case R.id.video6:
                case R.id.video7:
                case R.id.video8:
                case R.id.video9:
                    intent.setClass(getActivity(), SingleVideoActivity.class);
                    break;
            }
            getActivity().startActivity(intent);
        }
    }
}
