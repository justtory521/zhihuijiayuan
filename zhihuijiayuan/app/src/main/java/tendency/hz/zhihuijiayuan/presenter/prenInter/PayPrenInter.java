package tendency.hz.zhihuijiayuan.presenter.prenInter;

import tendency.hz.zhihuijiayuan.bean.Order;

/**
 * Created by JasonYao on 2018/7/26.
 */
public interface PayPrenInter {
    /**
     * 生成支付宝订单
     *
     * @param netCode
     * @param CardId
     * @param order
     */
    void greateAlipayOrder(int netCode, String CardId, Order order);

    /**
     * 支付宝结果回调
     *
     * @param netCode
     * @param order
     */
    void alipayResultNotify(int netCode, Order order);

    /**
     * 生成微信订单
     *
     * @param netCode
     * @param CardId
     * @param order
     */
    void greateWeixinOrder(int netCode, String CardId, Order order);

    /**
     * 微信结果回调
     *
     * @param netCode
     */
    void weiXinResultNotify(int netCode, String orderNum, String payStatus, String finishTime);

    /**
     * 获取时间戳
     *
     * @param netCode
     */
    void getTime(int netCode);
}
