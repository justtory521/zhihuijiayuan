package tendency.hz.zhihuijiayuan.bean;

import org.json.JSONObject;

/**
 * Created by JasonYao on 2018/10/22.
 */
public class JSCallBack {
    public String status;
    public JSONObject data;
    private String msg;

    @Override
    public String toString() {
        return "JSCallBack{" +
                "status='" + status + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
