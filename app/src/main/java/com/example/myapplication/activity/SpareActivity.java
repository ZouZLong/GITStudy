package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.service.ReceptionService;

/**
 * 备用页面
 */
public class SpareActivity extends BaseActivity implements View.OnClickListener {

    private Button button1; //启动一个前台服务
    private Button button2; //去数据库

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spare);
        initView();
        init();
    }

    private void initView() {
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    private void init() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button1:
                Intent intent1 = new Intent(SpareActivity.this, ReceptionService.class);
                if (Build.VERSION.SDK_INT >= 26) {
                    startForegroundService(intent1);
                } else {
                    startService(intent1);
                }
                break;
            case R.id.button2:
                Intent intent2 = new Intent(SpareActivity.this, GreenDaoActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
