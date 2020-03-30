package tendency.hz.zhihuijiayuan.units;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StatFs;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjt2325.cameralibrary.util.LogUtil;
import com.google.gson.Gson;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.tencent.bugly.beta.Beta;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import exocr.exocrengine.CaptureActivity;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.application.MyApplication;
import tendency.hz.zhihuijiayuan.bean.AddressBookBean;
import tendency.hz.zhihuijiayuan.bean.Bt;
import tendency.hz.zhihuijiayuan.bean.CardOrder;
import tendency.hz.zhihuijiayuan.bean.City;
import tendency.hz.zhihuijiayuan.bean.DialogBean;
import tendency.hz.zhihuijiayuan.bean.MenuBean;
import tendency.hz.zhihuijiayuan.bean.ToastBean;
import tendency.hz.zhihuijiayuan.bean.base.Config;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.bean.base.What;
import tendency.hz.zhihuijiayuan.fragment.DownLoadFragment;
import tendency.hz.zhihuijiayuan.fragment.FingerPrintFragment;
import tendency.hz.zhihuijiayuan.fragment.PlayAudioFragment;
import tendency.hz.zhihuijiayuan.fragment.PlayAudioFragment1;
import tendency.hz.zhihuijiayuan.fragment.RecordAudioFragment;
import tendency.hz.zhihuijiayuan.inter.AndroidToJSCallBack;
import tendency.hz.zhihuijiayuan.inter.ShareResultInter;
import tendency.hz.zhihuijiayuan.inter.ValidateListener;
import tendency.hz.zhihuijiayuan.listener.MyUMShareListener;
import tendency.hz.zhihuijiayuan.view.ScanQRCodeActivity;
import tendency.hz.zhihuijiayuan.view.VideoRecorderActivity;
import tendency.hz.zhihuijiayuan.view.card.CardContentActivity;
import tendency.hz.zhihuijiayuan.view.picker.CheckstandActivity;
import tendency.hz.zhihuijiayuan.view.picker.CityPickerActivity;
import tendency.hz.zhihuijiayuan.view.set.ValidateActivity;
import tendency.hz.zhihuijiayuan.view.user.LoginActivity;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;
import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;
import static tendency.hz.zhihuijiayuan.bean.base.Request.Permissions.REQUEST_CAMERA;
import static tendency.hz.zhihuijiayuan.bean.base.Request.Permissions.REQUEST_CONTACTS_PERMISSIONS;
import static tendency.hz.zhihuijiayuan.bean.base.Request.Permissions.REQUEST_RECORD_PERMISSIONS;

/**
 * Created by JasonYao on 2018/9/3.
 */
public class AndroidtoJS implements ShareResultInter {
    private static final String TAG = "libin";

    //巡逻相关
    public LocationClient mBaiduLocationClient;

    //定位相关
    public LocationClient mBaiduLocationClient1;


    private AndroidToJSCallBack mCallBack;
    private Gson mGson = new Gson();

    private Bt mConnectBt = new Bt();

    //传感器
    private SensorManager mSensorManager;
    //陀螺仪callback
    private String gyCallback;
    //加速仪callback
    private String acCallback;
    //陀螺仪检测间隔时间
    private double gyTime;
    //加速仪检测间隔时间
    private double acTime;
    //卡片参数
    public String cardUrlParams;

    private CardContentActivity mContext;


    public AndroidtoJS(Context context, AndroidToJSCallBack callBack) {
        mContext = (CardContentActivity) context;
        this.mCallBack = callBack;
    }


    @JavascriptInterface
    public void hello(Map<String, String> object) {

    }

    /**
     * 获取设备唯一标识码
     *
     * @return
     */
    @JavascriptInterface
    public void getDeviceId(String callBack) {
        Log.d(TAG, "getDeviceId: " + callBack);
        sendCallBack(callBack, "200", "success", BaseUnits.getInstance().getPhoneKey());
    }

