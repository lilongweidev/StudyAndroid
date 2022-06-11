package com.llw.study.ui.activity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.llw.study.R;
import com.llw.study.basic.StudyActivity;
import com.llw.study.databinding.ActivityNavigationBinding;

public class NavigationActivity extends StudyActivity<ActivityNavigationBinding> {

    private NavController navController;

    @Override
    protected void onCreate() {
        //获取navController
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //通过setupWithNavController将底部导航和导航控制器进行绑定
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
    }

    public void toMy() {
        navController.navigate(R.id.my_fragment);
    }

    public void toMsg() {
        navController.navigate(R.id.msg_fragment);
    }
}