package tendency.hz.zhihuijiayuan.view.index;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.constant.RefreshState;

import java.util.ArrayList;
import java.util.List;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.adapter.MessageRecyclerAdapter;
import tendency.hz.zhihuijiayuan.bean.CardItem;
import tendency.hz.zhihuijiayuan.bean.Message;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.databinding.FragmentMessageBinding;
import tendency.hz.zhihuijiayuan.inter.FragmentInteraction;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PersonalPrenInter;
import tendency.hz.zhihuijiayuan.units.CacheUnits;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.card.CardContentActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/11/14.
 */
public class MessageFragment extends Fragment implements AllViewInter {
    private static final String TAG = "MessageFragment---";
    private FragmentMessageBinding mBinding;
    private List<Message> mList = new ArrayList<>();
    private MessageRecyclerAdapter mAdapter;
    private PersonalPrenInter mPersonalPreInter;
    private FragmentInteraction mFragmentInteraction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false);
        ViewUnits.getInstance().setTitleHeight(mBinding.layoutTitle);

        mPersonalPreInter = new PersonalPrenImpl(this);
        initData();
        initView();
        setListener();
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            refreshData();
        }
    }

    private void refreshData() {
        Log.e(TAG, "执行RefreshData");
        mList.clear();
        mList.addAll(CacheUnits.getInstance().getMessage());
        if (mList.size() <= 0) {
            Log.e(TAG, "数据为空");
            mBinding.layoutBottom.setVisibility(View.GONE);
            mBinding.layoutMarginBottom.setVisibility(View.GONE);
            mBinding.btnEditor.setVisibility(View.GONE);
            mBinding.layoutNoMessage.setVisibility(View.VISIBLE);
            mBinding.btnEditor.setVisibility(View.INVISIBLE);
            mBinding.checkboxAllSelect.setChecked(false);
        } else {
            Log.e(TAG, "数据不为空");
            mBinding.layoutNoMessage.setVisibility(View.GONE);
            mBinding.btnEditor.setVisibility(View.VISIBLE);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initData() {
        mAdapter = new MessageRecyclerAdapter(getActivity(), mList);
    }

    private void initView() {
        mBinding.recyclerMessage.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerMessage.setAdapter(mAdapter);
        mBinding.swipeRefresh.setEnableLoadMore(false);
    }

    private void setListener() {
        mAdapter.setLitener(message -> {
        });
        mBinding.btnEditor.setOnClickListener(view -> {
            if (mBinding.textEditor.getText().toString().equals("取消")) {
                mBinding.textEditor.setText("编辑");
                mBinding.layoutBottom.setVisibility(View.GONE);
                mBinding.layoutMarginBottom.setVisibility(View.GONE);
                mAdapter.setIsShow(false);
                return;
            }

            if (mBinding.textEditor.getText().toString().equals("编辑")) {
                mBinding.textEditor.setText("取消");
                mBinding.layoutBottom.setVisibility(View.VISIBLE);
                mBinding.layoutMarginBottom.setVisibility(View.VISIBLE);
                mAdapter.setIsShow(true);
            }
        });
        mBinding.checkboxAllSelect.setOnCheckedChangeListener((compoundButton, b) -> mAdapter.setAllSelected(b));
        mBinding.btnDelete.setOnClickListener(view -> {
            mAdapter.remove();
            mList.clear();
            mList.addAll(CacheUnits.getInstance().getMessage());
            if (mList.size() <= 0) {
                mBinding.layoutBottom.setVisibility(View.GONE);
                mBinding.layoutMarginBottom.setVisibility(View.GONE);
                mBinding.textEditor.setText("编辑");
                mBinding.layoutNoMessage.setVisibility(View.VISIBLE);
                mBinding.btnEditor.setVisibility(View.INVISIBLE);
                mBinding.checkboxAllSelect.setChecked(false);
                mAdapter.setIsShow(false);
            } else {
                mBinding.layoutNoMessage.setVisibility(View.GONE);
                mBinding.btnEditor.setVisibility(View.VISIBLE);
            }
        });
        mBinding.swipeRefresh.setOnRefreshListener(refreshLayout -> {
            if (refreshLayout.getState() == RefreshState.Refreshing) {  //表示正在刷新
                mPersonalPreInter.getMessage(NetCode.Personal.RefreshMessage);
            }
        });
        mAdapter.setOnClickListener((view, postion) -> {
            Intent intent = new Intent(getActivity(), CardContentActivity.class);
            CardItem cardItem = new CardItem();
            cardItem.setCardID(mList.get(postion).getCardID());
            //如果消息URL为空，则直接打开对应卡片即可
            cardItem.setCardUrl(FormatUtils.getInstance().isEmpty(mList.get(postion).getUrl()) ? mList.get(postion).getCardUrl() : mList.get(postion).getUrl());
            intent.putExtra("cardItem", cardItem);
            intent.putExtra("type", Request.StartActivityRspCode.MESSAGE_CARDCONTENT_JUMP);
            startActivity(intent);
        });
    }

    @Override
    public void onSuccessed(int what, Object object) {
        mBinding.swipeRefresh.finishRefresh();
        CacheUnits.getInstance().clearMessageNum();
        mFragmentInteraction.clearMessage();
        refreshData();
    }

    @Override
    public void onFailed(int what, Object object) {
        mBinding.swipeRefresh.finishRefresh();
        CacheUnits.getInstance().clearMessageNum();
        mFragmentInteraction.clearMessage();
        refreshData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentInteraction = null;
    }
}

