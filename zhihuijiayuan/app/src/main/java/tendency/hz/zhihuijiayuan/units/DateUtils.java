package tendency.hz.zhihuijiayuan.units;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Author：Libin on 2019/5/14 11:49
 * Email：1993911441@qq.com
 * Describe：时间戳转换
 */
public class DateUtils {

    /**
     * @param type
     * @param time
     * @return 获取日期
     */
    public static String getDate(String type, long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(type, Locale.CHINA);

        return sdf.format(new Date(time));
    }

    /**
     * @param time
     * @return 字符串转时间戳毫秒
     */
    public static long getTime(String type, String time) {

        try {
            Calendar c = Calendar.getInstance();
            c.setTime(new SimpleDateFormat(type).parse(time));
            return c.getTimeInMillis();
        } catch (ParseException e) {

        }
        return 0;
    }

    /**
     * @return 时间格式转换
     */
    public static String formatTime(String type, String formatType, String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }

        String format = time.replaceAll("T", " ");
        long time1 = getTime(type, format);
        return getDate(formatType, time1);
    }


    /**
     * @return 获取上月
     */
    public static int getLastMonth() {

        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH);
    }


    /**
     * 获取年份
     *
     * @return
     */
    public static int getYear() {

        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }
}
