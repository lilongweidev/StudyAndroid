package com.llw.study;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.common.BaiduMapSDKException;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.llw.study.network.NetworkApi;

/**
 * 自定义App
 * @author llw
 * @description StudyApp
 * @date 2022/5/7 23:38
 */
public class StudyApp extends Application {

    @SuppressLint("StaticFieldLeak")
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //初始化百度地图
        initBaiduSDK();
        //初始化讯飞
        SpeechUtility.createUtility(context, SpeechConstant.APPID +"=1071f8ae");
        //初始化网络框架
        NetworkApi.init(new NetworkRequiredInfo(this));
    }

    /**
     * 初始化百度SDK
     */
    private void initBaiduSDK() {
        // 是否同意隐私政策，默认为false
        SDKInitializer.setAgreePrivacy(this, true);
        try {
            // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
            SDKInitializer.initialize(this);
            //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
            SDKInitializer.setCoordType(CoordType.BD09LL);
        } catch (BaiduMapSDKException e) {
            e.printStackTrace();
        }
    }

    public static ActivityManager getActivityManager() {
        return ActivityManager.getInstance();
    }
}
