package tendency.hz.zhihuijiayuan.bean;

/**
 * Author：Libin on 2019/12/30 0030 17:03
 * Email：1993911441@qq.com
 * Describe：
 */
public class LoginResultBean {
    private String callback;

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public LoginResultBean(String callback) {
        this.callback = callback;
    }
}
