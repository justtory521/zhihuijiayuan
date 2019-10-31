package tendency.hz.zhihuijiayuan.view.index;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.zhy.m.permission.MPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.jpush.android.api.JPushInterface;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.adapter.MainCardRecyclerAdapter;
import tendency.hz.zhihuijiayuan.adapter.holder.inter.HomeCardItemOnClickInter;
import tendency.hz.zhihuijiayuan.bean.AppCardItem;
import tendency.hz.zhihuijiayuan.bean.CardItem;
import tendency.hz.zhihuijiayuan.bean.City;
import tendency.hz.zhihuijiayuan.bean.base.Field;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.bean.base.What;
import tendency.hz.zhihuijiayuan.databinding.FragmentHomeBinding;
import tendency.hz.zhihuijiayuan.inter.FragmentInteraction;
import tendency.hz.zhihuijiayuan.inter.PopWindowOnClickInter;
import tendency.hz.zhihuijiayuan.presenter.CardPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.SetPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.CardPrenInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.SetPrenInter;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.CacheUnits;
import tendency.hz.zhihuijiayuan.units.ConfigUnits;
import tendency.hz.zhihuijiayuan.units.DateUtils;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.LocationUntis;
import tendency.hz.zhihuijiayuan.units.UserUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.ScanQRCodeActivity;
import tendency.hz.zhihuijiayuan.view.card.CardContentActivity;
import tendency.hz.zhihuijiayuan.view.card.SearchCardActivity;
import tendency.hz.zhihuijiayuan.view.picker.CityPickerActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

import static android.app.Activity.RESULT_OK;

/**
 * Created by JasonYao on 2018/11/14.
 */
public class HomeFragment extends Fragment implements AllViewInter {
    private static final String TAG = "HomeFragment---";
    private FragmentHomeBinding mBinding;
    private CardPrenInter mCardPrenInter;
    private SetPrenInter mSetPrenInter;
    private MainCardRecyclerAdapter mAdapter;
    private LinearLayoutManager mManager;
    private List<CardItem> mCardItems = new ArrayList<>();
    private int mPosition;  //操作条目
    private CardItem mCardItemOnClick;  //当前点击的卡片
    private FragmentInteraction mFragmentInteraction;
    private List<CardItem> mCardRecommend = new ArrayList<>();
    private List<String> mInfo = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        ViewUnits.getInstance().setTitleHeight(mBinding.layoutTitle);
        mCardPrenInter = new CardPrenImpl(this);
        mSetPrenInter = new SetPrenImpl(this);
        mCardPrenInter.getChoiceRecommendCard(NetCode.Card2.getChoiceRecommend);

        mAdapter = new MainCardRecyclerAdapter(getActivity(), mCardItems);
        mManager = new LinearLayoutManager(getActivity());
        mBinding.recyclerCardMain.setLayoutManager(mManager);
        mBinding.recyclerCardMain.setAdapter(mAdapter);

        setListener();

        checkLocationPermission();

