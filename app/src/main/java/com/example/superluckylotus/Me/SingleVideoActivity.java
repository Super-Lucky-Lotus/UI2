package com.example.superluckylotus.Me;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.example.superluckylotus.Earth.CommentDialog;
import com.example.superluckylotus.Earth.MoreDialog;
import com.example.superluckylotus.Earth.ShareDialog;
import com.example.superluckylotus.Manager.HttpServer;
import com.example.superluckylotus.Phone;
import com.example.superluckylotus.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @version: 2.0
 * @author: 宋佳容
 * @className: SingleVideoActivity
 * @packageName:com.example.superluckylotus
 * @description: 单个视频界面
 * @data: 2020.07.17 16:05
 **/

public class SingleVideoActivity extends AppCompatActivity {

    private Button close_sv_btn;
    private String video;

    private static final String TAG ="SingleVideoActivity";
    private VideoView single_video;

    private TextView tv_username;
    private TextView tv_tab;
    private TextView tv_detail;
    private TextView tv_bgm;

    private ToggleButton tb_like;
    private Button btn_comment;
    private Button btn_share;
    private Button btn_more;

    MoreDialog md;
    CommentDialog cd;
    ShareDialog sd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_video);

        tb_like = (ToggleButton) findViewById(R.id.sv_like);
        btn_comment = (Button) findViewById(R.id.sv_comment);
        btn_share = (Button) findViewById(R.id.sv_share);
        btn_more = (Button) findViewById(R.id.sv_more);

        single_video = (VideoView) findViewById(R.id.single_video);

        close_sv_btn = (Button) findViewById(R.id.close_sv);

        md=new MoreDialog(SingleVideoActivity.this);
        cd=new CommentDialog(SingleVideoActivity.this);
        sd=new ShareDialog(SingleVideoActivity.this);

        tv_username = (TextView) findViewById(R.id.textView_username);
        tv_tab = (TextView) findViewById(R.id.textView_tab);
        tv_detail = (TextView) findViewById(R.id.textView_detail);
        tv_bgm = (TextView) findViewById(R.id.textView_bgm);
        getData();

        setListeners();
    }

    private void getData(){
        Intent intent = getIntent();
        String pic_url2 = intent.getStringExtra("pic_url");
        String pic_url3 = pic_url2.substring(19);
        Log.v("SingleVideoActivity",pic_url3);

        Phone phoneObj = (Phone)getApplication();
        final String phone = phoneObj.getPhone();
        String path = "http://139.219.4.34/getvideo/";
        Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
        userParams.put("cover_path", pic_url3);
        userParams.put("phone", phone);
        //设置视频控制器
        //single_video.setMediaController(new MediaController(this));

        HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String result) throws JSONException {
                JSONObject result_json = new JSONObject(result);
                String get = result_json.getString("msg");
                Log.v("SingleVideoActivity",result);
                if (get.equals("success")) {
                    String username = result_json.getString("Username");
                    String tab = result_json.getString("Label");
                    String detail = result_json.getString("Description");
                    String like = result_json.getString("IfLike");
                    video = result_json.getString("Path");
                    String pic_url = video.substring(19);
                    String path2 = "http://139.219.4.34/";
                    video = path2.concat(pic_url);
                    video = video.replaceAll("\\\\","/");
                    Log.v(TAG,"123456789:"+video);
                    Uri uri = Uri.parse(video);
                    single_video.setVideoURI(uri);
                    single_video.requestFocus();
                    single_video.start();
                    if(like.equals("true")){
                        tb_like.setChecked(true);
                    }
                    else{
                        tb_like.setChecked(false);
                    }
                    tv_username.setText(username);
                    tv_tab.setText(tab);
                    tv_detail.setText(detail);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.v("SingleVideoActivity", "连接失败！");
            }

            @Override
            public void onFinish() {
            }

        });
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        close_sv_btn.setOnClickListener(onClick);
        tb_like.setOnClickListener(onClick);
        btn_more.setOnClickListener(onClick);
        btn_share.setOnClickListener(onClick);
        btn_comment.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.close_sv:
                    finish();
                    break;
                case R.id.sv_like:
                    Phone phoneObj = (Phone)getApplication();
                    final String phone = phoneObj.getPhone();
                    String path;
                    if(tb_like.isChecked()) {
                        path = "http://139.219.4.34/cancellike/";
                    }
                    else{
                        path = "http://139.219.4.34/addlike/";
                    }
                    Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
                    userParams.put("phone",phone);
                    userParams.put("video_path","media\\video\\faf0fb3837.mp4");

                    HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                        @Override
                        public void onSuccess(String result) throws JSONException {
                            JSONObject result_json = new JSONObject(result);
                            String get = result_json.getString("msg");
                            //int num = result_json.getInt("num");
                            Log.v("SingleVideoActivity", result);
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
                            Log.v("SingleVideoActivity", "连接失败！");
                        }

                        @Override
                        public void onFinish() {
                        }

                    });
                    break;
                case R.id.sv_more:
                    md.popupWindowDialog(v);
                    break;
                case R.id.sv_share:
                    sd.popupWindowDialog(v);
                    break;
                case R.id.sv_comment:
                    cd.popupWindowDialog(v);
                    break;
            }
        }
    }

}