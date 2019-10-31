package tendency.hz.zhihuijiayuan.presenter;

import tendency.hz.zhihuijiayuan.model.PersonalModelImpl;
import tendency.hz.zhihuijiayuan.model.modelInter.PersonalModelInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.AllPrenInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PersonalPrenInter;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/3/28.
 */

public class PersonalPrenImpl implements PersonalPrenInter, AllPrenInter {
    private AllViewInter mAllViewInter;
    private PersonalModelInter mPersonalModelInter;

    public PersonalPrenImpl(AllViewInter allViewInter) {
        mAllViewInter = allViewInter;
        mPersonalModelInter = new PersonalModelImpl(this);
    }

    @Override
    public void getPersonalInfo(int netCode) {
        mPersonalModelInter.getPersonalInfo(netCode);
    }

    @Override
    public void updatePersonalInfo(int netCode, String type, String value) {
        mPersonalModelInter.updatePersonalInfo(netCode, type, value);
    }

    @Override
    public void getMessage(int netCode) {
        mPersonalModelInter.getMessage(netCode);
    }

    @Override
    public void editPersonInfo(int netCode, String headImg, String nickName, String sex, String birthday, String address) {
        mPersonalModelInter.editPersonInfo(netCode, headImg, nickName, sex, birthday, address);
    }

    @Override
    public void changePhoneSendSms(int netCode) {
        mPersonalModelInter.changePhoneSendSms(netCode);
    }

    @Override
    public void changePhoneCheckCode(int netCode, String code) {
        mPersonalModelInter.changePhoneCheckCode(netCode, code);
    }

    @Override
    public void changePhoneSendBindSms(int netCode, String phone) {
        mPersonalModelInter.changePhoneSendBindSms(netCode, phone);
    }

    @Override
    public void changePhoneBindPhone(int netCode, String oldPhone, String newPhone, String code) {
        mPersonalModelInter.changePhoneBindPhone(netCode, oldPhone, newPhone, code);
    }

    @Override
    public void getMyServiceList(int netCode) {
        mPersonalModelInter.getMyServiceList(netCode);
    }

    @Override
    public void creditRankingsan(int netCode) {
        mPersonalModelInter.creditRankingsan(netCode);
    }

    @Override
    public void creditRankingwushi(int netCode) {
        mPersonalModelInter.creditRankingwushi(netCode);
    }

    @Override
    public void getEquityList(int netCode) {
        mPersonalModelInter.getEquityList(netCode);
    }

    @Override
    public void getExecuteList(int netCode) {
        mPersonalModelInter.getExecuteList(netCode);
    }

    @Override
    public void getCreditRecord(int netCode, int state, int PageIndex) {
        mPersonalModelInter.getCreditRecord(netCode, state, PageIndex);
    }

    @Override
    public void getCreditHistory(int netCode, int PageSize, int CurrentPage) {
        mPersonalModelInter.getCreditHistory(netCode, PageSize, CurrentPage);
    }

    @Override
    public void getMyEquity(int netCode) {
        mPersonalModelInter.getMyEquity(netCode);
    }

    @Override
    public void getAllInfo(int netCode) {
        mPersonalModelInter.getAllInfo(netCode);
    }

    @Override
    public void editEducation(int netCode, int EducationDistrictId, String SchoolName, int EducationType, String EducationEndTime, String EditEduTime) {
        mPersonalModelInter.editEducation(netCode, EducationDistrictId, SchoolName, EducationType, EducationEndTime, EditEduTime);
    }

    @Override
    public void sendEmailCode(int netCode, String EmailAccount) {
        mPersonalModelInter.sendEmailCode(netCode, EmailAccount);
    }

    @Override
    public void editEmail(int netCode, String EmailAccount, String NumCode, String EditMailTime) {
        mPersonalModelInter.editEmail(netCode, EmailAccount, NumCode, EditMailTime);
    }

    @Override
    public void editJob(int netCode, String JobCompany, String JobDepartment, String JobPosition, String JobEntryTime, String JobFirstWorkTime, String EditJobTime) {
        mPersonalModelInter.editJob(netCode, JobCompany, JobDepartment, JobPosition, JobEntryTime, JobFirstWorkTime, EditJobTime);
    }

    @Override
    public void editFamily(int netCode, String FamilyFatherName, String FamilyFatherPhone, String FamilyMotherName, String FamilyMotherPhone, int FamilyIsOnlyChild, String EditFamilyTime) {
        mPersonalModelInter.editFamily(netCode, FamilyFatherName, FamilyFatherPhone, FamilyMotherName, FamilyMotherPhone, FamilyIsOnlyChild, EditFamilyTime);
    }

    @Override
    public void editMarriage(int netCode, int MarriageStatus, String MarriageSpouseName, int MarriageHaveChildren, int MarriageChildrenNum, String EditMarriageTime) {
        mPersonalModelInter.editMarriage(netCode, MarriageStatus, MarriageSpouseName, MarriageHaveChildren, MarriageChildrenNum, EditMarriageTime);
    }

    @Override
    public void addMajor(int netCode, String ProfessionalName, String ImageUrl, String CreateTime) {
        mPersonalModelInter.addMajor(netCode, ProfessionalName, ImageUrl, CreateTime);
    }

    @Override
    public void editMajor(int netCode, int Id, String ProfessionalName, String ImageUrl, String EditTime) {
        mPersonalModelInter.editMajor(netCode, Id, ProfessionalName, ImageUrl, EditTime);
    }

    @Override
    public void deleteMajor(int netCode, int Id) {
        mPersonalModelInter.deleteMajor(netCode, Id);
    }

    @Override
    public void addService(int netCode, String ServiceName, String ServiceType, String ImageUrl, String CreateTime) {
        mPersonalModelInter.addService(netCode, ServiceName, ServiceType, ImageUrl, CreateTime);
    }

    @Override
    public void editService(int netCode, int Id, String ServiceName, String ServiceType, String ImageUrl, String EditTime) {
        mPersonalModelInter.editService(netCode, Id, ServiceName, ServiceType, ImageUrl, EditTime);
    }

    @Override
    public void deleteService(int netCode, int Id) {
        mPersonalModelInter.deleteService(netCode, Id);
    }

    @Override
    public void getMajorList(int netCode) {
        mPersonalModelInter.getMajorList(netCode);
    }

    @Override
    public void getServiceList(int netCode) {
        mPersonalModelInter.getServiceList(netCode);
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
