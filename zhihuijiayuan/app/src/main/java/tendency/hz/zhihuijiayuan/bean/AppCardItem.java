package tendency.hz.zhihuijiayuan.bean;

import java.io.Serializable;

/**
 * Created by JasonYao on 2018/4/20.
 */

public class AppCardItem implements Serializable {
    private String AppID;
    private String CardID;
    private String AppName;
    private String AppInfo;
    private String AndroidAppID;
    private String AndroidStartUrl;
    private String AndroidDownUrl;
    private String IOSAppID;
    private String IOSStartUrl;
    private String IOSDownUrl;

    @Override
    public String toString() {
        return "AppCardItem{" +
                "AppID='" + AppID + '\'' +
                ", CardID='" + CardID + '\'' +
                ", AppName='" + AppName + '\'' +
                ", AppInfo='" + AppInfo + '\'' +
                ", AndroidAppID='" + AndroidAppID + '\'' +
                ", AndroidStartUrl='" + AndroidStartUrl + '\'' +
                ", AndroidDownUrl='" + AndroidDownUrl + '\'' +
                ", IOSAppID='" + IOSAppID + '\'' +
                ", IOSStartUrl='" + IOSStartUrl + '\'' +
                ", IOSDownUrl='" + IOSDownUrl + '\'' +
                '}';
    }

    public String getAppID() {
        return AppID;
    }

    public void setAppID(String appID) {
        AppID = appID;
    }

    public String getCardID() {
        return CardID;
    }

    public void setCardID(String cardID) {
        CardID = cardID;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public String getAppInfo() {
        return AppInfo;
    }

    public void setAppInfo(String appInfo) {
        AppInfo = appInfo;
    }

    public String getAndroidAppID() {
        return AndroidAppID;
    }

    public void setAndroidAppID(String androidAppID) {
        AndroidAppID = androidAppID;
    }

    public String getAndroidStartUrl() {
        return AndroidStartUrl;
    }

    public void setAndroidStartUrl(String androidStartUrl) {
        AndroidStartUrl = androidStartUrl;
    }

    public String getAndroidDownUrl() {
        return AndroidDownUrl;
    }

    public void setAndroidDownUrl(String androidDownUrl) {
        AndroidDownUrl = androidDownUrl;
    }

    public String getIOSAppID() {
        return IOSAppID;
    }

    public void setIOSAppID(String IOSAppID) {
        this.IOSAppID = IOSAppID;
    }

    public String getIOSStartUrl() {
        return IOSStartUrl;
    }

    public void setIOSStartUrl(String IOSStartUrl) {
        this.IOSStartUrl = IOSStartUrl;
    }

    public String getIOSDownUrl() {
        return IOSDownUrl;
    }

    public void setIOSDownUrl(String IOSDownUrl) {
        this.IOSDownUrl = IOSDownUrl;
    }
}
