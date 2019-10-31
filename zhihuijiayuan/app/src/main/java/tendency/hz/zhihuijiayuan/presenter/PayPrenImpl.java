package tendency.hz.zhihuijiayuan.presenter;

import tendency.hz.zhihuijiayuan.bean.Order;
import tendency.hz.zhihuijiayuan.model.PayModelImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.AllPrenInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PayPrenInter;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/7/26.
 */
public class PayPrenImpl implements PayPrenInter, AllPrenInter {
    private AllViewInter mAllViewInter;
    private PayModelImpl mPayModelImpl;

    public PayPrenImpl(AllViewInter allViewInter) {
        mAllViewInter = allViewInter;
        mPayModelImpl = new PayModelImpl(this);
    }

    @Override
    public void onSuccess(int what, Object object) {
        mAllViewInter.onSuccessed(what, object);
    }

    @Override
    public void onFail(int what, Object object) {
        mAllViewInter.onFailed(what, object);
    }

    @Override
    public void greateAlipayOrder(int netCode, String CardId, Order order) {
        mPayModelImpl.greateAlipayOrder(netCode, CardId, order);
    }

    @Override
    public void alipayResultNotify(int netCode, Order order) {
        mPayModelImpl.alipayResultNotify(netCode, order);
    }

    @Override
    public void greateWeixinOrder(int netCode, String CardId, Order order) {
        mPayModelImpl.greateWeixinOrder(netCode, CardId, order);
    }

    @Override
    public void weiXinResultNotify(int netCode, String orderNum, String payStatus, String finishTime) {
        mPayModelImpl.weiXinResultNotify(netCode, orderNum, payStatus, finishTime);
    }

    @Override
    public void getTime(int netCode) {
        mPayModelImpl.getTime(netCode);
    }
}
