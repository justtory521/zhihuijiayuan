package tendency.hz.zhihuijiayuan.view.card;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.List;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.adapter.FindCardRecyclerAdapter;
import tendency.hz.zhihuijiayuan.adapter.HotSreachRecyclerAdapter;
import tendency.hz.zhihuijiayuan.adapter.holder.inter.SearchCardItemOnClickInter;
import tendency.hz.zhihuijiayuan.bean.AppCardItem;
import tendency.hz.zhihuijiayuan.bean.CardItem;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.What;
import tendency.hz.zhihuijiayuan.databinding.ActivityQueryCardBinding;
import tendency.hz.zhihuijiayuan.databinding.LayoutVoicePopupBinding;
import tendency.hz.zhihuijiayuan.listener.MainUMShareListener;
import tendency.hz.zhihuijiayuan.presenter.CardPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.CardPrenInter;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.CacheUnits;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.QrCodeUnits;
import tendency.hz.zhihuijiayuan.units.SpeechUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

import static tendency.hz.zhihuijiayuan.bean.base.Request.Permissions.REQUEST_RECORD_PERMISSIONS;

/**
 * Created by JasonYao on 2018/4/3.
 */

public class QueryCardActivity extends BaseActivity implements AllViewInter {
    private static final String TAG = "QueryCardActivity---";

    private CardPrenInter mCardPrenInter;

    private String mSreachContent;
    private List<CardItem> mList = new ArrayList<>();
    private FindCardRecyclerAdapter mAdapter;

    private InputMethodManager mImm;
    private View mView;

    private ActivityQueryCardBinding mBinding;
    private LayoutVoicePopupBinding mBindingPop;

    private PopupWindow mPopupWindow;  //语音弹出框
    private View mPopBackView;  //语音弹出框View
    private WindowManager.LayoutParams mLayoutParams;

    private Handler mHandler;
    private Runnable mRunable;
    private CardItem mCardItem;

    private String mThemeVal;

