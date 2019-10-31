package tendency.hz.zhihuijiayuan.view.index;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import tendency.hz.zhihuijiayuan.MainActivity;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.AppCardItem;
import tendency.hz.zhihuijiayuan.bean.CardItem;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.bean.base.What;
import tendency.hz.zhihuijiayuan.presenter.CardPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.SetPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.CardPrenInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.SetPrenInter;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.SPUtils;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.card.CardContentActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/4/8.
 */

public class SkipActivity extends BaseActivity implements AllViewInter {
    private static final String TAG = "SkipActivity---";

    private CardPrenInter mCardPrenInter;
    private SetPrenInter mSetPrenInter;

    private CardItem mCardItem;

    private String mCode;
    //邀请码
    private String mInvitationCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skip);

        mCardPrenInter = new CardPrenImpl(this);
        mSetPrenInter = new SetPrenImpl(this);

        startIntent();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //根据拦截的url跳转页面
    private void startIntent() {
        Uri uri = getIntent().getData();
        mCode = uri.getQueryParameter("code");
        mInvitationCode = uri.getQueryParameter("InvitationCode");
        ViewUnits.getInstance().showLoading(this, "加载中");
        mSetPrenInter.cardQrCode(NetCode.Set.cardQrCode, mCode);
    }

    /**
     * 跳转至卡页面（分为应用卡和业务卡）
     */
    private void jumpToCard(CardItem cardItem) {
        Log.e(TAG, "cardTypeId" + cardItem.getCardType());
        if (cardItem.getCardType().equals(What.CardType.businessCard)) {
            Intent intent = new Intent(this, CardContentActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("cardItem", cardItem);
            intent.putExtra("type", Request.StartActivityRspCode.WEB_CARDCONTENT_JUMP);
            startActivity(intent);
            Log.e(TAG, "结束页面");
            finish();
        } else {
            ViewUnits.getInstance().showLoading(this, "加载中");
            mCardPrenInter.getAppCardInfo(NetCode.Card.getAppCardInfo, cardItem.getCardID());
        }
    }

    private void addCard(CardItem cardItem) {
        ViewUnits.getInstance().showLoading(this, "加载中");
        mCardPrenInter.cardAttentionAdd(NetCode.Card.cardAttentionAdd, cardItem);
    }

    @Override
    public void onSuccessed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        switch (what) {
            case NetCode.Set.cardQrCode:
                mCardItem = (CardItem) object;

                if (!TextUtils.isEmpty(mInvitationCode)){
                    SPUtils.getInstance().put(SPUtils.FILE_CARD, SPUtils.cardNo, mCardItem.getCardID());
                    SPUtils.getInstance().put(SPUtils.FILE_CARD, SPUtils.invitationCode, mInvitationCode);
                }
                if ("1".equals(mCardItem.getFocusStatus())) {  //用户已经关注，直接跳转转卡详情页面
                    jumpToCard(mCardItem);
                } else {  //用户未关注，跳转至领卡页面
                    addCard(mCardItem);
                }
                break;
            case NetCode.Card.getAppCardInfo:
                AppCardItem appCardItem = (AppCardItem) object;
                if (BaseUnits.getInstance().isApkInstalled(this, appCardItem.getAndroidAppID())) {  //应用卡，先判断手机是否下载该app
                    //已下载该APP，直接打开APP
                    BaseUnits.getInstance().openAppCard(this, appCardItem.getAndroidAppID());
                } else {
                    //未下载该APP，则先进行下载
                    if (BaseUnits.getInstance().isEmpty(appCardItem.getAndroidDownUrl())) {
                        ViewUnits.getInstance().showToast("该应用卡无安卓版本");
                        return;
                    }
                    BaseUnits.getInstance().loadApk(this, appCardItem.getAndroidDownUrl());
                }
                break;
            case NetCode.Card.anonymousFocus:
            case NetCode.Card.cardAttentionAdd:
                jumpToCard(mCardItem);
                break;
        }
    }

    @Override
    public void onFailed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        ViewUnits.getInstance().showToast(object.toString());
        switch (what) {
            case NetCode.Card.anonymousFocus:
            case NetCode.Card.cardAttentionAdd:
                jumpToCard(mCardItem);
                break;
            default:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCardPrenInter = null;
        mSetPrenInter = null;
        mCardItem = null;
        mCode = null;
        mInvitationCode = null;
    }
}
