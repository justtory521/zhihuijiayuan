package tendency.hz.zhihuijiayuan.view.card;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apkfuns.log2file.LogFileEngineFactory;
import com.apkfuns.logutils.LogLevel;
import com.apkfuns.logutils.file.LogFileFilter;
import com.cjt2325.cameralibrary.util.LogUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.media.UMImage;
import com.wearlink.blecomm.BleCommMethod;
import com.wearlink.blecomm.BleService;
import com.wearlink.blecomm.OnBleCommProgressListener;

import org.json.JSONException;
import org.json.JSONObject;

import tendency.hz.zhihuijiayuan.MainActivity;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.AppCardItem;
import tendency.hz.zhihuijiayuan.bean.CardItem;
import tendency.hz.zhihuijiayuan.bean.IDCardBean;
import tendency.hz.zhihuijiayuan.bean.base.App;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.databinding.ActivityCardContentBinding;
import tendency.hz.zhihuijiayuan.inter.AndroidToJSCallBack;
import tendency.hz.zhihuijiayuan.listener.GetImageListener;
import tendency.hz.zhihuijiayuan.listener.MainUMShareListener;
import tendency.hz.zhihuijiayuan.presenter.CardPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.CardPrenInter;
import tendency.hz.zhihuijiayuan.units.AndroidtoJS;
import tendency.hz.zhihuijiayuan.units.Base64Utils;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.BleCommStatus;
import tendency.hz.zhihuijiayuan.units.BluetoothUtils;
import tendency.hz.zhihuijiayuan.units.CacheUnits;
import tendency.hz.zhihuijiayuan.units.ConfigUnits;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.ImageUtils;
import tendency.hz.zhihuijiayuan.units.LogUtils;
import tendency.hz.zhihuijiayuan.units.PermissionUtils;
import tendency.hz.zhihuijiayuan.units.QrCodeUnits;
import tendency.hz.zhihuijiayuan.units.UserUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.SplashActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;
import tendency.hz.zhihuijiayuan.widget.AndroidBug5497Workaround;

import static tendency.hz.zhihuijiayuan.bean.base.Request.Permissions.REQUEST_CONTACTS_PERMISSIONS;
import static tendency.hz.zhihuijiayuan.bean.base.Request.Permissions.REQUEST_RECORD_PERMISSIONS;

/**
 * 卡详情页面
 * Created by JasonYao on 2018/4/3.
 */

public class CardContentActivity extends BaseActivity implements AllViewInter, AndroidToJSCallBack {
    private static final String TAG = "libin";
    public static CardContentActivity mInstance;
    private ActivityCardContentBinding mBinding;
    private WebView mWebView;

    public CardPrenInter mCardPrenInter;

    private CardItem mCardItem;
    private int mJumpType;  //标记跳转来源

    //底部弹出的选项框

    private Dialog mDialogBottom;

    private GetImageListener mListener; //获取图片监听类

    private boolean isFirstLoaded = true;  //标记是否为第一次加载
    private IntentFilter mFilter = new IntentFilter();
    public static boolean isRunning = false;
    private AnimationDrawable mAnimationDrawable;
    private String mJumpUrl;  //跳转的Url,用于推送消息和消息列表里面页面跳转
    private AndroidtoJS androidtoJS;

