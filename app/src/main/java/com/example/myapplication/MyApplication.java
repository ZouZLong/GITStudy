package com.example.myapplication;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.inuker.bluetooth.library.BluetoothClient;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.config.CustomFaceConfig;
import com.tencent.qcloud.tim.uikit.config.GeneralConfig;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

public class MyApplication extends Application {

    /**
     * 系统上下文
     */
    private static Context mAppContext;
    public static final int SDKAPPID = 1400486822;
    public static BluetoothClient bluetoothClient ;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        // 初始化MultiDex
        MultiDex.install(this);

        initOkHttp();//配置OkhttpClient

        // 配置 Config，请按需配置
        TUIKitConfigs configs = TUIKit.getConfigs();
        configs.setSdkConfig(new V2TIMSDKConfig());
        configs.setCustomFaceConfig(new CustomFaceConfig());
        configs.setGeneralConfig(new GeneralConfig());
        TUIKit.init(this, SDKAPPID, configs);

        bluetoothClient = new BluetoothClient(this);
    }

    /**
     * 获取系统上下文：用于ToastUtil类
     */
    public static Context getAppContext() {
        return mAppContext;
    }

    public static BluetoothClient getBluetoothClient() {
        return bluetoothClient;
    }

    /**
     * 配置OkhttpClient
     */
    private void initOkHttp() {
        //修改成自带的cookie持久化，可以解决程序崩溃时返回到
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        //设置可访问所有的https网站
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60000L, TimeUnit.MILLISECONDS)
                .readTimeout(60000L, TimeUnit.MILLISECONDS)
                //配置Log,通过设置拦截器实现，框架中提供了一个LoggerInterceptor，当然你可以自行实现一个Interceptor
                .addInterceptor(new LoggerInterceptor("TAG"))
                //配置持久化Cookie(包含Session)
                .cookieJar(cookieJar)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        // TODO Auto-generated method stub
                        return false;
                    }
                })
                //配置Https
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

}
