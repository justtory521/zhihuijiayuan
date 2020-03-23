package tendency.hz.zhihuijiayuan.bean.base;

import android.os.Environment;

import static tendency.hz.zhihuijiayuan.bean.base.Config.PAY_URL;
import static tendency.hz.zhihuijiayuan.bean.base.Config.URL;

/**
 * Created by JasonYao on 2018/2/26.
 */

public class Uri {
    public static String DEVELOPMENTURL = "http://192.168.50.239/AndroidToJs/";
    //    public static String DEVELOPMENTURL = "http://ga.iotone.cn/?Q1gBAAAAADlCI=";
//    public static final String DEVELOPMENTURL = "http://192.168.50.236:8101/";
    //APP文件路径
    public static final String tdrPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zhihuijiayuan";
    //APP图片缓存路径
    public static final String imagePath = tdrPath + "/imgCache";
    //拍照路径
    public static final String camera = "/zhihuijiayuan/camera";
    //音频路径
    public static final String audioPath = tdrPath + "/audio";
    //视频路径
    public static final String videoPath = tdrPath + "/video";
    //下载路径
    public static final String downloadPath = tdrPath + "/download";
    //tbs临时文件夹
    public static final String tempPath = tdrPath + "/tbsTemp";

    /**
     * 用户相关操作
     */
    public static final class User {
        public static final String LOGIN = URL + "/api/AppLogin/Login";  //用户登录
        public static final String REGISTERSENDSMSCODE = URL + "/api/AppRegist/SendSMSCode"; //发送手机短信验证码
        public static final String REGISTERCHECKSMSCODE = URL + "/api/AppRegist/CheckSMSCode"; //校验短信验证码是否有效
        public static final String REGISTER = URL + "/api/AppRegist/Add"; //注册提交
        public static final String LOGOUT = URL + "/api/AppLogin/Logout"; //退出登录系统
        public static final String CHECKTOKEN = URL + "/api/AppLogin/IsValid"; //校验token是否过期
        public static final String PWDCHANGESENDSMS = URL + "/api/AppPwdChange/SendSMSCode"; //密码找回发送手机验证码
        public static final String PWDCHANGECHECKSMS = URL + "/api/AppPwdChange/CheckSMSCode";
        public static final String PWDCHANGEEDIT = URL + "/api/AppPwdChange/Edit"; //密码重置
        public static final String CHANGEPWD = URL + "/api/Password"; //密码修改
        public static final String SENDLOGINSMSCODE = URL + "/api/AppLogin/SendSMSCode"; //发送登录验证码
        public static final String LOGINBYSMS = URL + "/api/AppLogin/PhoneLogin"; //手机号验证码登录
        public static final String CHANGEPHONESENDSMSCODE = URL + "/api/AppPhoneChange/SendSMSCode"; //发送老手机短信验证码
        public static final String CHANGEPHONECHECKCODE = URL + "/api/AppPhoneChange/CheckCode";  //老手机验证码校验
        public static final String CHANGEPHONESENDBINDSMSCODE = URL + "/api/AppPhoneChange/SendBindSMSCode"; //发送新手机短信验证码
        public static final String CHANGEPHONEBINDING = URL + "/api/AppPhoneChange/PhoneBind"; //绑定新手机
        public static final String ISBINDPLATFORM = URL + "/api/DevPlatform/Login";//三方登录uid是否已存在
        public static final String ISBINDPHONE = URL + "/api/DevPlatform/ValidatePhone";//手机号是否绑定三方登录
        public static final String SENDCODE = URL + "/api/DevPlatform/SendSMSCode";//三方登录发送验证码
        public static final String CHECKCODE = URL + "/api/DevPlatform/CheckSMSCode";//三方登录检验验证码
        public static final String SETPWD = URL + "/api/DevPlatform/SetPassWord";//三方登录设置密码
    }

