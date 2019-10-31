package tendency.hz.zhihuijiayuan.view.card;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.adapter.ChoiceCardRecyclerAdapter;
import tendency.hz.zhihuijiayuan.adapter.GlideImageLoader;
import tendency.hz.zhihuijiayuan.adapter.ThemeRecyclerAdapter;
import tendency.hz.zhihuijiayuan.bean.AppCardItem;
import tendency.hz.zhihuijiayuan.bean.Banner;
import tendency.hz.zhihuijiayuan.bean.CardItem;
import tendency.hz.zhihuijiayuan.bean.Theme;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.What;
import tendency.hz.zhihuijiayuan.databinding.ActivityChoiceCardBinding;
import tendency.hz.zhihuijiayuan.inter.PopWindowOnClickInter;
import tendency.hz.zhihuijiayuan.presenter.CardPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.CardPrenInter;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.UserUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/9/28.
 */
public class ChoiceCardActivity extends BaseActivity implements AllViewInter {
    private static final String TAG = "ChoiceCardActivity--";
    private ActivityChoiceCardBinding mBinding;
    private List<CardItem> mCardsRecommend = new ArrayList<>();
    private List<Theme> mTheme = new ArrayList<>();
    private List<Banner> mBanners = new ArrayList<>();
    private List<String> mBannersUri = new ArrayList<>();

    private ChoiceCardRecyclerAdapter mRecommendAdapter;
    private ThemeRecyclerAdapter mThemeAdapter;
    private LinearLayoutManager mManager1, mManager2;

