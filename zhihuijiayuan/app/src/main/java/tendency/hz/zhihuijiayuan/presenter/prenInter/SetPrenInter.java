package tendency.hz.zhihuijiayuan.presenter.prenInter;

/**
 * Created by JasonYao on 2018/4/2.
 */

public interface SetPrenInter {

    /**
     * 意见反馈
     *
     * @param netCode
     * @param imgList
     */
    void feedBack(int netCode, String title, String adviceContent, String imgList, String contact);


    /**
     * 身份证正面照片
     *
     * @param netCode
     * @param img
     */
    void uploadFrontIDCard(int netCode, String img);



    /**
     * 身份证反面照片
     *
     * @param netCode
     * @param img
     */
    void uploadBackIDCard(int netCode, String img);
    /**
     * 身份认证
     *
     * @param netCode
     * @param cardId
     * @param realName
     */
    void validate(int netCode, String cardId, String realName, String sex, String nation,
                  String birthday, String address, String issue, String validate,
                  String frontCardPic, String backCardPic, String editIDCardTime);

    /**
     * 解析二维码中的code
     *
     * @param netCode
     * @param code
     */
    void cardQrCode(int netCode, String code);

    /**
     * 启动页广告图
     *
     * @param netCode
     */
    void startPage(int netCode);

    /**
     * 发送设备类型
     *
     * @param netCode
     */
    void sendAppTyoe(int netCode);

    /**
     * 获取消息免打扰状态
     *
     * @param netCode
     */
    void getMessagePrevent(int netCode);

    /**
     * 设置消息免打扰状态
     *
     * @param netCode
     */
    void editMessagePrevent(int netCode, String isEnablePrevent);

    /**
     * 获取最新版本
     *
     * @param netCode
     */
    void getVersion(int netCode);

    /**
     * 微信开放平台订阅消息发送接口
     *
     * @param netCode
     * @param touser
     * @param template_id
     * @param scene
     */
    void WXSubscribeMessage(int netCode, String touser, String template_id, String scene);
}
