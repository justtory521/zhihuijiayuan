package tendency.hz.zhihuijiayuan.view.user;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.databinding.ActivityFindPwdStep3Binding;
import tendency.hz.zhihuijiayuan.presenter.UserPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.UserPrenInter;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/12/19.
 */
public class FindPwdStep3Activity extends BaseActivity implements AllViewInter {
    private ActivityFindPwdStep3Binding mBinding;
    private String mPhone, mCode;
    private UserPrenInter mUserPrenInter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_find_pwd_step3);
        mPhone = super.getIntent().getStringExtra("phone");
        mCode = super.getIntent().getStringExtra("code");
        mUserPrenInter = new UserPrenImpl(this);

        setListener();
    }

    private void setListener() {
        mBinding.edtPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mBinding.btnNext.setBackgroundColor(getResources().getColor(R.color.bg_btn_unenable));
                if (editable.toString().length() >= 6) {
                    mBinding.btnNext.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }

                if (editable.toString().length() > 22) {
                    mBinding.edtPwd.setText(editable.toString().substring(0, 22));
                }
            }
        });

        mBinding.btnNext.setOnClickListener(view -> {
            if (mBinding.edtPwd.getText().toString().length() < 6) {
                ViewUnits.getInstance().showToast("密码长度为6~22位");
                return;
            }
            ViewUnits.getInstance().showLoading(this, "修改中");
            mUserPrenInter.changePwd(mPhone, mBinding.edtPwd.getText().toString(), mCode, NetCode.User.changePwdEdit);
        });
    }

    @Override
    public void onSuccessed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        switch (what) {
            case NetCode.User.changePwdEdit:
                ViewUnits.getInstance().showToast("修改成功，请重新登录");
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("flag", Request.StartActivityRspCode.JUMP_TO_LOGINBYPWD);
                startActivity(intent);
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
        mPhone = null;
        mCode = null;
        mUserPrenInter = null;
    }
}
