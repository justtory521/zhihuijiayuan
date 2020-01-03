package tendency.hz.zhihuijiayuan.units;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.cjt2325.cameralibrary.util.LogUtil;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.application.MyApplication;
import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.view.service.UpdateService;
import tendency.hz.zhihuijiayuan.widget.CommomDialog;

/**
 * 基础工具类
 * Created by JasonYao on 2018/2/26.
 */

public class BaseUnits {
    public static BaseUnits mInstance;

    /**
     * 私有化构造方法
     */
    private BaseUnits() {
    }

    /**
     * 设计单例获取方式
     */
    public static BaseUnits getInstance() {
        if (mInstance == null) {
            synchronized (BaseUnits.class) {
                if (mInstance == null) {

                    mInstance = new BaseUnits();
                }
            }
        }

        return mInstance;
    }

    /**
     * 获取手机IMEI号（15位）
     *
     * @return
     */
    public String getPhoneKey() {
        String szImei = null;
        if (!isEmpty(ConfigUnits.getInstance().getPhoneAnalogIMEI())) {  //如果已经缓存手机唯一标识码，优先返回
            return ConfigUnits.getInstance().getPhoneAnalogIMEI();
        }

        szImei = getSerilNumByLength(20);
        ConfigUnits.getInstance().setPhoneAnalogIMEI(szImei);
        return szImei;
    }

    /**
     * 获取指定位数的唯一序列号
     *
     * @param length
     * @return
     */
    public String getSerilNumByLength(int length) {
        if (length < 20) {
            length = 20;
        }
        String serlNum = "";
        int random = (int) (Math.random() * 9) + 10;
        serlNum = String.valueOf(Math.abs(UUID.randomUUID().toString()
                .hashCode()));
        String str = "0000000000";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String nowStr = sdf.format(new Date());
        int len = length - nowStr.length() - (random + "").length();
        if (serlNum.length() > len) {
            serlNum = serlNum.substring(0, len);
        } else if (serlNum.length() < len) {
            serlNum = serlNum + str.substring(serlNum.length());
        }
        serlNum = nowStr + random + serlNum;
        return serlNum;
    }

    /**
     * 判断文本是否为空
     *
     * @param content
     * @return true：为空 false：不为空
     */
    public boolean isEmpty(String content) {
        if (content == null) {
            return true;
        }

        if (content.trim() == null || content.trim().equals("")) {
            return true;
        }

        return false;
    }

    /**
     * 去掉字符串中的重复字符
     *
     * @param content
     * @return
     */
    public String removeSameString(String content) {
        List list = new ArrayList();
        StringBuffer sb = new StringBuffer(content);
        int j = 0;
        for (int i = 0; i < content.length(); i++) {
            if (list.contains(content.charAt(i))) {
                sb.deleteCharAt(i - j);
                j++;  //因为删除了Sb中的字符，有一个偏移
            } else {
                list.add(content.charAt(i));
            }
        }

        return sb.toString();
    }

    /**
     * 检查特定权限，是否
     *
     * @param context
     * @param permission
     * @return
     */
    public boolean checkPermission(Context context, String permission) {
        //没有权限
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;

    }

