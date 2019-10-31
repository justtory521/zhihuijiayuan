package tendency.hz.zhihuijiayuan.model;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tendency.hz.zhihuijiayuan.bean.AccessRecordBean;
import tendency.hz.zhihuijiayuan.bean.CreditRecordBean;
import tendency.hz.zhihuijiayuan.bean.MajorBean;
import tendency.hz.zhihuijiayuan.bean.Message;
import tendency.hz.zhihuijiayuan.bean.ServiceBean;
import tendency.hz.zhihuijiayuan.bean.User;
import tendency.hz.zhihuijiayuan.bean.UserInfoBean;
import tendency.hz.zhihuijiayuan.bean.UserRankBean;
import tendency.hz.zhihuijiayuan.bean.UserRightsBean;
import tendency.hz.zhihuijiayuan.bean.UserServeBean;
import tendency.hz.zhihuijiayuan.bean.UserServiceBean;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Uri;
import tendency.hz.zhihuijiayuan.model.modelInter.AllModelInter;
import tendency.hz.zhihuijiayuan.model.modelInter.PersonalModelInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.AllPrenInter;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.CacheUnits;
import tendency.hz.zhihuijiayuan.units.ConfigUnits;
import tendency.hz.zhihuijiayuan.units.GsonUtils;
import tendency.hz.zhihuijiayuan.units.NoHttpUtil;
import tendency.hz.zhihuijiayuan.units.UserUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;

/**
 * Created by JasonYao on 2018/3/28.
 */

public class PersonalModelImpl extends AllModelInter implements PersonalModelInter {
    private static final String TAG = "PersonalModelImpl---";
    private AllPrenInter mAllPrenInter;

    private Gson mGson = new Gson();

    public PersonalModelImpl(AllPrenInter allPrenInter) {
        mAllPrenInter = allPrenInter;
    }


    @Override
    public void getPersonalInfo(int netCode) {
        if (netCode != NetCode.Personal.getPersonalInfo && netCode != NetCode.Personal.updateRefPersonalInfo) {
            return;
        }

        NoHttpUtil.post(netCode, Uri.Personal.GETPERSONALINFO, onResponseListener, null);
    }

    @Override
    public void updatePersonalInfo(int netCode, String type, String value) {
        if (netCode != NetCode.Personal.updatePersonalInfo) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("ChangeType", type));
        params.add(new NoHttpUtil.Param("ChangeTo", value));

