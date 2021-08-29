package com.example.myapplication.activity;

import android.os.Bundle;
import android.widget.Button;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.FlipViewAdapter;
import com.example.myapplication.view.MyView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import se.emilsjolander.flipview.FlipView;

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

        List<String> list=new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("10");

        FlipView flipView = (FlipView) findViewById(R.id.flipView01);
        FlipViewAdapter adapter = new FlipViewAdapter(this,list);
        flipView.setAdapter(adapter);
    }
}
