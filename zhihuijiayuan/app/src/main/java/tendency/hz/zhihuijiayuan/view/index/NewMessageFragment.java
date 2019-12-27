package tendency.hz.zhihuijiayuan.view.index;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import exocr.exocrengine.ViewUtil;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.Message;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.inter.FragmentInteraction;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PersonalPrenInter;
import tendency.hz.zhihuijiayuan.units.CacheUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseFragment;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Author：Libin on 2019/12/6 0006 16:48
 * Email：1993911441@qq.com
 * Describe：
 */
public class NewMessageFragment extends BaseFragment implements AllViewInter {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.rv_msg)
    RecyclerView rvMsg;
    @BindView(R.id.srf_msg)
    SmartRefreshLayout srfMsg;
    @BindView(R.id.ll_no_msg)
    LinearLayout llNoMsg;
    private Unbinder unbinder;

    private PersonalPrenInter mPersonalPreInter;
    private FragmentInteraction mFragmentInteraction;
    private List<Message> msgList = new ArrayList<>();
    private BaseQuickAdapter<Message, BaseViewHolder> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_new, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
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
        initData();
    }


    private void initData() {
        msgList.clear();

        msgList.addAll(CacheUnits.getInstance().getMessageGroupByCardId());

        if (msgList.size() <= 0) {
            llNoMsg.setVisibility(View.VISIBLE);
            rvMsg.setVisibility(View.GONE);
        } else {
            llNoMsg.setVisibility(View.GONE);
            rvMsg.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }


    private void initView() {
        ivTitleBack.setVisibility(View.GONE);
        ViewUnits.getInstance().setTitleHeight(viewStatusBar);
        llTitle.setBackgroundResource(R.color.blue_0d8);
        tvTitleName.setText("消息");
        mPersonalPreInter = new PersonalPrenImpl(this);
        srfMsg.setEnableLoadMore(false);

        rvMsg.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BaseQuickAdapter<Message, BaseViewHolder>(R.layout.rv_message_item,msgList) {
            @Override
            protected void convert(BaseViewHolder helper, Message item) {
                SimpleDraweeView ivIcon = helper.getView(R.id.iv_msg_icon);
                if (!TextUtils.isEmpty( item.getCardID()) && !item.getCardID().equals("0")){
                    ivIcon.setImageURI(item.getCardLogoUrl());
                    helper.setText(R.id.tv_msg_card_title,item.getCardName());
                }else {
                    ivIcon.setImageURI("");
                    helper.setText(R.id.tv_msg_card_title,"系统消息");
                }

                helper.setText(R.id.tv_msg_card_time,item.getDateTime());
                helper.setText(R.id.tv_msg_subtitle,item.getContent());

                int unreadCount  = CacheUnits.getInstance().getUnreadCountsByCardId(item.getCardID());

                TextView tvUnreadCount = helper.getView(R.id.tv_msg_num);
                if (unreadCount <=0){
                    tvUnreadCount.setVisibility(View.GONE);
                    tvUnreadCount.setText("");
                } else if (unreadCount <=99){
                    tvUnreadCount.setVisibility(View.VISIBLE);
                    tvUnreadCount.setText(String.valueOf(unreadCount));
                }else {
                    tvUnreadCount.setVisibility(View.VISIBLE);
                    tvUnreadCount.setText("99+");
                }
            }
        };

        rvMsg.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(),MessageDetailActivity.class);
                intent.putExtra("type",msgList.get(position).getCardID());
                intent.putExtra("title",TextUtils.isEmpty(msgList.get(position).getCardID()) ||
                        msgList.get(position).getCardID().equals("0")? "系统消息":msgList.get(position).getCardName());
                startActivity(intent);
            }
        });


        srfMsg.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPersonalPreInter.getMessage(NetCode.Personal.RefreshMessage);
            }
        });
    }

    @Override
    public void onSuccessed(int what, Object object) {
        srfMsg.finishRefresh();
        mFragmentInteraction.clearMessage();
        initData();
    }

    @Override
    public void onFailed(int what, Object object) {
       srfMsg.finishRefresh();
        mFragmentInteraction.clearMessage();
        initData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentInteraction = null;
    }
}
