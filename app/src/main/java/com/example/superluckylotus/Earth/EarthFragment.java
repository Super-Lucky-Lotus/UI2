package com.example.superluckylotus.Earth;

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
import android.widget.ToggleButton;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superluckylotus.Manager.DouYinLayoutManager;
import com.example.superluckylotus.Manager.HttpServer;
import com.example.superluckylotus.Manager.OnViewPagerListener;
import com.example.superluckylotus.Phone;
import com.example.superluckylotus.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.mob.tools.utils.DeviceHelper.getApplication;


/**
 * @version: 1.0
 * @author: 黄诗雯
 * @className: EarthFragment
 * @packageName:com.example.superluckylotus
 * @description: 广场界面
 * @data: 2020.07.10 15:12
 **/

/**
 * @version: 2.0
 * @author: 宋佳容
 * @className: EarthFragment
 * @packageName:com.example.superluckylotus
 * @description: 跳转到搜索界面
 * @data: 2020.07.10 15:12
 **/

/**
 * @version: 3.0
 * @author: 黄诗雯
 * @className: EarthFragment
 * @packageName:com.example.superluckylotus
 * @description: 跳出更多弹窗和评论弹窗
 * @data: 2020.07.17 15:27
 **/

/**
 * @version: 3.0
 * @author: 宋佳容
 * @className: EarthFragment
 * @packageName:com.example.superluckylotus
 * @description: 修改打赏功能，增加分享弹窗
 * @data: 2020.07.20 15:06
 **/

public class EarthFragment extends Fragment {

    private Button turnSearchPage_btn;
    private Button rcm_btn;
    private ToggleButton mLike;
    private Button mMore;
    private Button mComment;
    private Button mShare;
    MoreDialog md;
    CommentDialog cd;
    ShareDialog sd;
    String[] videos;
    String[] nicknames;
    String[] labels;
    String[] descriptions;

    private RecyclerView recyclerView;

    private DouYinLayoutManager douYinLayoutManager;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        videos = new String[]{"http://139.219.4.34/media\\video\\833ed4c073.mp4","http://139.219.4.34/media\\video\\817073282e.mp4","http://139.219.4.34/media\\video\\24fef20322.mp4","http://139.219.4.34/media\\video\\43c0826f44.mp4"};
        String path = "http://139.219.4.34/getallmedia/";
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


                    String descriptionsArray = result_json.getString("descriptions");
                    Log.v(TAG,"123456789:"+descriptionsArray);
                    descriptionsArray = descriptionsArray.substring(1,descriptionsArray.length()-1);
                    String[] loacldescriptions = descriptionsArray.split(",");
                    for (int i = 0 ;i <loacldescriptions.length;i++){
                        loacldescriptions[i]=loacldescriptions[i].substring(1,loacldescriptions[i].length()-1);
                        Log.v(TAG,"123456789:"+loacldescriptions[i]);
                    }
                    descriptions = loacldescriptions;

                    String labelsArray = result_json.getString("labels");
                    Log.v(TAG,"123456789:"+labelsArray);
                    labelsArray = labelsArray.substring(1,labelsArray.length()-1);
                    String[] loacllabels = labelsArray.split(",");
                    for (int i = 0 ;i <loacllabels.length;i++){
                        loacllabels[i]=loacllabels[i].substring(1,loacllabels[i].length()-1);
                        Log.v(TAG,"123456789:"+loacllabels[i]);
                    }
                    labels=loacllabels;

                    String nicknamesArray = result_json.getString("nicknames");
                    Log.v(TAG,"123456789:"+nicknamesArray);
                    nicknamesArray = nicknamesArray.substring(1,nicknamesArray.length()-1);
                    String[] loaclnicknames = nicknamesArray.split(",");
                    for (int i = 0 ;i <loaclnicknames.length;i++){
                        loaclnicknames[i]=loaclnicknames[i].substring(1,loaclnicknames[i].length()-1);
                        Log.v(TAG,"123456789:"+loaclnicknames[i]);
                    }
                    nicknames = loaclnicknames;

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


