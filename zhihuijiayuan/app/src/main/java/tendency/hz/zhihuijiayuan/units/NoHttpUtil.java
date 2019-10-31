package tendency.hz.zhihuijiayuan.units;

import android.util.Log;

import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadQueue;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tendency.hz.zhihuijiayuan.bean.base.Field;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Uri;

/**
 * Created by JasonYao on 2017/6/9.
 */

public class NoHttpUtil {
    private static final String TAG = "NoHttpUtil---";
    private volatile static NoHttpUtil mNoHttpUtil;

    private RequestQueue mRequestQueue;
    public DownloadQueue mDownloadQueue;

    private NoHttpUtil() {
        mRequestQueue = NoHttp.newRequestQueue();
        mDownloadQueue = NoHttp.newDownloadQueue();
    }

    public static NoHttpUtil getNoHttpUtil() {
        if (mNoHttpUtil == null) {
            synchronized (NoHttpUtil.class) {
                if (mNoHttpUtil == null) {
                    mNoHttpUtil = new NoHttpUtil();
                }
            }
        }
        return mNoHttpUtil;
    }

    public static void post(int what, String Url, OnResponseListener listener, List<Param> paramList) {

        Request<String> request = NoHttp.createStringRequest(Url, RequestMethod.POST);
        request.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);
        Map<String, String> params = new HashMap<>();

        if (paramList != null) {
            for (Param param : paramList) {
                params.put(param.getKey(), String.valueOf(param.getValue()));
            }
        }

        request.addHeader("Authorization", "Bearer " + UserUnits.getInstance().getToken());
        request.add(params);

        getNoHttpUtil().mRequestQueue.add(what, request, listener);
    }

    public static void get(int what, String url, OnResponseListener listener, List<Param> paramList) {

        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);

        Map<String, String> params = new HashMap<>();

        if (paramList != null) {
            for (Param param : paramList) {
                params.put(param.getKey(), String.valueOf(param.getValue()));
            }
        }

        request.addHeader("Authorization", "Bearer " + UserUnits.getInstance().getToken());
        request.add(params);

        getNoHttpUtil().mRequestQueue.add(what, request, listener);
    }

    public static void postImage(int what, String url, OnResponseListener listener, List<Param> paramList) {
        Request<byte[]> request = NoHttp.createByteArrayRequest(url, RequestMethod.POST);
        request.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);
        Map<String, String> params = new HashMap<>();
        if (paramList != null) {
            for (Param param : paramList) {
                if (param.getKey().equals("pic")) {
                    params.put(param.getKey(), new String((byte[]) param.getValue()));
                }
                params.put(param.getKey(), String.valueOf(param.getValue()));
            }
        }

        request.addHeader("Author", "nohttp_sample");
        request.add(params);

        getNoHttpUtil().mRequestQueue.add(what, request, listener);
    }



    /**
     * 下载
     *
     * @param path
     * @param name
     * @param isRange     是否断点续传
     * @param isDeleteOld 如果发现文件已经存在是否删除后重新下载
     * @param adImg
     * @param adUrl
     */
    public static void downLoad(String path, String name, boolean isRange, boolean isDeleteOld, final String adImg, String adUrl) {
        DownloadRequest downloadRequest = NoHttp.createDownloadRequest(adImg, path, name, isRange, isDeleteOld);
        mNoHttpUtil.mDownloadQueue.add(NetCode.Set.downLoad, downloadRequest, new DownloadListener() {
            @Override
            public void onDownloadError(int what, Exception exception) {
                Log.e(TAG, exception.toString());
            }

            @Override
            public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {
            }

            @Override
            public void onProgress(int what, int progress, long fileCount, long speed) {
            }

            @Override
            public void onFinish(int what, String filePath) {
                Log.e(TAG, "下载完成");
                Log.e(TAG, adImg);
                Log.e(TAG, adUrl);
                ConfigUnits.getInstance().setAdImg(adImg);  //缓存广告图片
                ConfigUnits.getInstance().setAdUrl(adUrl);  //缓存广告Url
            }

            @Override
            public void onCancel(int what) {

            }
        });
    }

    public static class Param {
        String key;
        Object value;

        public Param(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Param{" +
                    "key='" + key + '\'' +
                    ", value=" + value +
                    '}';
        }
    }


}
