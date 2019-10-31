package tendency.hz.zhihuijiayuan.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.adapter.MyFragmentPagerAdapter;
import tendency.hz.zhihuijiayuan.fragment.CreditRecordFragment;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Author：Libin on 2019/5/15 16:11
 * Email：1993911441@qq.com
 * Describe：信用记录
 */
public class CreditRecordActivity extends BaseActivity implements AllViewInter {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.stl_credit_record)
    SlidingTabLayout stlCreditRecord;
    @BindView(R.id.vp_credit_record)
    ViewPager vpCreditRecord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_record);
        ButterKnife.bind(this);

        initView();
    }


    private void initView() {
        int type = getIntent().getIntExtra("type",1);
        ViewUnits.getInstance().setTitleHeight(viewStatusBar);
        llTitle.setBackgroundResource(R.color.colorPrimary);
        tvTitleName.setText("信用记录");

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(CreditRecordFragment.newInstance(2));
        fragmentList.add(CreditRecordFragment.newInstance(1));
        List<String> titleList = new ArrayList<>();
        titleList.add("进行中");
        titleList.add("已完成");

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),
                fragmentList, titleList);
        vpCreditRecord.setOffscreenPageLimit(titleList.size());

        vpCreditRecord.setAdapter(adapter);
        stlCreditRecord.setViewPager(vpCreditRecord);
        if (type == 1){
            stlCreditRecord.setCurrentTab(0);
        }else {
            stlCreditRecord.setCurrentTab(1);
        }

    }

    @OnClick(R.id.iv_title_back)
    public void onViewClicked() {
        finish();
    }
}
