package tendency.hz.zhihuijiayuan.bean.base;

import static tendency.hz.zhihuijiayuan.bean.base.Config.URL;

/**
 * 网络请求标识码
 * Created by JasonYao on 2018/3/21.
 */

public class NetCode {

    /**
     * 用户相关操作
     */
    public static final class User {
        public static final int login = 0x001; //用户登录
        public static final int logout = 0x002; //用户登出
        public static final int getRegisterSmsCode = 0x003; //注册获取短信验证码
        public static final int register = 0x004; //用户注册
        public static final int registerCheckSmsCode = 0x005; //注册校验短信验证码
        public static final int checkToken = 0x006; //校验Token是否过期
        public static final int changePwdSendSms = 0x007; //修改密码，发送手机验证码
        public static final int changePwdCheckSms = 0x008; //短信验证码验证
        public static final int changePwdEdit = 0x009; //密码重置
        public static final int changePwd = 0x00a; //修改密码
        public static final int isBindPlatform = 0x00b;//三方登录uid是否已存在
        public static final int isBindPhone = 0x00c;//手机号是否绑定三方登录
        public static final int sendCode = 0x00d;//三方登录发送验证码
        public static final int checkCode = 0x00e;//三方登录检验验证码
        public static final int setPwd = 0x00f;//三方登录设置密码

    }

    public static final class User2 {
        public static final int sendLoginSms = 0x600;  //发送登录短信验证码
        public static final int loginBySms = 0x601; //短信验证码登录
        public static final int changePhoneSendSms = 0x603; //发送老手机短信验证码
        public static final int changePhoneCheckSms = 0x604; //校验老手机短信验证码
        public static final int changePhoneSendBindSms = 0x605; //发送新手机短信验证码
        public static final int changePhoneBinding = 0x606; //绑定新手机
    }

    public static final class Base {
        public static final int getAllDistricts = 0x101; //获取标准3级区划
        public static final int getDataDictionary = 0x102; //获取数据字典
        public static final int uploadImg = 0x103; //上传图片,暂时只是用于上传头像
        public static final int appDeivceInfo = 0x104; //上传设备信息
    }

    /**
     * 个人信息相关操作
     */
    public static final class Personal {
        public static final int getAuthuser = 0x201; //获取卡包登录账户信息
        public static final int getPersonalInfo = 0x202;  //获取APP个人资料展示
        public static final int updatePersonalInfo = 0x203; //变更个人信息
        public static final int getMessage = 0x204; //获取消息列表
        public static final int RefreshMessage = 0x205; //刷新消息列表
        public static final int editPersonInfo = 0x206; //修改用户信息
        public static final int updateRefPersonalInfo = 0x207; //刷新个人资料
        public static final int getMyServiceList = 0x208; //获取用户进行服务
        public static final int creditRankingsan = 0x209; //获取用户信用积分排行
        public static final int creditRankingwushi = 0x20a; //获取信誉积分总排行
        public static final int getEquityList = 0x20b; //获得服务
        public static final int getExecuteList = 0x20c; //参与服务
        public static final int getCreditRecord = 0x20d; //信用记录
        public static final int getCreditHistory = 0x20e; //评估记录
        public static final int getMyEquity = 0x20f; //我的特权
        public static final int getAllInfo = 0x210; //获取用户所有信息
        public static final int editEducation = 0x211;//修改学历学籍
        public static final int sendEmailCode = 0x212;//发送邮箱验证码
        public static final int editEmail = 0x213;//修改邮箱账号
        public static final int editJob = 0x214;//修改职业信息
        public static final int editFamily = 0x215;//修改家庭信息
        public static final int editMarriage = 0x216;//修改婚姻信息
        public static final int addMajor = 0x217;//添加专业证书
        public static final int deleteMajor = 0x218;//删除专业证书
        public static final int editMajor = 0x219;//修改专业证书
        public static final int addService = 0x21a;//添加服务证书
        public static final int editService = 0x21b;//修改服务证书
        public static final int deleteService = 0x21c;//删除服务证书
        public static final int getMajorList = 0x21d;//专业证书列表
        public static final int getServiceList = 0x21e;//服务证书列表
    }

