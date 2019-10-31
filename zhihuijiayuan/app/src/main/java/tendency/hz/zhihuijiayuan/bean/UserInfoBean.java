package tendency.hz.zhihuijiayuan.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Author：Libin on 2019/5/16 13:10
 * Email：1993911441@qq.com
 * Describe：
 */
public class UserInfoBean implements Serializable {

    /**
     * Status : 2
     * Msg : 成功
     * Data : {"ID":144,"AccountID":329,"CardID":"110101199003072877","RealName":"曾勇yong","ImageCollection":"","HeadImg":5986,"NickName":"尕尕勇","Sex":null,"Birthday":null,"DistrictId":130303,"Status":3,"EducationDistrictId":360902,"SchoolName":"yi宜春学院","EducationType":5,"EducationEndTime":"2019-05-14","Email":"3036076103@qq.com","JobCompany":"天地人123","JobDepartment":"研发部","JobPosition":"iOS开发","JobEntryTime":"2019-05-10","JobFirstWorkTime":"2019-05-10","FamilyFatherName":"爸","FamilyFatherPhone":"18311111111","FamilyMotherName":"妈","FamilyMotherPhone":"18322222222","FamilyIsOnlyChild":1,"MarriageStatus":1,"MarriageSpouseName":"尕尕花","MarriageHaveChildren":2,"MarriageChildrenNum":10,"CreditScore":0,"PreviousCreditScoreRanking":1,"PreviousCreditDistrictId":130300,"PreviousCreditExecuteNum":0,"IntegralCoin":null,"CreditExecuteNum":30,"PreviousCreditTime":"2019-05-15T11:47:56","PreviousCreditScore":0,"EditIDCardTime":"2019-05-10","EditEduTime":"2019-05-14","EditMailTime":"2019-05-13","EditJobTime":"2019-05-13","EditFamilyTime":"2019-05-15","EditMarriageTime":"2019-05-13","EduDistricData":[{"ID":360000,"Name":"江西省"},{"ID":360900,"Name":"宜春市"},{"ID":360902,"Name":"袁州区"}]}
     */

