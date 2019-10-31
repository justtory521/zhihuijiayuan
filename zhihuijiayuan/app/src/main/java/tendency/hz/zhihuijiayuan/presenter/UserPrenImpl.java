package tendency.hz.zhihuijiayuan.presenter;

import tendency.hz.zhihuijiayuan.model.UserModelImpl;
import tendency.hz.zhihuijiayuan.model.modelInter.UserModelInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.AllPrenInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.UserPrenInter;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/3/21.
 */

public class UserPrenImpl implements AllPrenInter, UserPrenInter {
    private AllViewInter mAllViewInter;
    private UserModelInter mUserModelInter;

    public UserPrenImpl(AllViewInter mAllViewInter) {
        this.mAllViewInter = mAllViewInter;
        mUserModelInter = new UserModelImpl(this);
    }

    @Override
    public void login(String account, String password, int Netcode) {
        mUserModelInter.login(account, password, Netcode);
    }

    @Override
    public void logout(int netCode) {
        mUserModelInter.logout(netCode);
    }

    @Override
    public void getRegisterSMS(String phone, int netCode) {
        mUserModelInter.getRegisterSMS(phone, netCode);
    }

    @Override
    public void register(String account, String password, String code, int netCode) {
        mUserModelInter.register(account, password, code, netCode);
    }

    @Override
    public void checkRegisterSMS(String phone, String code, int netCode) {
        mUserModelInter.checkRegisterSMS(phone, code, netCode);
    }

    @Override
    public void checkToken(int netCode) {
        mUserModelInter.checkToken(netCode);
    }

    @Override
    public void changePwdSMS(String phone, int netCode) {
        mUserModelInter.changePwdSMS(phone, netCode);
    }

    @Override
    public void checkChangePwdSMS(String phone, String code, int netCode) {
        mUserModelInter.checkChangePwdSMS(phone, code, netCode);
    }

    @Override
    public void changePwd(String account, String password, String code, int netCode) {
        mUserModelInter.changePwd(account, password, code, netCode);
    }

    @Override
    public void changePassWord(String password, String passwordConfirm, int netCode) {
        mUserModelInter.changePassWord(password, passwordConfirm, netCode);
    }

    @Override
    public void sendLoginSmsCode(int netCode, String phone) {
        mUserModelInter.sendLoginSmsCode(netCode, phone);
    }

    @Override
    public void loginBySms(int netCode, String account, String code) {
        mUserModelInter.loginBySms(netCode, account, code);
    }

    @Override
    public void isBindPlatform(int netCode, String OpenId, int OpenIdType) {
        mUserModelInter.isBindPlatform(netCode,OpenId, OpenIdType);
    }

    @Override
    public void isBindPhone(int netCode, String OpenId, int OpenIdType, String Phone) {
        mUserModelInter.isBindPhone(netCode,OpenId, OpenIdType, Phone);
    }

    @Override
    public void sendCode(int netCode, String phone) {
        mUserModelInter.sendCode(netCode,phone);
    }

    @Override
    public void checkCode(int netCode, String Phone, String Code, int IsRegister, int OpenIdType, String OpenId) {
        mUserModelInter.checkCode(netCode,Phone, Code, IsRegister, OpenIdType, OpenId);
    }

    @Override
    public void setPwd(int netCode, String Phone, String Code, int OpenIdType, String OpenId, String pwd) {
        mUserModelInter.setPwd(netCode,Phone, Code, OpenIdType, OpenId, pwd);
    }

    @Override
    public void onSuccess(int what, Object object) {
        mAllViewInter.onSuccessed(what, object);
    }

    @Override
    public void onFail(int what, Object object) {
        mAllViewInter.onFailed(what, object);
    }
}
