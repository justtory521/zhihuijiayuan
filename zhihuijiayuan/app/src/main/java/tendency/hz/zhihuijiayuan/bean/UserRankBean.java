package tendency.hz.zhihuijiayuan.bean;

import java.util.List;

/**
 * Author：Libin on 2019/5/14 12:45
 * Email：1993911441@qq.com
 * Describe：
 */
public class UserRankBean {

    /**
     * Status : 2
     * Msg : 成功
     * Data : {"list":[{"AccountId":189,"HeadImg":"http://183.129.130.119:12015/Upload/2018/10/17/b0830477-d719-4640-8fb1-48c70f1c1e31.jpg","NickName":"哈哈","PreviousCreditExecuteNum":null,"CreditExecuteNum":0,"PreviousCreditScoreRanking":2,"CreditScore":null}],"AccountId":343,"HeadImg":"http://183.129.130.119:12015/Upload//2019/04/22/30d23533-ffba-4faa-ba03-6d1d74d9839e.jpg","NickName":null,"PreviousCreditExecuteNum":null,"CreditExecuteNum":0,"PreviousCreditScoreRanking":null,"CreditScore":0,"DistrictName":null,"PreviousCreditTime":"2019-04-23 10:58:55"}
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

    public static class DataBean {
        /**
         * list : [{"AccountId":189,"HeadImg":"http://183.129.130.119:12015/Upload/2018/10/17/b0830477-d719-4640-8fb1-48c70f1c1e31.jpg","NickName":"哈哈","PreviousCreditExecuteNum":null,"CreditExecuteNum":0,"PreviousCreditScoreRanking":2,"CreditScore":null}]
         * AccountId : 343
         * HeadImg : http://183.129.130.119:12015/Upload//2019/04/22/30d23533-ffba-4faa-ba03-6d1d74d9839e.jpg
         * NickName : null
         * PreviousCreditExecuteNum : null
         * CreditExecuteNum : 0
         * PreviousCreditScoreRanking : null
         * CreditScore : 0
         * DistrictName : null
         * PreviousCreditTime : 2019-04-23 10:58:55
         */

        private int AccountId;
        private String HeadImg;
        private String NickName;
        private Object PreviousCreditExecuteNum;
        private int CreditExecuteNum;
        private Integer PreviousCreditScoreRanking;
        private Integer CreditScore;
        private String DistrictName;
        private String PreviousCreditTime;
        private List<ListBean> list;

        public int getAccountId() {
            return AccountId;
        }

        public void setAccountId(int AccountId) {
            this.AccountId = AccountId;
        }

        public String getHeadImg() {
            return HeadImg;
        }

        public void setHeadImg(String HeadImg) {
            this.HeadImg = HeadImg;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String NickName) {
            this.NickName = NickName;
        }

        public Object getPreviousCreditExecuteNum() {
            return PreviousCreditExecuteNum;
        }

        public void setPreviousCreditExecuteNum(Object PreviousCreditExecuteNum) {
            this.PreviousCreditExecuteNum = PreviousCreditExecuteNum;
        }

        public int getCreditExecuteNum() {
            return CreditExecuteNum;
        }

        public void setCreditExecuteNum(int CreditExecuteNum) {
            this.CreditExecuteNum = CreditExecuteNum;
        }

        public Integer getPreviousCreditScoreRanking() {
            return PreviousCreditScoreRanking;
        }

        public void setPreviousCreditScoreRanking(Integer PreviousCreditScoreRanking) {
            this.PreviousCreditScoreRanking = PreviousCreditScoreRanking;
        }

        public Integer getCreditScore() {
            return CreditScore;
        }

        public void setCreditScore(Integer CreditScore) {
            this.CreditScore = CreditScore;
        }

        public String getDistrictName() {
            return DistrictName;
        }

        public void setDistrictName(String DistrictName) {
            this.DistrictName = DistrictName;
        }

        public String getPreviousCreditTime() {
            return PreviousCreditTime;
        }

        public void setPreviousCreditTime(String PreviousCreditTime) {
            this.PreviousCreditTime = PreviousCreditTime;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {


            public ListBean(Object headImg, String nickName, String previousCreditScoreRanking) {
                HeadImg = headImg;
                NickName = nickName;
                PreviousCreditScoreRanking = previousCreditScoreRanking;
            }

            /**
             * AccountId : 189
             * HeadImg : http://183.129.130.119:12015/Upload/2018/10/17/b0830477-d719-4640-8fb1-48c70f1c1e31.jpg
             * NickName : 哈哈
             * PreviousCreditExecuteNum : null
             * CreditExecuteNum : 0
             * PreviousCreditScoreRanking : 2
             * CreditScore : null
             */

            private int AccountId;
            private Object HeadImg;
            private String NickName;
            private Object PreviousCreditExecuteNum;
            private int CreditExecuteNum;
            private String PreviousCreditScoreRanking;
            private Integer CreditScore;

            public int getAccountId() {
                return AccountId;
            }

            public void setAccountId(int AccountId) {
                this.AccountId = AccountId;
            }

            public Object getHeadImg() {
                return HeadImg;
            }

            public void setHeadImg(Object HeadImg) {
                this.HeadImg = HeadImg;
            }

            public String getNickName() {
                return NickName;
            }

            public void setNickName(String NickName) {
                this.NickName = NickName;
            }

            public Object getPreviousCreditExecuteNum() {
                return PreviousCreditExecuteNum;
            }

            public void setPreviousCreditExecuteNum(Object PreviousCreditExecuteNum) {
                this.PreviousCreditExecuteNum = PreviousCreditExecuteNum;
            }

            public int getCreditExecuteNum() {
                return CreditExecuteNum;
            }

            public void setCreditExecuteNum(int CreditExecuteNum) {
                this.CreditExecuteNum = CreditExecuteNum;
            }

            public String getPreviousCreditScoreRanking() {
                return PreviousCreditScoreRanking;
            }

            public void setPreviousCreditScoreRanking(String PreviousCreditScoreRanking) {
                this.PreviousCreditScoreRanking = PreviousCreditScoreRanking;
            }

            public Integer getCreditScore() {
                return CreditScore;
            }

            public void setCreditScore(Integer CreditScore) {
                this.CreditScore = CreditScore;
            }
        }
    }
}
