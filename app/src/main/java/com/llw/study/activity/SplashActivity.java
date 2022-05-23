package com.llw.study.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.llw.study.R;
import com.llw.study.basic.StudyActivity;
import com.llw.study.databinding.ActivitySplashBinding;

/**
 * 启动页
 */
public class SplashActivity extends StudyActivity<ActivitySplashBinding> {

    @Override
    protected void onCreate() {
        new Handler().postDelayed(() -> jumpActivity(MainActivity.class), 500);
    }
}