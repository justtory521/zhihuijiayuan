package tendency.hz.zhihuijiayuan.view.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import com.bigkoo.pickerview.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.base.Field;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PersonalPrenInter;
import tendency.hz.zhihuijiayuan.units.DateUtils;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;

/**
 * Author：Libin on 2019/5/17 10:14
 * Email：1993911441@qq.com
 * Describe：职业信息
 */
public class CareerInfoActivity extends BaseActivity {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.et_company_name)
    EditText etCompanyName;
    @BindView(R.id.et_company_department)
    EditText etCompanyDepartment;
    @BindView(R.id.et_career_name)
    EditText etCareerName;
    @BindView(R.id.tv_career_time)
    TextView tvCareerTime;
    @BindView(R.id.tv_first_career_time)
    TextView tvFirstCareerTime;
    @BindView(R.id.tv_edit_career_time)
    TextView tvEditCareerTime;
    @BindView(R.id.btn_edit_career)
    Button btnEditCareer;

    private PersonalPrenInter mPersonalPrenInter;
    private TimePickerView mTimePickerView;

    private boolean isCompanyName;
    private boolean isDepartment;
    private boolean isCareerName;
    private boolean isCareerTime;
    private boolean isFirstCareer;

    private int type;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_career_info);
        ButterKnife.bind(this);

        ViewUnits.getInstance().setTitleHeight(viewStatusBar);
        tvTitleName.setText("职业信息");
        llTitle.setBackgroundResource(R.color.blue_0d8);
        mPersonalPrenInter = new PersonalPrenImpl(this);

        getData();

    }

    private void getData() {
        Intent intent = getIntent();
        String companyName = intent.getStringExtra("JobCompany");
        String companyDepartment = intent.getStringExtra("JobDepartment");
        String careerName = intent.getStringExtra("JobPosition");
        String careerTime = intent.getStringExtra("JobEntryTime");
        String firstCareerTime = intent.getStringExtra("JobFirstWorkTime");
        String editTime = intent.getStringExtra("EditJobTime");

        if (!TextUtils.isEmpty(companyName)) {
            etCompanyName.setText(companyName);
            isCompanyName = true;
        } else {
            isCompanyName = false;
        }

        if (!TextUtils.isEmpty(companyDepartment)) {
            etCompanyDepartment.setText(companyDepartment);
            isDepartment = true;
        } else {
            isDepartment = false;
        }


        if (!TextUtils.isEmpty(careerName)) {
            etCareerName.setText(careerName);
            isCareerName = true;
        } else {
            isCareerName = false;
        }


        if (!TextUtils.isEmpty(careerTime)) {
            tvCareerTime.setText(careerTime);
            isCareerTime = true;
        } else {
            isCareerTime = false;
        }


        if (!TextUtils.isEmpty(firstCareerTime)) {
            tvFirstCareerTime.setText(firstCareerTime);
            isFirstCareer = true;
        } else {
            isFirstCareer = false;
        }


        if (!TextUtils.isEmpty(editTime)) {
            tvEditCareerTime.setText("提交时间：" + editTime);
        }


        isEditFinish();


        etCompanyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isCompanyName = !TextUtils.isEmpty(s.toString().trim());
                isEditFinish();
            }
        });

        etCompanyDepartment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isDepartment = !TextUtils.isEmpty(s.toString().trim());
                isEditFinish();
            }
        });


        etCareerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isCareerName = !TextUtils.isEmpty(s.toString().trim());
                isEditFinish();
            }
        });


        Calendar startTime = Calendar.getInstance();
        startTime.set(1900, 0, 1);


        Calendar defaultTime = Calendar.getInstance();
        defaultTime.set(1985, 0, 1);


        Calendar endTime = Calendar.getInstance();
        endTime.set(endTime.get(Calendar.YEAR), endTime.get(Calendar.MONTH),endTime.get(Calendar.DATE));

        //初始化日期选择器
        mTimePickerView = new TimePickerView.Builder(this, (date, v) -> {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            String time = format.format(date);
            if (type == 1) {
                tvCareerTime.setText(time);
                isCareerTime = true;
            } else {
                tvFirstCareerTime.setText(time);
                isFirstCareer = true;
            }

            isEditFinish();
        })
                .setType(TimePickerView.Type.YEAR_MONTH)//默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentSize(15)//滚轮文字大小
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .setTextColorCenter(Color.BLACK)//设置选中项的颜色
                .setSubmitColor(getResources().getColor(R.color.colorPrimary))//确定按钮文字颜色
                .setSubCalSize(13)
                .setCancelColor(getResources().getColor(R.color.colorTextBlack))//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.commom_background2))//标题背景颜色 Night mode
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDate(defaultTime)
                .setRangDate(startTime, endTime)
                .build();

    }

    private void isEditFinish() {
        btnEditCareer.setEnabled(isCompanyName && isDepartment && isCareerName && isCareerTime && isFirstCareer);
    }

    @OnClick({R.id.iv_title_back, R.id.tv_career_time, R.id.tv_first_career_time, R.id.btn_edit_career})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_career_time:
                type = 1;
                mTimePickerView.show();
                break;
            case R.id.tv_first_career_time:
                type = 2;
                mTimePickerView.show();
                break;
            case R.id.btn_edit_career:
                ViewUnits.getInstance().showLoading(CareerInfoActivity.this, "修改中");
                mPersonalPrenInter.editJob(NetCode.Personal.editJob, etCompanyName.getText().toString().trim(),
                        etCompanyDepartment.getText().toString().trim(), etCareerName.getText().toString().trim(),
                        tvCareerTime.getText().toString().trim(), tvFirstCareerTime.getText().toString().trim(),
                        DateUtils.getDate(Field.DateType.year_month_day, System.currentTimeMillis()));
                break;
        }
    }

    @Override
    public void onSuccessed(int what, Object object) {
        super.onSuccessed(what, object);
        ViewUnits.getInstance().missLoading();
        if (what == NetCode.Personal.editJob) {
            ViewUnits.getInstance().showToast("修改成功");
            finish();
        }
    }

    @Override
    public void onFailed(int what, Object object) {
        super.onFailed(what, object);
        ViewUnits.getInstance().missLoading();
        ViewUnits.getInstance().showToast(object.toString());
    }
}
