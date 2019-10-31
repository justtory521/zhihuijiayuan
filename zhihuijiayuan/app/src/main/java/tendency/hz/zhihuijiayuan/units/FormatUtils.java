package tendency.hz.zhihuijiayuan.units;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tendency.hz.zhihuijiayuan.application.MyApplication;

/**
 * Created by JasonYao on 2018/3/21.
 */

public class FormatUtils {
    //定义单例对象
    public static FormatUtils mInstance = null;
    public static StringBuffer sb = new StringBuffer();


    /**
     * 私有化构造方法
     */
    private FormatUtils() {
    }

    /**
     * 获取单例对象
     *
     * @return
     */
    public static  FormatUtils getInstance() {
        if (mInstance == null) {
            synchronized (FormatUtils.class) {
                if (mInstance == null) {
                    mInstance = new FormatUtils();
                }
            }
        }

        return mInstance;
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

        if (content.toLowerCase().equals("null")) {
            return true;
        }

        if (content.trim() == null || content.trim().equals("")) {
            return true;
        }

        return false;
    }

    /**
     * 校验是否为手机号格式
     *
     * @param content
     * @return
     */
    public boolean isPhone(String content) {
        if (isEmpty(content)) {
            return false;
        }

        String regex = "^[1]+\\d{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        return matcher.matches();
    }

    /**
     * 校验是否为邮箱格式
     *
     * @param content
     * @return
     */
    public boolean isEMail(String content) {

        if (isEmpty(content)) {
            return false;
        }

        String regex = "^([a-z0-9A-Z]+[-|\\.]?)@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        return content.matches(regex);
    }

    /**
     * 汉字转拼音
     * @param chinese
     * @return
     */
    /**
     * 获取汉字字符串的汉语拼音，英文字符不变
     */
    public String getPinYin(String chines) {
        if (chines.equals("重庆市")) {
            return "chongqing";
        }
        sb.setLength(0);
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    sb.append(PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                sb.append(nameChar[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 验证是否是手机号
     *
     * @param str
     * @return
     */
    public boolean IsHandset(String str) {
        String regex = "^[1]+[3-9]+\\d{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 验证是否是身份证号（支持15位和18位身份验证）
     *
     * @param str
     * @return
     */
    public boolean IsIDCard(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String regex = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 隐藏一定位数的身份证
     *
     * @return
     */
    public String encryptIDCardNo(String IDCardNo) {
        if (IsIDCard(IDCardNo)) {
            return IDCardNo.substring(0, 6) + "********" + IDCardNo.substring(14, IDCardNo.length());
        }

        return IDCardNo;
    }

    public String encryptPhone(String phone) {
        if (IsHandset(phone)) {
            return phone.substring(0, 3) + "****" + phone.substring(7, 11);
        }

        return phone;
    }

    /**
     * 高德地图转百度地图
     *
     * @param gd_lon
     * @param gd_lat
     * @return
     */
    public double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }

    /**
     * 3位16进制颜色值转为6位
     *
     * @param color
     * @return
     */
    public String colorTo6Color(String color) {
        if (color.length() == 7) return color;
        if (color.length() == 4) {
            return color.substring(0, 2) + color.substring(1, 2) + color.substring(2, 3) + color.substring(2, 3) + color.substring(3, 4) + color.substring(3, 4);
        }

        return color;
    }

    /**
     * @param resourceName
     * @return 根据资源文件名获取id
     */
    public int getResource(String defType, String resourceName) {
        String resource = resourceName;
        if (resourceName.contains(".")) {
            resource = resourceName.split("\\.")[0];
        }

        //如果没有在"mipmap"下找到imageName,将会返回0
        return MyApplication.getInstance().getResources().getIdentifier(resource, defType, MyApplication.getInstance().getPackageName());
    }

    /**
     * 忽略大小写判断字符串中是否包含某个字符串
     *
     * @param a
     * @param b
     * @return
     */
    public boolean uplowContains(String a, String b) {
        String A = a.toUpperCase();
        String B = b.toUpperCase();
        if (A.contains(B)) {
            return true;
        }

        return false;
    }

    public boolean isEmpty(byte[] bytes) {
        return bytes == null || bytes.length == 0;
    }

    public String byteToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        if (!isEmpty(bytes)) {
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
        }

        return sb.toString();
    }


    /**
     * @param bytes 转16进制
     * @return
     */
    public String bytes2Hex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        StringBuilder hex = new StringBuilder();

        for (byte b : bytes) {
            hex.append(HEXES[(b >> 4) & 0x0F]);
            hex.append(HEXES[b & 0x0F]);
        }

        return hex.toString().toUpperCase();
    }


    private static final char[] HEXES = {
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'
    };


    /**
     * 16进制字符串转字节数组
     */
    public byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    /**
     * @return 保留两位小数
     */
    public String decimalFormat(double value, int count) {
        StringBuilder str = new StringBuilder("0.");
        for (int i = 0; i < count; i++) {
            str.append("0");
        }
        DecimalFormat df = new DecimalFormat(str.toString());
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(value);
    }

    /**
     * @return 若为整数显示整数，若为小数保留小数点后两位
     */
    public String intFormat(double value) {
        return (int) value == value ? String.valueOf((int) value) : decimalFormat(value, 2);
    }

}
