package com.llw.study.activity;

import android.view.KeyEvent;

import com.llw.study.basic.StudyActivity;
import com.llw.study.databinding.ActivityMainBinding;

/**
 * 主页面
 *
 * @author llw
 */
public class MainActivity extends StudyActivity<ActivityMainBinding> {

    @Override
    protected void onCreate() {
        initView();
    }

    private void initView() {
        //进入EasyPermission使用页面
        binding.btnEasyPermissionUsed.setOnClickListener(v -> jumpActivity(EasyPermissionActivity.class));
        //进入相机拍照使用页面
        binding.btnTakePhotoUsed.setOnClickListener(v -> jumpActivity(CameraActivity.class));
        //进入智能刷新使用页面
        binding.btnSmartRefreshUsed.setOnClickListener(v -> jumpActivity(SmartRefreshActivity.class));
        //进入百度定位地图使用页面
        binding.btnBaiduLocationMapUsed.setOnClickListener(v -> jumpActivity(BaiduMapActivity.class));
        //华为扫码服务使用页面
        binding.btnHuaweiScanUsed.setOnClickListener(v -> jumpActivity(HuaweiScanActivity.class));
    }

    private long timeMillis;

    /**
     * 再按一次退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - timeMillis) > 2000) {
                showMsg("再次按下退出应用程序");
                timeMillis = System.currentTimeMillis();
            } else {
                exitTheProgram();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}