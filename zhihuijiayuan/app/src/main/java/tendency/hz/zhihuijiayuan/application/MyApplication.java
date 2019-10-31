package tendency.hz.zhihuijiayuan.application;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.v4.app.NotificationCompat;

import com.cjt2325.cameralibrary.util.LogUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
//import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.URLConnectionNetworkExecutor;
import com.yanzhenjie.nohttp.cache.DBCacheStore;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

import tendency.hz.zhihuijiayuan.MainActivity;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.base.Config;
import tendency.hz.zhihuijiayuan.units.LogUtils;
import tendency.hz.zhihuijiayuan.units.MacUtils;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.card.ChoiceCardActivity;
import tendency.hz.zhihuijiayuan.widget.ClassicsHeader;

/**
 * Created by JasonYao on 2018/2/27.
 */

public class MyApplication extends Application {
    public static MyApplication mInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        //友盟初始化
        UMConfigure.init(this, "5b56ca8eb27b0a37e5000119", Config.UM_CHANNEL, UMConfigure.DEVICE_TYPE_PHONE, null);
        //设置微信分享
        PlatformConfig.setWeixin("wx6aae54c97cb5b49c", "a90af6720dfcf1bbf45995daf3d17e7a");
        //设置新浪微博分享
        PlatformConfig.setSinaWeibo("3356450636", "44a06c1c6145cc265e784a0d560a32c0", "http://sns.whalecloud.com");
        //QQ分享
        PlatformConfig.setQQZone("101510016", "18006954c2802f7a97ae9de7ca935b80");
        //设置支付宝
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);


        //初始化NoHttp
        NoHttp.initialize(this, new NoHttp.Config()
                .setReadTimeout(30 * 1000)  //服务器响应超时时间
                .setConnectTimeout(30 * 1000)  //连接超时时间
                .setCacheStore(new DBCacheStore(this).setEnable(false))
                .setNetworkExecutor(new URLConnectionNetworkExecutor()) //使用HttpURLConnection做网络层
        );

        Logger.setTag("NoHttpSample");
        Logger.setDebug(true); //开启调试模式

        Stetho.initializeWithDefaults(this);  //初始化Chrome查看Sqlite插件
        Fresco.initialize(this);  //网络图片异步加载

        Beta.upgradeDialogLayoutId = R.layout.layout_update_popup;
        Beta.tipsDialogLayoutId = R.layout.dialog_tips;
        Beta.strNetworkTipsCancelBtn = "";
        Beta.strUpgradeDialogCancelBtn = "     ";
        Beta.initDelay = 5 * 1000;
        Beta.canShowUpgradeActs.add(MainActivity.class);
        Beta.canShowUpgradeActs.add(ChoiceCardActivity.class);
        Bugly.init(getApplicationContext(), Config.BUGLY_APPID, false);

        //极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);


        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
        builder.statusBarDrawable = R.mipmap.logo;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL//
                | Notification.FLAG_SHOW_LIGHTS; // 设置为自动消失和呼吸灯闪烁
        builder.notificationDefaults = Notification.DEFAULT_VIBRATE | // 设置为、震动
                        Notification.DEFAULT_LIGHTS; // 设置为呼吸灯闪烁
        JPushInterface.setPushNotificationBuilder(1, builder);


        //科大讯飞
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5b3d92ad");

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mBuilder.setGroupSummary(false)
                    .setGroup("group");
        }

        String strMa = MacUtils.getMobileMAC(getApplicationContext());
        if (MacUtils.ERROR_MAC_STR.equals(strMa)) {
            ViewUnits.getInstance().showToast("请开启wifi 以保证正常使用", this);
            MacUtils.getStartWifiEnabled();
        }


        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                LogUtils.log("x5内核加载"+arg0);
            }

            @Override
            public void onCoreInitFinished() {


            }
        };

        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);

        //内存泄漏
//        LeakCanary.install(this);


//        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
//
//            @Override
//            public void onActivityStopped(Activity activity) {
//                count--;
//            }
//
//            @Override
//            public void onActivityStarted(Activity activity) {
//                count++;
//            }
//
//            @Override
//            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//            }
//
//            @Override
//            public void onActivityResumed(Activity activity) {
//            }
//
//            @Override
//            public void onActivityPaused(Activity activity) {
//            }
//
//            @Override
//            public void onActivityDestroyed(Activity activity) {
//            }
//
//            @Override
//            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//            }
//        });


        this.mInstance = this;

    }

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {

                return new ClassicsHeader(context);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                ClassicsFooter classicsFooter = new ClassicsFooter(context);
                classicsFooter.setTextSizeTitle(12);

                return classicsFooter;
            }
        });

    }

    public static MyApplication getInstance() {
        return mInstance;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