        View view = inflater.inflate(R.layout.fragment_earth,null);
        turnSearchPage_btn = (Button)view.findViewById(R.id.turnSearchPage_btn);
        rcm_btn=(Button)view.findViewById(R.id.rcm_btn);
        md=new MoreDialog(getActivity());
        cd=new CommentDialog(getActivity());
        sd=new ShareDialog(getActivity());
        OnClick onclick=new OnClick();
        turnSearchPage_btn.setOnClickListener(onclick);
        rcm_btn.setOnClickListener(onclick);
        recyclerView = view.findViewById(R.id.recyclerView_dy);
        initView();
        return view;
    }

    private void initView() {
        douYinLayoutManager = new DouYinLayoutManager(getActivity(), OrientationHelper.VERTICAL,false);
        recyclerView.setLayoutManager(douYinLayoutManager);

        MyAdapter a =new MyAdapter();
        recyclerView.setAdapter(a);
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

            public void onaddVideos(String[] ChangeVideos,String[] ChangeVideos1,String[] ChangeVideos2,String[] ChangeVideos3,View position,int pos) {
                addVideos(ChangeVideos,ChangeVideos1,ChangeVideos2,ChangeVideos3,position,pos);
            }
        });
    }

    public void addlist(String[] a,String[] b,String[] d,String[] e){
        String[] c= new String[a.length+videos.length];
        System.arraycopy(videos, 0, c, 0, videos.length);
        System.arraycopy(a, 0, c, videos.length, a.length);
        videos = c;

        String[] c1= new String[b.length+nicknames.length];
        System.arraycopy(nicknames, 0, c1, 0, nicknames.length);
        System.arraycopy(b, 0, c1, nicknames.length, b.length);
        nicknames = c1;

        String[] c2= new String[d.length+labels.length];
        System.arraycopy(labels, 0, c2, 0, labels.length);
        System.arraycopy(d, 0, c2, labels.length, d.length);
        labels = c2;

        String[] c3= new String[e.length+descriptions.length];
        System.arraycopy(descriptions, 0, c3, 0, descriptions.length);
        System.arraycopy(e, 0, c3, descriptions.length, e.length);
        descriptions = c3;

    }

    public void reInit(String[] a,String[] b,String[] c,String[] d){
    videos = a;
    nicknames =b;
    labels =c;
    descriptions =d;
}



    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        public MyAdapter(){
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video,parent,false);
            mLike = view.findViewById(R.id.video_like);
            mMore=view.findViewById(R.id.video_more);
            mComment=view.findViewById(R.id.video_comment);
            mShare=view.findViewById(R.id.video_share);
