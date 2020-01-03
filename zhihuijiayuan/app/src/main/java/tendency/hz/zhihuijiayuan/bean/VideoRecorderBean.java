package tendency.hz.zhihuijiayuan.bean;

/**
 * Author：Libin on 2019/12/30 0030 18:00
 * Email：1993911441@qq.com
 * Describe：
 */
public class VideoRecorderBean {
    private String callback;
    private String url;

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public VideoRecorderBean(String callback, String url) {
        this.callback = callback;
        this.url = url;
    }
}
