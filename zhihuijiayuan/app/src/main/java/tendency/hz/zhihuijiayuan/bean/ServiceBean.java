package tendency.hz.zhihuijiayuan.bean;

import java.util.List;

/**
 * Author：Libin on 2019/5/16 13:17
 * Email：1993911441@qq.com
 * Describe：
 */
public class ServiceBean {

    /**
     * Status : 2
     * Msg : 成功
     * Data : [{"ID":6,"AccountId":329,"ServiceName":"888","ImageUrl":"http://183.129.130.119:12015/Upload//2019/05/13/feb85037-215a-4484-b60e-9ad41dd2e435.jpg","CreateTime":"2019-05-13T00:00:00","ServiceType":"999","EditTime":"2019/5/13 0:00:00"},{"ID":7,"AccountId":329,"ServiceName":"111","ImageUrl":"http://183.129.130.119:12015/Upload//2019/05/15/967b185f-faa7-42fd-a995-69b3b40aa33e.jpg","CreateTime":"2019-05-15T00:00:00","ServiceType":"222","EditTime":"2019/5/15 0:00:00"}]
     */

    private int Status;
    private String Msg;
    private List<DataBean> Data;

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

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * ID : 6
         * AccountId : 329
         * ServiceName : 888
         * ImageUrl : http://183.129.130.119:12015/Upload//2019/05/13/feb85037-215a-4484-b60e-9ad41dd2e435.jpg
         * CreateTime : 2019-05-13T00:00:00
         * ServiceType : 999
         * EditTime : 2019/5/13 0:00:00
         */

        private int ID;
        private int AccountId;
        private String ServiceName;
        private String ImageUrl;
        private String CreateTime;
        private String ServiceType;
        private String EditTime;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public int getAccountId() {
            return AccountId;
        }

        public void setAccountId(int AccountId) {
            this.AccountId = AccountId;
        }

        public String getServiceName() {
            return ServiceName;
        }

        public void setServiceName(String ServiceName) {
            this.ServiceName = ServiceName;
        }

        public String getImageUrl() {
            return ImageUrl;
        }

        public void setImageUrl(String ImageUrl) {
            this.ImageUrl = ImageUrl;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public String getServiceType() {
            return ServiceType;
        }

        public void setServiceType(String ServiceType) {
            this.ServiceType = ServiceType;
        }

        public String getEditTime() {
            return EditTime;
        }

        public void setEditTime(String EditTime) {
            this.EditTime = EditTime;
        }
    }
}
