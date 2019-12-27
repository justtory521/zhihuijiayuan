package tendency.hz.zhihuijiayuan.units;

import android.content.Context;
import android.util.DisplayMetrics;

import tendency.hz.zhihuijiayuan.application.MyApplication;

/**
 * 屏幕相关工具类
 *
 * Created by JasonYao on 2017/7/27.
 */

public class ScreenUtils {
    private ScreenUtils() {
        throw new AssertionError();
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth() {

        DisplayMetrics dm = MyApplication.getInstance().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight() {
        DisplayMetrics dm = MyApplication.getInstance().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }
}
