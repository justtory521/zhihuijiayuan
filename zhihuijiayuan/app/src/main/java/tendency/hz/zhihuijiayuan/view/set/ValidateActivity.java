package tendency.hz.zhihuijiayuan.view.set;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.tools.PictureFileUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import exocr.exocrengine.CaptureActivity;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.IDCardBean;
import tendency.hz.zhihuijiayuan.bean.base.App;
import tendency.hz.zhihuijiayuan.bean.base.Field;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.bean.base.What;
import tendency.hz.zhihuijiayuan.inter.ValidateListener;
import tendency.hz.zhihuijiayuan.presenter.SetPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.SetPrenInter;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.DateUtils;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.PermissionUtils;
import tendency.hz.zhihuijiayuan.units.SPUtils;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.card.CardContentActivity;
import tendency.hz.zhihuijiayuan.view.user.AgreementActivity;
import tendency.hz.zhihuijiayuan.view.user.LoginActivity;
import tendency.hz.zhihuijiayuan.view.user.PrivacyStatementActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

import static tendency.hz.zhihuijiayuan.bean.base.Request.Permissions.REQUEST_CAMERA;

/**
 * Created by JasonYao on 2018/4/2.
 */

public class ValidateActivity extends BaseActivity implements AllViewInter {
    private static final String TAG = "ValidateActivity--";
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.btn_add_face)
    ImageView btnAddFace;
    @BindView(R.id.btn_add_back)
    ImageView btnAddBack;
    @BindView(R.id.cb_validate)
    CheckBox cbValidate;
    @BindView(R.id.tv_validate_agreement)
    TextView tvValidateAgreement;
    @BindView(R.id.tv_validate_privacy)
    TextView tvValidatePrivacy;
    @BindView(R.id.btn_send)
    CardView btnSend;


    private String mPhotoFace, mPhotoBack; //正面照片、背面照片
    private String mStrName = "", mStrCardID = ""; //中文姓名、身份证号

    private SetPrenInter mSetPrenInter;

    private int type;

    public static ValidateListener mValidateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 0);
        mSetPrenInter = new SetPrenImpl(this);
        ViewUnits.getInstance().setTitleHeight(viewStatusBar);
        tvTitleName.setText("实名认证");
        llTitle.setBackgroundResource(R.color.blue_0d8);

    }


    public static void setValidateListener(ValidateListener validateListener) {
        mValidateListener = validateListener;
    }


    /**
     * 上传审核内容
     */
    private void update() {


        if (FormatUtils.getInstance().isEmpty(mPhotoFace)) {
            ViewUnits.getInstance().showToast("身份证正面照片不可为空");
            return;
        }

        if (FormatUtils.getInstance().isEmpty(mPhotoBack)) {
            ViewUnits.getInstance().showToast("身份证背面照片不可为空");
            return;
        }

        if (!cbValidate.isChecked()){
            ViewUnits.getInstance().showToast("请勾选我已阅读并同意《用户协议》和《隐私权政策》");
            return;
        }

        ViewUnits.getInstance().showLoading(this, "请求中");
        mSetPrenInter.validate(NetCode.Set.validate, mStrCardID, mStrName,
                DateUtils.getDate(Field.DateType.year_month_day, System.currentTimeMillis()), null);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Request.StartActivityRspCode.SCAN_ID_CARD) {

            IDCardBean idCardBean = (IDCardBean) data.getSerializableExtra("id_card");
            if (idCardBean != null) {
                if (idCardBean.getOrientation() == 1) {
                    mStrName = idCardBean.getName();
                    mStrCardID = idCardBean.getNumber();
                    mPhotoFace = idCardBean.getPath();
                    btnAddFace.setImageURI(Uri.fromFile(new File(idCardBean.getPath())));

//                    value.put("gender", idCardBean.getGender());
//                    value.put("address", idCardBean.getAddress());
//                    value.put("IDNum", idCardBean.getNumber());
//                    value.put("nation", idCardBean.getNation());
                } else {
                    mPhotoBack = idCardBean.getPath();
                    btnAddBack.setImageURI(Uri.fromFile(new File(idCardBean.getPath())));
//                    value.put("issue", idCardBean.getPolice());
//                    value.put("valid", idCardBean.getDate());
                }

            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onSuccessed(int what, Object object) {
        if (what == NetCode.Set.validate) {
            ViewUnits.getInstance().showToast("提交成功");
            ViewUnits.getInstance().missLoading();
            SPUtils.getInstance().put(SPUtils.FILE_USER, SPUtils.realName, mStrName);
            SPUtils.getInstance().put(SPUtils.FILE_USER, SPUtils.cardId, mStrCardID);
            finish();
            if (type == 1) {
                mValidateListener.validate("200", "success", mStrCardID);
            }

        }

    }

    @Override
    public void onFailed(int what, Object object) {
        ViewUnits.getInstance().showToast(object.toString());
        ViewUnits.getInstance().missLoading();

        if (type == 1) {
            mValidateListener.validate("500", "fail", object.toString());
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        PictureFileUtils.deleteCacheDirFile(this);
        super.onDestroy();
    }

    @OnClick({R.id.iv_title_back, R.id.btn_add_face, R.id.btn_add_back, R.id.tv_validate_agreement, R.id.tv_validate_privacy, R.id.btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.btn_add_face:
                if (BaseUnits.getInstance().checkPermission(this, Manifest.permission.CAMERA)) {
                    Intent intent = new Intent(this, CaptureActivity.class);
                    intent.putExtra("is_front", true);
                    startActivityForResult(intent, Request.StartActivityRspCode.SCAN_ID_CARD);
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                }
                break;
            case R.id.btn_add_back:
                if (BaseUnits.getInstance().checkPermission(this, Manifest.permission.CAMERA)) {
                    Intent intent = new Intent(this, CaptureActivity.class);
                    intent.putExtra("is_front", false);
                    startActivityForResult(intent, Request.StartActivityRspCode.SCAN_ID_CARD);
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                }
                break;
            case R.id.tv_validate_agreement:
                startActivity(new Intent(ValidateActivity.this, AgreementActivity.class));
                break;
            case R.id.tv_validate_privacy:
                startActivity(new Intent(ValidateActivity.this, PrivacyStatementActivity.class));
                break;
            case R.id.btn_send:
                update();
                break;
        }
    }
}
