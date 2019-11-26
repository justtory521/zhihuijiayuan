package tendency.hz.zhihuijiayuan.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.json.JSONException;
import org.json.JSONObject;

import tendency.hz.zhihuijiayuan.BuildConfig;
import tendency.hz.zhihuijiayuan.MainActivity;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.AppCardItem;
import tendency.hz.zhihuijiayuan.bean.CardItem;
import tendency.hz.zhihuijiayuan.bean.base.App;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.bean.base.What;
import tendency.hz.zhihuijiayuan.presenter.BasePrenImpl;
import tendency.hz.zhihuijiayuan.presenter.CardPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.SetPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.BasePrenInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.CardPrenInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.SetPrenInter;
import tendency.hz.zhihuijiayuan.units.AddressDbHelper;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.BluetoothUtils;
import tendency.hz.zhihuijiayuan.units.ConfigUnits;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.SPUtils;
import tendency.hz.zhihuijiayuan.units.UserUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.card.CardContentActivity;
import tendency.hz.zhihuijiayuan.view.index.GuideActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

import static tendency.hz.zhihuijiayuan.bean.base.Request.Permissions.REQUEST_ALL_PERMISSIONS;

/**
 * Created by JasonYao on 2018/3/23.
 */

public class SplashActivity extends BaseActivity implements AllViewInter {
    private static final String TAG = "SplashActivity---";

    private SetPrenInter mSetPrenInter;
    private CardPrenInter mCardPrenInter;
    private BasePrenInter mBasePrenInter;

    private String mCode;
    //邀请码
    private String mInvitationCode;
    private CardItem mCardItem;

