package com.example.myapplication.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.R;
import com.example.myapplication.broadcast.KeepReceiver;

/**
 * 这是一个前台服务
 */
public class ReceptionService extends Service {

    private KeepReceiver keepReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        //重要3，大坑，不能重写onBind方法，重写的话要返回super.onBind(),否则onHandleWork不会回调。
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("123", "ReceptionService服务被启动");
        keepReceiver = new KeepReceiver();
        /* 注册屏幕唤醒时的广播 */
        IntentFilter mScreenOnFilter = new IntentFilter("android.intent.action.SCREEN_ON");
        registerReceiver(keepReceiver, mScreenOnFilter);
        /* 注册机器锁屏时的广播 */
        IntentFilter mScreenOffFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
        registerReceiver(keepReceiver, mScreenOffFilter);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //重要4，重写onStartCommand方法时要返回super.onStartCommand()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//判断是否为8.0以上：Build.VERSION_CODES.O为26
            int importance = NotificationManager.IMPORTANCE_HIGH;
            String channelId = "Push";
            String channelName = "推送消息";
            createNotificationChannel(channelId, channelName, importance);
        }
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //创建Notification，传入Context和channelId
        Notification notification = new NotificationCompat.Builder(this, "Push")
                .setAutoCancel(true)
                .setContentTitle("程序的前台进程")
                .setContentText("正在运行中")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                //.setContentIntent(pIntent) //意图
                .build();
        if (manager != null) {
            startForeground(1, notification);
        } else {
            Toast.makeText(this, "manager==null", Toast.LENGTH_SHORT).show();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("123", "ReceptionService服务被销毁");
        stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知
        unregisterReceiver(keepReceiver);
    }


    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        } else {
            Toast.makeText(this, "notificationManager==null", Toast.LENGTH_SHORT).show();
        }

    }
}
