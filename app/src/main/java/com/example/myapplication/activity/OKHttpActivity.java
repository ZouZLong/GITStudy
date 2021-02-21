package com.example.myapplication.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityOkhttpBinding;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 网络请求的类
 */
public class OKHttpActivity extends BaseActivity {
    //https://github.com/hongyangAndroid/okhttputils
    //okhttputils 封装好的okhttp类  但是已经停止维护
    private ActivityOkhttpBinding activityOkhttpBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOkhttpBinding = DataBindingUtil.setContentView(this, R.layout.activity_okhttp);
        activityOkhttpBinding.setLifecycleOwner(this);
        activityOkhttpBinding.setOkHttp(new OnClick_HTTP());
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    activityOkhttpBinding.httpText.setText(msg.obj.toString());
                    break;
            }
        }
    };


    public String getHTTP(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = getClient().newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String postHTTP(String url, String json) throws IOException {
        RequestBody body = RequestBody.Companion.create(json, getMediaType());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = getClient().newCall(request).execute()) {
            return response.body().string();
        }
    }

    public class OnClick_HTTP {

        public void onClick_get() {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        String http = getHTTP("http://www.weather.com.cn/data/sk/101190408.html");
                        Message message = Message.obtain();
                        message.what = 0;
                        message.obj = http;
                        handler.sendMessage(message);
                        Log.e("123", "http:" + http);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }


        public void onClick_post() {
            Toast.makeText(OKHttpActivity.this, "...", Toast.LENGTH_SHORT).show();
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        Map<String, Object> data = new HashMap<>();
                        data.put("deviceID", "");
                        data.put("UserName", "15818089055");
                        data.put("password", "666666");
                        JSONObject json = new JSONObject(data);
                        String http = postHTTP("http://fuhuibaapi.fuhuiba.ltd/api/User/Login", json.toString());
                        Message message = Message.obtain();
                        message.what = 0;
                        message.obj = http;
                        handler.sendMessage(message);
                        Log.e("123", "http:" + http);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

}
