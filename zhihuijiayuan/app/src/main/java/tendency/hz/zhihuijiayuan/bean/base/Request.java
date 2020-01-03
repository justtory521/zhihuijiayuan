package tendency.hz.zhihuijiayuan.bean.base;

/**
 * Created by JasonYao on 2018/2/27.
 * 请求标识码
 */

public class Request {
    public static final class Permissions {
        public static final int REQUEST_PHONE_STATE = 0x0001; //请求获取读取手机状态权限
        public static final int REQUEST_LOCATION_STATE = 0x0002; //请求获取定位权限
        public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 0x0003; //请求读写存储卡权限
        public static final int REQUEST_IGNORE_BATTERY_CODE = 0x0004; //请求忽略电池优化
        public static final int REQUEST_ALL_PERMISSIONS = 0x0005; //请求获取所有权限
        public static final int REQUEST_CONTACTS_PERMISSIONS = 0x0006; //请求读取通讯录权限
        public static final int REQUEST_RECORD_PERMISSIONS = 0x0007; //请求录音权限
        public static final int REQUEST_CAMERA = 0x0008; //请求相机权限
        public static final int REQUEST_CALL_PHONE = 0x0009; //打电话

    }

    public static final class StartActivityRspCode {
        public static final int GOTO_CITYPICKER = 0x1001; //跳转至城市选择页
        public static final int PUSH_CARDCONTENT_JUMP = 0x1002; //从推送跳转至卡片页面
        public static final int NORMAL_CARDCONTENT_JUMP = 0x1003; //正常跳转至卡片页面
        public static final int MESSAGE_CARDCONTENT_JUMP = 0x1007; //从消息列表中跳转至卡片页面
        public static final int SLPASH_CARDCONTENT_JUMP = 0x1004; //从启动页跳转至卡片页面
        public static final int WEB_CARDCONTENT_JUMP = 0x1005; //从浏览器跳转至卡片页面
        public static final int RSET_NICKNAME = 0x1006; //修改昵称
        public static final int PUSH_TOMESSAGELIST_JUMP = 0x1007; //从推送跳转至消息列表页面
        public static final int JUMP_TO_LOGINBYPWD = 0x1008; //跳转至密码登录页面
        public static final int CARD_JUMP_TO_LOGIN = 0x1009; //从卡片页面跳转至登录页
        public static final int CARD_JUMP_TO_CITYPICKER = 0x100a; //从卡片页面跳转至城市选择页面
        public static final int CREDIT_TO_PERSONAL = 0x100b; //从智慧信用跳转至个人资料
        public static final int SCAN_ID_CARD = 0x100c; //身份证扫描
        public static final int CARD_CARDCONTENT_JUMP = 0x100d; //卡片跳转至卡片页面，不弹出关注tips
    }

    public static final class Broadcast {
        public static final String RELOADURL = "reloadUrl"; //刷新页面
        public static final String JG_PUSH = "push"; //极光透传
        public static final String SENDBAIDU = "sendBaidu"; //发送百度定位点
    }

    public static final class Message {
        public static final int SDK_ALIPAY_FLAG = 0x2001; //阿里支付
    }

    /**
     * 定位
     */
    public static class LocateState {
        public static final int LOCATING = 0x1001;
        public static final int FAILED = 0x1002;
        public static final int SUCCESS = 0x1003;
    }

    public static class Bluetooth {
        public static final int bluetoothEnable = 0x1010; //检查蓝牙是否可用
    }
}

