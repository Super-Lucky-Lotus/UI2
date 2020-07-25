package com.example.superluckylotus.Earth;

/**
 * @version: 3.0
 * @author: 宋佳容
 * @className: ShareDialog
 * @packageName:com.example.superluckylotus
 * @description: 增加分享弹窗
 * @data: 2020.07.20 18:00
 **/

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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.superluckylotus.R;

public class ShareDialog  {
    private Context mContext;
    private AlertDialog.Builder builder;

    public ShareDialog(Context context) {
        mContext = context;
        builder = new AlertDialog.Builder(mContext);
    }

    public void popupWindowDialog( View v) {
        // 装载布局文件
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.share_dialog, null);
        // 创建PopupWindow对象，添加视图，设置宽高，最后一个参数为设置点击屏幕空白处(按返回键)对话框消失。
        // 也可以用.setFocusable(true);.
        final PopupWindow pWindow = new PopupWindow(view,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        pWindow.setBackgroundDrawable(new BitmapDrawable());// 为了让对话框点击空白处消失，必须有这条语句
        //pWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);// 出现输入法时，重新布局
        //pWindow.setAnimationStyle(R.style.myAnimationstyle);// 设置动画

        LinearLayout wechat;
        LinearLayout pyq;

        wechat=view.findViewById(R.id.share_wechat);
        pyq=view.findViewById(R.id.share_pyq);


        pWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }


}