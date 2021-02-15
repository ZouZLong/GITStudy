package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.activity.OKHttpActivity;
import com.example.myapplication.databinding.ActivityMainBinding;

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
    }


}