package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.activity.MemoryLeakActivity;
import com.example.myapplication.activity.OKHttpActivity;
import com.example.myapplication.activity.SpareActivity;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.service.ReceptionService;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setLifecycleOwner(this);
        activityMainBinding.setOnclickMain(new Onclick_Main());  //将两者之间关联起来
    }

    //单击事件需要写一个方法
    public class Onclick_Main {
        //跳转到请求网络的页面
        public void onClick_Okhttp() {
            Intent intent = new Intent(MainActivity.this, OKHttpActivity.class);
            startActivity(intent);
        }

        //跳转到内存泄漏的页面
        public void onClick_MemoryLeak() {
            Intent intent = new Intent(MainActivity.this, MemoryLeakActivity.class);
            startActivity(intent);
        }

        //跳转到备用界面
        public void onClick_ReceptionService() {
            Intent intent = new Intent(MainActivity.this, SpareActivity.class);
            startActivity(intent);
        }
    }


}