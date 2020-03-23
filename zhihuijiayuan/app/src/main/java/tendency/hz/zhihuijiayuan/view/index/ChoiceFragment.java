package tendency.hz.zhihuijiayuan.view.index;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjt2325.cameralibrary.util.LogUtil;
import com.youth.banner.Transformer;
import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.bannerview.constants.IndicatorSlideMode;
import com.zhpan.bannerview.holder.HolderCreator;
import com.zhpan.bannerview.holder.ViewHolder;

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
import tendency.hz.zhihuijiayuan.databinding.FragmentChoiceBinding;
import tendency.hz.zhihuijiayuan.inter.PopWindowOnClickInter;
import tendency.hz.zhihuijiayuan.presenter.CardPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.CardPrenInter;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.LogUtils;
import tendency.hz.zhihuijiayuan.units.UserUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.card.BannerDetailsActivity;
import tendency.hz.zhihuijiayuan.view.card.CardContentActivity;
import tendency.hz.zhihuijiayuan.view.card.SearchCardActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;
import tendency.hz.zhihuijiayuan.widget.NetViewHolder;
import uk.co.imallan.jellyrefresh.PullToRefreshLayout;

/**
 * Created by JasonYao on 2018/11/14.
 */
public class ChoiceFragment extends Fragment implements AllViewInter {
    private FragmentChoiceBinding mBinding;
    private List<CardItem> mCardsRecommend = new ArrayList<>();
    private List<Theme> mTheme = new ArrayList<>();
    private List<Banner> mBanners = new ArrayList<>();
    private List<String> mBannersUri = new ArrayList<>();

    private ChoiceCardRecyclerAdapter mRecommendAdapter;
    private ThemeRecyclerAdapter mThemeAdapter;
    private LinearLayoutManager mManager1, mManager2;

    private CardPrenInter mCardPrenInter;

    private CardItem mCardItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_choice, container, false);
        ViewUnits.getInstance().setTitleHeight(mBinding.layoutTitle);

        mCardPrenInter = new CardPrenImpl(this);

        initView();

        setListener();

        initData();

        return mBinding.getRoot();
    }

    public void updateData() {
        initData();
    }

    private void initData() {
        mCardPrenInter.getChoiceRecommendCard(NetCode.Card2.getChoiceRecommend);
        mCardPrenInter.getBanner(NetCode.Card2.getBanner);
    }

    private void initView() {
        mManager1 = new LinearLayoutManager(getActivity());
        mManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        mManager2 = new LinearLayoutManager(getActivity());
        mManager2.setOrientation(LinearLayoutManager.VERTICAL);


        mBinding.bannerView
                .setHolderCreator(new HolderCreator() {
                    @Override
                    public ViewHolder createViewHolder() {
                        return new NetViewHolder();
                    }
                })

                .setOnPageClickListener(new BannerViewPager.OnPageClickListener() {
                    @Override
                    public void onPageClick(int position) {
                        Intent intent1 = new Intent(getActivity(), BannerDetailsActivity.class);
                        intent1.putExtra("img", mBanners.get(position).getInnerImg());
                        startActivity(intent1);
                    }
                });

        mRecommendAdapter = new ChoiceCardRecyclerAdapter(mCardsRecommend, getActivity());
        mThemeAdapter = new ThemeRecyclerAdapter(getActivity(), mTheme);

        mBinding.recyclerRecommend.setLayoutManager(mManager1);
        mBinding.recyclerTheme.setLayoutManager(mManager2);
        mBinding.recyclerRecommend.setAdapter(mRecommendAdapter);
        mBinding.recyclerTheme.setAdapter(mThemeAdapter);

        mBinding.jellyRefreshChoice.setPullToRefreshListener(new PullToRefreshLayout.PullToRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                pullToRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBinding.jellyRefreshChoice.setRefreshing(false);
                    }
                }, 500);
            }
        });
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
            Intent intent = new Intent(getActivity(), SearchCardActivity.class);
            intent.putExtra("ThemeVal", mTheme.get(position).getThemeVal());
            startActivity(intent);
        });

        mBinding.layoutGoSreach.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), SearchCardActivity.class);
            intent.putExtra("ThemeVal", "");
            startActivity(intent);
        });
    }

    /**
     * 跳转至卡页面（分为应用卡和业务卡）
     */
    private void jumpToCard(CardItem cardItem) {
        if (cardItem.getCardType().equals(What.CardType.businessCard)) {
            Intent intent = new Intent(getActivity(), CardContentActivity.class);
            intent.putExtra("cardItem", cardItem);
            startActivity(intent);
        } else {
            ViewUnits.getInstance().showLoading(getActivity(), "请稍等");
            mCardPrenInter.getAppCardInfo(NetCode.Card.getAppCardInfo, cardItem.getCardID());
        }
    }

    /**
     * 删除卡片
     *
     * @param cardItem
     */
    private void deleteItem(CardItem cardItem) {
        ViewUnits.getInstance().showLoading(getActivity(), "删除中");
        if (FormatUtils.getInstance().isEmpty(UserUnits.getInstance().getToken())) {  //用户为匿名登录
            mCardPrenInter.anonymousCancel(NetCode.Card.anonymousCancel, cardItem.getCardID());
        } else {  //用户实名登录，删除服务器后台数据
            mCardPrenInter.deleteCard(NetCode.Card.deleteCard, cardItem.getID(), cardItem.getCardID());
        }
    }

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
                    } else {
                        mBinding.layoutRecommend.setVisibility(View.GONE);
                    }
                }
                break;
            case NetCode.Card2.checkCanOperate:
                jumpToCard(mCardItem);
