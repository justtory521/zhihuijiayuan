package tendency.hz.zhihuijiayuan.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.MajorBean;
import tendency.hz.zhihuijiayuan.bean.ServiceBean;
import tendency.hz.zhihuijiayuan.bean.UserInfoBean;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PersonalPrenInter;
import tendency.hz.zhihuijiayuan.units.GsonUtils;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Author：Libin on 2019/5/16 11:01
 * Email：1993911441@qq.com
 * Describe：个人信息
 */
public class UserInfoActivity extends BaseActivity implements AllViewInter {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.tv_identity_card)
    TextView tvIdentityCard;
    @BindView(R.id.ll_identity_card)
    LinearLayout llIdentityCard;
    @BindView(R.id.tv_academic_status)
    TextView tvAcademicStatus;
    @BindView(R.id.ll_academic_status)
    LinearLayout llAcademicStatus;
    @BindView(R.id.tv_company_email)
    TextView tvCompanyEmail;
    @BindView(R.id.ll_company_email)
    LinearLayout llCompanyEmail;
    @BindView(R.id.tv_career_info)
    TextView tvCareerInfo;
    @BindView(R.id.ll_career_info)
    LinearLayout llCareerInfo;
    @BindView(R.id.tv_family_info)
    TextView tvFamilyInfo;
    @BindView(R.id.ll_family_info)
    LinearLayout llFamilyInfo;
    @BindView(R.id.tv_marital_status)
    TextView tvMaritalStatus;
    @BindView(R.id.ll_marital_status)
    LinearLayout llMaritalStatus;
    @BindView(R.id.tv_professional_certificate)
    TextView tvProfessionalCertificate;
    @BindView(R.id.ll_professional_certificate)
    LinearLayout llProfessionalCertificate;
    @BindView(R.id.tv_service_certificate)
    TextView tvServiceCertificate;
    @BindView(R.id.ll_service_certificate)
    LinearLayout llServiceCertificate;

    private PersonalPrenInter mPersonalPrenInter;
    private UserInfoBean.DataBean userInfoBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        ViewUnits.getInstance().setTitleHeight(viewStatusBar);
        llTitle.setBackgroundResource(R.color.colorPrimary);
        tvTitleName.setText("个人信息");
        mPersonalPrenInter = new PersonalPrenImpl(this);
        ViewUnits.getInstance().showLoading(this, "加载中");
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPersonalPrenInter.getAllInfo(NetCode.Personal.getAllInfo);
        mPersonalPrenInter.getMajorList(NetCode.Personal.getMajorList);
        mPersonalPrenInter.getServiceList(NetCode.Personal.getServiceList);
    }

    @Override
    public void onSuccessed(int what, Object object) {
        super.onSuccessed(what, object);

        switch (what) {
            case NetCode.Personal.getAllInfo:
                ViewUnits.getInstance().missLoading();
                userInfoBean = (UserInfoBean.DataBean) object;
                initUserInfo();
                break;
            case NetCode.Personal.getMajorList:
                List<MajorBean.DataBean> majorList = (List<MajorBean.DataBean>) object;
                if (majorList != null && majorList.size() > 0) {
                    tvProfessionalCertificate.setVisibility(View.GONE);
                } else {
                    tvProfessionalCertificate.setVisibility(View.VISIBLE);
                }

                break;
            case NetCode.Personal.getServiceList:
                List<ServiceBean.DataBean> serviceList = (List<ServiceBean.DataBean>) object;
                if (serviceList != null && serviceList.size() > 0) {
                    tvServiceCertificate.setVisibility(View.GONE);
                } else {
                    tvServiceCertificate.setVisibility(View.VISIBLE);
                }
                break;
        }


    }

    private void initUserInfo() {
        if (!TextUtils.isEmpty(userInfoBean.getCardID())) {
            tvIdentityCard.setVisibility(View.GONE);
        } else {
            tvIdentityCard.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(userInfoBean.getSchoolName())) {
            tvAcademicStatus.setVisibility(View.GONE);
        } else {
            tvAcademicStatus.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(userInfoBean.getEmail())) {
            tvCompanyEmail.setVisibility(View.GONE);
        } else {
            tvCompanyEmail.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(userInfoBean.getJobCompany())) {
            tvCareerInfo.setVisibility(View.GONE);
        } else {
            tvCareerInfo.setVisibility(View.VISIBLE);
        }


        if (!TextUtils.isEmpty(userInfoBean.getFamilyFatherName())) {
            tvFamilyInfo.setVisibility(View.GONE);
        } else {
            tvFamilyInfo.setVisibility(View.VISIBLE);
        }

        if (userInfoBean.getMarriageStatus() != null) {
            tvMaritalStatus.setVisibility(View.GONE);
        } else {
            tvMaritalStatus.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onFailed(int what, Object object) {
        super.onFailed(what, object);
        ViewUnits.getInstance().missLoading();
        ViewUnits.getInstance().showToast(object.toString());
    }

    @OnClick({R.id.iv_title_back, R.id.ll_identity_card, R.id.ll_academic_status, R.id.ll_company_email,
            R.id.ll_career_info, R.id.ll_family_info, R.id.ll_marital_status, R.id.ll_professional_certificate, R.id.ll_service_certificate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.ll_identity_card:
                if (userInfoBean != null) {
                    Intent intent = new Intent(UserInfoActivity.this, IdentityCardActivity.class);
                    intent.putExtra("realName", userInfoBean.getRealName());
                    intent.putExtra("cardId", userInfoBean.getCardID());
                    intent.putExtra("editTime", userInfoBean.getEditIDCardTime());
                    startActivity(intent);
                }
                break;
            case R.id.ll_academic_status:
                if (userInfoBean != null) {
                    Intent intent = new Intent(UserInfoActivity.this, AcademicStatusActivity.class);
                    intent.putExtra("schoolName", userInfoBean.getSchoolName());
                    intent.putExtra("educationType", userInfoBean.getEducationType());
                    intent.putExtra("educationEndTime", userInfoBean.getEducationEndTime());
                    intent.putExtra("editEduTime", userInfoBean.getEditEduTime());
                    intent.putExtra("eduDistrictData", (Serializable) userInfoBean.getEduDistricData());
                    startActivity(intent);
                }
                break;
            case R.id.ll_company_email:
                if (userInfoBean != null) {
                    Intent intent = new Intent(UserInfoActivity.this, CompanyEmailActivity.class);
                    intent.putExtra("email", userInfoBean.getEmail());
                    intent.putExtra("editEmailTime", userInfoBean.getEditMailTime());
                    startActivity(intent);
                }
                break;
            case R.id.ll_career_info:
                if (userInfoBean != null) {
                    Intent intent = new Intent(UserInfoActivity.this, CareerInfoActivity.class);
                    intent.putExtra("JobCompany", userInfoBean.getJobCompany());
                    intent.putExtra("JobDepartment", userInfoBean.getJobDepartment());
                    intent.putExtra("JobPosition", userInfoBean.getJobPosition());
                    intent.putExtra("JobEntryTime", userInfoBean.getJobEntryTime());
                    intent.putExtra("JobFirstWorkTime", userInfoBean.getJobFirstWorkTime());
                    intent.putExtra("EditJobTime", userInfoBean.getEditJobTime());
                    startActivity(intent);
                }
                break;
            case R.id.ll_family_info:
                if (userInfoBean != null) {
                    Intent intent = new Intent(UserInfoActivity.this, FamilyInfoActivity.class);
                    intent.putExtra("FamilyFatherName", userInfoBean.getFamilyFatherName());
                    intent.putExtra("FamilyFatherPhone", userInfoBean.getFamilyFatherPhone());
                    intent.putExtra("FamilyMotherName", userInfoBean.getFamilyMotherName());
                    intent.putExtra("FamilyMotherPhone", userInfoBean.getFamilyMotherPhone());
                    intent.putExtra("FamilyIsOnlyChild", userInfoBean.getFamilyIsOnlyChild());
                    intent.putExtra("EditFamilyTime", userInfoBean.getEditFamilyTime());
                    startActivity(intent);
                }
                break;
            case R.id.ll_marital_status:
                if (userInfoBean != null) {
                    Intent intent = new Intent(UserInfoActivity.this, MaritalStatusActivity.class);
                    intent.putExtra("marriageStatus", userInfoBean.getMarriageStatus());
                    intent.putExtra("marriageSpouseName", userInfoBean.getMarriageSpouseName());
                    intent.putExtra("marriageHaveChildren", userInfoBean.getMarriageHaveChildren());
                    intent.putExtra("marriageChildrenNum", userInfoBean.getMarriageChildrenNum());
                    intent.putExtra("editMarriageTime", userInfoBean.getEditMarriageTime());
                    startActivity(intent);
                }
                break;
            case R.id.ll_professional_certificate:
                if (userInfoBean != null) {
                    Intent intent = new Intent(UserInfoActivity.this, AddCertificateActivity.class);
                    intent.putExtra("certificate_type", 1);
                    startActivity(intent);
                }
                break;
            case R.id.ll_service_certificate:
                if (userInfoBean != null) {
                    Intent intent = new Intent(UserInfoActivity.this, AddCertificateActivity.class);
                    intent.putExtra("certificate_type", 2);
                    startActivity(intent);
                }
                break;
        }
    }
}
