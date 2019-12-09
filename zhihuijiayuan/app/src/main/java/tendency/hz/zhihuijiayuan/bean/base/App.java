package tendency.hz.zhihuijiayuan.bean.base;

import android.Manifest;
import android.content.Context;

import tendency.hz.zhihuijiayuan.application.MyApplication;

/**
 * Created by JasonYao on 2018/3/5.
 */

public class App {
    public static final String[] mPermissionList = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_LOGS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.SET_DEBUG_APP,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            Manifest.permission.WRITE_APN_SETTINGS,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };

    public static final String[] pictureSelect = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    public static class Pay {
        public static final String WX_APP_ID = "wx6aae54c97cb5b49c";  //微信支付APPID
        public static final String WX_APP_SECRET = "38a4fae94954ef0343586e36391de63c"; //微信支付APPSecret
    }

    /**
     * 缓存数据库
     */
    public static class DB {
        public static final int firstVersion = 1; //第一个数据库版本号
        public static final int version = 6;  //数据库版本号
        public static final String dbName = "db_data.db";  //数据库名称
    }

    public static class BaseDB {
        public static final int version = 1; //数据库版本号
        public static final String dbName = "db_base.db"; //数据库名称
    }

    public static class AddressDB {
        public static final int version = 1; //数据库版本号
        public static final String dbName = "address.db"; //数据库名称
    }
}

