package com.example.superluckylotus.Me;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.superluckylotus.ItemInfo.ImageInfo;
import com.example.superluckylotus.Manager.HttpServer;
import com.example.superluckylotus.Manager.MyImageView;
import com.example.superluckylotus.Phone;
import com.example.superluckylotus.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static com.mob.tools.utils.DeviceHelper.getApplication;


/**
 * @version: 1.0
 * @author: 黄诗雯
 * @className: MeFragment
 * @packageName:com.example.superluckylotus
 * @description: 我的界面
 * @data: 2020.07.12 00:18
 **/

/**
 * @version: 2.0
 * @author: 宋佳容
 * @className: MeFragment
 * @packageName:com.example.superluckylotus
 * @description: 增加好友数 和添加好友按钮的跳转
 * @data: 2020.07.16 16:05
 **/
/**
 * @version: 2.0
 * @author: 黄坤
 * @className: MeFragment
 * @packageName:com.example.superluckylotus
 * @description: 显示对应用户的个人信息
 * @data: 2020.07.16 18:05
 **/

public class MeFragment extends Fragment {

    private static final String TAG = "MeFragment";
    protected String nickname="用户名";
    protected String likes_counts ="0";
    protected String fans_counts = "0";
    protected String follows_counts = "0";
    protected String friends_counts = "0";
    protected String id = "";
    protected String photo ="";

    private Button mInfo;
    private Button find_fri_btn;
    private Button fri_num_btn;
    private Button fans_num_btn;
    private Button follow_num_btn;

    private Button btn_my_collections;
    private Button btn_my_videos;
    private Button btn_my_likes;

    protected TextView textView1;
    protected TextView textView2;
    protected TextView textView3;
    protected TextView textView4;
    protected TextView textView5;
    protected TextView textView6;

    private MyImageView My_pic;

