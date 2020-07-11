package com.example.superluckylotus;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;



public class NoticeFragment extends Fragment {
    private Button mFans;
    private Button mLikes;
    private Button mAt;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice,null);
        return view;
    }

    public void onViewCreated(View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        mFans=view.findViewById(R.id.fans_tab);
        mLikes=view.findViewById(R.id.like_tab);
        mAt=view.findViewById(R.id.at_tab);
    }


}