    private CardPrenInter mCardPrenInter;
    private CardItem mCardItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_choice_card);

        mCardPrenInter = new CardPrenImpl(this);

        initView();

        setListener();

        initData();
    }

    private void initData() {
        mCardPrenInter.getChoiceRecommendCard(NetCode.Card2.getChoiceRecommend);
        mCardPrenInter.getBanner(NetCode.Card2.getBanner);
    }

    private void initView() {
        mManager1 = new LinearLayoutManager(this);
        mManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        mManager2 = new LinearLayoutManager(this);
        mManager2.setOrientation(LinearLayoutManager.VERTICAL);

        mBinding.banner.setImageLoader(new GlideImageLoader());
        mBinding.banner.setOnBannerListener(position -> {
            Intent intent1 = new Intent(ChoiceCardActivity.this, BannerDetailsActivity.class);
            intent1.putExtra("img", mBanners.get(position).getInnerImg());
            startActivity(intent1);
        });

        mRecommendAdapter = new ChoiceCardRecyclerAdapter(mCardsRecommend, this);
        mThemeAdapter = new ThemeRecyclerAdapter(this, mTheme);

        mBinding.recyclerRecommend.setLayoutManager(mManager1);
        mBinding.recyclerTheme.setLayoutManager(mManager2);
        mBinding.recyclerRecommend.setAdapter(mRecommendAdapter);
        mBinding.recyclerTheme.setAdapter(mThemeAdapter);
    }

    private void setListener() {
        mRecommendAdapter.setLisntener((view, postion) -> {
            mCardItem = mCardsRecommend.get(postion);
            mCardPrenInter.checkCanOperate(NetCode.Card2.checkCanOperate, mCardItem.getCardID());
        });

        mThemeAdapter.setListener(cardItem -> {
            mCardItem = cardItem;
            mCardPrenInter.checkCanOperate(NetCode.Card2.checkCanOperate, mCardItem.getCardID());
        });

        mThemeAdapter.setGoSreachListener((view, position) -> {
            Intent intent = new Intent(ChoiceCardActivity.this, SearchCardActivity.class);
            intent.putExtra("ThemeVal", mTheme.get(position).getThemeVal());
            startActivity(intent);
        });
    }

    public void goSreachCard(View view) {
        Intent intent = new Intent(this, SearchCardActivity.class);
        intent.putExtra("ThemeVal", "");
        startActivity(intent);
    }

    /**
     * 跳转至卡页面（分为应用卡和业务卡）
     */
    private void jumpToCard(CardItem cardItem) {
        Log.e(TAG, "cardItem" + cardItem.toString());
        if (cardItem.getCardType().equals(What.CardType.businessCard)) {
            Intent intent = new Intent(ChoiceCardActivity.this, CardContentActivity.class);
            intent.putExtra("cardItem", cardItem);
            ChoiceCardActivity.this.startActivity(intent);
        } else {
            ViewUnits.getInstance().showLoading(this, "请稍等");
            mCardPrenInter.getAppCardInfo(NetCode.Card.getAppCardInfo, cardItem.getCardID());
        }
    }

    /**
     * 删除卡片
     *
     * @param cardItem
     */
    private void deleteItem(CardItem cardItem) {
        ViewUnits.getInstance().showLoading(this, "删除中");
        if (FormatUtils.getInstance().isEmpty(UserUnits.getInstance().getToken())) {  //用户为匿名登录
            mCardPrenInter.anonymousCancel(NetCode.Card.anonymousCancel, cardItem.getCardID());
        } else {  //用户实名登录，删除服务器后台数据
            mCardPrenInter.deleteCard(NetCode.Card.deleteCard, cardItem.getID(), cardItem.getCardID());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onSuccessed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        switch (what) {
            case NetCode.Card2.getChoiceRecommend:
                List<CardItem> cardItemsRecommand = (List<CardItem>) object;
                mCardsRecommend.clear();
                mCardsRecommend.addAll(cardItemsRecommand);
                mRecommendAdapter.notifyDataSetChanged();
                if (mCardsRecommend.size() == 0) {
                    mBinding.layoutNoRecommend.setVisibility(View.VISIBLE);
                } else {
                    mBinding.layoutNoRecommend.setVisibility(View.GONE);
                }
                mCardPrenInter.getChoiceCardTheme(NetCode.Card2.getChoiceCardTheme, "6");

                break;
            case NetCode.Card2.getChoiceCardTheme:
                List<Theme> cardItemsChoice = (List<Theme>) object;
                mTheme.clear();
                mTheme.addAll(cardItemsChoice);
                mThemeAdapter.notifyDataSetChanged();
                if (mTheme.size() == 0) {
                    mBinding.layoutRecommend.setVisibility(View.VISIBLE);
                } else {
                    if (mCardsRecommend.size() != 0) {
                        mBinding.layoutRecommend.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case NetCode.Card2.checkCanOperate:
                Log.e(TAG, "当前卡片可用");
                mCardPrenInter.cardAttentionAdd(NetCode.Card.cardAttentionAdd, mCardItem);
                break;
            case NetCode.Card.anonymousFocus:
            case NetCode.Card.cardAttentionAdd:
                jumpToCard(mCardItem);
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
            case NetCode.Card2.getBanner:
                mBannersUri.clear();
                mBanners.clear();
                mBanners.addAll((List<Banner>) object);
                for (Banner banner : mBanners) {
                    mBannersUri.add(banner.getOuterImg());
                }
                mBinding.banner.setImages(mBannersUri);
                mBinding.banner.start();
                break;
            case NetCode.Card.deleteCard:
                initData();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onFailed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        switch (what) {
            case NetCode.Card.getRecommendCard:
                mCardPrenInter.getChoiceCardTheme(NetCode.Card2.getChoiceCardTheme, "6");
            case NetCode.Card2.checkCanOperate:
                Log.e(TAG, "卡片不能用：" + object.toString());
                if (object.equals("网络错误!!")) {
                    ViewUnits.getInstance().showToast(object.toString());
                    return;
                }
                ViewUnits.getInstance().showPopWindow(this, this.getWindow().peekDecorView(), object.toString() + "\n是否移除该卡片",
                        new PopWindowOnClickInter() {
                            @Override
                            public void leftBtnOnClick() {
                                ViewUnits.getInstance().missPopView();
                            }

                            @Override
                            public void rightBtnOnClick() {
                                ViewUnits.getInstance().missPopView();
                                deleteItem(mCardItem);
                            }
                        });
                break;
            case NetCode.Card.anonymousFocus:
            case NetCode.Card.cardAttentionAdd:
                jumpToCard(mCardItem);
                break;
            default:
                ViewUnits.getInstance().showToast(object.toString());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCardsRecommend = null;
        mTheme = null;
        mBanners = null;
        mBannersUri = null;
        mRecommendAdapter = null;
        mThemeAdapter = null;
        mManager1 = null;
        mManager2 = null;
        mCardPrenInter = null;
        mCardItem = null;
    }
}
