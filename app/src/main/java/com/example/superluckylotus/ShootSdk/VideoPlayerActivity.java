package com.example.superluckylotus.ShootSdk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.veuisdk.manager.VideoMetadataRetriever;
import com.example.superluckylotus.R;

/**
 * 播放界面
 */
public class VideoPlayerActivity extends Activity {
    private String TAG = "VideoPlayerActivity";
    public static final String ACTION_PATH = "视频路径";
    private VideoView mVideoView;
    private ProgressDialog mLoadingDialog;
    private FrameLayout mVideoViewParent;
    private String mSupportAntiChange;
    private int mLastPlayerPosition = -1;
    private boolean mLastPlaying = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.toString();
        setContentView(R.layout.activity_video_player);
        String path = getIntent().getStringExtra(ACTION_PATH);

        // 检索视频信息
        VideoMetadataRetriever vmr = new VideoMetadataRetriever();
        vmr.setDataSource(path);

        // 读取视频信息
        Log.i(TAG,
                "video duration:"
                        + vmr.extractMetadata(VideoMetadataRetriever.METADATA_KEY_VIDEO_DURATION));
        mSupportAntiChange = vmr
                .extractMetadata(VideoMetadataRetriever.METADATA_KEY_IS_SUPPORT_ANTI_CHANGE);
        Log.i(TAG, "is support anti-change:" + mSupportAntiChange);
        Log.i(TAG,
                "video bit rate:"
                        + vmr.extractMetadata(VideoMetadataRetriever.METADATA_KEY_VIDEO_BIT_RATE));
        Log.i(TAG,
                "video width:"
                        + vmr.extractMetadata(VideoMetadataRetriever.METADATA_KEY_VIDEO_WIDHT));
        Log.i(TAG,
                "video height:"
                        + vmr.extractMetadata(VideoMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        Log.i(TAG,
                "video frame rate:"
                        + vmr.extractMetadata(VideoMetadataRetriever.METADATA_KEY_VIDEO_FRAME_RATE));

        mVideoView = findViewById(R.id.svpriview);
        mVideoView.setVideoPath(path);
        mVideoViewParent = (FrameLayout) findViewById(R.id.videoParentGroup);

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
            }
        });

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mLoadingDialog.dismiss();
                playVideo();
            }
        });
        MediaController mc = new MediaController(this);
        mc.setAnchorView(mVideoView);
        mc.setKeepScreenOn(true);
        mVideoView.setMediaController(mc);

        findViewById(R.id.left).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mLoadingDialog = ProgressDialog.show(this, null, "正在加载视频...");
    }

    @Override
    protected void onPause() {
        mLastPlayerPosition = mVideoView.getCurrentPosition();
        mLastPlaying = mVideoView.isPlaying();
        mVideoView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mLastPlaying) {
            playVideo();
        } else {
            pauseVideo();
        }
        mVideoView.seekTo(mLastPlayerPosition);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void playVideo() {
        mVideoView.start();
    }

    private void pauseVideo() {
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
        }
    }
}
