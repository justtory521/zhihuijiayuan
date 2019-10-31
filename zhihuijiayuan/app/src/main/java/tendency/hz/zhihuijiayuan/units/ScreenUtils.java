package tendency.hz.zhihuijiayuan.units;

import android.content.Context;
import android.util.DisplayMetrics;

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
    public static int getScreenWidth(Context context) {

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenHeight = dm.heightPixels;
        return screenHeight;
    }
}
