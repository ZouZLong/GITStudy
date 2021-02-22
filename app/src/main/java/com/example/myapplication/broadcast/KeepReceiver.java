package com.example.myapplication.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 启动一个前台服务ReceptionService (也可以在不是前台服务中创建 不影响)
 * 在前台服务中注册监听屏幕开关的广播KeepReceiver
 * 广播中去判断，屏幕打开就销毁KeepActivity 屏幕关闭则启动KeepActivity
 */
public class KeepReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            if (action.equals("android.intent.action.SCREEN_ON")) {
                Log.e("123","屏幕打开");
            } else if (action.equals("android.intent.action.SCREEN_OFF")) {
                Log.e("123","屏幕关闭");
            }
        }

    }
}
