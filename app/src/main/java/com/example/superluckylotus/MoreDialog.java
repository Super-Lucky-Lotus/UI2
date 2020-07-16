package com.example.superluckylotus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class MoreDialog  {
    private Context mContext;
    private AlertDialog.Builder builder;

    public MoreDialog(Context context) {
        mContext = context;
        builder = new AlertDialog.Builder(mContext);
    }

    public void popupWindowDialog( View v) {
        // 装载布局文件
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_more, null);
        // 创建PopupWindow对象，添加视图，设置宽高，最后一个参数为设置点击屏幕空白处(按返回键)对话框消失。
        // 也可以用.setFocusable(true);.
        final PopupWindow pWindow = new PopupWindow(view,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        pWindow.setBackgroundDrawable(new BitmapDrawable());// 为了让对话框点击空白处消失，必须有这条语句
        //pWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);// 出现输入法时，重新布局
        //pWindow.setAnimationStyle(R.style.myAnimationstyle);// 设置动画

        Button mPay;
        Button mCollect;
        Button mUninterested;
        mCollect=view.findViewById(R.id.btn_collect);
        mPay=view.findViewById(R.id.btn_pay);
        mUninterested=view.findViewById(R.id.btn_uninterested);
        mCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"收藏",Toast.LENGTH_SHORT);
                //pWindow.dismiss();
            }
        });
        mPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"打赏",Toast.LENGTH_SHORT);
                //pWindow.dismiss();
            }
        });
        mUninterested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"不感兴趣",Toast.LENGTH_SHORT);
                //pWindow.dismiss();
            }
        });
        pWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }


}