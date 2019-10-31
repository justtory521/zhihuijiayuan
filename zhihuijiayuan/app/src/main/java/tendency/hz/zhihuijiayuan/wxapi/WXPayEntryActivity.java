package tendency.hz.zhihuijiayuan.wxapi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import tendency.hz.zhihuijiayuan.bean.base.App;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.presenter.PayPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PayPrenInter;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.picker.CheckstandActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler, AllViewInter {
    private static final String TAG = "WXPayEntryActivity---";
    private IWXAPI api;

    private PayPrenInter mPayPrenInter;

    public static String mOrderNum;

    private int errCode;
    private String errStr;

    private boolean first = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPayPrenInter = new PayPrenImpl(this);

        api = WXAPIFactory.createWXAPI(this, App.Pay.WX_APP_ID, false);
        try {
            if (!api.handleIntent(getIntent(), this)) {
                finish();
                return;
            }
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e(TAG, api.handleIntent(getIntent(), this) + "");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                Log.e(TAG, "COMMAND_GETMESSAGE_FROM_WX");
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                Log.e(TAG, "COMMAND_SHOWMESSAGE_FROM_WX");
                break;
            default:
                Log.e(TAG, req.toString());
                break;
        }
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e(TAG,resp.toString());
        if (!first) {
            return;
        }
        errCode = resp.errCode;
        errStr = resp.errStr;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                mPayPrenInter.weiXinResultNotify(NetCode.Pay.weixinResultNotify, mOrderNum, "2", BaseUnits.getInstance().getSerilNumByLength(20));
                break;
            default:
                CheckstandActivity.mInstance.setWeixinPayResult(resp.errCode, resp.errStr, false);
                finish();
        }

        first = !first;
    }

    @Override
    public void onSuccessed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        if (what == NetCode.Pay.weixinResultNotify) {
            first = !first;
            CheckstandActivity.mInstance.setWeixinPayResult(errCode, errStr, true);
            finish();
        }
    }

    @Override
    public void onFailed(int what, Object object) {
        first = !first;
        ViewUnits.getInstance().missLoading();
        
        ViewUnits.getInstance().showToast("支付失败，请联系客服");
        finish();
    }
}