package tendency.hz.zhihuijiayuan.view.user;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import tendency.hz.zhihuijiayuan.MainActivity;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.User;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.databinding.ActivityRegisterSmsBinding;
import tendency.hz.zhihuijiayuan.inter.LoginResultListener;
import tendency.hz.zhihuijiayuan.presenter.CardPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.UserPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.CardPrenInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PersonalPrenInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.UserPrenInter;
import tendency.hz.zhihuijiayuan.units.CacheUnits;
import tendency.hz.zhihuijiayuan.units.ConfigUnits;
import tendency.hz.zhihuijiayuan.units.LogUtils;
import tendency.hz.zhihuijiayuan.units.UserUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;
import tendency.hz.zhihuijiayuan.widget.VerificationCodeView;

/**
 * Created by JasonYao on 2018/12/18.
 */
public class RegisterSmsActivity extends BaseActivity implements AllViewInter {
    private UserPrenInter mUserPrenInter;
    private ActivityRegisterSmsBinding mBinding;
    private String mPhone;
    private int type;
    private String openId;
    private int openIdType;
    //手机号是否注册过
    private int isRegister;
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
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register_sms);
        mUserPrenInter = new UserPrenImpl(this);
        mCardPrenInter = new CardPrenImpl(this);
        mPersonalPrenInter = new PersonalPrenImpl(this);
        getData();

        mBinding.textPhone.setText(mPhone);

        EventBus.getDefault().register(this);

        mTimer.start();

        setListener();
    }

    private void getData() {
        Intent intent = getIntent();
        mPhone = intent.getStringExtra("phone");
        type = intent.getIntExtra("type", 0);
        if (type == 1) {
            openId = intent.getStringExtra("openId");
            openIdType = intent.getIntExtra("openIdType", 0);
            isRegister = intent.getIntExtra("isRegister", 1);
        }
    }

    private void setListener() {
        mBinding.editSmsCode.setInputCompleteListener(new VerificationCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                if (mBinding.editSmsCode.getInputContent().length() == 4) {
                    ViewUnits.getInstance().showLoading(RegisterSmsActivity.this, "验证中");
                    if (type == 1) {
                        mUserPrenInter.checkCode(NetCode.User.checkCode, mPhone, mBinding.editSmsCode.getInputContent(), isRegister, openIdType, openId);
                    } else {
                        mUserPrenInter.checkRegisterSMS(mPhone, mBinding.editSmsCode.getInputContent(), NetCode.User.registerCheckSmsCode);
                    }

                }
            }

            @Override
            public void deleteContent() {

            }
        });

        mBinding.textTime.setOnClickListener(view -> {
            ViewUnits.getInstance().showLoading(RegisterSmsActivity.this, "正在发送验证码");
            mTimer.start();
            if (type == 1) {
                mUserPrenInter.sendCode(NetCode.User.sendCode, mPhone);
            } else {
                mUserPrenInter.sendLoginSmsCode(NetCode.User.getRegisterSmsCode, mPhone);
            }

        });
    }

    public static void setLoginResultListener(String callBack, LoginResultListener listener) {
        mListener = listener;
        mCallBack = callBack;
    }


    @Override
    public void onSuccessed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        switch (what) {
            case NetCode.User.registerCheckSmsCode:
                Intent intent = new Intent(this, RegisterSetPwdActivity.class);
                User user = (User) object;
                intent.putExtra("phone", mPhone);
                intent.putExtra("smsCode", user.getCode());
                startActivity(intent);
                break;
            case NetCode.User.getRegisterSmsCode:
                break;
            case NetCode.User.checkCode:
                JSONObject jsonObject = (JSONObject) object;
                try {
                    JSONObject data = jsonObject.getJSONObject("Data");
                    if (isRegister == 1) {
                        String token = data.getString("Token");
                        String clientID = data.getString("ClientID");
                        UserUnits.getInstance().setToken(token);
                        ConfigUnits.getInstance().setPhoneAnalogIMEI(clientID);
                        CacheUnits.getInstance().clearMessage();  //清除消息

                        mCardPrenInter.infoSync(NetCode.Card.infoSync, CacheUnits.getInstance().getMyCacheCardIds());   //同步卡片
                    } else {
                        String code = data.getString("Code");

                        Intent intent1 = new Intent(this, RegisterSetPwdActivity.class);
                        intent1.putExtra("phone", mPhone);
                        intent1.putExtra("type", 1);
                        intent1.putExtra("openId", openId);
                        intent1.putExtra("openIdType", openIdType);
                        intent1.putExtra("code", code);
                        startActivity(intent1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case NetCode.Card.infoSync:
                mPersonalPrenInter.getPersonalInfo(NetCode.Personal.getPersonalInfo);  //获取用户信息
                break;
            case NetCode.Personal.getPersonalInfo:
                ViewUnits.getInstance().missLoading();
                EventBus.getDefault().post("login_success");
                if (LoginActivity.mFlag == Request.StartActivityRspCode.CARD_JUMP_TO_LOGIN) { //改标识表示从卡片页面跳转过来
                    mListener.getLoginResultListener(mCallBack, "1");
                    finish();
                } else {
                    Intent intent1 = new Intent(RegisterSmsActivity.this, MainActivity.class);  //跳转至首页
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);
                    finish();
                }
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
        mTimer = null;
        mUserPrenInter = null;
        EventBus.getDefault().unregister(this);
    }
}
