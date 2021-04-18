package com.example.myapplication.bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.utils.ToastUtil;
import com.lwkandroid.rtpermission.RTPermission;
import com.lwkandroid.rtpermission.listener.OnPermissionResultListener;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import okhttp3.internal.http2.Header;

/**
 * 蓝牙
 */
public class BluetoothActivity extends Activity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

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


        findViewById(R.id.button).setOnClickListener(v -> {
            mBluetoothAdapter.startDiscovery();//搜索设备
        });

        IntentFilter intent = new IntentFilter();// 注册Receiver来获取蓝牙设备相关的结果
        intent.addAction(BluetoothDevice.ACTION_FOUND); // 用BroadcastReceiver来取得搜索结果  //发现设备
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); //开始
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); //蓝牙搜索结束
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//设备连接状态改变
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//蓝牙设备状态改变
        BluetoothActivity.this.registerReceiver(mBluetoothReceiver, intent);
    }

    /**
     * 操作蓝牙设备
     */
    Thread blt = new Thread(new Runnable() {
        @Override
        public void run() {
            remoteDevice = mBluetoothAdapter.getRemoteDevice(BluetoothMAC);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

                bluetoothGatt = remoteDevice.connectGatt(BluetoothActivity.this, true, new BluetoothGattCallback() {
                    @Override
                    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                        if (newState == BluetoothGatt.STATE_CONNECTED) {//此处代表连接成功
                            Log.e("123", "此处代表连接成功");
                            mBluetoothAdapter.cancelDiscovery();//停止搜索设备
                            bluetoothGatt.discoverServices(); //启动服务
                        }
                    }

                    @Override
                    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                        if (status == BluetoothGatt.GATT_SUCCESS) {
                            List<BluetoothGattService> services = bluetoothGatt.getServices();
                            for (int i = 0; i < services.size(); i++) {
                                List<BluetoothGattCharacteristic> characteristics = services.get(i).getCharacteristics();
                                for (int j = 0; j < characteristics.size(); j++) {
                                    int charaProp = characteristics.get(j).getProperties();//返回此特征的属性。

                                    //LogUtil.loge("123456:" + characteristics.get(j).getProperties());
                                    if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                                        Log.e("123", "可读：" + "  serviceUUID:" + services.get(i).getUuid() +
                                                "   charUUID:" + characteristics.get(j).getUuid()+"  charaProp:"+characteristics.get(j).getProperties());
                                    }

                                    if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                                        Log.e("123", "可写：" + "  serviceUUID:" + services.get(i).getUuid() +
                                                "   charUUID:" + characteristics.get(j).getUuid()+"  charaProp:"+characteristics.get(j).getProperties());
                                    }

                                    if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                                        Log.e("123", "通知：" + "  serviceUUID:" + services.get(i).getUuid() +
                                                "   charUUID:" + characteristics.get(j).getUuid()+"  charaProp:"+characteristics.get(j).getProperties());
                                    }

                                }
                            }


                            //监听数据
                            handler.postDelayed(() -> {
                                BluetoothGattService service = bluetoothGatt.getService(
                                        UUID.fromString("0000fee7-0000-1000-8000-00805f9b34fb"));
                                BluetoothGattCharacteristic characteristic = service.getCharacteristic(
                                        UUID.fromString("000036f6-0000-1000-8000-00805f9b34fb"));
                                if (null == characteristic) {
                                    Log.e("123", "characteristic:" + characteristic);
                                }
                                setCharacteristicNotification(characteristic, true);
                            }, 500);


                            //写入数据
                            handler.postDelayed(() -> {
                                BluetoothGattService service = bluetoothGatt.getService(
                                        UUID.fromString("0000fee7-0000-1000-8000-00805f9b34fb"));
                                BluetoothGattCharacteristic characteristic = service.getCharacteristic(
                                        UUID.fromString("000036f5-0000-1000-8000-00805f9b34fb"));
                                characteristic.setValue("56F3430769");
                                boolean b = bluetoothGatt.writeCharacteristic(characteristic);

//                                //将指令放置进特征中
//                                characteristic.setValue(new byte[] {0x7e, 0x14, 0x00, 0x00,0x00,(byte) 0xaa});
//                                //设置回复形式
//                                characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
//                                //开始写数据
//                                boolean b =   bluetoothGatt.writeCharacteristic(characteristic);

                                Log.e("123", "是否写入成功:" + b);

                                int properties = characteristic.getProperties();//16  返回此特征的属性
                                Log.e("123","properties:"+properties);
                            }, 1500);

                            //写入数据
                            handler.postDelayed(() -> {
                                BluetoothGattService service = bluetoothGatt.getService(
                                        UUID.fromString("0000fee7-0000-1000-8000-00805f9b34fb"));
                                BluetoothGattCharacteristic characteristic = service.getCharacteristic(
                                        UUID.fromString("000036f5-0000-1000-8000-00805f9b34fb"));
                                characteristic.setValue("DDD6E1603A");
                                boolean b = bluetoothGatt.writeCharacteristic(characteristic);
                                Log.e("123", "是否写入成功:" + b);
                                int properties = characteristic.getProperties();//16  返回此特征的属性
                                Log.e("123","properties:"+properties);
                            }, 1550);

                            //写入数据
                            handler.postDelayed(() -> {
                                BluetoothGattService service = bluetoothGatt.getService(
                                        UUID.fromString("0000fee7-0000-1000-8000-00805f9b34fb"));
                                BluetoothGattCharacteristic characteristic = service.getCharacteristic(
                                        UUID.fromString("000036f5-0000-1000-8000-00805f9b34fb"));
                                characteristic.setValue("E4791A0BD4D1");
                                boolean b = bluetoothGatt.writeCharacteristic(characteristic);
                                Log.e("123", "是否写入成功:" + b);
                                int properties = characteristic.getProperties();//16  返回此特征的属性
                                Log.e("123","properties:"+properties);
                            }, 1600);




                            //读取数据
                            handler.postDelayed(() -> {
                                BluetoothGattService service = bluetoothGatt.getService(
                                        UUID.fromString("00001800-0000-1000-8000-00805f9b34fb"));
                                BluetoothGattCharacteristic characteristic = service.getCharacteristic(
                                        UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb"));
                                boolean b = bluetoothGatt.readCharacteristic(characteristic);
                                Log.e("123", "是否读取成功:" + b);
                            }, 2500);

                        }
                    }


                    @Override
                    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                        if (status == BluetoothGatt.GATT_SUCCESS) {
                            try {
                                byte[] value = descriptor.getValue();
                                String s1 = byteToHexString(value);
                                Log.e("123", "onDescriptorWrite:" + s1);


                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("123", "onDescriptorWrite:" + e.toString());
                            }
                        }
                    }

                    ;

                    @Override
                    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                        byte[] value = characteristic.getValue();
                        try {
                            String s = new String(value, "UTF-8");
                            Log.e("123", "读取数据:" + s);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Log.e("123", "读取数据:" + e.toString());
                        }


                    }

                    @Override
                    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                        byte[] value = characteristic.getValue();
                        try {
                            String s = new String(value, "UTF-8");
                            Log.e("123", "写入数据:" + s);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Log.e("123", "写入数据:" + e.toString());
                        }

                    }

                    @Override
                    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("123", "监听数据被触发");
                            }
                        }, 500);
                    }
                });

            }
        }
    });

    private final Handler handler = new Handler();


    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || bluetoothGatt == null) {
            Log.e("123", "BluetoothAdapter not initialized");
            return;
        }
        bluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
        //Log.e("123", "descriptors长度：" +descriptors.size());
        for (BluetoothGattDescriptor dp : descriptors) {
            boolean b1 = false;
            if ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
                b1 = dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            } else if ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0) {
                b1 = dp.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
            }
            //boolean b1 = dp.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
            if (b1) {
                // bluetoothGatt.requestMtu(60);//字节数过大需要设置接收字节
                bluetoothGatt.writeDescriptor(dp);
                Log.e("123", "描述：" + dp.getUuid().toString());
            }
        }
    }

    private final BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("123", "扫描设备中");
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {//每扫描到一个设备，系统都会发送此广播。 开始搜索
                BluetoothDevice scanDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);//获取蓝牙设备
                if (scanDevice == null || scanDevice.getName() == null) return;
                //Log.e("123", "address=" + scanDevice.getAddress() + "  ");
                if (scanDevice.getAddress().equals(BluetoothMAC)) { //将获取的地址 于设备地址进行匹配
                    mBluetoothAdapter.cancelDiscovery();//停止搜索设备
                    if (!blt.isAlive()) { //判断是否重复执行了这个线程
                        blt.start(); //连接设备
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) { //搜索结束
                mBluetoothAdapter.cancelDiscovery();//停止搜索设备
                Log.e("123", "停止搜索设备");
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

}