        return mBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteraction) {
            mFragmentInteraction = (FragmentInteraction) context;
        } else {
            throw new IllegalArgumentException("activity must implements FragmentInteraction");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //每次打开APP都重新设置一下推送别名
        JPushInterface.setAlias(getActivity(), new Random().nextInt(900) + 100, BaseUnits.getInstance().getPhoneKey());
        if (!ConfigUnits.getInstance().getFristInstallStatus()) {  //第一次安装,不需要获取卡片，自动绑定成功后，会执行安装
            getMyCard();
        }

        mAdapter.notifyDataSetChanged();
    }

    private void getMyCard() {
        mCardPrenInter.myCardList(NetCode.Card.myCardListRefresh, "", "1");
    }

    private void setListener() {

        mBinding.btnScanMain.setOnClickListener(view -> getCameraPermission());
        mBinding.btnScanTop2.setOnClickListener(view -> getCameraPermission());
        mBinding.btnLocationTop2.setOnClickListener(view -> {
            if (!checkLocationPermission()) return;
            Intent intent = new Intent(getActivity(), CityPickerActivity.class);
            startActivityForResult(intent, Request.StartActivityRspCode.GOTO_CITYPICKER);
        });
        mBinding.layoutCityPicker.setOnClickListener(view -> {
            if (!checkLocationPermission()) return;
            Intent intent = new Intent(getActivity(), CityPickerActivity.class);
            startActivityForResult(intent, Request.StartActivityRspCode.GOTO_CITYPICKER);
        });

        mAdapter.setListener(new HomeCardItemOnClickInter() {
            @Override
            public void onItemOnClick(View view, int postion) {
                if (postion < 0) return;
                mPosition = postion;
                jumpToCard(mCardItems.get(postion));
            }

            @Override
            public void addMoreCardOnClick(View view, int position) {
                Intent intent = new Intent(getActivity(), SearchCardActivity.class);
                intent.putExtra("ThemeVal", "");
                startActivity(intent);
            }
        });

        mBinding.marqueeView.setOnItemClickListener((position, textView) -> {
            addCard(mCardRecommend.get(position));  //异步关注卡
            Intent intent = new Intent(getActivity(), CardContentActivity.class);
            intent.putExtra("cardItem", mCardRecommend.get(position));
            startActivity(intent);
        });

        mBinding.recyclerCardMain.addOnScrollListener(new manyItemScrollListener());

        mBinding.btnGoTop.setOnClickListener(view -> mBinding.recyclerCardMain.smoothScrollToPosition(0));

        LocationUntis.getInstance().setLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {

                if (bdLocation == null) return;

                if (FormatUtils.getInstance().isEmpty(bdLocation.getCity())) {
                    return;
                }


                UserUnits.getInstance().setLocation(bdLocation.getCity());

                if (FormatUtils.getInstance().isEmpty(UserUnits.getInstance().getSelectCity())) {
                    UserUnits.getInstance().setSelectCity(bdLocation.getCity());
                }

                if (ConfigUnits.getInstance().getFristInstallStatus()) {  //第一次安装
                    ConfigUnits.getInstance().setFirstInstallStatus(false);
                    mCardPrenInter.authFocusCard(NetCode.Card2.autoFocusCard);
                } else {
                    getMyCard();
                }


                mBinding.textCityName.setText(UserUnits.getInstance().getSelectCity());

                LocationUntis.getInstance().stopLocation();

                String currentTime = DateUtils.getDate(Field.DateType.year_month_day, System.currentTimeMillis());
                if (!bdLocation.getCity().equals(UserUnits.getInstance().getSelectCity()) &&
                        !currentTime.equals(UserUnits.getInstance().getNotifyTime())
                        && !"中国移动".equals(UserUnits.getInstance().getSelectCity())) {
                    UserUnits.getInstance().setNotifyTime(currentTime);

                    if (getActivity() == null || getActivity().isFinishing()) {
                        return;
                    }
                    ViewUnits.getInstance().showPopWindow(getActivity(), getActivity().getWindow().peekDecorView()
                            , "定位到您在" + bdLocation.getCity() + ",是否切换至该城市?", new PopWindowOnClickInter() {
                                @Override
                                public void leftBtnOnClick() {
                                    ViewUnits.getInstance().missPopView();
                                }

                                @Override
                                public void rightBtnOnClick() {
                                    mBinding.textCityName.setText(bdLocation.getCity());
                                    UserUnits.getInstance().setSelectCity(bdLocation.getCity());
                                    ViewUnits.getInstance().missPopView();
                                }
                            });
                }
            }
        });
    }

    private class manyItemScrollListener extends RecyclerView.OnScrollListener {
        int mDistance = 0;

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (mManager.findFirstCompletelyVisibleItemPosition() > 0) {
                mBinding.btnGoTop.setVisibility(View.VISIBLE);
            } else {
                mBinding.btnGoTop.setVisibility(View.GONE);
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (mCardItems.size() <= (recyclerView.getHeight() / ViewUnits.getInstance().dp2px(getActivity(), 120) + 1)) {
                mBinding.layoutTop2.setAlpha(0);
                mBinding.layoutTop.setAlpha(1);
                mBinding.layoutTop2.setVisibility(View.GONE);
                mBinding.layoutTop.setVisibility(View.VISIBLE);
                mDistance = 0;
                return;
            }

            mDistance += dy;
            Log.e(TAG, "----------------------------->当前滑动距离：" + mDistance);
            int toolbarHeight = ViewUnits.getInstance().dp2px(getActivity(), 120);
            //当滑动的距离 <= toolbar高度的时候，改变Toolbar背景色的透明度，达到渐变的效果
            if (mDistance <= toolbarHeight) {
                Log.e(TAG, "---------------->正在显示第一个标题");
                mBinding.layoutTop.setVisibility(View.VISIBLE);
                mBinding.layoutTop2.setVisibility(View.VISIBLE);
                float scale = (float) mDistance / toolbarHeight;
                float alpha = scale * 255;
                float range = alpha / 255;
                mBinding.layoutTop2.setAlpha(range);
            } else {
                Log.e(TAG, "------>正在显示第二个标题");
                //将标题栏的颜色设置为完全不透明状态
                mBinding.layoutTop2.setAlpha(1);
                mBinding.layoutTop2.setVisibility(View.VISIBLE);
                mBinding.layoutTop.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Android6.0动态获取定位权限
     */
    private boolean checkLocationPermission() {
        if (!BaseUnits.getInstance().checkPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) { //没有定位相关权限，动态获取
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, Request.Permissions.REQUEST_LOCATION_STATE);
            return false;
        } else { //有定位权限，进行定位
            LocationUntis.getInstance().startLocation();
            return true;
        }
    }

    /**
     * 跳转至卡页面（分为应用卡和业务卡）
     */
    private void jumpToCard(CardItem cardItem) {
        mCardItemOnClick = cardItem;
        ViewUnits.getInstance().showLoading(getActivity(), "请求中");
        mCardPrenInter.checkCanOperate(NetCode.Card2.checkCanOperate, cardItem.getCardID());
    }

    /**
     * 获取权限（拍照权限）
     */
    private void getCameraPermission() {
        //获取读写SD卡权限
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            MPermissions.requestPermissions(getActivity(), What.Permissions.getSDPermissions, Manifest.permission.READ_EXTERNAL_STORAGE);
            return;
        }
        //获取拍照权限
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            MPermissions.requestPermissions(getActivity(), What.Permissions.getCameraPermissions, Manifest.permission.CAMERA);
            return;
        }

        Intent intent = new Intent(getActivity(), ScanQRCodeActivity.class);
        startActivityForResult(intent, What.SCANQRCODE);
        startActivity(intent);
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

    private void addCard(CardItem cardItem) {
        mCardPrenInter.cardAttentionAdd(NetCode.Card.cardAttentionAdd, cardItem);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == What.SCANQRCODE && resultCode == RESULT_OK) {  //扫描二维码回调
            if (FormatUtils.getInstance().isEmpty(data.getStringExtra("result"))) {
                return;
            }
            Uri mUri = Uri.parse(data.getStringExtra("result"));


            if (mUri == null || !mUri.isHierarchical() || FormatUtils.getInstance().isEmpty(mUri.getQueryParameter("code"))) {
                ViewUnits.getInstance().showToast("非卡片二维码，请重新扫码");
                return;
            }

            mSetPrenInter.cardQrCode(NetCode.Set.cardQrCode, mUri.getQueryParameter("code"));
        }

        if (requestCode == Request.StartActivityRspCode.GOTO_CITYPICKER) {
            try {
                if (data.getSerializableExtra("selectCity") == null) {
                    return;
                }
                City city = (City) data.getSerializableExtra("selectCity");
                mBinding.textCityName.setText(city.getName());
                mFragmentInteraction.process();
                mCardPrenInter.getChoiceRecommendCard(NetCode.Card2.getChoiceRecommend);   //切换城市后，刷新数据
            } catch (Exception e) {
            }
        }
    }

    /**
     * 获取权限监听
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Request.Permissions.REQUEST_PHONE_STATE && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            BaseUnits.getInstance().getPhoneKey();  //用户拒绝给权限，则产生随机序列号，并储存
        }

        if (requestCode == Request.Permissions.REQUEST_LOCATION_STATE && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            LocationUntis.getInstance().startLocation();  //用户给了定位权限，进行定位
        }

        if (requestCode == Request.Permissions.REQUEST_LOCATION_STATE && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            ViewUnits.getInstance().showToast("没有定位权限，无法正常使用APP");
        }
    }

    /**
     * 刷新列表，根据列表数据，跳转页面结构
     */
    private void refreshList(List<CardItem> cardItems) {
        mCardItems.clear();
        mCardItems.addAll(cardItems);
        mAdapter.notifyDataSetChanged();
        mBinding.imgLoading.setVisibility(View.GONE);
        if (mCardItems.size() == 0) {
            mBinding.layoutNoCard.setVisibility(View.VISIBLE);
            mBinding.recyclerCardMain.setVisibility(View.GONE);
            return;
        }
        mBinding.layoutNoCard.setVisibility(View.GONE);
        mBinding.recyclerCardMain.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        switch (what) {
            case NetCode.Set.cardQrCode:
                CardItem cardItem = (CardItem) object;
                addCard(cardItem);  //异步添加关注卡
                if ("1".equals(cardItem.getFocusStatus())) {  //用户已经关注，直接跳转转卡详情页面
                    jumpToCard(cardItem);
                } else {  //用户未关注，跳转至领卡页面
                    Intent intent = new Intent(getActivity(), CardContentActivity.class);
                    intent.putExtra("cardItem", cardItem);
                    startActivity(intent);
                }
                break;
            case NetCode.Card.myCardListRefresh:
                List<CardItem> cardItemsRefresh = (List<CardItem>) object;
                refreshList(cardItemsRefresh);

                break;
            case NetCode.Card2.getAnonymousList:
                List<CardItem> cardItemsAnonymousList = (List<CardItem>) object;
                refreshList(cardItemsAnonymousList);
                break;
            case NetCode.Card.deleteCard:
                //列表删除后，即使刷新
                mCardItems.remove(mPosition);
                mAdapter.notifyItemRemoved(mPosition);
                mAdapter.notifyItemRangeChanged(0, mCardItems.size() - 2);
                mAdapter.notifyDataSetChanged();
                break;
            case NetCode.Card.anonymousCancel:
                if (mCardItems.size() == 0) {
                    return;
                }
                CacheUnits.getInstance().deleteMyCacheCardById(mCardItems.get(mPosition).getCardID());
                //列表删除后，即使刷新
                mCardItems.remove(mPosition);
                mAdapter.notifyItemRemoved(mPosition);
                mAdapter.notifyItemRangeChanged(0, mCardItems.size() - 2);
                mAdapter.notifyDataSetChanged();
                break;
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
            case NetCode.Card2.checkCanOperate:
                ViewUnits.getInstance().missLoading();
                if (mCardItemOnClick.getCardType().equals(What.CardType.businessCard)) {
                    Intent intent = new Intent(getActivity(), CardContentActivity.class);
                    intent.putExtra("cardItem", mCardItemOnClick);
                    getActivity().startActivity(intent);
                } else {
                    ViewUnits.getInstance().showLoading(getActivity(), "请稍等");
                    mCardPrenInter.getAppCardInfo(NetCode.Card.getAppCardInfo, mCardItemOnClick.getCardID());
                }
                break;
            case NetCode.Card2.autoFocusCard:
                getMyCard();
                break;
            case NetCode.Card2.getChoiceRecommend:
                mCardRecommend.clear();
                mInfo.clear();
                if (object != null) {
                    mCardRecommend.addAll((List<CardItem>) object);
                    for (CardItem cardItem1 : mCardRecommend) {
                        mInfo.add(cardItem1.getTitle());
                    }
                }

                if (mInfo.size() > 0) {
                    mBinding.marqueeView.setVisibility(View.VISIBLE);
                    mBinding.marqueeView.startWithList(mInfo);
                } else {
                    mBinding.marqueeView.stopFlipping();
                    mBinding.marqueeView.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onFailed(int what, Object object) {
        ViewUnits.getInstance().missLoading();

        if (what == NetCode.Card.anonymousFocus || what == NetCode.Card.cardAttentionAdd) return;

        if (what == NetCode.Card2.checkCanOperate) { //卡片不能使用
            if (object.equals("网络错误!!")) {
                ViewUnits.getInstance().showToast(object.toString());
                return;
            }
            if (getActivity() != null && !getActivity().isFinishing()) {
                ViewUnits.getInstance().showPopWindow(getActivity(), getActivity().getWindow().peekDecorView(), object.toString() + "\n是否移除该卡片",
                        new PopWindowOnClickInter() {
                            @Override
                            public void leftBtnOnClick() {
                                ViewUnits.getInstance().missPopView();
                            }

                            @Override
                            public void rightBtnOnClick() {
                                ViewUnits.getInstance().missPopView();
                                deleteItem(mCardItemOnClick);
                            }
                        });

            }
            return;
        }

        ViewUnits.getInstance().showToast(object.toString());
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentInteraction = null;
    }
}