package tendency.hz.zhihuijiayuan.view.picker;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.CardOrder;
import tendency.hz.zhihuijiayuan.bean.Order;
import tendency.hz.zhihuijiayuan.bean.PayResultBean;
import tendency.hz.zhihuijiayuan.bean.base.App;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.databinding.ActivityCheckstandBinding;
import tendency.hz.zhihuijiayuan.inter.PayResultInter;
import tendency.hz.zhihuijiayuan.presenter.PayPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PayPrenInter;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;
import tendency.hz.zhihuijiayuan.wxapi.WXPayEntryActivity;

/**
 * Created by JasonYao on 2018/7/23.
 */
public class CheckstandActivity extends BaseActivity implements AllViewInter, View.OnClickListener {
    private static final String TAG = "CheckstandActivity--";
    private ActivityCheckstandBinding mBinding;

    private Gson mGson = new Gson();

    private PayPrenInter mPayPrenInter;


    private final IWXAPI api = WXAPIFactory.createWXAPI(this, App.Pay.WX_APP_ID);

    private Order mOrder;
    private String aliOrder;
    private  String mCallBack;
    private String cardId;

    private JSONObject mSource = new JSONObject();

    public static CheckstandActivity mInstance;

    private CardOrder mCardOrder;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            mBinding.btnPay.setEnabled(true);
            if (msg == null) {
                try {
                    mSource.put("memo", "未知错误");
                    mSource.put("resultStatus", "500");
                    mSource.put("result", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(() -> EventBus.getDefault().post(new PayResultBean(mCallBack,"1","500",mSource)));
               close();
                return;
            }
            Map<String, String> resultObj = (Map<String, String>) msg.obj;
            try {
                if (!FormatUtils.getInstance().isEmpty(resultObj.get("result"))) {
                    mSource.put("result", new JSONObject(resultObj.get("result")));
                } else {
                    mSource.put("result", "");
                }

                mSource.put("resultStatus", resultObj.get("resultStatus"));
                mSource.put("memo", FormatUtils.getInstance().isEmpty(resultObj.get("memo")) ? "" : resultObj.get("memo"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (resultObj.get("resultStatus").equals("9000")) {
                String result = resultObj.get("result");
                Order order = mGson.fromJson(new JsonParser().parse(result).getAsJsonObject().getAsJsonObject("alipay_trade_app_pay_response").toString(), Order.class);
                order.setResultStatus(resultObj.get("resultStatus"));
                mPayPrenInter.alipayResultNotify(NetCode.Pay.AlipayResultNotify, order);
            } else {
                runOnUiThread(() -> EventBus.getDefault().post(new PayResultBean(mCallBack,"1","500",mSource)));
                close();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_checkstand);
        ViewUnits.getInstance().setTitleHeight(mBinding.layoutTitle);

        mInstance = this;

        mPayPrenInter = new PayPrenImpl(this);

        mCardOrder = (CardOrder) getIntent().getSerializableExtra("CardOrder");
        mCallBack = super.getIntent().getStringExtra("CallBack");
        cardId = super.getIntent().getStringExtra("cardId");

        initView();

        setListener();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mBinding.btnPay.setEnabled(true);
    }

    private void initView() {
        if (mCardOrder.getAlipay() == null) {
            mBinding.layoutAlipay.setVisibility(View.GONE);
            mBinding.rbWeixin.setChecked(true);
            mBinding.rbAlipay.setChecked(false);
        }
        if (mCardOrder.getWxPay() == null) {
            mBinding.layoutWeixin.setVisibility(View.GONE);
            mBinding.rbAlipay.setChecked(true);
            mBinding.rbWeixin.setChecked(false);
        }
        mBinding.textOrder.setText("订单详情：" + mCardOrder.getOutTrageNo());
        mBinding.textAmount.setText("￥" + mCardOrder.getAmount());
    }

    private void setListener() {
        mBinding.layoutWeixin.setOnClickListener(this);
        mBinding.layoutAlipay.setOnClickListener(this);
        mBinding.rbAlipay.setOnCheckedChangeListener((compoundButton, b) -> {
            mBinding.rbAlipay.setChecked(false);
            mBinding.rbWeixin.setChecked(false);
            if (b) {
                mBinding.rbAlipay.setChecked(true);
            }
        });

        mBinding.rbWeixin.setOnCheckedChangeListener((compoundButton, b) -> {
            mBinding.rbAlipay.setChecked(false);
            mBinding.rbWeixin.setChecked(false);
            if (b) {
                mBinding.rbWeixin.setChecked(true);
            }
        });
    }

    public void pay(View view) {
        ViewUnits.getInstance().showLoading(CheckstandActivity.this, "加载中");
        mBinding.btnPay.setEnabled(false);
        mPayPrenInter.getTime(NetCode.Pay.getTime);
    }

    private void weChatOrder() {
        Order order = new Order();
        order.setSellerId(mCardOrder.getWxPay().getSellerId());
        order.setProductDesc(mCardOrder.getProductDesc());
        order.setOrderSubject(mCardOrder.getOrderSubject());
        order.setAmount(mCardOrder.getAmount());
        order.setOutTradeNo(mCardOrder.getOutTrageNo());
        order.setAsyncNotifyUrl(mCardOrder.getWxPay().getAsyncNotifyUrl());
        order.setBizKey(mCardOrder.getWxPay().getBizKey());
        order.setExtendInfo(mCardOrder.getExtendInfo());
        order.setTimeout(mCardOrder.getTimeout());

        mPayPrenInter.greateWeixinOrder(NetCode.Pay.createWeixinPayOrder, cardId, order);
    }

    private void aliOrder() {
        Order order = new Order();
        order.setSellerId(mCardOrder.getAlipay().getSellerId());
        order.setProductDesc(mCardOrder.getProductDesc());
        order.setOrderSubject(mCardOrder.getOrderSubject());
        order.setAmount(mCardOrder.getAmount());
        order.setOutTradeNo(mCardOrder.getOutTrageNo());
        order.setAsyncNotifyUrl(mCardOrder.getAlipay().getAsyncNotifyUrl());
        order.setExtendInfo(mCardOrder.getExtendInfo());
        order.setTimeout(mCardOrder.getTimeout());
        mPayPrenInter.greateAlipayOrder(NetCode.Pay.createAliPayOrder, cardId, order);
    }


    private void wechatPay() {
        WXPayEntryActivity.mOrderNum = mOrder.getOrderNum();
        PayReq request = new PayReq();
        request.appId = mOrder.getAppid();
        request.partnerId = mOrder.getPartnerid();
        request.prepayId = mOrder.getPrepayid();
        request.packageValue = "Sign=WXPay";
        request.nonceStr = mOrder.getNoncestr();
        request.timeStamp = mOrder.getTimestamp();
        request.sign = mOrder.getSign();

        api.registerApp(App.Pay.WX_APP_ID);
        api.sendReq(request);
        mBinding.btnPay.setEnabled(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewUnits.getInstance().missLoading();
            }
        }, 2000);
    }

    private void aliPay() {
        Runnable payRunnable = () -> {
            PayTask alipay = new PayTask(CheckstandActivity.this);

            Message message = new Message();
            message.what = Request.Message.SDK_ALIPAY_FLAG;
            message.obj = alipay.payV2(aliOrder, true);

            mHandler.sendMessage(message);
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Override
    public void onSuccessed(int what, Object object) {
        switch (what) {
            case NetCode.Pay.createAliPayOrder:
                ViewUnits.getInstance().missLoading();
                aliOrder = (String) object;
                aliPay();
                break;
            case NetCode.Pay.AlipayResultNotify:
                Log.e(TAG, mSource.toString());
                runOnUiThread(() -> EventBus.getDefault().post(new PayResultBean(mCallBack,"1","200",mSource)));
                close();
                break;
            case NetCode.Pay.createWeixinPayOrder:
                mOrder = (Order) object;
                wechatPay();
                break;
            case NetCode.Pay.getTime:
                if (mBinding.rbWeixin.isChecked()) {
                    if (mOrder != null) {
                        wechatPay();
                    } else {
                        weChatOrder();
                    }
                } else {
                    if (!TextUtils.isEmpty(aliOrder)) {
                        aliPay();
                    } else {
                        aliOrder();
                    }

                }
                break;
        }
    }

    @Override
    public void onFailed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        ViewUnits.getInstance().showToast(object.toString());
        switch (what) {
            case NetCode.Pay.getTime:
            case NetCode.Pay.createAliPayOrder:
            case NetCode.Pay.createWeixinPayOrder:
                mBinding.btnPay.setEnabled(true);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_weixin:
                mBinding.rbWeixin.setChecked(true);
                mBinding.rbAlipay.setChecked(false);
                break;
            case R.id.layout_alipay:
                mBinding.rbWeixin.setChecked(false);
                mBinding.rbAlipay.setChecked(true);
                break;
        }
    }


    /**
     * 在WXPayEntryActivity中动态设置支付结果
     */
    public void setWeixinPayResult(int errCode, String errStr, boolean result) {
        mBinding.btnPay.setEnabled(true);
        Log.e(TAG, "errCode=" + errCode);
        Log.e(TAG, "errStr=" + errStr);
        try {
            mSource.put("errCode", errCode);
            mSource.put("errStr", FormatUtils.getInstance().isEmpty(errStr) ? "" : errStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (result) {
            runOnUiThread(() -> EventBus.getDefault().post(new PayResultBean(mCallBack,"2","200",mSource)));
        } else {
            runOnUiThread(() -> EventBus.getDefault().post(new PayResultBean(mCallBack,"2","500",mSource)));
        }

        close();

    }

    public void close(){
        mInstance = null;
        finish();
    }


}


