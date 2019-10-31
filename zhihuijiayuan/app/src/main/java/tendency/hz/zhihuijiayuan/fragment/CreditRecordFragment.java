package tendency.hz.zhihuijiayuan.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.CreditRecordBean;
import tendency.hz.zhihuijiayuan.bean.base.Field;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PersonalPrenInter;
import tendency.hz.zhihuijiayuan.units.DateUtils;
import tendency.hz.zhihuijiayuan.units.SpaceItemDecoration;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseFragment;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Author：Libin on 2019/5/15 16:33
 * Email：1993911441@qq.com
 * Describe：
 */
public class CreditRecordFragment extends BaseFragment implements AllViewInter {
    @BindView(R.id.rv_credit_record)
    RecyclerView rvCreditRecord;
    Unbinder unbinder;
    @BindView(R.id.srl_credit_record)
    SmartRefreshLayout srlCreditRecord;
    //状态
    private int state;
    //当前页
    private int page = 1;
    //总页数
    private int totalPage;
    //刷新
    private boolean isRefresh;
    //加载更多
    private boolean isLoadMore;


    private PersonalPrenInter mPersonalPrenInter;
    private CreditRecordBean.DataBean dataBean;
    private List<CreditRecordBean.DataBean.ListBean> dataList = new ArrayList<>();
    private BaseQuickAdapter<CreditRecordBean.DataBean.ListBean, BaseViewHolder> adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_credit_record, container, false);
        unbinder = ButterKnife.bind(this, view);
        getData();
        initView();
        return view;
    }

    private void getData() {
        state = getArguments().getInt("state", 2);
    }

    private void initView() {
        mPersonalPrenInter = new PersonalPrenImpl(this);
        mPersonalPrenInter.getCreditRecord(NetCode.Personal.getCreditRecord, state, page);

        rvCreditRecord.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCreditRecord.addItemDecoration(new SpaceItemDecoration(ViewUnits.getInstance().dp2px(getActivity(), 12)));
        adapter = new BaseQuickAdapter<CreditRecordBean.DataBean.ListBean, BaseViewHolder>(R.layout.rv_credit_record_item, dataList) {
            @Override
            protected void convert(BaseViewHolder helper, CreditRecordBean.DataBean.ListBean item) {
                SimpleDraweeView sdv = helper.getView(R.id.sdv_service);
                sdv.setImageURI(item.getHeadImage());

                helper.setText(R.id.tv_join_service_time, DateUtils.formatTime(Field.DateType
                        .standard_time_format, Field.DateType.year_month_day, item.getCreateTime()));
                String serviceStatus = "";
                int status = item.getStatus();
                switch (status) {
                    case 2:
                        serviceStatus = "进行中";
                        break;
                    case 1:
                        serviceStatus = "已完成";
                        break;
                }
                helper.setText(R.id.tv_join_service_status, serviceStatus);

                helper.setText(R.id.tv_join_service_name, item.getServiceName());
            }
        };
        rvCreditRecord.setAdapter(adapter);

        srlCreditRecord.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (page < totalPage) {
                    isLoadMore = true;
                    page++;
                    mPersonalPrenInter.getCreditRecord(NetCode.Personal.getCreditRecord, state, page);
                } else {
                    srlCreditRecord.finishLoadMoreWithNoMoreData();
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                page = 1;
                mPersonalPrenInter.getCreditRecord(NetCode.Personal.getCreditRecord, state, page);
            }
        });
    }

    public static CreditRecordFragment newInstance(int state) {

        Bundle args = new Bundle();
        args.putInt("state", state);
        CreditRecordFragment fragment = new CreditRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSuccessed(int what, Object object) {

        if (what == NetCode.Personal.getCreditRecord) {
            dataBean = (CreditRecordBean.DataBean) object;

            if (dataBean != null) {
                totalPage = dataBean.getTotalPage();
            }

            if (isRefresh) {
                srlCreditRecord.finishRefresh();
                isRefresh = false;
                dataList.clear();
            } else if (isLoadMore) {
                srlCreditRecord.finishLoadMore();
            } else {
                ViewUnits.getInstance().missLoading();
                dataList.clear();
            }

            if (dataBean != null && dataBean.getList() != null) {
                dataList.addAll(dataBean.getList());
            } else {
                if (isLoadMore) {
                    page--;
                    srlCreditRecord.finishLoadMoreWithNoMoreData();
                    isLoadMore = false;
                }

            }

            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onFailed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        ViewUnits.getInstance().showToast(object.toString());
    }
}
