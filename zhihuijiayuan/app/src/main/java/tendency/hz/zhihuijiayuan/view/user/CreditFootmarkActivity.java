package tendency.hz.zhihuijiayuan.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.User;
import tendency.hz.zhihuijiayuan.bean.UserServiceBean;
import tendency.hz.zhihuijiayuan.bean.base.Field;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PersonalPrenInter;
import tendency.hz.zhihuijiayuan.units.DateUtils;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.RVDividerItemDecoration;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Author：Libin on 2019/5/15 11:34
 * Email：1993911441@qq.com
 * Describe：信用足迹
 */
public class CreditFootmarkActivity extends BaseActivity implements AllViewInter {
    @BindView(R.id.sdv_credit_avatar)
    SimpleDraweeView sdvAvatar;
    @BindView(R.id.tv_credit_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_last_times)
    TextView tvLastTimes;
    @BindView(R.id.tv_last_month)
    TextView tvLastMonth;
    @BindView(R.id.tv_total_times)
    TextView tvTotalTimes;
    @BindView(R.id.tv_discount_money)
    TextView tvDiscountMoney;
    @BindView(R.id.rv_finish_serve)
    RecyclerView rvFinishServe;
    @BindView(R.id.rv_join_serve)
    RecyclerView rvJoinServe;
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.nsv_footmask)
    NestedScrollView nsvFootmask;
    private PersonalPrenInter mPersonalPrenInter;

    private List<UserServiceBean.DataBean.ListBean> finishList = new ArrayList<>();
    private List<UserServiceBean.DataBean.ListBean> joinList = new ArrayList<>();
    private BaseQuickAdapter<UserServiceBean.DataBean.ListBean, BaseViewHolder> joinAdapter;
    private BaseQuickAdapter<UserServiceBean.DataBean.ListBean, BaseViewHolder> finishAdapter;
    private User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_footmask);
        ButterKnife.bind(this);
        init();


        mPersonalPrenInter = new PersonalPrenImpl(this);

        ViewUnits.getInstance().showLoading(this, "加载中");

        mPersonalPrenInter.getPersonalInfo(NetCode.Personal.getPersonalInfo);
        mPersonalPrenInter.getEquityList(NetCode.Personal.getEquityList);
        mPersonalPrenInter.getExecuteList(NetCode.Personal.getExecuteList);

        initFinished();
        initJoin();
    }

    private void init() {
        ViewUnits.getInstance().setTitleHeight(viewStatusBar);
        tvTitleName.setText("信用足迹");
        nsvFootmask.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int i, int i1, int i2, int i3) {
                int y = ViewUnits.getInstance().px2dip(i1);
                if (y > 8) {
                    llTitle.setBackgroundResource(R.color.colorPrimary);
                } else {
                    llTitle.setBackground(null);
                }
            }
        });
    }

    /**
     * 参与的服务
     */
    private void initJoin() {
        rvJoinServe.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvJoinServe.addItemDecoration(new RVDividerItemDecoration(this));
        joinAdapter = new BaseQuickAdapter<UserServiceBean.DataBean.ListBean, BaseViewHolder>(R.layout.rv_footmask_item, joinList) {
            @Override
            protected void convert(BaseViewHolder helper, UserServiceBean.DataBean.ListBean item) {
                helper.setText(R.id.tv_service_name, item.getServiceName());
                helper.setText(R.id.tv_service_content, item.getDescribe());
                TextView tvMoney = helper.getView(R.id.tv_service_time);
                Integer num = item.getNum();
                if (num != null) {
                    tvMoney.setText(num + "次");
                } else {
                    tvMoney.setText("0次");
                }
            }
        };
        rvJoinServe.setAdapter(joinAdapter);


        joinAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(CreditFootmarkActivity.this, CreditRecordActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
            }
        });
    }

    /**
     * 已获得服务
     */
    private void initFinished() {
        rvFinishServe.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        //分割线
        rvFinishServe.addItemDecoration(new RVDividerItemDecoration(this));
        finishAdapter = new BaseQuickAdapter<UserServiceBean.DataBean.ListBean, BaseViewHolder>(R.layout.rv_footmask_item, finishList) {
            @Override
            protected void convert(BaseViewHolder helper, UserServiceBean.DataBean.ListBean item) {
                helper.setText(R.id.tv_service_name, item.getServiceName());
                helper.setText(R.id.tv_service_content, item.getDescribe());
                TextView tvMoney = helper.getView(R.id.tv_service_time);
                Double valuation = item.getValuation();
                if (valuation != null) {
                    tvMoney.setText(FormatUtils.getInstance().intFormat(valuation) + "元");
                } else {
                    tvMoney.setText("0.00元");
                }
            }
        };
        rvFinishServe.setAdapter(finishAdapter);

    }

    @Override
    public void onSuccessed(int what, Object object) {
        super.onSuccessed(what, object);
        switch (what) {
            case NetCode.Personal.getPersonalInfo:
                ViewUnits.getInstance().missLoading();
                mUser = (User) object;
                initUserInfo();
                break;
            case NetCode.Personal.getEquityList:
                UserServiceBean.DataBean dataBean = (UserServiceBean.DataBean) object;
                Double totalValuation = dataBean.getTotalValuation();

                if (totalValuation != null) {
                    tvDiscountMoney.setText("共计为您节约了" + FormatUtils.getInstance().intFormat(totalValuation) + "元");
                } else {
                    tvDiscountMoney.setText("共计为您节约了0.00元");
                }

                List<UserServiceBean.DataBean.ListBean> list1 = dataBean.getList();

                if (list1 != null && list1.size() > 0) {

                    finishList.addAll(list1);
                    finishAdapter.notifyDataSetChanged();
                }


                break;
            case NetCode.Personal.getExecuteList:
                List<UserServiceBean.DataBean.ListBean> list2 = (List<UserServiceBean.DataBean.ListBean>) object;
                if (list2 != null && list2.size() > 0) {
                    joinList.addAll(list2);
                    joinAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private void initUserInfo() {
        sdvAvatar.setImageURI(mUser.getHeadImgPath());
        tvNickname.setText(mUser.getNickName());
        Integer previousCreditExecuteNum = mUser.getPreviousCreditExecuteNum();
        if (previousCreditExecuteNum != null) {
            tvLastTimes.setText(String.valueOf(previousCreditExecuteNum));
        } else {
            tvLastTimes.setText("0");
        }

        Integer creditExecuteNum = mUser.getCreditExecuteNum();
        if (creditExecuteNum != null) {
            tvTotalTimes.setText(String.valueOf(creditExecuteNum));
        } else {
            tvTotalTimes.setText("0");
        }

        String creditAssessMonthTime = mUser.getCreditAssessMonthTime();
        if (!TextUtils.isEmpty(creditAssessMonthTime)) {
            tvLastMonth.setText(Integer.parseInt(DateUtils.formatTime(Field.DateType.standard_time_format,
                    Field.DateType.month, creditAssessMonthTime)) + "月履约次数");
        } else {
            tvLastMonth.setText(Integer.valueOf(DateUtils.getLastMonth()) + "月履约次数");
        }

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