    private HotSreachRecyclerAdapter mHotSreachAdapter;
    private List<CardItem> mHotSreachLists = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_query_card);
        ViewUnits.getInstance().setTitleHeight(mBinding.layoutTitle);
        mView = getWindow().getDecorView();
        initData();

        initView();

        setListener();

        initHandler();
    }


    private void initData() {
        mThemeVal = super.getIntent().getStringExtra("ThemeVal");
        mAdapter = new FindCardRecyclerAdapter(this, mList);
        mHotSreachAdapter = new HotSreachRecyclerAdapter(this, mHotSreachLists);
        mCardPrenInter = new CardPrenImpl(this);
        mCardPrenInter.getCardHotSearch(NetCode.Card2.getCardHotSearch);

        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void initView() {
        mBinding.recyclerSreachResult.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerSreachResult.setAdapter(mAdapter);
        mPopupWindow = new PopupWindow();
        mLayoutParams = getWindow().getAttributes();
        mPopBackView = LayoutInflater.from(this).inflate(R.layout.layout_voice_popup, null);
        //设置Popup具体参数
        mPopupWindow.setContentView(mPopBackView);
        mPopupWindow.setWidth(LinearLayoutCompat.LayoutParams.MATCH_PARENT);//设置宽
        mPopupWindow.setHeight(LinearLayoutCompat.LayoutParams.WRAP_CONTENT);//设置高
        mPopupWindow.setFocusable(true);//点击空白，popup不自动消失
        mPopupWindow.setOutsideTouchable(true);//非popup区域可触摸
        mPopupWindow.setAnimationStyle(R.style.ActionSheetDialogAnimation);
        mBindingPop = DataBindingUtil.bind(mPopBackView);
        mBinding.swipeRefresh.setEnableLoadMore(false);

        if (CacheUnits.getInstance().getSearchHis().size() == 0) {  //没有搜索历史，不显示相关布局
            mBinding.layoutSreachHis.setVisibility(View.GONE);
        } else {
            mBinding.layoutSreachHis.setVisibility(View.VISIBLE);
        }
        mBinding.flowlayout.addViews(CacheUnits.getInstance().getSearchHis(), value -> {
            mBinding.edtSreachQuery.setText(value);
            mBinding.edtSreachQuery.setSelection(value.length());
            mCardPrenInter.getChoiceSreach(NetCode.Card.findListRefresh, mThemeVal, value, "1");
        });
        mBinding.recyclerHotSreach.setLayoutManager(new GridLayoutManager(QueryCardActivity.this, 2));
        mBinding.recyclerHotSreach.setAdapter(mHotSreachAdapter);
    }

    private void initHandler() {
        mHandler = new Handler();
        mRunable = () -> {
            mLayoutParams.alpha = 0.7f;
            getWindow().setAttributes(mLayoutParams);
            if (!mPopupWindow.isShowing()) {
                mPopupWindow.showAtLocation(getWindow().peekDecorView(), Gravity.BOTTOM, 0, 0);
            } else {
                ///如果正在显示等待框，，，则结束掉等待框然后重新显示
                mPopupWindow.dismiss();
                mPopupWindow.showAtLocation(getWindow().peekDecorView(), Gravity.BOTTOM, 0, 0);
            }
        };
    }

    private void setListener() {
        mAdapter.setListener(new SearchCardItemOnClickInter() {
            @Override
            public void onItemOnClick(View view, int postion) {
                mCardItem = mList.get(postion);
                mCardPrenInter.cardAttentionAdd(NetCode.Card.cardAttentionAdd, mList.get(postion));
            }

            @Override
            public void onQrCodeOnClick(View view, int position) {
                ViewUnits.getInstance().showLoading(QueryCardActivity.this, "请求中");
                mCardPrenInter.getCardQrCodeUrl(NetCode.Card2.getQrCodeUrl, mList.get(position).getCardID());
            }

            @Override
            public void onShareOnClick(View view, int position) {
                ViewUnits.getInstance().showLoading(QueryCardActivity.this, "请求中");
                mCardPrenInter.getCardQrCodeUrl(NetCode.Card2.getShareQrCodeUrl, mList.get(position).getCardID());
            }
        });

        mBinding.edtSreachQuery.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                query();
                return true;
            }

            return false;
        });

        mPopupWindow.setOnDismissListener(() -> {
            mLayoutParams.alpha = 1.0f;
            getWindow().setAttributes(mLayoutParams);
        });

        mBindingPop.btnClear.setOnClickListener(view -> mBinding.edtSreachQuery.setText(""));
        mBindingPop.btnCancel.setOnClickListener(view -> mPopupWindow.dismiss());

        mBindingPop.btnVoice.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_UP:
                    mBindingPop.textTitle.setText("按住说话");
                    mBindingPop.textTitle.setTextColor(QueryCardActivity.this.getResources().getColor(R.color.colorTextGray));
                    SpeechUnits.getInstance().stopListening();
                    break;
                case MotionEvent.ACTION_DOWN:
                    mBindingPop.textTitle.setText("请开始说话...");
                    mBindingPop.textTitle.setTextColor(QueryCardActivity.this.getResources().getColor(R.color.colorPrimary));
                    SpeechUnits.getInstance().startListening(result -> {
                        mBinding.edtSreachQuery.setText(result);
                        mBinding.edtSreachQuery.setSelection(mBinding.edtSreachQuery.length());
                        if (!FormatUtils.getInstance().isEmpty(result)) {
                            mPopupWindow.dismiss();
                            query();
                        }
                    });
                    break;
            }
            return true;
        });

        mBinding.swipeRefresh.setOnRefreshListener(refreshLayout -> {
            if (refreshLayout.getState() == RefreshState.Refreshing) {  //表示正在刷新
                refreshQuery();
            }
        });

        mBinding.btnClearHis.setOnClickListener(view -> {
            mBinding.layoutSreachHis.setVisibility(View.GONE);
            CacheUnits.getInstance().clearSearchHis();
        });

        mHotSreachAdapter.setListener((view, postion) -> {
            mCardItem = mHotSreachLists.get(postion);
            mCardPrenInter.cardAttentionAdd(NetCode.Card.cardAttentionAdd, mHotSreachLists.get(postion));
        });
    }

    /**
     * 隐藏键盘
     */
    private void hideInput() {
        mImm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
    }

    private void query() {
        mSreachContent = mBinding.edtSreachQuery.getText().toString();

        if (FormatUtils.getInstance().isEmpty(mSreachContent)) { //搜索内容为空，跳出
            ViewUnits.getInstance().showToast("搜索内容不能为空");
            mBinding.swipeRefresh.finishRefresh();
            return;
        }

        mBinding.recyclerSreachResult.setVisibility(View.GONE);
        mBinding.layoutNoSreachResult.setVisibility(View.GONE);
        mBinding.imgLoading.setVisibility(View.VISIBLE);
        CacheUnits.getInstance().insertSearchHis(mSreachContent);  //插入历史搜索表中
        mCardPrenInter.getChoiceSreach(NetCode.Card.findListRefresh, mThemeVal, mSreachContent, "1");
    }

    private void refreshQuery() {
        mSreachContent = mBinding.edtSreachQuery.getText().toString();

        if (FormatUtils.getInstance().isEmpty(mSreachContent)) { //搜索内容为空，跳出
            ViewUnits.getInstance().showToast("搜索内容不能为空");
            return;
        }

        mCardPrenInter.getChoiceSreach(NetCode.Card.findListRefresh, mThemeVal, mSreachContent, "1");
    }

    public void backOnClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    public void showPopup(View view) {
        if (BaseUnits.getInstance().checkPermission(this, Manifest.permission.RECORD_AUDIO)) {
            hideInput();
            mHandler.postDelayed(mRunable, 200);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_PERMISSIONS);
        }

    }

    /**
     * 跳转至卡页面（分为应用卡和业务卡）
     */
    private void jumpToCard(CardItem cardItem) {
        Log.e(TAG, "cardTypeId" + cardItem.getCardType());
        if (cardItem.getCardType().equals(What.CardType.businessCard)) {
            Intent intent = new Intent(QueryCardActivity.this, CardContentActivity.class);
            intent.putExtra("cardItem", cardItem);
            QueryCardActivity.this.startActivity(intent);
        } else {
            ViewUnits.getInstance().showLoading(this, "请稍等");
            mCardPrenInter.getAppCardInfo(NetCode.Card.getAppCardInfo, cardItem.getCardID());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onSuccessed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        switch (what) {
            case NetCode.Card.findListRefresh:
                mBinding.swipeRefresh.finishRefresh();
                mBinding.imgLoading.setVisibility(View.GONE);
                mBinding.recyclerSreachResult.setVisibility(View.VISIBLE);
                mBinding.layoutDefault.setVisibility(View.GONE);

                if (((List<CardItem>) object).size() < Integer.valueOf(What.PAGE_SIZE)) {
                    mAdapter.setHasMore(false);
                } else {
                    mAdapter.setHasMore(true);
                }
                mList.clear();
                mList.addAll((List<CardItem>) object);
                mAdapter.notifyDataSetChanged();
                if (mList.size() == 0) {
                    mBinding.layoutNoSreachResult.setVisibility(View.VISIBLE);
                    mBinding.swipeRefresh.setVisibility(View.GONE);
                } else {
                    mBinding.layoutNoSreachResult.setVisibility(View.GONE);
                    mBinding.swipeRefresh.setVisibility(View.VISIBLE);
                }
                ViewUnits.getInstance().showToast("搜索到" + mList.size() + "张卡");
                hideInput();
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
            case NetCode.Card2.getQrCodeUrl:
                ViewUnits.getInstance().showQrCodePopView(QueryCardActivity.this, getWindow().peekDecorView(), object.toString());
                break;
            case NetCode.Card2.getShareQrCodeUrl:
                UMImage umImage = new UMImage(this, QrCodeUnits.createQRCodeWithLogo(object.toString(), 600,
                        BitmapFactory.decodeResource(getResources(), R.mipmap.logo)));

                if (BaseUnits.getInstance().getShareList(this).length == 0) {
                    ViewUnits.getInstance().showToast("您未安装微信、微博和QQ");
                    return;
                }

                new ShareAction(this)
                        .withText("智慧家园")
                        .setDisplayList(BaseUnits.getInstance().getShareList(this))
                        .setCallback(new MainUMShareListener())
                        .withMedia(umImage)
                        .open();
                break;
            case NetCode.Card2.getCardHotSearch:
                List<CardItem> hotSearchs = (List<CardItem>) object;
                mHotSreachLists.clear();
                mHotSreachLists.addAll(hotSearchs);
                mHotSreachAdapter.notifyDataSetChanged();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onFailed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        hideInput();
        switch (what) {
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCardPrenInter = null;
        mSreachContent = null;
        mList = null;
        mAdapter = null;
        mImm = null;
        mView = null;
        mBindingPop = null;
        mPopupWindow = null;
        mPopBackView = null;
        mLayoutParams = null;
        mHandler = null;
        mRunable = null;
        mCardItem = null;
        mThemeVal = null;
    }
}
