package tendency.hz.zhihuijiayuan.view.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;

import tendency.hz.zhihuijiayuan.units.BuglyFileProvider;

/**
 * Created by JasonYao on 2018/4/20.
 */

public class UpdateService extends Service {
    private static final String TAG = "UpdateService---";

    public UpdateService() {
    }

    /**
     * 安卓系统下载类
     **/
    private DownloadManager manager;
    /**
     * 接收下载完的广播
     **/
    private DownloadCompleteReceiver receiver;
    private String url;
    private String DOWNLOADPATH = "/download/";//下载路径，如果不定义自己的路径，6.0的手机不自动安装

    /**
     * 初始化下载器
     **/
    private void initDownManager() {
        Log.e(TAG, "开始下载");
        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        receiver = new DownloadCompleteReceiver();
        //设置下载地址
        DownloadManager.Request down = new DownloadManager.Request(Uri.parse(url));
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                | DownloadManager.Request.NETWORK_WIFI);
        down.setAllowedOverRoaming(false);
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
        down.setMimeType(mimeString);
        // 下载时，通知栏显示途中
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        // 显示下载界面
        down.setVisibleInDownloadsUi(true);
        // 设置下载后文件存放的位置
        down.setDestinationInExternalPublicDir(DOWNLOADPATH, "app-release.apk");
        down.setTitle("正在下载");
        // 将下载请求放入队列
        manager.enqueue(down);
        //注册下载广播
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        url = intent.getStringExtra("downloadurl");
        Log.e(TAG, "下载地址：" + url);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + DOWNLOADPATH + "app-release.apk";
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            // 调用下载
            initDownManager();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {
        if (receiver != null)
            // 注销下载广播
            unregisterReceiver(receiver);
        super.onDestroy();
    }

    // 接受下载完成后的intent
    class DownloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "收到广播");
            //判断是否下载完成的广播
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                Log.e(TAG, "下载广播");
                //获取下载的文件id
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (manager.getUriForDownloadedFile(downId) != null) {
                    //自动安装apk
                    installAPK(manager.getUriForDownloadedFile(downId), context);
                    //installAPK(context);
                } else {
                    Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
                }
                //停止服务并关闭广播
                UpdateService.this.stopSelf();
            }
        }

        private void installAPK(Uri apk, Context context) {
            if (Build.VERSION.SDK_INT < 23) {
                Intent intents = new Intent();
                intents.setAction(Intent.ACTION_VIEW);
//                intents.addCategory("android.intent.category.DEFAULT");
                intents.setDataAndType(apk, "application/vnd.android.package-archive");
                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intents);
            } else if (Build.VERSION.SDK_INT >= 24) {
                install(context);
            } else {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DOWNLOADPATH + "app-release.apk");
                if (file.exists()) {
                    openFile(file, context);
                } else {
                }
            }
        }

        /**
         * android7.0之后的更新
         * 通过隐式意图调用系统安装程序安装APK
         */
        public void install(Context context) {
            File file = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    , "app-release.apk");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri =
                    BuglyFileProvider.getUriForFile(context, "tendency.hz.zhihuijiayuan.fileProvider", file);
            intent.addCategory("android.intent.category.DEFAULT");
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            context.startActivity(intent);
        }

        private void installAPK(Context context) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DOWNLOADPATH + "huijuquanqiu.apk");
            if (file.exists()) {
                openFile(file, context);
            } else {
                Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * android6.0之后的升级更新
     *
     * @param file
     * @param context
     */
    public void openFile(File file, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        intent.setAction("android.intent.action.VIEW");
        String type = getMIMEType(file);
        intent.setDataAndType(Uri.fromFile(file), type);
        try {
            context.startActivity(intent);
        } catch (Exception var5) {
            var5.printStackTrace();
            Toast.makeText(context, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
        }
    }

    public String getMIMEType(File var0) {
        String var1 = "";
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }

}
