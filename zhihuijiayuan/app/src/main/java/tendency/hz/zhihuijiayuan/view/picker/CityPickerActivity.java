package tendency.hz.zhihuijiayuan.view.picker;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.adapter.CityListAdapter;
import tendency.hz.zhihuijiayuan.adapter.ResultListAdapter;
import tendency.hz.zhihuijiayuan.bean.City;
import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.databinding.CpActivityCityListBinding;
import tendency.hz.zhihuijiayuan.databinding.LayoutVoicePopupBinding;
import tendency.hz.zhihuijiayuan.inter.CityPickerResultListener;
import tendency.hz.zhihuijiayuan.units.ConfigUnits;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.SpeechUnits;
import tendency.hz.zhihuijiayuan.units.UserUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/6/6.
 */
public class CityPickerActivity extends BaseActivity implements AllViewInter, View.OnClickListener {
    public static final String TAG = "CityPicker---";

    private CityListAdapter mCityAdapter;
    private List<City> mAllCities = new ArrayList<>(); //所有城市数据
    private List<City> mSelectCities = new ArrayList<>(); //选择的城市数据

    private City mSelectCity;

    private ResultListAdapter mResultAdapter;

    private CpActivityCityListBinding mBinding;

    private InputMethodManager mImm;
    private View mView;
    private Handler mHandler;
    private Runnable mRunable;

    private PopupWindow mPopupWindow;  //语音弹出框
    private View mPopBackView;  //语音弹出框View
    private WindowManager.LayoutParams mLayoutParams;
    private LayoutVoicePopupBinding mBindingPop;

