package com.llw.study.ui.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.MemoryFile;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.msc.util.FileUtil;
import com.iflytek.cloud.msc.util.log.DebugLog;
import com.llw.study.R;
import com.llw.study.basic.StudyActivity;
import com.llw.study.databinding.ActivityXunfeiSpeechSynthesisBinding;

import java.util.Vector;

/**
 * 讯飞语音合成（在线）使用页面
 */
public class XunfeiSpeechSynthesisActivity extends StudyActivity<ActivityXunfeiSpeechSynthesisBinding> implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = XunfeiSpeechSynthesisActivity.class.getSimpleName();

    private ActivityResultLauncher<String[]> requestPermission;
    // 语音合成对象
    private SpeechSynthesizer mTts;

    // 默认发音人
    private String voicer = "xiaoyan";

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    //播放的文字
    private String text = "富强、明主、文明、和谐、自由、平等、公正、法制、爱国、敬业、诚信、友善。";

    private Vector<byte[]> container = new Vector<>();
    //内存文件
    MemoryFile memoryFile;
    //总大小
    public volatile long mTotalSize = 0;

    //发音人名称
    private static final String[] arrayName = {"讯飞小燕", "讯飞许久", "讯飞小萍", "讯飞小婧", "讯飞许小宝"};

    //发音人值
    private static final String[] arrayValue = {"xiaoyan", "aisjiuxu", "aisxping", "aisjinger", "aisbabyxu"};

    //数组适配器
    private ArrayAdapter<String> arrayAdapter;

    //语速
    private String speedValue = "50";
    //音调
    private String pitchValue = "50";
    //音量
    private String volumeValue = "50";


    @Override
    protected void onRegister() {
        requestPermission = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            showMsg(Boolean.TRUE.equals(result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE)) ? "权限已获取" : "权限未获取");
        });
    }

    @Override
    protected void onCreate() {
        back(binding.toolbar);
        initView();
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);

    }

    private void initView() {
        binding.btnPlay.setOnClickListener(this);
        binding.btnCancel.setOnClickListener(this);
        binding.btnPause.setOnClickListener(this);
        binding.btnResume.setOnClickListener(this);

        setSeekBar(binding.sbSpeed, 1);
        setSeekBar(binding.sbPitch, 2);
        setSeekBar(binding.sbVolume, 3);

        //将可选内容与ArrayAdapter连接起来
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, arrayName);
        //设置下拉列表的风格
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        binding.spinner.setAdapter(arrayAdapter);
        //添加事件Spinner事件监听
        binding.spinner.setOnItemSelectedListener(this);
    }

    /**
     * 初始化监听。
     */
    private final InitListener mTtsInitListener = code -> {
        Log.i(TAG, "InitListener init() code = " + code);
        if (code != ErrorCode.SUCCESS) {
            showMsg("初始化失败,错误码：" + code);
        } else {
            showMsg("初始化成功");
        }
    };

    /**
     * 参数设置
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //支持实时音频返回，仅在synthesizeToUri条件下支持
            mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            // 设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, speedValue);
            // 设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, pitchValue);
            // 设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, volumeValue);
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
        }
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "false");
        // 设置音频保存路径，保存音频格式支持pcm、wav
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, getExternalFilesDir(null) + "/msc/tts.pcm");
    }

    //设置SeekBar
    private void setSeekBar(SeekBar seekBar, final int type) {

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (type) {
                    case 1://设置语速 范围 1~100
                        speedValue = Integer.toString(progress);
                        break;
                    case 2://设置音调  范围 1~100
                        pitchValue = Integer.toString(progress);
                        break;
                    case 3://设置音量  范围 1~100
                        volumeValue = Integer.toString(progress);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }


    /**
     * 合成回调监听。
     */
    private final SynthesizerListener mTtsListener = new SynthesizerListener() {
        //开始播放
        @Override
        public void onSpeakBegin() {
            Log.i(TAG, "开始播放");
        }
        //暂停播放
        @Override
        public void onSpeakPaused() {
            Log.i(TAG, "暂停播放");
        }
        //继续播放
        @Override
        public void onSpeakResumed() {
            Log.i(TAG, "继续播放");
        }
        //合成进度
        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            Log.i(TAG, "合成进度：" + percent + "%");
        }
        //播放进度
        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            Log.i(TAG, "播放进度：" + percent + "%");
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.setSpan(new BackgroundColorSpan(Color.RED), beginPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            binding.etText.setText(style);
        }
        //播放完成
        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                Log.i(TAG, "播放完成," + container.size());
                DebugLog.LogD("播放完成," + container.size());
                for (int i = 0; i < container.size(); i++) {
                    //写入文件
                    writeToFile(container.get(i));
                }
                //保存文件
                FileUtil.saveFile(memoryFile, mTotalSize, getExternalFilesDir(null) + "/1.pcm");
            } else {
                //异常信息
                showMsg(error.getPlainDescription(true));
            }
        }

        //事件
        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            //	 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	 若使用本地能力，会话id为null
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
                Log.i(TAG, "session id =" + sid);
            }

            //当设置SpeechConstant.TTS_DATA_NOTIFY为1时，抛出buf数据
            if (SpeechEvent.EVENT_TTS_BUFFER == eventType) {
                byte[] buf = obj.getByteArray(SpeechEvent.KEY_EVENT_TTS_BUFFER);
                Log.i(TAG, "bufis =" + buf.length);
                container.add(buf);
            }
        }

    };

    /**
     * 写入文件
     */
    private void writeToFile(byte[] data) {
        if (data == null || data.length == 0) {
            return;
        }
        try {
            if (memoryFile == null) {
                Log.i(TAG, "memoryFile is null");
                String mFilepath = getExternalFilesDir(null) + "/1.pcm";
                memoryFile = new MemoryFile(mFilepath, 1920000);
                memoryFile.allowPurging(false);
            }
            memoryFile.writeBytes(data, 0, (int) mTotalSize, data.length);
            mTotalSize += data.length;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        //检查权限
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission.launch(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_SETTINGS,
                    Manifest.permission.READ_EXTERNAL_STORAGE});
            return;
        }
        if (mTts == null) {
            showMsg("创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
            return;
        }

        switch (v.getId()) {
            case R.id.btn_play://开始合成
                //输入文本
                String etStr = binding.etText.getText().toString().trim();
                if (!etStr.isEmpty()) {
                    text = etStr;
                }
                //设置参数
                setParam();
                //开始合成播放
                int code = mTts.startSpeaking(text, mTtsListener);
                if (code != ErrorCode.SUCCESS) {
                    showMsg("语音合成失败,错误码: " + code);
                }
                break;
            case R.id.btn_cancel://取消合成
                mTts.stopSpeaking();
                break;
            case R.id.btn_pause://暂停播放
                mTts.pauseSpeaking();
                break;
            case R.id.btn_resume://继续播放
                mTts.resumeSpeaking();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        voicer = arrayValue[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}