    public static Handler uiHandler;
    private ListView list_image;
    public static MyAdapter adapter;
    public static List<ImageInfo> Images;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, null);
        textView1 = (TextView) view.findViewById(R.id.tv_username);
        textView2 = (TextView) view.findViewById(R.id.tv_userID);
        textView3 = (TextView) view.findViewById(R.id.num_like);
        textView4 = (TextView) view.findViewById(R.id.num_follow);
        textView5 = (TextView) view.findViewById(R.id.num_fans);
        textView6 = (TextView) view.findViewById(R.id.num_friends);
        mInfo = (Button) view.findViewById(R.id.btn_setting);
        find_fri_btn = (Button) view.findViewById(R.id.addFri_btn);
        fri_num_btn = (Button) view.findViewById(R.id.num_friend_btn);
        fans_num_btn = (Button) view.findViewById(R.id.num_fans_btn);
        follow_num_btn = (Button) view.findViewById(R.id.num_follow_btn);
        My_pic = (MyImageView) view.findViewById(R.id.photo);

        btn_my_collections = (Button) view.findViewById(R.id.btn_my_collections);
        btn_my_videos = (Button) view.findViewById(R.id.btn_my_videos);
        btn_my_likes = (Button) view.findViewById(R.id.btn_my_likes);

        OnClick onclick = new OnClick();

        mInfo.setOnClickListener(onclick);
        find_fri_btn.setOnClickListener(onclick);
        fri_num_btn.setOnClickListener(onclick);
        fans_num_btn.setOnClickListener(onclick);
        follow_num_btn.setOnClickListener(onclick);

        btn_my_likes.setOnClickListener(onclick);
        btn_my_videos.setOnClickListener(onclick);
        btn_my_collections.setOnClickListener(onclick);

        list_image=(ListView) view.findViewById(R.id.list_images);
        getMyVideo("my");
        start();
        return view;
    }

    private void getMyVideo(final String kind) {
        Images = new ArrayList<ImageInfo>();
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated constructor stub
                Images = new ArrayList<ImageInfo>();

                Phone phoneObj = (Phone) getApplication();
                final String phone = phoneObj.getPhone();

                String path;
                if(kind.equals("my")){
                    path = "http://139.219.4.34/myvideo/";
                }
                else if(kind.equals("collect")){
                    path = "http://139.219.4.34/collectvideo/";
                }
                else{
                    path = "http://139.219.4.34/likevideo/";
                }
                Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
                userParams.put("phone", phone);

                if (!phone.equals("")) {
                    HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                        @Override
                        public void onSuccess(String result) throws JSONException {
                            JSONObject result_json = new JSONObject(result);
                            String back = result_json.getString("msg");
                            Log.v("MeFragment_MyVideo", result);
                            if (back.equals("success")) {
                                int num = result_json.getInt("num");
                                for (int i = 1; i < num; i=i+3) {
                                    String image_url1 = result_json.getString("Image" + i);
                                    String image_url2 = "";
                                    String image_url3 = "";
                                    if(i+1<num){
                                        int j = i+1;
                                        image_url2 = result_json.getString("Image" + j);
                                    }
                                    if(i+2<num){
                                        int k =i+2;
                                        image_url3 = result_json.getString("Image" + k);
                                    }
                                    ImageInfo image = new ImageInfo(image_url1,image_url2,image_url3);
                                    Images.add(image);
                                }
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.v("MeFragment_MyVideo", "连接失败！");
                        }

                        @Override
                        public void onFinish() {
                            // 子线程执行完毕的地方，利用主线程的handler发送消息
                            Message msg = new Message();
                            msg.what = 1;
                            uiHandler.sendMessage(msg);
                        }
                    });
                }
            }
        }.start();
        Log.v("GetUsers", "Next");
        uiHandler = new Handler() {
            // 覆写这个方法，接收并处理消息。
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        Log.v("Thread", "finished");
                        adapter = new MyAdapter(Images);
                        list_image.setAdapter(adapter);
                        break;
                }

            }
        };
    }

    public  class MyAdapter extends BaseAdapter {
        private List<ImageInfo> infos;
        private LayoutInflater inflater;

        public MyAdapter(List<ImageInfo> infos) {
            super();
            this.infos = infos;
            inflater = getActivity().getLayoutInflater();
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

            View view = inflater.inflate(R.layout.item_image, null);
            final MyImageView video1 = (MyImageView) view.findViewById(R.id.video1);
            final MyImageView video2 = (MyImageView) view.findViewById(R.id.video2);
            final MyImageView video3 = (MyImageView) view.findViewById(R.id.video3);
            ImageInfo info1 = infos.get(position);

            String path1 = info1.getImage_url1();
            String path2 = info1.getImage_url2();
            String path3 = info1.getImage_url3();
            if(!path1.equals("")) {
                //Bitmap bitmap = getVideoThumbnail(path);
                video1.setImageURL(path1);
                final ImageInfo finalInfo = info1;
                video1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Intent intent = null;
                        intent = new Intent(getActivity(), SingleVideoActivity.class);
                        intent.putExtra("pic_url", finalInfo.getImage_url1());
                        startActivity(intent);
                    }
                });
            }
            else{
                video1.setVisibility(GONE);
            }
            if(!path2.equals("")) {
                //Bitmap bitmap = getVideoThumbnail(path);
                video2.setImageURL(path2);
                final ImageInfo finalInfo = info1;
                video2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Intent intent = null;
                        intent = new Intent(getActivity(), SingleVideoActivity.class);
                        intent.putExtra("pic_url", finalInfo.getImage_url2());
                        startActivity(intent);
                    }
                });
            }
            else{
                video2.setVisibility(GONE);
            }
            if(!path3.equals("")) {
                //Bitmap bitmap = getVideoThumbnail(path);
                video3.setImageURL(path3);

                final ImageInfo finalInfo = info1;
                video3.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Intent intent = null;
                        intent = new Intent(getActivity(), SingleVideoActivity.class);
                        intent.putExtra("pic_url", finalInfo.getImage_url3());
                        startActivity(intent);
                    }
                });
            }
            else{
                video3.setVisibility(GONE);
            }
            return view;
        }
    }

    // 获取视频缩略图
    public Bitmap getVideoThumbnail(String filePath) {
        Bitmap b=null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            b=retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    private void start() {
        Phone phoneObj = (Phone)getApplication();
        final String phone = phoneObj.getPhone();
        String path = "http://139.219.4.34/me/";
        Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
        userParams.put("phone", phone);

        if(!phone.equals("")) {
            HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                @Override
                public void onSuccess(String result) throws JSONException {
                    JSONObject result_json = new JSONObject(result);
                    String me = result_json.getString("msg");
                    Log.v(TAG,result);
                    if (me.equals("success")) {
                        photo =result_json.getString("face_image");
                        nickname = result_json.getString("nickname");
                        id = result_json.getString("id");
                        fans_counts = result_json.getString("fans_counts");
                        follows_counts = result_json.getString("follow_counts");
                        friends_counts = result_json.getString("friends_counts");
                        likes_counts = result_json.getString("receive_like_counts");
                        if(!photo.equals("")) {
                            My_pic.setImageURL(photo);
                        }
                        textView1.setText(nickname);
                        textView2.setText(id);
                        textView3.setText(likes_counts);
                        textView4.setText(follows_counts);
                        textView5.setText(fans_counts);
                        textView6.setText(friends_counts);
                    }
                }

                @Override
                public void onError(Exception e) {
                    Log.v(TAG,"连接失败！");
                }

                @Override
                public void onFinish() {
                }

            });
        }
    }


    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.btn_setting:
                    intent.setClass(getActivity(), SettingActivity.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.addFri_btn:
                    intent.setClass(getActivity(), FindFriendActivity.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.num_friend_btn:
                    intent.setClass(getActivity(), FriendListActivity.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.num_fans_btn:
                    intent.setClass(getActivity(), FansListActivity.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.num_follow_btn:
                    intent.setClass(getActivity(), FollowListActivity.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.btn_my_collections:
                    getMyVideo("collect");
                    break;
                case R.id.btn_my_videos:
                    getMyVideo("my");
                    break;
                case R.id.btn_my_likes:
                    getMyVideo("like");
                    break;

            }
        }
    }
}
