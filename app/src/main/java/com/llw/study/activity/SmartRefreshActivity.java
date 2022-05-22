package com.llw.study.activity;

import android.os.Handler;

import com.llw.study.basic.StudyActivity;
import com.llw.study.databinding.ActivitySmartRefreshBinding;

/**
 * 智能刷新页面
 *
 * @author llw
 */
public class SmartRefreshActivity extends StudyActivity<ActivitySmartRefreshBinding> {

    @Override
    protected void onCreate() {
        back(binding.toolbar);

        binding.refresh.setOnRefreshListener(refreshLayout -> {
            //刷新当前
            new Handler().postDelayed(() -> binding.refresh.finishRefresh(),1000);
            showMsg("下拉刷新数据");
        });
        binding.refresh.setOnLoadMoreListener(refreshLayout -> {
            new Handler().postDelayed(() -> binding.refresh.finishLoadMore(),1000);
            showMsg("上拉加载更多");
        });
    }
}