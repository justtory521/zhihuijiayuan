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

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.smarttop.library.bean.City;
import com.smarttop.library.bean.County;
import com.smarttop.library.bean.Province;
import com.smarttop.library.bean.Street;
import com.smarttop.library.widget.BottomDialog;
import com.smarttop.library.widget.OnAddressSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.UserInfoBean;
import tendency.hz.zhihuijiayuan.bean.base.Field;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PersonalPrenInter;
import tendency.hz.zhihuijiayuan.units.DateUtils;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Author：Libin on 2019/5/16 15:11
 * Email：1993911441@qq.com
 * Describe：学历学籍
 */
public class AcademicStatusActivity extends BaseActivity implements AllViewInter, OnAddressSelectedListener {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.tv_select_address)
    TextView tvSelectAddress;
    @BindView(R.id.et_school_name)
    EditText etSchoolName;
    @BindView(R.id.tv_select_academic)
    TextView tvSelectAcademic;
    @BindView(R.id.tv_graduate_time)
    TextView tvGraduateTime;
    @BindView(R.id.tv_edit_academic_time)
    TextView tvEditAcademicTime;
    @BindView(R.id.btn_edit_academic)
    Button btnEditAcademic;

    private PersonalPrenInter mPersonalPrenInter;

    private boolean isSchool;
    private boolean isEducationTime;
    private boolean isDistrict;
    private int educationType;
    private List<String> typeList = new ArrayList<>();

    private BottomDialog mAddressPicker = null; //地址选择器
    private TimePickerView mTimePickerView = null; //日期选择器
    private String educationEndTime;
    private int addressId;


    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic_status);
        ButterKnife.bind(this);
        ViewUnits.getInstance().setTitleHeight(viewStatusBar);
        tvTitleName.setText("学历学籍");
        llTitle.setBackgroundResource(R.color.blue_0d8);
        mPersonalPrenInter = new PersonalPrenImpl(this);
        getData();
    }

    private void getData() {
        Intent intent = getIntent();
        String schoolName = intent.getStringExtra("schoolName");
        educationType = intent.getIntExtra("educationType", 0);
        educationEndTime = intent.getStringExtra("educationEndTime");
        String editEduTime;
        editEduTime = intent.getStringExtra("editEduTime");
        List<UserInfoBean.DataBean.EduDistricDataBean> districtDataBean =
                (List<UserInfoBean.DataBean.EduDistricDataBean>) intent.getSerializableExtra("eduDistrictData");

        if (!TextUtils.isEmpty(schoolName)) {
            etSchoolName.setText(schoolName);
            isSchool = true;
        } else {
            isSchool = false;
        }

        typeList.add("小学");
        typeList.add("初中");
        typeList.add("高中");
        typeList.add("大专");
        typeList.add("本科");
        typeList.add("硕士");
        typeList.add("博士");

        if (educationType > 0) {
            tvSelectAcademic.setText(typeList.get(educationType - 1));
        }

        if (!TextUtils.isEmpty(educationEndTime)) {
            tvGraduateTime.setText(educationEndTime);
            isEducationTime = true;
        } else {
            isEducationTime = false;
        }


        if (!TextUtils.isEmpty(editEduTime)) {
            tvEditAcademicTime.setText("提交时间：" + editEduTime);
        }


        if (districtDataBean != null && districtDataBean.size() > 0) {
            String address = "";
            for (UserInfoBean.DataBean.EduDistricDataBean eduDistricDataBean : districtDataBean) {
                address += eduDistricDataBean.getName() + " ";
                addressId = eduDistricDataBean.getID();
            }
            tvSelectAddress.setText(address.trim());
            isDistrict = true;
        } else {
            isDistrict = false;
        }

        isEditFinish();

        etSchoolName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isSchool = !TextUtils.isEmpty(s.toString().trim());
                isEditFinish();
            }
        });


        //初始化地址选择器
        mAddressPicker = new BottomDialog(this);
        mAddressPicker.setIndicatorBackgroundColor(R.color.colorPrimary);
        mAddressPicker.setTextSelectedColor(R.color.colorPrimary);
        mAddressPicker.setTextUnSelectedColor(R.color.colorTextBlack);
        mAddressPicker.setOnAddressSelectedListener(this);

        Calendar startTime = Calendar.getInstance();
        startTime.set(1900, 0, 1);


        Calendar defaultTime = Calendar.getInstance();
        defaultTime.set(1985, 0, 1);


        Calendar endTime = Calendar.getInstance();
        endTime.set(endTime.get(Calendar.YEAR), endTime.get(Calendar.MONTH), endTime.get(Calendar.DATE));

        //初始化日期选择器
        mTimePickerView = new TimePickerView.Builder(this, (date, v) -> {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            educationEndTime = format.format(date);
            tvGraduateTime.setText(educationEndTime);
            isEducationTime = true;
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
        btnEditAcademic.setEnabled(isSchool && isDistrict && isEducationTime && educationType > 0);
    }

    @OnClick({R.id.iv_title_back, R.id.tv_select_address, R.id.tv_select_academic, R.id.tv_graduate_time, R.id.btn_edit_academic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_select_address:
                if (mAddressPicker != null) {
                    mAddressPicker.show();
                }

                break;
            case R.id.tv_select_academic:
                selectType();
                break;
            case R.id.tv_graduate_time:
                if (mTimePickerView != null) {
                    mTimePickerView.show();
                }
                break;
            case R.id.btn_edit_academic:
                ViewUnits.getInstance().showLoading(AcademicStatusActivity.this, "修改中");
                mPersonalPrenInter.editEducation(NetCode.Personal.editEducation, addressId,
                        etSchoolName.getText().toString().trim(), educationType, educationEndTime,
                        DateUtils.getDate(Field.DateType.year_month_day, System.currentTimeMillis()));
                break;
        }
    }


    private void selectType() {

        OptionsPickerView build = new OptionsPickerView.Builder(AcademicStatusActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
            educationType = options1 + 1;
            tvSelectAcademic.setText(typeList.get(educationType - 1));
            isEditFinish();
        }
    }).setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentTextSize(15)//滚轮文字大小
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .setTextColorCenter(Color.BLACK)//设置选中项的颜色
                .setSubmitColor(getResources().getColor(R.color.colorPrimary))//确定按钮文字颜色
            .setSubCalSize(13)
                .setCancelColor(getResources().getColor(R.color.colorTextBlack))//取消按钮文字颜色
            .setTitleBgColor(getResources().getColor(R.color.commom_background2))//标题背景颜色 Night mode
            .build();
        build.setPicker(typeList);
        build.show();
    }

    @Override
    public void onSuccessed(int what, Object object) {
        super.onSuccessed(what, object);
        ViewUnits.getInstance().missLoading();
        if (what == NetCode.Personal.editEducation) {
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

    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {
        isDistrict = true;
        if (province.name.equals("澳门特别行政区")) {
            if (city != null) {
                addressId = city.id;
                tvSelectAddress.setText(province.name + " " + city.name);
                mAddressPicker.dismiss();
                return;
            }
        }

        if (county == null) return;
        addressId = county.id;
        tvSelectAddress.setText(province.name + " " + city.name + " " + county.name);
        isEditFinish();
        mAddressPicker.dismiss();
    }
}
