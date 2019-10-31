package tendency.hz.zhihuijiayuan.view.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.adapter.CreditPagerAdapter;
import tendency.hz.zhihuijiayuan.adapter.UserRankAdapter;
import tendency.hz.zhihuijiayuan.bean.User;
import tendency.hz.zhihuijiayuan.bean.UserRankBean;
import tendency.hz.zhihuijiayuan.bean.UserServeBean;
import tendency.hz.zhihuijiayuan.bean.base.Field;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PersonalPrenInter;
import tendency.hz.zhihuijiayuan.units.DateUtils;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.picker.CityPickerActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Author：Libin on 2019/5/13 14:59
 * Email：1993911441@qq.com
 * Describe：智慧积分
 */
public class UserCreditActivity extends BaseActivity implements AllViewInter {
    @BindView(R.id.tv_user_credit_points)
    TextView tvUserPoints;
    @BindView(R.id.rl_user_credit)
    RelativeLayout rlUserCredit;
    @BindView(R.id.tv_assess_time)
    TextView tvAssessTime;
    @BindView(R.id.iv_user_credit_grade)
    ImageView ivUserGrade;
    @BindView(R.id.vp_user_activity)
    ViewPager vpUserActivity;
    @BindView(R.id.rg_activity_indicator)
    RadioGroup rgIndicator;
    @BindView(R.id.tv_current_city)
    TextView tvCurrentCity;
    @BindView(R.id.tv_more_rank)
    TextView tvMoreRank;
    @BindView(R.id.rv_user_rank)
    RecyclerView rvUserRank;
    @BindView(R.id.tv_select_city)
    TextView tvSelectCity;
    @BindView(R.id.sv_user_credit)
    NestedScrollView svUserCredit;
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.iv_title_more)
    ImageView ivTitleMore;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.tv_user_privilege)
    TextView tvUserPrivilege;
    @BindView(R.id.cv_user_credit)
    CardView cvUserCredit;
    @BindView(R.id.cv_user_serve)
    CardView cvUserServe;

    private PersonalPrenInter mPersonalPrenInter;
    private List<UserServeBean.DataBean> serveList = new ArrayList<>();
    private List<UserRankBean.DataBean.ListBean> rankList = new ArrayList<>();
    private User mUser;
    private int[] mCreditGrade = {R.mipmap.img_credit_first, R.mipmap.img_credit_second,
            R.mipmap.img_credit_third, R.mipmap.img_credit_fourth,
            R.mipmap.img_credit_fifth, R.mipmap.img_credit_sixth};
    private UserRankBean.DataBean rankBean;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_credit);
        ButterKnife.bind(this);

        initView();

        mPersonalPrenInter = new PersonalPrenImpl(this);

        ViewUnits.getInstance().showLoading(this, "加载中");

        mPersonalPrenInter.getPersonalInfo(NetCode.Personal.getPersonalInfo);
        mPersonalPrenInter.getMyServiceList(NetCode.Personal.getMyServiceList);
        mPersonalPrenInter.creditRankingsan(NetCode.Personal.creditRankingsan);


    }

    private void initView() {

        ViewUnits.getInstance().setTitleHeight(viewStatusBar);
        tvTitleName.setText("智慧信用");
        ivTitleMore.setVisibility(View.VISIBLE);
        svUserCredit.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int i, int i1, int i2, int i3) {
                int y = ViewUnits.getInstance().px2dip(i1);
                if (y > 50) {
                    llTitle.setBackgroundResource(R.color.colorPrimary);
                } else {
                    llTitle.setBackground(null);
                }
            }
        });


    }

    private void initUserInfo() {
        int score = mUser.getCreditScore();
        if (score <= 200) {
            ivUserGrade.setImageResource(mCreditGrade[0]);
        } else if (score <= 350) {
            ivUserGrade.setImageResource(mCreditGrade[1]);
        } else if (score <= 480) {
            ivUserGrade.setImageResource(mCreditGrade[2]);
        } else if (score <= 580) {
            ivUserGrade.setImageResource(mCreditGrade[3]);
        } else if (score <= 650) {
            ivUserGrade.setImageResource(mCreditGrade[4]);
        } else {
            ivUserGrade.setImageResource(mCreditGrade[5]);
        }
        tvUserPoints.setText(String.valueOf(score));
        tvAssessTime.setText("评估时间：" + DateUtils.formatTime(Field.DateType.standard_time_format,
                Field.DateType.year_month_day, mUser.getPreviousCreditTime()));
    }

    private void initServe() {
        CreditPagerAdapter adapter = new CreditPagerAdapter(this, serveList);
        vpUserActivity.setAdapter(adapter);

        for (int i = 0; i < serveList.size(); i++) {
            RadioButton radioButton = new RadioButton(this);

            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(18, 18);
            layoutParams.setMargins(10, 0, 10, 0);
            radioButton.setLayoutParams(layoutParams);
            radioButton.setButtonDrawable(null);
            radioButton.setClickable(false);
            radioButton.setBackgroundResource(R.drawable.selector_indicator);
            rgIndicator.addView(radioButton);
        }
        ((RadioButton) rgIndicator.getChildAt(0)).setChecked(true);

        vpUserActivity.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                ((RadioButton) rgIndicator.getChildAt(i)).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    /**
     * 排名城市
     */
    private void getRankCity() {
        if (rankBean != null) {
            if (!TextUtils.isEmpty(rankBean.getDistrictName())) {
                if (rankList.size() > 1) {
                    tvMoreRank.setEnabled(true);
                    tvSelectCity.setVisibility(View.GONE);
                    rvUserRank.setVisibility(View.VISIBLE);
                } else {
                    tvMoreRank.setEnabled(false);
                    tvSelectCity.setEnabled(false);
                    tvSelectCity.setVisibility(View.VISIBLE);
                    tvSelectCity.setText("暂无数据");
                    rvUserRank.setVisibility(View.GONE);
                }
                tvCurrentCity.setText("(" + rankBean.getDistrictName() + ")");
            } else {
                tvMoreRank.setEnabled(true);
                tvSelectCity.setVisibility(View.VISIBLE);
                tvSelectCity.setText("请选择您的当前所在城市");
                tvSelectCity.setEnabled(true);
                tvCurrentCity.setText("");
            }
        } else {
            tvMoreRank.setEnabled(false);
            tvSelectCity.setEnabled(false);
            tvSelectCity.setVisibility(View.VISIBLE);
            tvSelectCity.setText("暂无数据");
            rvUserRank.setVisibility(View.GONE);
        }


        rvUserRank.setLayoutManager(new GridLayoutManager(this, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });


        int count = rankList.size();

        if (count == 2) {
            int position = 0;
            for (int i = 0; i < rankList.size(); i++) {
                if (rankBean.getAccountId() == rankList.get(i).getAccountId()) {
                    position = i;
                }

                String previousCreditScoreRanking = rankList.get(i).getPreviousCreditScoreRanking();


                if (!TextUtils.isEmpty(previousCreditScoreRanking)) {
                    rankList.get(i).setPreviousCreditScoreRanking("第" + previousCreditScoreRanking + "名");
                } else {
                    rankList.get(i).setPreviousCreditScoreRanking("暂无排名");
                }
            }

            if (position == 0) {
                rankList.add(0, new UserRankBean.DataBean.ListBean(R.mipmap.ic_grade_first, "太棒了！", "您是最优秀的～"));
            } else {
                rankList.add(new UserRankBean.DataBean.ListBean(R.mipmap.ic_grade_third, "要加油噢！", "还需多多努力～"));
            }

        }

        if (count >= 3) {
            for (int i = 0; i < rankList.size(); i++) {

                String previousCreditScoreRanking = rankList.get(i).getPreviousCreditScoreRanking();
                if (!TextUtils.isEmpty(previousCreditScoreRanking)) {

                    rankList.get(i).setPreviousCreditScoreRanking("第" + previousCreditScoreRanking + "名");
                } else {
                    rankList.get(i).setPreviousCreditScoreRanking("暂无排名");
                }
            }
        }


        UserRankAdapter rankAdapter = new UserRankAdapter(this, rankList);
        rvUserRank.setAdapter(rankAdapter);


    }


    @OnClick({R.id.rl_user_credit, R.id.tv_more_rank, R.id.tv_select_city, R.id.iv_title_back, R.id.iv_title_more, R.id.tv_user_privilege})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_user_credit:
                startActivity(new Intent(UserCreditActivity.this, CreditFootmarkActivity.class));
                break;
            case R.id.tv_more_rank:
                if (rankBean != null && !TextUtils.isEmpty(rankBean.getDistrictName())) {
                    startActivity(new Intent(UserCreditActivity.this, CreditRankActivity.class));
                } else {
                    selectCity();
                }

                break;
            case R.id.tv_select_city:
                selectCity();
                break;
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.iv_title_more:
                showMore();
                break;
            case R.id.tv_user_privilege:
                startActivity(new Intent(UserCreditActivity.this, UserRightsActivity.class));
                break;
        }
    }

    /**
     * 选择城市
     */
    private void selectCity() {
        new AlertDialog.Builder(UserCreditActivity.this)
                .setTitle("提示")
                .setMessage("请完善“个人资料”中所在地数据")
                .setIcon(R.mipmap.logo)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    startActivityForResult(new Intent(UserCreditActivity.this, PersonalProfileActivity.class),
                            Request.StartActivityRspCode.CREDIT_TO_PERSONAL);
                })
                .setNegativeButton("取消", (dialogInterface, i) -> {

                })
                .create().show();
    }

    private void showMore() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popup_credit, null);
        PopupWindow popupWindow = new PopupWindow(contentView, ViewUnits.getInstance().dp2px(this, 120),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(contentView);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        popupWindow.showAsDropDown(ivTitleMore, -ViewUnits.getInstance().dp2px(this, 50),
                -ViewUnits.getInstance().dp2px(this, 10));


        TextView tvInfo = contentView.findViewById(R.id.tv_personal_info);
        TextView tvRecord = contentView.findViewById(R.id.tv_assess_record);
        TextView tvAgreement = contentView.findViewById(R.id.tv_relate_agreement);
        TextView tvAbout = contentView.findViewById(R.id.tv_understand_credit);

        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserCreditActivity.this, UserInfoActivity.class));
                popupWindow.dismiss();
            }
        });

        tvRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserCreditActivity.this, AccessRecordActivity.class));
                popupWindow.dismiss();
            }
        });

        tvAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserCreditActivity.this, UserAgreementActivity.class);
                intent.putExtra("title", "智慧信用隐私权政策");
                intent.putExtra("content", getString(R.string.user_agreement));
                startActivity(intent);
                popupWindow.dismiss();
            }
        });

        tvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserCreditActivity.this, UserAgreementActivity.class);
                intent.putExtra("title", "智慧信用规则说明");
                intent.putExtra("content", getString(R.string.user_agreement));
                startActivity(intent);

                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void onSuccessed(int what, Object object) {
        super.onSuccessed(what, object);
        switch (what) {
            case NetCode.Personal.getPersonalInfo:
                mUser = (User) object;
                initUserInfo();
                break;
            case NetCode.Personal.getMyServiceList:
                List<UserServeBean.DataBean> list = (List<UserServeBean.DataBean>) object;
                if (list != null && list.size() > 0) {
                    cvUserServe.setVisibility(View.VISIBLE);
                    serveList.addAll(list);
                    initServe();
                } else {
                    cvUserServe.setVisibility(View.GONE);
                }

                break;
            case NetCode.Personal.creditRankingsan:
                ViewUnits.getInstance().missLoading();
                rankBean = (UserRankBean.DataBean) object;

                rankList.clear();
                if (rankBean != null && rankBean.getList() != null) {
                    rankList.addAll(rankBean.getList());
                }
                getRankCity();

                break;
        }
    }


    @Override
    public void onFailed(int what, Object object) {
        super.onFailed(what, object);
        ViewUnits.getInstance().missLoading();
        ViewUnits.getInstance().showToast(object.toString());

        if (what == NetCode.Personal.creditRankingsan) {
            rankBean = null;
            rankList.clear();
            getRankCity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Request.StartActivityRspCode.CREDIT_TO_PERSONAL) {
            mPersonalPrenInter.creditRankingsan(NetCode.Personal.creditRankingsan);
        }
    }
}
