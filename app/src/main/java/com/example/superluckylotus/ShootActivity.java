package com.example.superluckylotus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.superluckylotus.ShootSdk.CameraWatermarkBuilder;
import com.example.superluckylotus.ShootSdk.DraftListActivity;
import com.example.superluckylotus.ShootSdk.authpack;
import com.example.superluckylotus.ShootSdk.dialog.AudioConfigDialog;
import com.example.superluckylotus.ShootSdk.dialog.ConfigData;
import com.example.superluckylotus.ShootSdk.utils.SDKUtils;
import com.vecore.VirtualVideo;
import com.vecore.base.lib.utils.CoreUtils;
import com.vecore.exception.InvalidArgumentException;
import com.vecore.listener.ExportListener;
import com.vecore.models.Trailer;
import com.vecore.models.VideoConfig;
import com.vecore.models.Watermark;
import com.veuisdk.SdkEntry;
import com.veuisdk.SdkService;
import com.veuisdk.manager.CameraConfiguration;
import com.veuisdk.manager.ChangeLanguageHelper;
import com.veuisdk.manager.EditObject;
import com.veuisdk.manager.ExportConfiguration;
import com.veuisdk.manager.FaceuInfo;
import com.veuisdk.manager.TrimConfiguration;
import com.veuisdk.manager.UIConfiguration;
import com.veuisdk.manager.VEOSDBuilder;
import com.veuisdk.manager.VideoMetadataRetriever;

import java.util.ArrayList;
import java.util.List;

import static com.mob.tools.utils.DeviceHelper.getApplication;
import static com.mob.tools.utils.Strings.getString;
import static com.veuisdk.SdkEntry.editMedia;
import static com.veuisdk.SdkEntry.getSdkService;
import static com.veuisdk.SdkEntry.trimVideo;
import static com.veuisdk.manager.CameraConfiguration.SQUARE_SCREEN_CAN_CHANGE;

public class ShootActivity extends AppCompatActivity {


    /**
     * REQUEST_CODE定义：<br>
     * 读取外置存储
     */
    private static final int REQUEST_CODE_EXTERNAL_STORAGE_PERMISSIONS = 1;
    private static final String TAG ="ShootActivity" ;
    private String EDIT_PICTURE_PATH = null;
    /**
     * 导出的横向16:9视频
     */
    private String EDIT_L_VIDEO_PATH = null;
    /**
     * 测试用1：1方型视频
     */
    private String EDIT_S_VIDEO_PATH = null;
    /**
     * 测试用竖向9:16视频
     */
    private String EDIT_P_VIDEO_PATH = null;
    /**
     * 测试用水印图片
     */
    private String EDIT_WATERMARK_PATH = null;

    private ConfigData configData;
    private byte[] authpackArr = null;


    private SparseArray<ActivityResultHandler> registeredActivityResultHandlers;
    /**
     * REQUEST_CODE定义：<br>
     * 录制
     */
    private final int CAMERA_REQUEST_CODE = 100;
    /**
     * REQUEST_CODE定义：<br>
     * 相册
     */
    private final int ALBUM_REQUEST_CODE = 101;
    /**
     * 从相册选折要播放的视频
     */
    private final int ALBUM_PLAYER_REQUEST_CODE = 1012;
    /**
     * 防篡改录制演示
     */
    private final int CAMERA_ANTI_CHANGE_REQUEST_CODE = 1013;
    /**
     * 动画演示
     */
    private final int ALBUM_ANIMATION_REQUEST_CODE = 1014;
    /**
     * 异形显示
     */
    private final int ALBUM_POINTF_REQUEST_CODE = 1040;
    /**
     * 仿quik
     */
    private final int ALBUM_QUIK_REQUEST_CODE = 1050;
    /**
     * 从相册选择设置音效的视频
     */
    private final int ALBUM_SOUND_EFFECT_REQUEST_CODE = 1016;
    /**
     * 从相册选择剪影视频
     */
    private final int ALBUM_SILHOUETT_REQUEST_CODE = 1017;
    /**
     * 从相册选择AE动画原图（仅支持图片）
     */
    private final int ALBUM_AE_IMAGE_REQUEST_CODE = 1018;
    /**
     * 从相册选择异形原图
     */
    private final int ALBUM_ALIEN_REQUEST_CODE = 1020;
    /**
     * 从相册选择设置音效的独立音频播放器
     */
    private final int ALBUM_SOUND_EFFECT_NP_REQUEST_CODE = 1021;
    /**
     * 拼接
     */
    private final int ALBUM_SPLICE_REQUEST_CODE = 1022;
    /**
     * REQUEST_CODE定义：<br>
     * 视频编辑
     */
    public static final int EDIT_REQUEST_CODE = 102;
    /**
     * REQUEST_CODE定义：<br>
     * 视频截取
     */
    private final int TRIM_REQUEST_CODE = 103;
    /**
     * REQUEST_CODE定义：<br>
     * 视频截取相册选择
     */
    private final int TRIM_ALBUM_REQUEST_CODE = 104;
    /**
     * 短视频录制演示request_code
     */
    private final int SHORTVIDEO_CAMERA_REQUEST_CODE = 110;
    /**
     * 短视频录制进入相册演示request_code
     */
    private final int SHORTVIDEO_ALBUM_REQUEST_CODE = 111;

    /**
     * 短视频录制进入相册进入截取演示request_code
     */
    private final int SHORTVIDEO_TRIM_REQUEST_CODE = 112;
    /**
     * 选折导出的文件(仅视频)
     */
    private final int ALBUM_REQUEST_EXPORT_CODE = 114;

    /**
     * 相册界面-图库按钮
     */
    private final int CAMERA_ALBUM_REQUEST_CODE = 116;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shoot);

