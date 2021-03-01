package com.example.myapplication.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityOkhttpBinding;
import com.example.myapplication.okhttp.LoginLogic;
import com.example.myapplication.utils.HttpUtil;
import com.example.myapplication.utils.ToastUtil;
import com.rabtman.wsmanager.WsManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import okhttp3.Call;
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

    private String TAG="OKHttpActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOkhttpBinding = DataBindingUtil.setContentView(this, R.layout.activity_okhttp);
        activityOkhttpBinding.setLifecycleOwner(this);
        activityOkhttpBinding.setOkHttp(new OnClick_HTTP());
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //取消网络请求,根据tag取消请求
        OkHttpUtils.getInstance().cancelTag(this);
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
//            new Thread() {
//                @Override
//                public void run() {
//                    super.run();
//                    try {
//                        String http = getHTTP("http://www.weather.com.cn/data/sk/101190408.html");
//                        Message message = Message.obtain();
//                        message.what = 0;
//                        message.obj = http;
//                        handler.sendMessage(message);
//                        Log.e("123", "http:" + http);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }.start();

            getJson();
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


    private void getJson() {
        if (HttpUtil.isNetworkAvailable(this)) {
            //执行网络请求接口
            try {
                LoginLogic.Instance().getJsonApi(new GetJsonStringCallback());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ToastUtil.showShortToast(MyApplication.getAppContext().getResources().getString(R.string.network_enable));
        }

//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .build();
//        WsManager wsManager = new WsManager.Builder(this)
//                .wsUrl("ws://localhost:8080/")  //ws连接地址
//                .client(okHttpClient)
//                .build();
    }


    /**
     * get接口的自定义回调函数
     */
    public class GetJsonStringCallback extends StringCallback {
        @Override
        public void onBefore(Request request, int id) {//showProgressDialog("");//显示进度加载框
        }

        @Override
        public void onAfter(int id) {//dismissProgressDialog();//隐藏进度加载框
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            ToastUtil.showShortToast(MyApplication.getAppContext().getResources().getString(R.string.login_again));
            Log.w(TAG, "{onError}e=" + e.toString());
        }

        @Override
        public void onResponse(String response, int id) {
            Log.e(TAG, "onResponse：response=" + response);
            switch (id) {
                case 100://http
                    try {
                        if (response != null && !"".equals(response)) {
                            //解析
                            JSONObject responseObj = new JSONObject(response);
                            activityOkhttpBinding.httpText.setText(responseObj.toString());
                        } else {
                            ToastUtil.showShortToast(MyApplication.getAppContext().getResources().getString(R.string.login_null_exception));
                        }
                    } catch (JSONException e) {
                        ToastUtil.showShortToast(MyApplication.getAppContext().getResources().getString(R.string.login_json_exception));
                    } catch (Exception e) {
                        ToastUtil.showShortToast(MyApplication.getAppContext().getResources().getString(R.string.login_json_exception));
                    } finally {
                    }
                    break;
                case 101://https
                    break;
            }
        }

        @Override
        public void inProgress(float progress, long total, int id) {
            Log.e(TAG, "inProgress:" + progress);
        }
    }

}
