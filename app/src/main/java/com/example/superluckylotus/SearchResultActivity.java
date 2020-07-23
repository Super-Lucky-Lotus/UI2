package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mob.tools.utils.DeviceHelper.getApplication;

/**
 * @version: 2.0
 * @author: 宋佳容
 * @className: SearchResultActivity
 * @packageName:com.example.superluckylotus
 * @description: 搜索结果界面
 * @data: 2020.07.15 20：10
 **/

public class SearchResultActivity extends AppCompatActivity {

    public EditText search_edit;
    private Button search_btn;
    private Button result_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        search_edit = (EditText) findViewById(R.id.search_result_tv);
        search_btn=(Button)findViewById(R.id.check_search_btn);
        result_back_btn=(Button)findViewById(R.id.search_result_back);

        getData();
        setListeners();
    }

    private void getData() {
        Phone phoneObj = (Phone)getApplication();
        final String phone = phoneObj.getPhone();
        Intent intent = getIntent();
        String search = intent.getStringExtra("search");
        search_edit.setText(search);
        String path = "http://139.219.4.34/searchvideo/";
        Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
        userParams.put("text", search);
        userParams.put("phone", phone);


        HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String result) throws JSONException {
                //JSONObject result_json = new JSONObject(result);
                //String get = result_json.getString("msg");
                //int num = result_json.getInt("num");
                Log.v("SearchResultActivity", result);
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
                Log.v("SearchResultActivity", "连接失败！");
            }

            @Override
            public void onFinish() {
            }

        });

    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        search_btn.setOnClickListener(onClick);
        result_back_btn.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.check_search_btn:
                    if(!search_edit.getText().toString().equals("")) {
                        intent = new Intent(SearchResultActivity.this, SearchResultActivity.class);
                        intent.putExtra("search", search_edit.getText().toString());
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(SearchResultActivity.this, "请输入你想输入的内容!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.search_result_back:
                    intent = new Intent(SearchResultActivity.this,SearchActivity.class);
                    startActivity(intent);
                    break;
            }

        }



    }

}