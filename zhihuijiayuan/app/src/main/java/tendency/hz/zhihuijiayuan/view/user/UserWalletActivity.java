package tendency.hz.zhihuijiayuan.view.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;

/**
 * Author：Libin on 2019/7/1 15:46
 * Description：智慧钱包
 */
public class UserWalletActivity extends BaseActivity {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_wallet);
        ButterKnife.bind(this);
        ViewUnits.getInstance().setTitleHeight(viewStatusBar);
        tvTitleName.setText("智慧钱包");
    }

    @OnClick(R.id.iv_title_back)
    public void onViewClicked() {
        finish();
    }
}
