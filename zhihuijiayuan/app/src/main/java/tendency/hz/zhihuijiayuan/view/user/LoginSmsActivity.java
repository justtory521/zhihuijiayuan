package tendency.hz.zhihuijiayuan.view.user;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import tendency.hz.zhihuijiayuan.MainActivity;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.databinding.ActivitySmsBinding;
import tendency.hz.zhihuijiayuan.inter.LoginResultListener;
import tendency.hz.zhihuijiayuan.presenter.CardPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.UserPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.CardPrenInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PersonalPrenInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.UserPrenInter;
import tendency.hz.zhihuijiayuan.units.CacheUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.card.CardContentActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;
import tendency.hz.zhihuijiayuan.widget.VerificationCodeView;

/**
 * Created by JasonYao on 2018/12/18.
 */
public class LoginSmsActivity extends BaseActivity implements AllViewInter {
    private static final String TAG = "LoginSmsActivity---";
    private ActivitySmsBinding mBinding;
    private String mPhone;
    private UserPrenInter mUserPrenInter;
    private CardPrenInter mCardPrenInter;
    private PersonalPrenInter mPersonalPrenInter;
    private static LoginResultListener mListener;
    private static String mCallBack;

    /**
     * 记时对象
     */
    private CountDownTimer mTimer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long l) {
            mBinding.textTime.setText((l / 1000) + "秒后重新发送");
            mBinding.textTime.setTextColor(getResources().getColor(R.color.text_gray9));
            mBinding.textTime.setClickable(false);
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
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sms);
        EventBus.getDefault().register(this);
        mUserPrenInter = new UserPrenImpl(this);
        mPersonalPrenInter = new PersonalPrenImpl(this);
        mCardPrenInter = new CardPrenImpl(this);

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
                    ViewUnits.getInstance().showLoading(LoginSmsActivity.this, "登录中");
                    mUserPrenInter.loginBySms(NetCode.User2.loginBySms, mPhone, mBinding.editSmsCode.getInputContent());
                }
            }

            @Override
            public void deleteContent() {

            }
        });

        mBinding.textTime.setOnClickListener(view -> {
            ViewUnits.getInstance().showLoading(LoginSmsActivity.this, "正在发送验证码");
            mTimer.start();
            mUserPrenInter.sendLoginSmsCode(NetCode.User2.sendLoginSms, mPhone);
        });

        mBinding.editSmsCode.getEditText().setFocusable(true);
    }

    public static void setLoginResultListener(String callBack, LoginResultListener listener) {
        mListener = listener;
        mCallBack = callBack;
    }

    @Override
    public void onSuccessed(int what, Object object) {
        switch (what) {
            case NetCode.User2.loginBySms:
                mCardPrenInter.infoSync(NetCode.Card.infoSync, CacheUnits.getInstance().getMyCacheCardIds());   //同步卡片
                break;
            case NetCode.Card.infoSync:
                mPersonalPrenInter.getPersonalInfo(NetCode.Personal.getPersonalInfo);  //获取用户信息
                break;
            case NetCode.Personal.getPersonalInfo:
                ViewUnits.getInstance().missLoading();
                ViewUnits.getInstance().showToast("登录成功");
                if (LoginActivity.mFlag == Request.StartActivityRspCode.CARD_JUMP_TO_LOGIN) {   //改标识表示从卡片页面跳转过来
                    mListener.getLoginResultListener(mCallBack, "1");
                    EventBus.getDefault().post("login_success");
                    finish();
                } else {
                    Intent intent = new Intent(this, MainActivity.class);  //跳转至首页
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                break;
            case NetCode.User2.sendLoginSms:
                ViewUnits.getInstance().missLoading();
                break;
        }
    }

    @Override
    public void onFailed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        ViewUnits.getInstance().showToast(object.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginSuccess(String msg) {
        if (msg.equals("login_success")) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPhone = null;
        mUserPrenInter = null;
        mCardPrenInter = null;
        mPersonalPrenInter = null;
        mListener = null;
        mCallBack = null;
        EventBus.getDefault().unregister(this);
    }
}