    /**
     * 获取设备硬件信息
     *
     * @param callBack
     */
    @JavascriptInterface
    public void getPhoneInfo(String callBack) {
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("mac", MacUtils.getMobileMAC());
            data.put("imei", BaseUnits.getInstance().getIMEI());
            data.put("imsi", BaseUnits.getInstance().getIMSI());
            data.put("phone", BaseUnits.getInstance().getTel());
            jsonObject.put("status", "200");
            jsonObject.put("msg", "success");
            jsonObject.put("data", data);
            Log.e(TAG, String.valueOf(jsonObject));
            mCallBack.callBackResult(callBack, jsonObject.toString());
        } catch (JSONException e) {
            mCallBack.callBackResult(callBack, "未知错误，联系管理员");
            e.printStackTrace();
        }
    }

    /**
     * 获取用户UserId（在该项目中，userid使用Token标识）
     *
     * @return
     */
    @JavascriptInterface
    public void getUserId(String callBack) {
        sendCallBack(callBack, "200", "success", UserUnits.getInstance().getToken());
    }

    /**
     * 获取当前定位城市
     *
     * @return
     */
    @JavascriptInterface
    public void getLocation(String callBack) {

        String cityName = UserUnits.getInstance().getLocation();
        if (TextUtils.isEmpty(cityName)) {
            sendCallBack(callBack, "500", "fail", "获取当前定位城市失败");
        } else {
            JSONObject jsonObject = new JSONObject();
            JSONObject data = new JSONObject();
            try {

                data.put("cityName", cityName);
                data.put("cityCode", ConfigUnits.getInstance().getCityIdByName(cityName));
                jsonObject.put("status", "200");
                jsonObject.put("msg", "success");
                jsonObject.put("data", data);
                mCallBack.callBackResult(callBack, jsonObject.toString());
            } catch (JSONException e) {
                mCallBack.callBackResult(callBack, "未知错误，联系管理员");
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取版本号
     *
     * @return
     */
    @JavascriptInterface
    public void getVersionNO(String callBack) {
        sendCallBack(callBack, "200", "success", "v." + BaseUnits.getInstance().getVerName(MyApplication.getInstance()));
    }

    /**
     * 版本号
     */
    @JavascriptInterface
    public void versionUpdate() {
        Beta.checkUpgrade();
    }

    /**
     * 扫描二维码
     *
     * @return
     */
    @JavascriptInterface
    public void qrScan(String callBack) {

        if (BaseUnits.getInstance().checkPermission(mContext, Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MyApplication.getInstance().getBaseContext(), ScanQRCodeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("callBack", callBack);
            mContext.startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(mContext,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }

    }

    /**
     * 获取当前选择城市
     *
     * @param callBack
     */
    @JavascriptInterface
    public void getCurrentCity(String callBack) {
        String cityName = UserUnits.getInstance().getSelectCity();
        if (TextUtils.isEmpty(cityName)) {
            sendCallBack(callBack, "500", "fail", "当前选择城市不存在");
        } else {
            sendCallBack(callBack, "200", "success", ConfigUnits.getInstance().getCityIdByName(cityName));
        }

    }

    @JavascriptInterface
    public void getStatusBarHeight(String callBack) {
        sendCallBack(callBack, "200", "success", ViewUnits.getInstance().px2dip(BaseUnits.getInstance().getStatusBarHeight()) + "");
    }

    @JavascriptInterface
    public void getUserInfo(String callBack) {
        Log.e(TAG, "getUserInfo");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("clientId", BaseUnits.getInstance().getPhoneKey());
            jsonObject.put("token", UserUnits.getInstance().getToken());
            jsonObject.put("loginStatus", FormatUtils.getInstance().isEmpty(UserUnits.getInstance().getToken()) ? "0" : "1");
            jsonObject.put("phone", UserUnits.getInstance().getPhone());
            jsonObject.put("realName", UserUnits.getInstance().getRealName());
            jsonObject.put("CardID", UserUnits.getInstance().getCardId());
            jsonObject.put("status", UserUnits.getInstance().getStatus());
            jsonObject.put("headImgPath", UserUnits.getInstance().getHeadImgPath());
            jsonObject.put("nickName", UserUnits.getInstance().getNickName());
            jsonObject.put("sex", UserUnits.getInstance().getSex());
            jsonObject.put("birthDay", UserUnits.getInstance().getBirthDay());
            jsonObject.put("address", UserUnits.getInstance().getAddress());
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        Log.e(TAG, jsonObject.toString());

        sendCallBackJson(callBack, "200", "success", jsonObject);
    }

    /**
     * 拨打电话
     *
     * @param number
     */
    @JavascriptInterface
    public void call(String number) {

        if (BaseUnits.getInstance().checkPermission(mContext, Manifest.permission.CALL_PHONE)) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplication.getInstance().startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(mContext,
                    new String[]{Manifest.permission.CALL_PHONE}, Request.Permissions.REQUEST_CALL_PHONE);
        }


    }

    /**
     * 退出页面
     */
    @JavascriptInterface
    public void finish() {
        if (mContext != null) {
            mContext.closeCard();
        }
    }

    /**
     * 获取百度定位点
     *
     * @return
     */
    @JavascriptInterface
    public void getBaiduCoordinate(String callBack) {
        LogUtils.log(callBack);

        mBaiduLocationClient1 = new LocationClient(MyApplication.getInstance());

        LocationClientOption mBaiduOption1 = new LocationClientOption();

        mBaiduOption1.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        mBaiduOption1.setCoorType("bd09ll");
        mBaiduOption1.setIgnoreKillProcess(false);
        mBaiduOption1.setScanSpan(0);

        mBaiduOption1.setOpenGps(true);
        mBaiduLocationClient1.setLocOption(mBaiduOption1);
        mBaiduLocationClient1.start();
        mBaiduLocationClient1.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation != null && bdLocation.getLatitude() > 1 && bdLocation.getLongitude() > 1) {
                    sendCallBack(callBack, "200", "success", bdLocation.getLatitude() + "," + bdLocation.getLongitude());
                    if (mBaiduLocationClient1 != null) {
                        mBaiduLocationClient1.stop();
                        mBaiduLocationClient1 = null;
                    }
                } else {
                    sendCallBack(callBack, "500", "fail", "定位失败");
                }

            }

            @Override
            public void onLocDiagnosticMessage(int i, int i1, String s) {
                LogUtils.log("定位失败：" + i1 + "," + s);
                sendCallBack(callBack, "500", "fail", s);
            }
        });
    }

    /**
     * 支付
     *
     * @param data
     */
    @JavascriptInterface
    public void pay(String data) {
        Log.e(TAG, data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            CardOrder cardOrder = mGson.fromJson(jsonObject.toString(), CardOrder.class);

            if (cardOrder != null) {
                Intent intent = new Intent(mContext, CheckstandActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("CardOrder", cardOrder);
                intent.putExtra("CallBack", cardOrder.getCallback());
                intent.putExtra("cardId", mContext.getCardId());
                mContext.startActivity(intent);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 分享
     *
     * @param jsonObjectStr
     */
    @JavascriptInterface
    public void share(String jsonObjectStr) {
        try {
            JSONObject jsonObject = new JSONObject(jsonObjectStr);
            String mCallBack = jsonObject.getString("callback");
            String img = jsonObject.getString("img");
            String title = jsonObject.getString("title");
            String url = jsonObject.getString("url");
            String content = jsonObject.getString("content");
            String type = jsonObject.getString("type");
            switch (type) {
                case What.ShareType.SHARETYPE_TEXT:
                    shareText(mCallBack, title, content);
                    break;
                case What.ShareType.SHARETYPE_IMG:
                    shareImage(mCallBack, img);
                    break;
                case What.ShareType.SHARETYPE_URL:
                    shareUrl(mCallBack, title, content, url, img);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置标题栏
     *
     * @param titleBar 0：黑色返回按钮，1：白色返回按钮，2：灰色返回按钮
     */
    @JavascriptInterface
    public void setTitleBar(String titleBar) {
        LogUtils.log("状态栏：" + titleBar);
        try {
            JSONObject jsonObject = new JSONObject(titleBar);
            Log.e(TAG, jsonObject.toString());
            String btnBackKey = jsonObject.getString("btnBackKey");
            String title = jsonObject.getString("tit");
            String textColor = jsonObject.getString("textColor");
            String bgColor = jsonObject.getString("bgColor");
            String isDisplay = jsonObject.getString("isShow");  //0:不显示  1:显示
            mCallBack.setTitleBar(btnBackKey, title, FormatUtils.getInstance().colorTo6Color(textColor), FormatUtils.getInstance().colorTo6Color(bgColor), isDisplay);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }


    /**
     * 设置按钮
     *
     * @param value 1:浅色 2：深色
     */
    @JavascriptInterface
    public void setToolbar(String value) {
        LogUtils.log("按钮：" + value);
        try {
            JSONObject jsonObject = new JSONObject(value);
            int toolbarColor = jsonObject.getInt("colorType");
            if (toolbarColor != 2) {
                mCallBack.setBtnStyle("#000000", "#EAEAEA");
            } else {
                mCallBack.setBtnStyle("#FFFFFF", "#80FFFFFF");
            }
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

    }


    @JavascriptInterface
    public void beginPatrol(String callBack) {
        if (mBaiduLocationClient == null) {

            mBaiduLocationClient = new LocationClient(MyApplication.getInstance());

            LocationClientOption mBaiduOption = new LocationClientOption();

            mBaiduOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
            mBaiduOption.setCoorType("bd09ll");

            mBaiduOption.setIgnoreKillProcess(false);
            mBaiduOption.setScanSpan(7000);

            mBaiduOption.setOpenGps(true);
            mBaiduLocationClient.setLocOption(mBaiduOption);
            mBaiduLocationClient.start();
            mBaiduLocationClient.registerLocationListener(new BDAbstractLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    if (bdLocation != null && bdLocation.getLatitude() > 1 && bdLocation.getLongitude() > 1) {
                        sendCallBack(callBack, "200", "success", bdLocation.getLatitude() + "," + bdLocation.getLongitude());
                    } else {
                        sendCallBack(callBack, "500", "fail", "定位失败");
                    }
                }


                @Override
                public void onLocDiagnosticMessage(int i, int i1, String s) {
                    LogUtils.log("定位失败：" + i1 + "," + s);
                    sendCallBack(callBack, "500", "success", s);
                }
            });
        } else if (!mBaiduLocationClient.isStarted()) {
            mBaiduLocationClient.start();
        }
    }

    /**
     * 结束巡逻（百度坐标系统）
     */
    @JavascriptInterface
    public void endPatrol() {
        if (mBaiduLocationClient != null) {
            LogUtils.log("结束巡逻");
            mBaiduLocationClient.stop();
            mBaiduLocationClient = null;
        }
    }

    /**
     * 获取照片（拍照、相册选择）
     *
     * @param callBack
     */
    @JavascriptInterface
    public void getPhoto(String callBack) {
        mContext.takePhoto(imageData -> {
            Log.e(TAG, imageData);
            sendCallBack(callBack, "200", "success", "data:image/jpeg;base64," + imageData);
        });
    }

    /**
     * 获取照片（拍照、相册选择）
     *
     * @param value
     */
    @JavascriptInterface
    public void getPhotoNew(String value) {
        Log.e(TAG, value);

        try {
            JSONObject jsonObject = new JSONObject(value);
            String callback = jsonObject.getString("callback");
            String ratio = jsonObject.getString("ratio");
            String[] split = ratio.split(":");
            int width = Integer.parseInt(split[0]);
            int height = Integer.parseInt(split[1]);


            mContext.takePhoto(imageData -> {
                Log.e(TAG, imageData);
                sendCallBack(callback, "200", "success", "data:image/jpeg;base64," + imageData);
            }, width, height);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开相册
     *
     * @param callBack
     */
    @JavascriptInterface
    public void OpenGallery(String callBack) {
        mContext.openGallery(imageData -> {
            Log.e(TAG, imageData);
            sendCallBack(callBack, "200", "success", "data:image/jpeg;base64," + imageData);
        });
    }

    /**
     * 打开相册
     *
     * @param value
     */
    @JavascriptInterface
    public void OpenGalleryNew(String value) {

        Log.e(TAG, value);

        try {
            JSONObject jsonObject = new JSONObject(value);
            String callback = jsonObject.getString("callback");
            String ratio = jsonObject.getString("ratio");
            String[] split = ratio.split(":");
            int width = Integer.parseInt(split[0]);
            int height = Integer.parseInt(split[1]);


            mContext.openGallery(imageData -> {
                Log.e(TAG, imageData);
                sendCallBack(callback, "200", "success", "data:image/jpeg;base64," + imageData);
            }, width, height);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * 打开相机
     *
     * @param callBack
     */
    @JavascriptInterface
    public void OpenTheCamera(String callBack) {
        mContext.openTheCamera(imageData -> {
            Log.e(TAG, imageData);
            sendCallBack(callBack, "200", "success", "data:image/jpeg;base64," + imageData);
        });
    }

    /**
     * 打开相机
     *
     * @param value
     */
    @JavascriptInterface
    public void OpenTheCameraNew(String value) {

        Log.e(TAG, value);

        try {
            JSONObject jsonObject = new JSONObject(value);
            String callback = jsonObject.getString("callback");
            String ratio = jsonObject.getString("ratio");
            String[] split = ratio.split(":");
            int width = Integer.parseInt(split[0]);
            int height = Integer.parseInt(split[1]);

            mContext.openTheCamera(imageData -> {
                Log.e(TAG, imageData);
                sendCallBack(callback, "200", "success", "data:image/jpeg;base64," + imageData);
            }, width, height);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    /**
     * 语音合成
     *
     * @param value
     */
    @JavascriptInterface
    public void textToSpeech(String value) {
        LogUtils.log(value);
        try {
            JSONObject jsonObject = new JSONObject(value);
            SpeechCompoundUnits.getInstance().speakText(jsonObject.getString("value"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取唤起百度地图的url
     *
     * @param data
     */
    public String getWakeUpBMapUrl(String data) {

        String end = null, start = null, title = null, content = null;
        String url = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data);
            end = jsonObject.getString("end");
            start = jsonObject.getString("start");
            title = jsonObject.getString("title");
            content = jsonObject.getString("content");

            url = "baidumap://map/direction?origin=" + start + "&destination=" + end + "&coord_type=bd09ll" + "&mode=driving&src=android.tdr.zhjy";
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * 获取唤起高德地图的url
     *
     * @param data
     */
    public String getWakeUpAMapUrl(String data) {

        String end, start, title, content;
        String slat, slon, dlat, dlon;
        String url = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data);
            end = jsonObject.getString("end");
            start = jsonObject.getString("start");
            title = jsonObject.getString("title");
            content = jsonObject.getString("content");
            slat = start.split(",")[0];
            slon = start.split(",")[1];
            dlat = end.split(",")[0];
            dlon = end.split(",")[1];

            url = "amapuri://route/plan/?slat=" + slat + "&slon=" + slon + "&dlat=" + dlat + "&dlon=" + dlon + "&t=0";
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * 唤起百度地图
     *
     * @param data
     */
    @JavascriptInterface
    public void wakeUpBMap(String data) {
        LogUtils.log("百度：" + data);
        if (BaseUnits.getInstance().isApkInstalled(MyApplication.getInstance(), Config.BAIDU_PKG)) {
            Intent intent = new Intent();
            intent.setData(Uri.parse(getWakeUpBMapUrl(data)));
            mContext.startActivity(intent);
        } else {
            ViewUnits.getInstance().showToast("未安装百度地图");
        }
    }

    /**
     * 唤起高德地图
     *
     * @param data
     */
    @JavascriptInterface
    public void wakeUpAMap(String data) {
        LogUtils.log("高德：" + data);
        if (BaseUnits.getInstance().isApkInstalled(MyApplication.getInstance(), Config.AMAP_PKG)) {
            Intent intent = new Intent();
            intent.setPackage(Config.AMAP_PKG);
            intent.setData(Uri.parse(getWakeUpAMapUrl(data)));
            mContext.startActivity(intent);
        } else {
            ViewUnits.getInstance().showToast("未安装高德地图");
        }
    }

    /**
     * 跳转至登录
     *
     * @param callback
     */
    @JavascriptInterface
    public void toLogin(String callback) {
        Log.d(TAG, "toLogin: " + callback);
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra("callback", callback);
        intent.putExtra("flag", Request.StartActivityRspCode.CARD_JUMP_TO_LOGIN);
        mContext.startActivity(intent);
    }

    /**
     * 跳转至城市选择页面
     *
     * @param data
     */
    @JavascriptInterface
    public void choiceCity(String data) {
        Log.e(TAG, data);
        City city = new City();
        String callback = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            city.setID(jsonObject.getString("cityCode"));
            city.setName(jsonObject.getString("cityName"));
            callback = jsonObject.getString("callback");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(mContext, CityPickerActivity.class);
        intent.putExtra("flag", Request.StartActivityRspCode.CARD_JUMP_TO_CITYPICKER);
        intent.putExtra("callback", callback);
        if (!city.getID().equals("0")) {
            intent.putExtra("city", city);
        }
        mContext.startActivity(intent);
    }


    /**
     * 在当前页面加载新的卡片
     *
     * @param url
     */
    @JavascriptInterface
    public void loadingNewCard(String url) {
        mContext.loadingNewCard(url);
    }

    /**
     * 关闭并打开新的卡片
     *
     * @param value
     */
    @JavascriptInterface
    public void openNewCard(String value) {
        Log.d("libin", "aaa: " + value);

        try {
            JSONObject jsonObject = new JSONObject(value);

            String cardId = null;
            try {
                cardId = jsonObject.getString("Id");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {

                JSONObject data = jsonObject.getJSONObject("Data");
                if (data != null && !TextUtils.isEmpty(data.toString())) {
                    cardUrlParams = data.toString();
                } else {
                    cardUrlParams = null;
                }

                mContext.openNewCard(cardId);


            } catch (JSONException e) {
                cardUrlParams = null;
                mContext.openNewCard(cardId);
            }


        } catch (JSONException e) {
            e.printStackTrace();

            cardUrlParams = null;
            mContext.openNewCard(value);
        }


    }


    /**
     * 扫描蓝牙
     *
     * @param callBack
     */
    @JavascriptInterface
    public void scanningBluetooth(String callBack) {
        Log.d(TAG, callBack);

        if (!checkBluetooth()) {
            return;
        }

        if (ClientManager.getClient(mContext).isBluetoothOpened()) {
            scanBluetooth(callBack);
        } else {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mContext.startActivityForResult(enableBtIntent, Request.Bluetooth.bluetoothEnable);
        }


        ClientManager.getClient(mContext).registerBluetoothStateListener(new BluetoothStateListener() {
            @Override
            public void onBluetoothStateChanged(boolean openOrClosed) {
                if (openOrClosed) {
                    scanBluetooth(callBack);
                }
            }
        });


    }


    /**
     * 检查蓝牙
     */
    private boolean checkBluetooth() {
        if (!MyApplication.getInstance().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            ViewUnits.getInstance().showToast("设备没有蓝牙功能");
            return false;
        }
        return true;
    }

    /**
     * 扫描蓝牙
     *
     * @param callBack
     */
    private void scanBluetooth(String callBack) {

        Log.e(TAG, "扫描蓝牙");
        Map<String, String> map = new HashMap<>();

        ClientManager.getClient(mContext).search(ClientManager.getRequest(), new SearchResponse() {
            @Override
            public void onSearchStarted() {
            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                if (!TextUtils.isEmpty(device.getName()) && !map.containsKey(device.getAddress())) {
                    LogUtils.log(device.getAddress() + "," + device.getName());
                    map.put(device.getAddress(), device.getName());
                }
            }

            @Override
            public void onSearchStopped() {
                scanResult(callBack, map);
            }

            @Override
            public void onSearchCanceled() {
                scanResult(callBack, map);
            }
        });
    }

    /**
     * 扫描结果
     *
     * @param callBack
     * @param map
     */
    private void scanResult(String callBack, Map<String, String> map) {
        if (map.size() == 0) {
            sendCallBack(callBack, "200", "success", "");
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                stringBuffer.append(entry.getValue() + ",");
            }
            sendCallBack(callBack, "200", "success", stringBuffer.substring(0, stringBuffer.length() - 1));
        }
    }

    /**
     * 连接蓝牙
     *
     * @param value
     */
    @JavascriptInterface
    public void connectBluetooth(String value) {
        LogUtils.log(value);

        if (!checkBluetooth()) {
            return;
        }

        if (ClientManager.getClient(mContext).isBluetoothOpened()) {
            connect(value);
        } else {
            ClientManager.getClient(mContext).openBluetooth();
        }

        ClientManager.getClient(mContext).registerBluetoothStateListener(new BluetoothStateListener() {
            @Override
            public void onBluetoothStateChanged(boolean openOrClosed) {
                Log.d(TAG, "onBluetoothStateChanged: " + "蓝牙已打开");
                if (openOrClosed) {
                    connect(value);
                } else {
                    Log.d(TAG, "onBluetoothStateChanged: " + "蓝牙已关闭");

//                    sendCallBack(blueToothBack, "200", "success", "蓝牙已关闭");
                }
            }
        });

    }

    /**
     * 连接
     *
     * @param value
     */
    private void connect(String value) {
        LogUtils.log(value);
        Map<String, String> map = new HashMap<>();

        ClientManager.getClient(mContext).search(ClientManager.getRequest(), new SearchResponse() {
            @Override
            public void onSearchStarted() {
            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                if (!TextUtils.isEmpty(device.getName()) && !map.containsKey(device.getAddress())) {
                    LogUtils.log(device.getAddress() + "," + device.getName());
                    map.put(device.getAddress(), device.getName());
                }
            }

            @Override
            public void onSearchStopped() {
                LogUtils.log("蓝牙扫描结束");
                String name, writerServerId, writerCharactId, readServerId, readCharactId, notifyServerId, notifyCharacId, callBack, notifyCallback;
                try {
                    JSONObject jsonObject = new JSONObject(value);
                    name = jsonObject.getString("name");
                    writerServerId = jsonObject.getJSONObject("write").getString("serverId");
                    writerCharactId = jsonObject.getJSONObject("write").getString("charactId");
                    readServerId = jsonObject.getJSONObject("read").getString("serverId");
                    readCharactId = jsonObject.getJSONObject("read").getString("charactId");
                    notifyServerId = jsonObject.getJSONObject("notify").getString("serverId");
                    notifyCharacId = jsonObject.getJSONObject("notify").getString("charactId");
                    callBack = jsonObject.getString("callback");
                    notifyCallback = jsonObject.getString("notifyCallBack");


                    if (map.size() > 0 && map.containsValue(name)) {
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            if (entry.getValue().equals(name)) {
                                if (ClientManager.getClient(mContext).getConnectStatus(entry.getKey()) == Constants.STATUS_DEVICE_CONNECTED) {
                                    mConnectBt.setName(entry.getValue());
                                    mConnectBt.setAddress(entry.getKey());
                                    mConnectBt.setWriteSUUID(UUID.fromString(writerServerId));
                                    mConnectBt.setWriteCUUID(UUID.fromString(writerCharactId));
                                    mConnectBt.setReadSUUID(UUID.fromString(readServerId));
                                    mConnectBt.setReadCUUID(UUID.fromString(readCharactId));
                                    mConnectBt.setNotifySUUID(UUID.fromString(notifyServerId));
                                    mConnectBt.setNotifyCUUID(UUID.fromString(notifyCharacId));
                                    sendCallBack(callBack, "200", "success", "连接成功");
                                } else {
                                    ClientManager.getClient(mContext).connect(entry.getKey(), ClientManager.getOption(), (code, data) -> {
                                        if (code == REQUEST_SUCCESS) {
                                            LogUtils.log("连接成功");
                                            sendCallBack(callBack, "200", "success", "连接成功");


                                            ClientManager.getClient(mContext).registerConnectStatusListener(entry.getKey(), new BleConnectStatusListener() {
                                                @Override
                                                public void onConnectStatusChanged(String mac, int status) {
                                                    if (status == Constants.STATUS_DISCONNECTED) {
                                                        sendCallBack(callBack, "500", "fail", "蓝牙已断开");
//                                                        sendCallBack(blueToothBack, "500", "fail", "蓝牙已断开");
                                                    }
                                                }
                                            });


                                            mConnectBt.setName(entry.getValue());
                                            mConnectBt.setAddress(entry.getKey());

                                            for (BleGattService service : data.getServices()) {

                                                if (FormatUtils.getInstance().uplowContains(service.getUUID().toString(), writerServerId)) {
                                                    for (BleGattCharacter character : service.getCharacters()) {
                                                        if (FormatUtils.getInstance().uplowContains(character.getUuid().toString(), writerCharactId)) {
                                                            mConnectBt.setWriteSUUID(service.getUUID());
                                                            mConnectBt.setWriteCUUID(character.getUuid());
                                                        }
                                                    }
                                                }

                                                if (FormatUtils.getInstance().uplowContains(service.getUUID().toString(), readServerId)) {
                                                    for (BleGattCharacter character : service.getCharacters()) {
                                                        if (FormatUtils.getInstance().uplowContains(character.getUuid().toString(), readCharactId)) {
                                                            mConnectBt.setReadSUUID(service.getUUID());
                                                            mConnectBt.setReadCUUID(character.getUuid());
                                                        }
                                                    }
                                                }

                                                if (FormatUtils.getInstance().uplowContains(service.getUUID().toString(), notifyServerId)) {
                                                    for (BleGattCharacter character : service.getCharacters()) {
                                                        if (FormatUtils.getInstance().uplowContains(character.getUuid().toString(), notifyCharacId)) {
                                                            mConnectBt.setNotifySUUID(service.getUUID());
                                                            mConnectBt.setNotifyCUUID(character.getUuid());
                                                        }
                                                    }
                                                }


                                                notifyBluetooth(notifyCallback);

                                                LogUtils.log(mConnectBt.toString());
                                            }
                                        } else {
                                            if (TextUtils.isEmpty(mConnectBt.getAddress())) {

                                                sendCallBack(callBack, "500", "fail", "连接失败");
                                            }
                                        }
                                    });
                                }

                            }
                        }

                    } else {
                        LogUtils.log("未查找到相应蓝牙");
                        sendCallBack(callBack, "500", "fail", "未查找到相应蓝牙");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSearchCanceled() {


            }
        });
    }


    /**
     * 蓝牙写入数据
     *
     * @param data
     */
    @JavascriptInterface
    public void bluetooth_write(String data) {
        Log.e(TAG, "读取的数据：" + data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            String value = jsonObject.getString("key");
            String callBack = jsonObject.getString("callback");

            if (mConnectBt.getAddress() == null ||
                    ClientManager.getClient(mContext).getConnectStatus(mConnectBt.getAddress()) != Constants.STATUS_DEVICE_CONNECTED) {
                sendCallBack(callBack, "500", "fail", "请先连接蓝牙设备");
                return;
            }
            ClientManager.getClient(mContext).write(mConnectBt.getAddress(),
                    mConnectBt.getWriteSUUID(), mConnectBt.getWriteCUUID(), FormatUtils.getInstance().hexStringToBytes(value), code -> {
                        if (code == REQUEST_SUCCESS) {
                            sendCallBack(callBack, "200", "success", value);
                            Log.e(TAG, "写入成功");
                        } else {
                            Log.e(TAG, "写入失败");
                            sendCallBack(callBack, "500", "fail", "写入失败");
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
            ViewUnits.getInstance().showToast(e.toString());
        }

    }

    /**
     * 蓝牙读取数据
     *
     * @param value
     */
    @JavascriptInterface
    public void bluetooth_read(String value) {
        Log.e(TAG, mConnectBt.getAddress());
        Log.e(TAG, ClientManager.getClient(mContext).getConnectStatus(mConnectBt.getAddress()) + "");

        if (mConnectBt.getAddress() == null ||
                ClientManager.getClient(mContext).getConnectStatus(mConnectBt.getAddress()) != Constants.STATUS_DEVICE_CONNECTED) {
            sendCallBack(value, "500", "fail", "请先连接蓝牙设备");
            return;
        }

        ClientManager.getClient(mContext).read(mConnectBt.getAddress(), mConnectBt.getReadSUUID(), mConnectBt.getReadCUUID(),
                (code, data) -> {
                    if (code == REQUEST_SUCCESS) {
                        sendCallBack(value, "200", "success", FormatUtils.getInstance().byteToString(data));
                    } else {
                        sendCallBack(value, "500", "fail", "读取失败");
                    }
                });
    }


    /**
     * 蓝牙数据通知
     *
     * @param callback
     */
    @JavascriptInterface
    public void bluetoothData_notify(String callback) {
        Log.e(TAG, callback);
//
//        if (mConnectBt.getAddress() == null ||
//                ClientManager.getClient(mContext).getConnectStatus(mConnectBt.getAddress()) != Constants.STATUS_DEVICE_CONNECTED) {
//            sendCallBack(callback, "500", "fail", "请先连接蓝牙设备");
//            isOpenNotify = false;
//            return;
//        }
//
//
//        ClientManager.getClient(MyApplication.getInstance()).notify(mConnectBt.getAddress(),
//                mConnectBt.getNotifySUUID(), mConnectBt.getNotifyCUUID(), new BleNotifyResponse() {
//                    @Override
//                    public void onNotify(UUID service, UUID character, byte[] value) {
//                        sendCallBack(callback, "200", "success", FormatUtils.getInstance().byteToString(value));
//                    }
//
//                    @Override
//                    public void onResponse(int code) {
//
//                    }
//                });
    }


    /**
     * 取消通知
     */
    private void notifyBluetooth(String callback) {
        if (mConnectBt.getAddress() != null) {
            ClientManager.getClient(MyApplication.getInstance()).notify(mConnectBt.getAddress(),
                    mConnectBt.getNotifySUUID(), mConnectBt.getNotifyCUUID(), new BleNotifyResponse() {
                        @Override
                        public void onNotify(UUID service, UUID character, byte[] value) {
                            String data = FormatUtils.getInstance().byteToString(value);
                            if (data.length() > 10) {
                                sendCallBack(callback, "200", "success", FormatUtils.getInstance().byteToString(value));
                            }

                            LogUtils.log("notify:" + data);
                        }

                        @Override
                        public void onResponse(int code) {

                        }
                    });
        }

    }

    /**
     * 取消通知
     */
    private void unNotifyBluetooth() {
        if (mConnectBt.getAddress() != null) {
            ClientManager.getClient(MyApplication.getInstance()).unnotify(mConnectBt.getAddress(),
                    mConnectBt.getNotifySUUID(), mConnectBt.getNotifyCUUID(), new BleUnnotifyResponse() {
                        @Override
                        public void onResponse(int code) {

                        }
                    });
        }

    }

    /**
     * 断开连接
     *
     * @param value
     */
    @JavascriptInterface
    public void bluetooth_disconnect(String value) {
        Log.d("libin", "bluetooth_disconnect: " + value);
        if (mConnectBt.getAddress() != null && ClientManager.getClient(mContext)
                .getConnectStatus(mConnectBt.getAddress()) == Constants.STATUS_DEVICE_CONNECTED) {
            ClientManager.getClient(mContext).disconnect(mConnectBt.getAddress());
        }

        sendCallBack(value, "200", "success", "断开成功");
    }


    /**
     * 断开连接
     */
    public void disconnectBluetooth() {
        if (mConnectBt.getAddress() != null && ClientManager.getClient(mContext)
                .getConnectStatus(mConnectBt.getAddress()) == Constants.STATUS_DEVICE_CONNECTED) {
            ClientManager.getClient(mContext).disconnect(mConnectBt.getAddress());
        }
    }


    /**
     * 连接蓝牙(新)
     *
     * @param value
     */
    @JavascriptInterface
    public void Bluetooth_connect(String value) {
        LogUtils.log("Bluetooth_connect:" + value);

        try {
            JSONObject jsonObject = new JSONObject(value);
            String name = jsonObject.getString("name");
            String address = jsonObject.getString("mac");
            String callBack = jsonObject.getString("callback");
            String notifyCallback = jsonObject.getString("notifyCallBack");


            if (!checkBluetooth()) {
                sendCallBack(callBack, "500", "fail", "蓝牙不支持");
                return;
            }


            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();

            if (!defaultAdapter.isEnabled()) {
                defaultAdapter.enable();

                new CountDownTimer(10000, 400) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (defaultAdapter.isEnabled()) {
                            mContext.connectBlueTooth(callBack, notifyCallback, name, address);
                            cancel();
                        }
                    }

                    @Override
                    public void onFinish() {
                        sendCallBack(callBack, "500", "fail", "蓝牙未开启");
                    }
                }.start();
            } else {
                mContext.connectBlueTooth(callBack, notifyCallback, name, address);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 蓝牙写入数据（新）
     *
     * @param data
     */
    @JavascriptInterface
    public void Bluetooth_write(String data) {
        Log.e(TAG, "Bluetooth_write：" + data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            String value = jsonObject.getString("key");
            String callBack = jsonObject.getString("callback");

            mContext.sendData(callBack, value);
        } catch (JSONException e) {
            e.printStackTrace();
            ViewUnits.getInstance().showToast(e.toString());
        }

    }

    /**
     * 蓝牙读取数据（新）
     *
     * @param value
     */
    @JavascriptInterface
    public void Bluetooth_read(String value) {
        Log.e(TAG, value);
        sendCallBack(value, "500", "fail", "提供的蓝牙sdk没这功能");
    }

    /**
     * 蓝牙断开（新）
     *
     * @param value
     */
    @JavascriptInterface
    public void Bluetooth_break(String value) {
        Log.e(TAG, value);
        mContext.disConnectBluetooth(value);

    }


    /**
     * 蓝牙通知（新）
     *
     * @param value
     */
    @JavascriptInterface
    public void Bluetooth_notify(String value) {
        Log.e(TAG, value);
    }


    /**
     * 指纹解锁
     *
     * @param value
     */
    @JavascriptInterface
    public void fingerPrint_unlocking(String value) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(mContext);

            if (fingerprintManagerCompat.isHardwareDetected()) {
                if (fingerprintManagerCompat.hasEnrolledFingerprints()) {
                    initCipher(value);
                } else {
                    sendCallBack(value, "507", "fail", "您还没有进行指纹输入，请指纹设定后打开");
                }

            } else {
                sendCallBack(value, "500", "fail", "您的设备不支持指纹输入，请切换为数字键盘");
            }


        } else {
            sendCallBack(value, "500", "fail", "您的设备不支持指纹输入，请切换为数字键盘");
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void initCipher(String callback) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder("default_key",
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
            keyGenerator.init(builder.build());
            keyGenerator.generateKey();
            SecretKey key = (SecretKey) keyStore.getKey("default_key", null);
            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            cipher.init(Cipher.ENCRYPT_MODE, key);


            FingerPrintFragment fragment = new FingerPrintFragment();
            fragment.setCipher(cipher);
            fragment.setCallback(callback);
            fragment.show(mContext.getFragmentManager(), "fingerprint");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取运动信息
     *
     * @param value
     */
    @JavascriptInterface
    public void getSportInfo(String value) {
        Log.d("getSportInfo", value);

        long startTime;
        long endTime;
        String callBack = null;
        try {
            JSONObject jsonObject = new JSONObject(value);
            startTime = jsonObject.getLong("startTime");
            endTime = jsonObject.getLong("endTime");
            callBack = jsonObject.getString("callback");

        } catch (JSONException e) {
            e.printStackTrace();
            ViewUnits.getInstance().showToast(e.toString());
        }

    }

    /**
     * 获取通讯录
     *
     * @param value
     */
    @JavascriptInterface
    public void getAddressBook(String value) {
        if (BaseUnits.getInstance().checkPermission(mContext, Manifest.permission.READ_CONTACTS)) {
            Cursor cursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                    null, null, null);
            AddressBookBean bookBean = new AddressBookBean();

            List<AddressBookBean.DataBean> dataList = new ArrayList<>();
            if (cursor != null) {
                bookBean.setStatus("200");
                bookBean.setMsg("success");
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    dataList.add(new AddressBookBean.DataBean(name, number.replaceAll("-", "").replaceAll(" ", "")));
                }
                cursor.close();
            } else {
                bookBean.setStatus("500");
                bookBean.setMsg("fail");
            }
            bookBean.setData(dataList);
            mCallBack.callBackResult(value, GsonUtils.jsonString(bookBean));
        } else {
            ActivityCompat.requestPermissions(mContext,
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACTS_PERMISSIONS);
        }

    }

    /**
     * 保存图片到系统相册
     *
     * @param value
     */
    @JavascriptInterface
    public void saveToAlbum(String value) {
        String imgUrl;
        String callBack = null;
        try {
            JSONObject jsonObject = new JSONObject(value);
            imgUrl = jsonObject.getString("imgUrl");
            callBack = jsonObject.getString("callback");

            byte[] decode = Base64Utils.decode(imgUrl);
            String fileName = ImageUtils.saveImgToGallery(decode);
            if (!TextUtils.isEmpty(fileName)) {
                ViewUnits.getInstance().showToast("保存成功");
                sendCallBack(callBack, "200", "success", fileName);
            } else {
                ViewUnits.getInstance().showToast("保存失败");
                sendCallBack(callBack, "500", "fail", "保存失败");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            ViewUnits.getInstance().showToast(e.toString());
        } catch (Exception e) {
            e.printStackTrace();
            ViewUnits.getInstance().showToast(e.toString());
            sendCallBack(callBack, "500", "fail", e.toString());
        }
    }

    /**
     * 音频录制
     *
     * @param value
     */
    @JavascriptInterface
    public void audioRecord(String value) {
        Log.d(TAG, "audioRecord: " + value);

        int time;
        String callBack;
        try {
            JSONObject jsonObject = new JSONObject(value);
            time = jsonObject.getInt("time");
            callBack = jsonObject.getString("callback");

            if (BaseUnits.getInstance().checkPermission(mContext, Manifest.permission.RECORD_AUDIO)) {
                RecordAudioFragment recordAudioFragment = RecordAudioFragment.newInstance(callBack, time > 60 ? 60 : time);
                recordAudioFragment.show(mContext.getFragmentManager(), "record_audio");
            } else {
                ActivityCompat.requestPermissions(mContext,
                        new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_PERMISSIONS);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            ViewUnits.getInstance().showToast(e.toString());
        }
    }

//    /**
//     * 音频播放
//     *
//     * @param value
//     */
//    @JavascriptInterface
//    public void audioPlay(String value) {
//
//
//        Log.d("libin", "audioPlay: " + value);
//        String audio;
//        try {
//            JSONObject jsonObject = new JSONObject(value);
//            audio = jsonObject.getString("value");
//            LogUtils.log("audio_length:"+audio.length());
//            byte[] decode = Base64Utils.decodeAudio(audio);
//            LogUtils.log("byte_length:"+decode.length);
//            String audioPath = FileUtils.getInstance().saveAudioToFile(decode);
//            mContext.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    PlayAudioFragment1 playAudioFragment = PlayAudioFragment1.newInstance(audioPath);
//                    playAudioFragment.show(mContext.getFragmentManager(), "audio");
//                }
//            });
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            ViewUnits.getInstance().showToast(e.toString());
//
//        }
//    }


    /**
     * 音频播放
     *
     * @param value
     */
    @JavascriptInterface
    public void audioPlay(String value) {


        Log.d("libin", "audioPlay: " + value);
        String audio;
        try {
            JSONObject jsonObject = new JSONObject(value);
            audio = jsonObject.getString("value");
            LogUtils.log("audio_length:"+audio.length());
            byte[] decode = Base64Utils.decode(audio);
            LogUtils.log("byte_length:"+decode.length);
            String audioPath = FileUtils.getInstance().saveAudioToFile(decode);
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PlayAudioFragment playAudioFragment = PlayAudioFragment.newInstance(audioPath);
                    playAudioFragment.show(mContext.getFragmentManager(), "audio");
                }
            });



        } catch (JSONException e) {
            e.printStackTrace();
            ViewUnits.getInstance().showToast(e.toString());

        }
    }

    /**
     * 开始陀螺仪
     *
     * @param value
     */
    @JavascriptInterface
    public void startGyro(String value) {
        try {
            JSONObject jsonObject = new JSONObject(value);
            gyTime = jsonObject.getDouble("time");
            gyCallback = jsonObject.getString("callback");

            mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
            mSensorManager.registerListener(sensorEventListener, mSensorManager.getDefaultSensor(
                    Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);

        } catch (JSONException e) {
            e.printStackTrace();
            ViewUnits.getInstance().showToast(e.toString());
        }
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        long index = 0;
        long index1 = 0;

        @Override
        public void onSensorChanged(SensorEvent event) {

            float[] values = event.values;
            //绕Z轴旋转
            float x = values[0];
            //绕X轴旋转
            float y = values[1];
            //绕Y轴旋转
            float z = values[2];
            // 获取手机触发event的传感器的类型
            switch (event.sensor.getType()) {
                case Sensor.TYPE_GYROSCOPE:
                    long currentTimeMillis = System.currentTimeMillis();
                    Log.d("cccc", index + "," + currentTimeMillis);
                    if (currentTimeMillis - index >= gyTime * 1000) {
                        JSONObject jsonObject = new JSONObject();
                        JSONObject data = new JSONObject();
                        try {
                            data.put("x", x);
                            data.put("y", y);
                            data.put("z", z);
                            jsonObject.put("status", "200");
                            jsonObject.put("msg", "success");
                            jsonObject.put("data", data);

                            mCallBack.callBackResult(gyCallback, jsonObject.toString());
                        } catch (JSONException e) {
                            mCallBack.callBackResult(gyCallback, "未知错误，联系管理员");
                            e.printStackTrace();
                        }
                        index = currentTimeMillis;
                    }


                    break;
                case Sensor.TYPE_ACCELEROMETER:

                    long currentTime = System.currentTimeMillis();
                    if (currentTime - index1 >= acTime * 1000) {
                        JSONObject jsonObject1 = new JSONObject();
                        JSONObject data1 = new JSONObject();
                        try {
                            data1.put("x", x);
                            data1.put("y", y);
                            data1.put("z", z);
                            jsonObject1.put("status", "200");
                            jsonObject1.put("msg", "success");
                            jsonObject1.put("data", data1);
                            mCallBack.callBackResult(acCallback, jsonObject1.toString());
                        } catch (JSONException e) {
                            mCallBack.callBackResult(acCallback, "未知错误，联系管理员");
                            e.printStackTrace();
                        }

                        index1 = currentTime;
                    }


                    break;
            }


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    /**
     * 结束陀螺仪
     *
     * @param value
     */
    @JavascriptInterface
    public void stopGyro(String value) {

        Log.d(TAG, "stopGyro: " + value);

        if (mSensorManager != null) {
            mSensorManager.unregisterListener(sensorEventListener, mSensorManager.getDefaultSensor(
                    Sensor.TYPE_GYROSCOPE));
        }

        if (value != null) {
            sendCallBack(value, "200", "success", "已结束陀螺仪");
        }
    }


    /**
     * 结束陀螺仪
     */
    public void stopGyroscope() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(sensorEventListener, mSensorManager.getDefaultSensor(
                    Sensor.TYPE_GYROSCOPE));
        }
    }


    /**
     * 开始加速计
     *
     * @param value
     */
    @JavascriptInterface
    public void startAccelerometer(String value) {
        Log.d(TAG, "startAccelerometer: " + value);

        try {
            JSONObject jsonObject = new JSONObject(value);
            acTime = jsonObject.getDouble("time");
            acCallback = jsonObject.getString("callback");

            mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
            mSensorManager.registerListener(sensorEventListener, mSensorManager.getDefaultSensor(
                    Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        } catch (JSONException e) {
            e.printStackTrace();
            ViewUnits.getInstance().showToast(e.toString());
        }
    }

    /**
     * 结束加速计
     *
     * @param value
     */
    @JavascriptInterface
    public void stopAccelerometer(String value) {
        Log.d(TAG, "stopAccelerometer: " + value);
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(sensorEventListener, mSensorManager.getDefaultSensor(
                    Sensor.TYPE_ACCELEROMETER));
        }
        if (value != null) {
            sendCallBack(value, "200", "success", "已结束加速计");
        }

    }


    /**
     * 结束加速计
     */

    public void stopAccelerate() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(sensorEventListener, mSensorManager.getDefaultSensor(
                    Sensor.TYPE_ACCELEROMETER));
        }
    }

    /**
     * 网络状况
     *
     * @param value
     */
    @JavascriptInterface
    public void networkStatus(String value) {
        Log.d(TAG, "networkStatus: " + value);

        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            sendCallBack(value, "0", "fail", "不可达的网络");
        } else {
            int nType = networkInfo.getType();
            if (nType == ConnectivityManager.TYPE_WIFI) {
                //WIFI
                sendCallBack(value, "2", "success", "wifi网络");
            } else if (nType == ConnectivityManager.TYPE_MOBILE) {
                int nSubType = networkInfo.getSubtype();
                TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
                //3G   联通的3G为UMTS或HSDPA 电信的3G为EVDO
                if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                        && !telephonyManager.isNetworkRoaming()) {
                    sendCallBack(value, "3", "success", "4G");
                } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                        || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                        || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                        && !telephonyManager.isNetworkRoaming()) {
                    sendCallBack(value, "5", "success", "3G");
                    //2G 移动和联通的2G为GPRS或EGDE，电信的2G为CDMA
                } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                        || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                        || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                        && !telephonyManager.isNetworkRoaming()) {
                    sendCallBack(value, "4", "success", "2G");
                } else {
                    sendCallBack(value, "4", "success", "2G");
                }

            } else {
                sendCallBack(value, "-1", "fail", "未识别的网络");
            }
        }


    }

    /**
     * 模块认证
     *
     * @param value
     */
    @JavascriptInterface
    public void authentication(String value) {
        Log.d("libin", "authentication: " + value);
        String callback;
        String typeId;
        try {
            JSONObject jsonObject = new JSONObject(value);
            typeId = jsonObject.getString("typeId");
            callback = jsonObject.getString("callback");

            if ("001".equals(typeId)) {
                Intent intent = new Intent(mContext, ValidateActivity.class);
                intent.putExtra("type", 1);
                ValidateActivity.setValidateListener(new ValidateListener() {
                    @Override
                    public void validate(String status, String msg, String data) {
                        sendCallBack(callback, status, msg, data);
                        ValidateActivity.mValidateListener = null;
                    }
                });
                mContext.startActivity(intent);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            ViewUnits.getInstance().showToast(e.toString());
        }


    }


    /**
     * 获取智慧家园推广码
     *
     * @param value
     */
    @JavascriptInterface
    public void getZHJYCode(String value) {
        Log.d("libin", value);
        String invitationCard = (String) SPUtils.getInstance().get(SPUtils.FILE_CARD, SPUtils.cardNo, "");
        String invitationCode = (String) SPUtils.getInstance().get(SPUtils.FILE_CARD, SPUtils.invitationCode, "");
        String cardId = mContext.getCardId();
        Log.d("libin", invitationCard + "," + invitationCode + "," + cardId);

        if (!TextUtils.isEmpty(invitationCode) && invitationCard.equals(cardId)) {
            sendCallBack(value, "200", "success", invitationCode);
            SPUtils.getInstance().remove(SPUtils.FILE_CARD, SPUtils.cardNo);
            SPUtils.getInstance().remove(SPUtils.FILE_CARD, SPUtils.invitationCode);
        } else {
            sendCallBack(value, "500", "fail", "无个人推广码");
        }
    }


    /**
     * 打开新的卡片
     *
     * @param value
     */
    @JavascriptInterface
    public void openOtherCard(String value) {
        Log.d("libin", "authentication: " + value);

        try {
            JSONObject jsonObject = new JSONObject(value);

            String cardId = null;
            try {
                cardId = jsonObject.getString("Id");
            } catch (JSONException e) {

            }

            try {

                JSONObject data = jsonObject.getJSONObject("Data");
                if (data != null && !TextUtils.isEmpty(data.toString())) {
                    cardUrlParams = data.toString();
                } else {
                    cardUrlParams = null;
                }

                openOrRefresh(cardId);

            } catch (JSONException e) {
                e.printStackTrace();

                cardUrlParams = null;
                openOrRefresh(cardId);
            }
        } catch (JSONException e) {
            e.printStackTrace();

            cardUrlParams = null;

            openOrRefresh(value);
        }


    }

    /**
     * @param value 相同卡片刷新页面，其他卡片打开新的卡片
     */
    private void openOrRefresh(String value) {

        if (mContext.getCardId().equals(value)) {
            mContext.refreshCard();
        } else {
            mContext.openOtherCard(value);
        }

    }

    /**
     * 是否有定位权限
     *
     * @param value
     */
    @JavascriptInterface
    public void locationServicesEnabled(String value) {
        if (ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", "001");
                jsonObject.put("msg", "没有定位权限");

                sendCallBackJson(value, "500", "error", jsonObject);

                return;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return;
        }


        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", "002");
                jsonObject.put("msg", "GPS未开启");
                sendCallBackJson(value, "500", "error", jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return;
        }

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", "000");
            jsonObject.put("msg", "success");
            sendCallBackJson(value, "200", "success", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 震动
     *
     * @param value
     */
    @JavascriptInterface
    public void vibrate(String value) {
        LogUtils.log(value);
        Vibrator vibrator = (Vibrator) mContext.getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(700);
    }

    /**
     * 剩余内存
     *
     * @param value
     */
    @JavascriptInterface
    public void freeMemory(String value) {

        LogUtils.log(value);
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long blockSize = sf.getBlockSizeLong();
            long availCount = sf.getAvailableBlocksLong();

            sendCallBack(value, "200", "success", FormatUtils.getInstance().decimalFormat((double) availCount * blockSize / (1024 * 1024 * 1024), 1));
        }

    }

    /**
     * 录制视频
     *
     * @param value
     */
    @JavascriptInterface
    public void recordVideo(String value) {

        LogUtils.log(value);

        int time;
        String callback;
        try {
            JSONObject jsonObject = new JSONObject(value);
            time = jsonObject.getInt("time");
            callback = jsonObject.getString("callback");

            if (BaseUnits.getInstance().checkPermission(mContext, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA})) {

                Intent intent = new Intent(mContext, VideoRecorderActivity.class);
                intent.putExtra("callback", callback);
                intent.putExtra("time", time > 60 ? 60 : time);
                mContext.startActivity(intent);
            } else {
                ActivityCompat.requestPermissions(mContext,
                        new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, REQUEST_RECORD_PERMISSIONS);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            ViewUnits.getInstance().showToast(e.toString());
        }


    }

    /**
     * 播放视频
     *
     * @param value
     */
    @JavascriptInterface
    public void playVideo(String value) {
        LogUtils.log(value);

        String data;
        try {
            JSONObject jsonObject = new JSONObject(value);
            data = jsonObject.getString("value");
            FileUtils.getInstance().clearDirectory(tendency.hz.zhihuijiayuan.bean.base.Uri.videoPath);
            String path = FileUtils.getInstance().saveVideoFile(Base64Utils.decode(data));

            OpenFileUtils.getInstance().openFile(mContext, path, "video/*");


        } catch (JSONException e) {
            e.printStackTrace();
            ViewUnits.getInstance().showToast(e.toString());
        }

    }

    /**
     * 下载
     *
     * @param value
     */
    @JavascriptInterface
    public void download(String value) {
        LogUtils.log(value);
        try {
            JSONObject jsonObject = new JSONObject(value);
            String callback = jsonObject.getString("callback");
            String url = jsonObject.getString("url");
            DownLoadFragment downLoadFragment = DownLoadFragment.newInstance(callback,
                    url, mContext.getCardId());
            downLoadFragment.show(mContext.getFragmentManager(), "download");


        } catch (JSONException e) {
            e.printStackTrace();
            ViewUnits.getInstance().showToast(e.toString());
        }

    }


    /**
     * 身份证正面
     *
     * @param value
     */
    @JavascriptInterface
    public void IDCard_front(String value) {
        LogUtils.log(value);
        if (BaseUnits.getInstance().checkPermission(mContext, Manifest.permission.CAMERA)) {
            Intent intent = new Intent(mContext, CaptureActivity.class);

            intent.putExtra("is_front", true);
            intent.putExtra("callback", value);
            mContext.startActivityForResult(intent, Request.StartActivityRspCode.SCAN_ID_CARD);
        } else {
            ActivityCompat.requestPermissions(mContext,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }


    }


    /**
     * 身份证反面
     *
     * @param value
     */
    @JavascriptInterface
    public void IDCard_reverseSide(String value) {
        LogUtils.log(value);
        if (BaseUnits.getInstance().checkPermission(mContext, Manifest.permission.CAMERA)) {
            Intent intent = new Intent(mContext, CaptureActivity.class);
            intent.putExtra("is_front", false);
            intent.putExtra("callback", value);
            mContext.startActivityForResult(intent, Request.StartActivityRspCode.SCAN_ID_CARD);
        } else {
            ActivityCompat.requestPermissions(mContext,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }


    }


    /**
     * 获取系统信息
     *
     * @param callback
     */
    @JavascriptInterface
    public void getPhoneSystemInfo(String callback) {
        LogUtils.log(callback);
        String phoneModel = Build.MODEL;
        String systemName = Build.VERSION.RELEASE;
        String phoneWidth = String.valueOf(ScreenUtils.getScreenWidth());
        String phoneHeight = String.valueOf(ScreenUtils.getScreenHeight());

        LogUtils.log(phoneModel + "," + systemName + "," + phoneWidth + "," + phoneHeight);

        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            data.put("phoneModel", phoneModel);
            data.put("systemName", systemName);
            data.put("phoneWidth", phoneWidth);
            data.put("phoneHeight", phoneHeight);
            data.put("phoneStatusBarHeight", String.valueOf(BaseUnits.getInstance().getStatusBarHeight()));
            jsonObject.put("status", "200");
            jsonObject.put("msg", "success");
            jsonObject.put("data", data);
            mCallBack.callBackResult(callback, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取电量
     *
     * @param callback
     */
    @JavascriptInterface
    public void getPhonePower(String callback) {
        LogUtils.log(callback);
        int level;
        Intent batteryInfoIntent = MyApplication.getInstance().registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        level = batteryInfoIntent.getIntExtra("level", 0);
        int batterySum = batteryInfoIntent.getIntExtra("scale", 100);
        int percentBattery = 100 * level / batterySum;

        sendCallBack(callback, "200", "success", String.valueOf(percentBattery));

    }

    /**
     * 获取电量
     *
     * @param callback
     */
    @JavascriptInterface
    public void getAppName(String callback) {
        LogUtils.log(callback);

        sendCallBack(callback, "200", "success", MyApplication.getInstance().getString(R.string.app_name));

    }


    /**
     * 复制内容到剪切板
     *
     * @param value
     */
    @JavascriptInterface
    public void setClipboard(String value) {
        LogUtils.log(value);
        int timeout;
        String callback;
        String data;
        try {
            JSONObject jsonObject = new JSONObject(value);
            callback = jsonObject.getString("callback");
            data = jsonObject.getString("value");
            //获取剪贴板管理器
            ClipboardManager cm = (ClipboardManager) MyApplication.getInstance().getSystemService(CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText(null, data);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            sendCallBack(callback, "200", "success", "成功");

            timeout = jsonObject.getInt("timeout");

            new CountDownTimer(timeout, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    LogUtils.log("清除剪切板");
                    clearClipboard();
                }
            }.start();

        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.log("无定时清剪切板");
        }
    }


    /**
     * 清除剪切板
     */
    private void clearClipboard() {
        try {
            //获取剪贴板管理器
            ClipboardManager cm = (ClipboardManager) MyApplication.getInstance().getSystemService(CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText(null, ""));  //清除剪切板数据
            cm.setText(null);
        } catch (Exception e) {
        }
    }

    /**
     * 获取剪切板
     *
     * @param callback
     */
    @JavascriptInterface
    public void getClipboard(String callback) {
        LogUtils.log(callback);
        ClipboardManager cm = (ClipboardManager) MyApplication.getInstance().getSystemService(CLIPBOARD_SERVICE);
        ClipData data = cm.getPrimaryClip();
        try {
            ClipData.Item item = data.getItemAt(0);
            String content = item.getText().toString();
            sendCallBack(callback, "200", "success", content);

        } catch (Exception e) {
            sendCallBack(callback, "500", "fail", "剪切板为空");
        }
    }

    /**
     * 清除剪切板
     *
     * @param callback
     */
    @JavascriptInterface
    public void clearClipboard(String callback) {
        LogUtils.log(callback);
        try {
            //获取剪贴板管理器
            ClipboardManager cm = (ClipboardManager) MyApplication.getInstance().getSystemService(CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText(null, ""));  //清除剪切板数据
            cm.setText(null);
            sendCallBack(callback, "200", "success", "清除成功");
        } catch (Exception e) {
            sendCallBack(callback, "200", "success", "清除成功");
        }
    }


    /**
     * toast
     *
     * @param value
     */
    @JavascriptInterface
    public void toast(String value) {
        LogUtils.log(value);
        ToastBean toastBean = GsonUtils.jsonToBean(value, ToastBean.class);
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoadingUtils.getInstance().showToast(mContext, toastBean);
            }
        });

    }


    /**
     * loading
     *
     * @param value
     */
    @JavascriptInterface
    public void loading(String value) {
        LogUtils.log(value);
        ToastBean toastBean = GsonUtils.jsonToBean(value, ToastBean.class);
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoadingUtils.getInstance().showLoading(mContext, toastBean);
            }
        });
    }

    /**
     * 隐藏loading
     */
    @JavascriptInterface
    public void hideLoading(String value) {
        LogUtils.log("隐藏");
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoadingUtils.getInstance().dismiss();
            }
        });

    }

    /**
     * dialog
     *
     * @param value
     */
    @JavascriptInterface
    public void dialog(String value) {
        LogUtils.log(value);
        DialogBean dialogBean = GsonUtils.jsonToBean(value, DialogBean.class);

        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if ("normal".equals(dialogBean.getType())) {
                    if (dialogBean.getButton() != null && dialogBean.getButton().size() <= 2) {
                        View contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_popup_dialog, null);
                        PopupWindow popupWindow = new PopupWindow(contentView,  ScreenUtils.getScreenWidth() - ViewUnits.getInstance().dp2px(mContext,120),
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        popupWindow.setContentView(contentView);
                        popupWindow.setTouchable(true);
                        popupWindow.setFocusable(true);
                        popupWindow.setOutsideTouchable(false);
                        popupWindow.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), (Bitmap) null));
                        if (!popupWindow.isShowing()) {
                            popupWindow.showAtLocation(mContext.getWindow().peekDecorView(), Gravity.CENTER, 0, 0);
                        } else {
                            ///如果正在显示等待框，，，则结束掉等待框然后重新显示
                            popupWindow.dismiss();
                            popupWindow.showAtLocation(mContext.getWindow().peekDecorView(), Gravity.CENTER, 0, 0);
                        }

                        WindowManager.LayoutParams layoutParams = mContext.getWindow().getAttributes();
                        layoutParams.alpha = 0.7f;
                        mContext.getWindow().setAttributes(layoutParams);


                        TextView tvTitle = contentView.findViewById(R.id.tv_dialog_title);
                        TextView tvContent = contentView.findViewById(R.id.tv_dialog_content);
                        TextView tvFirstItem = contentView.findViewById(R.id.tv_first_item);
                        TextView tvSecondItem = contentView.findViewById(R.id.tv_second_item);
                        View viewDivider = contentView.findViewById(R.id.view_divider);

                        if (!TextUtils.isEmpty(dialogBean.getTitle())){
                            tvTitle.setVisibility(View.VISIBLE);
                            tvTitle.setText(dialogBean.getTitle());
                        }else {
                            tvTitle.setVisibility(View.GONE);
                        }

                        if (!TextUtils.isEmpty(dialogBean.getContent())){
                            tvContent.setVisibility(View.VISIBLE);
                            tvContent.setText(dialogBean.getContent());
                        }else {
                            tvContent.setVisibility(View.GONE);
                        }

                        tvFirstItem.setText(dialogBean.getButton().get(0).getText());
                        tvFirstItem.setTextColor(Color.parseColor(FormatUtils.getInstance().colorTo6Color(dialogBean.getButton().get(0).getTextColor())));
                        if (dialogBean.getButton().size() ==2){
                            viewDivider.setVisibility(View.VISIBLE);
                            tvSecondItem.setVisibility(View.VISIBLE);
                            tvSecondItem.setText(dialogBean.getButton().get(1).getText());
                            tvSecondItem.setTextColor(Color.parseColor(FormatUtils.getInstance().colorTo6Color(dialogBean.getButton().get(1).getTextColor())));
                        }

                        tvFirstItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupWindow.dismiss();
                                sendCallBack(dialogBean.getCallback(),"200","success",dialogBean.getButton().get(0).getText());
                            }
                        });

                        tvSecondItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupWindow.dismiss();
                                sendCallBack(dialogBean.getCallback(),"200","success",dialogBean.getButton().get(1).getText());
                            }
                        });


                        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                layoutParams.alpha = 1.0f;
                                mContext.getWindow().setAttributes(layoutParams);
                            }
                        });
                    }else{

                        View contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_popup_dialog_more, null);
                        PopupWindow popupWindow = new PopupWindow(contentView, ScreenUtils.getScreenWidth() - ViewUnits.getInstance().dp2px(mContext,120),
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        popupWindow.setContentView(contentView);
                        popupWindow.setTouchable(true);
                        popupWindow.setFocusable(true);
                        popupWindow.setOutsideTouchable(false);
                        popupWindow.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), (Bitmap) null));
                        if (!popupWindow.isShowing()) {
                            popupWindow.showAtLocation(mContext.getWindow().peekDecorView(), Gravity.CENTER, 0, 0);
                        } else {
                            ///如果正在显示等待框，，，则结束掉等待框然后重新显示
                            popupWindow.dismiss();
                            popupWindow.showAtLocation(mContext.getWindow().peekDecorView(), Gravity.CENTER, 0, 0);
                        }

                        WindowManager.LayoutParams layoutParams = mContext.getWindow().getAttributes();
                        layoutParams.alpha = 0.7f;
                        mContext.getWindow().setAttributes(layoutParams);


                        TextView tvTitle = contentView.findViewById(R.id.tv_title_dialog);
                        TextView tvContent = contentView.findViewById(R.id.tv_content_dialog);
                        RecyclerView rvDialog = contentView.findViewById(R.id.rv_dialog);


                        if (!TextUtils.isEmpty(dialogBean.getTitle())){
                            tvTitle.setVisibility(View.VISIBLE);
                            tvTitle.setText(dialogBean.getTitle());
                        }else {
                            tvTitle.setVisibility(View.GONE);
                        }

                        if (!TextUtils.isEmpty(dialogBean.getContent())){
                            tvContent.setVisibility(View.VISIBLE);
                            tvContent.setText(dialogBean.getContent());
                        }else {
                            tvContent.setVisibility(View.GONE);
                        }

                        rvDialog.setLayoutManager(new LinearLayoutManager(mContext));
                        BaseQuickAdapter<DialogBean.ButtonBean, BaseViewHolder> adapter =
                                new BaseQuickAdapter<DialogBean.ButtonBean, BaseViewHolder>(R.layout.layout_item_dialog,dialogBean.getButton()) {
                                    @Override
                                    protected void convert(BaseViewHolder helper, DialogBean.ButtonBean item) {
                                        TextView textView = helper.getView(R.id.tv_dialog_item);
                                        textView.setText(item.getText());
                                        textView.setTextColor(Color.parseColor(FormatUtils.getInstance().colorTo6Color(item.getTextColor())));
                                    }
                                };
                        rvDialog.setAdapter(adapter);
                        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                sendCallBack(dialogBean.getCallback(),"200","success",dialogBean.getButton().get(position).getText());
                                popupWindow.dismiss();
                            }
                        });


                        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                layoutParams.alpha = 1.0f;
                                mContext.getWindow().setAttributes(layoutParams);
                            }
                        });
                    }

                }
            }
        });
    }

    /**
     * menu
     *
     * @param value
     */
    @JavascriptInterface
    public void menu(String value) {
        LogUtils.log(value);
        MenuBean menuBean = GsonUtils.jsonToBean(value,MenuBean.class);
        if (menuBean.getMenuList().size()>8){
            ViewUnits.getInstance().showToast("不得超过8个item");
            return;
        }
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_popup_menu, null);
                PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setContentView(contentView);
                popupWindow.setTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(false);
                        popupWindow.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), (Bitmap) null));
                if (!popupWindow.isShowing()) {
                    popupWindow.showAtLocation(mContext.getWindow().peekDecorView(), Gravity.BOTTOM, 0, 0);
                } else {
                    ///如果正在显示等待框，，，则结束掉等待框然后重新显示
                    popupWindow.dismiss();
                    popupWindow.showAtLocation(mContext.getWindow().peekDecorView(), Gravity.BOTTOM, 0, 0);
                }

                WindowManager.LayoutParams layoutParams = mContext.getWindow().getAttributes();
                layoutParams.alpha = 0.7f;
                mContext.getWindow().setAttributes(layoutParams);


                TextView tvCancel = contentView.findViewById(R.id.tv_cancel_menu);
                View viewDivier = contentView.findViewById(R.id.view_menu_divider);
                RecyclerView rvMenu = contentView.findViewById(R.id.rv_menu);


                if (!TextUtils.isEmpty(menuBean.getCancelBtnText())){
                    tvCancel.setVisibility(View.VISIBLE);
                    tvCancel.setText(menuBean.getCancelBtnText());
                    tvCancel.setVisibility(View.VISIBLE);
                    viewDivier.setVisibility(View.VISIBLE);
                }else {
                    tvCancel.setVisibility(View.GONE);
                    viewDivier.setVisibility(View.GONE);
                }

                rvMenu.setLayoutManager(new LinearLayoutManager(mContext));
                BaseQuickAdapter<String, BaseViewHolder> adapter =
                        new BaseQuickAdapter<String, BaseViewHolder>(R.layout.layout_item_menu,menuBean.getMenuList()) {
                            @Override
                            protected void convert(BaseViewHolder helper, String item) {
                                helper.setText(R.id.tv_menu_item,item);
                            }

                        };
                rvMenu.setAdapter(adapter);
                adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        sendCallBack(menuBean.getCallback(),"200","success",menuBean.getMenuList().get(position));
                        popupWindow.dismiss();
                    }
                });

                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendCallBack(menuBean.getCallback(),"200","success",menuBean.getCancelBtnText());
                        popupWindow.dismiss();
                    }
                });


                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        layoutParams.alpha = 1.0f;
                        mContext.getWindow().setAttributes(layoutParams);
                    }
                });
            }
        });
    }


    /**
     * @param value  获取卡片code
     */
    @JavascriptInterface
    public void getCardCode(String value) {
        LogUtils.log(value);
        mContext.cardCodeCallback = value;
        mContext.mCardPrenInter.getCardCode(NetCode.Card2.getCardCode,mContext.getCardId());
    }

    /**
     * @param value  获取微信openId
     */
    @JavascriptInterface
    public void getWXopenId(String value) {
        LogUtils.log(value);
        mContext.openIdCallback = value;
        if (!TextUtils.isEmpty(UserUnits.getInstance().getToken())){
            mContext.mCardPrenInter.getWXOpenId(NetCode.Set.wxOpenId,BaseUnits.getInstance().getPhoneKey());
        }else {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", "1");
                jsonObject.put("value", "用户未登录");
                sendCallBackJson(value, "500", "用户未登录", jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


//    /**
//     * 播放语音
//     *
//     * @param value
//     */
//    @JavascriptInterface
//    public void LXPlayAudio(String value) {
//        Log.d("libin", value);
//        try {
//            JSONObject jsonObject = new JSONObject(value);
//            int type = jsonObject.getInt("type");
//            switch (type) {
//                case 1:
//                    MediaUtils.getInstance().playAudio(R.raw.patrol_warn);
//                    break;
//                case 2:
//                    MediaUtils.getInstance().playAudio(R.raw.patrol_finish);
//                    break;
//            }
//
//            Vibrator vibrator = (Vibrator) mContext.getSystemService(VIBRATOR_SERVICE);
//            if (vibrator != null) {
//                vibrator.vibrate(2000);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }



    public void shareText(String callBack, String title, String content) {
        if (BaseUnits.getInstance().getShareList(mContext).length == 0) {
            ViewUnits.getInstance().showToast("您未安装微信、微博和QQ");
            return;
        }

        new ShareAction(mContext)
                .withText(content)
                .setDisplayList(BaseUnits.getInstance().getShareList(mContext))
                .setCallback(new MyUMShareListener(callBack, this))
                .open();
    }

    public void shareUrl(String callBack, String title, String content, String url, String image) {
        Log.e(TAG, title);
        Log.e(TAG, content);
        Log.e(TAG, url);
        Log.e(TAG, image);
        if (BaseUnits.getInstance().getShareList(mContext).length == 0) {
            ViewUnits.getInstance().showToast("您未安装微信、微博和QQ");
            return;
        }

        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题

        if (image.startsWith("http")) {
            web.setThumb(new UMImage(mContext, image));
        } else {
            web.setThumb(new UMImage(mContext, BaseUnits.getInstance().base64ToBitmap(image)));  //缩略图
        }


        web.setDescription(content);//描述

        new ShareAction(mContext)
                .setDisplayList(BaseUnits.getInstance().getShareList(mContext))
                .setCallback(new MyUMShareListener(callBack, this))
                .withMedia(web)
                .open();
    }

    public void shareImage(String callBack, String image) {
        if (BaseUnits.getInstance().getShareList(mContext).length == 0) {
            ViewUnits.getInstance().showToast("您未安装微信、微博和QQ");
            return;
        }

        UMImage umImage;
        if (image.startsWith("http")) {
            umImage = new UMImage(mContext, image);
        } else {
            umImage = new UMImage(mContext, BaseUnits.getInstance().base64ToBitmap(image));
        }


        new ShareAction(mContext)
                .setDisplayList(BaseUnits.getInstance().getShareList(mContext))
                .setCallback(new MyUMShareListener(callBack, this))
                .withMedia(umImage)
                .open();
    }

    public void sendCallBack(String callBack, String status, String msg, String value) {
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("value", value);
            jsonObject.put("status", status);
            jsonObject.put("msg", msg);
            jsonObject.put("data", data);
            LogUtils.log(jsonObject.toString());
            mCallBack.callBackResult(callBack, jsonObject.toString());
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
            mCallBack.callBackResult(callBack, "未知错误，联系管理员");
            e.printStackTrace();
        }
    }


    public void sendCallBackJson(String callBack, String status, String msg, JSONObject value) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status", status);
            jsonObject.put("msg", msg);
            jsonObject.put("data", value);
            Log.e(TAG, String.valueOf(jsonObject));
            mCallBack.callBackResult(callBack, jsonObject.toString());
        } catch (JSONException e) {
            mCallBack.callBackResult(callBack, "未知错误，联系管理员");
            e.printStackTrace();
        }
    }


    @Override
    public void getShareResult(String callBack, String status, String message) {
        sendCallBack(callBack, status, message, message);
    }

}
