package tendency.hz.zhihuijiayuan.model;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tendency.hz.zhihuijiayuan.application.MyApplication;
import tendency.hz.zhihuijiayuan.bean.AppCardItem;
import tendency.hz.zhihuijiayuan.bean.Banner;
import tendency.hz.zhihuijiayuan.bean.CardItem;
import tendency.hz.zhihuijiayuan.bean.Theme;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Uri;
import tendency.hz.zhihuijiayuan.bean.base.What;
import tendency.hz.zhihuijiayuan.model.modelInter.AllModelInter;
import tendency.hz.zhihuijiayuan.model.modelInter.CardModelInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.AllPrenInter;
import tendency.hz.zhihuijiayuan.units.AndroidtoJS;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.CacheUnits;
import tendency.hz.zhihuijiayuan.units.ConfigUnits;
import tendency.hz.zhihuijiayuan.units.NoHttpUtil;
import tendency.hz.zhihuijiayuan.units.UserUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.card.CardContentActivity;

/**
 * Created by JasonYao on 2018/3/28.
 */

public class CardModelImpl extends AllModelInter implements CardModelInter {
    private static final String TAG = "CardModelImpl---";
    private AllPrenInter mAllPrenInter;
    private Gson mGson = new Gson();
    private CardItem mCardItem;

    public CardModelImpl(AllPrenInter allPrenInter) {
        mAllPrenInter = allPrenInter;
    }

    @Override
    public void findCardList(int netCode, String title, String districtID, String pageIndex) {
        if (netCode != NetCode.Card.findListRefresh && netCode != NetCode.Card.findListLoad) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("Title", title));
        params.add(new NoHttpUtil.Param("DistrictID", ConfigUnits.getInstance().getCityIdByName(UserUnits.getInstance().getSelectCity())));
        params.add(new NoHttpUtil.Param("PageIndex", pageIndex));
        params.add(new NoHttpUtil.Param("PageSize", What.PAGE_SIZE));
        params.add(new NoHttpUtil.Param("ClientID", BaseUnits.getInstance().getPhoneKey()));

