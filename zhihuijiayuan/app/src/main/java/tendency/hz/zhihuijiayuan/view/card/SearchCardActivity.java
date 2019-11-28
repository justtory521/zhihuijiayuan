package tendency.hz.zhihuijiayuan.view.card;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.List;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.adapter.SreachCardRecyclerAdapter;
import tendency.hz.zhihuijiayuan.adapter.holder.inter.SearchCardItemOnClickInter;
import tendency.hz.zhihuijiayuan.bean.AppCardItem;
import tendency.hz.zhihuijiayuan.bean.CardItem;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.What;
import tendency.hz.zhihuijiayuan.databinding.ActivitySearchCardBinding;
import tendency.hz.zhihuijiayuan.listener.MainUMShareListener;
import tendency.hz.zhihuijiayuan.presenter.CardPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.CardPrenInter;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.QrCodeUnits;
import tendency.hz.zhihuijiayuan.units.ScreenUtils;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;
import tendency.hz.zhihuijiayuan.widget.ClassicsHeader;
import tendency.hz.zhihuijiayuan.widget.RotateXAnimation;

/**
 * Created by JasonYao on 2018/4/12.
 */

public class SearchCardActivity extends BaseActivity implements AllViewInter {
    private String TAG = "SearchCardActivity---";

    private List<CardItem> mList = new ArrayList<>();
    private int mPage = 1;
    private int mIntVisibleItem = 0;
    private boolean isLoading = false;  //当前是否正在加载数据
    private CardItem mCardItem;

    private SreachCardRecyclerAdapter mAdapter;
    private LinearLayoutManager mManager;

    private CardPrenInter mCardPrenInter;

    private ActivitySearchCardBinding mBinding;

