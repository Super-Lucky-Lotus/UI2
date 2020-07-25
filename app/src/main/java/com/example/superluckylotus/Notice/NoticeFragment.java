package com.example.superluckylotus.Notice;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.superluckylotus.R;


/**
 * @version: 1.0
 * @author: 黄诗雯
 * @className: NoticeFragment
 * @packageName:com.example.superluckylotus
 * @description: 消息界面
 * @data: 2020.07.11 16:30
 **/

/**
 * @version: 2.0
 * @author: 宋佳容
 * @className: NoticeFragment
 * @packageName:com.example.superluckylotus
 * @description: 点解上方四个按钮分别跳转到四个界面
 * @data: 2020.07.14 16：10
 **/

public class NoticeFragment extends Fragment {
    private Button fans_Btn;
    private Button likes_Btn;
    private Button at_Btn;
    private Button comment_Btn;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice,null);
        fans_Btn=(Button) view.findViewById(R.id.fans_tab);
        likes_Btn=(Button)view.findViewById(R.id.like_tab);
        at_Btn=(Button)view.findViewById(R.id.at_tab);
        comment_Btn = (Button)view.findViewById(R.id.comment_tab);
        OnClick onclick=new OnClick();
        fans_Btn.setOnClickListener(onclick);
        likes_Btn.setOnClickListener(onclick);
        at_Btn.setOnClickListener(onclick);
        comment_Btn.setOnClickListener(onclick);
        return view;
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Intent intent = new Intent();
            switch (v.getId()){
                case R.id.fans_tab:
                    intent.setClass(getActivity(),NewFansActivity.class);
                    break;
                case R.id.like_tab:
                    intent.setClass(getActivity(),GetLikesActivity.class);
                    break;
                case R.id.at_tab:
                    Intent intent3 = new Intent();
                    intent.setClass(getActivity(),AtMeActivity.class);
                    break;
                case R.id.comment_tab:
                    intent.setClass(getActivity(),CommentActivity.class);
                    break;
            }
            getActivity().startActivity(intent);
        }
    }


}
