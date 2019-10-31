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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;

import java.util.ArrayList;
import java.util.List;

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
 * Author：Libin on 2019/5/17 11:07
 * Email：1993911441@qq.com
 * Describe：家庭状况
 */
public class FamilyInfoActivity extends BaseActivity implements AllViewInter {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.et_father_name)
    EditText etFatherName;
    @BindView(R.id.et_father_phone)
    EditText etFatherPhone;
    @BindView(R.id.et_mother_name)
    EditText etMotherName;
    @BindView(R.id.et_mother_phone)
    EditText etMotherPhone;
    @BindView(R.id.tv_only_child)
    TextView tvOnlyChild;
    @BindView(R.id.tv_edit_family_info)
    TextView tvEditFamilyInfo;
    @BindView(R.id.btn_edit_family_info)
    Button btnEditFamilyInfo;

    private PersonalPrenInter mPersonalPrenInter;
    private List<String> typeList = new ArrayList<>();
    private int isOnlyChild;
    private boolean isFatherName;
    private boolean isFatherPhone;
    private boolean isMotherName;
    private boolean isMotherPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_info);
        ButterKnife.bind(this);

        ViewUnits.getInstance().setTitleHeight(viewStatusBar);
        tvTitleName.setText("家庭情况");
        llTitle.setBackgroundResource(R.color.blue_0d8);
        mPersonalPrenInter = new PersonalPrenImpl(this);
        getData();
    }

    private void getData() {
        Intent intent = getIntent();
        String fatherName = intent.getStringExtra("FamilyFatherName");
        String fatherPhone = intent.getStringExtra("FamilyFatherPhone");
        String motherName = intent.getStringExtra("FamilyMotherName");
        String motherPhone = intent.getStringExtra("FamilyMotherPhone");

        String editTime = intent.getStringExtra("EditFamilyTime");
        isOnlyChild = intent.getIntExtra("FamilyIsOnlyChild", 0);


        if (!TextUtils.isEmpty(fatherName)) {
            etFatherName.setText(fatherName);
            isFatherName = true;
        } else {
            isFatherName = false;
        }

        if (!TextUtils.isEmpty(fatherPhone)) {
            etFatherPhone.setText(fatherPhone);
            isFatherPhone = true;
        } else {
            isFatherPhone = false;
        }


        if (!TextUtils.isEmpty(motherName)) {
            etMotherName.setText(motherName);
            isMotherName = true;
        } else {
            isMotherName = false;
        }


        if (!TextUtils.isEmpty(motherPhone)) {
            etMotherPhone.setText(motherPhone);
            isMotherPhone = true;
        } else {
            isMotherPhone = false;
        }


        if (!TextUtils.isEmpty(editTime)) {
            tvEditFamilyInfo.setText("提交时间：" + editTime);
        }

        typeList.add("是");
        typeList.add("否");

        if (isOnlyChild > 0) {
            tvOnlyChild.setText(typeList.get(isOnlyChild - 1));
        }

        isEditFinish();


        etFatherName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isFatherName = !TextUtils.isEmpty(s.toString().trim());
                isEditFinish();
            }
        });

        etFatherPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isFatherPhone = FormatUtils.getInstance().isPhone(s.toString().trim());
                isEditFinish();
            }
        });

        etMotherName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isMotherName = !TextUtils.isEmpty(s.toString().trim());
                isEditFinish();
            }
        });

        etMotherPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isMotherPhone = FormatUtils.getInstance().isPhone(s.toString().trim());
                isEditFinish();
            }
        });


    }

    private void isEditFinish() {
        btnEditFamilyInfo.setEnabled(isFatherName && isFatherPhone && isMotherName && isMotherPhone && isOnlyChild > 0);
    }

    @OnClick({R.id.iv_title_back, R.id.tv_only_child, R.id.btn_edit_family_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_only_child:
                selectType();
                break;
            case R.id.btn_edit_family_info:
                mPersonalPrenInter.editFamily(NetCode.Personal.editFamily, etFatherName.getText().toString().trim(),
                        etFatherPhone.getText().toString().trim(), etMotherName.getText().toString().trim(),
                        etMotherPhone.getText().toString().trim(), isOnlyChild, DateUtils.getDate(Field.DateType.year_month_day,
                                System.currentTimeMillis()));
                break;
        }
    }

    private void selectType() {
        //条件选择器
        OptionsPickerView build = new OptionsPickerView.Builder(FamilyInfoActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                isOnlyChild = options1 + 1;
                tvOnlyChild.setText(typeList.get(isOnlyChild - 1));
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
        if (what == NetCode.Personal.editFamily) {
            ViewUnits.getInstance().missLoading();
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
