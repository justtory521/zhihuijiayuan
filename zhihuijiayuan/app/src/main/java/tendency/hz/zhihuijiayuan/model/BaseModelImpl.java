package tendency.hz.zhihuijiayuan.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tendency.hz.zhihuijiayuan.application.MyApplication;
import tendency.hz.zhihuijiayuan.bean.Area;
import tendency.hz.zhihuijiayuan.bean.base.Field;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Uri;
import tendency.hz.zhihuijiayuan.model.modelInter.AllModelInter;
import tendency.hz.zhihuijiayuan.model.modelInter.BaseModelInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.AllPrenInter;
import tendency.hz.zhihuijiayuan.units.AddressDbHelper;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.ConfigUnits;
import tendency.hz.zhihuijiayuan.units.MacUtils;
import tendency.hz.zhihuijiayuan.units.NoHttpUtil;

/**
 * Created by JasonYao on 2018/3/19.
 */

public class BaseModelImpl extends AllModelInter implements BaseModelInter {
    private static final String TAG = "BaseModelImpl---";

    private AllPrenInter mAllPrenInter;
    private Gson mGson = new Gson();

    public BaseModelImpl(AllPrenInter allPrenInter) {
        mAllPrenInter = allPrenInter;
    }

    @Override
    public void getAllDistricts(int netCode) {
        if (netCode != NetCode.Base.getAllDistricts) {
            return;
        }

        NoHttpUtil.get(netCode, Uri.Base.GETALLDISTRICT, onResponseListener, null);
    }

    @Override
    public void getDataDictionary(int netCode) {
        if (netCode != NetCode.Base.getDataDictionary) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("DicType", ""));
        params.add(new NoHttpUtil.Param("DicName", ""));
        params.add(new NoHttpUtil.Param("Status", ""));

        NoHttpUtil.post(netCode, Uri.Base.GETDATADICTIONARY, onResponseListener, params);
    }

    @Override
    public void uploadImg(int netCode, String img) {
        if (netCode != NetCode.Base.uploadImg) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("ImageStr", img));
        NoHttpUtil.post(netCode, Uri.Base.UPLOADIMG, onResponseListener, params);
    }

    @Override
    public void addAppDeviceInfo(int netCode) {
        if (netCode != NetCode.Base.appDeivceInfo) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("ClientId", BaseUnits.getInstance().getPhoneKey()));
        params.add(new NoHttpUtil.Param("AppType", "1"));
        params.add(new NoHttpUtil.Param("OSVersion", BaseUnits.getInstance().getOSVersion()));
        params.add(new NoHttpUtil.Param("MAC", MacUtils.getMobileMAC(MyApplication.getInstance())));
        params.add(new NoHttpUtil.Param("PhoneModel", BaseUnits.getInstance().getPhoneModel()));
        params.add(new NoHttpUtil.Param("Message", MacUtils.MESSAGE.toString()));
        params.add(new NoHttpUtil.Param("IMEI", BaseUnits.getInstance().getIMEI()));
        params.add(new NoHttpUtil.Param("IMSI", BaseUnits.getInstance().getIMSI()));

        NoHttpUtil.post(netCode, Uri.Base.ADDDEVICEINFO, onResponseListener, params);
    }

    @Override
    public void rspSuccess(int what, Object object) throws JSONException {

        switch (what) {
            case NetCode.Base.getAllDistricts:
                JSONArray jsonArrayArea = ((JSONObject) object).getJSONArray("Data");
                List<Area> listArea = mGson.fromJson(jsonArrayArea.toString(), new TypeToken<List<Area>>() {
                }.getType());

                AddressDbHelper.getInstance().insert("delete from " + Field.Area.dbName, new String[]{});

                //插入数据
                for (Area area : listArea) {
                    /*String sqlIns = "insert into " + Field.Area.dbName + " values(null,?,?,?,?)";
                    String[] arr = {
                            area.getID(), area.getParentId(), area.getName(), area.getShortName()
                    };

                    BaseDbHelper.getInstance().insert(sqlIns, arr);*/

                    if (area.getNext() != null && area.getNext().size() != 0) {
                        Log.e(TAG, "------------->" + area.getName());

                        for (Area area1 : area.getNext()) {

                            String sqlIns1 = "insert into " + Field.Area.dbName + " values(null,?,?,?,?)";
                            String[] arr1 = {
                                    area1.getID(), area1.getParentId(), area1.getName(), area1.getShortName()
                            };

                            AddressDbHelper.getInstance().insert(sqlIns1, arr1);

                            /*if (area1.getNext() != null && area1.getNext().size() != 0) {
                                for (Area area2 : area1.getNext()) {
                                    Log.e(TAG, "----->" + area2.getName());
                                    String sqlIns2 = "insert into " + Field.Area.dbName + " values(null,?,?,?,?)";
                                    String[] arr2 = {
                                            area2.getID(), area2.getParentId(), area2.getName(), area2.getShortName()
                                    };

                                    BaseDbHelper.getInstance().insert(sqlIns2, arr2);
                                }
                            }*/
                        }
                    }
                }


                AddressDbHelper.getInstance().insert("delete from " + Field.Address.dbName, new String[]{});

                //插入数据
                for (Area area : listArea) {
                    String sqlIns = "insert into " + Field.Address.dbName + " values(?,?,?,?)";
                    String[] arr = {
                            area.getID(), area.getParentId(), area.getID(), area.getName()
                    };

                    AddressDbHelper.getInstance().insert(sqlIns, arr);

                    if (area.getNext() != null && area.getNext().size() != 0) {
                        Log.e(TAG, "------------->" + area.getName());
                        for (Area area1 : area.getNext()) {
                            String sqlIns1 = "insert into " + Field.Address.dbName + " values(?,?,?,?)";
                            String[] arr1 = {
                                    area1.getID(), area1.getParentId(), area1.getID(), area1.getName()
                            };

                            AddressDbHelper.getInstance().insert(sqlIns1, arr1);

                            if (area1.getNext() != null && area1.getNext().size() != 0) {
                                for (Area area2 : area1.getNext()) {
                                    Log.e(TAG, "----->" + area2.getName());
                                    String sqlIns2 = "insert into " + Field.Address.dbName + " values(?,?,?,?)";
                                    String[] arr2 = {
                                            area2.getID(), area2.getParentId(), area2.getID(), area2.getName()
                                    };

                                    AddressDbHelper.getInstance().insert(sqlIns2, arr2);
                                }
                            }
                        }
                    }
                }

                mAllPrenInter.onSuccess(what, null);
                Log.e(TAG, listArea.toString());
                break;

            case NetCode.Base.getDataDictionary:

                break;
            case NetCode.Base.uploadImg:
                JSONArray jsonArray = ((JSONObject) object).getJSONArray("Data");
                mAllPrenInter.onSuccess(what, jsonArray.get(0).toString());
                break;
            case NetCode.Base.appDeivceInfo:
                Log.e(TAG, "修改成功");
                ConfigUnits.getInstance().setSVersionCode(BaseUnits.getInstance().getVerCode(MyApplication.getInstance()));
                break;
        }
    }

    @Override
    public void rspFailed(int what, Object object) {
        mAllPrenInter.onFail(what, object);
    }
}
