package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;

public class BaseActivity extends AppCompatActivity {
   private OkHttpClient client;

    public OkHttpClient getClient() {

        return client;
    }
}
