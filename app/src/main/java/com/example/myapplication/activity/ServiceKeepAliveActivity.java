package com.example.myapplication.activity;

import android.os.Bundle;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityServicekeepaliveBinding;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 进程保活页面
 */
public class ServiceKeepAliveActivity extends BaseActivity {

    private ActivityServicekeepaliveBinding activityServicekeepaliveBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityServicekeepaliveBinding = DataBindingUtil.setContentView(this, R.layout.activity_servicekeepalive);
        activityServicekeepaliveBinding.setLifecycleOwner(this);

        //1像素保活法
        //监听锁屏广播和解屏广播  启动和关闭一个 只有1像素的Activity 且是透明的

    }
}
