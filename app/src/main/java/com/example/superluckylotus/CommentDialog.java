package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mob.tools.utils.DeviceHelper.getApplication;

/**
 * @version: 1.0
 * @author: 黄诗雯
 * @className: EarthFragment
 * @packageName:com.example.superluckylotus
 * @description: 评论弹窗
 * @data: 2020.07.17 15:27
 **/



public class CommentDialog  {
    private Context mContext;
    private AlertDialog.Builder builder;

    public CommentDialog(Context context) {
        mContext = context;
        builder = new AlertDialog.Builder(mContext);
    }

    public void popupWindowDialog( View v) {
        // 装载布局文件
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_comment, null);
        // 创建PopupWindow对象，添加视图，设置宽高，最后一个参数为设置点击屏幕空白处(按返回键)对话框消失。
        // 也可以用.setFocusable(true);.
        final PopupWindow pWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pWindow.setBackgroundDrawable(new BitmapDrawable());// 为了让对话框点击空白处消失，必须有这条语句
        pWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);// 出现输入法时，重新布局
        //pWindow.setAnimationStyle(R.style.myAnimationstyle);// 设置动画
        pWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);

        Button mExit;
        mExit=view.findViewById(R.id.btn_exit);
        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWindow.dismiss();
            }

        });

        getComment("media\\video\\faf0fb3837.mp4");
        addComment("太可爱了！","media\\video\\faf0fb3837.mp4");
    }

    private void getComment(String vediourl) {
        String path = "http://139.219.4.34/getcomments/";
        Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
        userParams.put("video_path",vediourl);

        HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String result) throws JSONException {
                //JSONObject result_json = new JSONObject(result);
                //String get = result_json.getString("msg");
                //int num = result_json.getInt("num");
                Log.v("CommentDialog", result);
                //if (get.equals("success")) {
                //  for (int i = 1; i < num; i++) {
                //    String username = result_json.getString("Fan" + i + "Name");
                //  String time = result_json.getString("time" + i);
                //Log.v("GetLikesActivity", username);
                // }
                //} else {
                //  Log.v("CommentDialog","没有评论！");
                //}
            }

            @Override
            public void onError(Exception e) {
                Log.v("CommentDialog", "连接失败！");
            }

            @Override
            public void onFinish() {
            }

        });
    }

    private void addComment(String text,String vediourl) {
        Phone phoneObj = (Phone)getApplication();
        final String phone = phoneObj.getPhone();
        String path = "http://139.219.4.34/addcomment/";
        Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
        userParams.put("phone",phone);
        userParams.put("text",text);
        userParams.put("video_path",vediourl);

        HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String result) throws JSONException {
                //JSONObject result_json = new JSONObject(result);
                //String get = result_json.getString("msg");
                //int num = result_json.getInt("num");
                Log.v("CommentDialog", result);
                //if (get.equals("success")) {
                //  for (int i = 1; i < num; i++) {
                //    String username = result_json.getString("Fan" + i + "Name");
                //  String time = result_json.getString("time" + i);
                //Log.v("GetLikesActivity", username);
                // }
                //} else {
                //  Log.v("CommentDialog","没有评论！");
                //}
            }

            @Override
            public void onError(Exception e) {
                Log.v("CommentDialog", "连接失败！");
            }

            @Override
            public void onFinish() {
            }

        });
    }


}