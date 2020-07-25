package com.example.superluckylotus;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.mob.tools.utils.DeviceHelper.getApplication;

/**
 * @version: 1.0
 * @author: 黄诗雯
 * @className: NearFragment
 * @packageName:com.example.superluckylotus
 * @description: 附近界面
 * @data: 2020.07.11 22:14
 **/

/**
 * @version: 2.0
 * @author: 黄诗雯
 * @className: NearFragment
 * @packageName:com.example.superluckylotus
 * @description: 更多弹窗和评论弹窗
 * @data: 2020.07.17 15:29
 **/

/**
 * @version: 3.0
 * @author: 宋佳容
 * @className: NearFragment
 * @packageName:com.example.superluckylotus
 * @description: 修改打赏功能，增加分享弹窗
 * @data: 2020.07.20 18:19
 **/

public class NearFragment extends Fragment {

    private Button mMore;
    private Button mComment;
    private  Button mShare;
    private TextView mCity;
    MoreDialog md;
    CommentDialog cd;
    ShareDialog sd;
    String[] videos;
    private RecyclerView recyclerView;

    private DouYinLayoutManager douYinLayoutManager;


        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

            videos = new String[]{"http://139.219.4.34/media\\video\\b31855c05f.mp4", "http://139.219.4.34/media\\video\\b31855c05f.mp4",
                    "http://139.219.4.34/media\\video\\b31855c05f.mp4", "http://139.219.4.34/media\\video\\b31855c05f.mp4"};
            String path = "http://139.219.4.34/getfollowvideo/";
            Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
            Phone phoneObj = (Phone)getApplication();
            final String phone = phoneObj.getPhone();
            userParams.put("phone",phone);
            HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                @Override
                public void onSuccess(String result) throws JSONException {
                    JSONObject result_json = new JSONObject(result);
                    String get = result_json.getString("msg");
                    //int num = result_json.getInt("num");
                    Log.v("EarthFragment", result);
                    if (get.equals("success")){
                        String videoArray = result_json.getString("videos");
                        Log.v(TAG,"123456789:"+videoArray);
                        videoArray = videoArray.substring(1,videoArray.length()-1);
                        String[] loaclvideos = videoArray.split(",");
                        for (int i = 0 ;i <loaclvideos.length;i++){
                            loaclvideos[i]=loaclvideos[i].replaceFirst("\\\\\\\\","//");
                            loaclvideos[i]=loaclvideos[i].replaceFirst("\\\\\\\\","/");
                            loaclvideos[i]=loaclvideos[i].replaceAll("\\\\\\\\","/");
                            loaclvideos[i]=loaclvideos[i].substring(1,loaclvideos[i].length()-1);
                            Log.v(TAG,"123456789:"+loaclvideos[i]);
                        }
                        videos = loaclvideos;
                    }
                }
                @Override
                public void onError(Exception e) {
                    Log.v("EarthFragment", "连接失败！");
                }
                @Override
                public void onFinish() {
                }
            });

            View view = inflater.inflate(R.layout.fragment_near,null);
            mCity=view.findViewById(R.id.tv_city);
            mCity.setText(MainActivity.city);
            md=new MoreDialog(getActivity());
            cd=new CommentDialog(getActivity());
            sd=new ShareDialog(getActivity());
            NearFragment.OnClick onclick=new NearFragment.OnClick();
            recyclerView = view.findViewById(R.id.near_recyclerView_dy);
            initView();
            return view;
        }


    private void initView() {
        douYinLayoutManager = new DouYinLayoutManager(getActivity(), OrientationHelper.VERTICAL,false);
        recyclerView.setLayoutManager(douYinLayoutManager);
        recyclerView.setAdapter(new MyAdapter());

        douYinLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onPageRelease(boolean isNest, View position) {
                releaseVideo(position);

            }

            @Override
            public void onPageSelected(boolean isButten, View position) {
                int index = 0;
                if (isButten){
                    index = 0;
                }else {
                    index = 1;
                }
                playVideo(position);

            }


            public void onaddVideos(String[] ChangeVideos,View position,int pos) {
                addVideos(ChangeVideos,position,pos);
            }
        });
    }



    public void addlist(String[] a){
        String[] c= new String[a.length+videos.length];

        System.arraycopy(videos, 0, c, 0, videos.length);
        System.arraycopy(a, 0, c, videos.length, a.length);
        videos = c;

    }

    public void reInit(String[] a){
        videos = a;
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        public MyAdapter(){
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video,parent,false);
            mMore=view.findViewById(R.id.video_more);
            mComment=view.findViewById(R.id.video_comment);
            mShare=view.findViewById(R.id.video_share);
            mMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    md.popupWindowDialog(view);
                }
            });
            mComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cd.popupWindowDialog(view);
                }
            });
            mShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sd.popupWindowDialog(view);
                }
            });

            return new MyAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.videoView.setVideoURI(Uri.parse(videos[position]));
        }


        @Override
        public int getItemCount() {
            return videos.length;
        }


        public class ViewHolder extends RecyclerView.ViewHolder{
//            ImageView img_thumb;
            VideoView videoView;
            ImageView img_play;
            RelativeLayout rootView;
            public ViewHolder(View itemView) {
                super(itemView);
//                img_thumb = itemView.findViewById(R.id.img_thumb);
                videoView = itemView.findViewById(R.id.video_view);
                img_play = itemView.findViewById(R.id.img_play);
                rootView = itemView.findViewById(R.id.root_view);
            }
        }
    }


    private void addVideos(String[] ChangeVideos,View position,int pos){
        addlist(ChangeVideos);
        int length = videos.length;
        recyclerView.getAdapter().notifyItemRangeChanged(pos+1,length);
    }

    /**
     * 停止播放
     * @param itemView
     */
    private void releaseVideo(View  itemView){
        final VideoView videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
        videoView.stopPlayback();
        imgThumb.animate().alpha(1).start();
        imgPlay.animate().alpha(0f).start();
    }

    /**
     * 开始播放
     * @param itemView
     */

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void playVideo(View itemView) {
        final VideoView videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final RelativeLayout rootView = itemView.findViewById(R.id.root_view);
        final MediaPlayer[] mediaPlayer = new MediaPlayer[1];
        videoView.start();
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                mediaPlayer[0] = mp;
                mp.setLooping(true);
                imgThumb.animate().alpha(0).setDuration(200).start();
                return false;
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

            }
        });


        imgPlay.setOnClickListener(new View.OnClickListener() {
            boolean isPlaying = true;
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()){
                    imgPlay.animate().alpha(1f).start();
                    videoView.pause();
                    isPlaying = false;
                }else {
                    imgPlay.animate().alpha(0f).start();
                    videoView.start();
                    isPlaying = true;
                }
            }
        });
    }


    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Intent intent = new Intent();
            switch (v.getId()){

            }
        }
    }
}