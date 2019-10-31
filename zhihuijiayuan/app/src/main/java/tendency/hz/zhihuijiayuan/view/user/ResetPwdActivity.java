package tendency.hz.zhihuijiayuan.view.user;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.databinding.ActivityResetPwdBinding;
import tendency.hz.zhihuijiayuan.presenter.UserPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.UserPrenInter;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/4/3.
 */

public class ResetPwdActivity extends BaseActivity implements AllViewInter {
    private String mPassword1, mPassword2;

    private UserPrenInter mUserPrenInter;

    private ActivityResetPwdBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_reset_pwd);
        ViewUnits.getInstance().setTitleHeight(mBinding.layoutTitle);

        mUserPrenInter = new UserPrenImpl(this);

        setListener();
    }

    private void setListener() {
        mBinding.btnResetPwd.setOnClickListener(view -> {
            if (checkPwd()) {
                ViewUnits.getInstance().showLoading(this, "请求中");
                mUserPrenInter.changePassWord(mPassword1, mPassword1, NetCode.User.changePwd);
            }
        });

        findViewById(R.id.img_btn_back).setOnClickListener(view -> finish());

        mBinding.checkboxEyes1.setOnCheckedChangeListener((compoundButton, b) -> {
            mBinding.edtPwd1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            if (b) {
                mBinding.edtPwd1.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            mBinding.edtPwd1.setSelection(mBinding.edtPwd1.getText().toString().length());
        });

        mBinding.checkboxEyes2.setOnCheckedChangeListener((compoundButton, b) -> {
            mBinding.edtPwd2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            if (b) {
                mBinding.edtPwd2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            mBinding.edtPwd2.setSelection(mBinding.edtPwd2.getText().toString().length());
        });

        mBinding.edtPwd1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mBinding.edtPwd1.setSelection(mBinding.edtPwd1.getText().toString().length());
                checkFormat();
            }
        });

        mBinding.edtPwd2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mBinding.edtPwd2.setSelection(mBinding.edtPwd2.getText().toString().length());
                checkFormat();
            }
        });
    }

    private void checkFormat() {
        mBinding.btnResetPwd.setEnabled(false);
        if ((mBinding.edtPwd1.getText().toString().length() >= 6) &&
                mBinding.edtPwd2.getText().toString().length() >= 6) {
            mBinding.btnResetPwd.setEnabled(true);
        }
    }

    /**
     * 校验密码录入情况
     *
     * @return
     */
    private boolean checkPwd() {
        mPassword1 = mBinding.edtPwd1.getText().toString();
        mPassword2 = mBinding.edtPwd2.getText().toString();
        if (FormatUtils.getInstance().isEmpty(mPassword1) || FormatUtils.getInstance().isEmpty(mPassword2)) {
            ViewUnits.getInstance().showToast("请录入完整内容");
            return false;
        }

        if (!mPassword1.equals(mPassword2)) {
            ViewUnits.getInstance().showToast("两次密码不一致");
            return false;
        }

        return true;
    }

    @Override
    public void onSuccessed(int what, Object object) {
        switch (what) {
            case NetCode.User.changePwd:
                ViewUnits.getInstance().missLoading();
                ViewUnits.getInstance().showToast("修改成功");
                finish();
                break;
        }
    }

    @Override
    public void onFailed(int what, Object object) {
        ViewUnits.getInstance().showToast(object.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPassword1 = null;
        mPassword2 = null;
        mUserPrenInter = null;
    }
}
