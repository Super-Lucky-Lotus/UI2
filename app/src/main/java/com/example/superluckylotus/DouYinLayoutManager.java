package com.example.superluckylotus;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.mob.tools.utils.DeviceHelper.getApplication;

public class DouYinLayoutManager extends LinearLayoutManager implements RecyclerView.OnChildAttachStateChangeListener{

    //判断是否上滑还是下滑
    private int mDrift;

    private OnViewPagerListener onViewPagerListener;
    //吸顶，吸底
    private PagerSnapHelper pagerSnapHelper;

    public OnViewPagerListener getOnViewPagerListener() {
        return onViewPagerListener;
    }

    public void setOnViewPagerListener(OnViewPagerListener onViewPagerListener) {
        this.onViewPagerListener = onViewPagerListener;
    }

    public DouYinLayoutManager(Context context) {
        super(context);
    }

    public DouYinLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        pagerSnapHelper = new PagerSnapHelper();
    }

    /**
     * 当manager完全添加到recycleview中是会被调用
     * @param view
     */
    @Override
    public void onAttachedToWindow(RecyclerView view) {
        view.addOnChildAttachStateChangeListener(this);
        pagerSnapHelper.attachToRecyclerView(view);
        super.onAttachedToWindow(view);
    }

    @Override
    public boolean canScrollVertically() {
        return super.canScrollVertically();
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        mDrift = dy;
        return super.scrollVerticallyBy(dy, recycler, state);

    }

    @Override
    public void onChildViewAttachedToWindow(@NonNull View view) {
        if (mDrift > 0){
            //向上滑
            if (onViewPagerListener != null && Math.abs(mDrift) == view.getHeight()){
                onViewPagerListener.onPageSelected(false,view);
            }
        }else {
            //向下滑
            if (onViewPagerListener != null && Math.abs(mDrift) == view.getHeight()){
                onViewPagerListener.onPageSelected(true,view);
            }
        }
    }

    @Override
    public void onChildViewDetachedFromWindow(@NonNull View view) {
        if (mDrift >= 0){
            //向上滑
            if (onViewPagerListener != null){
                onViewPagerListener.onPageRelease(true,view);
            }
        }else {
            //向下滑
            if (onViewPagerListener != null){
                onViewPagerListener.onPageRelease(false,view);
            }
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        switch (state){
            case RecyclerView.SCROLL_STATE_IDLE:
                //当前显示的item
                final View snapView = pagerSnapHelper.findSnapView(this);
                if (onViewPagerListener != null){
                    if ( findLastVisibleItemPosition()==getItemCount()-1){
                        final int position = getPosition(snapView);
                        String path = "http://139.219.4.34/getallvideo/";
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
//                                    String[] a ={"http://139.219.4.34/media\\video\\543c4fa467.mp4", "http://139.219.4.34/media\\video\\543c4fa467.mp4",
//                                            "http://139.219.4.34/media\\video\\543c4fa467.mp4", "http://139.219.4.34/media\\video\\543c4fa467.mp4"};
                                    onViewPagerListener.onaddVideos(videos,snapView,position);
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

                    }
                    onViewPagerListener.onPageSelected(false,snapView);
                }
                break;
        }
        super.onScrollStateChanged(state);

    }
}
