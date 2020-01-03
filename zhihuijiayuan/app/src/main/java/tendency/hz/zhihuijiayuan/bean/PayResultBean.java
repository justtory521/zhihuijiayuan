package tendency.hz.zhihuijiayuan.bean;

import org.json.JSONObject;

/**
 * Author：Libin on 2019/12/30 0030 18:10
 * Email：1993911441@qq.com
 * Describe：
 */
public class PayResultBean {
    private String callback;
    private String type;
    private String resultCode;
    private JSONObject source;

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public JSONObject getSource() {
        return source;
    }

    public void setSource(JSONObject source) {
        this.source = source;
    }

    public PayResultBean(String callback, String type, String resultCode, JSONObject source) {
        this.callback = callback;
        this.type = type;
        this.resultCode = resultCode;
        this.source = source;
    }
}
