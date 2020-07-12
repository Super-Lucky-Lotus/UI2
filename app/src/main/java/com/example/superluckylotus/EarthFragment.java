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
 * @className: EarthFragment
 * @packageName:com.example.superluckylotus
 * @description: 广场界面
 * @data: 2020.07.10 15:12
 **/

public class EarthFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_earth,null);
        return view;
    }



}