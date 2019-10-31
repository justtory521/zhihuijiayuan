package tendency.hz.zhihuijiayuan.view.user;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.databinding.FragmentLoginBySmsBinding;
import tendency.hz.zhihuijiayuan.presenter.UserPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.UserPrenInter;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/12/17.
 */
public class LoginBySmsFragment extends Fragment implements AllViewInter {
    private static final String TAG = "LoginBySmsFragment---";
    private FragmentLoginBySmsBinding mBinding;
    private UserPrenInter mUserPrenInter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_by_sms, container, false);

        mUserPrenInter = new UserPrenImpl(this);

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
                Log.e(TAG, editable.toString());
                mBinding.btnNext.setBackgroundColor(getActivity().getResources().getColor(R.color.bg_btn_unenable));
                if (editable.toString().length() >= 13) {
                    mBinding.btnNext.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                }
            }
        });

        mBinding.btnNext.setOnClickListener(view -> {
            if (mBinding.edtPhone.getTextTrim(mBinding.edtPhone).toString().length() < 11) {
                ViewUnits.getInstance().showToast("请输入完整手机号");
                return;
            }
            ViewUnits.getInstance().showLoading(getActivity(), "发送中");
            mUserPrenInter.sendLoginSmsCode(NetCode.User2.sendLoginSms, mBinding.edtPhone.getTextTrim(mBinding.edtPhone));
        });
    }

    @Override
    public void onSuccessed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        if (what == NetCode.User2.sendLoginSms) {
            ViewUnits.getInstance().showToast("发送成功，请查收");
            Intent intent = new Intent(getActivity(), LoginSmsActivity.class);
            intent.putExtra("phone", mBinding.edtPhone.getTextTrim(mBinding.edtPhone));
            startActivity(intent);
        }
    }

    @Override
    public void onFailed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        ViewUnits.getInstance().showToast(object.toString());
    }
}