//            mLike.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Phone phoneObj = (Phone)getApplication();
//                    final String phone = phoneObj.getPhone();
//                    String path;
//                    if(mLike.isChecked()) {
//                        path = "http://139.219.4.34/addlike/";
//                    }
//                    else{
//                        path = "http://139.219.4.34/cancellike/";
//                    }
//                    Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
//                    userParams.put("phone",phone);
//                    userParams.put("video_path","media\\video\\faf0fb3837.mp4");
//
//                    HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
//                        @Override
//                        public void onSuccess(String result) throws JSONException {
////                            JSONObject result_json = new JSONObject(result);
////                            String get = result_json.getString("msg");
////                            //int num = result_json.getInt("num");
////                            Log.v("EarthFragment", result);
//                            //if (get.equals("success")) {
//                            //  for (int i = 1; i < num; i++) {
//                            //    String username = result_json.getString("Fan" + i + "Name");
//                            //  String time = result_json.getString("time" + i);
//                            //Log.v("GetLikesActivity", username);
//                            // }
//                            //} else {
//                            //  Log.v("CommentDialog","没有评论！");
//                            //}
//                        }
//
//                        @Override
//                        public void onError(Exception e) {
//                            Log.v("EarthFragment", "连接失败！");
//                        }
//
//                        @Override
//                        public void onFinish() {
//                        }
//
//                    });
//                }
//
//            });
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
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
//            holder.img_thumb.setImageResource(imgs[position]); sdfsdf
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
            TextView a;
            TextView b;
            TextView c;

            public ViewHolder(View itemView) {
                super(itemView);
//                img_thumb = itemView.findViewById(R.id.img_thumb);
                videoView = itemView.findViewById(R.id.video_view);
                img_play = itemView.findViewById(R.id.img_play);
                rootView = itemView.findViewById(R.id.root_view);
                a = itemView.findViewById(R.id.textView_username);
                b = itemView.findViewById(R.id.textView_tab);
                c = itemView.findViewById(R.id.textView_detail);
            }
        }
    }


    private void addVideos(String[] ChangeVideos,String[] ChangeVideos1,String[] ChangeVideos2,String[] ChangeVideos3,View position,int pos){
        addlist(ChangeVideos,ChangeVideos1,ChangeVideos2,ChangeVideos3);
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
        public void onClick(final View v){
            Intent intent = new Intent();
            switch (v.getId()){
                case R.id.turnSearchPage_btn:
                    intent.setClass(getActivity(),SearchActivity.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.rcm_btn:
                    String path = "http://139.219.4.34/getallmedia/";
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
                                String[] videos = videoArray.split(",");
                                for (int i = 0 ;i <videos.length;i++){
                                    videos[i]=videos[i].replaceFirst("\\\\\\\\","//");
                                    videos[i]=videos[i].replaceFirst("\\\\\\\\","/");
                                    videos[i]=videos[i].replaceAll("\\\\\\\\","/");
                                    videos[i]=videos[i].substring(1,videos[i].length()-1);
                                    Log.v(TAG,"123456789:"+videos[i]);
                                }

                                String descriptionsArray = result_json.getString("descriptions");
                                Log.v(TAG,"123456789:"+descriptionsArray);
                                descriptionsArray = descriptionsArray.substring(1,descriptionsArray.length()-1);
                                String[] descriptions = descriptionsArray.split(",");
                                for (int i = 0 ;i <descriptions.length;i++){
                                    descriptions[i]=descriptions[i].substring(1,descriptions[i].length()-1);
                                    Log.v(TAG,"123456789:"+descriptions[i]);
                                }

                                String labelsArray = result_json.getString("labels");
                                Log.v(TAG,"123456789:"+labelsArray);
                                labelsArray = labelsArray.substring(1,labelsArray.length()-1);
                                String[] labels = labelsArray.split(",");
                                for (int i = 0 ;i <labels.length;i++){
                                    labels[i]=labels[i].substring(1,labels[i].length()-1);
                                    Log.v(TAG,"123456789:"+labels[i]);
                                }

                                String nicknamesArray = result_json.getString("nicknames");
                                Log.v(TAG,"123456789:"+nicknamesArray);
                                nicknamesArray = nicknamesArray.substring(1,nicknamesArray.length()-1);
                                String[] nicknames = nicknamesArray.split(",");
                                for (int i = 0 ;i <nicknames.length;i++){
                                    nicknames[i]=nicknames[i].substring(1,nicknames[i].length()-1);
                                    Log.v(TAG,"123456789:"+nicknames[i]);
                                }

                                reInit(videos,nicknames,labels,descriptions);
                                recyclerView.getAdapter().notifyDataSetChanged();
                            }

                            recyclerView.scrollToPosition(0);
//                            View view1 = recyclerView.getFocusedChild();
//                            playVideo(view1);

                        }

                        @Override
                        public void onError(Exception e) {
                            Log.v("EarthFragment", "连接失败！");
                        }

                        @Override
                        public void onFinish() {
                        }

                    });
                    break;

            }
        }
    }

}