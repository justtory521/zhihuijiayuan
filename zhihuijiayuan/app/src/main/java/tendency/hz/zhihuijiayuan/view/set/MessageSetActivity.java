package tendency.hz.zhihuijiayuan.view.set;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.databinding.ActivityMessageSetBinding;
import tendency.hz.zhihuijiayuan.presenter.SetPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.SetPrenInter;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/11/16.
 */
public class MessageSetActivity extends BaseActivity implements AllViewInter {
    private ActivityMessageSetBinding mBinding;
    private SetPrenInter mSetPrenInter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_message_set);
        ViewUnits.getInstance().setTitleHeight(mBinding.layoutTitle);
        mSetPrenInter = new SetPrenImpl(this);

        initData();

        setListener();
    }

    private void initData() {
        ViewUnits.getInstance().showLoading(this, "加载中");
        mSetPrenInter.getMessagePrevent(NetCode.Set.getMessagePrevent);
    }

    private void setListener() {
        mBinding.switchMessagePrevent.setOnCheckedChangeListener((compoundButton, b) -> {
            ViewUnits.getInstance().showLoading(this, "修改中");
            if (b) {
                mSetPrenInter.editMessagePrevent(NetCode.Set.editMessagePrevent, "1");
            } else {
                mSetPrenInter.editMessagePrevent(NetCode.Set.editMessagePrevent, "0");
            }
        });
    }

    @Override
    public void onSuccessed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        switch (what) {
            case NetCode.Set.getMessagePrevent:
                switch (object.toString()) {
                    case "1":
                        mBinding.switchMessagePrevent.setChecked(true);
                        break;
                    case "0":
                        mBinding.switchMessagePrevent.setChecked(false);
                        break;
                }
                break;
            case NetCode.Set.editMessagePrevent:
                break;
        }
    }

    @Override
    public void onFailed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSetPrenInter = null;
    }
}
