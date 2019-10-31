package tendency.hz.zhihuijiayuan.model.modelInter;

import android.content.Intent;
import android.util.Log;

import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import tendency.hz.zhihuijiayuan.application.MyApplication;
import tendency.hz.zhihuijiayuan.units.NoHttpUtil;
import tendency.hz.zhihuijiayuan.units.UserUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.user.LoginActivity;

/**
 * 全局M层继承接口
 * Created by JasonYao on 2017/6/12.
 */

public abstract class AllModelInter {
    private static final String TAG = "libin";
    public static final String tokenKey = "AccessToken";

    /**
     * 网络请求成功M层回调
     *
     * @param what   网络请求标识码
     * @param object 返回对象
     */
    public abstract void rspSuccess(int what, Object object) throws JSONException;

    /**
     * 网络请求失败M层回调
     *
     * @param what   网络请求标识码
     * @param object 返回对象
     */
    public abstract void rspFailed(int what, Object object);

    /**
     * 定义网络请求回调
     */
    public OnResponseListener<Object> onResponseListener = new OnResponseListener<Object>() {
        @Override
        public void onStart(int what) {
            Log.e(TAG, "onStart");
        }

        @Override
        public void onSucceed(int what, Response<Object> response) {
            Log.e(TAG, "onSucceed");
            String result = response.get().toString();
            Log.e(TAG, what + ":" + result);
            try {
                JSONObject jso = new JSONObject(result);
                //获取数据
                String status = jso.getString("Status");
                if (status.equals("2") || status.equals("99")) {
                    rspSuccess(what, jso);  //返回状态码是2，标识网络请求成功
                } else if (status.equals("7")) {
                    ViewUnits.getInstance().showToast("身份认证失败，请重新登录");
                    UserUnits.getInstance().clearUserInfo();
                    Intent intent = new Intent(MyApplication.getInstance(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getInstance().startActivity(intent);
                } else {
                    rspFailed(what, jso.getString("Msg"));  //返回码不是2，标识网络请求失败
                }
            } catch (JSONException e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
                //获取失败，错误
                rspFailed(what, "网络错误!!");

            }
        }

        @Override
        public void onFailed(int what, Response<Object> response) {
            rspFailed(what, response.toString());
        }

        @Override
        public void onFinish(int what) {
        }
    };
}




