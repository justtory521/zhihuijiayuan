package tendency.hz.zhihuijiayuan.bean;

import java.util.List;

import tendency.hz.zhihuijiayuan.units.FormatUtils;

/**
 * 用户相关Bean
 * Created by JasonYao on 2018/3/22.
 */

public class User {
    private String Account;
    private String Code;
    private String Token;
    private String AccountID;
    private String Phone;
    private String Email;
    private String AccountName;
    private String CardID;
    private String RealName;
    private String Status;
    private String ConfirmStatusName;
    private String AutherizeName;
    private String HeadImgPath;
    private String NickName;
    private String Sex;
    private String Birthday;
    private String RepairBizAudit;
    private String PreviousCreditTime;
    private String CreditAssessMonthTime;
    private Integer PreviousCreditExecuteNum;
    private Integer CreditExecuteNum;

    public Integer getPreviousCreditExecuteNum() {
        return PreviousCreditExecuteNum;
    }

    public void setPreviousCreditExecuteNum(Integer previousCreditExecuteNum) {
        PreviousCreditExecuteNum = previousCreditExecuteNum;
    }

    public Integer getCreditExecuteNum() {
        return CreditExecuteNum;
    }

    public void setCreditExecuteNum(Integer creditExecuteNum) {
        CreditExecuteNum = creditExecuteNum;
    }

    public String getCreditAssessMonthTime() {
        return CreditAssessMonthTime;
    }

    public void setCreditAssessMonthTime(String creditAssessMonthTime) {
        CreditAssessMonthTime = creditAssessMonthTime;
    }

    public String getPreviousCreditTime() {
        return PreviousCreditTime;
    }

    public void setPreviousCreditTime(String previousCreditTime) {
        PreviousCreditTime = previousCreditTime;
    }

    public int getCreditScore() {
        return CreditScore;
    }

    public void setCreditScore(int creditScore) {
        CreditScore = creditScore;
    }

    private int CreditScore;
    private List<Area> DistricData;
    private int IntegralCoin;

    public int getIntegralCoin() {
        return IntegralCoin;
    }

    public void setIntegralCoin(int integralCoin) {
        IntegralCoin = integralCoin;
    }

    @Override
    public String toString() {
        return "User{" +
                "Account='" + Account + '\'' +
                ", Code='" + Code + '\'' +
                ", Token='" + Token + '\'' +
                ", AccountID='" + AccountID + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Email='" + Email + '\'' +
                ", AccountName='" + AccountName + '\'' +
                ", CardID='" + CardID + '\'' +
                ", RealName='" + RealName + '\'' +
                ", Status='" + Status + '\'' +
                ", ConfirmStatusName='" + ConfirmStatusName + '\'' +
                ", AutherizeName='" + AutherizeName + '\'' +
                ", HeadImgPath='" + HeadImgPath + '\'' +
                ", NickName='" + NickName + '\'' +
                ", Sex='" + Sex + '\'' +
                ", Birthday='" + Birthday + '\'' +
                ", RepairBizAudit='" + RepairBizAudit + '\'' +
                ", PreviousCreditTime='" + PreviousCreditTime + '\'' +
                ", CreditScore=" + CreditScore +
                ", DistricData=" + DistricData +
                '}';
    }

    public String getRepairBizAudit() {
        return RepairBizAudit;
    }

    public void setRepairBizAudit(String repairBizAudit) {
        RepairBizAudit = repairBizAudit;
    }

    public String getHeadImgPath() {
        return HeadImgPath;
    }

    public void setHeadImgPath(String headImgPath) {
        HeadImgPath = headImgPath;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public List<Area> getDistricData() {
        return DistricData;
    }

    public void setDistricData(List<Area> districData) {
        DistricData = districData;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getAccountID() {
        return AccountID;
    }

    public void setAccountID(String accountID) {
        AccountID = accountID;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getCardID() {
        return CardID;
    }

    public void setCardID(String cardID) {
        CardID = cardID;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getStatus() {
        if (FormatUtils.getInstance().isEmpty(Status)) {
            return "";
        }
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getConfirmStatusName() {
        return ConfirmStatusName;
    }

    public void setConfirmStatusName(String confirmStatusName) {
        ConfirmStatusName = confirmStatusName;
    }

    public String getAutherizeName() {
        return AutherizeName;
    }

    public void setAutherizeName(String autherizeName) {
        AutherizeName = autherizeName;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        this.Code = code;
    }

    public String getAddress() {
        if (DistricData == null || DistricData.size() == 0) return "";
        StringBuffer stringBuffer = new StringBuffer();
        for (Area area : DistricData) {
            stringBuffer.append(" " + area.getName());
        }

        return stringBuffer.toString().substring(1);
    }
}
