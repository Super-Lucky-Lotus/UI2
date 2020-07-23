package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
 * @className: SearchActivity
 * @packageName:com.example.superluckylotus
 * @description: 搜索界面
 * @data: 2020.07.15 18：00
 **/

public class SearchActivity extends AppCompatActivity {

    public EditText search_et;
    private Button search_btn;
    private Button search_back_btn;
    public TextView record_tv1;
    public TextView record_tv2;
    public TextView record_tv3;
    public TextView record_tv4;
    public TextView record_tv5;
    public TextView record_tv6;
    public TextView record_tv7;
    public TextView record_tv8;
    public TextView record_tv9;
    public TextView record_tv10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_et = (EditText) findViewById(R.id.search_edittext);
        search_btn = (Button) findViewById(R.id.search_btn);
        search_back_btn = (Button) findViewById(R.id.search_back);
        record_tv1 = (TextView) findViewById(R.id.record_tv1);
        record_tv2 = (TextView) findViewById(R.id.record_tv2);
        record_tv3 = (TextView) findViewById(R.id.record_tv3);
        record_tv4 = (TextView) findViewById(R.id.record_tv4);
        record_tv5 = (TextView) findViewById(R.id.record_tv5);
        record_tv6 = (TextView) findViewById(R.id.record_tv6);
        record_tv7 = (TextView) findViewById(R.id.record_tv7);
        record_tv8 = (TextView) findViewById(R.id.record_tv8);
        record_tv9 = (TextView) findViewById(R.id.record_tv9);
        record_tv10 = (TextView) findViewById(R.id.record_tv10);

        getData();
        setListeners();
    }

    private void getData() {
        Phone phoneObj = (Phone)getApplication();
        final String phone = phoneObj.getPhone();
        String path = "http://139.219.4.34/searchrecords/";
        Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
        userParams.put("phone",phone);

        HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String result) throws JSONException {
                JSONObject result_json = new JSONObject(result);
                String get = result_json.getString("msg");
                int num = result_json.getInt("num");
                Log.v("SearchActivity", result);
                if (get.equals("success")) {
                    for (int i = 1; i < num; i++) {
                        String record = result_json.getString("Record" + i);
                        if(i == 1){
                            String record1 = "1  "+record;
                            record_tv1.setText(record1);
                        }
                        else if(i == 2){
                            String record2 = "2  "+record;
                            record_tv2.setText(record2);
                        }
                        else if(i == 3){
                            String record3 = "3  "+record;
                            record_tv3.setText(record3);
                        }
                        else if(i == 4){
                            String record4 = "4  "+record;
                            record_tv4.setText(record4);
                        }
                        else if(i == 5){
                            String record5 = "5  "+record;
                            record_tv5.setText(record5);
                        }
                        else if(i == 6){
                            String record6 = "6  "+record;
                            record_tv6.setText(record6);
                        }
                        else if(i == 7){
                            String record7 = "7  "+record;
                            record_tv7.setText(record7);
                        }
                        else if(i == 8){
                            String record8 = "8  "+record;
                            record_tv8.setText(record8);
                        }
                        else if(i == 9){
                            String record9 = "9  "+record;
                            record_tv9.setText(record9);
                        }
                        else if(i == 10) {
                            String record10 = "10  " + record;
                            record_tv10.setText(record10);
                        }
                        Log.v("SearchActivity", record);
                    }
                } else {
                    Log.v("SearchActivity","没有历史记录！");
                }
            }

            @Override
            public void onError(Exception e) {
                Log.v("SearchActivity", "连接失败！");
            }

            @Override
            public void onFinish() {
            }

        });
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        search_btn.setOnClickListener(onClick);
        search_back_btn.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.search_btn:
                    if(!search_et.equals("")) {
                        intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                        intent.putExtra("search", search_et.getText().toString());
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(SearchActivity.this, "请输入你想搜索的内容!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.search_back:
                    intent = new Intent(SearchActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
            }

        }


    }

}