package com.example.superluckylotus.Manager;

import android.view.View;

public interface OnViewPagerListener {
    //停止播放的监听
    void onPageRelease(boolean isNest, View position);

    //播放的监听
    void onPageSelected(boolean isButten, View position);

    //加载监听
    void onaddVideos(String[] a,String[] b,String[] c,String[] d, View position, int pos);
}
