package tendency.hz.zhihuijiayuan.view.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.UserRankBean;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PersonalPrenInter;
import tendency.hz.zhihuijiayuan.units.RVDividerItemDecoration;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Author：Libin on 2019/5/14 16:18
 * Email：1993911441@qq.com
 * Describe：信用排行
 */
public class CreditRankActivity extends BaseActivity implements AllViewInter {
    @BindView(R.id.rv_credit_rank)
    RecyclerView rvCreditRank;
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.iv_rank_badge)
    ImageView ivRankBadge;
    @BindView(R.id.tv_rank_badge)
    TextView tvRankBadge;
    @BindView(R.id.sdv_user_head)
    SimpleDraweeView sdvUserHead;
    @BindView(R.id.tv_user_nickname)
    TextView tvUserNickname;
    @BindView(R.id.tv_credit_times)
    TextView tvCreditTimes;
    @BindView(R.id.tv_rank_score)
    TextView tvRankScore;
    @BindView(R.id.tv_rank_city)
    TextView tvRankCity;
    @BindView(R.id.tv_current_rank)
    TextView tvCurrentRank;
    @BindView(R.id.tv_user_rank_score)
    TextView tvUserRankScore;
    @BindView(R.id.rl_rank_header)
    RelativeLayout rlRankHeader;
    @BindView(R.id.view_rank)
    View viewRank;
    @BindView(R.id.nsv_rank)
    NestedScrollView nsvRank;

    private UserRankBean.DataBean rankBean;
    private PersonalPrenInter mPersonalPrenInter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_rank);
        ButterKnife.bind(this);

        ViewUnits.getInstance().setTitleHeight(viewStatusBar);
        tvTitleName.setText("信用排行");

        mPersonalPrenInter = new PersonalPrenImpl(this);

        ViewUnits.getInstance().showLoading(this, "加载中");

        mPersonalPrenInter.creditRankingwushi(NetCode.Personal.creditRankingwushi);

    }

    private void initView() {

        Integer rank = rankBean.getPreviousCreditScoreRanking();

        if (rank != null) {

            nsvRank.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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
            rlRankHeader.setVisibility(View.VISIBLE);
            viewRank.setVisibility(View.GONE);
            tvRankCity.setText(String.valueOf(rankBean.getDistrictName()));
            tvCurrentRank.setText(String.valueOf(rank));
            if (rank <= 3) {
                ivRankBadge.setVisibility(View.VISIBLE);
                tvRankBadge.setVisibility(View.GONE);
                switch (rank) {
                    case 1:
                        ivRankBadge.setImageResource(R.mipmap.ic_rank_first);
                        break;
                    case 2:
                        ivRankBadge.setImageResource(R.mipmap.ic_rank_second);
                        break;
                    case 3:
                        ivRankBadge.setImageResource(R.mipmap.ic_rank_third);
                        break;
                }
            } else {
                ivRankBadge.setVisibility(View.GONE);
                tvRankBadge.setVisibility(View.VISIBLE);
                tvRankBadge.setText(String.valueOf(rank));
                tvRankBadge.setTextColor(ContextCompat.getColor(CreditRankActivity.this, R.color.blue_0d8));
            }

            sdvUserHead.setImageURI(rankBean.getHeadImg());
            tvUserNickname.setText(rankBean.getNickName());
            tvCreditTimes.setText("履约" + rankBean.getCreditExecuteNum() + "次");

            Integer score = rankBean.getCreditScore();
            if (score != null) {
                tvRankScore.setText(String.valueOf(score));
                tvUserRankScore.setText("智慧分： " + score + "分");
            } else {
                tvRankScore.setText("0");
                tvUserRankScore.setText("智慧分： 0分");
            }


            tvRankScore.setTextColor(ContextCompat.getColor(CreditRankActivity.this, R.color.blue_0d8));

        } else {
            llTitle.setBackgroundResource(R.color.colorPrimary);
            rlRankHeader.setVisibility(View.GONE);
            viewRank.setVisibility(View.VISIBLE);
        }

        //rv禁止滑动
        rvCreditRank.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        //分割线
        rvCreditRank.addItemDecoration(new RVDividerItemDecoration(this));

        BaseQuickAdapter<UserRankBean.DataBean.ListBean, BaseViewHolder> adapter
                = new BaseQuickAdapter<UserRankBean.DataBean.ListBean, BaseViewHolder>(R.layout.rv_credit_rank_item, rankBean.getList()) {
            @Override
            protected void convert(BaseViewHolder helper, UserRankBean.DataBean.ListBean item) {
                ImageView ivBadge = helper.getView(R.id.iv_rank_badge);
                TextView tvBadge = helper.getView(R.id.tv_rank_badge);

                String rank = item.getPreviousCreditScoreRanking();
                if (!TextUtils.isEmpty(rank)) {

                    if (Integer.parseInt(rank) <= 3) {
                        ivBadge.setVisibility(View.VISIBLE);
                        tvBadge.setVisibility(View.GONE);
                        switch (Integer.parseInt(rank)) {
                            case 1:
                                ivBadge.setImageResource(R.mipmap.ic_rank_first);
                                break;
                            case 2:
                                ivBadge.setImageResource(R.mipmap.ic_rank_second);
                                break;
                            case 3:
                                ivBadge.setImageResource(R.mipmap.ic_rank_third);
                                break;
                        }
                    } else {
                        ivBadge.setVisibility(View.GONE);
                        tvBadge.setVisibility(View.VISIBLE);
                        tvBadge.setText(rank);
                        tvBadge.setTextColor(ContextCompat.getColor(CreditRankActivity.this, R.color.gray_88));
                    }
                } else {
                    ivBadge.setVisibility(View.GONE);
                    tvBadge.setVisibility(View.VISIBLE);
                    tvBadge.setText("");
                    tvBadge.setTextColor(ContextCompat.getColor(CreditRankActivity.this, R.color.gray_88));
                }


                SimpleDraweeView sdvAvatar = helper.getView(R.id.sdv_user_head);
                sdvAvatar.setImageURI(String.valueOf(item.getHeadImg()));
                helper.setText(R.id.tv_user_nickname, item.getNickName());
                helper.setText(R.id.tv_credit_times, "履约" + item.getCreditExecuteNum() + "次");
                TextView tvScore = helper.getView(R.id.tv_rank_score);
                Integer score = item.getCreditScore();
                if (score != null) {
                    tvScore.setText(String.valueOf(score));
                } else {
                    tvScore.setText("0");
                }

                tvScore.setTextColor(ContextCompat.getColor(CreditRankActivity.this, R.color.orange_fdb));
            }
        };


        rvCreditRank.setAdapter(adapter);

    }


    @Override
    public void onSuccessed(int what, Object object) {
        super.onSuccessed(what, object);
        ViewUnits.getInstance().missLoading();
        if (what == NetCode.Personal.creditRankingwushi) {
            rankBean = (UserRankBean.DataBean) object;
            initView();
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
