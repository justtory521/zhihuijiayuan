package tendency.hz.zhihuijiayuan.bean.base;

/**
 * Created by JasonYao on 2018/3/19.
 */

public class What {
    public static final String TOKEN_HEADER = "Bearer ";
    public static final String PAGE_SIZE = "50";
    public static final int PICKERPHOTO = 0x2001; //选择图片请求标识码
    public static final int TAKEPHOTO = 0x2002; //拍照请求标识码
    public static final int SCANQRCODE = 0x2003; //扫描二维码

    /**
     * 权限相关标识符
     */
    public static class Permissions {
        public static final int checkPermissions = 0x2061; //权限校验
        public static final int getSDPermissions = 0x2062; //获取SD卡相关权限
        public static final int getCameraPermissions = 0x2063; //获取拍照权限
        public static final int getLocatPermissions = 0x2064; //获取定位权限
    }

    public static class RecyclerItemType {
        public static final int typeItem = 0x2071; //Item类型
        public static final int typeFooter = 0x2072; //Footer类型
    }

    /**
     * 相机相关操作
     */
    public static class Camera {
        public static final int iDCard = 0x2081; //身份证识别
    }

    /**
     * 卡类型
     */
    public static class CardType {
        public static final String businessCard = "2"; //业务卡
        public static final String appCard = "1"; //应用卡
    }

    /**
     * 分享内容类型
     */
    public static class ShareType {
        public static final String SHARETYPE_TEXT = "1"; //文本
        public static final String SHARETYPE_IMG = "2"; //图片分享类型
        public static final String SHARETYPE_URL = "3"; //链接
    }

}