//                mCardPrenInter.cardAttentionAdd(NetCode.Card.cardAttentionAdd, mCardItem);
                break;
//            case NetCode.Card.anonymousFocus:
//            case NetCode.Card.cardAttentionAdd:
//                jumpToCard(mCardItem);
//                break;
            case NetCode.Card.getAppCardInfo:
                AppCardItem appCardItem = (AppCardItem) object;
                if (BaseUnits.getInstance().isApkInstalled(getActivity(), appCardItem.getAndroidAppID())) {  //应用卡，先判断手机是否下载该app
                    //已下载该APP，直接打开APP
                    BaseUnits.getInstance().openAppCard(getActivity(), appCardItem.getAndroidAppID());
                } else {
                    //未下载该APP，则先进行下载
                    if (BaseUnits.getInstance().isEmpty(appCardItem.getAndroidDownUrl())) {
                        ViewUnits.getInstance().showToast("该应用卡无安卓版本");
                        return;
                    }
                    BaseUnits.getInstance().loadApk(getActivity(), appCardItem.getAndroidDownUrl());
                }
                break;
            case NetCode.Card2.getBanner:
                mBannersUri.clear();
                mBanners.clear();
                mBanners.addAll((List<Banner>) object);
                for (Banner banner : mBanners) {
                    mBannersUri.add(banner.getOuterImg());
                }

                mBinding.bannerView.create(mBannersUri);
                break;
            case NetCode.Card.anonymousCancel:
            case NetCode.Card.deleteCard:
                initData();
                break;
        }
    }

    @Override
    public void onFailed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        switch (what) {
            case NetCode.Card.getRecommendCard:
                mCardPrenInter.getChoiceCardTheme(NetCode.Card2.getChoiceCardTheme, "6");
            case NetCode.Card2.checkCanOperate:
                if (object.equals("网络错误!!")) {
                    ViewUnits.getInstance().showToast(object.toString());
                    return;
                }

                if (getActivity() !=null && !getActivity().isFinishing()){
                    ViewUnits.getInstance().showPopWindow(getActivity(), getActivity().getWindow().peekDecorView(), object.toString() + "\n是否移除该卡片",
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
                }

                break;
//            case NetCode.Card.anonymousFocus:
//            case NetCode.Card.cardAttentionAdd:
//                jumpToCard(mCardItem);
//                break;
            default:
                ViewUnits.getInstance().showToast(object.toString());
                break;
        }
    }
}
