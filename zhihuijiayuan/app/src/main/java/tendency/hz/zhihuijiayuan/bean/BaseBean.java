package tendency.hz.zhihuijiayuan.bean;

import java.io.Serializable;

/**
 * Author：Libin on 2019/5/7 11:36
 * Email：1993911441@qq.com
 * Describe：
 */
public class BaseBean <T> implements Serializable {

    private String status;
    private T data;
    private String msg;



    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "status='" + status + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
