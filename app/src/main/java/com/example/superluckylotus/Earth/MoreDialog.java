package com.example.superluckylotus.Earth;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.superluckylotus.Manager.HttpServer;
import com.example.superluckylotus.Phone;
import com.example.superluckylotus.R;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import static com.mob.tools.utils.DeviceHelper.getApplication;

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
        final ToggleButton mCollect;
        Button mUninterested;
        Phone phoneObj = (Phone)getApplication();
        final String phone = phoneObj.getPhone();
        mCollect=view.findViewById(R.id.btn_collect);
        mPay=view.findViewById(R.id.btn_pay);
        mUninterested=view.findViewById(R.id.btn_uninterested);
        mCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path;
                if(mCollect.isChecked()) {
                    path = "http://139.219.4.34/addfavorite/";
                    mCollect.setChecked(true);
                }
                else {
                    path="http://139.219.4.34/removefavorite/";
                    mCollect.setChecked(false);
                }

                Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
                userParams.put("video_path", "media\\video\\faf0fb3837.mp4");
                userParams.put("phone", phone);

                HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                    @Override
                    public void onSuccess(String result) throws JSONException {
                        //JSONObject result_json = new JSONObject(result);
                        //String get = result_json.getString("msg");
                        //int num = result_json.getInt("num");
                        Log.v("MoreDialog", result);
                        // if (get.equals("success")) {
                        // for (int i = 1; i < num; i++) {
                        //   String username = result_json.getString("User" + i + "Name");
                        // String description = result_json.getString("User" + i + "Description");
                        // String time = result_json.getString("User" + i + "Time");
                        // String tage = result_json.getString("User" + i + "Tage");
                        //}
                        //} else {
                        //  Toast.makeText(SearchResultActivity.this, "没有搜到相关信息!", Toast.LENGTH_SHORT).show();
                        // }
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.v("MoreDialog", "连接失败！");


                    }

                    @Override
                    public void onFinish() {
                    }

                });
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