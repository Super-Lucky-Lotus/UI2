package com.example.superluckylotus;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Environment;

import androidx.multidex.MultiDex;

import com.example.superluckylotus.ShootSdk.FaceHandler;
import com.veuisdk.MyCrashHandler;
import com.veuisdk.SdkEntry;
import com.veuisdk.manager.ChangeLanguageHelper;

import java.io.File;

/**
 * @version: 3.0
 * @author: 宋佳容
 * @className: Phone
 * @packageName:com.example.superluckylotus
 * @description: 调用通讯录手机号码信息
 * @data: 2020.07.21 11:18
 **/

public class Phone extends Application {

    private static final String APP_KEY = "4dd9bb55dec0319e";
    private static final String APP_SECRET = "cc14aa7f0a2962880dae86a61f87bb1dxHUvLObjU5OlnG6SSIdFuMjAJAyjtemSdd1hnpjjo6DMQieBaVNHgltNifmxdWmkyrpSofemI6WwYMfpTrExBtrhOrYb";

    private String mStrCustomPath;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeSdk();
        ChangeLanguageHelper.init(this);
    }

    private String phone = "";

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }


    /**
     * initSdk
     */
    public void initializeSdk() {
        boolean debugMode = isApkInDebug(this);

        // Here to determine whether to enable logging, this option can be turned on during the debugging stage to facilitate the location of the problem.
        SdkEntry.enableDebugLog(debugMode);
        // Custom root directory, if it is empty, the default is/sdcard/Android/data/com.vesdk.demo/files/ve
        mStrCustomPath = getExternalFilesDirEx(this, "ve").getAbsolutePath();
        SdkEntry.initialize(Phone.this, mStrCustomPath, APP_KEY, APP_SECRET,
                new com.example.superluckylotus.ShootSdk.SdkHandler().getCallBack());
        // Custom Crash handler, the actual project can not be added
        MyCrashHandler.getInstance().init(this);
        FaceHandler.initialize(this);
    }



    /**
     * 判断签名是debug签名还是release签名
     */
    private boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    protected void attachBaseContext(Context base) {
        //7.0 以上，处理初始化时切换语言环境
        super.attachBaseContext(ChangeLanguageHelper.attachBaseContext(base,
                ChangeLanguageHelper.getAppLanguage(base)));
        MultiDex.install(this);
    }


    /**
     * 获取自定义根目录
     */
    public String getCustomPath() {
        return mStrCustomPath;
    }

    private File getExternalFilesDirEx(Context context, String type) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File ef = context.getExternalFilesDir(type);
            if (ef != null && ef.isDirectory()) {
                return ef;
            }
        }
        return new File(Environment.getExternalStorageDirectory(), type);
    }
}
