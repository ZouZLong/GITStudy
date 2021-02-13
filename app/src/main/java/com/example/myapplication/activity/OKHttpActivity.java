package com.example.myapplication.activity;

import android.os.Bundle;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityOkhttpBinding;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

public class OKHttpActivity extends BaseActivity{

    private ActivityOkhttpBinding activityOkhttpBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOkhttpBinding= DataBindingUtil.setContentView(this, R.layout.activity_okhttp);
        activityOkhttpBinding.setLifecycleOwner(this);
    }

}
