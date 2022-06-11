package com.llw.study.ui.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.llw.study.basic.StudyActivity;
import com.llw.study.databinding.ActivityListViewBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView使用页面
 */
public class ListViewActivity extends StudyActivity<ActivityListViewBinding> {

    private final List<String> lists = new ArrayList<>();

    @Override
    protected void onCreate() {
        back(binding.toolbar);
        int num = (int) (1 + Math.random() * (50 - 10 + 1));
        for (int i = 0; i < num; i++ ){
            lists.add("第 " + i + " 条数据");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lists);
        binding.lvStudy.setAdapter(adapter);
        binding.lvStudy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showMsg(lists.get(position));
            }
        });
    }
}