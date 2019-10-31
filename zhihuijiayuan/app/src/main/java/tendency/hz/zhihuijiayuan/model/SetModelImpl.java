package tendency.hz.zhihuijiayuan.model;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tendency.hz.zhihuijiayuan.bean.CardItem;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Uri;
import tendency.hz.zhihuijiayuan.model.modelInter.AllModelInter;
import tendency.hz.zhihuijiayuan.model.modelInter.SetModelInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.AllPrenInter;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.ConfigUnits;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.NoHttpUtil;

/**
 * Created by JasonYao on 2018/4/2.
 */

public class SetModelImpl extends AllModelInter implements SetModelInter {
    private static final String TAG = "SetModelImpl---";

    private Gson mGson = new Gson();

    private AllPrenInter mAllPrenInter;

    public SetModelImpl(AllPrenInter allPrenInter) {
        mAllPrenInter = allPrenInter;
    }


    @Override
    public void feedBack(int netCode, String title, String adviceContent, String imgList, String contact) {
        if (netCode != NetCode.Set.feedBack) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("AdviceContent", adviceContent));
        params.add(new NoHttpUtil.Param("ImgList", imgList));
        params.add(new NoHttpUtil.Param("Contact", contact));

        NoHttpUtil.post(netCode, Uri.Set.FEEDBACK, onResponseListener, params);
    }

    @Override
    public void validate(int netCode, String cardId, String realName,String EditIDCardTime, String imgList) {
        if (netCode != NetCode.Set.validate) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("CardID", cardId));
        params.add(new NoHttpUtil.Param("RealName", realName));
        params.add(new NoHttpUtil.Param("EditIDCardTime", EditIDCardTime));
        Log.e(TAG, params.toString());

        NoHttpUtil.post(netCode, Uri.Set.VALIDATE, onResponseListener, params);
    }

    @Override
    public void cardQrCode(int netCode, String code) {
        if (netCode != NetCode.Set.cardQrCode) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("code", code));
        params.add(new NoHttpUtil.Param("clientId", BaseUnits.getInstance().getPhoneKey()));

        NoHttpUtil.get(netCode, Uri.Set.CARDQRCODE, onResponseListener, params);
    }

    @Override
    public void startPage(int netCode) {
        if (netCode != NetCode.Set.startPage) {
            return;
        }

        NoHttpUtil.get(netCode, Uri.Set.STARTPAGE, onResponseListener, null);
    }

    @Override
    public void sendAppTyoe(int netCode) {
        if (netCode != NetCode.Set.sendAppType) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("ClientID", BaseUnits.getInstance().getPhoneKey()));
        params.add(new NoHttpUtil.Param("Type", "1"));
        NoHttpUtil.post(netCode, Uri.Set.SENDADDTYPE, onResponseListener, params);
    }

    @Override
    public void getMessagePrevent(int netCode) {
        if (netCode != NetCode.Set.getMessagePrevent) {
            return;
        }

        NoHttpUtil.get(netCode, Uri.Set.GETMESSAGEPREVENT, onResponseListener, null);
    }

    @Override
    public void editMessagePrevent(int netCode, String isEnablePrevent) {
        if (netCode != NetCode.Set.editMessagePrevent) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("IsEnablePrevent", isEnablePrevent));
        NoHttpUtil.post(netCode, Uri.Set.EDITMESSAGEPREVENT, onResponseListener, params);
    }

    @Override
    public void getVersion(int netCode) {
        if (netCode != NetCode.Set.getVersion) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("appType", "1"));

        NoHttpUtil.get(netCode, Uri.Set.VERSION, onResponseListener, params);
    }

    @Override
    public void WXSubscribeMessage(int netCode, String touser, String template_id, String scene) {
        Log.e(TAG, "WXSubscribeMessage");
        if (netCode != NetCode.Set.wxSubscribeMessage) {
            return;
        }

        Log.e(TAG, "WXSubscribeMessage");
        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("touser", touser));
        params.add(new NoHttpUtil.Param("template_id", template_id));
        params.add(new NoHttpUtil.Param("scene", scene));

        Log.e(TAG, params.toString());
        NoHttpUtil.post(netCode, Uri.Set.WXSUBSCRIBEMESSAGE, onResponseListener, params);
    }

    @Override
    public void rspSuccess(int what, Object object) throws JSONException {
        switch (what) {
            case NetCode.Set.cardQrCode:
                CardItem cardItem = mGson.fromJson(((JSONObject) object).getJSONObject("Data").toString(), CardItem.class);
                mAllPrenInter.onSuccess(what, cardItem);
                break;
            case NetCode.Set.startPage:
                JSONObject jsonObject = ((JSONObject) object).getJSONObject("Data");
                String adImg = jsonObject.getString("Img");
                String adWeb = jsonObject.getString("Web");
                if (FormatUtils.getInstance().isEmpty(adImg)) {
                    ConfigUnits.getInstance().setAdImg("");
                }
                if (!ConfigUnits.getInstance().checkAdImg(adImg, adWeb)) {
                    NoHttpUtil.downLoad(Uri.tdrPath, "index.png", true, true, adImg, adWeb);
                }
                mAllPrenInter.onSuccess(what, object);
                break;
            case NetCode.Set.getMessagePrevent:
                String messagePrevent = ((JSONObject) object).getString("Data");
                mAllPrenInter.onSuccess(what, messagePrevent);
                break;
            case NetCode.Set.editMessagePrevent:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.Set.getVersion:
                JSONObject jsoVersion = ((JSONObject) object).getJSONObject("Data");
                try {
                    String version = jsoVersion.getString("Version");
                    mAllPrenInter.onSuccess(what, version);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case NetCode.Set.wxSubscribeMessage:
                mAllPrenInter.onSuccess(what, null);
                break;
            default:
                mAllPrenInter.onSuccess(what, object);
                break;
        }
    }

    @Override
    public void rspFailed(int what, Object object) {
        mAllPrenInter.onFail(what, object);
    }
}
