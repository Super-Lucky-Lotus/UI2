package com.example.superluckylotus;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

/**
 * @version: 1.0
 * @author: 黄诗雯
 * @className: NearFragment
 * @packageName:com.example.superluckylotus
 * @description: 附近界面
 * @data: 2020.07.11 22:14
 **/
public class NearFragment extends Fragment {


        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_near,null);
            return view;
        }
}