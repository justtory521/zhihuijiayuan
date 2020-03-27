package tendency.hz.zhihuijiayuan.wxapi;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import tendency.hz.zhihuijiayuan.MainActivity;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.application.MyApplication;
import tendency.hz.zhihuijiayuan.bean.EventBusBean;
import tendency.hz.zhihuijiayuan.bean.base.App;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.presenter.SetPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.SetPrenInter;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.LogUtils;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler, AllViewInter {
    private static final String TAG = "libin";
    private IWXAPI api;
    private SetPrenInter mSetPrenInter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSetPrenInter = new SetPrenImpl(this);
        api = WXAPIFactory.createWXAPI(this, App.Pay.WX_APP_ID, true);
        try {

            if (!api.handleIntent(getIntent(), this)) {
                finish();
                return;
            }
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        if (ConstantsAPI.COMMAND_SUBSCRIBE_MESSAGE == resp.getType()) {
            if (resp.errCode == BaseResp.ErrCode.ERR_OK && !TextUtils.isEmpty(resp.openId)) {
                mSetPrenInter.WXSubscribeMessage(NetCode.Set.wxSubscribeMessage, resp.openId, "5SwMB1T-YD1xPxii3-MJ-UhGwa1urczBzUvSMxTgI2w", 999 + "");
            } else {
                EventBus.getDefault().post("wechat_resp");
            }
            finish();
        }

    }


    @Override
    public void onSuccessed(int what, Object object) {

    }

    @Override
    public void onFailed(int what, Object object) {

    }
}