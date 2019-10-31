package tendency.hz.zhihuijiayuan.view.user;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.databinding.ActivityRebindingPhone1Binding;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PersonalPrenInter;
import tendency.hz.zhihuijiayuan.units.UserUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/12/19.
 */
public class ReBindingPhone1Activity extends BaseActivity implements AllViewInter {
    private ActivityRebindingPhone1Binding mBinding;
    private PersonalPrenInter mPersonalPrenInter;

    /**
     * 记时对象
     */
    private CountDownTimer mTimer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long l) {
            mBinding.btnGetSms.setText((l / 1000) + " s");
            mBinding.btnGetSms.setTextColor(getResources().getColor(R.color.text_gray9));
            mBinding.btnGetSms.setClickable(false);
        }

        @Override
        public void onFinish() {
            mBinding.btnGetSms.setText("重新发送");
            mBinding.btnGetSms.setTextColor(getResources().getColor(R.color.colorAccent));
            mBinding.btnGetSms.setClickable(true);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_rebinding_phone1);
        ViewUnits.getInstance().setTitleHeight(mBinding.layoutTitle);
        mPersonalPrenInter = new PersonalPrenImpl(this);

        initView();

        setLisntener();
    }

    private void initView() {
        mBinding.edtPhone.setText(UserUnits.getInstance().getPhone());
    }

    private void setLisntener() {
        mBinding.btnGetSms.setOnClickListener(view -> {
            ViewUnits.getInstance().showToast("短信验证码正在发送");
            mPersonalPrenInter.changePhoneSendSms(NetCode.User2.changePhoneSendSms);
            mTimer.start();
        });

        mBinding.edtSms.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mBinding.btnResetPhone.setEnabled(false);
                if (editable.toString().length() == 4) {
                    mBinding.btnResetPhone.setEnabled(true);
                }
            }
        });

        mBinding.btnResetPhone.setOnClickListener(view -> {
            ViewUnits.getInstance().showLoading(ReBindingPhone1Activity.this, "校验中");
            mPersonalPrenInter.changePhoneCheckCode(NetCode.User2.changePhoneCheckSms, mBinding.edtSms.getText().toString());
        });
    }

    @Override
    public void onSuccessed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        switch (what) {
            case NetCode.User2.changePhoneSendSms:
                break;
            case NetCode.User2.changePhoneCheckSms:
                Intent intent = new Intent(this, ReBindingPhone2Activity.class);
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
        mPersonalPrenInter = null;
        mTimer = null;
    }
}
