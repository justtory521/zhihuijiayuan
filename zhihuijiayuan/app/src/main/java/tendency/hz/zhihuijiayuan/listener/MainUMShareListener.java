package tendency.hz.zhihuijiayuan.listener;

import android.util.Log;

import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import tendency.hz.zhihuijiayuan.units.ViewUnits;

/**
 * Created by JasonYao on 2018/9/12.
 */
public class MainUMShareListener implements UMShareListener {
    private static final String TAG = "MyUMShareListener---";

    @Override
    public void onStart(SHARE_MEDIA share_media) {
        Log.e(TAG, "分享开始");
    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        ViewUnits.getInstance().showToast("分享成功");
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        Log.e(TAG, "分享错误" + share_media.getName() + ":" + throwable.getMessage());
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        ViewUnits.getInstance().showToast("分享取消");
    }
}
