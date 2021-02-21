package com.example.myapplication.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.example.myapplication.BaseActivity;

import androidx.annotation.Nullable;

/**
 * 只有1像素且是透明的Activity
 */
public class KeepActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window=getWindow();
        window.setGravity(Gravity.START|Gravity.TOP);

        WindowManager.LayoutParams params=window.getAttributes();
        params.width=1;
        params.height=1;
        params.x=0;
        params.y=0;
        window.setAttributes(params);

        //KeepManage.getIn
    }
}
