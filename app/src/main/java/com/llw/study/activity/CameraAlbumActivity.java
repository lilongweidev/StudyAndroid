package com.llw.study.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.Manifest;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.llw.study.basic.StudyActivity;
import com.llw.study.databinding.ActivityCameraAlbumBinding;

/**
 * 拍照页面
 *
 * @author llw
 */
public class CameraAlbumActivity extends StudyActivity<ActivityCameraAlbumBinding> {

    private static final String TAG = CameraAlbumActivity.class.getSimpleName();
    private ActivityResultLauncher<String> requestCamera;
    private ActivityResultLauncher<String> requestReadStorage;
    private ActivityResultLauncher<Uri> cameraUriLauncher;
    private ActivityResultLauncher<String> albumUriLauncher;

    //用于保存拍照图片的uri
    private Uri mCameraUri;

    @Override
    protected void onRegister() {
        super.onRegister();
        //请求相机权限返回
        requestCamera = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (result) {
                cameraUriLauncher.launch(mCameraUri);
            }
        });
        //请求文件读取权限返回
        requestReadStorage = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (result) {
                albumUriLauncher.launch("image/*");
            }
        });
        //相机页面返回
        cameraUriLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            if (result) {
                binding.ivPicture.setImageURI(mCameraUri);
            }
        });
        //相册选择返回
        albumUriLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                binding.ivPicture.setImageURI(result);
            }
        });
    }

    @Override
    protected void onCreate() {
        back(binding.toolbar);

        mCameraUri = getContentResolver().insert(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI : MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());

        binding.btnTakePhoto.setOnClickListener(v -> {
            if (!hasPermission(Manifest.permission.CAMERA)) {
                requestCamera.launch(Manifest.permission.CAMERA);
                return;
            }
            //打开相机
            cameraUriLauncher.launch(mCameraUri);
        });
        binding.btnAlbumSelection.setOnClickListener(v -> {
            if (!hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                requestReadStorage.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                return;
            }
            //选择图片
            albumUriLauncher.launch("image/*");
        });

    }
}