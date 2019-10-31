package tendency.hz.zhihuijiayuan.presenter;

import android.util.Log;

import tendency.hz.zhihuijiayuan.application.MyApplication;
import tendency.hz.zhihuijiayuan.model.BaseModelImpl;
import tendency.hz.zhihuijiayuan.model.modelInter.BaseModelInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.AllPrenInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.BasePrenInter;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.ConfigUnits;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/3/19.
 */

public class BasePrenImpl implements AllPrenInter, BasePrenInter {
    private static final String TAG = "BasePrenImpl---";
    private AllViewInter mAllViewInter;
    private BaseModelInter mBaseModelInter;

    public BasePrenImpl(AllViewInter allViewInter) {
        mAllViewInter = allViewInter;
        mBaseModelInter = new BaseModelImpl(this);
    }

    @Override
    public void getAllDistricts(int netCode) {
        mBaseModelInter.getAllDistricts(netCode);
    }

    @Override
    public void getDataDictionary(int netCode) {
        mBaseModelInter.getDataDictionary(netCode);
    }

    @Override
    public void uploadImg(int netCode, String img) {
        mBaseModelInter.uploadImg(netCode, img);
    }

    @Override
    public void addAppDeviceInfo(int netCode) {
        Log.e(TAG, "真实版本号：" + BaseUnits.getInstance().getVerCode(MyApplication.getInstance()));
        Log.e(TAG, "缓存版本号：" + ConfigUnits.getInstance().getSVersionCode());
        if (!ConfigUnits.getInstance().getSVersionCode().equals(BaseUnits.getInstance().getVerCode(MyApplication.getInstance()))) {
            mBaseModelInter.addAppDeviceInfo(netCode);
        }
    }

    @Override
    public void onSuccess(int what, Object object) {
        mAllViewInter.onSuccessed(what, object);
    }

    @Override
    public void onFail(int what, Object object) {
        mAllViewInter.onFailed(what, object);
    }
}
