package tendency.hz.zhihuijiayuan.bean.base;

/**
 * 基础配置信息（生产环境和测试环境）
 * Created by JasonYao on 2018/9/30.
 */
public class Config {

    /**
     * 开发环境
     */
//    public static final String URL = "http://183.129.130.119:12012"; //杭州测试环境（外网地址）
//    public static final String PAY_URL = "http://183.129.130.119:34805";  //杭州支付测试接口
//    public static final String UPLOADIMG = "http://183.129.130.119:12015";  //上传头像
//
//    public static final String UM_CHANNEL = "测试环境";  //友盟渠道信息
//    public static final String BUGLY_APPID = "e73b57ee1b";  //bugly开发环境
//    public static String VERSION = "-开发版"; //开发版
//    public static String USER_AGREEMENT = "http://www.yikahui.net/AppH5/agreement/user-potocol.html"; //用户协议
//    public static String PRIVACY_STATEMENT = "http://www.yikahui.net/AppH5/agreement/privacy-statement.html"; //隐私声明


    /**
     * 生产环境
     */
//    public static final String URL = "http://api.yikahui.net";  //生产环境
//    public static final String PAY_URL = "http://openapi.yikahui.net";//杭州支付正式接口
//    public static final String UPLOADIMG = "http://file.yikahui.net";  //上传头像
//
//    public static final String UM_CHANNEL = "智慧环境";  //友盟渠道信息
//    public static final String BUGLY_APPID = "e9950e84c6";  //bugly生产环境
//    public static String VERSION = ""; //正式版
//    public static String USER_AGREEMENT = "http://www.yikahui.net/AppH5/agreement/user-potocol.html"; //用户协议
//    public static String PRIVACY_STATEMENT = "http://www.yikahui.net/AppH5/agreement/privacy-statement.html"; //隐私声明


    /**
     * 测试环境
     */
    public static final String URL = "http://183.129.130.119:13104"; //杭州测试环境（外网地址）
    public static final String PAY_URL = "http://183.129.130.119:13100";  //杭州支付测试接口
    public static final String UPLOADIMG = "http://183.129.130.119:13107";  //上传头像

    public static final String UM_CHANNEL = "测试环境";  //友盟渠道信息
    public static final String BUGLY_APPID = "cc7d63e069";  //bugly测试环境
    public static String VERSION = "-测试版"; //测试版
    public static String USER_AGREEMENT = "http://www.yikahui.net/AppH5/agreement/user-potocol.html"; //用户协议
    public static String PRIVACY_STATEMENT = "http://www.yikahui.net/AppH5/agreement/privacy-statement.html"; //隐私声明

    /**
     * 唤起百度地图
     */
    //百度地图的包名
    public final static String BAIDU_PKG = "com.baidu.BaiduMap";
    //高德地图的包名
    public final static String AMAP_PKG = "com.autonavi.minimap";
}