    /**
     * 检查特定权限
     *
     * @param context
     * @param permission
     * @return
     */
    public boolean checkPermission(Context context, String[] permission) {
        for (String permiss : permission) {
            if (!checkPermission(context, permiss)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断手机是否安装该APP
     *
     * @param context
     * @param packageName APP包名
     * @return
     */
    public boolean isApkInstalled(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }


        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 下载APP
     */
    public void loadApk(Context context, String url) {
        new CommomDialog(context, R.style.dialog, "手机未安装该APP，是否下载？", new CommomDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {  //点击确定
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Request.Permissions.REQUEST_WRITE_EXTERNAL_STORAGE);
                        ViewUnits.getInstance().showToast("请允许权限进行下载安装");
                    } else {
                        dialog.dismiss();
                        ViewUnits.getInstance().showToast("开始下载");
                        Intent intent = new Intent(context, UpdateService.class);
                        intent.putExtra("downloadurl", url);
                        context.startService(intent);
                    }
                } else {  //点击取消
                    dialog.dismiss();
                }
            }
        }).setTitle("提示").show();
    }

    /**
     * 启动应用卡
     *
     * @param context
     * @param packageName
     */
    public void openAppCard(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public String getVerCode(Context context) {
        String verName = "";
        try {
            verName = String.valueOf(context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public String getPhoneModel() {
        return Build.MODEL;
    }

    /**
     * 获取当前安卓系统版本号
     *
     * @return
     */
    public String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取当前应用程序的包名
     *
     * @param context 上下文对象
     * @return 返回包名
     */
    public String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }

    public boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    /**
     * 判断某个Activity是否正在运行
     * @param activityClassName
     * @return
     */
    public boolean isActivityRunning(String activityClassName) {
        ActivityManager activityManager = (ActivityManager) MyApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = activityManager.getRunningTasks(1);
        if (info != null && info.size() > 0) {
            ComponentName component = info.get(0).topActivity;
            return activityClassName.equals(component.getClassName());
        }
        return false;
    }

    /**
     * 判断某个Activity是否已经启动
     *
     * @param PackageName
     * @param context
     * @return
     */
    public boolean isForeground(String PackageName, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> task = manager.getRunningTasks(1);

        ComponentName componentInfo = task.get(0).baseActivity;

        return componentInfo.getClassName().equals(PackageName);

    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = MyApplication.getInstance().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = MyApplication.getInstance().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int[] getScreenSize(Context context) {
        int[] size = new int[2];

        WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
            }
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17)
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            } catch (Exception ignored) {
            }
        size[0] = widthPixels;
        size[1] = heightPixels;
        return size;
    }


    /*public static int getStatusBarHeight() {
        //如果小米手机开启了全面屏手势隐藏了导航栏则返回 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (Settings.Global.getInt(MyApplication.getInstance().getContentResolver(),
                    "force_fsg_nav_bar", 0) != 0) {
                return 0;
            }
        }
        int realHeight = getScreenSize(MyApplication.getInstance())[1];

        Display d = ((WindowManager) MyApplication.getInstance().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;

        return realHeight - displayHeight;
    }*/

    /**
     * Activity跳转
     *
     * @param context
     */
    public void jumpActivity(Class<?> context) {
        Intent intent = new Intent();
        intent.setClass(MyApplication.getInstance().getBaseContext(), context);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getInstance().startActivity(intent);
    }

    public Bitmap base64ToBitmap(String base64String) {

        byte[] decode = Base64.decode(base64String, Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(decode, 0, decode.length);
    }

    /**
     * 获取当前手机号
     *
     * @return
     */
    public String getTel() {
        if (ActivityCompat.checkSelfPermission(MyApplication.getInstance(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MyApplication.getInstance(), Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MyApplication.getInstance(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            TelephonyManager tm = (TelephonyManager) MyApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getLine1Number();
        }
        return "";
    }

    /**
     * 检查旧版（包名为io.cordova.hellocordova）智慧家园，是否安装，如果是的话，执行删除
     */
    public void checkOldPackage(Context context) {
        if (isApkInstalled(MyApplication.getInstance(), "io.cordova.hellocordova")) {
            new AlertDialog.Builder(context)
                    .setTitle("提示")
                    .setMessage("此版本为官方版,建议删除此前安装的试用版本")
                    .setIcon(R.mipmap.logo)
                    .setPositiveButton("删除", (dialogInterface, i) -> {
                        Uri uri = Uri.fromParts("package", "io.cordova.hellocordova", null);
                        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
                        context.startActivity(intent);
                    }).create().show();

        }
    }

    public  String getIpAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            // 3/4g网络
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                //  wifi网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                return intIP2StringIP(wifiInfo.getIpAddress());
            } else if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                // 有限网络
                return getLocalIp();
            }
        }
        return null;
    }

    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    // 获取有限网IP
    private static String getLocalIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return "0.0.0.0";

    }


    /**
     * 根据IP地址获取MAC地址
     *
     * @return
     */
    public String getMac() {
        String mac_s = "";
        StringBuilder buf = new StringBuilder();
        try {
            byte[] mac;
            NetworkInterface ne = NetworkInterface.getByInetAddress(InetAddress.getByName(getIpAddress(MyApplication.getInstance())));
            mac = ne.getHardwareAddress();
            for (byte b : mac) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            mac_s = buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mac_s;
    }

    /**
     * 获取手机IMEI
     *
     * @return
     */
    public String getIMEI() {
        try {
            //实例化TelephonyManager对象
            TelephonyManager telephonyManager = (TelephonyManager) MyApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            //获取IMEI号
            @SuppressLint("MissingPermission") String imei = telephonyManager.getDeviceId();
            //在次做个验证，也不是什么时候都能获取到的啊
            if (imei == null) {
                imei = "";
            }
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 获取手机IMSI
     */
    public String getIMSI() {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) MyApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            //获取IMSI号
            @SuppressLint("MissingPermission") String imsi = telephonyManager.getSubscriberId();
            if (null == imsi) {
                imsi = "";
            }
            return imsi;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取移动设备本地IP
     *
     * @return
     */
    public InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            //列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {//是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();//得到下一个元素
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();//得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return ip;
    }

    /**
     * 判断网络是否连接
     *
     * @return
     */
    public boolean isLinkNet() {
        Context context = MyApplication.getInstance()
                .getBaseContext();
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //说明有网络连接
        return manager.getActiveNetworkInfo() != null;
    }

    public  String getAuthorizeURL(String appId, String scene, String template_id, String redirectUri, String reserved) throws UnsupportedEncodingException {
        StringBuffer sbf = new StringBuffer();
        sbf.append("https://mp.weixin.qq.com/mp/subscribemsg?action=get_confirm").append("&appid=").append(appId)
                .append("&scene=").append(scene)
                .append("&template_id=").append(template_id)
                .append("&redirect_uri=").append(URLEncoder.encode(redirectUri, "UTF-8"));

        sbf.append("#wechat_redirect");

        return sbf.toString();
    }

    /**
     * 获取分享渠道列表
     *
     * @param activity
     * @return
     */
    public SHARE_MEDIA[] getShareList(Activity activity) {
        ArrayList<SHARE_MEDIA> shareMedia = new ArrayList<>();

        if (UMShareAPI.get(MyApplication.getInstance()).isInstall(activity, SHARE_MEDIA.WEIXIN)) {
            shareMedia.add(SHARE_MEDIA.WEIXIN);
        }

        if (UMShareAPI.get(MyApplication.getInstance()).isInstall(activity, SHARE_MEDIA.WEIXIN_CIRCLE)) {
            shareMedia.add(SHARE_MEDIA.WEIXIN_CIRCLE);
        }

        if (UMShareAPI.get(MyApplication.getInstance()).isInstall(activity, SHARE_MEDIA.SINA)) {
            shareMedia.add(SHARE_MEDIA.SINA);
        }

        if (UMShareAPI.get(MyApplication.getInstance()).isInstall(activity, SHARE_MEDIA.QQ)) {
            shareMedia.add(SHARE_MEDIA.QQ);
        }

        return shareMedia.toArray(new SHARE_MEDIA[shareMedia.size()]);
    }




}
