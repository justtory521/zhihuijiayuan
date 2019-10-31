package tendency.hz.zhihuijiayuan.view.set;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.tencent.bugly.beta.Beta;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.databinding.ActivitySetBinding;
import tendency.hz.zhihuijiayuan.handlers.SetActivityHandler;
import tendency.hz.zhihuijiayuan.presenter.UserPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.UserPrenInter;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.UserUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.TestBleActivity;
import tendency.hz.zhihuijiayuan.view.user.LoginActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/11/16.
 */
public class SetActivity extends BaseActivity implements AllViewInter {
    private static final String TAG = "SetActivity---";
    private ActivitySetBinding mBinding;
    private UserPrenInter mUserPrenInter;

    private PackageManager mPm;
    private ComponentName mDefault, mDouble1, mDouble2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_set);
        mBinding.setSetHandler(new SetActivityHandler());
        ViewUnits.getInstance().setTitleHeight(mBinding.layoutTitle);
        mUserPrenInter = new UserPrenImpl(this);

        hasNewVersion();

        mPm = getApplicationContext().getPackageManager();

        mDefault = new ComponentName(getBaseContext(), "tendency.hz.zhihuijiayuan.default");
        mDouble1 = new ComponentName(getBaseContext(), "tendency.hz.zhihuijiayuan.icon1");
        mDouble2 = new ComponentName(getBaseContext(), "tendency.hz.zhihuijiayuan.icon2");

        mBinding.btnIcon1.setOnClickListener(view -> {
            disableComponent(mDefault);
            disableComponent(mDouble2);
            enableComponent(mDouble1);
        });

        mBinding.btnIcon2.setOnClickListener(view -> {
            disableComponent(mDefault);
            disableComponent(mDouble1);
            enableComponent(mDouble2);
        });

        mBinding.btnTestBle.setOnClickListener(view -> startActivity(new Intent(SetActivity.this, TestBleActivity.class)));
    }

    /**
     * 是否有新版本
     */
    private void hasNewVersion() {
        mBinding.imgNew.setVisibility(Beta.getUpgradeInfo() !=null ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FormatUtils.getInstance().isEmpty(UserUnits.getInstance().getToken())) {
            mBinding.btnLogout.setVisibility(View.GONE);
        }
    }

    public void logout(View view) {
        ViewUnits.getInstance().showLoading(this, "退出中");
        mUserPrenInter.logout(NetCode.User.logout);
    }

    @Override
    public void onSuccessed(int what, Object object) {
        switch (what) {
            case NetCode.User.logout:
                ViewUnits.getInstance().missLoading();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case NetCode.Set.getVersion:
                Log.e(TAG, object.toString());
                Log.e(TAG, BaseUnits.getInstance().getVerName(this));

                boolean isUpdate = false;
                String[] newVersion = ((String) object).split("\\.");

                String[] currentVersion = BaseUnits.getInstance().getVerName(this).split("\\.");


                for (int i = 0; i < newVersion.length; i++) {
                    if (i < 2) {
                        if (Integer.parseInt(newVersion[i]) > Integer.parseInt(currentVersion[i])) {
                            isUpdate = true;
                            break;
                        }
                    } else {
                        int newVersionName;
                        int currentVersionName;

                        if (newVersion[i].contains("-")) {
                            String[] split = newVersion[i].split("-");
                            newVersionName = Integer.parseInt(split[0]);
                        } else {
                            newVersionName = Integer.parseInt(newVersion[i]);
                        }

                        if (currentVersion[i].contains("-")) {
                            String[] split = currentVersion[i].split("-");
                            currentVersionName = Integer.parseInt(split[0]);
                        } else {
                            currentVersionName = Integer.parseInt(currentVersion[i]);
                        }

                        if (newVersionName > currentVersionName) {
                            isUpdate = true;
                            break;
                        }
                    }

                }

                mBinding.imgNew.setVisibility(isUpdate ? View.VISIBLE : View.GONE);

                break;
        }
    }

    @Override
    public void onFailed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        ViewUnits.getInstance().showToast(object.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserPrenInter = null;
    }

    private void enableComponent(ComponentName componentName) {
        mPm.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void disableComponent(ComponentName componentName) {
        mPm.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
