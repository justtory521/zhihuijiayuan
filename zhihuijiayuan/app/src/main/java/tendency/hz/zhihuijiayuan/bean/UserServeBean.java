package tendency.hz.zhihuijiayuan.bean;

import java.util.List;

/**
 * Author：Libin on 2019/5/14 11:26
 * Email：1993911441@qq.com
 * Describe：
 */
public class UserServeBean {

    /**
     * Status : 2
     * Msg : 成功
     * Data : [{"Id":1,"CreateTime":"2019-04-23 10:00:00","ServiceName":"服务名称","ImageUrl":"服务封面图片","Status":2,"StatusText":"进行中"}]
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
         * Id : 1
         * CreateTime : 2019-04-23 10:00:00
         * ServiceName : 服务名称
         * ImageUrl : 服务封面图片
         * Status : 2
         * StatusText : 进行中
         */

        private int Id;
        private String CreateTime;
        private String ServiceName;
        private String ImageUrl;
        private int Status;
        private String StatusText;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
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

        public int getStatus() {
            return Status;
        }

        public void setStatus(int Status) {
            this.Status = Status;
        }

        public String getStatusText() {
            return StatusText;
        }

        public void setStatusText(String StatusText) {
            this.StatusText = StatusText;
        }
    }
}
