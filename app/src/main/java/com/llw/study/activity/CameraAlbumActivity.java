package com.llw.study.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.llw.study.basic.StudyActivity;
import com.llw.study.databinding.ActivityCameraAlbumBinding;
import com.llw.study.utils.BitmapUtil;
import com.llw.study.utils.ImageUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
    private ActivityResultLauncher<Intent> albumUriLauncher;

    //用于保存拍照图片的uri
    private Uri mCameraUri;

    private ImageUtil imageUtil;

    @Override
    protected void onRegister() {
        super.onRegister();
        //请求相机权限返回
        requestCamera = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (result) {
                mCameraUri = imageUtil.createImageUri(this);
                cameraUriLauncher.launch(mCameraUri);
            }
        });
        //请求文件读取权限返回
        requestReadStorage = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (result) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                albumUriLauncher.launch(intent);
            }
        });
        //相机页面返回
        cameraUriLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            if (result) {
                Bitmap bitmap = BitmapFactory.decodeFile(imageUtil.currentImagePath);
                binding.ivPicture.setImageBitmap(bitmap);
                binding.tvImgUrl.setText(imageUtil.currentImagePath);
                String base64 = BitmapUtil.bitmapToBase64(bitmap);
                Log.d(TAG, "base64: data:image/jpeg;base64,"+base64);
                new Handler().postDelayed(() -> {
                    Bitmap testBitmap = BitmapUtil.base64ToBitmap(base64);
                    binding.ivPicture.setImageBitmap(testBitmap);
                    Log.d(TAG, "onRegister: 重新设置图片");

                },2000);
            }
        });
        //图片选择返回
        albumUriLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Uri data = result.getData().getData();
                String imagePath = imageUtil.getPath(this,data);
                if(!TextUtils.isEmpty(imagePath)){
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    binding.ivPicture.setImageBitmap(bitmap);
                    binding.tvImgUrl.setText(imagePath);
                    String base64 = BitmapUtil.bitmapToBase64(bitmap);
                    Log.d(TAG, "base64: "+base64);
                }
            }
        });
    }

    @Override
    protected void onCreate() {
        back(binding.toolbar);
        imageUtil = ImageUtil.getInstance();

        binding.btnTakePhoto.setOnClickListener(v -> {
            if (!hasPermission(Manifest.permission.CAMERA)) {
                requestCamera.launch(Manifest.permission.CAMERA);
                return;
            }
            mCameraUri = imageUtil.createImageUri(this);
            //打开相机
            cameraUriLauncher.launch(mCameraUri);
        });
        binding.btnAlbumSelection.setOnClickListener(v -> {
            if (!hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                requestReadStorage.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                return;
            }
            //选择图片
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            albumUriLauncher.launch(intent);
        });

    }
}