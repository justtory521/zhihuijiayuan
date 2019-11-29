package tendency.hz.zhihuijiayuan.units;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import tendency.hz.zhihuijiayuan.application.MyApplication;

/**
 * Created by JasonYao on 2018/12/29.
 */
public class LocationUtils {
//    public static LocationUtils mInstance = null;
//    //声明LocationClient 对象
//    private LocationClient mLocationClient = null;
//    //声明AMapLocationClientOption对象
//    private LocationClientOption mLocationOption = null;
//
//    /**
//     * 私有构造方法
//     */
//    private LocationUtils() {
//        mLocationClient = new LocationClient(MyApplication.getInstance());
//        mLocationOption = new LocationClientOption();
//        mLocationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        mLocationOption.setCoorType("bd09ll");
//        mLocationOption.setIsNeedAddress(true);
//        mLocationOption.setScanSpan(1000);
//        mLocationOption.setOpenGps(true);
//        mLocationClient.setLocOption(mLocationOption);
//    }
//
//    public static LocationUtils getInstance() {
//        if (mInstance == null) {
//            synchronized (LocationUtils.class) {
//                if (mInstance == null) {
//                    mInstance = new LocationUtils();
//                }
//            }
//        }
//
//        return mInstance;
//    }
//
//    public void setLocationListener(BDAbstractLocationListener listener) {
//        mLocationClient.registerLocationListener(listener);
//    }
//
//    public void startLocation() {
//        mLocationClient.start();
//    }
//
//
//    public void stopLocation() {
//        mInstance = null;
//
//        if (mLocationClient != null) {
//            mLocationClient.stop();
//            mLocationClient = null;
//            mLocationOption = null;
//        }
//    }
}
