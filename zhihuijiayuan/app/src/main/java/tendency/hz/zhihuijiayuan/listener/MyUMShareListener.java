package tendency.hz.zhihuijiayuan.listener;

import android.util.Log;

import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import tendency.hz.zhihuijiayuan.inter.ShareResultInter;

/**
 * Created by JasonYao on 2018/9/12.
 */
public class MyUMShareListener implements UMShareListener {
    private static final String TAG = "libin";
    private ShareResultInter mListener;
    private String mCallBack;

    public MyUMShareListener(String callBack, ShareResultInter listener) {
        mCallBack = callBack;
        mListener = listener;
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {
        Log.e(TAG, "分享开始");
    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        mListener.getShareResult(mCallBack, "200", "success");
        Log.e(TAG, "分享成功" + share_media.getName());
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        mListener.getShareResult(mCallBack, "500", throwable.getMessage());
        Log.e(TAG, "分享错误" + share_media.getName() + ":" + throwable.getMessage());
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        mListener.getShareResult(mCallBack, "501", "分享取消");
        Log.e(TAG, "分享取消");
    }
}
