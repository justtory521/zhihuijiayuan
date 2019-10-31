package tendency.hz.zhihuijiayuan.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;

/**
 * Author：Libin on 2019/5/16 09:39
 * Email：1993911441@qq.com
 * Describe：相关协议
 */
public class UserAgreementActivity extends BaseActivity {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ViewUnits.getInstance().setTitleHeight(viewStatusBar);
        llTitle.setBackgroundResource(R.color.colorPrimary);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");

        tvTitleName.setText(title);
        tvAgreement.setText(content);
    }

    @OnClick(R.id.iv_title_back)
    public void onViewClicked() {
        finish();
    }
}
