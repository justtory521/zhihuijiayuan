package tendency.hz.zhihuijiayuan.units;

import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JasonYao on 2017/6/9.
 */

public abstract class NoHttpInter {

    private final static String TAG = "NoHttpInter--";

    public abstract void respSuccess(JSONObject jsonObjectResult, int what);

    public abstract void respFail(JSONObject jsonObjectResult, int what);

    /**
     * 定义网络请求回调
     */
    public OnResponseListener<String> onResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            String result = response.get().toString();
            try {
                JSONObject jso = new JSONObject(result);
                respSuccess(jso, what);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailed(int what, Response<String> response) {

            respFail(null, what);
        }

        @Override
        public void onFinish(int what) {

        }
    };
}