//        LinearLayout li = findViewById(R.id.background111);
//        //方法1
//        int w = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        li.measure(w, h);
//        int li_height = li.getMeasuredHeight();
//        int li_width = li.getMeasuredWidth();
//
//        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.height = li_height;
////                (int) (getWindowManager().getDefaultDisplay().getHeight() * 0.3); // 高度设置为屏幕的0.3
////
//        params.width = li_width;
////                (int) (getWindowManager().getDefaultDisplay().getWidth() * 0.8); // 宽度设置为屏幕的0.8
////
//        getWindow().setAttributes(params);

        //设置进出动画
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);



        LinearLayout shoot;
        LinearLayout sketch;
        LinearLayout release;

        shoot=findViewById(R.id.add_shoot);
        sketch=findViewById(R.id.add_sketch);
        release=findViewById(R.id.add_release);

        release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDialog(view);
            }
        });
        shoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDialog(view);
            }
        });
        sketch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDialog(view);
            }
        });


        EDIT_PICTURE_PATH = ((Phone) getApplication()).getCustomPath() + "/android.jpg";
        EDIT_L_VIDEO_PATH = ((Phone) getApplication()).getCustomPath() + "/demoVideo1.mp4";
        EDIT_S_VIDEO_PATH = ((Phone) getApplication()).getCustomPath() + "/demoVideo2.mp4";
        EDIT_P_VIDEO_PATH = ((Phone) getApplication()).getCustomPath() + "/demoVideo3.mp4";
        EDIT_WATERMARK_PATH = "asset://watermark.png";
        ConfigData.setLanguage(ChangeLanguageHelper.isZh(this));
        restoreConfigInstanceState();
        boolean hasM = CoreUtils.hasM();
        authpackArr = authpack.A();
        if (hasM && !SdkEntry.isInitialized()) {
            List<String> permissions = new ArrayList<>();
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE_EXTERNAL_STORAGE_PERMISSIONS);
            }
        } else {
            exportDemoResource();
        }
        // 初始化 配置
        initEditorUIAndExportConfig();
        registerAllResultHandlers();
        authpackArr = authpack.A();
    }

