package tendency.hz.zhihuijiayuan.view.user;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.User;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.databinding.ActivityFindPwdStep2Binding;
import tendency.hz.zhihuijiayuan.presenter.UserPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.UserPrenInter;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;
import tendency.hz.zhihuijiayuan.widget.VerificationCodeView;

/**
 * Created by JasonYao on 2018/12/19.
 */
public class FindPwdStep2Activity extends BaseActivity implements AllViewInter {
    private ActivityFindPwdStep2Binding mBinding;
    private String mPhone;
    private UserPrenInter mUserPrenInter;

    /**
     * 记时对象
     */
    private CountDownTimer mTimer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long l) {
            if (mBinding.textTime !=null){
                mBinding.textTime.setText((l / 1000) + "秒后重新发送");
                mBinding.textTime.setTextColor(getResources().getColor(R.color.text_gray9));
                mBinding.textTime.setClickable(false);
            }

        }

        @Override
        public void onFinish() {
            mBinding.textTime.setText("重新发送");
            mBinding.textTime.setTextColor(getResources().getColor(R.color.colorAccent));
            mBinding.textTime.setClickable(true);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_find_pwd_step2);
        mUserPrenInter = new UserPrenImpl(this);

        mPhone = super.getIntent().getStringExtra("phone");
        mBinding.textPhone.setText(mPhone);

        mTimer.start();

        setListener();
    }

    private void setListener() {
        mBinding.editSmsCode.setInputCompleteListener(new VerificationCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                if (mBinding.editSmsCode.getInputContent().length() == 4) {
                    ViewUnits.getInstance().showLoading(FindPwdStep2Activity.this, "请求中");
                    mUserPrenInter.checkChangePwdSMS(mPhone, mBinding.editSmsCode.getInputContent(), NetCode.User.changePwdCheckSms);
                }
            }

            @Override
            public void deleteContent() {

            }
        });

        mBinding.textTime.setOnClickListener(view -> {
            ViewUnits.getInstance().showLoading(FindPwdStep2Activity.this, "正在发送验证码");
            mTimer.start();
            mUserPrenInter.sendLoginSmsCode(NetCode.User.changePwdSendSms, mPhone);
        });
    }

    @Override
    public void onSuccessed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        switch (what) {
            case NetCode.User.changePwdSendSms:
                break;
            case NetCode.User.changePwdCheckSms:
                User user = (User) object;
                Intent intent = new Intent(this, FindPwdStep3Activity.class);
                intent.putExtra("phone", user.getAccount());
                intent.putExtra("code", user.getCode());
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
        mPhone=null;
        mUserPrenInter=null;
    }
}
