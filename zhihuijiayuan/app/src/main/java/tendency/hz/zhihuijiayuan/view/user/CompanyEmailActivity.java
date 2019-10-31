package tendency.hz.zhihuijiayuan.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.base.Field;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PersonalPrenInter;
import tendency.hz.zhihuijiayuan.units.DateUtils;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Author：Libin on 2019/5/16 17:31
 * Email：1993911441@qq.com
 * Describe：单位邮箱
 */
public class CompanyEmailActivity extends BaseActivity implements AllViewInter {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.et_company_email)
    EditText etCompanyEmail;
    @BindView(R.id.et_email_code)
    EditText etEmailCode;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    @BindView(R.id.ll_send_code)
    LinearLayout llSendCode;
    @BindView(R.id.tv_edit_email_time)
    TextView tvEditEmailTime;
    @BindView(R.id.btn_edit_email)
    Button btnEditEmail;

    private boolean isEmail;
    private boolean isCode;
    private PersonalPrenInter mPersonalPrenInter;
    private int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmpany_email);
        ButterKnife.bind(this);

        getData();
    }

    private MyHandler mHandler = new MyHandler(this);


    private static class MyHandler extends Handler {
        private WeakReference<CompanyEmailActivity> mActivity;

        private MyHandler(CompanyEmailActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CompanyEmailActivity activity = mActivity.get();
            if (activity != null) {
                if (msg.what == 1) {
                    activity.tvSendCode.setText(msg.arg1 + "s");
                    activity.tvSendCode.setEnabled(false);
                } else if (msg.what == 2) {
                    activity.tvSendCode.setText("获取验证码");
                    activity.tvSendCode.setEnabled(true);
                }
            }

        }
    }

    private void getData() {
        mPersonalPrenInter = new PersonalPrenImpl(this);
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String editEmailTime = intent.getStringExtra("editEmailTime");
        ViewUnits.getInstance().setTitleHeight(viewStatusBar);
        tvTitleName.setText("单位邮箱");
        llTitle.setBackgroundResource(R.color.blue_0d8);

        if (!TextUtils.isEmpty(editEmailTime)) {
            tvEditEmailTime.setText("提交时间：" + editEmailTime);
        }


        if (!TextUtils.isEmpty(email)) {
            llSendCode.setVisibility(View.GONE);
            etCompanyEmail.setText(email);
            btnEditEmail.setEnabled(true);
            btnEditEmail.setText("修改");
            isEmail = true;
        } else {
            llSendCode.setVisibility(View.VISIBLE);
            btnEditEmail.setEnabled(false);
            btnEditEmail.setText("绑定");
            isEmail = false;
            type = 1;
            addListener();
        }


    }

    private void addListener() {
        etCompanyEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isEmail = FormatUtils.getInstance().isEMail(s.toString().trim());
                isEditFinish();
            }
        });

        etEmailCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isCode = !TextUtils.isEmpty(s.toString().trim());

                isEditFinish();
            }


        });
    }


    private void isEditFinish() {
        btnEditEmail.setEnabled(isEmail && isCode);
    }


    @OnClick({R.id.iv_title_back, R.id.tv_send_code, R.id.btn_edit_email})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_send_code:
                sendCode();
                break;
            case R.id.btn_edit_email:
                switch (type) {
                    case 0:
                        llSendCode.setVisibility(View.VISIBLE);
                        addListener();
                        btnEditEmail.setEnabled(false);
                        type = 2;
                        break;
                    case 1:
                        ViewUnits.getInstance().showLoading(CompanyEmailActivity.this, "绑定中");
                        mPersonalPrenInter.editEmail(NetCode.Personal.editEmail,
                                etCompanyEmail.getText().toString().trim(), etEmailCode.getText().toString().trim(),
                                DateUtils.getDate(Field.DateType.year_month_day, System.currentTimeMillis()));
                        break;
                    case 2:
                        ViewUnits.getInstance().showLoading(CompanyEmailActivity.this, "修改中");
                        mPersonalPrenInter.editEmail(NetCode.Personal.editEmail,
                                etCompanyEmail.getText().toString().trim(), etEmailCode.getText().toString().trim(),
                                DateUtils.getDate(Field.DateType.year_month_day, System.currentTimeMillis()));

                        break;
                }
                break;
        }
    }

    /**
     * 发送验证码
     */
    private void sendCode() {
        if (isEmail) {
            mPersonalPrenInter.sendEmailCode(NetCode.Personal.sendEmailCode, etCompanyEmail.getText().toString().trim());

        } else {
            ViewUnits.getInstance().showToast("请输入邮箱号");
        }

    }

    @Override
    public void onSuccessed(int what, Object object) {
        super.onSuccessed(what, object);
        ViewUnits.getInstance().missLoading();
        switch (what) {
            case NetCode.Personal.sendEmailCode:
                ViewUnits.getInstance().showToast("已发送");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 30; i > 0; i--) {
                            Message msg = new Message();
                            msg.what = 1;
                            msg.arg1 = i;
                            mHandler.sendMessage(msg);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        mHandler.sendEmptyMessage(2);
                    }
                }).start();
                break;
            case NetCode.Personal.editEmail:
                if (type == 1) {
                    ViewUnits.getInstance().showToast("绑定成功");
                } else {
                    ViewUnits.getInstance().showToast("修改成功");
                }

                finish();
                break;
        }
    }

    @Override
    public void onFailed(int what, Object object) {
        super.onFailed(what, object);
        ViewUnits.getInstance().missLoading();
        ViewUnits.getInstance().showToast(String.valueOf(object));
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

}