    private String mThemeVal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_card);
        ViewUnits.getInstance().setTitleHeight(mBinding.layoutTitle);

        initData();

        initView();

        setListener();

        isLoading = true;
        mPage = 1;
        mCardPrenInter.getChoiceSreach(NetCode.Card.findListRefresh, mThemeVal, "", mPage + "");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initData() {
        mThemeVal = getIntent().getStringExtra("ThemeVal");
        mCardPrenInter = new CardPrenImpl(this);
        mAdapter = new SreachCardRecyclerAdapter(this, mList);
    }

    private void initView() {
        mManager = new LinearLayoutManager(this);
        mBinding.recyclerCardFind.setLayoutManager(mManager);
        mBinding.recyclerCardFind.setAdapter(mAdapter);
        mBinding.swipeRefresh.setEnableLoadMore(false);
    }

    /**
     * 跳转至卡页面（分为应用卡和业务卡）
     */
    private void jumpToCard(CardItem cardItem) {
        if (cardItem.getCardType().equals(What.CardType.businessCard)) {
            Intent intent = new Intent(SearchCardActivity.this, CardContentActivity.class);
            intent.putExtra("cardItem", cardItem);
            SearchCardActivity.this.startActivity(intent);
        } else {
            ViewUnits.getInstance().showLoading(this, "请稍等");
            mCardPrenInter.getAppCardInfo(NetCode.Card.getAppCardInfo, cardItem.getCardID());
        }
    }

    private void setListener() {
        mBinding.recyclerCardFind.addOnScrollListener(new MyOnScrollListener());
        mAdapter.setListener(new SearchCardItemOnClickInter() {
            @Override
            public void onItemOnClick(View view, int postion) {
                mCardItem = mList.get(postion);

                jumpToCard(mCardItem);
//                mCardPrenInter.cardAttentionAdd(NetCode.Card.cardAttentionAdd, mList.get(postion));
            }

            @Override
            public void onQrCodeOnClick(View view, int position) {
                ViewUnits.getInstance().showLoading(SearchCardActivity.this, "请求中");
                mCardPrenInter.getCardQrCodeUrl(NetCode.Card2.getQrCodeUrl, mList.get(position).getCardID());
            }

            @Override
            public void onShareOnClick(View view, int position) {
                ViewUnits.getInstance().showLoading(SearchCardActivity.this, "请求中");
                mCardPrenInter.getCardQrCodeUrl(NetCode.Card2.getShareQrCodeUrl, mList.get(position).getCardID());
            }

        });

        mBinding.layoutToQuery.setOnClickListener(view -> {
            Intent intent = new Intent(this, QueryCardActivity.class);
            intent.putExtra("ThemeVal", mThemeVal);
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, mBinding.layoutToQuery, "querylayout");
            startActivity(intent, optionsCompat.toBundle());
        });

        mBinding.edtSreachQuery.setOnClickListener(view -> {
            Intent intent = new Intent(SearchCardActivity.this, QueryCardActivity.class);
            intent.putExtra("ThemeVal", mThemeVal);
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(SearchCardActivity.this, mBinding.layoutToQuery, "querylayout");
            startActivity(intent, optionsCompat.toBundle());
        });

        mBinding.swipeRefresh.setOnRefreshListener(refreshLayout -> {
            if (refreshLayout.getState() == RefreshState.Refreshing) {  //表示正在刷新
                mPage = 1;
                mCardPrenInter.getChoiceSreach(NetCode.Card.findListRefresh, mThemeVal, "", mPage + "");
            }
        });

    }

    /**
     * rv入场动画
     */
    private void enterAnimation(){
        mBinding.recyclerCardFind.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        mBinding.recyclerCardFind.getViewTreeObserver().removeOnPreDrawListener(this);
                        for (int i = 0; i <  mBinding.recyclerCardFind.getChildCount(); i++) {
                            View v =  mBinding.recyclerCardFind.getChildAt(i);

                            RotateXAnimation rotateXAnimation = new RotateXAnimation(v.getWidth()/2,v.getHeight()/2);
                            rotateXAnimation.setDuration(400);
                            rotateXAnimation.setStartOffset(100*i);
                            v.setAnimation(rotateXAnimation);

                        }

                        return true;
                    }
                });
    }

    private class MyOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                    mIntVisibleItem + 1 >= mList.size()) {
                if (!isLoading) {
                    mAdapter.setHasMore(true);
                    mAdapter.notifyDataSetChanged();
                    enterAnimation();
                    isLoading = true;
                    mPage++;
                    new Handler().postDelayed(() -> mCardPrenInter.getChoiceSreach(NetCode.Card.findListLoad, mThemeVal, "", mPage + ""), 100);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mIntVisibleItem = mManager.findLastCompletelyVisibleItemPosition();
        }
    }

    @Override
    public void onSuccessed(int what, Object object) {
        isLoading = false;
        ViewUnits.getInstance().missLoading();
        switch (what) {
            case NetCode.Card.findListRefresh:
                mBinding.swipeRefresh.finishRefresh();
                mBinding.imgLoading.setVisibility(View.GONE);
                mList.clear();
                if (object != null) {
                    mList.addAll((List<CardItem>) object);
                }
                mAdapter.notifyDataSetChanged();
                enterAnimation();
                if (mList.size() == 0) {
                    mBinding.layoutNoSreachResult.setVisibility(View.VISIBLE);
                    mBinding.swipeRefresh.setVisibility(View.GONE);
                } else {
                    mBinding.layoutNoSreachResult.setVisibility(View.GONE);
                    mBinding.swipeRefresh.setVisibility(View.VISIBLE);
                }
                ViewUnits.getInstance().showToast("搜索到" + mList.size() + "张卡");
                break;
            case NetCode.Card.findListLoad:
                List<CardItem> listLoad = (List<CardItem>) object;
                if (listLoad != null && listLoad.size() > 0) {
                    mList.addAll(listLoad);
                } else {
                    mAdapter.setHasMore(false);
                    mPage--;
                }

                mAdapter.notifyDataSetChanged();
                enterAnimation();
                ViewUnits.getInstance().showToast("搜索到" + mList.size() + "张卡");
                break;
//            case NetCode.Card.anonymousFocus:
//            case NetCode.Card.cardAttentionAdd:
//                jumpToCard(mCardItem);
//                break;
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
            case NetCode.Card2.getQrCodeUrl:
                ViewUnits.getInstance().showQrCodePopView(SearchCardActivity.this, getWindow().peekDecorView(), object.toString());
                break;
            case NetCode.Card2.getShareQrCodeUrl:
                if (BaseUnits.getInstance().getShareList(this).length == 0) {
                    ViewUnits.getInstance().showToast("您未安装微信、微博和QQ");
                    return;
                }

                UMImage umImage = new UMImage(this, QrCodeUnits.createQRCodeWithLogo(object.toString(), 600,
                        BitmapFactory.decodeResource(getResources(), R.mipmap.logo)));
                new ShareAction(this)
                        .withText("智慧家园")
                        .setDisplayList(BaseUnits.getInstance().getShareList(this))
                        .setCallback(new MainUMShareListener())
                        .withMedia(umImage)
                        .open();
                break;
        }
    }

    @Override
    public void onFailed(int what, Object object) {
        isLoading = false;
        ViewUnits.getInstance().missLoading();
        ViewUnits.getInstance().showToast(object.toString());
//        switch (what) {
//            case NetCode.Card.anonymousFocus:
//            case NetCode.Card.cardAttentionAdd:
//                jumpToCard(mCardItem);
//                break;
//            default:
//                ViewUnits.getInstance().showToast(object.toString());
//                break;
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "分享结果:requestCode=" + requestCode + ";resultCode=" + resultCode + ";data=" + data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);  //回调分享结果
    }

    @Override
    protected void onDestroy() {
        mCardItem = null;
        mThemeVal = null;
        super.onDestroy();
    }
}