    //声明LocationClient 对象
    private LocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    private LocationClientOption mLocationOption = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slpash);

        mSetPrenInter = new SetPrenImpl(this);
        mCardPrenInter = new CardPrenImpl(this);
        mBasePrenInter = new BasePrenImpl(this);

        mSetPrenInter.startPage(NetCode.Set.startPage);  //异步校验、下载广告相关内容

        getAllPermission();

        if (!checkDBState()) {
            ViewUnits.getInstance().showToast("配置失败，请重新打开APP!");
            return;
        }

        ((TextView) findViewById(R.id.text_version)).setText(BuildConfig.VERSION_NAME);

    }

    /**
     * 剪切板跳转到指定页面
     */
    private void date() {
        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData data = cm.getPrimaryClip();
        try {
            ClipData.Item item = data.getItemAt(0);
            String content = item.getText().toString();
            try {
                JSONObject jsonObject = new JSONObject(content);
                mCode = jsonObject.getString("code");
                mInvitationCode = jsonObject.getString("InvitationCode");

                cm.setPrimaryClip(ClipData.newPlainText(null, ""));  //清除剪切板数据

                if (TextUtils.isEmpty(mCode)) {
                    new Handler().postDelayed(() -> initData(), 1000);
                } else {
                    startIntent();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                new Handler().postDelayed(() -> initData(), 1000);
            }

        } catch (Exception e) {
            Log.d("libin", "date: " + e.getMessage());
            new Handler().postDelayed(() -> initData(), 1000);
        }
    }

    //根据拦截的url跳转页面
    private void startIntent() {
        mSetPrenInter.cardQrCode(NetCode.Set.cardQrCode, mCode);
    }


    private void initData() {
        if (ConfigUnits.getInstance().getFristInstallStatus()) {
            startActivity(new Intent(this, GuideActivity.class));
            finish();
            return;
        }

        if (!FormatUtils.getInstance().isEmpty(ConfigUnits.getInstance().getAdImg())) {
            startActivity(new Intent(this, AdActivity.class));
            finish();
            return;
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }


    /**
     * 检查配置数据库是否已经从asset拷贝到data下
     *
     * @return
     */
    private boolean checkDBState() {
        if (FormatUtils.getInstance().isEmpty(ConfigUnits.getInstance().getDBStatus()) ||
                !ConfigUnits.getInstance().getDBStatus().equals("2")) {  //没有拷贝，则进行拷贝
            return AddressDbHelper.getInstance().copyDBFileInside();
        }

        return true;
    }

    /**
     * 跳转至卡页面（分为应用卡和业务卡）
     */
    private void jumpToCard(CardItem cardItem) {
        if (cardItem.getCardType().equals(What.CardType.businessCard)) {
            Intent intent = new Intent(this, CardContentActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("cardItem", cardItem);
            intent.putExtra("type", Request.StartActivityRspCode.SLPASH_CARDCONTENT_JUMP);
            startActivity(intent);
            finish();
        } else {
            mCardPrenInter.getAppCardInfo(NetCode.Card.getAppCardInfo, cardItem.getCardID());
        }
    }

//    private void addCard(CardItem cardItem) {
//        mCardPrenInter.cardAttentionAdd(NetCode.Card.cardAttentionAdd, cardItem);
//    }

    /**
     * Android6.0动态获取所有权限
     */
    private void getAllPermission() {
        ActivityCompat.requestPermissions(this, App.mPermissionList, REQUEST_ALL_PERMISSIONS);
    }

    private void initLocation() {
        mLocationClient = new LocationClient(this.getApplicationContext());
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {

                if (bdLocation != null && !TextUtils.isEmpty(bdLocation.getCity())) {
                    UserUnits.getInstance().setLocation(bdLocation.getCity());
                    mLocationClient.stop();
                    mLocationClient = null;
                    if (TextUtils.isEmpty(UserUnits.getInstance().getSelectCity())) {
                        UserUnits.getInstance().setSelectCity(bdLocation.getCity());
                    }
                }

                date();

            }
        });
        mLocationOption = new LocationClientOption();
        mLocationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        mLocationOption.setCoorType("bd09ll");
        mLocationOption.setScanSpan(0);
        mLocationOption.setIsNeedAddress(true);
        mLocationClient.setLocOption(mLocationOption);
        mLocationClient.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ALL_PERMISSIONS) {
            if (!BaseUnits.getInstance().checkPermission(this, Manifest.permission.READ_PHONE_STATE)) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                        AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this)
                                .setTitle("提示")
                                .setMessage("未获得相应权限，无法正常使用APP，请前往安全中心>权限管理>智慧家园，开启相关权限")
                                .setIcon(R.mipmap.logo)
                                .setPositiveButton("确定", (dialogInterface, i1) -> {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivityForResult(intent, REQUEST_ALL_PERMISSIONS);
                                })
                                .setNegativeButton("取消", (dialogInterface, i12) -> SplashActivity.this.finish()).create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                        return;
                    }
                }
                AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this)
                        .setTitle("提示")
                        .setMessage("未获得读取手机状态权限，无法正常使用APP，是否给予权限？")
                        .setIcon(R.mipmap.logo)
                        .setPositiveButton("是", (dialogInterface, i1) -> getAllPermission())
                        .setNegativeButton("否", (dialogInterface, i12) -> SplashActivity.this.finish()).create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();
            } else if (!BaseUnits.getInstance().checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this)
                                .setTitle("提示")
                                .setMessage("未获得定位权限，无法正常使用APP，请前往安全中心>权限管理>智慧家园，开启相关权限")
                                .setIcon(R.mipmap.logo)
                                .setPositiveButton("确定", (dialogInterface, i1) -> {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivityForResult(intent, REQUEST_ALL_PERMISSIONS);
                                })
                                .setNegativeButton("取消", (dialogInterface, i12) -> SplashActivity.this.finish()).create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                        return;
                    }
                }
                AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this)
                        .setTitle("提示")
                        .setMessage("未获得定位权限，无法正常使用APP，是否给予权限？")
                        .setIcon(R.mipmap.logo)
                        .setPositiveButton("是", (dialogInterface, i1) -> getAllPermission())
                        .setNegativeButton("否", (dialogInterface, i12) -> SplashActivity.this.finish()).create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();
            } else if (!BaseUnits.getInstance().checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this)
                        .setTitle("提示")
                        .setMessage("未获得读写权限，无法正常使用APP，是否给予权限？")
                        .setIcon(R.mipmap.logo)
                        .setPositiveButton("是", (dialogInterface, i1) -> getAllPermission())
                        .setNegativeButton("否", (dialogInterface, i12) -> SplashActivity.this.finish()).create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();
            } else {
                mBasePrenInter.addAppDeviceInfo(NetCode.Base.appDeivceInfo);
                initLocation();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ALL_PERMISSIONS) {
            getAllPermission();
        }
    }

    @Override
    public void onSuccessed(int what, Object object) {
        switch (what) {
            case NetCode.User.checkToken:
                if (FormatUtils.getInstance().isEmpty(ConfigUnits.getInstance().getAdImg())) {
                    startActivity(new Intent(this, AdActivity.class));
                    finish();
                    return;
                }

                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case NetCode.Set.cardQrCode:
                mCardItem = (CardItem) object;
                if (!TextUtils.isEmpty(mInvitationCode)) {
                    SPUtils.getInstance().put(SPUtils.FILE_CARD, SPUtils.cardNo, mCardItem.getCardID());
                    SPUtils.getInstance().put(SPUtils.FILE_CARD, SPUtils.invitationCode, mInvitationCode);
                }


                jumpToCard(mCardItem);
//                if ("1".equals(mCardItem.getFocusStatus())) {  //用户已经关注，直接跳转转卡详情页面
//                    jumpToCard(mCardItem);
//                } else {  //用户未关注，先执行领卡
//                    addCard(mCardItem);
//                }
                break;
            case NetCode.Card.getAppCardInfo:
                AppCardItem appCardItem = (AppCardItem) object;
                if (BaseUnits.getInstance().isApkInstalled(this, appCardItem.getAndroidAppID())) {  //应用卡，先判断手机是否下载该app
                    //已下载该APP，直接打开APP
                    BaseUnits.getInstance().openAppCard(this, appCardItem.getAndroidAppID());
                } else {
                    //未下载该APP，则先进行下载
                    if (BaseUnits.getInstance().isEmpty(appCardItem.getAndroidDownUrl())) {
                        ViewUnits.getInstance().showToast("该应用卡无安卓版本");
                        return;
                    }
                    BaseUnits.getInstance().loadApk(this, appCardItem.getAndroidDownUrl());
                }
                break;
//            case NetCode.Card.anonymousFocus:
//            case NetCode.Card.cardAttentionAdd:
//                jumpToCard(mCardItem);
//                break;
        }

    }

    @Override
    public void onFailed(int what, Object object) {
//        switch (what) {
//            case NetCode.Card.anonymousFocus:
//            case NetCode.Card.cardAttentionAdd:
//                jumpToCard(mCardItem);
//                break;
//            default:
//                Intent intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            //do something.
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSetPrenInter = null;
        mCardPrenInter = null;
        mBasePrenInter = null;
        mCode = null;
        mCardItem = null;
        mLocationClient = null;
        mLocationOption = null;
    }
}