    public static CityPickerResultListener mListener;
    private static String mCallBack;
    private LinearLayoutManager mManager;
    private City mNowSelectedCity;  //当前城市

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.cp_activity_city_list);
        ViewUnits.getInstance().setTitleHeight(mBinding.layoutTitle);

        mView = getWindow().getDecorView();

        initData();

        initView();

        setListenter();

        initHandler();
    }

    private void initData() {
        mNowSelectedCity = (City) super.getIntent().getSerializableExtra("city");
        mAllCities = new ArrayList<>();
        mAllCities.clear();
        mAllCities.addAll(ConfigUnits.getInstance().getAllCities());
        mAllCities.add(0, new City("当前"));
        mAllCities.add(1, new City("定位"));
        mAllCities.add(2, new City("热门"));
        mCityAdapter = new CityListAdapter(this, mAllCities, mNowSelectedCity);
        mResultAdapter = new ResultListAdapter(this, null);
        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void initView() {
        mManager = new LinearLayoutManager(this);
        mBinding.listviewAllSelector.setLayoutManager(mManager);
        mBinding.listviewAllSelector.setAdapter(mCityAdapter);
        mBinding.listviewSearchResult.setAdapter(mResultAdapter);

        mCityAdapter.updateLocateState(Request.LocateState.SUCCESS, UserUnits.getInstance().getLocation());

        mPopupWindow = new PopupWindow();
        mLayoutParams = getWindow().getAttributes();
        mPopBackView = LayoutInflater.from(this).inflate(R.layout.layout_voice_popup, null);
        //设置Popup具体参数
        mPopupWindow.setContentView(mPopBackView);
        mPopupWindow.setWidth(LinearLayoutCompat.LayoutParams.MATCH_PARENT);//设置宽
        mPopupWindow.setHeight(LinearLayoutCompat.LayoutParams.WRAP_CONTENT);//设置高
        mPopupWindow.setFocusable(true);//点击空白，popup不自动消失
        mPopupWindow.setOutsideTouchable(true);//非popup区域可触摸
        mPopupWindow.setAnimationStyle(R.style.ActionSheetDialogAnimation);
        mBindingPop = DataBindingUtil.bind(mPopBackView);
        mBinding.sideLetterBar.setOverlay(mBinding.textOverly);
    }

    private void initHandler() {
        mHandler = new Handler();
        mRunable = () -> {
            mLayoutParams.alpha = 0.7f;
            getWindow().setAttributes(mLayoutParams);
            if (!mPopupWindow.isShowing()) {
                mPopupWindow.showAtLocation(getWindow().peekDecorView(), Gravity.BOTTOM, 0, 0);
            } else {
                ///如果正在显示等待框，，，则结束掉等待框然后重新显示
                mPopupWindow.dismiss();
                mPopupWindow.showAtLocation(getWindow().peekDecorView(), Gravity.BOTTOM, 0, 0);
            }
        };
    }

    private void setListenter() {
        mBinding.sideLetterBar.setOnLetterChangedListener(letter -> {
            int position = mCityAdapter.getLetterPosition(letter);
            mBinding.listviewAllSelector.scrollToPosition(position);
        });

        mBinding.listviewAllSelector.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mBinding.sideLetterBar.setSelectCityIndex(mAllCities.get(mManager.findFirstCompletelyVisibleItemPosition()));
            }
        });

        mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {

            @Override
            public void onCityClick(int position, View view) {
                mSelectCity = mAllCities.get(position);
                back();
            }

            @Override
            public void onLocateClick() {
                mCityAdapter.updateLocateState(Request.LocateState.LOCATING, null);
            }

            @Override
            public void onLocateResultClick(String cityName) {
                mSelectCity = ConfigUnits.getInstance().getCityByCityName(cityName);
                back();
            }

            @Override
            public void onHotCityClick(String cityName) {
                mSelectCity = ConfigUnits.getInstance().getCityByCityName(cityName);
                back();
            }
        });

        mBinding.includeSearch.ivSearchClear.setOnClickListener(this);

        mBinding.includeSearch.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if (TextUtils.isEmpty(keyword)) {
                    mBinding.includeSearch.ivSearchClear.setVisibility(View.GONE);
                    mBinding.emptyView.setVisibility(View.GONE);
                    mBinding.listviewSearchResult.setVisibility(View.GONE);
                } else {
                    mBinding.includeSearch.ivSearchClear.setVisibility(View.VISIBLE);
                    mBinding.listviewSearchResult.setVisibility(View.VISIBLE);
                    List<City> result = ConfigUnits.getInstance().getSreachCity(keyword);
                    mSelectCities.clear();
                    if (result != null) {
                        mSelectCities.addAll(result);
                    }
                    if (result == null || result.size() == 0) {
                        mBinding.emptyView.setVisibility(View.VISIBLE);
                    } else {
                        mBinding.emptyView.setVisibility(View.GONE);
                        mResultAdapter.changeData(result);
                    }
                }
            }
        });

        mResultAdapter.setListener((view, postion) -> {
            mSelectCity = mSelectCities.get(postion);
            back();
        });

        mBinding.includeTitle.imgBtnBackComm.setOnClickListener(view -> finish());

        mPopupWindow.setOnDismissListener(() -> {
            mLayoutParams.alpha = 1.0f;
            getWindow().setAttributes(mLayoutParams);
        });

        mBindingPop.btnClear.setOnClickListener(view -> mBinding.includeSearch.etSearch.setText(""));
        mBindingPop.btnCancel.setOnClickListener(view -> mPopupWindow.dismiss());

        mBindingPop.btnVoice.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_UP:
                    mBindingPop.textTitle.setText("按住说话");
                    mBindingPop.textTitle.setTextColor(CityPickerActivity.this.getResources().getColor(R.color.colorTextGray));
                    SpeechUnits.getInstance().stopListening();
                    break;
                case MotionEvent.ACTION_DOWN:
                    mBindingPop.textTitle.setText("请开始说话...");
                    mBindingPop.textTitle.setTextColor(CityPickerActivity.this.getResources().getColor(R.color.colorPrimary));
                    SpeechUnits.getInstance().startListening(result -> {
                        mBinding.includeSearch.etSearch.setText(result);
                        mBinding.includeSearch.etSearch.setSelection(mBinding.includeSearch.etSearch.length());
                        if (!FormatUtils.getInstance().isEmpty(result)) {
                            mPopupWindow.dismiss();
                        }
                    });
                    break;
            }
            return true;
        });
    }

    private void back() {
        if (mSelectCity == null) {
            return;
        }

        if (mListener != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("cityCode", mSelectCity.getID());
                jsonObject.put("cityName", mSelectCity.getName());
                mListener.getCityPickerResultListener(mCallBack, jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        UserUnits.getInstance().setSelectCity(mSelectCity.getName());
        Intent intent = new Intent();
        intent.putExtra("selectCity", mSelectCity);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 隐藏键盘
     */
    private void hideInput() {
        mImm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
    }

    public void showPopup(View view) {
        hideInput();
        mHandler.postDelayed(mRunable, 200);
    }

    public static void setCityPickerResultListener(String callBack, CityPickerResultListener listener) {
        mCallBack = callBack;
        mListener = listener;
    }

    @Override
    public void onSuccessed(int what, Object object) {
    }

    @Override
    public void onFailed(int what, Object object) {
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_search_clear) {
            mBinding.includeSearch.etSearch.setText("");
            mBinding.includeSearch.ivSearchClear.setVisibility(View.GONE);
            mBinding.emptyView.setVisibility(View.GONE);
            mBinding.listviewSearchResult.setVisibility(View.GONE);
        } else if (i == R.id.back) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCityAdapter = null;
        mAllCities = null;
        mSelectCities = null;
        mSelectCity = null;
        mResultAdapter = null;
        mImm = null;
        mView = null;
        mHandler = null;
        mRunable = null;
        mPopupWindow = null;
        mPopBackView = null;
        mLayoutParams = null;
        mBindingPop = null;
        mListener = null;
        mCallBack = null;
        mManager = null;
        mNowSelectedCity = null;
    }
}
