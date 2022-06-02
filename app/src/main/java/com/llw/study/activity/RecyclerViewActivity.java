package com.llw.study.activity;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.llw.study.adapter.StudyAdapter;
import com.llw.study.basic.StudyActivity;
import com.llw.study.databinding.ActivityRecyclerViewBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView使用页面
 */
public class RecyclerViewActivity extends StudyActivity<ActivityRecyclerViewBinding> {

    private final List<String> lists = new ArrayList<>();

    @Override
    protected void onCreate() {
        back(binding.toolbar);

        int num = (int) (1 + Math.random() * (50 - 10 + 1));
        for (int i = 0; i < num; i++ ){
            lists.add("第 " + i + " 条数据");
        }
        StudyAdapter studyAdapter = new StudyAdapter(lists);
        studyAdapter.setOnItemClickListener(new StudyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showMsg(lists.get(position));
            }
        });
        binding.rvStudy.setLayoutManager(new LinearLayoutManager(this));
        binding.rvStudy.setAdapter(studyAdapter);
    }
}