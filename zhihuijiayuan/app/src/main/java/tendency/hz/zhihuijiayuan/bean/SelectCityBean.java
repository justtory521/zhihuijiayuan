package tendency.hz.zhihuijiayuan.bean;

/**
 * Author：Libin on 2019/12/30 0030 17:49
 * Email：1993911441@qq.com
 * Describe：
 */
public class SelectCityBean {
    private String callback;
    private String cityCode;
    private String cityName;

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public SelectCityBean(String callback, String cityCode, String cityName) {
        this.callback = callback;
        this.cityCode = cityCode;
        this.cityName = cityName;
    }
}
