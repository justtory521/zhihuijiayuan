package tendency.hz.zhihuijiayuan.view.user;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;

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
import tendency.hz.zhihuijiayuan.databinding.ActivityRegisterSetpwdBinding;
import tendency.hz.zhihuijiayuan.inter.LoginResultListener;
import tendency.hz.zhihuijiayuan.presenter.CardPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.UserPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.CardPrenInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PersonalPrenInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.UserPrenInter;
import tendency.hz.zhihuijiayuan.units.CacheUnits;
import tendency.hz.zhihuijiayuan.units.ConfigUnits;
import tendency.hz.zhihuijiayuan.units.UserUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/12/18.
 */
public class RegisterSetPwdActivity extends BaseActivity implements AllViewInter {
    private UserPrenInter mUserPrenInter;
    private CardPrenInter mCardPrenInter;
    private PersonalPrenInter mPersonalPrenInter;
    private ActivityRegisterSetpwdBinding mBinding;
    private String mPhone, mSmsCode;
    private int type;
    private String openId;
    private int openIdType;
    private String code;

    private static LoginResultListener mListener;
    private static String mCallBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register_setpwd);
        mUserPrenInter = new UserPrenImpl(this);
        mCardPrenInter = new CardPrenImpl(this);
        mPersonalPrenInter = new PersonalPrenImpl(this);
        getData();

        EventBus.getDefault().register(this);
        setListener();
    }

    private void getData() {
        Intent intent = getIntent();
        mPhone = intent.getStringExtra("phone");
        type = intent.getIntExtra("type", 0);
        if (type == 1) {
            openId = intent.getStringExtra("openId");
            openIdType = intent.getIntExtra("openIdType", 0);
            code = intent.getStringExtra("code");
        } else {
            mSmsCode = super.getIntent().getStringExtra("smsCode");
        }

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
            if (type == 1) {
                ViewUnits.getInstance().showLoading(this, "请求中");
                mUserPrenInter.setPwd(NetCode.User.setPwd, mPhone, code, openIdType, openId, mBinding.edtPwd.getText().toString().trim());
            } else {
                ViewUnits.getInstance().showLoading(this, "注册中");
                mUserPrenInter.register(mPhone, mBinding.edtPwd.getText().toString(), mSmsCode, NetCode.User.register);
            }

        });
    }

    public static void setLoginResultListener(String callBack, LoginResultListener listener) {
        mListener = listener;
        mCallBack = callBack;
    }


    @Override
    public void onSuccessed(int what, Object object) {
        switch (what) {
            case NetCode.User.register:
                mUserPrenInter.login(mPhone, mBinding.edtPwd.getText().toString(), NetCode.User.login);
                break;
            case NetCode.User.login:
                User user = new User();
                user.setPhone(mPhone);
                user.setToken(object.toString());
                UserUnits.getInstance().setAllUserInfo(user);  //登录成功，缓存用户数据
                mCardPrenInter.infoSync(NetCode.Card.infoSync, CacheUnits.getInstance().getMyCacheCardIds());
                break;
            case NetCode.Card.infoSync:
                ViewUnits.getInstance().missLoading();
                mPersonalPrenInter.getPersonalInfo(NetCode.Personal.getPersonalInfo);  //获取用户信息
                break;
            case NetCode.User.setPwd:
                JSONObject jsonObject = (JSONObject) object;
                try {
                    JSONObject data = jsonObject.getJSONObject("Data");

                    String token = data.getString("Token");
                    String clientID = data.getString("ClientID");
                    UserUnits.getInstance().setToken(token);
                    ConfigUnits.getInstance().setPhoneAnalogIMEI(clientID);
                    CacheUnits.getInstance().clearMessage();  //清除消息
                    mCardPrenInter.infoSync(NetCode.Card.infoSync, CacheUnits.getInstance().getMyCacheCardIds());   //同步卡片
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case NetCode.Personal.getPersonalInfo:
                ViewUnits.getInstance().missLoading();
                EventBus.getDefault().post("login_success");
                if (LoginActivity.mFlag == Request.StartActivityRspCode.CARD_JUMP_TO_LOGIN) { //改标识表示从卡片页面跳转过来
                    if (mListener !=null){
                        mListener.getLoginResultListener(mCallBack, "1");
                    }
                    finish();
                } else {
                    Intent intent1 = new Intent(RegisterSetPwdActivity.this, MainActivity.class);  //跳转至首页
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
        mCardPrenInter = null;
        mPhone = null;
        mSmsCode = null;
        mUserPrenInter = null;
        EventBus.getDefault().unregister(this);
    }
}
