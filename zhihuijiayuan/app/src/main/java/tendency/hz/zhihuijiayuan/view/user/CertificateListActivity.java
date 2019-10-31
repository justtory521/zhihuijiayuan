package tendency.hz.zhihuijiayuan.view.user;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.MajorBean;
import tendency.hz.zhihuijiayuan.bean.ServiceBean;
import tendency.hz.zhihuijiayuan.bean.UserInfoBean;
import tendency.hz.zhihuijiayuan.bean.base.Field;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.units.DateUtils;
import tendency.hz.zhihuijiayuan.units.SpaceItemDecoration;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Author：Libin on 2019/5/17 15:37
 * Email：1993911441@qq.com
 * Describe：查看证书
 */
public class CertificateListActivity extends BaseActivity implements AllViewInter {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.rv_certificate)
    RecyclerView rvCertificate;


    private List<MajorBean.DataBean> majorList = new ArrayList<>();
    private List<ServiceBean.DataBean> serviceList = new ArrayList<>();
    private PersonalPrenImpl mPersonalPrenInter;
    private int certificateType;

    private BaseQuickAdapter<MajorBean.DataBean, BaseViewHolder> majorAdapter;
    private BaseQuickAdapter<ServiceBean.DataBean, BaseViewHolder> serviceAdapter;
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate_list);
        ButterKnife.bind(this);


        ViewUnits.getInstance().setTitleHeight(viewStatusBar);
        llTitle.setBackgroundResource(R.color.blue_0d8);


        mPersonalPrenInter = new PersonalPrenImpl(this);

        getData();
    }

    private void getData() {
        certificateType = getIntent().getIntExtra("certificate_type", 0);
        ViewUnits.getInstance().showLoading(this, "加载中");
        rvCertificate.setLayoutManager(new LinearLayoutManager(this));
        rvCertificate.addItemDecoration(new SpaceItemDecoration(ViewUnits.getInstance().dp2px(this, 12)));
        if (certificateType == 1) {
            tvTitleName.setText("专业证书");
            mPersonalPrenInter.getMajorList(NetCode.Personal.getMajorList);
            majorAdapter = new BaseQuickAdapter<MajorBean.DataBean, BaseViewHolder>(R.layout.rv_certificate_item, majorList) {
                @Override
                protected void convert(BaseViewHolder helper, MajorBean.DataBean item) {
                    helper.setText(R.id.tv_name_certificate, item.getProfessionalName());
                    helper.setText(R.id.tv_certificate_time, DateUtils.formatTime(Field.DateType.standard_time_format,
                            Field.DateType.year_month_day, item.getCreateTime()));
                    SimpleDraweeView sdvCertificate = helper.getView(R.id.sdv_certificate);
                    sdvCertificate.setImageURI(item.getImageUrl());

                    ImageView ivDelete = helper.getView(R.id.iv_delete_service);

                    ivDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(CertificateListActivity.this)
                                    .setTitle("提示")
                                    .setMessage("是否删除专业证书")
                                    .setIcon(R.mipmap.logo)
                                    .setPositiveButton("确定", (dialogInterface, i) -> {
                                        position = helper.getLayoutPosition();
                                        mPersonalPrenInter.deleteMajor(NetCode.Personal.deleteMajor, item.getID());
                                    })
                                    .setNegativeButton("取消", (dialogInterface, i) -> {

                                    })
                                    .create().show();
                        }
                    });
                }
            };

            rvCertificate.setAdapter(majorAdapter);


        } else {
            tvTitleName.setText("服务证书");
            mPersonalPrenInter.getServiceList(NetCode.Personal.getServiceList);

            serviceAdapter = new BaseQuickAdapter<ServiceBean.DataBean, BaseViewHolder>(R.layout.rv_certificate_item, serviceList) {
                @Override
                protected void convert(BaseViewHolder helper, ServiceBean.DataBean item) {
                    helper.setText(R.id.tv_name_certificate, item.getServiceName());
                    helper.setText(R.id.tv_certificate_time, DateUtils.formatTime(Field.DateType.standard_time_format,
                            Field.DateType.year_month_day, item.getCreateTime()));
                    TextView tvTag = helper.getView(R.id.tv_certificate_tag);
                    tvTag.setVisibility(View.VISIBLE);
                    tvTag.setText(item.getServiceType());
                    SimpleDraweeView sdvCertificate = helper.getView(R.id.sdv_certificate);
                    sdvCertificate.setImageURI(item.getImageUrl());

                    ImageView ivDelete = helper.getView(R.id.iv_delete_service);

                    ivDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(CertificateListActivity.this)
                                    .setTitle("提示")
                                    .setMessage("是否删除服务证书")
                                    .setIcon(R.mipmap.logo)
                                    .setPositiveButton("确定", (dialogInterface, i) -> {
                                        position = helper.getLayoutPosition();
                                        mPersonalPrenInter.deleteService(NetCode.Personal.deleteService, item.getID());
                                    })
                                    .setNegativeButton("取消", (dialogInterface, i) -> {

                                    })
                                    .create().show();
                        }
                    });
                }
            };

            rvCertificate.setAdapter(serviceAdapter);
        }

    }

    @Override
    public void onSuccessed(int what, Object object) {
        super.onSuccessed(what, object);
        ViewUnits.getInstance().missLoading();
        switch (what) {
            case NetCode.Personal.getMajorList:
                List<MajorBean.DataBean> list1 = (List<MajorBean.DataBean>) object;
                if (list1 != null && list1.size() > 0) {
                    majorList.addAll(list1);
                    majorAdapter.notifyDataSetChanged();
                }
                break;
            case NetCode.Personal.getServiceList:
                List<ServiceBean.DataBean> list2 = (List<ServiceBean.DataBean>) object;
                if (list2 != null && list2.size() > 0) {
                    serviceList.addAll(list2);
                    serviceAdapter.notifyDataSetChanged();
                }
                break;

            case NetCode.Personal.deleteMajor:
                ViewUnits.getInstance().showToast("删除成功");


                majorList.remove(position);

                majorAdapter.notifyItemRemoved(position);
                majorAdapter.notifyItemChanged(position, majorList.size() - position);

                break;
            case NetCode.Personal.deleteService:
                ViewUnits.getInstance().showToast("删除成功");

                serviceList.remove(position);

                serviceAdapter.notifyItemRemoved(position);
                serviceAdapter.notifyItemChanged(position, serviceList.size() - position);
                break;

        }
    }

    @Override
    public void onFailed(int what, Object object) {
        super.onFailed(what, object);
        ViewUnits.getInstance().missLoading();
        ViewUnits.getInstance().showToast(object.toString());
    }

    @OnClick(R.id.iv_title_back)
    public void onViewClicked() {
        finish();
    }
}
