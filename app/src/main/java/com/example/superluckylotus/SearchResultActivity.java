package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static Handler uiHandler;
    private ListView list_search_result;
    public static MyAdapter adapter;
    public static List<SearchInfo> Searchs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        search_edit = (EditText) findViewById(R.id.search_result_tv);
        search_btn=(Button)findViewById(R.id.check_search_btn);
        result_back_btn=(Button)findViewById(R.id.search_result_back);

        list_search_result=(ListView) findViewById(R.id.list_search_result);

        getData();
        setListeners();
    }

    private void getData() {

        Intent intent = getIntent();
        final String search = intent.getStringExtra("search");
        search_edit.setText(search);

        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated constructor stub
                Searchs = new ArrayList<SearchInfo>();

                Phone phoneObj = (Phone) getApplication();
                final String phone = phoneObj.getPhone();

                String path = "http://139.219.4.34/searchvideo/";
                Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
                userParams.put("text", search);
                userParams.put("phone", phone);


                HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                    @Override
                    public void onSuccess(String result) throws JSONException {
                        JSONObject result_json = new JSONObject(result);
                        String get = result_json.getString("msg");

                        Log.v("SearchResultActivity", result);
                        if (get.equals("success")) {
                        int num = result_json.getInt("num");
                            for (int i = 1; i < num; i++) {
                                String username = result_json.getString("Video" + i + "Username");
                                String description = result_json.getString("Video" + i + "Desc");
                                String time = result_json.getString("Video" + i + "Time");
                                String tag = result_json.getString("Video" + i + "Label");
                                String cover_path = result_json.getString("Video" + i + "Cover");
                                String like_count = result_json.getString("Video" + i + "Likecount");
                                String state = result_json.getString("Video" + i + "Likestate" );
                                SearchInfo search = new SearchInfo(username, time, tag, description, cover_path, like_count,state);
                                Searchs.add(search);

                            }
                        } else {
                            Toast.makeText(SearchResultActivity.this, "没有搜到相关信息!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.v("SearchResultActivity", "连接失败！");
                    }

                    @Override
                    public void onFinish() {
                        // 子线程执行完毕的地方，利用主线程的handler发送消息
                        Message msg2 = new Message();
                        msg2.what = 1;
                        uiHandler.sendMessage(msg2);
                    }
                });
            }
            }.start();
        uiHandler = new Handler() {
            // 覆写这个方法，接收并处理消息。
            @Override
            public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 1:
                            Log.v("Thread", "finished");
                            adapter = new MyAdapter(Searchs);
                            list_search_result.setAdapter(adapter);
                            break;
                    }
                }
        };
    }

    public  class MyAdapter extends BaseAdapter {
        private List<SearchInfo> infos;
        private LayoutInflater inflater;

        public MyAdapter(List<SearchInfo> infos) {
            super();
            this.infos = infos;
            inflater = (LayoutInflater) SearchResultActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public Object getItem(int position) {

            return infos.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            int i=0;
            View view = inflater.inflate(R.layout.item_search, null);
            final TextView username = (TextView) view.findViewById(R.id.search_name);
            final TextView time = (TextView) view.findViewById(R.id.search_time);
            final TextView tag = (TextView) view.findViewById(R.id.search_tage);
            final TextView description = (TextView) view.findViewById(R.id.search_description);
            final MyImageView video = (MyImageView) view.findViewById(R.id.search_video);
            final TextView like_count = (TextView) view.findViewById(R.id.search_like_count);

            final SearchInfo info = infos.get(position);
            username.setText("  "+info.getName());
            time.setText("  "+info.getTime());
            tag.setText("  "+info.getTag());
            description.setText("  "+info.getDetail());
            video.setImageURL(info.getCover_path());
            like_count.setText("  "+info.getLike_count());

            video.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = null;
                    intent = new Intent(SearchResultActivity.this, SingleVideoActivity.class);
                    intent.putExtra("pic_url",info.getCover_path());
                    startActivity(intent);
                }
            });

            return view;
        }
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