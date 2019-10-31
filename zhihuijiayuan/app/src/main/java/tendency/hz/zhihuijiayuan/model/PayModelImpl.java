package tendency.hz.zhihuijiayuan.model;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tendency.hz.zhihuijiayuan.bean.Order;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Uri;
import tendency.hz.zhihuijiayuan.model.modelInter.AllModelInter;
import tendency.hz.zhihuijiayuan.model.modelInter.PayModelInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.AllPrenInter;
import tendency.hz.zhihuijiayuan.units.AESOperator;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.NoHttpUtil;

/**
 * Created by JasonYao on 2018/7/26.
 */
public class PayModelImpl extends AllModelInter implements PayModelInter {
    private static final String TAG = "PayModelImpl---";
    private AllPrenInter mAllPrenInter;
    private Gson mGson = new Gson();
    private static String mTime;

    public PayModelImpl(AllPrenInter allPrenInter) {
        mAllPrenInter = allPrenInter;
    }

    @Override
    public void rspSuccess(int what, Object object) throws JSONException {
        switch (what) {
            case NetCode.Pay.createAliPayOrder:
                Log.e(TAG, "支付宝解密服务器返回：" + AESOperator.getInstance().decrypt(String.valueOf(((JSONObject) object).get("Data"))));
                mAllPrenInter.onSuccess(what, AESOperator.getInstance().decrypt(String.valueOf(((JSONObject) object).get("Data"))));
                break;
            case NetCode.Pay.AlipayResultNotify:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.Pay.createWeixinPayOrder:
                Log.e(TAG, "微信解密服务器返回：" + AESOperator.getInstance().decrypt(String.valueOf(((JSONObject) object).get("Data"))));
                JSONObject jsonObject = new JSONObject(AESOperator.getInstance().decrypt(String.valueOf(((JSONObject) object).get("Data"))));
                Order order = mGson.fromJson(jsonObject.toString(), Order.class);
                order.setMypackage(jsonObject.getString("package"));
                mAllPrenInter.onSuccess(what, order);
                break;
            case NetCode.Pay.weixinResultNotify:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.Pay.getTime:
                mTime = ((JSONObject) object).getString("Data");
                mAllPrenInter.onSuccess(what, null);
                break;
        }
    }

    @Override
    public void rspFailed(int what, Object object) {
        mAllPrenInter.onFail(what, object);
    }

    @Override
    public void greateAlipayOrder(int netCode, String CardId, Order order) {
        if (netCode != NetCode.Pay.createAliPayOrder) {
            return;
        }

        List<NoHttpUtil.Param> list = new ArrayList<>();
        list.add(new NoHttpUtil.Param("SellerId", order.getSellerId()));
        list.add(new NoHttpUtil.Param("ProductDesc", order.getProductDesc()));
        list.add(new NoHttpUtil.Param("OrderSubject", order.getOrderSubject()));
        list.add(new NoHttpUtil.Param("Amount", order.getAmount()));
        list.add(new NoHttpUtil.Param("OutTradeNo", order.getOutTradeNo()));
        list.add(new NoHttpUtil.Param("ClientId", BaseUnits.getInstance().getPhoneKey()));
        list.add(new NoHttpUtil.Param("CardId", CardId));
        list.add(new NoHttpUtil.Param("AsyncNotifyUrl", order.getAsyncNotifyUrl()));
        list.add(new NoHttpUtil.Param("Date", mTime));
        list.add(new NoHttpUtil.Param("ExtendInfo", order.getExtendInfo()));
        if (order.getTimeout() != null) {
            list.add(new NoHttpUtil.Param("Timeout", order.getTimeout()));
        }
        Log.e(TAG, list.toString());

        List<NoHttpUtil.Param> listParam = new ArrayList<>();
        listParam.add(new NoHttpUtil.Param("Encrypt", new String(AESOperator.getInstance().encrypt(mGson.toJson(list2Map(list))))));

        NoHttpUtil.post(netCode, Uri.Pay.GENERATIONORDER_ALIPAY, onResponseListener, listParam);
    }

