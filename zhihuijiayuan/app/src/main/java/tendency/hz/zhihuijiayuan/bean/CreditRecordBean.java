package tendency.hz.zhihuijiayuan.bean;

import java.util.List;

/**
 * Author：Libin on 2019/5/15 16:20
 * Email：1993911441@qq.com
 * Describe：
 */
public class CreditRecordBean {


    /**
     * Status : 2
     * Msg : 成功
     * Data : {"TotalCount":1,"TotalPage":1,"List":[{"ServerId":6,"CardId":257,"AccountId":329,"CreateTime":"2019-04-28T11:27:51","Status":2,"IsEquity":0,"CompleteTime":"2019-04-28T11:28:00","CreditEquityId":0,"HeadImage":"http://183.129.130.119:12015/Upload/2018/11/07/b72559a9-e17e-477c-9ba4-e6afc8771ca4.jpg","ServiceName":"测试服务2"}]}
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
         * TotalCount : 1
         * TotalPage : 1
         * List : [{"ServerId":6,"CardId":257,"AccountId":329,"CreateTime":"2019-04-28T11:27:51","Status":2,"IsEquity":0,"CompleteTime":"2019-04-28T11:28:00","CreditEquityId":0,"HeadImage":"http://183.129.130.119:12015/Upload/2018/11/07/b72559a9-e17e-477c-9ba4-e6afc8771ca4.jpg","ServiceName":"测试服务2"}]
         */

        private int TotalCount;
        private int TotalPage;
        private java.util.List<ListBean> List;

        public int getTotalCount() {
            return TotalCount;
        }

        public void setTotalCount(int TotalCount) {
            this.TotalCount = TotalCount;
        }

        public int getTotalPage() {
            return TotalPage;
        }

        public void setTotalPage(int TotalPage) {
            this.TotalPage = TotalPage;
        }

        public List<ListBean> getList() {
            return List;
        }

        public void setList(List<ListBean> List) {
            this.List = List;
        }

        public static class ListBean {
            /**
             * ServerId : 6
             * CardId : 257
             * AccountId : 329
             * CreateTime : 2019-04-28T11:27:51
             * Status : 2
             * IsEquity : 0
             * CompleteTime : 2019-04-28T11:28:00
             * CreditEquityId : 0
             * HeadImage : http://183.129.130.119:12015/Upload/2018/11/07/b72559a9-e17e-477c-9ba4-e6afc8771ca4.jpg
             * ServiceName : 测试服务2
             */

            private int ServerId;
            private int CardId;
            private int AccountId;
            private String CreateTime;
            private int Status;
            private int IsEquity;
            private String CompleteTime;
            private int CreditEquityId;
            private String HeadImage;
            private String ServiceName;

            public int getServerId() {
                return ServerId;
            }

            public void setServerId(int ServerId) {
                this.ServerId = ServerId;
            }

            public int getCardId() {
                return CardId;
            }

            public void setCardId(int CardId) {
                this.CardId = CardId;
            }

            public int getAccountId() {
                return AccountId;
            }

            public void setAccountId(int AccountId) {
                this.AccountId = AccountId;
            }

            public String getCreateTime() {
                return CreateTime;
            }

            public void setCreateTime(String CreateTime) {
                this.CreateTime = CreateTime;
            }

            public int getStatus() {
                return Status;
            }

            public void setStatus(int Status) {
                this.Status = Status;
            }

            public int getIsEquity() {
                return IsEquity;
            }

            public void setIsEquity(int IsEquity) {
                this.IsEquity = IsEquity;
            }

            public String getCompleteTime() {
                return CompleteTime;
            }

            public void setCompleteTime(String CompleteTime) {
                this.CompleteTime = CompleteTime;
            }

            public int getCreditEquityId() {
                return CreditEquityId;
            }

            public void setCreditEquityId(int CreditEquityId) {
                this.CreditEquityId = CreditEquityId;
            }

            public String getHeadImage() {
                return HeadImage;
            }

            public void setHeadImage(String HeadImage) {
                this.HeadImage = HeadImage;
            }

            public String getServiceName() {
                return ServiceName;
            }

            public void setServiceName(String ServiceName) {
                this.ServiceName = ServiceName;
            }
        }
    }
}
