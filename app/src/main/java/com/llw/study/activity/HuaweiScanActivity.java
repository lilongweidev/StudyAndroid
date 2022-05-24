package com.llw.study.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.Manifest;
import android.content.Intent;

import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;
import com.llw.study.basic.StudyActivity;
import com.llw.study.databinding.ActivityHuaweiScanBinding;

/**
 * 华为扫码服务使用页面
 * @author llw
 */
public class HuaweiScanActivity extends StudyActivity<ActivityHuaweiScanBinding> {

    private static final String TAG = CameraAlbumActivity.class.getSimpleName();
    private static final int REQUEST_CODE_SCAN = 100;
    private ActivityResultLauncher<String[]> resultLauncher;

    @Override
    protected void onRegister() {
        super.onRegister();
        //请求相机权限返回
        resultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            if (Boolean.TRUE.equals(result.get(Manifest.permission.CAMERA)) &&
                    Boolean.TRUE.equals(result.get(Manifest.permission.READ_EXTERNAL_STORAGE))) {
                showMsg("可以开始扫码了");
            }
        });
    }

    @Override
    protected void onCreate() {
        back(binding.toolbar);
        binding.btnScan.setOnClickListener(v -> {
            //启动扫描Acticity
            ScanUtil.startScan(this, REQUEST_CODE_SCAN, new HmsScanAnalyzerOptions.Creator().create());
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasPermission(Manifest.permission.CAMERA)) {
            //请求权限
            resultLauncher.launch(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE});
        }
    }

    //Activity回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            showMsg("未进行扫码");
            binding.tvScanResult.setText("未进行扫码");
            return;
        }
        if (requestCode == REQUEST_CODE_SCAN) {
            HmsScan hmsScan = data.getParcelableExtra(ScanUtil.RESULT);
            if (hmsScan != null) {
                showMsg(hmsScan.originalValue);
                binding.tvScanResult.setText(hmsScan.originalValue);
            }
        } else {
            showMsg("其他结果");
            binding.tvScanResult.setText("其他结果");
        }
    }


}