package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.aaa_study.SingleCase;
import com.example.myapplication.databinding.ActivityMemoryleakBinding;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 会导致内存泄露的Activity
 */
public class MemoryLeakActivity extends BaseActivity {

    private ActivityMemoryleakBinding activityMemoryleakBinding;
    private static SingleCase singleCase;
    private static Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMemoryleakBinding = DataBindingUtil.setContentView(this, R.layout.activity_memoryleak);
        activityMemoryleakBinding.setLifecycleOwner(this);
        activityMemoryleakBinding.setSingleCase(new Onclick_MemoryLeak());
       // context=this;
    }

    public class Onclick_MemoryLeak {
        public void onClick_SingleCase() {
            getError_Instance(MemoryLeakActivity.this);
        }
    }


    //会导致内存泄露的单例  持有一个较短的生命周期的Actvivty
    public static SingleCase getError_Instance(Context con) {
        Toast.makeText(con, "111", Toast.LENGTH_SHORT).show();
        context = con;
        if (singleCase == null) {
            synchronized (SingleCase.class) {
                if (singleCase == null) {
                    singleCase = new SingleCase();
                }
            }
        }
        return singleCase;
    }
}
