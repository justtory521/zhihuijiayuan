package tendency.hz.zhihuijiayuan.view.user;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.databinding.ActivityFindPwdStep1Binding;
import tendency.hz.zhihuijiayuan.presenter.UserPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.UserPrenInter;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/12/19.
 */
public class FindPwdStep1Activity extends BaseActivity implements AllViewInter {
    private ActivityFindPwdStep1Binding mBinding;
    private UserPrenInter mUserPrenInter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_find_pwd_step1);
        mUserPrenInter = new UserPrenImpl(this);

        setListener();
    }

    private void setListener() {
        mBinding.edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mBinding.btnNext.setBackgroundColor(getResources().getColor(R.color.bg_btn_unenable));
                if (editable.toString().length() >= 13) {
                    mBinding.btnNext.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });

        mBinding.btnNext.setOnClickListener(view -> {
            if (mBinding.edtPhone.getTextTrim(mBinding.edtPhone).length() < 11) {
                ViewUnits.getInstance().showToast("请输入完整的手机号");
                return;
            }
            ViewUnits.getInstance().showLoading(this, "发送中");
            mUserPrenInter.changePwdSMS(mBinding.edtPhone.getTextTrim(mBinding.edtPhone), NetCode.User.changePwdSendSms);
        });
    }

    @Override
    public void onSuccessed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        switch (what) {
            case NetCode.User.changePwdSendSms:
                ViewUnits.getInstance().showToast("发送成功，请查收");
                Intent intent = new Intent(this, FindPwdStep2Activity.class);
                intent.putExtra("phone", mBinding.edtPhone.getTextTrim(mBinding.edtPhone));
                startActivity(intent);
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
        mUserPrenInter = null;
    }
}
