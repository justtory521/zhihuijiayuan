package tendency.hz.zhihuijiayuan.bean;

import java.util.List;

/**
 * Author：Libin on 2019/5/16 13:17
 * Email：1993911441@qq.com
 * Describe：
 */
public class MajorBean {

    /**
     * Status : 2
     * Msg : 成功
     * Data : [{"ID":10,"AccountId":329,"ProfessionalName":"回来咯","ImageUrl":"http://183.129.130.119:12015/Upload//2019/05/13/247dd49e-9893-4602-a0d7-0b17942af819.jpg","CreateTime":"2019-05-13T00:00:00","EditTime":"2019/5/13 0:00:00"},{"ID":11,"AccountId":329,"ProfessionalName":"压缩的","ImageUrl":"http://183.129.130.119:12015/Upload//2019/05/13/4a10a62c-4cf7-4fbf-a6fc-6b722863044e.jpg","CreateTime":"2019-05-13T00:00:00","EditTime":"2019/5/13 0:00:00"},{"ID":12,"AccountId":329,"ProfessionalName":"是啊","ImageUrl":"http://183.129.130.119:12015/Upload//2019/05/14/0f34dc57-5525-4886-8fbc-7d81aca062ee.jpg","CreateTime":"2019-05-14T00:00:00","EditTime":"2019/5/14 0:00:00"},{"ID":13,"AccountId":329,"ProfessionalName":"这样","ImageUrl":"http://183.129.130.119:12015/Upload//2019/05/14/4bff8e8c-eb07-4b6e-994f-3bf4b2cba346.jpg","CreateTime":"2019-05-14T00:00:00","EditTime":"2019/5/14 0:00:00"},{"ID":14,"AccountId":329,"ProfessionalName":"我","ImageUrl":"http://183.129.130.119:12015/Upload//2019/05/14/dbaaf710-fb93-409e-b687-c64fff859dda.jpg","CreateTime":"2019-05-14T00:00:00","EditTime":"2019/5/14 0:00:00"},{"ID":15,"AccountId":329,"ProfessionalName":"12345","ImageUrl":"http://183.129.130.119:12015/Upload//2019/05/16/0645a2be-56ea-485b-b22d-92531c28f3dd.jpg","CreateTime":"2019-05-16T00:00:00","EditTime":"2019/5/16 0:00:00"}]
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
         * ID : 10
         * AccountId : 329
         * ProfessionalName : 回来咯
         * ImageUrl : http://183.129.130.119:12015/Upload//2019/05/13/247dd49e-9893-4602-a0d7-0b17942af819.jpg
         * CreateTime : 2019-05-13T00:00:00
         * EditTime : 2019/5/13 0:00:00
         */

        private int ID;
        private int AccountId;
        private String ProfessionalName;
        private String ImageUrl;
        private String CreateTime;
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

        public String getProfessionalName() {
            return ProfessionalName;
        }

        public void setProfessionalName(String ProfessionalName) {
            this.ProfessionalName = ProfessionalName;
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

        public String getEditTime() {
            return EditTime;
        }

        public void setEditTime(String EditTime) {
            this.EditTime = EditTime;
        }
    }
}