    //蓝牙服务
    private BleService bleService = null;
    private BleCommMethod bleCommMethod;
    //蓝牙是否连接
    private boolean isConnect;
    //蓝牙名称
    private String blueToothName;
    //蓝牙mac
    private String blueToothAddress;
    //连接callback
    private String connectCallback;
    //断开callback
    private String disconnectCallback;
    //通知callback
    private String notifyCallback;
    //发送callback
    private String sendCallback;
    //扫描连接超时定时器
    private CountDownTimer countDownTimer;
    //手动断开
    private boolean disconnectOnclick;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_card_content);
        mWebView = new WebView(this);
        mWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setVerticalScrollBarEnabled(false);

        IX5WebViewExtension ix5 = mWebView.getX5WebViewExtension();
        if (null != ix5) {
            ix5.setScrollBarFadingEnabled(false);
        }
        mBinding.llCard.addView(mWebView);

        AndroidBug5497Workaround.assistActivity(mBinding.getRoot());  //解决在沉浸式菜单栏中，软键盘不能顶起页面的bug

        mFilter.addAction(Request.Broadcast.RELOADURL);
        this.registerReceiver(mBroadcastReceiver, mFilter);

        initView();

        setListener();

        initData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mInstance = this;
        isRunning = true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        showFocusWindow();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }

    /**
     * 刷新
     */
    public void refreshCard() {
        this.runOnUiThread(() -> mWebView.reload());
    }

    public String getCardId() {
        return mCardItem.getCardID();
    }


    public static CardContentActivity getInstance() {
        return mInstance;
    }

    private void initData() {
        mCardPrenInter = new CardPrenImpl(this);


        mCardItem = (CardItem) super.getIntent().getSerializableExtra("cardItem");
        mJumpType = super.getIntent().getIntExtra("type", Request.StartActivityRspCode.NORMAL_CARDCONTENT_JUMP);  //默认为正常跳转

        if (mJumpType == Request.StartActivityRspCode.PUSH_CARDCONTENT_JUMP || mJumpType == Request.StartActivityRspCode.MESSAGE_CARDCONTENT_JUMP) {
            mJumpUrl = mCardItem.getCardUrl();
        }

        //记录一次卡片点击(异步执行，不计请求结果)
        mCardPrenInter.cardClickVolume(NetCode.Card.clickVolumeAdd, mCardItem.getCardID(),
                ConfigUnits.getInstance().getCityIdByName(UserUnits.getInstance().getLocation()));

        if (mJumpType == Request.StartActivityRspCode.NORMAL_CARDCONTENT_JUMP) {  //正常跳转
            //校验卡片有效性
            mCardPrenInter.checkCanOperate(NetCode.Card2.checkCanOperate, mCardItem.getCardID());
        } else {  //推送跳转过来
            mCardPrenInter.previewCard(NetCode.Card.previewCard, mCardItem.getCardID());
        }

    }

    private void initView() {
        WebSettings webSetting = mWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setUserAgentString(webSetting.getUserAgentString() + "-Android");  //设置用户代理
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setMediaPlaybackRequiresUserGesture(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSetting.setAllowFileAccessFromFileURLs(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(0);
        }

        mBinding.imgLoading.setBackgroundResource(R.drawable.anim_loading);
        mAnimationDrawable = (AnimationDrawable) mBinding.imgLoading.getBackground();

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onGeolocationPermissionsShowPrompt(String s, GeolocationPermissionsCallback geolocationPermissionsCallback) {
                geolocationPermissionsCallback.invoke(s, true, false);
                super.onGeolocationPermissionsShowPrompt(s, geolocationPermissionsCallback);
            }


            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if (isFirstLoaded) {
                    mBinding.layoutLoading.setVisibility(View.VISIBLE);
                    Log.e(TAG, newProgress + "");
                    if (newProgress >= 100) {
                        isFirstLoaded = false;
                        mBinding.layoutLoading.startAnimation(AnimationUtils.loadAnimation(CardContentActivity.this, R.anim.layout_card_loading_close));
                        mBinding.layoutLoading.setVisibility(View.GONE);
                        if (mAnimationDrawable != null) {
                            mAnimationDrawable.stop();
                        }

                        showFocusWindow();

                    }
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });

        mWebView.setWebViewClient(new com.tencent.smtt.sdk.WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url == null) return false;
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                } else {
                    try {
                        Intent intent = new Intent();
                        intent.setData(Uri.parse(url));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    } catch (Exception e) {
                        return true;
                    }
                }
                return true;
            }

        });

        androidtoJS = new AndroidtoJS(this);
        mWebView.addJavascriptInterface(androidtoJS, "NativeForJSUnits");
        mBinding.layoutTitle.setPadding(0, BaseUnits.getInstance().getStatusBarHeight(), 0, 0);
        mBinding.btnTitle.setPadding(0, BaseUnits.getInstance().getStatusBarHeight(), 0, 0);
        LinearLayout.LayoutParams titleLayoutLp = (LinearLayout.LayoutParams) mBinding.layoutTitle.getLayoutParams();
        titleLayoutLp.height = (int) (BaseUnits.getInstance().getStatusBarHeight() + ViewUnits.getInstance().jsPX2AndroidPX(44));
        mBinding.layoutTitle.setLayoutParams(titleLayoutLp);
        FrameLayout.LayoutParams titleLayoutBtn = (FrameLayout.LayoutParams) mBinding.btnTitle.getLayoutParams();
        titleLayoutBtn.height = (int) (BaseUnits.getInstance().getStatusBarHeight() + ViewUnits.getInstance().jsPX2AndroidPX(44));
        mBinding.btnTitle.setLayoutParams(titleLayoutBtn);
        startAnimation();
    }


    /**
     * 弹出关注栏
     */
    private void showFocusWindow(){
        //卡片跳卡片，不弹出关注tip
        LogUtils.log(mJumpType+","+CacheUnits.getInstance().singleCacheCard(mCardItem));
        if (mJumpType != Request.StartActivityRspCode.CARD_CARDCONTENT_JUMP && !CacheUnits.getInstance().singleCacheCard(mCardItem)) {
            mBinding.rlFocusCard.setVisibility(View.VISIBLE);
            mBinding.ivCloseFocus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBinding.rlFocusCard.setVisibility(View.GONE);
                }
            });
            mBinding.tvFocusCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(UserUnits.getInstance().getToken())) {
                        mCardPrenInter.cardAttentionAdd(NetCode.Card.cardAttentionAdd, mCardItem);
                    } else {
                        mCardPrenInter.cardAttentionAdd(NetCode.Card.anonymousFocus, mCardItem);
                    }
                }
            });
        }else {
            mBinding.rlFocusCard.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        LogUtils.log("返回");
        if (mWebView.canGoBack()) {
            LogUtils.log("返回1");
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }

    }

    private void setListener() {

        mBinding.imgBtnBack.setOnClickListener(view -> {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                mInstance = null;
                finish();
            }
        });

    }


    /**
     * 初始化底部弹出框
     */
    private void showPopWindow() {
        if (mDialogBottom != null) {
            mDialogBottom.show();
            return;
        }

        mDialogBottom = new Dialog(this, R.style.ActionSheetDialogStyle);

        //填充对话框的布局
        View mBottomView = LayoutInflater.from(this).inflate(R.layout.layout_card_buttom_popup, null);
        //初始化控件
        TextView mCancel = mBottomView.findViewById(R.id.btn_cancel);
        TextView mCancelForce = mBottomView.findViewById(R.id.btn_cancel_force);
        TextView mShare = mBottomView.findViewById(R.id.btn_share);

        //将布局设置给Dialog
        mDialogBottom.setContentView(mBottomView);
        //获取当前Activity所在的窗体
        Window dialogWindow = mDialogBottom.getWindow();
        dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);

        mCancel.setOnClickListener(view -> mDialogBottom.dismiss());
        mCancelForce.setOnClickListener(view -> {  //取消关注卡片
            ViewUnits.getInstance().showLoading(this, "取消关注中");
            mCardPrenInter.deleteCard(NetCode.Card.deleteCard, mCardItem.getID(), mCardItem.getCardID());
        });

        mShare.setOnClickListener(view -> {  //分享卡片
            ViewUnits.getInstance().showLoading(this, "请求中");
            mCardPrenInter.getCardQrCodeUrl(NetCode.Card2.getShareQrCodeUrl, mCardItem.getCardID());
        });

        mDialogBottom.show();
    }

    public void more(View view) {
        showPopWindow();
    }

    public void finish1(View view) {
        closeCard();
    }

    /**
     * @param view 返回
     */
    public void comeback(View view) {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            mInstance = null;
            finish();
        }
    }


    /**
     * 拍照或选择照片
     */
    public void takePhoto(GetImageListener listener) {
        mListener = listener;
        if (PermissionUtils.getInstance().hasPermission(this, App.pictureSelect)) {
            PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofImage())
                    .selectionMode(PictureConfig.SINGLE)
                    .isCamera(true)
                    .enableCrop(true)// 是否裁剪 true or false
                    .compress(true)// 是否压缩 true or false
                    .previewImage(false)
                    .setOutputCameraPath(tendency.hz.zhihuijiayuan.bean.base.Uri.camera)
                    .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                    .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                    .showCropGrid(false)
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                    .isDragFrame(true)// 是否可拖动裁剪框(固定)
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        } else {
            ActivityCompat.requestPermissions(this, App.pictureSelect, Request.Permissions.REQUEST_CAMERA);
        }

    }

    /**
     * 拍照或选择照片
     */
    public void takePhoto(GetImageListener listener, int width, int height) {
        mListener = listener;

        if (PermissionUtils.getInstance().hasPermission(this, App.pictureSelect)) {
            PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofImage())
                    .selectionMode(PictureConfig.SINGLE)
                    .isCamera(true)
                    .enableCrop(true)// 是否裁剪 true or false
                    .compress(true)// 是否压缩 true or false
                    .previewImage(false)
                    .setOutputCameraPath(tendency.hz.zhihuijiayuan.bean.base.Uri.camera)
                    .withAspectRatio(width, height)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                    .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                    .showCropGrid(false)
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                    .isDragFrame(true)// 是否可拖动裁剪框(固定)
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        } else {
            ActivityCompat.requestPermissions(this, App.pictureSelect, Request.Permissions.REQUEST_CAMERA);
        }

    }

    public void openGallery(GetImageListener listener, int width, int height) {
        mListener = listener;

        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .selectionMode(PictureConfig.SINGLE)
                .previewImage(false)
                .isCamera(false)
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .setOutputCameraPath(tendency.hz.zhihuijiayuan.bean.base.Uri.camera)
                .withAspectRatio(width, height)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropGrid(false)
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .isDragFrame(true)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

    }


    public void openGallery(GetImageListener listener) {
        mListener = listener;


        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .selectionMode(PictureConfig.SINGLE)
                .previewImage(false)
                .isCamera(false)
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .setOutputCameraPath(tendency.hz.zhihuijiayuan.bean.base.Uri.camera)
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropGrid(false)
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .isDragFrame(true)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

    }


    public void openTheCamera(GetImageListener listener) {
        mListener = listener;


        if (PermissionUtils.getInstance().hasPermission(this, App.pictureSelect)) {
            PictureSelector.create(this)
                    .openCamera(PictureMimeType.ofImage())
                    .selectionMode(PictureConfig.SINGLE)
                    .previewImage(false)
                    .enableCrop(true)// 是否裁剪 true or false
                    .compress(true)// 是否压缩 true or false
                    .setOutputCameraPath(tendency.hz.zhihuijiayuan.bean.base.Uri.camera)
                    .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                    .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                    .showCropGrid(false)
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                    .isDragFrame(true)// 是否可拖动裁剪框(固定)
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        } else {
            ActivityCompat.requestPermissions(this, App.pictureSelect, Request.Permissions.REQUEST_CAMERA);
        }

    }

    public void openTheCamera(GetImageListener listener, int width, int height) {
        mListener = listener;


        if (PermissionUtils.getInstance().hasPermission(this, App.pictureSelect)) {
            PictureSelector.create(this)
                    .openCamera(PictureMimeType.ofImage())
                    .selectionMode(PictureConfig.SINGLE)
                    .previewImage(false)
                    .enableCrop(true)// 是否裁剪 true or false
                    .compress(true)// 是否压缩 true or false
                    .setOutputCameraPath(tendency.hz.zhihuijiayuan.bean.base.Uri.camera)
                    .withAspectRatio(width, height)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                    .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                    .showCropGrid(false)
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                    .isDragFrame(true)// 是否可拖动裁剪框(固定)
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        } else {
            ActivityCompat.requestPermissions(this, App.pictureSelect, Request.Permissions.REQUEST_CAMERA);
        }

    }


    @Override
    public void onSuccessed(int what, Object object) {
        switch (what) {
            case NetCode.Card.anonymousCancel:
                CacheUnits.getInstance().deleteMyCacheCardById(mCardItem.getCardID());
            case NetCode.Card.deleteCard:
                CacheUnits.getInstance().deleteMyCacheCardById(mCardItem.getCardID());
                ViewUnits.getInstance().missLoading();
                ViewUnits.getInstance().showToast("取消成功");
                mInstance = null;
                finish();
                break;
            case NetCode.Card2.checkCanOperate:
                this.runOnUiThread(() -> {
                    if (mCardItem == null) {
                        return;
                    }

                    mBinding.textCardName.setText(mCardItem.getTitle());
                    mBinding.logo.setImageURI(mCardItem.getLogoUrl() == null ? mCardItem.getLogo() : mCardItem.getLogoUrl());

                    if (mJumpType == Request.StartActivityRspCode.PUSH_CARDCONTENT_JUMP || mJumpType == Request.StartActivityRspCode.MESSAGE_CARDCONTENT_JUMP) {
                        LogUtils.log("cardId" + mCardItem.getCardID());
                        LogUtils.log("JUMPURL=" + mJumpUrl);

                        mWebView.loadUrl(mJumpUrl);
                    } else {
                        LogUtils.log("cardId" + mCardItem.getCardID());
                        LogUtils.log("CardURL=" + mCardItem.getCardUrl());
                        mWebView.loadUrl(mCardItem.getCardUrl());
                    }

                });
                break;
            case NetCode.Card.previewCard:
                mCardItem = (CardItem) object;
                mCardPrenInter.checkCanOperate(NetCode.Card2.checkCanOperate, mCardItem.getCardID());
                break;

            case NetCode.Card2.previewCard:
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CardItem cardItem = (CardItem) object;

                        String data = androidtoJS.cardUrlParams;

                        if (!TextUtils.isEmpty(data)) {
                            String cardUrl = cardItem.getCardUrl();
                            if (cardUrl.contains("?")) {
                                cardItem.setCardUrl(cardUrl + "&data=" + data);
                            } else {
                                cardItem.setCardUrl(cardUrl + "?data=" + data);
                            }

                            androidtoJS.cardUrlParams = null;
                        }
                        Intent intent = new Intent(CardContentActivity.this, CardContentActivity.class);
                        intent.putExtra("cardItem", cardItem);
                        intent.putExtra("type", Request.StartActivityRspCode.CARD_CARDCONTENT_JUMP);
                        startActivity(intent);
                        mInstance = null;
                        finish();
                    }
                });
                break;

            case NetCode.Card.openOtherCard:
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CardItem cardItem = (CardItem) object;

                        String data = androidtoJS.cardUrlParams;

                        if (!TextUtils.isEmpty(data)) {
                            String cardUrl = cardItem.getCardUrl();
                            if (cardUrl.contains("?")) {
                                cardItem.setCardUrl(cardUrl + "&data=" + data);
                            } else {
                                cardItem.setCardUrl(cardUrl + "?data=" + data);
                            }

                            androidtoJS.cardUrlParams = null;
                        }
                        Intent intent = new Intent(CardContentActivity.this, CardContentActivity.class);
                        intent.putExtra("cardItem", cardItem);
                        intent.putExtra("type", Request.StartActivityRspCode.CARD_CARDCONTENT_JUMP);
                        startActivity(intent);
                    }
                });


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
            case NetCode.Card2.getShareQrCodeUrl:
                ViewUnits.getInstance().missLoading();
                mDialogBottom.dismiss();
                if (BaseUnits.getInstance().getShareList(this).length == 0) {
                    ViewUnits.getInstance().showToast("您未安装微信、微博和QQ");
                    return;
                }

                UMImage umImage = new UMImage(this, QrCodeUnits.createQRCodeWithLogo(object.toString(), 600,
                        BitmapFactory.decodeResource(getResources(), R.mipmap.logo)));
                new ShareAction(this)
                        .withText("智慧家园")
                        .setDisplayList(BaseUnits.getInstance().getShareList(this))
                        .setCallback(new MainUMShareListener())
                        .withMedia(umImage)
                        .open();
                break;

            case NetCode.Card.anonymousFocus:
            case NetCode.Card.cardAttentionAdd:
                mBinding.rlFocusCard.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onFailed(int what, Object object) {
        if (what != NetCode.Card.clickVolumeAdd) {
            ViewUnits.getInstance().showToast(object.toString());
        }
    }

    @Override
    public void callBackResult(String callBack, String value) {
        Log.d("libin", "javascript:" + callBack + "('" + value + "')");
        if (FormatUtils.getInstance().isEmpty(callBack)) {
            return;
        }

        runOnUiThread(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mWebView.post(() -> mWebView.evaluateJavascript("javascript:" + callBack + "('" + value + "')", s -> Log.e("libin", s)));
            } else {
                mWebView.post(() -> mWebView.loadUrl("javascript:" + callBack + "('" + value + "')"));
            }
        });

    }

    @Override
    public void setTitleBar(String btnBackType, String title, String textColor, String bgColor, String isDisplay) {
        runOnUiThread(() -> {
            try {
                if (isDisplay.equals("0")) {
                    mBinding.layoutTitle.setVisibility(View.GONE);
                    return;
                } else {
                    mBinding.layoutTitle.setVisibility(View.VISIBLE);
                }
                mBinding.imgBtnBack.setVisibility(View.VISIBLE);
                switch (btnBackType) {
                    case "0":
                        mBinding.imgBtnBack.setImageResource(R.mipmap.btn_back_black);
                        break;
                    case "1":
                        mBinding.imgBtnBack.setImageResource(R.mipmap.btn_back_white);
                        break;
                    case "2":
                        mBinding.imgBtnBack.setImageResource(R.mipmap.btn_back_gray);
                        break;
                    default:
                        mBinding.imgBtnBack.setVisibility(View.GONE);
                        break;
                }
                mBinding.textTitle.setText(title);
                mBinding.textTitle.setTextColor(FormatUtils.getInstance().isEmpty(textColor) ? getResources().getColor(R.color.colorPrimary) : Color.parseColor(textColor));
                mBinding.layoutTitle.setBackgroundColor(FormatUtils.getInstance().isEmpty(bgColor) ? getResources().getColor(R.color.colorPrimary) : Color.parseColor(bgColor));
            } catch (Exception e) {

            }
        });
    }


    /**
     * 修改按钮样式
     */
    @Override
    public void setBtnStyle(String btnColor, String strokeColor) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //分享按钮样式
                GradientDrawable drawableLeft = new GradientDrawable();
                drawableLeft.setShape(GradientDrawable.OVAL);
                drawableLeft.setColor(Color.parseColor(btnColor));
                drawableLeft.setSize(ViewUnits.getInstance().dp2px(CardContentActivity.this, 4),
                        ViewUnits.getInstance().dp2px(CardContentActivity.this, 4));

                GradientDrawable drawableMiddle = new GradientDrawable();
                drawableMiddle.setShape(GradientDrawable.OVAL);
                drawableMiddle.setColor(Color.parseColor(btnColor));
                drawableMiddle.setSize(ViewUnits.getInstance().dp2px(CardContentActivity.this, 7),
                        ViewUnits.getInstance().dp2px(CardContentActivity.this, 7));


                GradientDrawable drawableRight = new GradientDrawable();
                drawableRight.setShape(GradientDrawable.OVAL);
                drawableRight.setColor(Color.parseColor(btnColor));
                drawableRight.setSize(ViewUnits.getInstance().dp2px(CardContentActivity.this, 4),
                        ViewUnits.getInstance().dp2px(CardContentActivity.this, 4));


                LayerDrawable ld = new LayerDrawable(new Drawable[]{drawableLeft, drawableMiddle, drawableRight});

                ld.setLayerInset(0, 0, ViewUnits.getInstance().dp2px(CardContentActivity.this, 6.5f),
                        ViewUnits.getInstance().dp2px(CardContentActivity.this, 13),
                        ViewUnits.getInstance().dp2px(CardContentActivity.this, 6.5f));


                ld.setLayerInset(1, ViewUnits.getInstance().dp2px(CardContentActivity.this, 5),
                        ViewUnits.getInstance().dp2px(CardContentActivity.this, 5),
                        ViewUnits.getInstance().dp2px(CardContentActivity.this, 5),
                        ViewUnits.getInstance().dp2px(CardContentActivity.this, 5));


                ld.setLayerInset(2, ViewUnits.getInstance().dp2px(CardContentActivity.this, 13),
                        ViewUnits.getInstance().dp2px(CardContentActivity.this, 6.5f),
                        0, ViewUnits.getInstance().dp2px(CardContentActivity.this, 6.5f));

                mBinding.ivShareCard.setImageDrawable(ld);


                //关闭按钮样式
                GradientDrawable drawableOut = new GradientDrawable();
                drawableOut.setShape(GradientDrawable.OVAL);
                drawableOut.setStroke(ViewUnits.getInstance().dp2px(CardContentActivity.this, 2), Color.parseColor(btnColor));
                drawableOut.setSize(ViewUnits.getInstance().dp2px(CardContentActivity.this, 17),
                        ViewUnits.getInstance().dp2px(CardContentActivity.this, 17));


                GradientDrawable drawableCenter = new GradientDrawable();
                drawableCenter.setShape(GradientDrawable.OVAL);
                drawableCenter.setColor(Color.parseColor(btnColor));
                drawableCenter.setSize(ViewUnits.getInstance().dp2px(CardContentActivity.this, 6),
                        ViewUnits.getInstance().dp2px(CardContentActivity.this, 6));


                LayerDrawable ld1 = new LayerDrawable(new Drawable[]{drawableOut, drawableCenter});

                ld1.setLayerInset(0, 0, 0, 0, 0);

                ld1.setLayerInset(1, ViewUnits.getInstance().dp2px(CardContentActivity.this, 5.5f),
                        ViewUnits.getInstance().dp2px(CardContentActivity.this, 5.5f),
                        ViewUnits.getInstance().dp2px(CardContentActivity.this, 5.5f),
                        ViewUnits.getInstance().dp2px(CardContentActivity.this, 5.5f));

                mBinding.ivCloseCard.setImageDrawable(ld1);

                //背景
                GradientDrawable gd = new GradientDrawable();
                gd.setCornerRadius(ViewUnits.getInstance().dp2px(CardContentActivity.this, 14.5f));
                gd.setStroke(ViewUnits.getInstance().dp2px(CardContentActivity.this, 0.5f), Color.parseColor(strokeColor));
                mBinding.btnDelete.setBackground(gd);

                mBinding.viewLine.setBackgroundColor(Color.parseColor(strokeColor));

            }
        });
    }

    @Override
    protected void onDestroy() {

        isConnect = false;

        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }

        //断开蓝牙（新版本）
        if (bleCommMethod != null && bleCommMethod.bleGetOperator() == BleCommStatus.OPER_TRAN) {
            LogUtils.log("开始断开蓝牙。。。。");
            disconnectOnclick = true;
            bleCommMethod.bleDisConnect();
        }


        mCardPrenInter = null;
        mCardItem = null;
        mDialogBottom = null;
        mListener = null;
        mFilter = null;
        mAnimationDrawable = null;
        mJumpUrl = null;
        isFirstLoaded = true;
        unregisterReceiver(mBroadcastReceiver);

        //结束巡逻
        androidtoJS.endPatrol();
        //结束陀螺仪
        androidtoJS.stopGyroscope();
        //断开蓝牙（老版本）
        androidtoJS.disconnectBluetooth();
        //结束加速器
        androidtoJS.stopAccelerate();
        //销毁webview
        destroyWebView();
        //清除图片选择器缓存
        PictureFileUtils.deleteCacheDirFile(this);

        super.onDestroy();

    }

    /**
     * 销毁webview
     */
    public void destroyWebView() {
        if (mWebView != null) {

            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
            }
            mWebView.stopLoading();
            mWebView.clearCache(true);
            mWebView.clearHistory();
            mWebView.removeAllViews();
            mWebView.destroy();
        }


    }

    /**
     * 关闭卡片
     */
    public void closeCard() {
        if (mJumpType == Request.StartActivityRspCode.NORMAL_CARDCONTENT_JUMP) {
            mInstance = null;
            finish();
            return;
        }

        if (mJumpType == Request.StartActivityRspCode.SLPASH_CARDCONTENT_JUMP) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            mInstance = null;
            finish();
            return;
        }

        if (BaseUnits.getInstance().isForeground("tendency.hz.zhihuijiayuan.MainActivity", this)) {
            mInstance = null;
            finish();
        } else {
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            mInstance = null;
            finish();
        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Request.Broadcast.RELOADURL)) {
                String url = intent.getStringExtra("url");
                mWebView.loadUrl(url);
            }
        }
    };

    private void startAnimation() {
        if (mAnimationDrawable != null && !mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
            //图片压缩成功
            String img = PictureSelector.obtainMultipleResult(data).get(0).getCompressPath();
            if (!TextUtils.isEmpty(img) && mListener != null) {
                mListener.getImage(Base64Utils.encode(ImageUtils.getInstance().image2byte(img)));
            }

        } else if (resultCode == RESULT_OK && requestCode == Request.StartActivityRspCode.SCAN_ID_CARD) {

            try {
                IDCardBean idCardBean = (IDCardBean) data.getSerializableExtra("id_card");
                if (idCardBean != null) {
                    JSONObject jsonObject = new JSONObject();
                    JSONObject value = new JSONObject();
                    if (idCardBean.getOrientation() == 1) {
                        value.put("name", idCardBean.getName());
                        value.put("gender", idCardBean.getGender());
                        value.put("address", idCardBean.getAddress());
                        value.put("IDNum", idCardBean.getNumber());
                        value.put("nation", idCardBean.getNation());
                    } else {
                        value.put("issue", idCardBean.getPolice());
                        value.put("valid", idCardBean.getDate());
                    }

                    jsonObject.put("status", "200");
                    jsonObject.put("msg", "success");
                    jsonObject.put("data", value);

                    callBackResult(idCardBean.getCallback(), jsonObject.toString());
                }

            } catch (JSONException e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
            }
        } else {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CONTACTS_PERMISSIONS && !BaseUnits.getInstance().checkPermission(
                CardContentActivity.this, Manifest.permission.READ_CONTACTS)) {
            AlertDialog alertDialog = new AlertDialog.Builder(CardContentActivity.this)
                    .setTitle("提示")
                    .setMessage("未获得读取通讯录权限，无法正常使用APP，请前往安全中心>权限管理>智慧家园，开启相关权限")
                    .setIcon(R.mipmap.logo)
                    .setPositiveButton("确定", (dialogInterface, i1) -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();
        }

        if (requestCode == REQUEST_RECORD_PERMISSIONS && !BaseUnits.getInstance().checkPermission(
                CardContentActivity.this, Manifest.permission.RECORD_AUDIO)) {
            AlertDialog alertDialog = new AlertDialog.Builder(CardContentActivity.this)
                    .setTitle("提示")
                    .setMessage("未获得录音权限，无法正常使用APP，请前往安全中心>权限管理>智慧家园，开启相关权限")
                    .setIcon(R.mipmap.logo)
                    .setPositiveButton("确定", (dialogInterface, i1) -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();
        }

    }

    /**
     * 禁止横竖屏切换
     *
     * @param requestedOrientation
     */
    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        return;
    }

    /**
     * 在当前页面，加载新的卡片URL
     *
     * @param url
     */
    public void loadingNewCard(String url) {
        this.runOnUiThread(() -> mWebView.loadUrl(url));
    }

    /**
     * @param cardId 关闭卡片并且打开新的卡片
     */
    public void openNewCard(String cardId) {
        mCardPrenInter.previewCard(NetCode.Card2.previewCard, cardId);
    }


    /**
     * @param cardId 打开新的卡片
     */
    public void openOtherCard(String cardId) {
        mCardPrenInter.previewCard(NetCode.Card.openOtherCard, cardId);
    }


    /**
     * 连接蓝牙
     */
    public void connectBlueTooth(String connectCallback, String notifyCallback, String blueToothName, String blueToothAddress) {

//        com.apkfuns.logutils.LogUtils.getLogConfig()
//                .configAllowLog(true)  // 是否在Logcat显示日志
//                .configShowBorders(false) // 是否显示边框
//                .configLevel(LogLevel.TYPE_VERBOSE); // 配置可展示日志等级
//
//        // 支持输入日志到文件
//        String filePath = getExternalFilesDir(null) + "/LogUtils/logs/";
//        com.apkfuns.logutils.LogUtils.getLog2FileConfig() onStartSuccess send message requirement success
//                .configLog2FileEnable(true)  // 是否输出日志到文件
//                .configLogFileEngine(new LogFileEngineFactory(this)) // 日志文件引擎实现
//                .configLog2FilePath(filePath)  // 日志路径
//                .configLog2FileNameFormat("app-%d{yyyyMMddhhmm}.txt") // 日志文件名称
//                .configLog2FileLevel(LogLevel.TYPE_VERBOSE) // 文件日志等级
//                .configLogFileFilter(new LogFileFilter() {  // 文件日志过滤
//                    @Override
//                    public boolean accept(int level, String tag, String logContent) {
//                        return true;
//                    }
//                });

        this.connectCallback = connectCallback;
        this.notifyCallback = notifyCallback;
        this.blueToothName = blueToothName;
        this.blueToothAddress = blueToothAddress;

        bleService = BluetoothUtils.getInstance().getBleService();
        if (bleService == null) {
            LogUtils.log("卡片绑定服务");
            Intent intent = new Intent(this, BleService.class);
            bindService(intent, connection, BIND_AUTO_CREATE);
            startTimer();
        } else {

            LogUtils.log("主页绑定服务");
            bleService.setOnBleCommProgressListener(onServiceProgressListener);

            if (bleCommMethod.bleGetOperator() == BleCommStatus.OPER_TRAN) {
                sendCallback(connectCallback, "200", "success", "连接成功");
            } else {

                isConnect = false;
                disconnectOnclick = false;
                LogUtils.log("扫描：" + this.blueToothName);
                bleCommMethod.bleRestart(this.blueToothName);
                startTimer();
            }
        }

        IntentFilter statusFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mStatusReceive, statusFilter);

    }


    /**
     * 定时，超过25s未连接成功提示连接超时
     */
    private void startTimer() {
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(25000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (bleCommMethod != null && bleCommMethod.bleGetOperator() == BleCommStatus.OPER_TRAN) {
                        countDownTimer.cancel();
                        countDownTimer = null;
                    }
                }

                @Override
                public void onFinish() {
                    LogUtils.log("超时。。。");
                    if (bleCommMethod.bleGetOperator() != BleCommStatus.OPER_TRAN && countDownTimer != null) {
                        countDownTimer.cancel();
                        countDownTimer = null;

                        sendCallback(connectCallback, "500", "success", "连接超时");
                    }
                }
            }.start();
        }
    }


    /**
     * 蓝牙发送
     */
    public void sendData(String callback, String data) {
        this.sendCallback = callback;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter.isEnabled()) {
            if (bleCommMethod != null) {
                byte ble_oper = bleCommMethod.bleGetOperator();
                Log.i(TAG, "bleGetOperator :" + ble_oper);
                if (ble_oper == BleCommStatus.OPER_TRAN) {
                    if (data.length() > 32) {
                        ViewUnits.getInstance().showToast("不得超过32个字符");
                        return;
                    }
                    byte[] bytes = FormatUtils.getInstance().hexStringToBytes(data);
                    bleCommMethod.bleSendMessage(bytes, (byte) bytes.length);
                } else {

                    sendCallback(sendCallback, "500", "success", "蓝牙未建立连接");
                }
            } else {
                sendCallback(sendCallback, "500", "success", "蓝牙未建立连接");
            }

        } else {
            sendCallback(sendCallback, "500", "success", "蓝牙未建立连接");
        }

    }

    /**
     * @param callback
     * @param status
     * @param msg
     * @param value
     */
    private void sendCallback(String callback, String status, String msg, String value) {

        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject data = new JSONObject();
            data.put("value", value);
            jsonObject.put("status", status);
            jsonObject.put("msg", msg);
            jsonObject.put("data", data);
            LogUtils.log(jsonObject.toString());
            callBackResult(callback, jsonObject.toString());
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
            callBackResult(callback, "未知错误，联系管理员");
            e.printStackTrace();
        }
    }

    /**
     * 蓝牙断开
     */
    public void disConnectBluetooth(String disconnectCallback) {
        this.disconnectCallback = disconnectCallback;
        if (bleCommMethod != null && bleCommMethod.bleGetOperator() == BleCommStatus.OPER_TRAN) {
            LogUtils.log("手动断开");
            disconnectOnclick = true;
            bleCommMethod.bleDisConnect();
        } else {
            sendCallback(disconnectCallback, "200", "success", "手动断开成功1");
        }
    }

    private BroadcastReceiver mStatusReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
            if (blueState == BluetoothAdapter.STATE_ON) {
                reOpenBluetooth();
            }
        }
    };

    /**
     * 重新开启蓝牙，开始扫描
     */
    private void reOpenBluetooth() {
        if (!disconnectOnclick && this.blueToothName != null) {
            LogUtils.log("重新开启蓝牙");
            isConnect = false;
            bleCommMethod.bleRestart(this.blueToothName);
            startTimer();
        }
    }


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onBindService");
            bleService = ((BleService.MyBinder) service).getService();
            bleService.setOnBleCommProgressListener(onServiceProgressListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "service disconnected.");
        }

    };

    OnBleCommProgressListener onServiceProgressListener = new OnBleCommProgressListener() {
        @Override
        public void onScanDevice(String device_addr, String device_name, byte adv_flag) {
            Log.d(TAG, device_addr + ' ' + device_name + "," + isConnect + "," + disconnectOnclick);
            if (blueToothAddress.toUpperCase().equals(device_addr.toUpperCase()) && blueToothName.equals(device_name)
                    && !isConnect && !disconnectOnclick && bleCommMethod.bleGetOperator() != BleCommStatus.OPER_TRAN) {
                LogUtils.log("开始连接");
                isConnect = true;
                bleCommMethod.bleStartConnect(device_addr, new byte[]{1, 2, 3, 4, 5, 6}, 5000); /* 5000毫秒之後,连接超时*/
            }
        }

        @Override
        public void onConnection(String device_addr, int errorCode) {
            LogUtils.log("onConnection" + errorCode);
            if (errorCode == BleCommStatus.BLE_ERROR_OK) {
                sendCallback(connectCallback, "200", "success", "连接成功");
                disconnectOnclick = false;
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
            } else if (errorCode == BleCommStatus.BLE_ERROR_CONNECTION_TIMEOUT) {
                LogUtils.log("超时重连");
                isConnect = false;
                disconnectOnclick = false;
            }
        }

        @Override
        public void onDisConnection(String device_address, int errorCode) {
            LogUtils.log("onDisConnection:" + errorCode + "," + bleCommMethod.bleGetOperator());

            if (disconnectOnclick) {
                sendCallback(disconnectCallback, "200", "success", "手动断开蓝牙2");
            } else {
                isConnect = false;
                if (connectCallback != null) {
                    sendCallback(connectCallback, "500", "success", "蓝牙断开1");
                }
            }
        }

        @Override
        public void onReceive(byte[] dat, int len) {
            if (len > 0) {
                String data = FormatUtils.getInstance().bytes2Hex(dat);
                LogUtils.log("onReceive:" + data);
                if (data.length() > 10) {
                    sendCallback(notifyCallback, "200", "success", data);
                }
            }
        }

        @Override
        public void onSendSta(int code) {
            Log.i(TAG, "onSendSta index= " + code);
            sendCallback(sendCallback, "200", "success", "写入成功");
        }

        @Override
        public void onServiceOpen() {
            Log.i(TAG, "onServiceOpen");
            bleCommMethod = bleService.getBleCommMethod();
            bleCommMethod.bleOpen(blueToothName);
        }

        @Override
        public void onStartSuccess(byte oper) {

            switch (oper) {
                case BleCommStatus.OPER_ADV:
                    Log.i(TAG, "onStartSuccess bleStartAdvertisementSimulate success");
                    break;
                case BleCommStatus.OPER_CON_REQ:
                    Log.i(TAG, "onStartSuccess bleStartConnect success");
                    break;
                case BleCommStatus.OPER_DISCON_REQ:
                    Log.i(TAG, "onStartSuccess bleDisConnect success");
                    break;

                case BleCommStatus.OPER_TRAN:
                    Log.i(TAG, "onStartSuccess send message requirement success");
                    break;

                case BleCommStatus.OPER_OPEN:
                    Log.i(TAG, "onStartSuccess ble communication session open success");
                    break;

                case BleCommStatus.OPER_CLOSE:
                    Log.i(TAG, "onStartSuccess ble communication session close success");
                    break;

                default:
                    Log.i(TAG, "onStartSuccess reserve operation  " + oper);
                    break;
            }
        }

        @Override
        public void onStartFailure(byte oper, int errorCode) {
            String strErrorMsg = "";
            switch (oper) {
                case BleCommStatus.OPER_ADV:
                    if (errorCode == BleCommStatus.BLE_ERROR_INVALID_PARAMETER) { // 1
                        Log.w(TAG, "onStartFailure bleStartAdvertisementSimulate parameter fail");
                        strErrorMsg = "onStartFailure bleStartAdvertisementSimulate parameter fail";
                    } else if (errorCode == BleCommStatus.BLE_ERROR_INVALID_OPERATION) {
                        Log.w(TAG, "onStartFailure bleStartAdvertisementSimulate operation error");
                    }
                    strErrorMsg = "onStartFailure bleStartAdvertisementSimulate operation error";
                    break;
                case BleCommStatus.OPER_CON_REQ:
                    if (errorCode == BleCommStatus.BLE_ERROR_INVALID_PARAMETER) { // 1
                        Log.w(TAG, "onStartFailure bleStartConnect parameter incorrect");
                        strErrorMsg = "onStartFailure bleStartConnect parameter incorrect";
                    } else if (errorCode == BleCommStatus.BLE_ERROR_INVALID_OPERATION) {
                        Log.w(TAG, "onStartFailure bleStartConnect operation error");
                        strErrorMsg = "onStartFailure bleStartConnect operation error";
                    }
                    break;
                case BleCommStatus.OPER_DISCON_REQ:
                    if (errorCode == BleCommStatus.BLE_ERROR_INVALID_PARAMETER) { // 1
                        Log.w(TAG, "onStartFailure bleDisConnect parameter incorrect");
                        strErrorMsg = "onStartFailure bleDisConnect parameter incorrect";
                    } else if (errorCode == BleCommStatus.BLE_ERROR_INVALID_OPERATION) {
                        Log.w(TAG, "onStartFailure bleDisConnect operation error");
                        strErrorMsg = "onStartFailure bleDisConnect operation error";
                    }

                    break;

                case BleCommStatus.OPER_TRAN:
                    if (errorCode == BleCommStatus.BLE_ERROR_INVALID_PARAMETER) { // 1
                        Log.w(TAG, "onStartFailure bleSendMessage parameter incorrect");
                        strErrorMsg = "onStartFailure bleSendMessage parameter incorrect";
                    } else if (errorCode == BleCommStatus.BLE_ERROR_INVALID_OPERATION) {
                        Log.w(TAG, "onStartFailure ble connection interval operation error");
                        strErrorMsg = "onStartFailure ble connection interval operation error";
                    }
                    break;

                default:
                    Log.w(TAG, "onStartFailure reserve operation  " + oper + ' ' + errorCode);
                    strErrorMsg = "onStartFailure reserve operation  " + oper + ' ' + errorCode;
                    break;
            }
        }

        @Override
        public void onStateChange(int i) {
            if (i == BluetoothAdapter.STATE_ON) {
                reOpenBluetooth();
            }
        }

    };

}
