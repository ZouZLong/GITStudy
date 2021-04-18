package com.example.myapplication.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleReadRssiResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.lwkandroid.rtpermission.RTPermission;
import com.lwkandroid.rtpermission.listener.OnPermissionResultListener;

import java.util.UUID;

import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;

/**
 * 第三方框架实现蓝牙通信
 */
public class BluetoothActivity1 extends Activity {

    private String BluetoothMAC = "EE:ED:0D:C4:C2:20";

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice remoteDevice;
    private BluetoothGatt bluetoothGatt;

    String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,//允许应用写（非读）用户的外部存储器
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,//能够启动照相机设备的请求
            Manifest.permission.RECORD_AUDIO,//允许应用记录音频信息
            Manifest.permission.ACCESS_COARSE_LOCATION,//用于进行网络定位
            Manifest.permission.ACCESS_FINE_LOCATION//用于访问GPS定位
    };

    BluetoothClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        IntentFilter intent = new IntentFilter();// 注册Receiver来获取蓝牙设备相关的结果
        intent.addAction(BluetoothDevice.ACTION_FOUND); // 用BroadcastReceiver来取得搜索结果  //发现设备
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); //开始
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); //蓝牙搜索结束
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//设备连接状态改变
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//蓝牙设备状态改变
       registerReceiver(mBluetoothReceiver, intent);

        //权限申请
        new RTPermission.Builder()
                .permissions(perms)
                .start(this, new OnPermissionResultListener() {
                    @Override
                    public void onAllGranted(String[] allPermissions) {
                    }

                    @Override
                    public void onDeined(String[] dinedPermissions) {

                    }
                });

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mClient= MyApplication.getBluetoothClient();

        findViewById(R.id.button).setOnClickListener(v -> {
            mBluetoothAdapter.startDiscovery();//搜索设备
        });

    }


    private final BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("123", "扫描设备中");
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {//每扫描到一个设备，系统都会发送此广播。 开始搜索
                BluetoothDevice scanDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);//获取蓝牙设备
                if (scanDevice == null || scanDevice.getName() == null) return;
                Log.e("123", "address=" + scanDevice.getAddress() + "  ");
                if (scanDevice.getAddress().equals(BluetoothMAC)) { //将获取的地址 于设备地址进行匹配
                    mBluetoothAdapter.cancelDiscovery();//停止搜索设备


                    BleConnectOptions options = new BleConnectOptions.Builder()
                            .setConnectRetry(3)   // 连接如果失败重试3次
                            .setConnectTimeout(30000)   // 连接超时30s
                            .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
                            .setServiceDiscoverTimeout(20000)  // 发现服务超时20s
                            .build();

                    mClient.connect(BluetoothMAC, options, new BleConnectResponse() {
                        @Override
                        public void onResponse(int code, BleGattProfile data) {
                            if (code == REQUEST_SUCCESS) {
                                Log.e("123", "data:"+data.toString());
                            }else{
                                Log.e("123", "连接失败");
                            }
                        }
                    });

                    mClient.registerConnectStatusListener(BluetoothMAC, mBleConnectStatusListener);

                    mClient.notify(BluetoothMAC, UUID.fromString("0000fee7-0000-1000-8000-00805f9b34fb"),
                            UUID.fromString("000036f6-0000-1000-8000-00805f9b34fb"), new BleNotifyResponse() {
                        @Override
                        public void onNotify(UUID service, UUID character, byte[] value) {
                            String s = byteToHexString(value);
                            Log.e("123", "notify:" + s);
                        }

                        @Override
                        public void onResponse(int code) {
                            if (code == REQUEST_SUCCESS) {
                                Log.e("123", "监听成功");
                            }else{
                                Log.e("123", "监听失败");
                            }
                        }
                    });

                    mClient.write(BluetoothMAC, UUID.fromString("0000fee7-0000-1000-8000-00805f9b34fb"),
                            UUID.fromString("000036f5-0000-1000-8000-00805f9b34fb"), stringToByte("56F3430769DDD6E1603AE4791A0BD4D1"), new BleWriteResponse() {
                        @Override
                        public void onResponse(int code) {
                            if (code == REQUEST_SUCCESS) {
                                Log.e("123", "写入成功");
                            }else{
                                Log.e("123", "写入失败"+code);
                            }
                        }
                    });

                    mClient.read(BluetoothMAC, UUID.fromString("00001800-0000-1000-8000-00805f9b34fb"),
                            UUID.fromString("00002a04-0000-1000-8000-00805f9b34fb"), new BleReadResponse() {
                        @Override
                        public void onResponse(int code, byte[] data) {
                            if (code == REQUEST_SUCCESS) {
                                String s = byteToHexString(data);
                                Log.e("123", "value:" + s);
                            }else{
                                Log.e("123", "读取失败");
                            }
                        }
                    });
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) { //搜索结束
                mBluetoothAdapter.cancelDiscovery();//停止搜索设备
                Log.e("123", "停止搜索设备");
            }

        }
    };

    private final BleConnectStatusListener mBleConnectStatusListener = new BleConnectStatusListener() {

        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (status == STATUS_CONNECTED) {
                Log.e("123", "AAA");
            } else if (status == STATUS_DISCONNECTED) {
                Log.e("123", "BBB");
            }
        }
    };

    /**
     * 将byte转换为16进制字符串
     */
    public static String byteToHexString(byte[] src) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xff;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                sb.append("0");
            }
            sb.append(hv);
        }
        return sb.toString();
    }

    /**
     * 字符串转换为字节数组
     */
    public byte[] stringToByte  (String strings) {
        byte[] result=new byte[strings.length()/2];
        for (int i = 0; i < strings.length(); i += 2) {
            result[i/2]=(byte) (charToInt(strings.charAt(i))*16+charToInt(strings.charAt(i+1)));
        }
        return result;
    }

    private int charToInt(char ch) {
        if (ch >= 65 && ch <= 70) {
            return ch - 55;
        } else if (ch >= 97 && ch <= 102) {
            return ch - 87;
        } else {
            return ch;
        }
    }

}
