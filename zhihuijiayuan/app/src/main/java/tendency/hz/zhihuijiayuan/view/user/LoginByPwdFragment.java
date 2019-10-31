package tendency.hz.zhihuijiayuan.view.user;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import tendency.hz.zhihuijiayuan.MainActivity;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.databinding.FragmentLoginByPwdBinding;
import tendency.hz.zhihuijiayuan.inter.LoginResultListener;
import tendency.hz.zhihuijiayuan.presenter.CardPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.UserPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.CardPrenInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PersonalPrenInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.UserPrenInter;
import tendency.hz.zhihuijiayuan.units.CacheUnits;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/12/17.
 */
public class LoginByPwdFragment extends Fragment implements AllViewInter, View.OnClickListener {
    private String mUserName, mPassWord;

    private UserPrenInter mUserPrenInter;
    private CardPrenInter mCardPrenInter;
    private PersonalPrenInter mPersonalPrenInter;

    private FragmentLoginByPwdBinding mBinding;

    private static LoginResultListener mListener;
    private static String mCallBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_by_pwd, container, false);

        mUserPrenInter = new UserPrenImpl(this);
        mPersonalPrenInter = new PersonalPrenImpl(this);
        mCardPrenInter = new CardPrenImpl(this);

        setListener();

        return mBinding.getRoot();
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
                checkFormat();
            }
        });

        mBinding.edtPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkFormat();
            }
        });

        mBinding.checkboxEyes.setOnCheckedChangeListener((compoundButton, b) -> {
            mBinding.edtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            if (b) {
                mBinding.edtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            mBinding.edtPwd.setSelection(mBinding.edtPwd.getText().toString().length());
        });

        mBinding.btnLogin.setOnClickListener(this);
        mBinding.btnForgetPwd.setOnClickListener(this);
        mBinding.btnGoRegister.setOnClickListener(this);
    }

    private void checkFormat() {
        mBinding.btnLogin.setBackgroundColor(getActivity().getResources().getColor(R.color.bg_btn_unenable));
        if ((mBinding.edtPhone.getTextTrim(mBinding.edtPhone).length() >= 11) &&
                !FormatUtils.getInstance().isEmpty(mBinding.edtPwd.getText().toString())) {
            mBinding.btnLogin.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        }
    }

    public static void setLoginResultListener(String callBack, LoginResultListener listener) {
        mListener = listener;
        mCallBack = callBack;
    }

    @Override
    public void onSuccessed(int what, Object object) {
        switch (what) {
            case NetCode.User.login:
                mCardPrenInter.infoSync(NetCode.Card.infoSync, CacheUnits.getInstance().getMyCacheCardIds());   //同步卡片
                break;
            case NetCode.Card.infoSync:
                mPersonalPrenInter.getPersonalInfo(NetCode.Personal.getPersonalInfo);  //获取用户信息
                break;
            case NetCode.Personal.getPersonalInfo:
                ViewUnits.getInstance().missLoading();
                if (LoginActivity.mFlag == Request.StartActivityRspCode.CARD_JUMP_TO_LOGIN) { //改标识表示从卡片页面跳转过来
                    mListener.getLoginResultListener(mCallBack, "1");
                    EventBus.getDefault().post("login_success");
                    getActivity().finish();
                } else {
                    Intent intent = new Intent(getActivity(), MainActivity.class);  //跳转至首页
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();
                }
                break;
        }
    }

    @Override
    public void onFailed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        ViewUnits.getInstance().showToast(object.toString());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_forget_pwd:
                Intent intentFindPwd = new Intent(getActivity(), FindPwdStep1Activity.class);
                startActivityForResult(intentFindPwd,Request.StartActivityRspCode.CARD_JUMP_TO_LOGIN);
                break;
            case R.id.btn_go_register:
                Intent intentGoRegister = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intentGoRegister);
                break;
            case R.id.btn_login:
                if (mBinding.edtPhone.getTextTrim(mBinding.edtPhone).length() < 11) {
                    ViewUnits.getInstance().showToast("请输入正确手机号");
                    return;
                }

                if (FormatUtils.getInstance().isEmpty(mBinding.edtPwd.getText().toString())) {
                    ViewUnits.getInstance().showToast("请输入密码");
                    return;
                }

                mUserName = mBinding.edtPhone.getTextTrim(mBinding.edtPhone);
                mPassWord = mBinding.edtPwd.getText().toString();
                ViewUnits.getInstance().showLoading(getActivity(), "登录中");
                mUserPrenInter.login(mUserName, mPassWord, NetCode.User.login);
                break;
        }
    }
}
