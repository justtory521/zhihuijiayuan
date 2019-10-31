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

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.databinding.ActivityRegisterPhoneBinding;
import tendency.hz.zhihuijiayuan.presenter.UserPrenImpl;
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
public class RegisterActivity extends BaseActivity implements AllViewInter {
    private UserPrenInter mUserPrenInter;
    private ActivityRegisterPhoneBinding mBinding;
    private int type;
    private String openId;
    private int openIdType;
    //手机号是否注册过
    private int isRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register_phone);
        mUserPrenInter = new UserPrenImpl(this);
        EventBus.getDefault().register(this);
        getData();
        setListener();
    }

    private void getData() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        if (type == 1) {
            openId = intent.getStringExtra("openId");
            openIdType = intent.getIntExtra("openIdType", 0);
            mBinding.tvBindPhone.setText("绑定手机号码");
        }
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
                ViewUnits.getInstance().showToast("请输入正确手机号");
                return;
            }
            ViewUnits.getInstance().showLoading(this, "发送中");
            if (type == 1) {
                mUserPrenInter.isBindPhone(NetCode.User.isBindPhone, openId, openIdType, mBinding.edtPhone.getTextTrim(mBinding.edtPhone));
            } else {
                mUserPrenInter.getRegisterSMS(mBinding.edtPhone.getTextTrim(mBinding.edtPhone), NetCode.User.getRegisterSmsCode);
            }
        });
    }

    @Override
    public void onSuccessed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        switch (what) {
            case NetCode.User.getRegisterSmsCode:
                ViewUnits.getInstance().showToast("发送成功，请查收");
                Intent intent = new Intent(this, RegisterSmsActivity.class);
                intent.putExtra("phone", mBinding.edtPhone.getTextTrim(mBinding.edtPhone));
                startActivity(intent);
                break;
            case NetCode.User.isBindPhone:
                try {
                    JSONObject jsonObject = (JSONObject) object;
                    int status = jsonObject.getInt("Status");
                    String msg = jsonObject.getString("Msg");
                    if (status == 2) {
                        JSONObject data = jsonObject.getJSONObject("Data");
                        isRegister = data.getInt("IsRegister");
                        mUserPrenInter.sendCode(NetCode.User.sendCode, mBinding.edtPhone.getTextTrim(mBinding.edtPhone));
                    } else {
                        ViewUnits.getInstance().showToast(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case NetCode.User.sendCode:
                ViewUnits.getInstance().showToast("发送成功，请查收");
                Intent intent1 = new Intent(this, RegisterSmsActivity.class);
                intent1.putExtra("phone", mBinding.edtPhone.getTextTrim(mBinding.edtPhone));
                intent1.putExtra("type", 1);
                intent1.putExtra("openId", openId);
                intent1.putExtra("openIdType", openIdType);
                intent1.putExtra("isRegister", isRegister);
                startActivity(intent1);
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
        mUserPrenInter = null;
        EventBus.getDefault().unregister(this);
    }
}
