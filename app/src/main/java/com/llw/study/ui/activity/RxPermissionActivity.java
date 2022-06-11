package com.llw.study.ui.activity;

import android.Manifest;

import com.llw.study.basic.StudyActivity;
import com.llw.study.databinding.ActivityRxPermissionBinding;
import com.tbruyelle.rxpermissions3.RxPermissions;
/**
 * RxPermissions使用页面
 *
 * @author llw
 */
public class RxPermissionActivity extends StudyActivity<ActivityRxPermissionBinding> {

    private RxPermissions rxPermissions;
    @Override
    protected void onCreate() {
        back(binding.toolbar);
        binding.btnRequestCamera.setOnClickListener(v -> requiresPermission());
        rxPermissions = new RxPermissions(this);
    }

    private void requiresPermission() {
        // Must be done during an initialization phase like onCreate
        rxPermissions
                .request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {
                        showMsg("已获取相机权限");
                    } else {
                        showMsg("权限获取失败");
                    }
                });
    }
}