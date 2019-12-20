package tendency.hz.zhihuijiayuan.presenter;

import android.text.TextUtils;

import java.util.concurrent.TimeoutException;

import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.model.CardModelImpl;
import tendency.hz.zhihuijiayuan.model.modelInter.CardModelInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.AllPrenInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.CardPrenInter;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.CacheUnits;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.UserUnits;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/3/28.
 */

public class CardPrenImpl implements CardPrenInter, AllPrenInter {
    private static final String TAG = "CardPrenImpl--";
    private AllViewInter mAllViewInter;
    private CardModelInter mCardModelInter;

    public CardPrenImpl(AllViewInter allViewInter) {
        mAllViewInter = allViewInter;
        mCardModelInter = new CardModelImpl(this);
    }

    @Override
    public void findCardList(int netCode, String title, String districtID, String pageIndex) {
        if (netCode == NetCode.Card.findListRefresh) {  //如果重新加载数据，返回缓存好的卡片
            if (CacheUnits.getInstance().getFindCacheCard() != null &&
                    CacheUnits.getInstance().getFindCacheCard().size() > 0) {  //先返回缓存好的卡片
                onSuccess(netCode, CacheUnits.getInstance().getFindCacheCard());
            }
        }

        mCardModelInter.findCardList(netCode, title, districtID, pageIndex);
    }

    @Override
    public void myCardList(int netCode, String serviceTypeID, String pageIndex) {
        if (FormatUtils.getInstance().isEmpty(UserUnits.getInstance().getToken())) {  //用户匿名登录
            mCardModelInter.anonymousList(NetCode.Card2.getAnonymousList, serviceTypeID, pageIndex);
            return;
        }
        mCardModelInter.myCardList(netCode, serviceTypeID, pageIndex);
    }

    @Override
    public void sortCard(int netCode, String cardIds) {
        if (TextUtils.isEmpty(UserUnits.getInstance().getToken())) {  //用户匿名登录
            mCardModelInter.anonymousCardSort(NetCode.Card.anonymousCardSort, cardIds);
            return;
        }
        mCardModelInter.sortCard(NetCode.Card.sortCard, cardIds);
    }

    @Override
    public void cardAttentionAdd(int netCode, Object cardItem) {
        if (FormatUtils.getInstance().isEmpty(UserUnits.getInstance().getToken())) { //token为空，标识用户为匿名登录
            mCardModelInter.anonymousFocus(NetCode.Card.anonymousFocus, cardItem);  //匿名用户，则执行匿名用户关注卡
            return;
        }

        mCardModelInter.cardAttentionAdd(NetCode.Card.cardAttentionAdd, cardItem);  //已登录用户，则这些已登录用户关注卡
    }

    @Override
    public void infoSync(int netCode, String CardIDs) {
        mCardModelInter.infoSync(netCode, CardIDs);
    }

    @Override
    public void previewCard(int netCode, String CardID) {
        mCardModelInter.previewCard(netCode, CardID);
    }

    @Override
    public void deleteCard(int netCode, String ID, String CardID) {
        if (FormatUtils.getInstance().isEmpty(UserUnits.getInstance().getToken())) {  //用户为匿名登录
            mCardModelInter.anonymousCancel(NetCode.Card.anonymousCancel, CardID);
            return;
        }
        mCardModelInter.deleteCard(NetCode.Card.deleteCard, ID, CardID);
    }

    @Override
    public void cardClickVolume(int netCode, String cardId, String distinctId) {
        //记录一次卡片点击(异步执行，不计请求结果)
        if (FormatUtils.getInstance().isEmpty(UserUnits.getInstance().getToken())) {
            mCardModelInter.cardClickVolume(netCode, cardId, BaseUnits.getInstance().getPhoneKey(), distinctId);
        } else {
            mCardModelInter.cardClickVolume(netCode, cardId, "", distinctId);
        }
    }

    @Override
    public void getHisCard(int netCode) {
        mCardModelInter.getHisCard(netCode);
    }

    @Override
    public void getPopularCard(int netCode, String num) {
        mCardModelInter.getPopularCard(netCode, num);
    }

    @Override
    public void anonymousFocus(int netCode, String cardId) {
        mCardModelInter.anonymousFocus(netCode, cardId);
    }

    @Override
    public void anonymousCancel(int netCode, String cardId) {
        mCardModelInter.anonymousCancel(netCode, cardId);
    }

    @Override
    public void recommendCard(int netCode) {
        mCardModelInter.recommendCard(netCode);
    }

    @Override
    public void getAppCardInfo(int netCode, String cardId) {
        mCardModelInter.getAppCardInfo(netCode, cardId);
    }

    @Override
    public void checkCanOperate(int netCode, String cardId) {
        mCardModelInter.checkCanOperate(netCode, cardId);
    }

    @Override
    public void getRecommendCard(int netCode) {
        mCardModelInter.getRecommendCard(netCode);
    }

    @Override
    public void getChoiceCard(int netCode) {
        mCardModelInter.getChoiceCard(netCode);
    }

    @Override
    public void getBanner(int netCode) {
        mCardModelInter.getBanner(netCode);
    }

    @Override
    public void getBannerDetail(int netCode, String id) {
        mCardModelInter.getBannerDetail(netCode, id);
    }

    @Override
    public void authFocusCard(int netCode) {
        mCardModelInter.authFocusCard(netCode);
    }

    @Override
    public void getCardQrCodeUrl(int netCode, String cardId) {
        mCardModelInter.getCardQrCodeUrl(netCode, cardId);
    }

    @Override
    public void getChoiceRecommendCard(int netCode) {
        mCardModelInter.getChoiceRecommendCard(netCode);
    }

    @Override
    public void getChoiceCardTheme(int netCode, String eachCount) {
        mCardModelInter.getChoiceCardTheme(netCode, eachCount);
    }

    @Override
    public void getChoiceSreach(int netCode, String themeVal, String title, String pageIndex) {
        mCardModelInter.getChoiceSreach(netCode, themeVal, title, pageIndex);
    }

    @Override
    public void getCardHotSearch(int netCode) {
        mCardModelInter.getCardHotSearch(netCode);
    }

    @Override
    public void onSuccess(int what, Object object) {
        mAllViewInter.onSuccessed(what, object);
    }

    @Override
    public void onFail(int what, Object object) {
        mAllViewInter.onFailed(what, object);
    }
}
