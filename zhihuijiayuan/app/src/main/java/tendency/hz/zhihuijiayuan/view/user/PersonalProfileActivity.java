package tendency.hz.zhihuijiayuan.view.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.smarttop.library.bean.City;
import com.smarttop.library.bean.County;
import com.smarttop.library.bean.Province;
import com.smarttop.library.bean.Street;
import com.smarttop.library.widget.BottomDialog;
import com.smarttop.library.widget.OnAddressSelectedListener;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.User;
import tendency.hz.zhihuijiayuan.bean.base.App;
import tendency.hz.zhihuijiayuan.bean.base.Config;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.bean.base.Uri;
import tendency.hz.zhihuijiayuan.databinding.ActivityPersonalProfileBinding;
import tendency.hz.zhihuijiayuan.inter.TakeSexOnClick;
import tendency.hz.zhihuijiayuan.presenter.BasePrenImpl;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.BasePrenInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PersonalPrenInter;
import tendency.hz.zhihuijiayuan.units.AddressDbHelper;
import tendency.hz.zhihuijiayuan.units.Base64Utils;
import tendency.hz.zhihuijiayuan.units.ConfigUnits;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.ImageUtils;
import tendency.hz.zhihuijiayuan.units.PermissionUtils;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/4/15.
 */

public class PersonalProfileActivity extends BaseActivity implements AllViewInter, OnAddressSelectedListener {
    private static final String TAG = "Personal---";
    private ActivityPersonalProfileBinding mBinding;
    private PersonalPrenInter mPersonalPrenInter;
    private BasePrenImpl mBasePrenInter;
    private User mUser;

    private BottomDialog mAddressPicker = null; //地址选择器
    private TimePickerView mTimePickerView = null; //日期选择器
    private String mHeadUrl = "", mNickName = "", mSex = "", mBirthDay = "", mAddress = "";  //标记所修改的头像、昵称、性别、生日、所在地

