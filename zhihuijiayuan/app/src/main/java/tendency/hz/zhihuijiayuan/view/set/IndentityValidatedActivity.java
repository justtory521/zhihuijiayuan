package tendency.hz.zhihuijiayuan.view.set;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.databinding.ActivityIndentityValidatedBinding;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.UserUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;

/**
 * Created by JasonYao on 2018/4/1.
 */

public class IndentityValidatedActivity extends BaseActivity {
    private ActivityIndentityValidatedBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_indentity_validated);


        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.textName.setText(UserUnits.getInstance().getRealName());
        mBinding.icHead.setImageURI(UserUnits.getInstance().getHeadImgPath());
        mBinding.textPhone.setText(UserUnits.getInstance().getPhone());
        mBinding.textCardId.setText(FormatUtils.getInstance().encryptIDCardNo(UserUnits.getInstance().getCardId()));
        mBinding.textNameUser.setText(UserUnits.getInstance().getNickName());
    }

    private void initView() {
        ViewUnits.getInstance().setTitleHeight(mBinding.layoutTitle);
        mBinding.tvEditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndentityValidatedActivity.this,ValidateActivity.class));
            }
        });
    }

}
