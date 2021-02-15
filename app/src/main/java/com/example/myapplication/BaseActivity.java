package com.example.myapplication;

import com.example.myapplication.aaa_study.SingleCase;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class BaseActivity extends AppCompatActivity {
    private static OkHttpClient client;
    private static MediaType mediaType;

    public static MediaType getMediaType() {
        if (mediaType == null) {//第一次检查
            synchronized (SingleCase.class) {
                if (mediaType == null) {//第二次检查
                    mediaType = MediaType.Companion.parse("application/json;charset=utf-8");
                }
            }
        }
        return mediaType;
    }

    public static OkHttpClient getClient() {
        if (client == null) {//第一次检查
            synchronized (SingleCase.class) {
                if (client == null) {//第二次检查
                    client = new OkHttpClient();
                }
            }
        }
        return client;
    }
}
