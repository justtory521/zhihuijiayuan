package tendency.hz.zhihuijiayuan.bean;

import java.util.List;

/**
 * Author：Libin on 2019/5/15 19:22
 * Email：1993911441@qq.com
 * Describe：
 */
public class AccessRecordBean {

    /**
     * Status : 2
     * Msg : 成功
     * Data : {"FirstData":{"AccountId":329,"CreditIntegral":0,"ExecuteNum":30,"CreateTime":"2019-05-15T11:47:55.5602061+08:00"},"data":[{"AccountId":329,"CreditIntegral":0,"ExecuteNum":27,"CreateTime":"2019-05-15T11:42:19.1881394+08:00"},{"AccountId":329,"CreditIntegral":0,"ExecuteNum":24,"CreateTime":"2019-05-15T11:38:51.1915992+08:00"},{"AccountId":329,"CreditIntegral":0,"ExecuteNum":21,"CreateTime":"2019-05-15T11:26:36.9674147+08:00"},{"AccountId":329,"CreditIntegral":0,"ExecuteNum":18,"CreateTime":"2019-05-15T11:25:38.8992363+08:00"},{"AccountId":329,"CreditIntegral":0,"ExecuteNum":15,"CreateTime":"2019-05-15T11:24:35.627129+08:00"},{"AccountId":329,"CreditIntegral":0,"ExecuteNum":12,"CreateTime":"2019-05-15T11:18:24.6786459+08:00"},{"AccountId":329,"CreditIntegral":0,"ExecuteNum":9,"CreateTime":"2019-05-15T10:53:26.7873354+08:00"},{"AccountId":329,"CreditIntegral":0,"ExecuteNum":6,"CreateTime":"2019-05-15T10:50:40.0717533+08:00"},{"AccountId":329,"CreditIntegral":0,"ExecuteNum":3,"CreateTime":"2019-05-15T10:48:17.765538+08:00"},{"AccountId":329,"CreditIntegral":29,"ExecuteNum":1,"CreateTime":"2019-04-01T00:00:00+08:00"}]}
     */

    private int Status;
    private String Msg;
    private DataBeanX Data;

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

    public DataBeanX getData() {
        return Data;
    }

    public void setData(DataBeanX Data) {
        this.Data = Data;
    }

    public static class DataBeanX {
        /**
         * FirstData : {"AccountId":329,"CreditIntegral":0,"ExecuteNum":30,"CreateTime":"2019-05-15T11:47:55.5602061+08:00"}
         * data : [{"AccountId":329,"CreditIntegral":0,"ExecuteNum":27,"CreateTime":"2019-05-15T11:42:19.1881394+08:00"},{"AccountId":329,"CreditIntegral":0,"ExecuteNum":24,"CreateTime":"2019-05-15T11:38:51.1915992+08:00"},{"AccountId":329,"CreditIntegral":0,"ExecuteNum":21,"CreateTime":"2019-05-15T11:26:36.9674147+08:00"},{"AccountId":329,"CreditIntegral":0,"ExecuteNum":18,"CreateTime":"2019-05-15T11:25:38.8992363+08:00"},{"AccountId":329,"CreditIntegral":0,"ExecuteNum":15,"CreateTime":"2019-05-15T11:24:35.627129+08:00"},{"AccountId":329,"CreditIntegral":0,"ExecuteNum":12,"CreateTime":"2019-05-15T11:18:24.6786459+08:00"},{"AccountId":329,"CreditIntegral":0,"ExecuteNum":9,"CreateTime":"2019-05-15T10:53:26.7873354+08:00"},{"AccountId":329,"CreditIntegral":0,"ExecuteNum":6,"CreateTime":"2019-05-15T10:50:40.0717533+08:00"},{"AccountId":329,"CreditIntegral":0,"ExecuteNum":3,"CreateTime":"2019-05-15T10:48:17.765538+08:00"},{"AccountId":329,"CreditIntegral":29,"ExecuteNum":1,"CreateTime":"2019-04-01T00:00:00+08:00"}]
         */

        private FirstDataBean FirstData;
        private List<DataBean> data;

        public FirstDataBean getFirstData() {
            return FirstData;
        }

        public void setFirstData(FirstDataBean FirstData) {
            this.FirstData = FirstData;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class FirstDataBean {
            /**
             * AccountId : 329
             * CreditIntegral : 0
             * ExecuteNum : 30
             * CreateTime : 2019-05-15T11:47:55.5602061+08:00
             */

            private int AccountId;
            private int CreditIntegral;
            private int ExecuteNum;
            private String CreateTime;

            public int getAccountId() {
                return AccountId;
            }

            public void setAccountId(int AccountId) {
                this.AccountId = AccountId;
            }

            public int getCreditIntegral() {
                return CreditIntegral;
            }

            public void setCreditIntegral(int CreditIntegral) {
                this.CreditIntegral = CreditIntegral;
            }

            public int getExecuteNum() {
                return ExecuteNum;
            }

            public void setExecuteNum(int ExecuteNum) {
                this.ExecuteNum = ExecuteNum;
            }

            public String getCreateTime() {
                return CreateTime;
            }

            public void setCreateTime(String CreateTime) {
                this.CreateTime = CreateTime;
            }
        }

        public static class DataBean {
            /**
             * AccountId : 329
             * CreditIntegral : 0
             * ExecuteNum : 27
             * CreateTime : 2019-05-15T11:42:19.1881394+08:00
             */

            private int AccountId;
            private int CreditIntegral;
            private int ExecuteNum;
            private String CreateTime;

            public int getAccountId() {
                return AccountId;
            }

            public void setAccountId(int AccountId) {
                this.AccountId = AccountId;
            }

            public int getCreditIntegral() {
                return CreditIntegral;
            }

            public void setCreditIntegral(int CreditIntegral) {
                this.CreditIntegral = CreditIntegral;
            }

            public int getExecuteNum() {
                return ExecuteNum;
            }

            public void setExecuteNum(int ExecuteNum) {
                this.ExecuteNum = ExecuteNum;
            }

            public String getCreateTime() {
                return CreateTime;
            }

            public void setCreateTime(String CreateTime) {
                this.CreateTime = CreateTime;
            }
        }
    }
}
