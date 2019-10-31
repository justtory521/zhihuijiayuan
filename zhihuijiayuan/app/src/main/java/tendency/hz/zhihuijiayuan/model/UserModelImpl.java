package tendency.hz.zhihuijiayuan.model;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tendency.hz.zhihuijiayuan.bean.User;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Uri;
import tendency.hz.zhihuijiayuan.model.modelInter.AllModelInter;
import tendency.hz.zhihuijiayuan.model.modelInter.UserModelInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.AllPrenInter;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.CacheUnits;
import tendency.hz.zhihuijiayuan.units.ConfigUnits;
import tendency.hz.zhihuijiayuan.units.NoHttpUtil;
import tendency.hz.zhihuijiayuan.units.UserUnits;

/**
 * Created by JasonYao on 2018/3/21.
 */

public class UserModelImpl extends AllModelInter implements UserModelInter {
    private static final String TAG = "UserModelImpl---";
    private AllPrenInter mAllPrenInter;
    private Gson mGson = new Gson();

    public UserModelImpl(AllPrenInter mAllPrenInter) {
        this.mAllPrenInter = mAllPrenInter;
    }

    @Override
    public void login(String account, String password, int netCode) {
        if (netCode != NetCode.User.login) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("Account", account));
        params.add(new NoHttpUtil.Param("password", password));
        params.add(new NoHttpUtil.Param("AppType", "1"));

