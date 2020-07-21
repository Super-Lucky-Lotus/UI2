package com.example.superluckylotus.ShootSdk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superluckylotus.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.example.superluckylotus.ShootSdk.utils.SDKUtils;
import com.vecore.VirtualVideo;
import com.vecore.base.lib.utils.FileUtils;
import com.vecore.exception.InvalidArgumentException;
import com.vecore.listener.ExportListener;
import com.veuisdk.BaseActivity;
import com.veuisdk.IShortVideoInfo;
import com.veuisdk.SdkEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * 草稿箱列表
 */
public class DraftListActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private List<IShortVideoInfo> mVideoInfoList;
    private DraftAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = "DraftListActivity";
        setContentView(R.layout.activity_draft_list_layout);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    /**
     * 草稿数据
     */
    private void initData() {
        mVideoInfoList.clear();
        List<IShortVideoInfo> tmp = SdkEntry.getDraftList(this);
        if (null != tmp && tmp.size() > 0) {
            mVideoInfoList.addAll(tmp);
        }
        if (mVideoInfoList.size() == 0) {
            Toast.makeText(this, getString(R.string.noDraftList), Toast.LENGTH_SHORT).show();
        }
        mAdapter.notifyDataSetChanged();
    }


    private void initView() {
        findViewById(R.id.btnLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mRecyclerView = (RecyclerView) findViewById(com.veuisdk.R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new DraftAdapter(this);
        mVideoInfoList = new ArrayList<>();
        //设置适配器
        mRecyclerView.setAdapter(mAdapter);
    }


    private final int REQUEST_EDIT = 500;

    /**
     * 执行删除草稿
     *
     * @param info
     * @param showToast
     */
    private void onDeleteShortImp(IShortVideoInfo info, boolean showToast) {
        boolean re = SdkEntry.deleteDraft(DraftListActivity.this, info);
        if (showToast) {
            Toast.makeText(DraftListActivity.this, re ? getString(R.string.delete_success) : getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
        }
        initData();
    }

    private void onEditResult(String mediaPath) {
        SDKUtils.onVideoExport(DraftListActivity.this, mediaPath);
        SDKUtils.onPlayVideo(DraftListActivity.this, mediaPath);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_EDIT) {
                final String mediaPath = data.getStringExtra(SdkEntry.EDIT_RESULT);
                if (!TextUtils.isEmpty(mediaPath)) {
                    if (null != mCurrentDraft) {
                        onAlertDelete(R.string.draft_dialog_title_release, new IDialog() {
                            @Override
                            public void onDialogPos() {
                                onDeleteShortImp(mCurrentDraft, false);
                                mCurrentDraft = null;
                                onEditResult(mediaPath);
                            }

                            @Override
                            public void onDialogNeg() {
                                mCurrentDraft = null;
                                onEditResult(mediaPath);
                            }
                        });

                    } else {
                        Log.d(TAG, "onActivityResult:" + mediaPath);
                        Toast.makeText(this, mediaPath, Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }

        }
    }


    /**
     * 导出视频的回调演示
     */
    private class ExportVideoLisenter implements ExportListener {
        private String mPath;
        private AlertDialog mDialog;
        private ProgressBar mProgressBar;
        private Button mBtnCancel;
        private TextView mTvTitle;
        private IShortVideoInfo mVideoInfo;

        public void setPath(String path) {
            mPath = path;
        }

        public ExportVideoLisenter(IShortVideoInfo info) {
            mVideoInfo = info;
        }

        @Override
        public void onExportStart() {
            View v = LayoutInflater.from(DraftListActivity.this).inflate(
                    R.layout.progress_view, null);
            mTvTitle = (TextView) v.findViewById(R.id.tvTitle);
            mTvTitle.setText(R.string.exporting);
            mProgressBar = (ProgressBar) v.findViewById(R.id.pbCompress);
            mBtnCancel = (Button) v.findViewById(R.id.btnCancelCompress);
            mBtnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SdkEntry.cancelExport();
                }
            });
            mDialog = new AlertDialog.Builder(DraftListActivity.this).setView(v)
                    .show();
            mDialog.setCanceledOnTouchOutside(false);
        }

        /**
         * 导出进度回调
         *
         * @param progress 当前进度
         * @param max      最大进度
         * @return 返回是否继续执行，false为终止导出
         */
        @Override
        public boolean onExporting(int progress, int max) {
            if (mProgressBar != null) {
                mProgressBar.setMax(max);
                mProgressBar.setProgress(progress);
            }
            return true;
        }

        @Override
        public void onExportEnd(int result) {
            Log.e(TAG, "onExportEnd: " + result + "   :" + mPath);
            mDialog.dismiss();
            if (result >= VirtualVideo.RESULT_SUCCESS) {
                SDKUtils.onVideoExport(DraftListActivity.this, mPath);
                onAlertDelete(R.string.draft_dialog_title_release, new IDialog() {
                    @Override
                    public void onDialogPos() {
                        onDeleteShortImp(mVideoInfo, false);
                        SDKUtils.onPlayVideo(DraftListActivity.this, mPath);

                    }

                    @Override
                    public void onDialogNeg() {
                        SDKUtils.onPlayVideo(DraftListActivity.this, mPath);
                    }
                });
            } else if (result != VirtualVideo.RESULT_EXPORT_CANCEL) {
                Log.e(TAG, "onExportEnd: " + result);
                String text = getString(R.string.exportFailed);
                Toast.makeText(DraftListActivity.this, text + result, Toast.LENGTH_LONG).show();
            }
        }
    }

    private IShortVideoInfo mCurrentDraft = null;


    interface IDialog {

        void onDialogPos();

        void onDialogNeg();
    }

    /**
     * 删除草稿的提示
     *
     * @param iDialog
     */
    private void onAlertDelete(int title, final IDialog iDialog) {
        if (!DraftListActivity.this.isDestroyed()) {
            AlertDialog.Builder ab = new AlertDialog.Builder(DraftListActivity.this);
            ab.setMessage(title);
            ab.setPositiveButton(getString(R.string.draft_dialog_pos), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (null != iDialog) {
                        iDialog.onDialogPos();
                    }
                }
            });
            ab.setNegativeButton(getString(R.string.draft_dialog_neg), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (null != iDialog) {
                        iDialog.onDialogNeg();
                    }
                }
            });
            ab.show();
        }
    }

    class DraftAdapter extends RecyclerView.Adapter<DraftAdapter.ViewHolder> {
        private LayoutInflater mLayoutInflater;

        DraftAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        /***
         * 部分资源不存在（原始视频被删除）
         * @param info
         */
        private void somethingNotExits(IShortVideoInfo info) {
            String text = getString(R.string.somethingNotExits);
            Toast.makeText(DraftListActivity.this, text, Toast.LENGTH_SHORT).show();
            //部分媒体被删除，主动删除草稿箱视频，清理数据
            onDeleteShortImp(info, false);
        }

        @Override
        public DraftAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.item_draft_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(DraftAdapter.ViewHolder holder, int position) {

            final IShortVideoInfo info = mVideoInfoList.get(position);
            if (FileUtils.isExist(info.getCover())) {
                SDKUtils.setCover(holder.ivThumb, info.getCover());
            }
            holder.tvCreateTime.setText(SDKUtils.getDate(info.getCreateTime()));
            holder.tvDuration.setText(getString(R.string.duration_text, info.getDuration()));

            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCurrentDraft = info;
                    try {
                        SdkEntry.onEditDraft(DraftListActivity.this, mCurrentDraft, REQUEST_EDIT);
                    } catch (InvalidArgumentException e) {
                        e.printStackTrace();
                        somethingNotExits(mCurrentDraft);
                        mCurrentDraft = null;
                    }
                }
            });
            holder.btnExport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ExportVideoLisenter exportVideoLisenter = new ExportVideoLisenter(info);
                    try {
                        String dst = SdkEntry.onExportDraft(DraftListActivity.this, info, exportVideoLisenter);
                        exportVideoLisenter.setPath(dst);
                    } catch (InvalidArgumentException e) {
                        e.printStackTrace();
                        somethingNotExits(info);
                    }
                }
            });
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除单个草稿箱视频
                    onAlertDelete(R.string.draft_dialog_title, new IDialog() {
                        @Override
                        public void onDialogPos() {
                            onDeleteShortImp(info, true);
                        }

                        @Override
                        public void onDialogNeg() {
                        }
                    });

                }
            });


        }


        @Override
        public int getItemCount() {
            return mVideoInfoList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            SimpleDraweeView ivThumb;
            TextView tvCreateTime;
            TextView tvDuration;
            Button btnExport, btnEdit, btnDelete;

            public ViewHolder(View itemView) {
                super(itemView);
                ivThumb = itemView.findViewById(R.id.ivCover);
                tvCreateTime = itemView.findViewById(R.id.tvCreateTime);
                tvDuration = itemView.findViewById(R.id.tvDuration);
                btnExport = itemView.findViewById(R.id.btnExport);
                btnEdit = itemView.findViewById(R.id.btnEdit);
                btnDelete = itemView.findViewById(R.id.btnDelete);

            }
        }
    }

}
