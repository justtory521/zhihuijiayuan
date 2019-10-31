package tendency.hz.zhihuijiayuan.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Author：Libin on 2019/5/15 20:46
 * Email：1993911441@qq.com
 * Describe：
 */
public class UserRightsBean implements Serializable {

    /**
     * Status : 2
     * Msg : 成功
     * Data : [{"ClassName":"分类名称","ClassId":1,"List":[{"Id":1,"ServiceName":"服务名称","HeadImage":"封面照片","Describe":"服务描述"}]},{"ClassName":"分类名称","ClassId":1,"List":[{"Id":1,"ServiceName":"服务名称","HeadImage":"封面照片","Describe":"服务描述"}]}]
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

    public static class DataBean implements Serializable{
        /**
         * ClassName : 分类名称
         * ClassId : 1
         * List : [{"Id":1,"ServiceName":"服务名称","HeadImage":"封面照片","Describe":"服务描述"}]
         */

        private String ClassName;
        private int ClassId;
        private java.util.List<ListBean> List;

        public String getClassName() {
            return ClassName;
        }

        public void setClassName(String ClassName) {
            this.ClassName = ClassName;
        }

        public int getClassId() {
            return ClassId;
        }

        public void setClassId(int ClassId) {
            this.ClassId = ClassId;
        }

        public List<ListBean> getList() {
            return List;
        }

        public void setList(List<ListBean> List) {
            this.List = List;
        }

        public static class ListBean implements Serializable{
            /**
             * Id : 1
             * ServiceName : 服务名称
             * HeadImage : 封面照片
             * Describe : 服务描述
             */

            private int Id;
            private String ServiceName;
            private String HeadImage;
            private String Describe;

            public int getId() {
                return Id;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public String getServiceName() {
                return ServiceName;
            }

            public void setServiceName(String ServiceName) {
                this.ServiceName = ServiceName;
            }

            public String getHeadImage() {
                return HeadImage;
            }

            public void setHeadImage(String HeadImage) {
                this.HeadImage = HeadImage;
            }

            public String getDescribe() {
                return Describe;
            }

            public void setDescribe(String Describe) {
                this.Describe = Describe;
            }
        }
    }
}