        Log.e(TAG, "登录信息" + params.toString());
        NoHttpUtil.post(netCode, Uri.User.LOGIN, onResponseListener, params);
    }

    @Override
    public void logout(int netCode) {
        if (netCode != NetCode.User.logout) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
//        params.add(new NoHttpUtil.Param("token", What.TOKEN_HEADER + UserUnits.getInstance().getToken()));

        Log.e(TAG, params.toString());

        NoHttpUtil.post(netCode, Uri.User.LOGOUT, onResponseListener, params);
    }

    @Override
    public void getRegisterSMS(String phone, int netCode) {
        if (netCode != NetCode.User.getRegisterSmsCode) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("phone", phone));

        NoHttpUtil.get(netCode, Uri.User.REGISTERSENDSMSCODE, onResponseListener, params);
    }

    @Override
    public void register(String account, String password, String code, int netCode) {
        if (netCode != NetCode.User.register) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("account", account));
        params.add(new NoHttpUtil.Param("password", password));
        params.add(new NoHttpUtil.Param("PasswordConfirm", password));
        params.add(new NoHttpUtil.Param("code", code));
        params.add(new NoHttpUtil.Param("ClientID", BaseUnits.getInstance().getPhoneKey()));

        Log.e(TAG, params.toString());
        NoHttpUtil.post(netCode, Uri.User.REGISTER, onResponseListener, params);
    }

    @Override
    public void checkRegisterSMS(String phone, String code, int netCode) {
        if (netCode != NetCode.User.registerCheckSmsCode) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("phone", phone));
        params.add(new NoHttpUtil.Param("code", code));

        Log.e(TAG, "phone=" + phone);
        NoHttpUtil.post(netCode, Uri.User.REGISTERCHECKSMSCODE, onResponseListener, params);
    }

    @Override
    public void checkToken(int netCode) {
        if (netCode != NetCode.User.checkToken) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("token", UserUnits.getInstance().getToken()));

        NoHttpUtil.get(netCode, Uri.User.CHECKTOKEN, onResponseListener, params);
    }

    @Override
    public void changePwdSMS(String phone, int netCode) {
        if (netCode != NetCode.User.changePwdSendSms) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("Phone", phone));

        NoHttpUtil.get(netCode, Uri.User.PWDCHANGESENDSMS, onResponseListener, params);
    }

    @Override
    public void checkChangePwdSMS(String phone, String code, int netCode) {
        if (netCode != NetCode.User.changePwdCheckSms) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("Phone", phone));
        params.add(new NoHttpUtil.Param("Code", code));

        NoHttpUtil.post(netCode, Uri.User.PWDCHANGECHECKSMS, onResponseListener, params);
    }

    @Override
    public void changePwd(String account, String password, String code, int netCode) {
        if (netCode != NetCode.User.changePwdEdit) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("Account", account));
        params.add(new NoHttpUtil.Param("Password", password));
        params.add(new NoHttpUtil.Param("PasswordConfirm", password));
        params.add(new NoHttpUtil.Param("Code", code));

        Log.e(TAG, "注册内容：" + params.toString());
        NoHttpUtil.post(netCode, Uri.User.PWDCHANGEEDIT, onResponseListener, params);
    }

    @Override
    public void changePassWord(String password, String passwordConfirm, int netCode) {
        if (netCode != NetCode.User.changePwd) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("Password", password));
        params.add(new NoHttpUtil.Param("PasswordConfirm", passwordConfirm));

        NoHttpUtil.post(netCode, Uri.User.CHANGEPWD, onResponseListener, params);
    }

    @Override
    public void sendLoginSmsCode(int netCode, String phone) {
        if (netCode != NetCode.User2.sendLoginSms) {
            return;
        }

        List<NoHttpUtil.Param> list = new ArrayList<>();
        list.add(new NoHttpUtil.Param("phone", phone));

        NoHttpUtil.get(netCode, Uri.User.SENDLOGINSMSCODE, onResponseListener, list);
    }

    @Override
    public void loginBySms(int netCode, String account, String code) {
        if (netCode != NetCode.User2.loginBySms) {
            return;
        }

        List<NoHttpUtil.Param> list = new ArrayList<>();
        list.add(new NoHttpUtil.Param("Account", account));
        list.add(new NoHttpUtil.Param("Code", code));
        list.add(new NoHttpUtil.Param("AppType", "1"));
        list.add(new NoHttpUtil.Param("ClientID", BaseUnits.getInstance().getPhoneKey()));

        NoHttpUtil.post(netCode, Uri.User.LOGINBYSMS, onResponseListener, list);
    }

    @Override
    public void isBindPlatform(int netCode, String OpenId, int OpenIdType) {
        if (netCode != NetCode.User.isBindPlatform) {
            return;
        }

        List<NoHttpUtil.Param> list = new ArrayList<>();
        list.add(new NoHttpUtil.Param("OpenId", OpenId));
        list.add(new NoHttpUtil.Param("OpenIdType", OpenIdType));
        list.add(new NoHttpUtil.Param("AppType", "1"));

        NoHttpUtil.post(netCode, Uri.User.ISBINDPLATFORM, onResponseListener, list);
    }

    @Override
    public void isBindPhone(int netCode, String OpenId, int OpenIdType, String Phone) {
        if (netCode != NetCode.User.isBindPhone) {
            return;
        }


        List<NoHttpUtil.Param> list = new ArrayList<>();
        list.add(new NoHttpUtil.Param("OpenId", OpenId));
        list.add(new NoHttpUtil.Param("OpenIdType", OpenIdType));
        list.add(new NoHttpUtil.Param("Phone", Phone));

        NoHttpUtil.post(netCode, Uri.User.ISBINDPHONE, onResponseListener, list);
    }

    @Override
    public void sendCode(int netCode, String phone) {
        if (netCode != NetCode.User.sendCode) {
            return;
        }

        List<NoHttpUtil.Param> list = new ArrayList<>();
        list.add(new NoHttpUtil.Param("phone", phone));

        NoHttpUtil.get(netCode, Uri.User.SENDCODE, onResponseListener, list);
    }

    @Override
    public void checkCode(int netCode, String Phone, String Code, int IsRegister, int OpenIdType, String OpenId) {
        if (netCode != NetCode.User.checkCode) {
            return;
        }


        List<NoHttpUtil.Param> list = new ArrayList<>();
        list.add(new NoHttpUtil.Param("Phone", Phone));
        list.add(new NoHttpUtil.Param("Code", Code));
        list.add(new NoHttpUtil.Param("IsRegister", IsRegister));
        list.add(new NoHttpUtil.Param("OpenIdType", OpenIdType));
        list.add(new NoHttpUtil.Param("OpenId", OpenId));
        list.add(new NoHttpUtil.Param("AppType", "1"));

        NoHttpUtil.post(netCode, Uri.User.CHECKCODE, onResponseListener, list);
    }

    @Override
    public void setPwd(int netCode, String Phone, String Code, int OpenIdType, String OpenId, String pwd) {
        if (netCode != NetCode.User.setPwd) {
            return;
        }

        List<NoHttpUtil.Param> list = new ArrayList<>();
        list.add(new NoHttpUtil.Param("Phone", Phone));
        list.add(new NoHttpUtil.Param("Code", Code));
        list.add(new NoHttpUtil.Param("pwd", pwd));
        list.add(new NoHttpUtil.Param("OpenIdType", OpenIdType));
        list.add(new NoHttpUtil.Param("OpenId", OpenId));
        list.add(new NoHttpUtil.Param("AppType", "1"));
        list.add(new NoHttpUtil.Param("Type", 3));
        list.add(new NoHttpUtil.Param("ClientID", BaseUnits.getInstance().getPhoneKey()));

        NoHttpUtil.post(netCode, Uri.User.SETPWD, onResponseListener, list);

    }

    @Override
    public void rspSuccess(int what, Object object) throws JSONException {
        switch (what) {
            case NetCode.User.getRegisterSmsCode:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.User.registerCheckSmsCode:
                JSONObject jsoCheckSms = ((JSONObject) object).getJSONObject("Data");
                User user = mGson.fromJson(jsoCheckSms.toString(), User.class);
                mAllPrenInter.onSuccess(what, user);
                break;
            case NetCode.User.register:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.User.login:
                JSONObject jsoLogin = ((JSONObject) object).getJSONObject("Data");
                Log.e(TAG, "登录成功：数据为：" + ((JSONObject) object).getJSONObject("Data").toString());
                String token = jsoLogin.getString("Token");
                Log.e(TAG, "登录成功，token为：" + token);
                UserUnits.getInstance().setToken(token);
                ConfigUnits.getInstance().setPhoneAnalogIMEI(jsoLogin.getString("ClientID"));
                CacheUnits.getInstance().clearMessage();  //清除消息
                mAllPrenInter.onSuccess(what, token);
                break;
            case NetCode.User.logout:
                UserUnits.getInstance().clearUserInfo(); //用户登出，清空缓存
                ConfigUnits.getInstance().clearPhoneAnalogIMEI();
                CacheUnits.getInstance().deleteMyCacheCard();
                CacheUnits.getInstance().clearMessage();  //清除消息
                Log.e(TAG, "缓存的卡片数据：" + CacheUnits.getInstance().getMyCacheCards(null));
                Log.e(TAG, "缓存的卡片ID数据：" + CacheUnits.getInstance().getMyCacheCardIds());
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.User.checkToken:
                JSONObject jsoCheckToken = ((JSONObject) object).getJSONObject("Data");
                String result = jsoCheckToken.getString("IsValid");
                if (!result.equals("true")) {
                    UserUnits.getInstance().clearUserInfo(); //用户登出，清空缓存
                    ConfigUnits.getInstance().clearPhoneAnalogIMEI();
                    CacheUnits.getInstance().deleteMyCacheCard();
                }
                mAllPrenInter.onSuccess(what, result);
                break;
            case NetCode.User.changePwdSendSms:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.User.changePwdCheckSms:
                JSONObject jsoChangePwdCheckSms = ((JSONObject) object).getJSONObject("Data");
                User userChangePwd = mGson.fromJson(jsoChangePwdCheckSms.toString(), User.class);
                mAllPrenInter.onSuccess(what, userChangePwd);
                break;
            case NetCode.User.changePwdEdit:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.User.changePwd:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.User2.sendLoginSms:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.User2.loginBySms:
                JSONObject jsoLoginBySms = ((JSONObject) object).getJSONObject("Data");
                String tokenBySms = jsoLoginBySms.getString("Token");
                UserUnits.getInstance().setToken(tokenBySms);
                ConfigUnits.getInstance().setPhoneAnalogIMEI(jsoLoginBySms.getString("ClientID"));
                CacheUnits.getInstance().clearMessage();  //清除消息
                mAllPrenInter.onSuccess(what, tokenBySms);
                break;
            case NetCode.User.isBindPlatform:
                mAllPrenInter.onSuccess(what, object);
                break;
            case NetCode.User.isBindPhone:
                mAllPrenInter.onSuccess(what, object);
                break;
            case NetCode.User.sendCode:
                mAllPrenInter.onSuccess(what, object);
                break;
            case NetCode.User.checkCode:
                mAllPrenInter.onSuccess(what, object);
                break;
            case NetCode.User.setPwd:
                mAllPrenInter.onSuccess(what, object);
                break;
        }
    }

    @Override
    public void rspFailed(int what, Object object) {
        mAllPrenInter.onFail(what, object);
    }
}
