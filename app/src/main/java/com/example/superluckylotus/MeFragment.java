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

import androidx.annotation.Nullable;


/**
 * @version: 1.0
 * @author: 黄诗雯
 * @className: MeFragment
 * @packageName:com.example.superluckylotus
 * @description: 我的界面
 * @data: 2020.07.12 00:18
 **/

public class MeFragment extends Fragment {

    private Button mInfo;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me,null);
        mInfo=(Button) view.findViewById(R.id.btn_setting);
        OnClick onclick=new OnClick();
        mInfo.setOnClickListener(onclick);
        return view;
    }

    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Intent intent = new Intent();
            intent.setClass(getActivity(),SettingActivity.class);
            getActivity().startActivity(intent);
        }
    }
}
