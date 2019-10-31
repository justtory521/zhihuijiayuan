package tendency.hz.zhihuijiayuan.view.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import tendency.hz.zhihuijiayuan.application.MyApplication;
import tendency.hz.zhihuijiayuan.units.ViewUnits;

/**
 * Created by JasonYao on 2018/7/4.
 */
public class LocationService extends Service {
    private static final String TAG = "LocationService---";

    public  Handler handler;

    private LocationClient locationClient;  //定位对象

    private LocationClientOption clientOption;  //定位选项设置

    private MyBind bind = new MyBind();

    private String mLocationResult;

    private int num = 0;

    @Override
    public void onCreate() {
        getLocation();
        super.onCreate();
    }

    private void getLocation() {
        locationClient = new LocationClient(MyApplication.getInstance());
        locationClient.registerLocationListener(new MyLocationListener());
        clientOption = new LocationClientOption();
        clientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        clientOption.setCoorType("bd09ll");
        clientOption.setIgnoreKillProcess(true);
        clientOption.setScanSpan(1000);
        clientOption.setOpenGps(true);

        locationClient.setLocOption(clientOption);
        locationClient.start();
    }

    private class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null) {
                ViewUnits.getInstance().showToast("无网络连接，定位失败");
                return;
            }

            mLocationResult = bdLocation.getLatitude() + "," + bdLocation.getLongitude();
            if (handler != null) {
                handler.sendEmptyMessage(0);
            }

            num++;
            Log.e(TAG, num + "");
//            Log.e(TAG, mLocationResult);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return bind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class MyBind extends Binder {
        public String getLocationResult() {
            return mLocationResult;
        }
    }

}
