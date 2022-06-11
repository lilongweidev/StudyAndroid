package com.llw.study.ui.activity;

import android.Manifest;

import com.llw.study.basic.StudyActivity;
import com.llw.study.databinding.ActivityEasyPermissionBinding;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * EasyPermission使用页面
 *
 * @author llw
 */
public class EasyPermissionActivity extends StudyActivity<ActivityEasyPermissionBinding> {

    /**
     * 权限请求码
     */
    private static final int LOCATION = 100;

    @Override
    protected void onCreate() {
        back(binding.toolbar);
        binding.btnRequestLocation.setOnClickListener(v -> requiresPermission());
    }

    @AfterPermissionGranted(LOCATION)
    private void requiresPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 已经有权限，做你想做的事情
            showMsg("权限已经请求了！");
        } else {
            // 没有权限，现在申请
            EasyPermissions.requestPermissions(this, "请求定位权限", LOCATION, perms);
        }
    }

    /**
     * 请求权限返回
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 将结果转发到 EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}