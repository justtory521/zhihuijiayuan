package tendency.hz.zhihuijiayuan.bean;

/**
 * Author：Libin on 2020/1/3 0003 10:52
 * Email：1993911441@qq.com
 * Describe：
 */
public class ToastBean {

    /**
     * position : middle
     * message : 假如我是一只鱼
     * duration : 8230
     * iconType : icon-精选拷贝
     * msgColor :
     * bgColor :
     * overlay : true
     */

    private String position;
    private String message;
    private int duration;
    private String iconType;
    private String msgColor;
    private String bgColor;
    private boolean outsideTouchable;

    public boolean isOutsideTouchable() {
        return outsideTouchable;
    }

    public void setOutsideTouchable(boolean outsideTouchable) {
        this.outsideTouchable = outsideTouchable;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getIconType() {
        return iconType;
    }

    public void setIconType(String iconType) {
        this.iconType = iconType;
    }

    public String getMsgColor() {
        return msgColor;
    }

    public void setMsgColor(String msgColor) {
        this.msgColor = msgColor;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

}
