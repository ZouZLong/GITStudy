package com.example.myapplication.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import java.util.UUID;

import androidx.annotation.Nullable;

/**
 * 蓝牙服务
 */
public class BluetoothService extends Service {

    BluetoothAdapter mBluetoothAdapter; //蓝牙的适配器
    BluetoothGattServer bluetoothGattServer;
    BluetoothGattService bluetoothGattService;
    BluetoothDevice bluetoothDevice; //设备

    Intent mIntent;
    Handler handler;

    UUID SERVICE_UUID= UUID.fromString("");

    UUID STIC_UUID= UUID.fromString("");

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BluetoothGattService service=new BluetoothGattService(SERVICE_UUID,BluetoothGattService.SERVICE_TYPE_PRIMARY);
            BluetoothGattCharacteristic bluetoothGattCharacteristic=new BluetoothGattCharacteristic(
                    STIC_UUID,0X0A,0X11);
        }
    }
}