    /**
     * 个人信息相关操作
     */
    public static final class Personal {
        public static final String GETPERSONALINFO = URL + "/api/PersonInfo/GetPersonalInfoOnly"; //获取个人资料展示信息
        //        public static final String GETAUTHUSER = URL + "/api/PersonInfo/GetPersonalInfo"; //获取登录用户信息
        public static final String ADDORUPDATEPERSONALINFO = URL + "/api/PersonInfo/AddOrUpdatePersonalInfoOnly";  //个人信息变更
        public static final String GETMESSAGE = URL + "/api/Message/get";  //获取个人信息
        public static final String EDITPERSONINFO = URL + "/api/PersonInfo/EditPersonInfo";  //个人信息变更
        public static final String GETMYSERVICELIST = URL + "/api/MyCardService/GetMyServiceList"; //获取用户进行服务
        public static final String CREDITRANKINGSAN = URL + "/api/Credit/CreditRankingsan"; //获取用户信誉积分排行
        public static final String CREDITRANKINGWUSHI = URL + "/api/Credit/CreditRankingwushi"; //获取信誉积分总排行
        public static final String GETEQUITYLIST = URL + "/api/ExecuteServer/GetEquityList";//获得服务
        public static final String GETEXECUTELIST = URL + "/api/ExecuteServer/GetExecuteList";//参与服务
        public static final String GETCREDITRECORD = URL + "/api/WisdomCredit/GetCreditRecord";//信用记录
        public static final String GETCREDITHISTORY = URL + "/api/WisdomCredit/GetCreditHistory";//评估记录
        public static final String GETMYEQUITY = URL + "/api/AccountEquity/GetMyEquity";//我的特权
        public static final String GETALLINFO = URL + "/api/EditUserInfo/GetAllInfo"; //获取用户所有信息
        public static final String EDITEDUCATION = URL + "/api/EditUserInfo/EditEducation";//修改学历学籍
        public static final String SENDEMAILCODE = URL + "/api/EditUserInfo/SendEmailCode";//发送邮箱验证码
        public static final String EDITEMAIL = URL + "/api/EditUserInfo/EditEmail";//修改邮箱账号
        public static final String EDITJOB = URL + "/api/EditUserInfo/EditJob";//修改职业信息
        public static final String EDITFAMILY = URL + "/api/EditUserInfo/EditFamily";//修改家庭信息
        public static final String EDITMARRIAGE = URL + "/api/EditUserInfo/EditMarriage";//修改婚姻信息
        public static final String ADDMAJOR = URL + "/api/EditUserInfo/AddMajor";//添加专业证书
        public static final String DELETEMAJOR = URL + "/api/EditUserInfo/DeleteMajor";//删除专业证书
        public static final String EDITMAJOR = URL + "/api/EditUserInfo/EditMajor";//修改专业证书
        public static final String ADDSERVICE = URL + "/api/EditUserInfo/AddService";//添加服务证书
        public static final String DELETESERVICE = URL + "/api/EditUserInfo/DeleteService";//修改服务证书
        public static final String EDITSERVICE = URL + "/api/EditUserInfo/EditService";//删除服务证书
        public static final String GETMAJORLIST = URL + "/api/EditUserInfo/GetMajorList";//专业证书列表
        public static final String GETSERVICELIST = URL + "/api/EditUserInfo/GetServiceList";//服务证书列表

    }

    /**
     * 基本配置信息
     */
    public static final class Base {
        public static final String GETALLDISTRICT = URL + "/api/District/AllDistricts"; //获取标准的3级区划
        public static final String GETDATADICTIONARY = URL + "/api/DataDictionary/List"; //获取字典列表
        public static final String UPLOADIMG = Config.UPLOADIMG + "/api/Base64"; //上传头像
        public static final String ADDDEVICEINFO = URL + "/api/AppDeviceInfo/Add"; //提交设备信息
    }

