package tendency.hz.zhihuijiayuan.presenter.prenInter;

/**
 * 个人信息操作接口
 * Created by JasonYao on 2018/3/28.
 */

public interface PersonalPrenInter {


    /**
     * 获取个人资料展示信息
     *
     * @param netCode
     */
    void getPersonalInfo(int netCode);

    /**
     * 个人信息变更
     *
     * @param netCode
     * @param type
     * @param value
     */
    void updatePersonalInfo(int netCode, String type, String value);

    /**
     * 获取消息列表
     *
     * @param netCode
     */
    void getMessage(int netCode);

    /**
     * 个人信息变更
     *
     * @param netCode
     * @param headImg
     * @param nickName
     * @param sex
     * @param birthday
     * @param address
     */
    void editPersonInfo(int netCode, String headImg, String nickName, String sex, String birthday, String address);

    /**
     * 发送老手机短信验证码
     *
     * @param netCode
     */
    void changePhoneSendSms(int netCode);

    /**
     * 校验老手机短信验证码
     *
     * @param netCode
     * @param code
     */
    void changePhoneCheckCode(int netCode, String code);

    /**
     * 发送新手机短信验证码
     *
     * @param netCode
     * @param phone
     */
    void changePhoneSendBindSms(int netCode, String phone);

    /**
     * 绑定新手机
     *
     * @param netCode
     * @param oldPhone
     * @param newPhone
     * @param code
     */
    void changePhoneBindPhone(int netCode, String oldPhone, String newPhone, String code);


    /**
     * 用户进行服务列表
     *
     * @param netCode
     */
    void getMyServiceList(int netCode);

    /**
     * 用户信用积分排行
     *
     * @param netCode
     */
    void creditRankingsan(int netCode);

    /**
     * 信用积分总排行
     *
     * @param netCode
     */
    void creditRankingwushi(int netCode);

    /**
     * 获得服务
     *
     * @param netCode
     */
    void getEquityList(int netCode);


    /**
     * 参与服务
     *
     * @param netCode
     */
    void getExecuteList(int netCode);


    /**
     * 信用记录
     *
     * @param netCode
     * @param PageIndex
     */
    void getCreditRecord(int netCode, int state, int PageIndex);


    /**
     * 评估记录
     *
     * @param netCode
     * @param CurrentPage
     */
    void getCreditHistory(int netCode, int PageSize, int CurrentPage);

    /**
     * 我的特权
     *
     * @param netCode
     */
    void getMyEquity(int netCode);


    /**
     * 获取个人所有信息
     *
     * @param netCode
     */
    void getAllInfo(int netCode);

    /**
     * 修改学历学籍
     *
     * @param netCode
     */
    void editEducation(int netCode, int EducationDistrictId, String SchoolName, int EducationType, String EducationEndTime, String EditEduTime);


    /**
     * 发送邮箱验证码
     *
     * @param netCode
     */
    void sendEmailCode(int netCode, String EmailAccount);


    /**
     * 修改邮箱账号
     *
     * @param netCode
     */
    void editEmail(int netCode, String EmailAccount, String NumCode, String EditMailTime);


    /**
     * 修改职业信息
     *
     * @param netCode
     */
    void editJob(int netCode, String JobCompany, String JobDepartment, String JobPosition, String JobEntryTime, String JobFirstWorkTime, String EditJobTime);


    /**
     * 修改家庭信息
     *
     * @param netCode
     */
    void editFamily(int netCode, String FamilyFatherName, String FamilyFatherPhone, String FamilyMotherName, String FamilyMotherPhone, int FamilyIsOnlyChild, String EditFamilyTime);


    /**
     * 修改婚姻信息
     *
     * @param netCode
     */
    void editMarriage(int netCode, int MarriageStatus, String MarriageSpouseName, int MarriageHaveChildren, int MarriageChildrenNum, String EditMarriageTime);


    /**
     * 添加专业证书
     *
     * @param netCode
     */
    void addMajor(int netCode, String ProfessionalName, String ImageUrl, String CreateTime);

    /**
     * 修改专业证书
     *
     * @param netCode
     */
    void editMajor(int netCode, int Id, String ProfessionalName, String ImageUrl, String EditTime);


    /**
     * 删除专业证书
     *
     * @param netCode
     */
    void deleteMajor(int netCode, int Id);

    /**
     * 添加服务证书
     *
     * @param netCode
     */
    void addService(int netCode, String ServiceName, String ServiceType, String ImageUrl, String CreateTime);

    /**
     * 修改服务证书
     *
     * @param netCode
     */
    void editService(int netCode, int Id, String ServiceName, String ServiceType, String ImageUrl, String EditTime);

    /**
     * 删除服务证书
     *
     * @param netCode
     */
    void deleteService(int netCode, int Id);

    /**
     * 专业证书列表
     *
     * @param netCode
     */
    void getMajorList(int netCode);

    /**
     * 服务证书列表
     *
     * @param netCode
     */
    void getServiceList(int netCode);


}
