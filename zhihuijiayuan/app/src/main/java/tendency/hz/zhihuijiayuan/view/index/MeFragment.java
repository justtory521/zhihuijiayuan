package tendency.hz.zhihuijiayuan.view.index;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.User;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.databinding.FragmentMeBinding;
import tendency.hz.zhihuijiayuan.handlers.MeFragmentHandler;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PersonalPrenInter;
import tendency.hz.zhihuijiayuan.units.UserUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/11/14.
 */
public class MeFragment extends Fragment implements AllViewInter {
    private static final String TAG = "MeFragment---";
    private FragmentMeBinding mBinding;

    private PersonalPrenInter mPersonalPrenInter;
    private User mUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_me, container, false);
        mBinding.setMeHandlers(new MeFragmentHandler());
        mPersonalPrenInter = new PersonalPrenImpl(this);
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        isLogin();
    }

    private void isLogin() {
        if (!TextUtils.isEmpty(UserUnits.getInstance().getToken())) {
            mPersonalPrenInter.getPersonalInfo(NetCode.Personal.getPersonalInfo);
        } else {
            mBinding.icPortrait.setImageURI("");
            mBinding.textBtnLogin.setVisibility(View.VISIBLE);
            mBinding.textPhone.setVisibility(View.GONE);
            mBinding.tvUserIntegrals.setText("0");
            mBinding.tvUserCredit.setText("0");
        }

    }

    private void initView() {
        mBinding.icPortrait.setImageURI(mUser.getHeadImgPath());
        mBinding.textBtnLogin.setVisibility(View.GONE);
        mBinding.textPhone.setVisibility(View.VISIBLE);
        mBinding.textPhone.setText(mUser.getPhone());
        mBinding.tvUserIntegrals.setText(String.valueOf(mUser.getIntegralCoin()));
        mBinding.tvUserCredit.setText(String.valueOf(mUser.getCreditScore()));

    }

    @Override
    public void onSuccessed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        mUser = (User) object;
        initView();
    }

    @Override
    public void onFailed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
    }
}
