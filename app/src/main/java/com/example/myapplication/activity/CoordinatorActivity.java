package com.example.myapplication.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;

import androidx.annotation.Nullable;

public class CoordinatorActivity extends BaseActivity {

    TextView text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);

        text=findViewById(R.id.text);

        for (int i = 0; i < 100; i++) {
            text.setText(text.getText().toString()+"  "+i+"\n\t");
        }

    }
}
