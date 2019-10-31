package tendency.hz.zhihuijiayuan.presenter.prenInter;

/**
 * 用户操作相关P层接口定义
 * Created by JasonYao on 2018/3/21.
 */

public interface UserPrenInter {

    /**
     * 用户登录
     *
     * @param account  用户名
     * @param password 密码
     */
    void login(String account, String password, int NetCode);

    /**
     * 用户登出
     */
    void logout(int netCode);

    /**
     * 获取注册短信验证码
     *
     * @param phone
     * @param netCode
     */
    void getRegisterSMS(String phone, int netCode);

    /**
     * 注册
     *
     * @param account
     * @param password
     * @param code
     * @param netCode
     */
    void register(String account, String password, String code, int netCode);

    /**
     * 校验短信验证码是否有效
     *
     * @param phone
     * @param code
     * @param netCode
     */
    void checkRegisterSMS(String phone, String code, int netCode);

    /**
     * 校验token是否过期
     *
     * @param netCode
     */
    void checkToken(int netCode);

    /**
     * 修改密码发送短信
     *
     * @param phone
     * @param netCode
     */
    void changePwdSMS(String phone, int netCode);

    /**
     * 修改密码校验短信
     *
     * @param phone
     * @param netCode
     */
    void checkChangePwdSMS(String phone, String code, int netCode);

    /**
     * 忘记密码修改密码
     *
     * @param account
     * @param password
     * @param netCode
     */
    void changePwd(String account, String password, String code, int netCode);

    /**
     * 用户密码修改
     *
     * @param password
     * @param passwordConfirm
     * @param netCode
     */
    void changePassWord(String password, String passwordConfirm, int netCode);

    /**
     * 发送登录短信验证码
     *
     * @param netCode
     * @param phone
     */
    void sendLoginSmsCode(int netCode, String phone);

    /**
     * 使用短信验证码登录
     *
     * @param netCode
     * @param account
     * @param code
     */
    void loginBySms(int netCode, String account, String code);


    /**
     * 三方登录uid是否已存在
     *
     * @param OpenId
     * @param OpenIdType
     */
    void isBindPlatform(int netCode, String OpenId, int OpenIdType);


    /**
     * 手机号是否绑定三方登录
     *
     * @param OpenId
     * @param OpenIdType
     * @param Phone
     */
    void isBindPhone(int netCode, String OpenId, int OpenIdType, String Phone);


    /**
     * 三方登录发送验证码
     *
     * @param phone
     */
    void sendCode(int netCode, String phone);


    /**
     * 三方登录检验验证码
     *
     * @param Phone
     * @param Code
     * @param IsRegister
     * @param OpenIdType
     * @param OpenId
     */
    void checkCode(int netCode, String Phone, String Code, int IsRegister, int OpenIdType, String OpenId);

    /**
     * 三方登录设置密码
     *
     * @param Phone
     * @param Code
     * @param OpenIdType
     * @param OpenId
     * @param pwd
     */
    void setPwd(int netCode, String Phone, String Code,int OpenIdType, String OpenId, String pwd);
}
