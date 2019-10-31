package tendency.hz.zhihuijiayuan.inter;

import org.json.JSONObject;

/**
 * Created by JasonYao on 2018/7/27.
 */
public interface PayResultInter {

    /**
     * 获取支付结果
     *
     * @param type
     * @param resultCode
     * @param message
     */
    void getPayResult(String type, String resultCode, JSONObject message, String callBack);
}