    /**
     * 卡片相关
     */
    public static final class Card {
        public static final int findListRefresh = 0x301; //发现卡片列表刷新
        public static final int findListLoad = 0x302; //发现卡片列表加载更多
        public static final int myCardListRefresh = 0x303; //个人卡片列表刷新
        public static final int myCardListLoad = 0x304; //个人卡片列表加载更多
        public static final int cardAttentionAdd = 0x305; //个人卡片添加
        public static final int infoSync = 0x306; //信息同步
        public static final int previewCard = 0x307; //卡片预览接口
        public static final int deleteCard = 0x308; //删除卡片
        public static final int clickVolumeAdd = 0x309; //卡片点击次数记录
        public static final int getHisCard = 0x30a; //获取历史卡片
        public static final int getPopularCard = 0x30b;//获取最受欢迎的卡片
        public static final int anonymousFocus = 0x30c; //匿名关注卡
        public static final int anonymousCancel = 0x30d; //匿名取消关注卡
        public static final int getRecommendCard = 0x30e; //获取推荐卡
        public static final int getAppCardInfo = 0x30f; //获取应用卡信息
        public static final int openOtherCard = 0x310; //卡片预览接口
        public static final int sortCard = 0x311; //卡片排序接口
        public static final int anonymousCardSort = 0x312; //未登录卡片排序接口
    }

    /**
     * 卡片相关2
     */
    public static final class Card2 {
        public static final int checkCanOperate = 0x501; //校验卡片有效性
        public static final int checkPushCanOperate = 0x502; //校验推送过来的卡片有效性
        public static final int getAnonymousList = 0x503; //个人卡列表（匿名状态）
        public static final int getRecommandCard = 0x504; //获取推荐卡片
        public static final int getChoiceCard = 0x505; //获取精选卡片
        public static final int getBanner = 0x506; //获取Banner
        public static final int getBannerDetail = 0x507; //获取Banner详情
        public static final int autoFocusCard = 0x508; //自动绑定特定卡片
        public static final int getQrCodeUrl = 0x509; //获取卡片二维码图片
        public static final int getShareQrCodeUrl = 0x50a; //获取分享卡片二维码图片
        public static final int getChoiceRecommend = 0x50b; //为你推荐卡
        public static final int getChoiceCardTheme = 0x50c; //按主题拉取卡片接口
        public static final int getChoiceCardSearch = 0x50d; //按主题搜索卡片
        public static final int getCardHotSearch = 0x50e; //获取卡片热搜榜
        public static final int previewCard = 0x50f; //卡片内关闭并打卡卡片时预览卡片
        public static final int getCardCode = 0x511; //获取卡片Code
    }

    public static final class Set {
        public static final int feedBack = 0x401; //意见反馈
        public static final int validate = 0x402; //实名认证
        public static final int cardQrCode = 0x403; //二维码内容中code解析
        public static final int startPage = 0x404; //启动页广告图
        public static final int downLoad = 0x405; //下载
        public static final int sendAppType = 0x406; //发送设备类型
        public static final int getMessagePrevent = 0x407; //获取消息免打扰
        public static final int editMessagePrevent = 0x408; //编辑消息免打扰
        public static final int getVersion = 0x409; //获取版本号
        public static final int wxSubscribeMessage = 0x40a; //微信开放平台订阅消息发送接口
        public static final int uploadFrontIDCard = 0x40b; //发送身份证正面照片
        public static final int uploadBackIDCard = 0x40c; //发送身份证背面照片
    }

    public static final class Pay {
        public static final int createAliPayOrder = 0x501; //生成阿里支付订单
        public static final int AlipayResultNotify = 0x502; //支付宝支付结果同步通知接口
        public static final int createWeixinPayOrder = 0x503; //生产微信支付订单
        public static final int weixinResultNotify = 0x504; //微信支付结果同步通知接口
        public static final int getTime = 0x505; //获取时间戳
    }
}
