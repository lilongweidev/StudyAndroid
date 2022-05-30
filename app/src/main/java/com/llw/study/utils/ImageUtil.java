package com.llw.study.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

/**
 * 图片工具类
 */
public class ImageUtil {

    private static final String TAG = ImageUtil.class.getSimpleName();
    private static ImageUtil mInstance;
    public String currentImagePath;

    public static ImageUtil getInstance() {
        if (mInstance == null) {
            synchronized (ImageUtil.class) {
                if (mInstance == null) {
                    mInstance = new ImageUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 创建图片Uri
     */
    public Uri createImageUri(Context context) {
        Uri uri = null;
        try {
            File imageFile = createImageFile(context);
            if (imageFile != null) {
                uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", imageFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

    /**
     * 创建图片文件
     */
    public File createImageFile(Context context) throws IOException {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentImagePath = image.getAbsolutePath();
        return image;
    }


    public String getPath(Context context, Uri uri) {
        String imagePath = "";
        if (DocumentsContract.isDocumentUri(context, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                //解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(context, uri, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(context, contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(context, uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        return imagePath;
    }

    @SuppressLint("Range")
    private String getImagePath(Context context, Uri uri, String selection) {
        String path = null;
        //通过uri和selection来获取真实的图片路径
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 将照片添加到图库
     */
    private void galleryAddPic(Context context){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        if(TextUtils.isEmpty(currentImagePath)){
            File f = new File(currentImagePath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
        }
    }

    public void setPic(ImageView imageView, boolean isGallery){
        if(isGallery){
            galleryAddPic(imageView.getContext());
        }
        if(!TextUtils.isEmpty(currentImagePath)){
            int targetW = imageView.getWidth();
            int targetH = imageView.getHeight();
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;
            int scaleFactor = Math.min(photoW/targetW,photoH/targetH);
            Log.i(TAG,"photoW:" + photoW + "-photoH:" + photoH + "-scaleFactor:" + scaleFactor);

            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;
            Bitmap bitmap = BitmapFactory.decodeFile(currentImagePath,bmOptions);
            imageView.setImageBitmap(bitmap);
        }
    }

}