        Log.e(TAG, "发送参数：" + params.toString());
        NoHttpUtil.post(netCode, Uri.Personal.ADDORUPDATEPERSONALINFO, onResponseListener, params);
    }

    @Override
    public void getMessage(int netCode) {
        if (netCode != NetCode.Personal.getMessage && netCode != NetCode.Personal.RefreshMessage) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("ClientID", BaseUnits.getInstance().getPhoneKey()));
        NoHttpUtil.get(netCode, Uri.Personal.GETMESSAGE, onResponseListener, params);
    }

    @Override
    public void editPersonInfo(int netCode, String headImg, String nickName, String sex, String birthday, String address) {
        if (netCode != NetCode.Personal.editPersonInfo) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("HeadImgPath", headImg));
        params.add(new NoHttpUtil.Param("NickName", nickName));
        params.add(new NoHttpUtil.Param("Sex", sex));
        params.add(new NoHttpUtil.Param("Birthday", birthday));
        params.add(new NoHttpUtil.Param("DistrictId", address));

        NoHttpUtil.post(netCode, Uri.Personal.EDITPERSONINFO, onResponseListener, params);
    }

    @Override
    public void changePhoneSendSms(int netCode) {
        if (netCode != NetCode.User2.changePhoneSendSms) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("phone", UserUnits.getInstance().getPhone()));

        NoHttpUtil.get(netCode, Uri.User.CHANGEPHONESENDSMSCODE, onResponseListener, params);
    }

    @Override
    public void changePhoneCheckCode(int netCode, String code) {
        if (netCode != NetCode.User2.changePhoneCheckSms) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("Phone", UserUnits.getInstance().getPhone()));
        params.add(new NoHttpUtil.Param("Code", code));

        NoHttpUtil.post(netCode, Uri.User.CHANGEPHONECHECKCODE, onResponseListener, params);
    }

    @Override
    public void changePhoneSendBindSms(int netCode, String phone) {
        if (netCode != NetCode.User2.changePhoneSendBindSms) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("phone", phone));

        NoHttpUtil.get(netCode, Uri.User.CHANGEPHONESENDBINDSMSCODE, onResponseListener, params);
    }

    @Override
    public void changePhoneBindPhone(int netCode, String oldPhone, String newPhone, String code) {
        if (netCode != NetCode.User2.changePhoneBinding) {
            return;
        }

        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("OldPhone", oldPhone));
        params.add(new NoHttpUtil.Param("NewPhone", newPhone));
        params.add(new NoHttpUtil.Param("Code", code));

        NoHttpUtil.post(netCode, Uri.User.CHANGEPHONEBINDING, onResponseListener, params);
    }

    @Override
    public void getMyServiceList(int netCode) {
        if (netCode != NetCode.Personal.getMyServiceList) {
            return;
        }

        NoHttpUtil.get(netCode, Uri.Personal.GETMYSERVICELIST, onResponseListener, null);
    }

    @Override
    public void creditRankingsan(int netCode) {
        if (netCode != NetCode.Personal.creditRankingsan) {
            return;
        }
        NoHttpUtil.post(netCode, Uri.Personal.CREDITRANKINGSAN, onResponseListener, null);
    }

    @Override
    public void creditRankingwushi(int netCode) {
        if (netCode != NetCode.Personal.creditRankingwushi) {
            return;
        }
        NoHttpUtil.post(netCode, Uri.Personal.CREDITRANKINGWUSHI, onResponseListener, null);
    }

    @Override
    public void getEquityList(int netCode) {
        if (netCode != NetCode.Personal.getEquityList) {
            return;
        }
        NoHttpUtil.get(netCode, Uri.Personal.GETEQUITYLIST, onResponseListener, null);
    }

    @Override
    public void getExecuteList(int netCode) {
        if (netCode != NetCode.Personal.getExecuteList) {
            return;
        }
        NoHttpUtil.get(netCode, Uri.Personal.GETEXECUTELIST, onResponseListener, null);
    }

    @Override
    public void getCreditRecord(int netCode, int state, int PageIndex) {
        if (netCode != NetCode.Personal.getCreditRecord) {
            return;
        }
        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("state", state));
        params.add(new NoHttpUtil.Param("PageIndex", PageIndex));

        NoHttpUtil.get(netCode, Uri.Personal.GETCREDITRECORD, onResponseListener, params);
    }

    @Override
    public void getCreditHistory(int netCode, int PageSize, int CurrentPage) {
        if (netCode != NetCode.Personal.getCreditHistory) {
            return;
        }
        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("PageSize", PageSize));
        params.add(new NoHttpUtil.Param("CurrentPage", CurrentPage));

        NoHttpUtil.get(netCode, Uri.Personal.GETCREDITHISTORY, onResponseListener, params);
    }

    @Override
    public void getMyEquity(int netCode) {
        if (netCode != NetCode.Personal.getMyEquity) {
            return;
        }
        NoHttpUtil.get(netCode, Uri.Personal.GETMYEQUITY, onResponseListener, null);
    }

    @Override
    public void getAllInfo(int netCode) {
        if (netCode != NetCode.Personal.getAllInfo) {
            return;
        }
        NoHttpUtil.get(netCode, Uri.Personal.GETALLINFO, onResponseListener, null);
    }

    @Override
    public void editEducation(int netCode, int EducationDistrictId, String SchoolName, int EducationType, String EducationEndTime, String EditEduTime) {
        if (netCode != NetCode.Personal.editEducation) {
            return;
        }
        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("EducationDistrictId", EducationDistrictId));
        params.add(new NoHttpUtil.Param("SchoolName", SchoolName));
        params.add(new NoHttpUtil.Param("EducationType", EducationType));
        params.add(new NoHttpUtil.Param("EducationEndTime", EducationEndTime));
        params.add(new NoHttpUtil.Param("EditEduTime", EditEduTime));

        NoHttpUtil.post(netCode, Uri.Personal.EDITEDUCATION, onResponseListener, params);
    }

    @Override
    public void sendEmailCode(int netCode, String EmailAccount) {
        if (netCode != NetCode.Personal.sendEmailCode) {
            return;
        }
        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("EmailAccount", EmailAccount));

        NoHttpUtil.post(netCode, Uri.Personal.SENDEMAILCODE, onResponseListener, params);
    }

    @Override
    public void editEmail(int netCode, String EmailAccount, String NumCode, String EditMailTime) {
        if (netCode != NetCode.Personal.editEmail) {
            return;
        }
        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("EmailAccount", EmailAccount));
        params.add(new NoHttpUtil.Param("NumCode", NumCode));
        params.add(new NoHttpUtil.Param("EditMailTime", EditMailTime));

        NoHttpUtil.post(netCode, Uri.Personal.EDITEMAIL, onResponseListener, params);
    }

    @Override
    public void editJob(int netCode, String JobCompany, String JobDepartment, String JobPosition, String JobEntryTime, String JobFirstWorkTime, String EditJobTime) {
        if (netCode != NetCode.Personal.editJob) {
            return;
        }
        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("JobCompany", JobCompany));
        params.add(new NoHttpUtil.Param("JobDepartment", JobDepartment));
        params.add(new NoHttpUtil.Param("JobPosition", JobPosition));
        params.add(new NoHttpUtil.Param("JobEntryTime", JobEntryTime));
        params.add(new NoHttpUtil.Param("JobFirstWorkTime", JobFirstWorkTime));
        params.add(new NoHttpUtil.Param("EditJobTime", EditJobTime));

        NoHttpUtil.post(netCode, Uri.Personal.EDITJOB, onResponseListener, params);
    }

    @Override
    public void editFamily(int netCode, String FamilyFatherName, String FamilyFatherPhone, String FamilyMotherName, String FamilyMotherPhone, int FamilyIsOnlyChild, String EditFamilyTime) {
        if (netCode != NetCode.Personal.editFamily) {
            return;
        }
        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("FamilyFatherName", FamilyFatherName));
        params.add(new NoHttpUtil.Param("FamilyFatherPhone", FamilyFatherPhone));
        params.add(new NoHttpUtil.Param("FamilyMotherName", FamilyMotherName));
        params.add(new NoHttpUtil.Param("FamilyMotherPhone", FamilyMotherPhone));
        params.add(new NoHttpUtil.Param("FamilyIsOnlyChild", FamilyIsOnlyChild));
        params.add(new NoHttpUtil.Param("EditFamilyTime", EditFamilyTime));


        NoHttpUtil.post(netCode, Uri.Personal.EDITFAMILY, onResponseListener, params);
    }

    @Override
    public void editMarriage(int netCode, int MarriageStatus, String MarriageSpouseName, int MarriageHaveChildren, int MarriageChildrenNum, String EditMarriageTime) {
        if (netCode != NetCode.Personal.editMarriage) {
            return;
        }
        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("MarriageStatus", MarriageStatus));
        if (!TextUtils.isEmpty(MarriageSpouseName)){
            params.add(new NoHttpUtil.Param("MarriageSpouseName", MarriageSpouseName));
        }

        params.add(new NoHttpUtil.Param("MarriageHaveChildren", MarriageHaveChildren));
        params.add(new NoHttpUtil.Param("MarriageChildrenNum", MarriageChildrenNum));
        params.add(new NoHttpUtil.Param("EditMarriageTime", EditMarriageTime));

        NoHttpUtil.post(netCode, Uri.Personal.EDITMARRIAGE, onResponseListener, params);
    }

    @Override
    public void addMajor(int netCode, String ProfessionalName, String ImageUrl, String CreateTime) {
        if (netCode != NetCode.Personal.addMajor) {
            return;
        }
        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("ProfessionalName", ProfessionalName));
        params.add(new NoHttpUtil.Param("ImageUrl", ImageUrl));
        params.add(new NoHttpUtil.Param("CreateTime", CreateTime));

        NoHttpUtil.post(netCode, Uri.Personal.ADDMAJOR, onResponseListener, params);
    }

    @Override
    public void editMajor(int netCode, int Id, String ProfessionalName, String ImageUrl, String EditTime) {
        if (netCode != NetCode.Personal.editMajor) {
            return;
        }
        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("Id", Id));
        params.add(new NoHttpUtil.Param("ProfessionalName", ProfessionalName));
        params.add(new NoHttpUtil.Param("ImageUrl", ImageUrl));
        params.add(new NoHttpUtil.Param("EditTime", EditTime));

        NoHttpUtil.post(netCode, Uri.Personal.EDITMAJOR, onResponseListener, params);
    }

    @Override
    public void deleteMajor(int netCode, int Id) {
        if (netCode != NetCode.Personal.deleteMajor) {
            return;
        }

        NoHttpUtil.post(netCode, Uri.Personal.DELETEMAJOR + "?Id=" + Id, onResponseListener, null);
    }

    @Override
    public void addService(int netCode, String ServiceName, String ServiceType, String ImageUrl, String CreateTime) {
        if (netCode != NetCode.Personal.addService) {
            return;
        }
        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("ServiceName", ServiceName));
        params.add(new NoHttpUtil.Param("ImageUrl", ImageUrl));
        params.add(new NoHttpUtil.Param("ServiceType", ServiceType));
        params.add(new NoHttpUtil.Param("CreateTime", CreateTime));

        NoHttpUtil.post(netCode, Uri.Personal.ADDSERVICE, onResponseListener, params);
    }

    @Override
    public void editService(int netCode, int Id, String ServiceName, String ServiceType, String ImageUrl, String EditTime) {
        if (netCode != NetCode.Personal.editService) {
            return;
        }
        List<NoHttpUtil.Param> params = new ArrayList<>();
        params.add(new NoHttpUtil.Param("ServiceName", ServiceName));
        params.add(new NoHttpUtil.Param("ImageUrl", ImageUrl));
        params.add(new NoHttpUtil.Param("ServiceType", ServiceType));
        params.add(new NoHttpUtil.Param("EditTime", EditTime));
        NoHttpUtil.post(netCode, Uri.Personal.EDITSERVICE, onResponseListener, params);
    }

    @Override
    public void deleteService(int netCode, int Id) {
        if (netCode != NetCode.Personal.deleteService) {
            return;
        }

        NoHttpUtil.post(netCode, Uri.Personal.DELETESERVICE + "?Id=" + Id, onResponseListener, null);
    }

    @Override
    public void getMajorList(int netCode) {
        if (netCode != NetCode.Personal.getMajorList) {
            return;
        }


        NoHttpUtil.get(netCode, Uri.Personal.GETMAJORLIST, onResponseListener, null);
    }

    @Override
    public void getServiceList(int netCode) {
        if (netCode != NetCode.Personal.getServiceList) {
            return;
        }

        NoHttpUtil.get(netCode, Uri.Personal.GETSERVICELIST, onResponseListener, null);
    }

    @Override
    public void rspSuccess(int what, Object object) throws JSONException {
        switch (what) {
            case NetCode.Personal.updateRefPersonalInfo:
            case NetCode.Personal.getPersonalInfo:
                Log.e(TAG, "请求到的用户信息为：" + object.toString());
                User authuser = mGson.fromJson(((JSONObject) object).getJSONObject("Data").toString(), User.class);
                Log.e(TAG, authuser.toString());
                UserUnits.getInstance().setAllUserInfo(authuser);
                mAllPrenInter.onSuccess(what, authuser);
                break;
            case NetCode.Personal.updatePersonalInfo:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.Personal.RefreshMessage:
                JSONArray jsaMessageRef = ((JSONObject) object).getJSONArray("Data");
                List<Message> messagesRef = mGson.fromJson(jsaMessageRef.toString(), new TypeToken<List<Message>>() {
                }.getType());
                if (messagesRef.size() == 0) ViewUnits.getInstance().showToast("暂无新消息");
                Collections.reverse(messagesRef);
                CacheUnits.getInstance().insertMessage(messagesRef);
                mAllPrenInter.onSuccess(what, messagesRef);
                break;
            case NetCode.Personal.getMessage:
                if (object !=null){
                    JSONArray jsaMessage = ((JSONObject) object).getJSONArray("Data");
                    List<Message> messages = mGson.fromJson(jsaMessage.toString(), new TypeToken<List<Message>>() {
                    }.getType());
                    Collections.reverse(messages);
                    CacheUnits.getInstance().insertMessage(messages);
                    mAllPrenInter.onSuccess(what, messages);
                }

                break;
            case NetCode.Personal.editPersonInfo:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.User2.changePhoneSendSms:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.User2.changePhoneCheckSms:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.User2.changePhoneSendBindSms:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.User2.changePhoneBinding:
                String token = ((JSONObject) object).getJSONObject("Data").getString("Token");
                String clientId = ((JSONObject) object).getJSONObject("Data").getString("ClientID");
                UserUnits.getInstance().setToken(token);
                ConfigUnits.getInstance().setPhoneAnalogIMEI(clientId);
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.Personal.getMyServiceList:
                UserServeBean serveBean = GsonUtils.jsonToBean(object.toString(), UserServeBean.class);
                mAllPrenInter.onSuccess(what, serveBean.getData());
                break;
            case NetCode.Personal.creditRankingsan:
                UserRankBean userRankBean = GsonUtils.jsonToBean(object.toString(), UserRankBean.class);
                mAllPrenInter.onSuccess(what, userRankBean.getData());
                break;
            case NetCode.Personal.creditRankingwushi:
                UserRankBean rankBean = GsonUtils.jsonToBean(object.toString(), UserRankBean.class);
                mAllPrenInter.onSuccess(what, rankBean.getData());
                break;

            case NetCode.Personal.getEquityList:
                UserServiceBean finishBean = GsonUtils.jsonToBean(object.toString(), UserServiceBean.class);
                mAllPrenInter.onSuccess(what, finishBean.getData());
                break;
            case NetCode.Personal.getExecuteList:
                UserServiceBean joinBean = GsonUtils.jsonToBean(object.toString(), UserServiceBean.class);
                mAllPrenInter.onSuccess(what, joinBean.getData().getList());
                break;
            case NetCode.Personal.getCreditRecord:
                CreditRecordBean recordBean = GsonUtils.jsonToBean(object.toString(), CreditRecordBean.class);
                mAllPrenInter.onSuccess(what, recordBean.getData());
                break;
            case NetCode.Personal.getCreditHistory:
                AccessRecordBean accessBean = GsonUtils.jsonToBean(object.toString(), AccessRecordBean.class);
                mAllPrenInter.onSuccess(what, accessBean.getData().getData());
                break;
            case NetCode.Personal.getMyEquity:
                UserRightsBean rightsBean = GsonUtils.jsonToBean(object.toString(), UserRightsBean.class);
                mAllPrenInter.onSuccess(what, rightsBean.getData());
                break;
            case NetCode.Personal.getAllInfo:
                UserInfoBean userInfoBean = GsonUtils.jsonToBean(object.toString(), UserInfoBean.class);
                mAllPrenInter.onSuccess(what, userInfoBean.getData());
                break;
            case NetCode.Personal.editEducation:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.Personal.sendEmailCode:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.Personal.editEmail:
                mAllPrenInter.onSuccess(what, null);

                break;
            case NetCode.Personal.editJob:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.Personal.editFamily:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.Personal.editMarriage:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.Personal.addMajor:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.Personal.deleteMajor:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.Personal.editMajor:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.Personal.addService:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.Personal.editService:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.Personal.deleteService:
                mAllPrenInter.onSuccess(what, null);
                break;
            case NetCode.Personal.getMajorList:
                MajorBean majorBean = GsonUtils.jsonToBean(object.toString(), MajorBean.class);
                mAllPrenInter.onSuccess(what, majorBean.getData());

                break;
            case NetCode.Personal.getServiceList:
                ServiceBean serviceBean = GsonUtils.jsonToBean(object.toString(), ServiceBean.class);
                mAllPrenInter.onSuccess(what, serviceBean.getData());
                break;


        }
    }

    @Override
    public void rspFailed(int what, Object object) {
        switch (what) {
            case NetCode.Personal.RefreshMessage:
                ViewUnits.getInstance().showToast("暂无新消息");
                break;
        }
        mAllPrenInter.onFail(what, object);
    }
}