    @Override
    public void alipayResultNotify(int netCode, Order order) {
        if (netCode != NetCode.Pay.AlipayResultNotify) {
            return;
        }

        List<NoHttpUtil.Param> list = new ArrayList<>();
        list.add(new NoHttpUtil.Param("out_trade_no", order.getOut_trade_no()));
        list.add(new NoHttpUtil.Param("trade_no", order.getTrade_no()));
        list.add(new NoHttpUtil.Param("app_id", order.getApp_id()));
        list.add(new NoHttpUtil.Param("total_amount", order.getTotal_amount()));
        list.add(new NoHttpUtil.Param("seller_id", order.getSeller_id()));
        list.add(new NoHttpUtil.Param("timestamp", order.getTimestamp()));
        list.add(new NoHttpUtil.Param("code", order.getCode()));
        list.add(new NoHttpUtil.Param("resultStatus", order.getResultStatus()));
        list.add(new NoHttpUtil.Param("ClientId", BaseUnits.getInstance().getPhoneKey()));
        list.add(new NoHttpUtil.Param("Date", mTime));

        List<NoHttpUtil.Param> listParam = new ArrayList<>();
        listParam.add(new NoHttpUtil.Param("Encrypt", new String(AESOperator.getInstance().encrypt(mGson.toJson(list2Map(list))))));

        NoHttpUtil.post(netCode, Uri.Pay.PAYRESULTNOTIFY_ALIPAY, onResponseListener, listParam);
    }

    @Override
    public void greateWeixinOrder(int netCode, String CardId, Order order) {
        if (netCode != NetCode.Pay.createWeixinPayOrder) {
            return;
        }

        List<NoHttpUtil.Param> list = new ArrayList<>();
        list.add(new NoHttpUtil.Param("SellerId", order.getSellerId()));
        list.add(new NoHttpUtil.Param("ProductDesc", order.getProductDesc()));
        list.add(new NoHttpUtil.Param("OrderSubject", order.getOrderSubject()));
        list.add(new NoHttpUtil.Param("Amount", order.getAmount()));
        list.add(new NoHttpUtil.Param("OutTradeNo", order.getOutTradeNo()));
        list.add(new NoHttpUtil.Param("CardId", CardId));
        list.add(new NoHttpUtil.Param("ClientId", BaseUnits.getInstance().getPhoneKey()));
        list.add(new NoHttpUtil.Param("AsyncNotifyUrl", order.getAsyncNotifyUrl()));
        list.add(new NoHttpUtil.Param("Remarks", order.getBizKey()));
        list.add(new NoHttpUtil.Param("Date", mTime));
        list.add(new NoHttpUtil.Param("ExtendInfo", order.getExtendInfo()));
        if (order.getTimeout() != null) {
            list.add(new NoHttpUtil.Param("Timeout", order.getTimeout()));
        }

        List<NoHttpUtil.Param> listParam = new ArrayList<>();
        listParam.add(new NoHttpUtil.Param("Encrypt", new String(AESOperator.getInstance().encrypt(mGson.toJson(list2Map(list))))));
        NoHttpUtil.post(netCode, Uri.Pay.GENERATIONORDER_WEIXIN, onResponseListener, listParam);
    }

    @Override
    public void weiXinResultNotify(int netCode, String orderNum, String payStatus, String finishTime) {
        if (netCode != NetCode.Pay.weixinResultNotify) {
            return;
        }

        List<NoHttpUtil.Param> list = new ArrayList<>();
        list.add(new NoHttpUtil.Param("OrderNum", orderNum));
        list.add(new NoHttpUtil.Param("PayStatus", payStatus));
        list.add(new NoHttpUtil.Param("FinishTime", finishTime));
        list.add(new NoHttpUtil.Param("ClientId", BaseUnits.getInstance().getPhoneKey()));
        list.add(new NoHttpUtil.Param("Date", mTime));

        Log.e(TAG, "微信同步参数：" + list.toString());
        List<NoHttpUtil.Param> listParam = new ArrayList<>();
        listParam.add(new NoHttpUtil.Param("Encrypt", new String(AESOperator.getInstance().encrypt(mGson.toJson(list2Map(list))))));
        Log.e(TAG, "微信同步参数：" + listParam.toString());
        NoHttpUtil.post(netCode, Uri.Pay.PAYRESULTNOTIFY_WEIXIN, onResponseListener, listParam);
    }

    @Override
    public void getTime(int netCode) {
        if (netCode != NetCode.Pay.getTime) {
            return;
        }

        List<NoHttpUtil.Param> list = new ArrayList<>();
        list.add(new NoHttpUtil.Param("ClientID", BaseUnits.getInstance().getPhoneKey()));

        NoHttpUtil.get(netCode, Uri.Pay.GETTIME, onResponseListener, list);
    }

    private Map<String, String> list2Map(List<NoHttpUtil.Param> list) {
        Map<String, String> params = new HashMap<>();

        if (list != null) {
            for (NoHttpUtil.Param param : list) {
                params.put(param.getKey(), String.valueOf(param.getValue()));
            }
        }

        return params;
    }
}
