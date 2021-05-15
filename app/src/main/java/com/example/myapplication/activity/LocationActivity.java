package com.example.myapplication.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;

import java.util.List;

import androidx.annotation.Nullable;

import static com.amap.api.fence.GeoFenceClient.GEOFENCE_IN;
import static com.amap.api.fence.GeoFenceClient.GEOFENCE_OUT;
import static com.amap.api.fence.GeoFenceClient.GEOFENCE_STAYED;

/**
 * 高德定位 定理围栏
 */
public class LocationActivity extends BaseActivity {

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    GeoFenceClient mGeoFenceClient;
    //定义接收广播的action字符串
    public static final String GEOFENCE_BROADCAST_ACTION = "com.location.apis.geofencedemo.broadcast";


    TextView text, text1, text2,text3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        text = findViewById(R.id.text);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3=findViewById(R.id.text3);

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

        CoordinateConverter converter = new CoordinateConverter(this);
        // CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.GPS);

        try {
            // 执行转换操作
            DPoint desLatLng = converter.convert();
            // sourceLatLng待转换坐标点 DPoint类型
            converter.coord(desLatLng);
        } catch (Exception e) {
            e.printStackTrace();
        }


        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(GEOFENCE_BROADCAST_ACTION);
        registerReceiver(mGeoFenceReceiver, filter);

    }


    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {

            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {//可在其中解析amapLocation获取相应内容。
                    Log.e("123", "获取纬度:" + amapLocation.getLatitude());
                    Log.e("123", "获取经度:" + amapLocation.getLongitude());

                    if (mGeoFenceClient == null) {
                        //实例化地理围栏客户端
                        mGeoFenceClient = new GeoFenceClient(getApplicationContext());
                        //设置希望侦测的围栏触发行为，默认只侦测用户进入围栏的行为
                        //GEOFENCE_IN 进入地理围栏  GEOFENCE_OUT 退出地理围栏  GEOFENCE_STAYED 停留在地理围栏内10分钟
                        mGeoFenceClient.setActivateAction(GEOFENCE_IN | GEOFENCE_OUT | GEOFENCE_STAYED);
                        //创建一个中心点坐标
                        DPoint centerPoint = new DPoint();
                        centerPoint.setLatitude(amapLocation.getLatitude()); //设置中心点纬度
                        centerPoint.setLongitude(amapLocation.getLongitude());//设置中心点经度
                        mGeoFenceClient.addGeoFence(centerPoint, 1, "0");
                        mGeoFenceClient.setGeoFenceListener(fenceListenter);//设置回调监听
                        //创建并设置PendingIntent
                        mGeoFenceClient.createPendingIntent(GEOFENCE_BROADCAST_ACTION);
                    }


                    text.setText("纬度" + amapLocation.getLatitude());
                    text1.setText("经度" + amapLocation.getLongitude());
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("123", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    //创建回调监听
    GeoFenceListener fenceListenter = new GeoFenceListener() {
        @Override
        public void onGeoFenceCreateFinished(List<GeoFence> list, int i, String s) {
            if (i == GeoFence.ADDGEOFENCE_SUCCESS) {//判断围栏是否创建成功
                text2.setText("添加围栏成功!!");
                //geoFenceList是已经添加的围栏列表，可据此查看创建的围栏
            } else {
                text2.setText("添加围栏失败!!");
            }
        }

    };


    private BroadcastReceiver mGeoFenceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(GEOFENCE_BROADCAST_ACTION)) {
                //解析广播内容
                //获取Bundle
                Bundle bundle = intent.getExtras();
                //获取围栏行为：
                int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
                //获取自定义的围栏标识：
                String customId = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
                //获取围栏ID:
                String fenceId = bundle.getString(GeoFence.BUNDLE_KEY_FENCEID);
                //获取当前有触发的围栏对象：
                GeoFence fence = bundle.getParcelable(GeoFence.BUNDLE_KEY_FENCE);

                text3.setText("status:"+status);
            }
        }
    };
}
