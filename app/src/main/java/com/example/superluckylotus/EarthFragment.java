package com.example.superluckylotus;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;


/**
 * @version: 1.0
 * @author: 黄诗雯
 * @className: EarthFragment
 * @packageName:com.example.superluckylotus
 * @description: 广场界面
 * @data: 2020.07.10 15:12
 **/

/**
 * @version: 2.0
 * @author: 宋佳容
 * @className: EarthFragment
 * @packageName:com.example.superluckylotus
 * @description: 跳转到搜索界面
 * @data: 2020.07.10 15:12
 **/

public class EarthFragment extends Fragment {

    private Button turnSearchPage_btn;
    private Button mMore;
    MoreDialog md;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_earth,null);
        turnSearchPage_btn = (Button)view.findViewById(R.id.turnSearchPage_btn);
        mMore=view.findViewById(R.id.more);
        md=new MoreDialog(getActivity());
        OnClick onclick=new OnClick();
        turnSearchPage_btn.setOnClickListener(onclick);
        mMore.setOnClickListener(onclick);
        return view;
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Intent intent = new Intent();
            switch (v.getId()){
                case R.id.turnSearchPage_btn:
                    intent.setClass(getActivity(),SearchActivity.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.more:
                    md.popupWindowDialog( v);
                    break;
            }
        }
    }


}