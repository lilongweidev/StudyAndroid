package com.llw.study.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.llw.study.R;
import com.llw.study.basic.StudyActivity;
import com.llw.study.databinding.ActivityCameraBinding;

/**
 * 拍照页面
 *
 * @author llw
 */
public class CameraActivity extends StudyActivity<ActivityCameraBinding> {

    private static final String TAG = CameraActivity.class.getSimpleName();
    private ActivityResultLauncher<String> requestCamera;
    private ActivityResultLauncher<Uri> uriLauncher;

    //用于保存拍照图片的uri
    private Uri mCameraUri;

    @Override
    protected void onRegister() {
        super.onRegister();
        //请求相机权限返回
        requestCamera = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (result) {
                showMsg("可以打开相机了");
            }
        });

        uriLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            Log.d(TAG, result ? "已拍照" : "未拍照");
            if (result) {
                binding.ivPicture.setImageURI(mCameraUri);
            }
        });
    }

    @Override
    protected void onCreate() {
        back(binding.toolbar);

        binding.btnTakePhoto.setOnClickListener(v -> {
            if (!hasPermission(Manifest.permission.CAMERA)) {
                requestCamera.launch(Manifest.permission.CAMERA);
                return;
            }
            mCameraUri = getContentResolver().insert(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI : MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
            //打开相机
            uriLauncher.launch(mCameraUri);
        });
    }
}