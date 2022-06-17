package com.llw.study.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.llw.study.R;
import com.llw.study.basic.StudyActivity;
import com.llw.study.databinding.ActivityNetworkBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkActivity extends StudyActivity<ActivityNetworkBinding> {

    @Override
    protected void onCreate() {
        back(binding.toolbar);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "{\"data\": \"0\"}";
                new Thread(new Runnable(){
                    @Override
                    public void run() {

                        String img = Environment.getExternalStorageDirectory()+"/1/123456789.png";
                        String url = "http://192.168.0.103:8019/uploadAudio";
                        try {
                            uploadImage(url,img);
                        } catch (IOException e) {
                            Looper.prepare();
                            e.printStackTrace();
                            Looper.loop();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //创建信息对象
                        Message message = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putString("data",result);
                        message.setData(bundle);//向主线程发信息
                        addTrackHandler.sendMessage(message);
                    }
                }).start();

            }
        });
    }

    /**
     * 上传图片
     * @param url
     * @param imagePath 图片路径
     * @return 新图片的路径
     * @throws IOException
     * @throws JSONException
     */
    public static String uploadImage(String url, String imagePath) throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        File file = new File(imagePath);
        RequestBody image = RequestBody.create(MediaType.parse("image/png"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", imagePath, image)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONObject jsonObject = new JSONObject(response.body().string());
        return jsonObject.optString("image");
    }

    Handler addTrackHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String result = "";
            try {
                result = message.getData().getString("data");
                Toast.makeText(NetworkActivity.this, "新增成功", Toast.LENGTH_SHORT).show();
            }catch (Exception e){

            }
            //Toast.makeText(MainActivity.this, "调用成功"+result, Toast.LENGTH_SHORT).show();//测试弹框
            return true;
        }
    });

}