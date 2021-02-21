package com.example.myapplication.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class KeepReceiver extends BroadcastReceiver {

    private static final String TAG="KeepReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
          String action=intent.getAction();
    }
}
