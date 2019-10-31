package tendency.hz.zhihuijiayuan.view.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.adapter.MyFragmentPagerAdapter;
import tendency.hz.zhihuijiayuan.bean.User;
import tendency.hz.zhihuijiayuan.bean.UserRightsBean;
import tendency.hz.zhihuijiayuan.bean.base.Field;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.fragment.UserRightsFragment;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.units.DateUtils;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Author：Libin on 2019/5/15 20:14
 * Email：1993911441@qq.com
 * Describe：我的特权
 */
public class UserRightsActivity extends BaseActivity implements AllViewInter {
    @BindView(R.id.sdv_credit_avatar)
    SimpleDraweeView sdvCreditAvatar;
    @BindView(R.id.tv_credit_nickname)
    TextView tvCreditNickname;
    @BindView(R.id.tv_last_times)
    TextView tvLastTimes;
    @BindView(R.id.tv_last_month)
    TextView tvLastMonth;
    @BindView(R.id.tv_total_times)
    TextView tvTotalTimes;
    @BindView(R.id.stl_user_rights)
    SlidingTabLayout stlUserRights;
    @BindView(R.id.vp_user_rights)
    ViewPager vpUserRights;
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.ctl_rights)
    CollapsingToolbarLayout ctlRights;
    @BindView(R.id.cl_rights)
    CoordinatorLayout clRights;
    @BindView(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @BindView(R.id.tv_user_grade)
    TextView tvUserGrade;
    @BindView(R.id.ll_user_grade)
    LinearLayout llUserGrade;
    @BindView(R.id.tv_grade_left)
    TextView tvGradeLeft;
    @BindView(R.id.tv_grade_right)
    TextView tvGradeRight;
    @BindView(R.id.tv_title_more)
    TextView tvTitleMore;

    private PersonalPrenImpl mPersonalPrenInter;
    private User mUser;
    private List<UserRightsBean.DataBean> dataList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rights);
        ButterKnife.bind(this);

        init();


    }

    private void init() {
        ViewUnits.getInstance().setTitleHeight(viewStatusBar);

        tvTitleName.setText("我的特权");
        mPersonalPrenInter = new PersonalPrenImpl(this);
        llUserGrade.setVisibility(View.VISIBLE);

        ViewUnits.getInstance().showLoading(this, "加载中");

        mPersonalPrenInter.getPersonalInfo(NetCode.Personal.getPersonalInfo);
        mPersonalPrenInter.getMyEquity(NetCode.Personal.getMyEquity);


        appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i < 0) {
                    llTitle.setBackgroundResource(R.color.colorPrimary);
                } else {
                    llTitle.setBackground(null);
                }
            }
        });


    }

    private void initUserInfo() {
        sdvCreditAvatar.setImageURI(mUser.getHeadImgPath());
        tvCreditNickname.setText(mUser.getNickName());
        Integer previousCreditExecuteNum = mUser.getPreviousCreditExecuteNum();
        if (previousCreditExecuteNum != null) {
            tvLastTimes.setText(String.valueOf(previousCreditExecuteNum));
        } else {
            tvLastTimes.setText("0");
        }

        int score = mUser.getCreditScore();
        if (score == 0) {
            tvGradeLeft.setVisibility(View.VISIBLE);
            tvUserGrade.setVisibility(View.GONE);
            tvGradeRight.setVisibility(View.GONE);
            tvGradeLeft.setText("您还未参与智慧信用，暂无相关特权");
        } else {
            tvGradeLeft.setVisibility(View.VISIBLE);
            tvUserGrade.setVisibility(View.VISIBLE);
            tvGradeRight.setVisibility(View.VISIBLE);
            tvGradeLeft.setText("您的信用等级为");

            if (score <= 200) {
                tvUserGrade.setText("较差");
            } else if (score <= 350) {
                tvUserGrade.setText("普通");
            } else if (score <= 480) {
                tvUserGrade.setText("中等");
            } else if (score <= 580) {
                tvUserGrade.setText("良好");
            } else if (score <= 650) {
                tvUserGrade.setText("优秀");
            } else {
                tvUserGrade.setText("极好");
            }
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
    public void onSuccessed(int what, Object object) {
        super.onSuccessed(what, object);

        switch (what) {
            case NetCode.Personal.getPersonalInfo:
                mUser = (User) object;
                initUserInfo();
                break;
            case NetCode.Personal.getMyEquity:
                ViewUnits.getInstance().missLoading();
                dataList = (List<UserRightsBean.DataBean>) object;

                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appbarLayout.getLayoutParams();
                AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
                if (dataList !=null && dataList.size() > 0){
                    initRights();
                    behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                        @Override
                        public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                            return true;
                        }
                    });
                }else {
                    behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                        @Override
                        public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                            return false;
                        }
                    });

                }

                break;
        }
    }

    private void initRights() {
        List<Fragment> fragmentList = new ArrayList<>();

        List<String> titleList = new ArrayList<>();

        for (UserRightsBean.DataBean dataBean : dataList) {
            fragmentList.add(UserRightsFragment.newInstance(dataBean.getList()));
            titleList.add(dataBean.getClassName());
        }

        if (dataList.size() <= 3) {
            stlUserRights.setTabSpaceEqual(true);
        } else {
            stlUserRights.setTabSpaceEqual(false);
        }

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),
                fragmentList, titleList);
        vpUserRights.setOffscreenPageLimit(titleList.size());

        vpUserRights.setAdapter(adapter);
        stlUserRights.setViewPager(vpUserRights);
        stlUserRights.setCurrentTab(0);
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
