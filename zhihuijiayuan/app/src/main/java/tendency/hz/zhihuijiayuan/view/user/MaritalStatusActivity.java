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
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Author：Libin on 2019/5/17 11:52
 * Email：1993911441@qq.com
 * Describe：婚姻状况
 */
public class MaritalStatusActivity extends BaseActivity implements AllViewInter {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.tv_is_marry)
    TextView tvIsMarry;
    @BindView(R.id.et_spouse_name)
    EditText etSpouseName;
    @BindView(R.id.tv_has_child)
    TextView tvHasChild;
    @BindView(R.id.tv_child_num)
    TextView tvChildNum;
    @BindView(R.id.tv_edit_marry_time)
    TextView tvEditMarryTime;
    @BindView(R.id.btn_edit_marry)
    Button btnEditMarry;
    @BindView(R.id.ll_spouse_name)
    LinearLayout llSpouseName;
    @BindView(R.id.ll_has_child)
    LinearLayout llHasChild;
    @BindView(R.id.ll_child_num)
    LinearLayout llChildNum;

    private PersonalPrenInter mPersonalPrenInter;

    private List<String> marryList = new ArrayList<>();
    private List<String> hasChildList = new ArrayList<>();
    private List<String> childNumList = new ArrayList<>();


    private int isMarried;
    private int hasChild;
    private int childNum;
    private OptionsPickerView optionsPickerView;
    private int type;
    private boolean isSpouseName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marital_status);
        ButterKnife.bind(this);

        ViewUnits.getInstance().setTitleHeight(viewStatusBar);
        tvTitleName.setText("婚姻状况");
        llTitle.setBackgroundResource(R.color.blue_0d8);
        mPersonalPrenInter = new PersonalPrenImpl(this);
        getData();
    }

    private void getData() {
        Intent intent = getIntent();
        isMarried = intent.getIntExtra("marriageStatus", 0);
        String spouseName = intent.getStringExtra("marriageSpouseName");
        hasChild = intent.getIntExtra("marriageHaveChildren", 0);
        childNum = intent.getIntExtra("marriageChildrenNum", 0);

        String editTime = intent.getStringExtra("editMarriageTime");

        if (!TextUtils.isEmpty(editTime)) {
            tvEditMarryTime.setText("提交时间：" + editTime);
        }



        marryList.add("未婚");
        marryList.add("已婚");


        hasChildList.add("是");
        hasChildList.add("否");

        for (int i = 1; i < 11; i++) {
            childNumList.add(String.valueOf(i));
            isSpouseName = true;
        }


        if (!TextUtils.isEmpty(spouseName)) {
            etSpouseName.setText(spouseName);
        }

        etSpouseName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isSpouseName = !TextUtils.isEmpty(s.toString().trim());
                isFinishEdit();
            }
        });


        isFinishEdit();


        optionsPickerView = new OptionsPickerView.Builder(MaritalStatusActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                switch (type) {
                    case 1:
                        isMarried = options1 + 1;
                        tvIsMarry.setText(marryList.get(isMarried - 1));
                        break;
                    case 2:
                        hasChild = options1 + 1;
                        tvHasChild.setText(hasChildList.get(hasChild - 1));
                        break;
                    case 3:
                        childNum = options1 + 1;
                        tvChildNum.setText(childNumList.get(childNum - 1));
                        break;
                }

                isFinishEdit();
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
    }

    /**
     * 是否编辑完成
     */
    private void isFinishEdit() {
        if (isMarried == 0) {
            llSpouseName.setVisibility(View.GONE);
            llHasChild.setVisibility(View.GONE);
            llChildNum.setVisibility(View.GONE);

            btnEditMarry.setEnabled(false);

        } else if (isMarried == 2) {
            tvIsMarry.setText(marryList.get(isMarried - 1));
            llSpouseName.setVisibility(View.VISIBLE);
            llHasChild.setVisibility(View.VISIBLE);

            if (hasChild == 0) {
                llChildNum.setVisibility(View.GONE);
                btnEditMarry.setEnabled(false);
            } else if (hasChild == 1) {
                llChildNum.setVisibility(View.VISIBLE);
                tvHasChild.setText(hasChildList.get(hasChild - 1));

                if (childNum > 0) {
                    tvChildNum.setText(childNumList.get(childNum - 1));
                    btnEditMarry.setEnabled(isSpouseName);
                } else {
                    btnEditMarry.setEnabled(false);
                }
            } else if (hasChild == 2) {
                llChildNum.setVisibility(View.GONE);
                tvHasChild.setText(hasChildList.get(hasChild - 1));

                btnEditMarry.setEnabled(isSpouseName);
                childNum = 0;
            }
        } else if (isMarried == 1) {
            tvIsMarry.setText(marryList.get(isMarried - 1));
            llSpouseName.setVisibility(View.GONE);
            llHasChild.setVisibility(View.GONE);
            llChildNum.setVisibility(View.GONE);

            isSpouseName = false;
            hasChild = 2;
            childNum = 0;

            btnEditMarry.setEnabled(true);
        }

    }


    @OnClick({R.id.iv_title_back, R.id.tv_is_marry, R.id.tv_has_child, R.id.tv_child_num, R.id.btn_edit_marry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_is_marry:
                type = 1;
                optionsPickerView.setPicker(marryList);
                optionsPickerView.show();
                break;
            case R.id.tv_has_child:
                type = 2;
                optionsPickerView.setPicker(hasChildList);
                optionsPickerView.show();
                break;
            case R.id.tv_child_num:
                type = 3;
                optionsPickerView.setPicker(childNumList);
                optionsPickerView.show();
                break;
            case R.id.btn_edit_marry:

                ViewUnits.getInstance().showLoading(MaritalStatusActivity.this, "修改中");


                mPersonalPrenInter.editMarriage(NetCode.Personal.editMarriage, isMarried,
                        isSpouseName ? etSpouseName.getText().toString().trim() : null, hasChild, childNum,
                        DateUtils.getDate(Field.DateType.year_month_day, System.currentTimeMillis()));
                break;
        }
    }

    @Override
    public void onSuccessed(int what, Object object) {
        super.onSuccessed(what, object);
        ViewUnits.getInstance().missLoading();
        if (what == NetCode.Personal.editMarriage) {
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