        Log.e(TAG, "设备ID：" + BaseUnits.getInstance().getPhoneKey());
        Log.e(TAG, params.toString());
        NoHttpUtil.post(netCode, Uri.Card.FINDCARDLIST, onResponseListener, params);
    }

    @Override
    public void myCardList(int netCode, String serviceTypeID, String pageIndex) {
        if (netCode != NetCode.Card.myCardListRefresh && netCode != NetCode.Card.myCardListLoad) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("ServiceTypeID", serviceTypeID));
        params.add(new NoHttpUtil.Param("PageIndex", pageIndex));
        params.add(new NoHttpUtil.Param("PageSize", What.PAGE_SIZE));
        params.add(new NoHttpUtil.Param("ClientID", BaseUnits.getInstance().getPhoneKey()));

        NoHttpUtil.get(netCode, Uri.Card.MYCARDLIST, onResponseListener, params);
    }

    @Override
    public void sortCard(int netCode, String cardIds) {
        if (netCode != NetCode.Card.sortCard) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("ClientID", BaseUnits.getInstance().getPhoneKey()));
        params.add(new NoHttpUtil.Param("Order", cardIds));

        NoHttpUtil.post(netCode, Uri.Card.CARD_SORT, onResponseListener, params);
    }

    @Override
    public void anonymousCardSort(int netCode, String cardIds) {
        if (netCode != NetCode.Card.anonymousCardSort) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("ClientID", BaseUnits.getInstance().getPhoneKey()));
        params.add(new NoHttpUtil.Param("Order", cardIds));

        NoHttpUtil.post(netCode, Uri.Card.ANONYMOUS_CARD_SORT, onResponseListener, params);
    }


    @Override
    public void cardAttentionAdd(int netCode, Object object) {
        if (netCode != NetCode.Card.cardAttentionAdd) {
            return;
        }

        mCardItem = (CardItem) object;
        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("CardID", mCardItem.getCardID()));

        NoHttpUtil.get(netCode, Uri.Card.CARDATTENTIONADD, onResponseListener, params);
    }

    @Override
    public void infoSync(int netCode, String CardIDs) {
        if (netCode != NetCode.Card.infoSync) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("CardIDs", CardIDs));

        NoHttpUtil.post(netCode, Uri.Card.INFOSYNC, onResponseListener, params);
    }

    @Override
    public void previewCard(int netCode, String CardID) {
        if (netCode != NetCode.Card.previewCard && netCode != NetCode.Card2.previewCard && netCode != NetCode.Card.openOtherCard) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("CardID", CardID));

        NoHttpUtil.get(netCode, Uri.Card.PREVIEWCARD, onResponseListener, params);
    }

    @Override
    public void deleteCard(int netCode, String ID, String CardID) {
        if (netCode != NetCode.Card.deleteCard) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("ID", ID));
        params.add(new NoHttpUtil.Param("CardID", CardID));

        NoHttpUtil.get(netCode, Uri.Card.DELETECARD, onResponseListener, params);
    }

    @Override
    public void cardClickVolume(int netCode, String cardId, String uniqueID, String distinctId) {
        if (netCode != NetCode.Card.clickVolumeAdd) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("CardID", cardId));
        params.add(new NoHttpUtil.Param("UniqueID", uniqueID));
        params.add(new NoHttpUtil.Param("DistinctID", distinctId));

        NoHttpUtil.post(netCode, Uri.Card.CARDCLICKVOLUME, onResponseListener, params);
    }

    @Override
    public void getHisCard(int netCode) {
        if (netCode != NetCode.Card.getHisCard) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("clientId", BaseUnits.getInstance().getPhoneKey()));
        params.add(new NoHttpUtil.Param("DistrictID", ConfigUnits.getInstance().getCityIdByName(UserUnits.getInstance().getSelectCity())));

        NoHttpUtil.get(netCode, Uri.Card.GETHISCARD, onResponseListener, params);
    }

    @Override
    public void getPopularCard(int netCode, String num) {
        if (netCode != NetCode.Card.getPopularCard) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("num", num));
        params.add(new NoHttpUtil.Param("clientId", BaseUnits.getInstance().getPhoneKey()));
        params.add(new NoHttpUtil.Param("DistrictID", ConfigUnits.getInstance().getCityIdByName(UserUnits.getInstance().getSelectCity())));

        NoHttpUtil.get(netCode, Uri.Card.GETPOPULARCARD, onResponseListener, params);
    }

    @Override
    public void anonymousFocus(int netCode, Object cardItem) {
        if (netCode != NetCode.Card.anonymousFocus) {
            return;
        }

        mCardItem = (CardItem) cardItem;
        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("ClientID", BaseUnits.getInstance().getPhoneKey()));
        params.add(new NoHttpUtil.Param("CardID", mCardItem.getCardID()));

        NoHttpUtil.post(netCode, Uri.Card.ANONYMOUSFOCUS, onResponseListener, params);
    }

    @Override
    public void anonymousCancel(int netCode, String cardId) {
        if (netCode != NetCode.Card.anonymousCancel) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("ClientID", BaseUnits.getInstance().getPhoneKey()));
        params.add(new NoHttpUtil.Param("CardID", cardId));

        NoHttpUtil.post(netCode, Uri.Card.ANONYMOUSCANCEL, onResponseListener, params);
    }

    @Override
    public void recommendCard(int netCode) {
        if (netCode != NetCode.Card.getRecommendCard) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("clientId", BaseUnits.getInstance().getPhoneKey()));
        params.add(new NoHttpUtil.Param("DistrictID", ConfigUnits.getInstance().getCityIdByName(UserUnits.getInstance().getSelectCity())));

        NoHttpUtil.get(netCode, Uri.Card.CARDRECOMMEND, onResponseListener, params);
    }

    @Override
    public void getAppCardInfo(int netCode, String cardId) {
        if (netCode != NetCode.Card.getAppCardInfo) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("cardId", cardId));

        NoHttpUtil.get(netCode, Uri.Card.GETAPPCARDINFO, onResponseListener, params);
    }

    @Override
    public void checkCanOperate(int netCode, String cardId) {
        if (netCode != NetCode.Card2.checkCanOperate) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("cardId", cardId));

        NoHttpUtil.get(netCode, Uri.Card.CHECKCANOPERATE, onResponseListener, params);
    }

    @Override
    public void anonymousList(int netCode, String serviceTypeID, String pageIndex) {
        if (netCode != NetCode.Card2.getAnonymousList) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("ServiceTypeID", serviceTypeID));
        params.add(new NoHttpUtil.Param("PageIndex", pageIndex));
        params.add(new NoHttpUtil.Param("PageSize", What.PAGE_SIZE));
        params.add(new NoHttpUtil.Param("ClientID", BaseUnits.getInstance().getPhoneKey()));

        NoHttpUtil.get(netCode, Uri.Card.MYCARDLISTANONYMOUS, onResponseListener, params);
    }

    @Override
    public void getRecommendCard(int netCode) {
        if (netCode != NetCode.Card2.getRecommandCard) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("ClientID", BaseUnits.getInstance().getPhoneKey()));

        NoHttpUtil.get(netCode, Uri.Card.FORYOUCARD, onResponseListener, params);
    }

    @Override
    public void getChoiceCard(int netCode) {
        if (netCode != NetCode.Card2.getChoiceCard) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("ClientID", BaseUnits.getInstance().getPhoneKey()));
        params.add(new NoHttpUtil.Param("DistrictID", ConfigUnits.getInstance().getCityIdByName(UserUnits.getInstance().getSelectCity())));

        NoHttpUtil.get(netCode, Uri.Card.FORYOUCARD, onResponseListener, params);
    }

    @Override
    public void getBanner(int netCode) {
        if (netCode != NetCode.Card2.getBanner) {
            return;
        }

        NoHttpUtil.get(netCode, Uri.Card.GETAPPBANNER, onResponseListener, null);
    }

    @Override
    public void getBannerDetail(int netCode, String id) {
        if (netCode != NetCode.Card2.getBannerDetail) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("Id", id));

        NoHttpUtil.get(netCode, Uri.Card.GETBANNERDETAIL, onResponseListener, params);
    }

    @Override
    public void authFocusCard(int netCode) {
        if (netCode != NetCode.Card2.autoFocusCard) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("ClientID", BaseUnits.getInstance().getPhoneKey()));
        params.add(new NoHttpUtil.Param("CityID", ConfigUnits.getInstance().getCityIdByName(UserUnits.getInstance().getSelectCity())));
        params.add(new NoHttpUtil.Param("National", "1"));

        Log.e("TAGTAG", params.toString());
        NoHttpUtil.post(netCode, Uri.Card.AUTOFOCUSCARD, onResponseListener, params);
    }

    @Override
    public void getCardQrCodeUrl(int netCode, String cardId) {
        if (netCode != NetCode.Card2.getQrCodeUrl &&
                netCode != NetCode.Card2.getShareQrCodeUrl) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("cardId", cardId));
        NoHttpUtil.get(netCode, Uri.Card.GETQRCODE, onResponseListener, params);
    }

    @Override
    public void getChoiceRecommendCard(int netCode) {
        if (netCode != NetCode.Card2.getChoiceRecommend) {
            return;
        }

        Log.e(TAG, "当前定位城市----------------->" + ConfigUnits.getInstance().getCityIdByName(UserUnits.getInstance().getSelectCity()));
        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("Num", "6"));
        params.add(new NoHttpUtil.Param("ClientID", BaseUnits.getInstance().getPhoneKey()));
        params.add(new NoHttpUtil.Param("DistrictID", ConfigUnits.getInstance().getCityIdByName(UserUnits.getInstance().getSelectCity())));

        NoHttpUtil.get(netCode, Uri.Card.CHOICECARDRECOMMEND, onResponseListener, params);
    }

    @Override
    public void getChoiceCardTheme(int netCode, String eachCount) {
        if (netCode != NetCode.Card2.getChoiceCardTheme) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("EachCount", eachCount));
        params.add(new NoHttpUtil.Param("DistrictID", ConfigUnits.getInstance().getCityIdByName(UserUnits.getInstance().getSelectCity())));
        params.add(new NoHttpUtil.Param("IsContainsNull", "1"));

        NoHttpUtil.get(netCode, Uri.Card.CHOICECARDTHEME, onResponseListener, params);
    }

    @Override
    public void getChoiceSreach(int netCode, String themeVal, String title, String pageIndex) {
        if (netCode != NetCode.Card.findListRefresh && netCode != NetCode.Card.findListLoad) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("ThemeVal", themeVal));
        params.add(new NoHttpUtil.Param("Title", title));
        params.add(new NoHttpUtil.Param("DistrictID", ConfigUnits.getInstance().getCityIdByName(UserUnits.getInstance().getSelectCity())));
        params.add(new NoHttpUtil.Param("ClientID", BaseUnits.getInstance().getPhoneKey()));
        params.add(new NoHttpUtil.Param("IsContainsNull", "1"));
        params.add(new NoHttpUtil.Param("PageIndex", pageIndex));
        params.add(new NoHttpUtil.Param("PageSize", "1000"));

        Log.e(TAG, "获取卡片" + params.toString());
        NoHttpUtil.get(netCode, Uri.Card.CHOICECARDSEARCH, onResponseListener, params);
    }

    @Override
    public void getCardHotSearch(int netCode) {
        if (netCode != NetCode.Card2.getCardHotSearch) {
            return;
        }

        NoHttpUtil.get(netCode, Uri.Card.GETCARDHOTSEARCH, onResponseListener, null);
    }

    @Override
    public void rspSuccess(int what, Object object) throws JSONException {
        switch (what) {
            case NetCode.Card.findListRefresh:  //刷新数据
                JSONArray jsaFindListRefresh = ((JSONObject) object).getJSONObject("Data").getJSONArray("List");
                List<CardItem> findCards = mGson.fromJson(jsaFindListRefresh.toString(), new TypeToken<List<CardItem>>() {
                }.getType());

                //数据请求成功，先缓存数据
                CacheUnits.getInstance().clearFindCacheCard();   //先清空缓存的数据
                CacheUnits.getInstance().insertFindCacheCard(findCards); //添加缓存数据

                mAllPrenInter.onSuccess(what, findCards);
                break;
            case NetCode.Card.findListLoad: //加载更多数据
                JSONArray jsaFindListLoad = ((JSONObject) object).getJSONObject("Data").getJSONArray("List");
                List<CardItem> loadCards = mGson.fromJson(jsaFindListLoad.toString(), new TypeToken<List<CardItem>>() {
                }.getType());

                //数据请求成功，先缓存数据
                CacheUnits.getInstance().insertFindCacheCard(loadCards); //添加缓存数据

                mAllPrenInter.onSuccess(what, loadCards);
                break;
            case NetCode.Card.myCardListRefresh:
            case NetCode.Card.myCardListLoad:
                JSONArray jsaMyCardList = ((JSONObject) object).getJSONObject("Data").getJSONArray("List");
                List<CardItem> cardItems = mGson.fromJson(jsaMyCardList.toString(), new TypeToken<List<CardItem>>() {
                }.getType());


                CacheUnits.getInstance().deleteMyCacheCard();
                CacheUnits.getInstance().insertMyCacheCards(cardItems);
                mAllPrenInter.onSuccess(what, cardItems);
                break;
            case NetCode.Card2.getAnonymousList:
                JSONArray jsaMyCardList2 = ((JSONObject) object).getJSONObject("Data").getJSONArray("List");
                List<CardItem> cardItems2 = mGson.fromJson(jsaMyCardList2.toString(), new TypeToken<List<CardItem>>() {
                }.getType());

                CacheUnits.getInstance().deleteMyCacheCard();
                CacheUnits.getInstance().insertMyCacheCards(cardItems2);
                mAllPrenInter.onSuccess(what, cardItems2);
                break;
            case NetCode.Card.sortCard:
                break;
            case NetCode.Card.cardAttentionAdd:
                mAllPrenInter.onSuccess(what, mCardItem);
                break;
            case NetCode.Card.infoSync:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.Card.openOtherCard:
            case NetCode.Card.previewCard:
            case NetCode.Card2.previewCard:
                JSONObject jsoCardItem = ((JSONObject) object).getJSONObject("Data");
                CardItem cardItem = mGson.fromJson(jsoCardItem.toString(), CardItem.class);
                mAllPrenInter.onSuccess(what, cardItem);
                break;
            case NetCode.Card.deleteCard:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.Card.getHisCard:
            case NetCode.Card.getPopularCard:
                JSONArray jsaCards = ((JSONObject) object).getJSONArray("Data");
                List<CardItem> hisAndrPopCards = mGson.fromJson(jsaCards.toString(), new TypeToken<List<CardItem>>() {
                }.getType());

                mAllPrenInter.onSuccess(what, hisAndrPopCards);
                break;
            case NetCode.Card.anonymousFocus:
                mAllPrenInter.onSuccess(what, mCardItem);
            case NetCode.Card.anonymousCancel:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.Card.getRecommendCard:
                JSONArray jsarecommend = ((JSONObject) object).getJSONArray("Data");
                List<CardItem> recommendCards = mGson.fromJson(jsarecommend.toString(), new TypeToken<List<CardItem>>() {
                }.getType());

                mAllPrenInter.onSuccess(what, recommendCards);
                break;
            case NetCode.Card.getAppCardInfo:
                JSONObject jsoAppCardInfo = ((JSONObject) object).getJSONObject("Data");
                AppCardItem appCardItem = mGson.fromJson(jsoAppCardInfo.toString(), AppCardItem.class);
                mAllPrenInter.onSuccess(what, appCardItem);
                break;
            case NetCode.Card2.checkCanOperate:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.Card2.getRecommandCard:
            case NetCode.Card2.getChoiceCard:
                JSONArray jsoForYouCard = ((JSONObject) object).getJSONArray("Data");
                List<CardItem> cardItemsForYou = mGson.fromJson(jsoForYouCard.toString(), new TypeToken<List<CardItem>>() {
                }.getType());
                mAllPrenInter.onSuccess(what, cardItemsForYou);
                break;
            case NetCode.Card2.getBanner:
                JSONArray jsaBanners = ((JSONObject) object).getJSONArray("Data");
                List<Banner> banners = mGson.fromJson(jsaBanners.toString(), new TypeToken<List<Banner>>() {
                }.getType());
                mAllPrenInter.onSuccess(what, banners);
                break;
            case NetCode.Card2.getBannerDetail:
                JSONObject jsoBannerDetal = ((JSONObject) object).getJSONObject("Data");
                Banner banner = mGson.fromJson(jsoBannerDetal.toString(), Banner.class);
                mAllPrenInter.onSuccess(what, banner);
                break;
            case NetCode.Card2.autoFocusCard:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.Card2.getShareQrCodeUrl:
            case NetCode.Card2.getQrCodeUrl:
                mAllPrenInter.onSuccess(what, ((JSONObject) object).getString("Data"));
                break;
            case NetCode.Card2.getChoiceRecommend:
                JSONArray jsoRecommend = ((JSONObject) object).getJSONArray("Data");
                List<CardItem> cardItemsRecommend = mGson.fromJson(jsoRecommend.toString(), new TypeToken<List<CardItem>>() {
                }.getType());
                mAllPrenInter.onSuccess(what, cardItemsRecommend);
                break;
            case NetCode.Card2.getChoiceCardTheme:
                JSONArray jsoTheme = ((JSONObject) object).getJSONArray("Data");
                List<Theme> themes = mGson.fromJson(jsoTheme.toString(), new TypeToken<List<Theme>>() {
                }.getType());
                mAllPrenInter.onSuccess(what, themes);
                break;
            case NetCode.Card2.getChoiceCardSearch:
                break;
            case NetCode.Card2.getCardHotSearch:
                JSONArray jsaHotSearch = ((JSONObject) object).getJSONArray("Data");
                List<CardItem> hotCardItems = mGson.fromJson(jsaHotSearch.toString(), new TypeToken<List<CardItem>>() {
                }.getType());
                mAllPrenInter.onSuccess(what, hotCardItems);
                break;
        }
    }

    @Override
    public void rspFailed(int what, Object object) {
        mAllPrenInter.onFail(what, object);
    }
}
