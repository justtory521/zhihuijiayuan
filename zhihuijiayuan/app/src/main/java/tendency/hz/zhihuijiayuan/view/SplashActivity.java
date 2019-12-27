package tendency.hz.zhihuijiayuan.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
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
import tendency.hz.zhihuijiayuan.application.MyApplication;
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
import tendency.hz.zhihuijiayuan.units.ConfigUnits;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.SPUtils;
import tendency.hz.zhihuijiayuan.units.UserUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.card.CardContentActivity;
import tendency.hz.zhihuijiayuan.view.index.GuideActivity;
import tendency.hz.zhihuijiayuan.view.user.AgreementActivity;
import tendency.hz.zhihuijiayuan.view.user.PrivacyStatementActivity;
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
    private LocationClient mLocationClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slpash);

        mSetPrenInter = new SetPrenImpl(this);
        mCardPrenInter = new CardPrenImpl(this);
        mBasePrenInter = new BasePrenImpl(this);

        mSetPrenInter.startPage(NetCode.Set.startPage);  //异步校验、下载广告相关内容

        showPrivacyDialog();


        if (!checkDBState()) {
            ViewUnits.getInstance().showToast("配置失败，请重新打开APP!");
        }

    }

    /**
     * 首次安装隐私声明弹窗
     */
    private void showPrivacyDialog() {
        if (!ConfigUnits.getInstance().getFirstInstallStatus()){
            getAllPermission();
            return;
        }

        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        //对话框弹出后点击或按返回键不消失;
        dialog.setCancelable(false);

         Window window = dialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.popup_privacy);
            window.setGravity(Gravity.CENTER);
            TextView textView = window.findViewById(R.id.tv_privacy_content);
            TextView tvCancel= window.findViewById(R.id.tv_privacy_cancel);
            TextView tvAgree= window.findViewById(R.id.tv_privacy_agree);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SplashActivity.this.finish();
                }
            });
            tvAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    getAllPermission();
                }
            });
            String str = "\t\t\t\t感谢您选择智慧家园APP!我们非常重视您的个人信息和隐私保护。" +
                    "为了更好地保障您的个人权益，在您使用我们的产品前，" +
                    "请务必审阅《用户协议》和《隐私政策》内的所有条款，" +
                    "您点击“同意”的行为即表示您已阅读并同意以上协议的全部内容。" +
                    "如您同意以上协议内容，请点击“同意”，开始使用我们的产品和服务!";
            textView.setText(str);

            SpannableStringBuilder ssb = new SpannableStringBuilder();
            ssb.append(str);
            final int start = str.indexOf("《");//第一个出现的位置
            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    startActivity(new Intent(SplashActivity.this, AgreementActivity.class));

                }

                @Override

                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(ContextCompat.getColor(SplashActivity.this,R.color.colorTextBlue));       //设置文件颜色
                    // 去掉下划线
                    ds.setUnderlineText(false);
                }

            }, start, start + 6, 0);

            final int end = str.lastIndexOf("《");//最后一个出现的位置

            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    startActivity(new Intent(SplashActivity.this, PrivacyStatementActivity.class));
                }

                @Override

                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor((ContextCompat.getColor(SplashActivity.this,R.color.colorTextBlue)));       //设置文件颜色
                    // 去掉下划线

                    ds.setUnderlineText(false);
                }

            }, end, end + 6, 0);

            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText(ssb, TextView.BufferType.SPANNABLE);
        }

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
        if (ConfigUnits.getInstance().getFirstInstallStatus()) {
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

    /**
     * Android6.0动态获取所有权限
     */
    private void getAllPermission() {
        ActivityCompat.requestPermissions(this, App.mPermissionList, REQUEST_ALL_PERMISSIONS);
    }

    private void initLocation() {
        mLocationClient = new LocationClient(MyApplication.getInstance());
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {

                if (bdLocation != null && !TextUtils.isEmpty(bdLocation.getCity())) {
                    UserUnits.getInstance().setLocation(bdLocation.getCity());
                    if (mLocationClient !=null){
                        mLocationClient.stop();
                        mLocationClient = null;
                    }

                    if (TextUtils.isEmpty(UserUnits.getInstance().getSelectCity())) {
                        UserUnits.getInstance().setSelectCity(bdLocation.getCity());
                    }
                }

                date();

            }
        });
        LocationClientOption mLocationOption = new LocationClientOption();
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
        }

    }

    @Override
    public void onFailed(int what, Object object) {

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

        mSetPrenInter = null;
        mCardPrenInter = null;
        mBasePrenInter = null;
        mCode = null;
        mCardItem = null;

        if (mLocationClient !=null && mLocationClient.isStarted()){
            mLocationClient.stop();
            mLocationClient = null;
        }

        super.onDestroy();
    }
}


