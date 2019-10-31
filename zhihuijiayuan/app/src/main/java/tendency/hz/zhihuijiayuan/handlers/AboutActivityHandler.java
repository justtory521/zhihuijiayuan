package tendency.hz.zhihuijiayuan.handlers;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.tencent.bugly.beta.Beta;

import tendency.hz.zhihuijiayuan.view.set.VersionExplainActivity;

/**
 * Created by JasonYao on 2018/6/28.
 */
public class AboutActivityHandler {
    /**
     * 跳转至版本页面
     *
     * @param view
     */
    public void jumpToVersion(View view) {
        Intent intent = new Intent(view.getContext(), VersionExplainActivity.class);
        view.getContext().startActivity(intent);
    }

    /**
     * 检查版本
     *
     * @param view
     */
    public void checkVersion(View view) {
        Beta.checkUpgrade();
    }

    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        ((Activity) view.getContext()).finish();
    }


}
