package com.llw.study.ui.activity;

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
        //进入RxPermissions使用页面
        binding.btnRxPermissionUsed.setOnClickListener(v -> jumpActivity(RxPermissionActivity.class));
        //进入EasyPermission使用页面
        binding.btnEasyPermissionUsed.setOnClickListener(v -> jumpActivity(EasyPermissionActivity.class));
        //进入相机拍照、相册获取使用页面
        binding.btnTakePhotoUsed.setOnClickListener(v -> jumpActivity(CameraAlbumActivity.class));
        //进入智能刷新使用页面
        binding.btnSmartRefreshUsed.setOnClickListener(v -> jumpActivity(SmartRefreshActivity.class));
        //进入百度定位地图使用页面
        binding.btnBaiduLocationMapUsed.setOnClickListener(v -> jumpActivity(BaiduMapActivity.class));
        //华为扫码服务使用页面
        binding.btnHuaweiScanUsed.setOnClickListener(v -> jumpActivity(HuaweiScanActivity.class));
        //科大讯飞语音识别使用页面
        binding.btnXunfeiSpeechRecognitionUsed.setOnClickListener(v -> jumpActivity(XunfeiSpeechActivity.class));
        //AndroidPicker使用页面
        binding.btnPickerUsed.setOnClickListener(v -> jumpActivity(PickerActivity.class));
        //ListView使用页面
        binding.btnListViewUsed.setOnClickListener(v -> jumpActivity(ListViewActivity.class));
        //RecyclerView使用页面
        binding.btnRecyclerViewUsed.setOnClickListener(v -> jumpActivity(RecyclerViewActivity.class));
        //Navigation使用页面
        binding.btnNavigationUsed.setOnClickListener(v -> jumpActivity(NavigationActivity.class));
        //网络请求使用页面
        binding.btnNetworkUsed.setOnClickListener(v -> jumpActivity(NetworkActivity.class));
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