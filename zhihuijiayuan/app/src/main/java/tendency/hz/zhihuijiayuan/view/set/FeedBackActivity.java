package tendency.hz.zhihuijiayuan.view.set;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.databinding.ActivityFeedbackBinding;
import tendency.hz.zhihuijiayuan.presenter.SetPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.SetPrenInter;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.UserUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/4/13.
 */

public class FeedBackActivity extends BaseActivity implements AllViewInter {
    private String mContentFeedBack;

    private SetPrenInter mSetPrenInter;

    private ActivityFeedbackBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);
        ViewUnits.getInstance().setTitleHeight(mBinding.layoutTitle);
        mSetPrenInter = new SetPrenImpl(this);

        mBinding.edtContact.setText(UserUnits.getInstance().getPhone());
        setListener();
    }

    private void setListener() {
        findViewById(R.id.img_btn_back).setOnClickListener(view -> finish());
        mBinding.btnFeedback.setOnClickListener(view -> {
            mContentFeedBack = mBinding.edtFeedback.getText().toString();
            if (FormatUtils.getInstance().isEmpty(mContentFeedBack)) {
                ViewUnits.getInstance().showToast("内容不能为空!");
                return;
            }

            if (!FormatUtils.getInstance().isEmpty(mBinding.edtContact.getText().toString()) &&
                    !FormatUtils.getInstance().isPhone(mBinding.edtContact.getText().toString())) {
                ViewUnits.getInstance().showToast("手机号格式错误");
                return;
            }

            ViewUnits.getInstance().showLoading(this, "反馈中");
            mSetPrenInter.feedBack(NetCode.Set.feedBack, "", mContentFeedBack, "", mBinding.edtContact.getText().toString());
        });

        mBinding.edtFeedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 200) {
                    mBinding.edtFeedback.setText(editable.toString().substring(0, 200));
                    mBinding.textNum.setText("200/200");
                } else {
                    mBinding.textNum.setText(editable.toString().length() + "/200");
                }
                mBinding.edtFeedback.setSelection(mBinding.edtFeedback.getText().toString().length());
            }
        });

        mBinding.layoutTitle.setPadding(0, ViewUnits.getInstance().dp2px(this, 25), 0, 0);
    }

    @Override
    public void onSuccessed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        ViewUnits.getInstance().showToast("谢谢您的反馈");
        finish();
    }

    @Override
    public void onFailed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        ViewUnits.getInstance().showToast(object.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContentFeedBack = null;
        mSetPrenInter = null;
    }
}