    private int Status;
    private String Msg;
    private DataBean Data;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public static class DataBean implements Serializable {
        /**
         * ID : 144
         * AccountID : 329
         * CardID : 110101199003072877
         * RealName : 曾勇yong
         * ImageCollection :
         * HeadImg : 5986
         * NickName : 尕尕勇
         * Sex : null
         * Birthday : null
         * DistrictId : 130303
         * Status : 3
         * EducationDistrictId : 360902
         * SchoolName : yi宜春学院
         * EducationType : 5
         * EducationEndTime : 2019-05-14
         * Email : 3036076103@qq.com
         * JobCompany : 天地人123
         * JobDepartment : 研发部
         * JobPosition : iOS开发
         * JobEntryTime : 2019-05-10
         * JobFirstWorkTime : 2019-05-10
         * FamilyFatherName : 爸
         * FamilyFatherPhone : 18311111111
         * FamilyMotherName : 妈
         * FamilyMotherPhone : 18322222222
         * FamilyIsOnlyChild : 1
         * MarriageStatus : 1
         * MarriageSpouseName : 尕尕花
         * MarriageHaveChildren : 2
         * MarriageChildrenNum : 10
         * CreditScore : 0
         * PreviousCreditScoreRanking : 1
         * PreviousCreditDistrictId : 130300
         * PreviousCreditExecuteNum : 0
         * IntegralCoin : null
         * CreditExecuteNum : 30
         * PreviousCreditTime : 2019-05-15T11:47:56
         * PreviousCreditScore : 0
         * EditIDCardTime : 2019-05-10
         * EditEduTime : 2019-05-14
         * EditMailTime : 2019-05-13
         * EditJobTime : 2019-05-13
         * EditFamilyTime : 2019-05-15
         * EditMarriageTime : 2019-05-13
         * EduDistricData : [{"ID":360000,"Name":"江西省"},{"ID":360900,"Name":"宜春市"},{"ID":360902,"Name":"袁州区"}]
         */

        private int ID;
        private int AccountID;
        private String CardID;
        private String RealName;
        private String ImageCollection;
        private int HeadImg;
        private String NickName;
        private Object Sex;
        private Object Birthday;
        private int DistrictId;
        private int Status;
        private int EducationDistrictId;
        private String SchoolName;
        private Integer EducationType;
        private String EducationEndTime;
        private String Email;
        private String JobCompany;
        private String JobDepartment;
        private String JobPosition;
        private String JobEntryTime;
        private String JobFirstWorkTime;
        private String FamilyFatherName;
        private String FamilyFatherPhone;
        private String FamilyMotherName;
        private String FamilyMotherPhone;
        private Integer FamilyIsOnlyChild;
        private Integer MarriageStatus;
        private String MarriageSpouseName;
        private Integer MarriageHaveChildren;
        private Integer MarriageChildrenNum;
        private int CreditScore;
        private int PreviousCreditScoreRanking;
        private int PreviousCreditDistrictId;
        private int PreviousCreditExecuteNum;
        private Object IntegralCoin;
        private int CreditExecuteNum;
        private String PreviousCreditTime;
        private int PreviousCreditScore;
        private String EditIDCardTime;
        private String EditEduTime;
        private String EditMailTime;
        private String EditJobTime;
        private String EditFamilyTime;
        private String EditMarriageTime;
        private List<EduDistricDataBean> EduDistricData;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public int getAccountID() {
            return AccountID;
        }

        public void setAccountID(int AccountID) {
            this.AccountID = AccountID;
        }

        public String getCardID() {
            return CardID;
        }

        public void setCardID(String CardID) {
            this.CardID = CardID;
        }

        public String getRealName() {
            return RealName;
        }

        public void setRealName(String RealName) {
            this.RealName = RealName;
        }

        public String getImageCollection() {
            return ImageCollection;
        }

        public void setImageCollection(String ImageCollection) {
            this.ImageCollection = ImageCollection;
        }

        public int getHeadImg() {
            return HeadImg;
        }

        public void setHeadImg(int HeadImg) {
            this.HeadImg = HeadImg;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String NickName) {
            this.NickName = NickName;
        }

        public Object getSex() {
            return Sex;
        }

        public void setSex(Object Sex) {
            this.Sex = Sex;
        }

        public Object getBirthday() {
            return Birthday;
        }

        public void setBirthday(Object Birthday) {
            this.Birthday = Birthday;
        }

        public int getDistrictId() {
            return DistrictId;
        }

        public void setDistrictId(int DistrictId) {
            this.DistrictId = DistrictId;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int Status) {
            this.Status = Status;
        }

        public int getEducationDistrictId() {
            return EducationDistrictId;
        }

        public void setEducationDistrictId(int EducationDistrictId) {
            this.EducationDistrictId = EducationDistrictId;
        }

        public String getSchoolName() {
            return SchoolName;
        }

        public void setSchoolName(String SchoolName) {
            this.SchoolName = SchoolName;
        }

        public Integer getEducationType() {
            return EducationType;
        }

        public void setEducationType(Integer EducationType) {
            this.EducationType = EducationType;
        }

        public String getEducationEndTime() {
            return EducationEndTime;
        }

        public void setEducationEndTime(String EducationEndTime) {
            this.EducationEndTime = EducationEndTime;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public String getJobCompany() {
            return JobCompany;
        }

        public void setJobCompany(String JobCompany) {
            this.JobCompany = JobCompany;
        }

        public String getJobDepartment() {
            return JobDepartment;
        }

        public void setJobDepartment(String JobDepartment) {
            this.JobDepartment = JobDepartment;
        }

        public String getJobPosition() {
            return JobPosition;
        }

        public void setJobPosition(String JobPosition) {
            this.JobPosition = JobPosition;
        }

        public String getJobEntryTime() {
            return JobEntryTime;
        }

        public void setJobEntryTime(String JobEntryTime) {
            this.JobEntryTime = JobEntryTime;
        }

        public String getJobFirstWorkTime() {
            return JobFirstWorkTime;
        }

        public void setJobFirstWorkTime(String JobFirstWorkTime) {
            this.JobFirstWorkTime = JobFirstWorkTime;
        }

        public String getFamilyFatherName() {
            return FamilyFatherName;
        }

        public void setFamilyFatherName(String FamilyFatherName) {
            this.FamilyFatherName = FamilyFatherName;
        }

        public String getFamilyFatherPhone() {
            return FamilyFatherPhone;
        }

        public void setFamilyFatherPhone(String FamilyFatherPhone) {
            this.FamilyFatherPhone = FamilyFatherPhone;
        }

        public String getFamilyMotherName() {
            return FamilyMotherName;
        }

        public void setFamilyMotherName(String FamilyMotherName) {
            this.FamilyMotherName = FamilyMotherName;
        }

        public String getFamilyMotherPhone() {
            return FamilyMotherPhone;
        }

        public void setFamilyMotherPhone(String FamilyMotherPhone) {
            this.FamilyMotherPhone = FamilyMotherPhone;
        }

        public Integer getFamilyIsOnlyChild() {
            return FamilyIsOnlyChild;
        }

        public void setFamilyIsOnlyChild(Integer FamilyIsOnlyChild) {
            this.FamilyIsOnlyChild = FamilyIsOnlyChild;
        }

        public Integer getMarriageStatus() {
            return MarriageStatus;
        }

        public void setMarriageStatus(Integer MarriageStatus) {
            this.MarriageStatus = MarriageStatus;
        }

        public String getMarriageSpouseName() {
            return MarriageSpouseName;
        }

        public void setMarriageSpouseName(String MarriageSpouseName) {
            this.MarriageSpouseName = MarriageSpouseName;
        }

        public Integer getMarriageHaveChildren() {
            return MarriageHaveChildren;
        }

        public void setMarriageHaveChildren(Integer MarriageHaveChildren) {
            this.MarriageHaveChildren = MarriageHaveChildren;
        }

        public Integer getMarriageChildrenNum() {
            return MarriageChildrenNum;
        }

        public void setMarriageChildrenNum(Integer MarriageChildrenNum) {
            this.MarriageChildrenNum = MarriageChildrenNum;
        }

        public int getCreditScore() {
            return CreditScore;
        }

        public void setCreditScore(int CreditScore) {
            this.CreditScore = CreditScore;
        }

        public int getPreviousCreditScoreRanking() {
            return PreviousCreditScoreRanking;
        }

        public void setPreviousCreditScoreRanking(int PreviousCreditScoreRanking) {
            this.PreviousCreditScoreRanking = PreviousCreditScoreRanking;
        }

        public int getPreviousCreditDistrictId() {
            return PreviousCreditDistrictId;
        }

        public void setPreviousCreditDistrictId(int PreviousCreditDistrictId) {
            this.PreviousCreditDistrictId = PreviousCreditDistrictId;
        }

        public int getPreviousCreditExecuteNum() {
            return PreviousCreditExecuteNum;
        }

        public void setPreviousCreditExecuteNum(int PreviousCreditExecuteNum) {
            this.PreviousCreditExecuteNum = PreviousCreditExecuteNum;
        }

        public Object getIntegralCoin() {
            return IntegralCoin;
        }

        public void setIntegralCoin(Object IntegralCoin) {
            this.IntegralCoin = IntegralCoin;
        }

        public int getCreditExecuteNum() {
            return CreditExecuteNum;
        }

        public void setCreditExecuteNum(int CreditExecuteNum) {
            this.CreditExecuteNum = CreditExecuteNum;
        }

        public String getPreviousCreditTime() {
            return PreviousCreditTime;
        }

        public void setPreviousCreditTime(String PreviousCreditTime) {
            this.PreviousCreditTime = PreviousCreditTime;
        }

        public int getPreviousCreditScore() {
            return PreviousCreditScore;
        }

        public void setPreviousCreditScore(int PreviousCreditScore) {
            this.PreviousCreditScore = PreviousCreditScore;
        }

        public String getEditIDCardTime() {
            return EditIDCardTime;
        }

        public void setEditIDCardTime(String EditIDCardTime) {
            this.EditIDCardTime = EditIDCardTime;
        }

        public String getEditEduTime() {
            return EditEduTime;
        }

        public void setEditEduTime(String EditEduTime) {
            this.EditEduTime = EditEduTime;
        }

        public String getEditMailTime() {
            return EditMailTime;
        }

        public void setEditMailTime(String EditMailTime) {
            this.EditMailTime = EditMailTime;
        }

        public String getEditJobTime() {
            return EditJobTime;
        }

        public void setEditJobTime(String EditJobTime) {
            this.EditJobTime = EditJobTime;
        }

        public String getEditFamilyTime() {
            return EditFamilyTime;
        }

        public void setEditFamilyTime(String EditFamilyTime) {
            this.EditFamilyTime = EditFamilyTime;
        }

        public String getEditMarriageTime() {
            return EditMarriageTime;
        }

        public void setEditMarriageTime(String EditMarriageTime) {
            this.EditMarriageTime = EditMarriageTime;
        }

        public List<EduDistricDataBean> getEduDistricData() {
            return EduDistricData;
        }

        public void setEduDistricData(List<EduDistricDataBean> EduDistricData) {
            this.EduDistricData = EduDistricData;
        }

        public static class EduDistricDataBean implements Serializable {
            /**
             * ID : 360000
             * Name : 江西省
             */

            private int ID;
            private String Name;

            public int getID() {
                return ID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }
        }
    }
}
