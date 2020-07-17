package com.example.superluckylotus;

import android.app.Fragment;
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
 * @className: NearFragment
 * @packageName:com.example.superluckylotus
 * @description: 附近界面
 * @data: 2020.07.11 22:14
 **/

/**
 * @version: 2.0
 * @author: 黄诗雯
 * @className: NearFragment
 * @packageName:com.example.superluckylotus
 * @description: 更多弹窗和评论弹窗
 * @data: 2020.07.17 15:29
 **/
public class NearFragment extends Fragment {

    private Button mMore;
    private Button mComment;
    MoreDialog md;
    CommentDialog cd;


        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_near,null);
            mMore=view.findViewById(R.id.more);
            mComment=view.findViewById(R.id.comment);
            md=new MoreDialog(getActivity());
            cd=new CommentDialog(getActivity());
            NearFragment.OnClick onclick=new NearFragment.OnClick();
            mMore.setOnClickListener(onclick);
            mComment.setOnClickListener(onclick);
            return view;
        }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Intent intent = new Intent();
            switch (v.getId()){
                case R.id.more:
                    md.popupWindowDialog( v);
                    break;
                case R.id.comment:
                    cd.popupWindowDialog( v);
                    break;
            }
        }
    }
}