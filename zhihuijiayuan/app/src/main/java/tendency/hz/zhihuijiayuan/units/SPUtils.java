package tendency.hz.zhihuijiayuan.units;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import tendency.hz.zhihuijiayuan.application.MyApplication;

/**
 * Created by JasonYao on 2018/2/27.
 */

public class SPUtils {
    private static final String TAG = "SPUtils---";
    private static SPUtils mIntance = null;  //单例对象

    //定义文件名
    public static final String FILE_CONFIG = "config";

    //用户相关
    public static final String FILE_USER = "user";

    //消息相关
    public static final String FILE_MESSAGE = "message";

    //card相关
    public static final String FILE_CARD = "card";


    //记录数据库是否拷贝完成
    public static final String DBCOPY = "dbcopy";
    public static final String addressCopy = "addressStatus"; //地址数据库拷贝状态标记
    public static final String dataCopy = "dataStatus"; //基础数据库拷贝状态标记

    public static final String FILE_LOCATION = "location"; //记录当前定位城市

    //config中的字段
    public static final String analogImei = "imei"; //模拟IMEI号
    public static final String isFirstInstall = "isFirstInstall"; //标记是否第一次安装
    public static final String adImg = "img";  //广告Img
    public static final String adUrl = "web"; //广告Url
    public static final String sVersionCode = "SVersionCode"; //当前缓存的APP版本号

    //message中的字段
    public static final String messageNum = "messageNum"; //消息条数

    //卡号
    public static final String cardNo = "cardNO";
    //智慧家园邀请码
    public static final String invitationCode = "invitationCode";

    //user中的字段
    public static final String account = "account"; //用户名
    public static final String token = "token"; //Token
    public static final String accountId = "AccountID"; //用户ID
    public static final String phone = "Phone"; //用户手机号
    public static final String email = "Email"; //用户邮箱
    public static final String accountName = "AccountName"; //用户名
    public static final String cardId = "CardID"; //身份证
    public static final String realName = "RealName"; //真实姓名
    public static final String status = "Status"; //审核状态
    public static final String confirmStatusName = "ConfirmStatusName";  //审核状态描述
    public static final String autherizeName = "AutherizeName"; //认证状态描述
    public static final String headImgPath = "HeadImgPath"; //头像
    public static final String nickName = "NickName"; //昵称
    public static final String sex = "Sex";
    public static final String birthDay = "Birthday";
    public static final String address = "Address";

    //location中的字段
    public static final String location = "Location"; //当前定位城市
    public static final String selectCity = "selectCity";  //当前选择城市
    public static final String notifyTime = "notifyTime";  //提示切换城市时间

    private static Context context = MyApplication.getInstance().getBaseContext();

    private SPUtils() {
    }  //私有化构造方法

    /**
     * 获取单例方式
     *
     * @return
     */
    public static SPUtils getInstance() {
        if (mIntance == null) {
            synchronized (SPUtils.class) {
                if (mIntance == null) {
                    mIntance = new SPUtils();
                }
            }
        }

        return mIntance;
    }

    /**
     * 向指定文件插入值
     *
     * @param fileName
     * @param key
     * @param object
     */
    public void put(String fileName, String key, Object object) {
        if (object == null) {
            Log.e(TAG, "跳出");
            return;
        }

        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public void remove(String filename, String key) {
        SharedPreferences sp = context.getSharedPreferences(filename,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.clear();
        editor.commit();
    }

    /**
     * 清除所有数据
     */
    public void clear(String filename) {
        SharedPreferences sp = context.getSharedPreferences(filename,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    public boolean contains(String filename, String key) {
        SharedPreferences sp = context.getSharedPreferences(filename,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @return
     */
    public Map<String, ?> getAll(String filename) {
        SharedPreferences sp = context.getSharedPreferences(filename,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 得到指定文件中的key对应的value，如果没有则返回传递的默认值
     *
     * @param filename
     * @param key
     * @param defaultObject
     * @return
     */
    public Object get(String filename, String key, Object defaultObject) {

        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getFloat(key, (Long) defaultObject);
        }

        return null;
    }


    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     * ，，，我感觉最后使用的都是apply方法进行提交修改的
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                //捕捉到异常但是没有做处理，是因为这个一定不会产生异常？？？应该是这样的
            }
            return null;
        }

        /**
         * @param editor SharedPreferences编辑器
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }
}
