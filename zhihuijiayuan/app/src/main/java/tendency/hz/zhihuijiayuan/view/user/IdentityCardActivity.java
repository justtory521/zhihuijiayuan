package tendency.hz.zhihuijiayuan.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
 * Author：Libin on 2019/5/16 13:43
 * Email：1993911441@qq.com
 * Describe：修改身份证
 */
/**
 * Author：Libin on 2019/5/16 13:43
 * Email：1993911441@qq.com
 * Describe：修改身份证
 */
public class IdentityCardActivity extends BaseActivity {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.tv_edit_time)
    TextView tvEditTime;
    @BindView(R.id.tv_real_name)
    TextView tvRealName;
    @BindView(R.id.tv_card_number)
    TextView tvCardNumber;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indentity_card);
        ButterKnife.bind(this);
        ViewUnits.getInstance().setTitleHeight(viewStatusBar);
        tvTitleName.setText("身份证");
        llTitle.setBackgroundResource(R.color.blue_0d8);
        getData();
    }

    private void getData() {
        Intent intent = getIntent();
        String cardId = intent.getStringExtra("cardId");
        String realName = intent.getStringExtra("realName");

        if (!TextUtils.isEmpty(realName)) {
            tvRealName.setText(realName);

        }

        if (!TextUtils.isEmpty(cardId)) {
            tvCardNumber.setText(cardId);
        }


        tvEditTime.setText("请前往 我的>身份认证 编辑身份证相关信息");

    }


    @OnClick(R.id.iv_title_back)
    public void onViewClicked() {
        finish();
    }
}

