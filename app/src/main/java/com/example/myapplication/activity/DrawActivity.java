package com.example.myapplication.activity;

import android.os.Bundle;
import android.widget.Button;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.view.MyView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 绘画Activity
 */
public class DrawActivity extends AppCompatActivity {

    Button Btn;
    MyView myView;

    int data = 20;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        Btn = findViewById(R.id.Btn);
        myView = findViewById(R.id.myView);

        Btn.setOnClickListener(v -> {
            for (int i = 0; i < 100; i++) {
                myView.addData((int) (1 + Math.random() * (150 - 1 + 1)));
                //myView.addData(data++);
            }
            //myView.addData(data++);
        });
    }
}
