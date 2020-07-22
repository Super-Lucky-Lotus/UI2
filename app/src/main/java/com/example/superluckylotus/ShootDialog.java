package com.example.superluckylotus;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class ShootDialog {

    private Context mContext;
    private AlertDialog.Builder builder;

    public ShootDialog(Context context) {
        mContext = context;
        builder = new AlertDialog.Builder(mContext);
    }

    public void popupWindowDialog( View v) {
        // 装载布局文件
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_shoot, null);
        // 创建PopupWindow对象，添加视图，设置宽高，最后一个参数为设置点击屏幕空白处(按返回键)对话框消失。
        // 也可以用.setFocusable(true);.
        final PopupWindow pWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pWindow.setBackgroundDrawable(new BitmapDrawable());// 为了让对话框点击空白处消失，必须有这条语句
        //pWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);// 出现输入法时，重新布局
        //pWindow.setAnimationStyle(R.style.myAnimationstyle);// 设置动画

        LinearLayout shoot;
        LinearLayout sketch;
        LinearLayout release;

        shoot=view.findViewById(R.id.add_shoot);
        sketch=view.findViewById(R.id.add_sketch);
        release=view.findViewById(R.id.add_release);


        pWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }
}