    /**
     * 卡片相关接口
     */
    public static final class Card {
        public static final String FINDCARDLIST = URL + "/api/CardManage/CardDiscover"; //发现页面卡片列表
        public static final String MYCARDLIST = URL + "/api/CardAttention/List"; //个人卡片列表
        public static final String MYCARDLISTANONYMOUS = URL + "/api/CardAttention/AnonymousList"; //个人卡列表匿名状态
        public static final String CARD_SORT = URL + "/api/CardAttention/CardSort";//卡片排序
        public static final String ANONYMOUS_CARD_SORT = URL + "/api/CardAttention/AnonymousCardSort";//未登录卡片排序
        public static final String CARDATTENTIONADD = URL + "/api/CardAttention/AddFocus"; //个人卡片添加
        public static final String INFOSYNC = URL + "/api/InfoSync"; //信息同步
        public static final String PREVIEWCARD = URL + "/api/CardManage/Preview"; //卡片预览接口/api/CardClickVolume/Add
        public static final String DELETECARD = URL + "/api/CardAttention/CancelFocus"; //删除卡片
        public static final String CARDCLICKVOLUME = URL + "/api/CardClickVolume/Add"; //卡片点击次数记录
        public static final String GETHISCARD = URL + "/api/HisCard"; //历史关注卡列表
        public static final String GETPOPULARCARD = URL + "/api/PopularCard"; //最受欢迎卡列表
        public static final String ANONYMOUSFOCUS = URL + "/api/CardAttention/AnonymousFocus"; //匿名关注卡
        public static final String ANONYMOUSCANCEL = URL + "/api/CardAttention/AnonymousCancel"; //匿名取消关注卡
        public static final String CARDRECOMMEND = URL + "/api/CardAttention/Recommend"; //推荐卡列表
        public static final String GETAPPCARDINFO = URL + "/api/CardAttention/GetAppCardInfo"; //获取应用卡的信息
        public static final String CHECKCANOPERATE = URL + "/api/AppCardCheck/CanOperate"; //app端卡片有效性校验
        public static final String FORYOUCARD = URL + "/api/ForYouCard";
        public static final String GETAPPBANNER = URL + "/api/AppBanner/GetTop";  //获取Banner列表
        public static final String GETBANNERDETAIL = URL + "/api/AppBanner/GetDetail"; //获取Banner详情
        public static final String AUTOFOCUSCARD = URL + "/api/CardAutoFocus/Post"; //自动绑定特定卡片
        public static final String GETQRCODE = URL + "/api/CardQRCode/GetFullUrl"; //获取卡片二维码图片
        public static final String CHOICECARDRECOMMEND = URL + "/api/CardRecommend"; //为你推荐卡
        public static final String CHOICECARDTHEME = URL + "/api/CardTheme"; //按主题拉取卡片接口
        public static final String CHOICECARDSEARCH = URL + "/api/CardSearch"; //卡片搜索
        public static final String GETCARDHOTSEARCH = URL + "/api/CardHotSearch"; //获取卡片热搜榜
        public static final String GETCARDCODE = URL + "/api/CardQRCode/GetQRCodeID"; //获取卡片热搜榜

        public static final String MYCARDLISTLEXUN = URL + "/api/CardAttentionLeXun/List";  //乐巡个人卡片列表
        public static final String MYCARDLISTLEXUNANONYMOUS = URL + "/api/CardAttentionLeXun/AnonymousList"; //乐巡个人卡片列表（匿名）
    }

    /**
     * 设置相关
     */
    public static final class Set {
        public static final String FEEDBACK = URL + "/api/AdviceFeedback/Add"; //意见反馈
        public static final String VALIDATE = URL + "/api/PersonInfo/Add"; //实名认证
        public static final String CARDQRCODE = URL + "/api/CardQRCode/Parse"; //二维码内容中code解析
        public static final String STARTPAGE = URL + "/api/StartPage"; //启动页广告图
        public static final String SENDADDTYPE = URL + "/api/APPClient/AppType"; //提交APP客户端ID和类型
        public static final String GETMESSAGEPREVENT = URL + "/api/MessagePrevent/Get"; //获取消息免打扰状态
        public static final String EDITMESSAGEPREVENT = URL + "/api/MessagePrevent/Edit"; //编辑消息免打扰
        public static final String VERSION = URL + "/api/YiKaHuiAppVersion/Version"; //智慧家园最新版本号接口地址
        public static final String WXSUBSCRIBEMESSAGE = PAY_URL + "/api/WXSubscribeMessae"; //微信开放平台订阅消息发送接口
    }

    public static final class Pay {
        public static final String GENERATIONORDER_ALIPAY = PAY_URL + "/api/AliPay";
        public static final String PAYRESULTNOTIFY_ALIPAY = PAY_URL + "/api/PayResultNotify";
        public static final String GENERATIONORDER_WEIXIN = PAY_URL + "/api/WXPay";
        public static final String PAYRESULTNOTIFY_WEIXIN = PAY_URL + "/api/WXNotify";
        public static final String GETTIME = PAY_URL + "/api/GetTime";
    }
}