//       private void setListeners(){
//        OnClick onClick = new OnClick();
//        shoot_Btn.setOnClickListener(onClick);
//    }
//
//   private class OnClick implements View.OnClickListener{
//
//       @Override
//        public void onClick(View v){
//           Intent intent = null;
//            switch (v.getId()){
//                case R.id.btn_addshoot:
//                    intent = new Intent(MainActivity.this,ShootActivity.class);
//                   startActivity(intent);
//                   //onVideo(v);
//                sd.popupWindowDialog(v);
//                    break;
//           }
//
//        }
//    }


    public void onDialog(View v) {
        switch (v.getId()) {

            case R.id.add_shoot: // 正方形，长方形可切换录制视频，编辑录制后的视频，如果有导出时，导出视频的路径
                SdkEntry.registerOSDBuilder(CameraWatermarkBuilder.class);
                CameraWatermarkBuilder.setText("好运莲莲");// 可自定义水印显示文本
                initCameraConfig(SQUARE_SCREEN_CAN_CHANGE);
                SdkEntry.record(this, CAMERA_REQUEST_CODE);
                break;
            case R.id.add_sketch:
                startActivity(new Intent(this, DraftListActivity.class));
                break;
            case R.id.add_release:
                Intent intent=new Intent(ShootActivity.this,VideoPostActivity.class);
                startActivity(intent);
                break;


        }
    }

    @Override
    public void finish() {
        super.finish();
        //重写finish，解决退出动画无效的问题
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }


    /**
     * 初始拍摄配置
     *
     * @param UIType * 设置录制时默认界面:<br>
     *               默认16：9录制:<br>
     *               CameraConfiguration#WIDE_SCREEN_CAN_CHANGE<br>
     *               默认1：1:<br>
     *               CameraConfiguration. SQUARE_SCREEN_CAN_CHANGE<br>
     *               仅16：9录制:<br>
     *               CameraConfiguration.ONLY_WIDE_SCREEN<br>
     *               仅1：1录制:<br>
     *               CameraConfiguration.ONLY_SQUARE_SCREEN
     */
    private void initCameraConfig(int UIType) {

        CameraConfiguration.Builder builder = new CameraConfiguration.Builder();


        // 可设置最小录制时长,0代表不限制
        builder.setVideoMinTime(configData.cameraMinTime)
                // 可设置最大录制时长,0代表不限制
                .setVideoMaxTime(configData.cameraMaxTime)
                // 为true代表多次拍摄，拍摄完成一段之后，将保存至相册并开始下一段拍摄，默认为false单次拍摄，拍摄完成后返回资源地址
                .useMultiShoot(configData.useMultiShoot)
                /**
                 * 设置录制时默认界面:<br>
                 * 默认16：9录制:<br>
                 * CameraConfiguration. WIDE_SCREEN_CAN_CHANGE<br>
                 * 默认1：1:<br>
                 * CameraConfiguration. SQUARE_SCREEN_CAN_CHANGE<br>
                 * 仅16：9录制:<br>
                 * CameraConfiguration.ONLY_SCREEN_SCREEN 仅1：1录制:<br>
                 * CameraConfiguration.ONLY_SQUARE_SCREEN
                 */
                .setCameraUIType(UIType)
                // 设置拍摄完成后，是否保存至相册（仅单次拍摄方式有效），同时通过onActivityResult及SIMPLE_CAMERA_REQUEST_CODE返回
                .setSingleCameraSaveToAlbum(configData.isSaveToAlbum)
                // 设置录制时是否静音，true代表录制后无声音
                .setAudioMute(false)
                // 设置是否启用人脸贴纸功能
                .enableFaceu(configData.isDefaultFace)
                // 设置人脸贴纸鉴权证书
                .setPack(authpackArr)
                // 设置是否默认为后置摄像头
                .setDefaultRearCamera(configData.isDefaultRearCamera)
                // 是否显示相册按钮
                .enableAlbum(configData.enableAlbum)
                // 是否使用自定义相册
                .useCustomAlbum(configData.useCustomAlbum)
                // 设置隐藏拍摄功能（全部隐藏将强制开启视频拍摄）
                .hideMV(configData.hideMV)
                .hidePhoto(configData.hidePhoto)
                .hideRec(configData.hideRec)
                // 设置mv最小时长
                .setCameraMVMinTime(configData.cameraMVMinTime)
                // 设置mv最大时长
                .setCameraMVMaxTime(configData.cameraMVMaxTime)
                // 开启相机水印时需注册水印
                // SdkEntry.registerOSDBuilder(CameraWatermarkBuilder.class);
                // 相机录制水印
                .enableWatermark(configData.enableCameraWatermark)
                // 相机水印片头
                .setCameraTrailerTime(VEOSDBuilder.OSDState.header, 2f)
                // 相机录制结束时片尾水印时长(0-1.0 单位：秒)
                .setCameraTrailerTime(VEOSDBuilder.OSDState.end,
                        configData.cameraWatermarkEnd)
                // 是否启用防篡改录制
                .enableAntiChange(configData.enableAntiChange)
                // 启用前置输出时镜像
                .enableFrontMirror(configData.enableFrontMirror)
                // 固定录制界面的方向
                .setOrientation(configData.mRecordOrientation)
                // 是否支持录制时播放音乐
                .enablePlayMusic(configData.enablePlayMusic)
                // 是否美颜
                .enableBeauty(configData.enableBeauty)
                //滤镜(lookup)
                .setFilterUrl(configData.enableNewApi ? configData.customApi : "");

        //录制的云音乐
        if (configData.enableNewApi) {
            //云音乐支持分页查询
            builder.setCloudMusicUrl(ConfigData.TYPE_URL, ConfigData.SOUND_URL, "Jason Shaw", "audionautix.com", "https://audionautix.com",
                    getString(R.string.yunmusic_sign), "http://d.56show.com/accredit/accredit.jpg");
        }

        getSdkService().initConfiguration(builder.get());
    }

    /**
     * 导出测试资源
     */
    @SuppressLint("StaticFieldLeak")
    private void exportDemoResource() {

        if (!SDKUtils.isValidFile(EDIT_PICTURE_PATH)
                || !SDKUtils.isValidFile(EDIT_L_VIDEO_PATH)
                || !SDKUtils.isValidFile(EDIT_S_VIDEO_PATH)
                || !SDKUtils.isValidFile(EDIT_P_VIDEO_PATH)
        ) {
            new AsyncTask<Integer, Integer, Integer>() {
                @SuppressLint("StaticFieldLeak")
                private ProgressDialog m_dlgProgress;


                @SuppressLint("StaticFieldLeak")
                @Override
                protected void onPreExecute() {
                    m_dlgProgress = ProgressDialog.show(ShootActivity.this,
                            null, getString(R.string.export_asset));
                }

                @SuppressLint("StaticFieldLeak")
                @Override
                protected Integer doInBackground(Integer... params) {
                    SDKUtils.assetRes2File(getAssets(), "demomedia/android.jpg", EDIT_PICTURE_PATH);
                    SDKUtils.assetRes2File(getAssets(), "demomedia/demoVideo1.mp4", EDIT_L_VIDEO_PATH);
                    SDKUtils.assetRes2File(getAssets(), "demomedia/demoVideo2.mp4", EDIT_S_VIDEO_PATH);
                    SDKUtils.assetRes2File(getAssets(), "demomedia/demoVideo3.mp4", EDIT_P_VIDEO_PATH);
                    return null;
                }

                @SuppressLint("StaticFieldLeak")
                @Override
                protected void onPostExecute(Integer result) {
                    m_dlgProgress.dismiss();
                    m_dlgProgress = null;
                }
            }.execute();
        }



    }

    /**
     * 还原持续久化保存的配置
     */
    private void restoreConfigInstanceState() {
        SharedPreferences sharedPreferences = getSharedPreferences("demo",
                Context.MODE_PRIVATE);
        configData = SdkService.restoreObject(sharedPreferences,
                "CONFIG_DATA", initAndGetConfigData());
    }

    /**
     * 初始化并返回配置
     */
    private ConfigData initAndGetConfigData() {
        if (configData == null) {
            configData = new ConfigData();
        }
        return configData;
    }

    /**
     * 初始标准编辑及导出配置
     */
    private void initEditorUIAndExportConfig() {

        initAndGetConfigData();
        initEditorUIAndExportConfig(configData);

    }

    /**
     * 新的网络接口方式(资源放到自己服务器或锐动服务器)
     *
     * @param builder
     * @param configData
     */
    private void initThridServer(UIConfiguration.Builder builder, ConfigData configData) {

        String url = configData.customApi;
        // 设置MV和mv网络地址
        builder.enableNewMV(configData.enableMV, url)
                //设置字幕URL
                .setTitlingUrl(url)
                //设置字体URL
                .setFontUrl(url)
                //贴纸URL
                .setSpecialEffectsUrl(url)
                //滤镜URL(必须是lookup滤镜)
                .setFilterUrl(url)
                //转场URL
                .setTransitionUrl(url)
                // 设置自定义的网络音乐
                .setNewMusicUrl(url)
                //云音乐
                .setNewCloudMusicUrl(ConfigData.TYPE_URL, ConfigData.SOUND_URL, "Jason Shaw", "audionautix.com", "https://audionautix.com",
                        getString(R.string.yunmusic_sign), "http://d.56show.com/accredit/accredit.jpg")
                .setSoundUrl(ConfigData.TYPE_URL, ConfigData.SOUND_URL);

    }


    private void initEditorUIAndExportConfig(ConfigData configData) {
        // 视频编辑UI配置
        UIConfiguration.Builder builder = new UIConfiguration.Builder()

                //是否显示画中画
                .enableCollage(configData.enableCollage)
                //是否显示封面功能
                .enableCover(configData.enableCover)
                //是否显示涂鸦功能
                .enableGraffiti(configData.enableGraffiti)

                //是否启用音效
                .enableSoundEffect(configData.enableSoundEffect)
                //是否启用去水印功能
                .enableDewatermark(configData.enableDewatermark)
                //是否启用草稿箱
                .enableDraft(configData.enableDraft)
                // 设置是否使用自定义相册
                .useCustomAlbum(configData.useCustomAlbum)
                // 设置向导化
                .enableWizard(configData.enableWizard)
                // 设置自动播放
                .enableAutoRepeat(configData.enableAutoRepeat)
                // 配音模式
                .setVoiceLayoutType(configData.voiceLayoutType)
                // 设置相册支持格式
                .setAlbumSupportFormat(configData.albumSupportFormatType)
                // 设置默认进入界面画面比例
                .setVideoProportion(configData.videoProportionType)
                // 设置滤镜界面风格
                .setFilterType(UIConfiguration.FILTER_LAYOUT_3)
                // 设置相册媒体选择数量上限(目前只对相册接口生效)
                .setMediaCountLimit(configData.albumMediaCountLimit)
                // 设置相册是否显示跳转拍摄按钮(目前只对相册接口生效)
                .enableAlbumCamera(configData.enableAlbumCamera)
                // 编辑与导出模块显示与隐藏（默认不设置为显示）
                .setEditAndExportModuleVisibility(
                        UIConfiguration.EditAndExportModules.SOUNDTRACK,
                        configData.enableSoundTrack)

                .setEditAndExportModuleVisibility(UIConfiguration.EditAndExportModules.DUBBING,
                        configData.enableDubbing)
                //音效和多段配音
                .setEditAndExportModuleVisibility(UIConfiguration.EditAndExportModules.SOUND, configData.enableSound)
                .setEditAndExportModuleVisibility(UIConfiguration.EditAndExportModules.MUSIC_MANY, configData.enableMusicMany)
                .setEditAndExportModuleVisibility(UIConfiguration.EditAndExportModules.FILTER,
                        configData.enableFilter)
                .setEditAndExportModuleVisibility(UIConfiguration.EditAndExportModules.TITLING,
                        configData.enableTitling)
                //是否启用特效
                .setEditAndExportModuleVisibility(
                        UIConfiguration.EditAndExportModules.SPECIAL_EFFECTS,
                        configData.enableSpecialEffects)
                //特效URL
                .setEffectUrl(ConfigData.APP_DATA)

                //特效
                .setEditAndExportModuleVisibility(
                        UIConfiguration.EditAndExportModules.EFFECTS,
                        configData.enableEffects)

                .setEditAndExportModuleVisibility(
                        UIConfiguration.EditAndExportModules.CLIP_EDITING,
                        configData.enableClipEditing)
                // 片段编辑模块显示与隐藏（默认不设置为显示）
                .setClipEditingModuleVisibility(
                        UIConfiguration.ClipEditingModules.IMAGE_DURATION_CONTROL,
                        configData.enableImageDuration)
                .setClipEditingModuleVisibility(UIConfiguration.ClipEditingModules.EDIT,
                        configData.enableEdit)
                .setClipEditingModuleVisibility(UIConfiguration.ClipEditingModules.TRIM,
                        configData.enableTrim)
                .setClipEditingModuleVisibility(
                        UIConfiguration.ClipEditingModules.VIDEO_SPEED_CONTROL,
                        configData.enableVideoSpeed)
                .setClipEditingModuleVisibility(UIConfiguration.ClipEditingModules.SPLIT,
                        configData.enableSplit)
                .setClipEditingModuleVisibility(UIConfiguration.ClipEditingModules.COPY,
                        configData.enableCopy)
                .setClipEditingModuleVisibility(UIConfiguration.ClipEditingModules.PROPORTION,
                        configData.enableProportion)
                .setClipEditingModuleVisibility(UIConfiguration.ClipEditingModules.SORT,
                        configData.enableSort)
                .setClipEditingModuleVisibility(UIConfiguration.ClipEditingModules.TEXT,
                        configData.enableText)
                .setClipEditingModuleVisibility(UIConfiguration.ClipEditingModules.REVERSE,
                        configData.enableReverse)
                .setClipEditingModuleVisibility(UIConfiguration.ClipEditingModules.TRANSITION, true);

        if (configData.enableNewApi) {
            initThridServer(builder, configData);
        } else {
            // 设置MV和mv网络地址
            builder.enableMV(configData.enableMV, ConfigData.WEB_MV_URL)
                    // 设置自定义的网络音乐
                    .setMusicUrl(ConfigData.MUSIC_URL)
                    //云音乐
                    .setCloudMusicUrl(ConfigData.CLOUDMUSIC_URL);
        }
        //设置音效地址
        builder.setSoundUrl(ConfigData.TYPE_URL, ConfigData.SOUND_URL);


        //设置资源分类
        builder.setResouceTypeUrl(ConfigData.TYPE_URL);
        //AE模板
        builder.setAEUrl(ConfigData.APP_DATA);

        UIConfiguration uiConfig = builder
                //是否显示本地音乐
                .enableLocalMusic(configData.enableLocalMusic)
                // 字幕、贴纸在mv的上面
                .enableTitlingAndSpecialEffectOuter(configData.enableTitlingAndSpecialEffectOuter)
                .get();

        // 导出视频参数配置
        ExportConfiguration exportConfig = new ExportConfiguration.Builder()
                // 设置保存路径，传null或不设置
                // 将保存至默认路径(即调用SdkEntry.initialize初始时自定义路径）
                .setSavePath(null)
                //设置导出时最大边,不设置时，默认为960
                .setVideoMaxWH(960)
                //设置视频导出码率，Mbps为单位,不设置时，默认为4
                .setVideoBitRate(4)
                //设置视频导出帧率，,不设置时，默认为30
                .setVideoFrameRate(30)
                // 设置片尾图片路径，传null或者不设置 将没有片尾
                .setTrailerPath(configData.videoTrailerPath)
                // 设置片尾时长 单位s 默认2s
                .setTrailerDuration(2)
                // 设置导出视频时长 单位s 传0或者不设置 将导出完整视频
                .setVideoDuration(configData.exportVideoDuration)
                // 设置添加媒体时长限制 单位s 传0或者不设置 将没有限制
                .setImportVideoDuration(0)
                // 设置图片水印路径
                .setWatermarkPath(configData.enableWatermark ? EDIT_WATERMARK_PATH : null)
                // 设置水印显示模式
                .setWatermarkShowMode(Watermark.MODE_DEFAULT)
                // 设置是否使用文字水印（使用文字水印，将不再显示图片水印）
                .enableTextWatermark(configData.enableTextWatermark)
                // 设置文字水印内容（开启文字水印才生效）
                .setTextWatermarkContent("watarmark")
                // 设置文字水印大小（开启文字水印才生效）
                .setTextWatermarkSize(10)
                // 设置文字水印颜色（开启文字水印才生效）
                .setTextWatermarkColor(Color.WHITE)
                // 设置文字水印阴影颜色（开启文字水印才生效）
                .setTextWatermarkShadowColor(Color.BLACK)
                // 设置水印位置 (文字或图片水印开启才生效)
                .setWatermarkPosition(configData.watermarkShowRectF).get();

        SdkService sdkService = getSdkService();
        if (null != sdkService) {
            // 初始化所有配置
            sdkService.initConfiguration(exportConfig, uiConfig);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SdkEntry.onExitApp(this);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }



    /**
     * 保存并播放
     */
    private void onVideoExportPlay(String path) {
        //存入数据库
        SDKUtils.onVideoExport(this, path);
        SDKUtils.onPlayVideo(this, path);
    }

    private ActivityResultHandler albumSoundEffectResultHandler = new ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data
                        .getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0) {
                    if (!TextUtils.isEmpty(arrMediaListPath.get(0))) {
                        try {
                            SdkEntry.musicFilter(context, arrMediaListPath, EDIT_REQUEST_CODE);
                        } catch (InvalidArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };

    private ActivityResultHandler albumSoundEffectNpResultHandler = new ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data
                        .getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0) {
                    if (!TextUtils.isEmpty(arrMediaListPath.get(0))) {
                        try {
                            SdkEntry.virtualAudioFilter(context, arrMediaListPath, EDIT_REQUEST_CODE);
                        } catch (InvalidArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };

    private ActivityResultHandler albumSilhouetteResultHandler = new ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode,
                                     Intent data) {
            if (resultCode == RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data
                        .getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0) {
                    if (!TextUtils.isEmpty(arrMediaListPath.get(0))) {
                        try {
                            SdkEntry.silhouette(context, arrMediaListPath.get(0), EDIT_REQUEST_CODE);
                        } catch (InvalidArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };


    private ActivityResultHandler albumAlienResultHandler = new ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode,
                                     Intent data) {
            if (resultCode == RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data
                        .getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0) {
                    if (!TextUtils.isEmpty(arrMediaListPath.get(0))) {
                        try {
                            SdkEntry.alien(context, arrMediaListPath, EDIT_REQUEST_CODE);
                        } catch (InvalidArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };

    private ActivityResultHandler albumAEImageResultHandler = new ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode,
                                     Intent data) {
            if (resultCode == RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data
                        .getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0) {
                    if (!TextUtils.isEmpty(arrMediaListPath.get(0))) {
                        try {
                            SdkEntry.AEAnimation(context, arrMediaListPath, EDIT_REQUEST_CODE, true);
                        } catch (InvalidArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };

    private ActivityResultHandler albumSpliceImageResultHandler = new ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data
                        .getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0) {
                    if (!TextUtils.isEmpty(arrMediaListPath.get(0))) {
//                        UIConfiguration.Builder builder = new UIConfiguration.Builder();
//                        initThridServer(builder, new ConfigData());
//                        getSdkService().initConfiguration(new ExportConfiguration.Builder().get(), builder.get());
                        try {
                            SdkEntry.splice(context, arrMediaListPath, EDIT_REQUEST_CODE);
                        } catch (InvalidArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };

    private ActivityResultHandler cameraResultHandler = new ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode,
                                     Intent data) {
            if (resultCode == SdkEntry.RESULT_CAMERA_TO_ALBUM) {
                // 点击拍摄的相册按钮，将返回在此，并在这里做进入相册界面操作
                SdkEntry.openAlbum(context,
                        UIConfiguration.ALBUM_SUPPORT_DEFAULT,
                        CAMERA_ALBUM_REQUEST_CODE);
            } else if (resultCode == RESULT_OK) {
                // 美颜参数回调
                FaceuInfo info = data
                        .getParcelableExtra(SdkEntry.INTENT_KEY_FACEU);
                if (null != info) {
                    Log.e("faceu美颜参数", info.toString());
                }
                // 拍摄后返回的媒体路径
                ArrayList<String> arrMediaListPath = new ArrayList<String>();
                String videoPath = data
                        .getStringExtra(SdkEntry.INTENT_KEY_VIDEO_PATH);
                String picPath = data
                        .getStringExtra(SdkEntry.INTENT_KEY_PICTURE_PATH);
                arrMediaListPath.add(videoPath);
                arrMediaListPath.add(picPath);
                String logInfo = String.format("Video path：%s,Picture path：%s",
                        videoPath, picPath);
                Log.e(TAG, logInfo);


                if (configData.albumSupportFormatType == UIConfiguration.ALBUM_SUPPORT_IMAGE_ONLY) {

                    if (videoPath != null) {
                        SdkEntry.selectMedia(context);
                        return;
                    }
                } else if (configData.albumSupportFormatType == UIConfiguration.ALBUM_SUPPORT_VIDEO_ONLY) {
                    if (picPath != null) {
                        SdkEntry.selectMedia(context);
                        return;
                    }
                }

                if (data.getBooleanExtra(SdkEntry.INTENT_KEY_USE_MV_EDIT, false)) {
                    Log.e(TAG, "onActivityResult: mv");
                    initCameraShortVideoConfig();
                } else {
                    Log.e(TAG, "onActivityResult: false");
                    initEditorUIAndExportConfig();
                }
                try {
                    editMedia(context, arrMediaListPath, EDIT_REQUEST_CODE);
                } catch (InvalidArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    private ActivityResultHandler cameraAntiChangeResultHandler = new ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode,
                                     Intent data) {
            if (resultCode == RESULT_OK) {
                String logInfo = String.format("Video path：%s,Picture path：%s",
                        data.getStringExtra(SdkEntry.INTENT_KEY_VIDEO_PATH),
                        data.getStringExtra(SdkEntry.INTENT_KEY_PICTURE_PATH));
                Log.d(TAG, logInfo);
                onVideoExportPlay(data.getStringExtra(SdkEntry.INTENT_KEY_VIDEO_PATH));

            }
        }
    };
    private ActivityResultHandler albumResultHandler = new ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode,
                                     Intent data) {
            if (resultCode == SdkEntry.RESULT_ALBUM_TO_CAMERA) {
                // 点击相册的拍摄按钮，将返回在此，并在这里做进入拍摄界面操作
                SdkEntry.record(context, CAMERA_REQUEST_CODE);
            } else if (resultCode == RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data
                        .getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                initEditorUIAndExportConfig();
                try {
                    editMedia(context, arrMediaListPath, EDIT_REQUEST_CODE);
                } catch (InvalidArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 拍摄界面->图库
     */
    private ActivityResultHandler cameraAlbumResultHandler = new ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> arrMediaListPath = data
                        .getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                initEditorUIAndExportConfig();
                try {
                    editMedia(context, arrMediaListPath, EDIT_REQUEST_CODE);
                } catch (InvalidArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    /**
     * 要导出的视频
     */
    private ActivityResultHandler albumExportResultHandler = new ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode,
                                     Intent data) {
            if (resultCode == RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data
                        .getStringArrayListExtra(SdkEntry.ALBUM_RESULT);

                for (String path : arrMediaListPath) {
                    VideoConfig videoConfig = new VideoConfig();
                    float durationS = VirtualVideo.getMediaInfo(path, videoConfig, true);
                    Log.d(TAG, path + "," + durationS + "," + videoConfig);
                }

                VideoConfig videoConfig = new VideoConfig();
                videoConfig.setVideoEncodingBitRate(4000 * 1000);

                int numChannel = AudioConfigDialog.audioNumChannel;
                if (numChannel == 0) {
                    numChannel = videoConfig.getAudioNumChannels();
                }
                int sampleRate = AudioConfigDialog.audioSampleRate;
                if (sampleRate == 0) {
                    sampleRate = videoConfig.getAudioSampleRate();
                }
                int audioBitRate = AudioConfigDialog.audioBitRate;
                if (audioBitRate == 0) {
                    audioBitRate = videoConfig.getAudioBitrate();
                }
                videoConfig.setAudioEncodingParameters(numChannel, sampleRate, audioBitRate);

                //.水印,左下角
                Watermark watermark = new Watermark(EDIT_WATERMARK_PATH);
                watermark.setShowRect(new RectF(0, 1f, 1f, 1.0f));
                //片尾
                Trailer trailer = new Trailer(SDKUtils.createVideoTrailerImage(ShootActivity.this, "秀友", 480, 50, 50), 2f, 0.5f);

                String exportPath = Environment.getExternalStorageDirectory() + "/export.mp4";
                ExportVideoLisenter mExportListener = new ExportVideoLisenter(exportPath);
                try {
                    SdkEntry.exportVideo(ShootActivity.this, videoConfig, arrMediaListPath, exportPath, watermark, trailer, mExportListener);
                } catch (InvalidArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 导出视频的回调演示
     */
    private class ExportVideoLisenter implements ExportListener {
        private String mPath;
        private AlertDialog mDialog;
        private ProgressBar mProgressBar;
        private Button mBtnCancel;
        private TextView mTvTitle;

        public ExportVideoLisenter(String videoPath) {
            mPath = videoPath;
        }

        @Override
        public void onExportStart() {
            View v = LayoutInflater.from(ShootActivity.this).inflate(
                    R.layout.progress_view, null);
            mTvTitle = (TextView) v.findViewById(R.id.tvTitle);
            mTvTitle.setText("正在导出...");
            mProgressBar = (ProgressBar) v.findViewById(R.id.pbCompress);
            mBtnCancel = (Button) v.findViewById(R.id.btnCancelCompress);
            mBtnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SdkEntry.cancelExport();
                }
            });
            mDialog = new AlertDialog.Builder(ShootActivity.this).setView(v)
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
            mDialog.dismiss();
            if (result >= VirtualVideo.RESULT_SUCCESS) {
                onVideoExportPlay(mPath);
            } else if (result != VirtualVideo.RESULT_SAVE_CANCEL) {
                Log.e(TAG, "onExportEnd: " + result);
                Toast.makeText(ShootActivity.this, "导出失败" + result, Toast.LENGTH_LONG).show();
            }
        }
    }

    private ActivityResultHandler editResultHandler = new ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode, Intent data) {
            if (resultCode == RESULT_OK && null != data) {
                String mediaPath = data.getStringExtra(SdkEntry.EDIT_RESULT);
                String mimeType = data.getStringExtra(SdkEntry.INTENT_KEY_MEDIA_MIME);
                if (mediaPath != null) {
                    Log.d(TAG, mediaPath);
                    onVideoExportPlay(mediaPath);
                }
            }
        }
    };

    private ActivityResultHandler trimResultHandler = new ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode,
                                     Intent data) {
            if (resultCode == RESULT_OK) {
                float startTime = data.getFloatExtra(SdkEntry.TRIM_START_TIME, 0f);
                float endTime = data.getFloatExtra(SdkEntry.TRIM_END_TIME, 0);
                String path = data.getStringExtra(SdkEntry.TRIM_MEDIA_PATH);
                Rect rect = data.getParcelableExtra(SdkEntry.TRIM_CROP_RECT);
                String logInfo = null;
                if (startTime != 0 && endTime != 0) {
                    logInfo = "截取开始时间:" + startTime + "s" + ",结束时间:" + endTime + "s\n裁剪区域：" + rect + "...视频:" + path;
                } else {
                    logInfo = "裁剪区域：" + rect + "...视频:" + path;
                }
                Log.d(TAG, logInfo);
                Toast.makeText(context, logInfo, Toast.LENGTH_LONG).show();
            }
        }
    };

    private ActivityResultHandler trimAlbumResultHandler = new ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode,
                                     Intent data) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> arrCameraMediaListPath = data
                        .getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrCameraMediaListPath != null
                        && arrCameraMediaListPath.get(0) != null) {
                    String path = arrCameraMediaListPath.get(0);
                    initTrimConfig();
                    try {
                        SdkEntry.trimVideo(context, path, TRIM_REQUEST_CODE);
                    } catch (InvalidArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };


    private ActivityResultHandler shortvideoCameraResultHandler = new ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode,
                                     Intent data) {
            if (resultCode == SdkEntry.RESULT_CAMERA_TO_ALBUM) {
                SdkEntry.openAlbum(context,
                        UIConfiguration.ALBUM_SUPPORT_VIDEO_ONLY,
                        SHORTVIDEO_ALBUM_REQUEST_CODE);
            } else if (resultCode == RESULT_OK) {
                FaceuInfo info = data
                        .getParcelableExtra(SdkEntry.INTENT_KEY_FACEU);
                if (null != info) {
                    Log.e("faceu美颜参数", info.toString());
                }

                ArrayList<String> arrMediaListPath = new ArrayList<String>();
                arrMediaListPath.add(data
                        .getStringExtra(SdkEntry.INTENT_KEY_VIDEO_PATH));
                try {
                    editMedia(context, arrMediaListPath, EDIT_REQUEST_CODE);
                } catch (InvalidArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private ActivityResultHandler shortvideoAlbumResultHandler = new ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode,
                                     Intent data) {
            if (resultCode == SdkEntry.RESULT_ALBUM_TO_CAMERA) {
                SdkEntry.record(context, SHORTVIDEO_CAMERA_REQUEST_CODE);
            } else if (resultCode == RESULT_OK) {
                ArrayList<String> arrCameraMediaListPath = data
                        .getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrCameraMediaListPath != null) {
                    if (arrCameraMediaListPath.get(0) != null) {
                        String path = arrCameraMediaListPath.get(0);
                        // 获取视频信息
                        VideoMetadataRetriever vmr = new VideoMetadataRetriever();
                        vmr.setDataSource(path);
                        float duration = Float
                                .valueOf(vmr
                                        .extractMetadata(VideoMetadataRetriever.METADATA_KEY_VIDEO_DURATION));
                        int videoHeight = Integer
                                .valueOf(vmr
                                        .extractMetadata(VideoMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                        int videoWidth = Integer
                                .valueOf(vmr
                                        .extractMetadata(VideoMetadataRetriever.METADATA_KEY_VIDEO_WIDHT));

                        if (duration < 15 && videoHeight == videoWidth) {
                            try {
                                editMedia(ShootActivity.this,
                                        arrCameraMediaListPath,
                                        EDIT_REQUEST_CODE);
                            } catch (InvalidArgumentException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                        try {
                            trimVideo(ShootActivity.this,
                                    arrCameraMediaListPath.get(0),
                                    SHORTVIDEO_TRIM_REQUEST_CODE);
                        } catch (InvalidArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };

    private ActivityResultHandler shortvideoTrimResultHandler = new ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode,
                                     Intent data) {
            if (resultCode == RESULT_OK) {
                EditObject eo = new EditObject(
                        data.getStringExtra(SdkEntry.TRIM_MEDIA_PATH));
                Rect clip = data.getParcelableExtra(SdkEntry.TRIM_CROP_RECT);
                if (null != clip) {
                    eo.setCropRect(new RectF(clip));
                }
                eo.setStartTime(data.getFloatExtra(SdkEntry.TRIM_START_TIME, 0f));
                eo.setEndTime(data.getFloatExtra(SdkEntry.TRIM_END_TIME, 0f));
                try {
                    editMedia(context, eo, EDIT_REQUEST_CODE);
                } catch (InvalidArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * quik
     */
    private ActivityResultHandler ablumQuikResultHandler = new ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode,
                                     Intent data) {
            if (resultCode == RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data
                        .getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0) {
                    try {
                        SdkEntry.quik(ShootActivity.this, arrMediaListPath, EDIT_REQUEST_CODE);
                    } catch (InvalidArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    private ActivityResultHandler albumPlayerResultHandler = new ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode,
                                     Intent data) {
            if (resultCode == RESULT_OK) {
                // 返回选择的视频地址list
                ArrayList<String> arrMediaListPath = data
                        .getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0) {
                    if (!TextUtils.isEmpty(arrMediaListPath.get(0))) {
                        SDKUtils.onPlayVideo(context, arrMediaListPath.get(0));
                    }
                }
            }
        }
    };
    /**
     * 异形显示
     */
    private ActivityResultHandler ablumPointFResultHandler = new ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode,
                                     Intent data) {
            if (resultCode == RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data
                        .getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0) {
                    SdkEntry.onAnimation(ShootActivity.this, arrMediaListPath, false, EDIT_REQUEST_CODE);
                }
            }
        }
    };
    /**
     * 照片电影模式
     */
    private ActivityResultHandler ablumAnimationResultHandler = new ActivityResultHandler() {

        @Override
        public void onActivityResult(Context context, int resultCode,
                                     Intent data) {
            if (resultCode == RESULT_OK) {
                // 返回选择的图片视频地址list
                ArrayList<String> arrMediaListPath = data
                        .getStringArrayListExtra(SdkEntry.ALBUM_RESULT);
                if (arrMediaListPath.size() > 0) {
                    SdkEntry.onAnimation(ShootActivity.this, arrMediaListPath, true, EDIT_REQUEST_CODE);
                }
            }
        }
    };

    public static interface ActivityResultHandler {
        /**
         * 响应
         *
         * @param context
         * @param resultCode The integer result code returned by the child activity
         *                   through its setResult().
         * @param data       An Intent, which can return result data to the caller
         *                   (various data can be attached to Intent "extras").
         */
        void onActivityResult(Context context, int resultCode, Intent data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != registeredActivityResultHandlers) {
            ActivityResultHandler handler = registeredActivityResultHandlers
                    .get(requestCode);
            if (null != handler) {
                handler.onActivityResult(this, resultCode, data);
            }
        }
    }

    private void registerAllResultHandlers() {
        registerActivityResultHandler(CAMERA_REQUEST_CODE,
                cameraResultHandler);
        registerActivityResultHandler(CAMERA_ANTI_CHANGE_REQUEST_CODE,
                cameraAntiChangeResultHandler);

        registerActivityResultHandler(ALBUM_REQUEST_CODE,
                albumResultHandler);
        registerActivityResultHandler(CAMERA_ALBUM_REQUEST_CODE,
                cameraAlbumResultHandler);


        //quik
        registerActivityResultHandler(ALBUM_QUIK_REQUEST_CODE,
                ablumQuikResultHandler);


        registerActivityResultHandler(ALBUM_SOUND_EFFECT_REQUEST_CODE,
                albumSoundEffectResultHandler);
        registerActivityResultHandler(ALBUM_SOUND_EFFECT_NP_REQUEST_CODE,
                albumSoundEffectNpResultHandler);
        registerActivityResultHandler(ALBUM_ALIEN_REQUEST_CODE,
                albumAlienResultHandler);
        registerActivityResultHandler(ALBUM_SILHOUETT_REQUEST_CODE,
                albumSilhouetteResultHandler);
        registerActivityResultHandler(ALBUM_AE_IMAGE_REQUEST_CODE,
                albumAEImageResultHandler);
        registerActivityResultHandler(ALBUM_SPLICE_REQUEST_CODE,
                albumSpliceImageResultHandler);
        registerActivityResultHandler(EDIT_REQUEST_CODE, editResultHandler);
        registerActivityResultHandler(ALBUM_PLAYER_REQUEST_CODE,
                albumPlayerResultHandler);
        registerActivityResultHandler(TRIM_REQUEST_CODE, trimResultHandler);
        registerActivityResultHandler(TRIM_ALBUM_REQUEST_CODE, trimAlbumResultHandler);


        registerActivityResultHandler(SHORTVIDEO_CAMERA_REQUEST_CODE,
                shortvideoCameraResultHandler);
        registerActivityResultHandler(SHORTVIDEO_ALBUM_REQUEST_CODE,
                shortvideoAlbumResultHandler);
        registerActivityResultHandler(SHORTVIDEO_TRIM_REQUEST_CODE,
                shortvideoTrimResultHandler);
        registerActivityResultHandler(ALBUM_REQUEST_EXPORT_CODE, albumExportResultHandler);

    }

    /**
     * 注册响应activity result
     *
     * @param requestCode
     * @param handler
     */
    private void registerActivityResultHandler(int requestCode, ActivityResultHandler handler) {
        if (null == registeredActivityResultHandlers) {
            registeredActivityResultHandlers = new SparseArray<ActivityResultHandler>();
        }
        registeredActivityResultHandlers.put(requestCode, handler);
    }

    /**
     * 初始视频截取配置
     */
    private void initTrimConfig() {
        SdkService sdkService = getSdkService();
        if (null != sdkService) {
            sdkService
                    .initTrimConfiguration(new TrimConfiguration.Builder()
                            //设置实际截取时视频导出最大边,不设置时，默认为960
                            .setVideoMaxWH(960)
                            //设置实际截取时视频导出码率，Mbps为单位,不设置时，默认为4
                            .setVideoBitRate(4)
                            // 设置默认裁剪区域为1:1
                            .setDefault1x1CropMode(
                                    configData.default1x1CropMode)
                            // 设置是否显示1:1裁剪按钮
                            .enable1x1(configData.enable1x1)
                            // 设置截取返回类型
                            .setTrimReturnMode(configData.mTrimReturnMode)
                            // 设置截取类型
                            .setTrimType(configData.mTrimType)
                            // 设置两定长截取时间
                            .setTrimDuration(configData.trimTime1, configData.trimTime2)
                            // 设置单个定长截取时间
                            .setTrimDuration(configData.trimSingleFixedDuration)
                            .get());
        }
    }

    /**
     * 短视频录制推荐参数
     */
    private void initCameraShortVideoConfig() {

        CameraConfiguration cameraConfig = new CameraConfiguration.Builder()
                // 为true代表多次拍摄，拍摄完成一段之后，将保存至相册并开始下一段拍摄，默认为false单次拍摄，拍摄完成后返回资源地址
                .useMultiShoot(false)
                /**
                 * 设置录制时默认界面:<br>
                 * 默认16：9录制:<br>
                 * CameraConfiguration. WIDE_SCREEN_CAN_CHANGE<br>
                 * 默认1：1:<br>
                 * CameraConfiguration. SQUARE_SCREEN_CAN_CHANGE<br>
                 * 仅1：1录制:<br>
                 * CameraConfiguration.ONLY_SQUARE_SCREEN
                 */
                .setCameraUIType(CameraConfiguration.ONLY_SQUARE_SCREEN)
                // 设置拍摄完成后，是否保存至相册（仅单次拍摄方式有效），同时通过onActivityResult及SIMPLE_CAMERA_REQUEST_CODE返回
                .setSingleCameraSaveToAlbum(true)
                // 设置录制时是否静音，true代表录制后无声音
                .setAudioMute(false)
                // 设置是否启用人脸贴纸
                .enableFaceu(false)
                // 设置启用人脸贴纸鉴权证书
                .setPack(authpackArr)
                // 设置是否默认为后置摄像头
                .setDefaultRearCamera(false)
                // 是否显示相册按钮
                .enableAlbum(true)
                // 是否使用自定义相册
                .useCustomAlbum(false)
                // 设置隐藏拍摄功能（全部隐藏将强制开启视频拍摄）
                .hideMV(false).hidePhoto(true).hideRec(true)
                // 设置mv最小时长
                .setCameraMVMinTime(3)
                // 设置mv最大时长
                .setCameraMVMaxTime(15)
                //录制的云音乐
                .setCloudMusicUrl(configData.enableNewApi ? configData.customApi : "")
                //滤镜(lookup)
                .setFilterUrl(configData.enableNewApi ? configData.customApi : "")
                // 强制美颜
                .enableBeauty(true).get();
        // 视频编辑UI配置

        UIConfiguration.Builder builder = new UIConfiguration.Builder()
                // 设置是否使用自定义相册
                .useCustomAlbum(false);
        if (configData.enableNewApi) {
            //是否启用新的网络接口方式(资源放到自己服务器或锐动服务器)
            ConfigData configData = new ConfigData();
            configData.enableMV = true;
            configData.customApi = ShootActivity.this.configData.customApi;

            initThridServer(builder, configData);
        } else {
            //网络音乐
            builder.setMusicUrl(ConfigData.MUSIC_URL)
                    // 设置MV和mv网络地
                    .enableMV(true, ConfigData.WEB_MV_URL)
                    //云音乐
                    .setCloudMusicUrl(ConfigData.CLOUDMUSIC_URL)
                    //自定义lookup滤镜
                    .setFilterUrl(ConfigData.APP_DATA);
        }


        // 设置画面比例为1:1
        builder.setVideoProportion(UIConfiguration.PROPORTION_SQUARE)
                // 相册仅支持视频
                .setAlbumSupportFormat(UIConfiguration.ALBUM_SUPPORT_VIDEO_ONLY)
                // 设置视频选择最大数量
                .setMediaCountLimit(1)
                // 设置隐藏相册中的拍摄按钮
                .enableAlbumCamera(false)
                //显示配乐
                .setEditAndExportModuleVisibility(UIConfiguration.EditAndExportModules.SOUNDTRACK, true)
                //关闭配音
                .setEditAndExportModuleVisibility(UIConfiguration.EditAndExportModules.DUBBING, false)
                // 隐藏字幕
                .setEditAndExportModuleVisibility(UIConfiguration.EditAndExportModules.TITLING,
                        false)
                // 隐藏片段编辑
                .setEditAndExportModuleVisibility(
                        UIConfiguration.EditAndExportModules.CLIP_EDITING, false)
                // 隐藏贴纸
                .setEditAndExportModuleVisibility(
                        UIConfiguration.EditAndExportModules.SPECIAL_EFFECTS, false)
                // 隐藏特效
                .setEditAndExportModuleVisibility(
                        UIConfiguration.EditAndExportModules.EFFECTS, false)
                //隐藏变声
                .enableSoundEffect(false)
                //隐藏去水印
                .enableDewatermark(false)
                //隐藏画中画
                .enableCollage(false)
                //隐藏涂鸦
                .enableGraffiti(false)
                // 启用自动重播
                .enableAutoRepeat(true)
                //设置音效分类、url
                .setSoundUrl(ConfigData.TYPE_URL, ConfigData.SOUND_URL);


        UIConfiguration uiConfig = builder.get();

        TrimConfiguration trimConfig = new TrimConfiguration.Builder()
                //设置实际截取时视频导出最大边,不设置时，默认为960
                .setVideoMaxWH(960)
                //设置实际截取时视频导出码率，Mbps为单位,不设置时，默认为4
                .setVideoBitRate(4)
                // 设置默认裁剪区域为1:1
                .setDefault1x1CropMode(true)
                // 设置截取返回类型
                .setTrimReturnMode(TrimConfiguration.TRIM_RETURN_TIME)
                // 设置截取类型
                .setTrimType(TrimConfiguration.TRIM_TYPE_SINGLE_FIXED)
                // 设置是否显示1：1按钮
                .enable1x1(false)
                // 设置定长截取时间
                .setTrimDuration(15).get();
        getSdkService().initConfiguration(null, uiConfig,
                cameraConfig);
        getSdkService().initTrimConfiguration(trimConfig);
    }

}
