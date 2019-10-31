package tendency.hz.zhihuijiayuan.bean;

/**
 * Author：Libin on 2019/6/13 16:34
 * Description：
 */
public class EventBusBean {
    private boolean isSuccess;
    private String openId;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getOpenId() {
        return openId;
    }

    public EventBusBean() {
    }

    public EventBusBean(boolean isSuccess, String openId) {
        this.isSuccess = isSuccess;
        this.openId = openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
