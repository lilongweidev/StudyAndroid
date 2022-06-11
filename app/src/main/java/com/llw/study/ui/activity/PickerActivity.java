package com.llw.study.ui.activity;

import com.github.gzuliyujiang.wheelpicker.OptionPicker;
import com.github.gzuliyujiang.wheelpicker.SexPicker;
import com.github.gzuliyujiang.wheelpicker.contract.OnOptionPickedListener;
import com.github.gzuliyujiang.wheelpicker.contract.OnOptionSelectedListener;
import com.github.gzuliyujiang.wheelpicker.widget.OptionWheelLayout;
import com.github.gzuliyujiang.wheelview.annotation.CurtainCorner;
import com.llw.study.basic.StudyActivity;
import com.llw.study.bean.BookBean;
import com.llw.study.databinding.ActivityPickerBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * AndroidPicker使用页面
 */
public class PickerActivity extends StudyActivity<ActivityPickerBinding> implements OnOptionPickedListener {

    @Override
    protected void onCreate() {
        back(binding.toolbar);

        binding.btnOptionBean.setOnClickListener(v -> {
            List<BookBean> data = new ArrayList<>();
            data.add(new BookBean(1, "《三国演义》"));
            data.add(new BookBean(2, "《水浒传》"));
            data.add(new BookBean(3, "《西游记》"));
            data.add(new BookBean(4, "《红楼梦》"));
            OptionPicker picker = new OptionPicker(this);
            picker.setBodyWidth(280);
            picker.setData(data);
            picker.setDefaultPosition(2);
            picker.setOnOptionPickedListener(this);
            OptionWheelLayout wheelLayout = picker.getWheelLayout();
            wheelLayout.setIndicatorEnabled(false);
            wheelLayout.setTextColor(0xFF000000);
            wheelLayout.setSelectedTextColor(0xFF000000);
            wheelLayout.setTextSize(15 * v.getResources().getDisplayMetrics().scaledDensity);
            //注：建议通过`setStyle`定制样式设置文字加大，若通过`setSelectedTextSize`设置，该解决方案会导致选择器展示时跳动一下
            //wheelLayout.setStyle(R.style.WheelStyleDemo);
            wheelLayout.setSelectedTextSize(17 * v.getResources().getDisplayMetrics().scaledDensity);
            wheelLayout.setSelectedTextBold(true);
            wheelLayout.setCurtainEnabled(true);
            wheelLayout.setCurtainColor(0xEE000000);
            wheelLayout.setCurtainCorner(CurtainCorner.ALL);
            wheelLayout.setCurtainRadius(16);
            wheelLayout.setOnOptionSelectedListener(new OnOptionSelectedListener() {
                @Override
                public void onOptionSelected(int position, Object item) {
                    picker.getTitleView().setText(picker.getWheelView().formatItem(position));
                }
            });
            picker.show();
        });

        binding.btnSex.setOnClickListener(v -> {
            SexPicker picker = new SexPicker(this);
            picker.setBodyWidth(140);
            picker.setIncludeSecrecy(false);
            picker.setDefaultValue("女");
            picker.setOnOptionPickedListener(this);
            picker.getWheelLayout().setOnOptionSelectedListener(new OnOptionSelectedListener() {
                @Override
                public void onOptionSelected(int position, Object item) {
                    picker.getTitleView().setText(picker.getWheelView().formatItem(position));
                }
            });
            picker.show();
        });

    }

    @Override
    public void onOptionPicked(int position, Object item) {
        showMsg(position + "-" + item);
    }
}