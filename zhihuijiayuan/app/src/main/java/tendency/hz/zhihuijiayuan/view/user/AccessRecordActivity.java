package tendency.hz.zhihuijiayuan.view.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.AccessRecordBean;
import tendency.hz.zhihuijiayuan.bean.User;
import tendency.hz.zhihuijiayuan.bean.base.Field;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PersonalPrenInter;
import tendency.hz.zhihuijiayuan.units.DateUtils;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Author：Libin on 2019/5/15 18:59
 * Email：1993911441@qq.com
 * Describe：评估记录
 */
public class AccessRecordActivity extends BaseActivity implements AllViewInter {
    @BindView(R.id.rv_access_record)
    RecyclerView rvAccessRecord;
    @BindView(R.id.srl_access_record)
    SmartRefreshLayout srlAccessRecord;
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;

    //当前页
    private int page = 1;
    //当前页
    private int pageSize = 10;
    //刷新
    private boolean isRefresh;
    //加载更多
    private boolean isLoadMore;
    private PersonalPrenInter mPersonalPrenInter;
    private User mUser;
    private BaseQuickAdapter<AccessRecordBean.DataBeanX.DataBean, BaseViewHolder> adapter;
    private List<AccessRecordBean.DataBeanX.DataBean> dataList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_record);
        ButterKnife.bind(this);
        ViewUnits.getInstance().showLoading(this, "加载中");
        mPersonalPrenInter = new PersonalPrenImpl(this);
        mPersonalPrenInter.getPersonalInfo(NetCode.Personal.getPersonalInfo);
        mPersonalPrenInter.getCreditHistory(NetCode.Personal.getCreditHistory, pageSize, page);
        initView();
    }

    private void initView() {
        ViewUnits.getInstance().setTitleHeight(viewStatusBar);
        tvTitleName.setText("评估记录");
        rvAccessRecord.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<AccessRecordBean.DataBeanX.DataBean, BaseViewHolder>(R.layout.rv_access_record_item, dataList) {
            @Override
            protected void convert(BaseViewHolder helper, AccessRecordBean.DataBeanX.DataBean item) {
                helper.setText(R.id.tv_current_score, "本月智慧分：" + item.getCreditIntegral());
                String createTime = item.getCreateTime();
                String time = "";

                if (createTime != null) {
                    if (createTime.contains("T")) {
                        time = DateUtils.formatTime(Field.DateType.year_month_day, Field.DateType.year_month, createTime.split("T")[0]);
                    } else {
                        time = DateUtils.formatTime(Field.DateType.standard_time_format, Field.DateType.year_month, createTime);
                    }
                }

                helper.setText(R.id.tv_current_month, "评估时间：" + time);
            }
        };
        rvAccessRecord.setAdapter(adapter);


        rvAccessRecord.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int position = layoutManager.findFirstCompletelyVisibleItemPosition();
                    if (position > 0) {
                        llTitle.setBackgroundResource(R.color.colorPrimary);
                    } else {
                        llTitle.setBackground(null);
                    }
                }

            }
        });

        srlAccessRecord.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isLoadMore = true;
                page++;
                mPersonalPrenInter.getCreditHistory(NetCode.Personal.getCreditHistory, pageSize, page);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                page = 1;
                mPersonalPrenInter.getCreditHistory(NetCode.Personal.getCreditHistory, pageSize, page);
            }
        });
    }

    @Override
    public void onSuccessed(int what, Object object) {
        super.onSuccessed(what, object);

        switch (what) {
            case NetCode.Personal.getPersonalInfo:
                mUser = (User) object;
                addHeader();

                break;
            case NetCode.Personal.getCreditHistory:

                List<AccessRecordBean.DataBeanX.DataBean> list = (List<AccessRecordBean.DataBeanX.DataBean>) object;

                if (isRefresh) {
                    srlAccessRecord.finishRefresh();
                    isRefresh = false;
                    dataList.clear();
                } else if (isLoadMore) {
                    srlAccessRecord.finishLoadMore();
                } else {
                    dataList.clear();
                    ViewUnits.getInstance().missLoading();
                }

                if (list != null && list.size() > 0) {
                    dataList.addAll(list);
                } else {
                    if (isLoadMore) {
                        page--;
                        srlAccessRecord.finishLoadMoreWithNoMoreData();
                        isLoadMore = false;
                    }
                }

                adapter.notifyDataSetChanged();

                break;
        }
    }

    private void addHeader() {
        View view = LayoutInflater.from(this).inflate(R.layout.rv_access_record_header, null);
        TextView tvTotalScore = view.findViewById(R.id.tv_record_total_score);
        TextView tvLastTime = view.findViewById(R.id.tv_record_last_time);

        tvTotalScore.setText("智慧分：" + mUser.getCreditScore());
        tvLastTime.setText("更新时间：" + DateUtils.formatTime(Field.DateType.standard_time_format,
                Field.DateType.year_month, mUser.getPreviousCreditTime()));

        adapter.addHeaderView(view);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onFailed(int what, Object object) {
        super.onFailed(what, object);
        ViewUnits.getInstance().missLoading();
        ViewUnits.getInstance().showToast(object.toString());
    }

    @OnClick(R.id.iv_title_back)
    public void onViewClicked() {
        finish();
    }
}