    //选择性别底部弹出框
    RadioGroup mRgSex;
    RadioButton mRbMan, mRbWomen;
    Dialog mDialogSex;
    View viewSex;
    TextView mTextViewSure;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_personal_profile);
        ViewUnits.getInstance().setTitleHeight(mBinding.layoutTitle);

        mPersonalPrenInter = new PersonalPrenImpl(this);
        mBasePrenInter = new BasePrenImpl(this);

        initView();

        mPersonalPrenInter.getPersonalInfo(NetCode.Personal.getPersonalInfo);
    }


    private void initView() {

        //初始化地址选择器
        mAddressPicker = new BottomDialog(this);
        mAddressPicker.setIndicatorBackgroundColor(R.color.colorPrimary);
        mAddressPicker.setTextSelectedColor(R.color.colorPrimary);
        mAddressPicker.setTextUnSelectedColor(R.color.colorTextBlack);
        mAddressPicker.setOnAddressSelectedListener(this);

        Calendar startTime = Calendar.getInstance();
        startTime.set(1900, 0, 1);


        Calendar defaultTime = Calendar.getInstance();
        defaultTime.set(1970, 0, 1);


        Calendar endTime = Calendar.getInstance();
        endTime.set(endTime.get(Calendar.YEAR), endTime.get(Calendar.MONTH), endTime.get(Calendar.DATE));

        //初始化日期选择器
        mTimePickerView = new TimePickerView.Builder(this, (date, v) -> {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            mBirthDay = format.format(date);
            mBinding.textBirthday.setText(mBirthDay);
        })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)//默认全部显示
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


        /**
         * 性别选择框
         */
        mDialogSex = new Dialog(this, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        viewSex = LayoutInflater.from(this).inflate(R.layout.layout_buttom_sex_popup, null);
        //初始化控件
        mRbMan = viewSex.findViewById(R.id.rb_man);
        mRbWomen = viewSex.findViewById(R.id.rb_weman);
        mTextViewSure = viewSex.findViewById(R.id.btn_sure);
        mRgSex = viewSex.findViewById(R.id.rg);
        //将布局设置给Dialog
        mDialogSex.setContentView(viewSex);
        //获取当前Activity所在的窗体
        Window dialogWindowSex = mDialogSex.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindowSex.setGravity(Gravity.BOTTOM);
        dialogWindowSex.getDecorView().setPadding(0, 0, 0, 0);
        //获得窗体的属性
        WindowManager.LayoutParams lpSex = dialogWindowSex.getAttributes();
        lpSex.width = WindowManager.LayoutParams.MATCH_PARENT;
        lpSex.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将属性设置给窗体
        dialogWindowSex.setAttributes(lpSex);
    }

    private void updateView() {
        Log.e(TAG, "用户信息为：" + mUser.toString());
        if (!FormatUtils.getInstance().isEmpty(mUser.getNickName())) {
            mBinding.textName.setText(mUser.getNickName());
            mBinding.textName.setTextColor(this.getResources().getColor(R.color.colorTextGray));
        }

        if (!FormatUtils.getInstance().isEmpty(mUser.getBirthday())) {
            mBinding.textBirthday.setText(mUser.getBirthday());
            mBinding.textBirthday.setTextColor(this.getResources().getColor(R.color.colorTextGray));
        }

        if (!FormatUtils.getInstance().isEmpty(mUser.getSex())) {
            if (mUser.getSex().equals("1") || mUser.getSex().equals("2")) {
                mBinding.textSex.setTextColor(this.getResources().getColor(R.color.colorTextGray));
            }
        }

        if (!FormatUtils.getInstance().isEmpty(mUser.getHeadImgPath())) {
            mBinding.headImage.setImageURI(mUser.getHeadImgPath());
        }

        if (!FormatUtils.getInstance().isEmpty(mUser.getAddress())) {
            mBinding.textLocal.setText(mUser.getAddress());
            mBinding.textLocal.setTextColor(this.getResources().getColor(R.color.colorTextGray));
        }

        mBinding.textSex.setText(ConfigUnits.getInstance().getSexById(mUser.getSex()));
    }


    /**
     * 显示性别选择框
     */
    public void showSexDialog(TakeSexOnClick takeSexOnClick, String selectedSex) {
        mRbMan.setChecked(true);  //默认选中男
        if (!FormatUtils.getInstance().isEmpty(selectedSex) && selectedSex.equals("2")) {
            mRbWomen.setChecked(true);
        }

        mTextViewSure.setOnClickListener(view -> {
            switch (mRgSex.getCheckedRadioButtonId()) {
                case R.id.rb_man:
                    takeSexOnClick.getSelectedSex("1");
                    break;
                case R.id.rb_weman:
                    takeSexOnClick.getSelectedSex("2");
                    break;
            }
        });
        runOnUiThread(() -> mDialogSex.show());
    }

    /**
     * 隐藏性别选择框
     */
    public void missSexDialog() {
        if (mDialogSex == null) {
            return;
        }

        runOnUiThread(() -> {
            if (mDialogSex.isShowing()) {
                mDialogSex.dismiss();
            }
        });
    }


    /**
     * 打开昵称编辑器
     *
     * @param view
     */
    public void openNameEdit(View view) {
        Intent intent = new Intent(this, ResetNickNameActivity.class);
        startActivityForResult(intent, Request.StartActivityRspCode.RSET_NICKNAME);
    }

    /**
     * 打开照片选择器
     *
     * @param view
     */
    public void chooseImage(View view) {
        if (PermissionUtils.getInstance().hasPermission(this, App.pictureSelect)) {
            PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofImage())
                    .selectionMode(PictureConfig.SINGLE)
                    .previewImage(false)
                    .isCamera(true)
                    .setOutputCameraPath(Uri.camera)
                    .enableCrop(true)// 是否裁剪 true or false
                    .compress(true)// 是否压缩 true or false
                    .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
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

    /**
     * 选择性别
     *
     * @param view
     */
    public void chooseSex(View view) {
        showSexDialog(value -> {
            mSex = value;
            mBinding.textSex.setText(ConfigUnits.getInstance().getSexById(mSex));
            missSexDialog();
        }, mUser.getSex());

    }


    /**
     * 智慧钱包
     *
     * @param view
     */
    public void openWallet(View view) {
        startActivity(new Intent(this, UserWalletActivity.class));
    }


    /**
     * 时间选择器
     *
     * @param view
     */
    public void openDatePicker(View view) {
        mTimePickerView.show();
    }

    /**
     * 地址选择器
     *
     * @param view
     */
    public void openAddressPicker(View view) {
        mAddressPicker.show();
    }

    /**
     * 上传个人信息
     */
    public void updatePersonalInfo(View view) {
        ViewUnits.getInstance().showLoading(this, "修改中");
        mPersonalPrenInter.editPersonInfo(NetCode.Personal.editPersonInfo, mHeadUrl, mNickName, mSex, mBirthDay, mAddress);
    }

    public void downloadData(View view) {
        BasePrenInter mBasePrenInter = new BasePrenImpl(this);
        mBasePrenInter.getAllDistricts(NetCode.Base.getAllDistricts);
    }

    public void copyData(View view) {
        try {
            AddressDbHelper.getInstance().copyDBFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void goBack(View view) {
        goBack();
    }

    private void goBack() {
        if (FormatUtils.getInstance().isEmpty(mHeadUrl) && FormatUtils.getInstance().isEmpty(mNickName) && FormatUtils.getInstance().isEmpty(mSex) &&
                FormatUtils.getInstance().isEmpty(mBirthDay) && FormatUtils.getInstance().isEmpty(mAddress)) {
            finish();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("是否放弃保存个人资料")
                .setIcon(R.mipmap.logo)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    finish();
                })
                .setNegativeButton("取消", (dialogInterface, i) -> {

                })
                .create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Request.StartActivityRspCode.RSET_NICKNAME && resultCode == RESULT_OK) {
            Log.e(TAG, data.getStringExtra("nickName"));
            mNickName = data.getStringExtra("nickName");
            mBinding.textName.setText(mNickName);
        }

        if (resultCode == RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
            ViewUnits.getInstance().showLoading(this, "上传中");
            String img = PictureSelector.obtainMultipleResult(data).get(0).getCompressPath();
            if (!TextUtils.isEmpty(img)) {
                mBasePrenInter.uploadImg(NetCode.Base.uploadImg, Base64Utils.encodeDefault(ImageUtils.
                        getInstance().image2byte(img)));
            }

        }
    }

    @Override
    public void onSuccessed(int what, Object object) {
        ViewUnits.getInstance().missLoading();
        switch (what) {
            case NetCode.Personal.getPersonalInfo:
                mBinding.layoutLoading.setVisibility(View.GONE);
                mUser = (User) object;
                updateView();
                break;
            case NetCode.Base.uploadImg:
                mHeadUrl = (String) object;
                Log.e(TAG, "头像图片:" + Config.UPLOADIMG + mHeadUrl);
                mBinding.headImage.setImageURI(Config.UPLOADIMG + mHeadUrl);
                break;
            case NetCode.Personal.editPersonInfo:
                mPersonalPrenInter.getPersonalInfo(NetCode.Personal.updateRefPersonalInfo);
                break;
            case NetCode.Personal.updateRefPersonalInfo:
                ViewUnits.getInstance().showToast("修改成功");
                if (!TextUtils.isEmpty(mAddress)) {
                    setResult(Request.StartActivityRspCode.CREDIT_TO_PERSONAL);
                }
                finish();
                break;
        }
    }

    @Override
    public void onFailed(int what, Object object) {
        mBinding.layoutLoading.setVisibility(View.GONE);
        ViewUnits.getInstance().missLoading();
        ViewUnits.getInstance().showToast(object.toString());
    }

    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {
        if (province.name.equals("澳门特别行政区")) {
            if (city != null) {
                mAddress = city.code;
                mBinding.textLocal.setText(province.name + " " + city.name);
                mAddressPicker.dismiss();
                return;
            }
        }

        if (county == null) return;
        mAddress = county.code;
        mBinding.textLocal.setText(province.name + " " + city.name + " " + county.name);
        mAddressPicker.dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
            return false;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPersonalPrenInter = null;
        mBasePrenInter = null;
        mUser = null;
        mAddressPicker = null;
        mTimePickerView = null;

        PictureFileUtils.deleteCacheDirFile(this);
    }
}
