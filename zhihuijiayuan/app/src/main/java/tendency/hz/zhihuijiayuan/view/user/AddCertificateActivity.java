package tendency.hz.zhihuijiayuan.view.user;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.tools.PictureFileUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.base.App;
import tendency.hz.zhihuijiayuan.bean.base.Field;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.presenter.BasePrenImpl;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PersonalPrenInter;
import tendency.hz.zhihuijiayuan.units.Base64Utils;
import tendency.hz.zhihuijiayuan.units.DateUtils;
import tendency.hz.zhihuijiayuan.units.ImageUtils;
import tendency.hz.zhihuijiayuan.units.PermissionUtils;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Author：Libin on 2019/5/17 14:01
 * Email：1993911441@qq.com
 * Describe：证书
 */
public class AddCertificateActivity extends BaseActivity implements AllViewInter {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title_more)
    TextView tvTitleMore;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.tv_certificate_name)
    TextView tvCertificateName;
    @BindView(R.id.et_certificate_name)
    EditText etCertificateName;
    @BindView(R.id.et_service_type)
    EditText etServiceType;
    @BindView(R.id.ll_certificate_type)
    LinearLayout llCertificateType;
    @BindView(R.id.tv_certificate_type)
    TextView tvCertificateType;

    @BindView(R.id.iv_delete_certificate)
    ImageView ivDeleteCertificate;
    @BindView(R.id.btn_edit_certificate)
    Button btnEditCertificate;
    @BindView(R.id.iv_add_certificate)
    ImageView ivAddCertificate;
    @BindView(R.id.rl_add_picture)
    RelativeLayout rlAddPicture;

    private int certificateType;
    private PersonalPrenInter mPersonalPrenInter;
    private BasePrenImpl mBasePrenInter;

    private boolean isName;
    private boolean isType;
    private String imgUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_certificate);
        ButterKnife.bind(this);

        ViewUnits.getInstance().setTitleHeight(viewStatusBar);
        llTitle.setBackgroundResource(R.color.blue_0d8);
        tvTitleMore.setText("查看证书");
        tvTitleMore.setVisibility(View.VISIBLE);


        mPersonalPrenInter = new PersonalPrenImpl(this);
        mBasePrenInter = new BasePrenImpl(this);

        getData();
    }

    private void getData() {
        certificateType = getIntent().getIntExtra("certificate_type", 0);

        if (certificateType == 1) {
            llCertificateType.setVisibility(View.GONE);
            tvTitleName.setText("专业证书");
            tvCertificateName.setText("专业名称");
            etCertificateName.setHint("请输入专业名称");
            tvCertificateType.setText("专业证书");
        } else {
            llCertificateType.setVisibility(View.VISIBLE);
            tvTitleName.setText("服务证书");
            tvCertificateName.setText("服务名称");
            etCertificateName.setHint("请输入服务名称");
            tvCertificateType.setText("服务证书");
            etServiceType.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    isType = !TextUtils.isEmpty(s.toString().trim());
                    isEditFinish();
                }
            });
        }

        isEditFinish();


        etCertificateName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isName = !TextUtils.isEmpty(s.toString().trim());
                isEditFinish();
            }
        });
    }

    private void isEditFinish() {
        btnEditCertificate.setEnabled(isName && !TextUtils.isEmpty(imgUrl) && certificateType == 1 || isType);
    }


    @OnClick({R.id.iv_title_back, R.id.tv_title_more, R.id.rl_add_picture, R.id.iv_delete_certificate, R.id.btn_edit_certificate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_more:
                Intent intent = new Intent(AddCertificateActivity.this, CertificateListActivity.class);
                intent.putExtra("certificate_type", certificateType);
                startActivity(intent);
                break;
            case R.id.rl_add_picture:
                takePhoto();
                break;
            case R.id.iv_delete_certificate:
                ivAddCertificate.setImageResource(0);
                imgUrl = null;
                ivDeleteCertificate.setVisibility(View.GONE);
                isEditFinish();
                break;
            case R.id.btn_edit_certificate:
                ViewUnits.getInstance().showLoading(AddCertificateActivity.this, "保存中");

                mBasePrenInter.uploadImg(NetCode.Base.uploadImg, Base64Utils.encodeDefault(ImageUtils.getInstance().image2byte(imgUrl)));

                break;
        }
    }

    /**
     * 拍照或选择照片
     */
    private void takePhoto() {
        if (PermissionUtils.getInstance().hasPermission(this, App.pictureSelect)) {
            PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofImage())
                    .selectionMode(PictureConfig.SINGLE)
                    .isCamera(true)
                    .enableCrop(true)// 是否裁剪 true or false
                    .compress(true)// 是否压缩 true or false
                    .previewImage(false)
                    .setOutputCameraPath(tendency.hz.zhihuijiayuan.bean.base.Uri.camera)
                    .withAspectRatio(5, 2)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                    .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                    .showCropGrid(false)
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                    .isDragFrame(true)// 是否可拖动裁剪框(固定)
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        } else {
            ActivityCompat.requestPermissions(this, App.pictureSelect, Request.Permissions.REQUEST_CAMERA);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
            //图片压缩成功
            imgUrl = PictureSelector.obtainMultipleResult(data).get(0).getCompressPath();
            if (!TextUtils.isEmpty(imgUrl)) {
                ivAddCertificate.setImageURI(Uri.parse(imgUrl));
                ivDeleteCertificate.setVisibility(View.VISIBLE);
                isEditFinish();
            }
        }
    }


    @Override
    public void onSuccessed(int what, Object object) {

        if (what == NetCode.Personal.addMajor || what == NetCode.Personal.addService) {
            ViewUnits.getInstance().missLoading();
            ViewUnits.getInstance().showToast("修改成功");
            finish();
        } else if (what == NetCode.Base.uploadImg) {
            if (certificateType == 1) {
                mPersonalPrenInter.addMajor(NetCode.Personal.addMajor,
                        etCertificateName.getText().toString().trim(),
                        (String) object,
                        DateUtils.getDate(Field.DateType.year_month_day, System.currentTimeMillis()));
            } else {
                mPersonalPrenInter.addService(NetCode.Personal.addService,
                        etCertificateName.getText().toString().trim(),
                        etServiceType.getText().toString().trim(),
                        (String) object,
                        DateUtils.getDate(Field.DateType.year_month_day, System.currentTimeMillis()));
            }
        }
    }

    @Override
    public void onFailed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        ViewUnits.getInstance().showToast(object.toString());

    }

    @Override
    protected void onDestroy() {
        PictureFileUtils.deleteCacheDirFile(this);
        super.onDestroy();
    }
}
