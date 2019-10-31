package tendency.hz.zhihuijiayuan.view.user;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.databinding.ActivityResetNicknameBinding;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.UserUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/11/2.
 */
public class ResetNickNameActivity extends BaseActivity implements AllViewInter {
    private ActivityResetNicknameBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_reset_nickname);
        ViewUnits.getInstance().setTitleHeight(mBinding.layoutTitle);
        mBinding.edtNickname.setText(UserUnits.getInstance().getNickName());
    }

    public void resetNickName(View view) {
        if (FormatUtils.getInstance().isEmpty(mBinding.edtNickname.getText().toString())) {
            ViewUnits.getInstance().showToast("请输入完整昵称");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("nickName", mBinding.edtNickname.getText().toString());
        this.setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onSuccessed(int what, Object object) {
        switch (what) {
            case NetCode.Personal.updatePersonalInfo:
                ViewUnits.getInstance().missLoading();
                ViewUnits.getInstance().showToast("修改成功");
                finish();
                break;
        }
    }

    @Override
    public void onFailed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        ViewUnits.getInstance().showToast(object.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
