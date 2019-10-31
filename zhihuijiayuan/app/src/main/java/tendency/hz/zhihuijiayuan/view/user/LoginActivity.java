package tendency.hz.zhihuijiayuan.view.user;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;


import com.alipay.sdk.app.AuthTask;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tendency.hz.zhihuijiayuan.MainActivity;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.adapter.MainFragmentPagerAdapter;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.databinding.ActivityLoginBinding;
import tendency.hz.zhihuijiayuan.inter.LoginResultListener;
import tendency.hz.zhihuijiayuan.presenter.CardPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.UserPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.CardPrenInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PersonalPrenInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.UserPrenInter;
import tendency.hz.zhihuijiayuan.units.CacheUnits;
import tendency.hz.zhihuijiayuan.units.ConfigUnits;
import tendency.hz.zhihuijiayuan.units.LoadingDialog;
import tendency.hz.zhihuijiayuan.units.LogUtils;
import tendency.hz.zhihuijiayuan.units.UserUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/12/17.
 */
public class LoginActivity extends BaseActivity implements AllViewInter {
    private ActivityLoginBinding mBinding;

    //碎片list
    private List<Fragment> mListFragments = new ArrayList<>();

    //碎片适配器
    private MainFragmentPagerAdapter mAdapter = null;

    private LoginBySmsFragment mFragmentBySms;
    private LoginByPwdFragment mFragmentByPwd;

    public static int mFlag;
    private UMShareAPI umShareAPI;

    private UserPrenInter mUserPrenInter;
    private CardPrenInter mCardPrenInter;
    private PersonalPrenInter mPersonalPrenInter;
    public static LoginResultListener mListener;
    private static String mCallBack;
    //三方登录
    private int type;
    private String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        EventBus.getDefault().register(this);
        mUserPrenInter = new UserPrenImpl(this);
        mCardPrenInter = new CardPrenImpl(this);
        mPersonalPrenInter = new PersonalPrenImpl(this);
        mFragmentBySms = new LoginBySmsFragment();
        mFragmentByPwd = new LoginByPwdFragment();

        mListFragments.add(mFragmentByPwd);
        mListFragments.add(mFragmentBySms);
        mFlag = getIntent().getIntExtra("flag", 0);

        //初始化默认显示页面
        initDefault();

        //设置监听
        setListener();
    }

    private void initDefault() {
        mAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), mListFragments);
        mBinding.viewpager.setAdapter(mAdapter);
        mBinding.viewpager.setOffscreenPageLimit(5);
        if (super.getIntent().getIntExtra("flag", 0) == Request.StartActivityRspCode.JUMP_TO_LOGINBYPWD) {
            mBinding.viewpager.setCurrentItem(0);
            changeBtn(R.id.rb_login_by_pwd);
        }
    }

    private void setListener() {
        mBinding.rg.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.rb_login_by_pwd:
                    mBinding.viewpager.setCurrentItem(0);
                    mBinding.imgLogo.setImageResource(R.mipmap.ic_logo_by_pwd);
                    break;
                case R.id.rb_login_by_sms:
                    mBinding.viewpager.setCurrentItem(1);
                    mBinding.imgLogo.setImageResource(R.mipmap.ic_logo_by_sms);
                    break;
            }

            changeBtn(i);
        });


        mBinding.ivWechatLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                getPlatformInfo(SHARE_MEDIA.WEIXIN, "微信");

            }
        });


        mBinding.ivQqLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 2;
                getPlatformInfo(SHARE_MEDIA.QQ, "QQ");
            }
        });


        mBinding.ivSinaLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 3;
                getPlatformInfo(SHARE_MEDIA.SINA, "新浪微博");
            }
        });

        mBinding.tvUserAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,AgreementActivity.class));
            }
        });

        mBinding.tvPrivacyStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,PrivacyStatementActivity.class));
            }
        });


    }

    public static void setLoginResultListener(String callBack, LoginResultListener listener) {
        mListener = listener;
        mCallBack = callBack;
    }


    /**
     * 获取授权信息
     *
     * @param shareType
     * @param typeName
     */
    private void getPlatformInfo(SHARE_MEDIA shareType, String typeName) {

        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);

        umShareAPI = UMShareAPI.get(this);
        umShareAPI.setShareConfig(config);


        if (umShareAPI.isInstall(this, shareType)) {
            umShareAPI.getPlatformInfo(this, shareType, umAuthListener);
        } else {
            ViewUnits.getInstance().showToast("手机未安装" + typeName);
        }

    }


    UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

            if (map.containsKey("uid") && map.get("uid") != null) {
                uid = map.get("uid");
                ViewUnits.getInstance().showLoading(LoginActivity.this, "请求中");
                mUserPrenInter.isBindPlatform(NetCode.User.isBindPlatform, uid, type);
            } else {
                ViewUnits.getInstance().showToast("授权失败");
            }

        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };

    @Override
    public void onSuccessed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        switch (what) {
            case NetCode.User.isBindPlatform:
                try {
                    JSONObject jsonObject = (JSONObject) object;
                    int status = jsonObject.getInt("Status");
                    if (status == 2) {
                        JSONObject data = jsonObject.getJSONObject("Data");
                        String token = data.getString("Token");
                        String clientID = data.getString("ClientID");
                        UserUnits.getInstance().setToken(token);
                        ConfigUnits.getInstance().setPhoneAnalogIMEI(clientID);
                        CacheUnits.getInstance().clearMessage();  //清除消息

                        mCardPrenInter.infoSync(NetCode.Card.infoSync, CacheUnits.getInstance().getMyCacheCardIds());   //同步卡片
                    } else {
                        Intent bindPhone = new Intent(LoginActivity.this, RegisterActivity.class);
                        bindPhone.putExtra("type", 1);
                        bindPhone.putExtra("openId", uid);
                        bindPhone.putExtra("openIdType", type);
                        startActivity(bindPhone);
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
                if (LoginActivity.mFlag == Request.StartActivityRspCode.CARD_JUMP_TO_LOGIN) { //该标识表示从卡片页面跳转过来
                    mListener.getLoginResultListener(mCallBack, "1");
                    finish();
                } else {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);  //跳转至首页
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
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

    /**
     * 选择后改变按钮文本颜色
     *
     * @param checkedId
     */
    public void changeBtn(int checkedId) {
        //重置所有字体颜色
        mBinding.rbLoginBySms.setTextColor(getResources().getColor(R.color.text_gray9));
        mBinding.rbLoginByPwd.setTextColor(getResources().getColor(R.color.text_gray9));
        mBinding.rbLoginBySms.setTextSize(15);
        mBinding.rbLoginByPwd.setTextSize(15);
        switch (checkedId) {
            case R.id.rb_login_by_sms:
                mBinding.rbLoginBySms.setTextColor(getResources().getColor(R.color.colorAccent));
                mBinding.rbLoginBySms.setTextSize(16);
                break;
            case R.id.rb_login_by_pwd:
                mBinding.rbLoginByPwd.setTextColor(getResources().getColor(R.color.colorAccent));
                mBinding.rbLoginByPwd.setTextSize(16);
                break;
        }
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
        mFlag = 0;
        mListFragments = null;
        mAdapter = null;
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (umShareAPI != null) {
            umShareAPI.onActivityResult(requestCode, resultCode, data);
        }

    }
}
