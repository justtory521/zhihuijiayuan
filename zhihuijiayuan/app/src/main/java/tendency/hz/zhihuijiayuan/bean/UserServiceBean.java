package tendency.hz.zhihuijiayuan.bean;

import java.util.List;

/**
 * Author：Libin on 2019/5/15 13:06
 * Email：1993911441@qq.com
 * Describe：
 */
public class UserServiceBean {

    /**
     * Status : 2
     * Msg : 成功
     * Data : {"TotalValuation":29.8,"List":[{"Id":1,"ServiceName":"测试服务","Describe":"服务描述","HeadImage":"/Upload/2018/11/07/b72559a9-e17e-477c-9ba4-e6afc8771ca4.jpg","Valuation":9.9,"Num":1},{"Id":2,"ServiceName":"测试服务2","Describe":"服务描述2","HeadImage":"/Upload/2018/11/07/b72559a9-e17e-477c-9ba4-e6afc8771ca4.jpg","Valuation":19.9,"Num":1}]}
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
         * TotalValuation : 29.8
         * List : [{"Id":1,"ServiceName":"测试服务","Describe":"服务描述","HeadImage":"/Upload/2018/11/07/b72559a9-e17e-477c-9ba4-e6afc8771ca4.jpg","Valuation":9.9,"Num":1},{"Id":2,"ServiceName":"测试服务2","Describe":"服务描述2","HeadImage":"/Upload/2018/11/07/b72559a9-e17e-477c-9ba4-e6afc8771ca4.jpg","Valuation":19.9,"Num":1}]
         */

        private Double TotalValuation;
        private java.util.List<ListBean> List;

        public Double getTotalValuation() {
            return TotalValuation;
        }

        public void setTotalValuation(Double TotalValuation) {
            this.TotalValuation = TotalValuation;
        }

        public List<ListBean> getList() {
            return List;
        }

        public void setList(List<ListBean> List) {
            this.List = List;
        }

        public static class ListBean {
            /**
             * Id : 1
             * ServiceName : 测试服务
             * Describe : 服务描述
             * HeadImage : /Upload/2018/11/07/b72559a9-e17e-477c-9ba4-e6afc8771ca4.jpg
             * Valuation : 9.9
             * Num : 1
             */

            private int Id;
            private String ServiceName;
            private String Describe;
            private String HeadImage;
            private Double Valuation;
            private Integer Num;

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

            public String getDescribe() {
                return Describe;
            }

            public void setDescribe(String Describe) {
                this.Describe = Describe;
            }

            public String getHeadImage() {
                return HeadImage;
            }

            public void setHeadImage(String HeadImage) {
                this.HeadImage = HeadImage;
            }

            public Double getValuation() {
                return Valuation;
            }

            public void setValuation(Double Valuation) {
                this.Valuation = Valuation;
            }

            public Integer getNum() {
                return Num;
            }

            public void setNum(Integer Num) {
                this.Num = Num;
            }
        }
    }
}
