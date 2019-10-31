package tendency.hz.zhihuijiayuan.handlers;

import android.content.Intent;
import android.view.View;

import com.tencent.bugly.beta.Beta;

import tendency.hz.zhihuijiayuan.units.CacheUnits;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.UserUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.set.MessageSetActivity;
import tendency.hz.zhihuijiayuan.view.set.VersionExplainActivity;
import tendency.hz.zhihuijiayuan.view.user.FileManager1Activity;
import tendency.hz.zhihuijiayuan.view.user.LoginActivity;
import tendency.hz.zhihuijiayuan.view.user.ReBindingPhone1Activity;
import tendency.hz.zhihuijiayuan.view.user.ResetPwdActivity;

/**
 * Created by JasonYao on 2018/11/16.
 */
public class SetActivityHandler {
    /**
     * 跳转至消息设置页面
     *
     * @param view
     */
    public void jumpToMessageSet(View view) {
        if (FormatUtils.getInstance().isEmpty(UserUnits.getInstance().getToken())) {
            view.getContext().startActivity(new Intent(view.getContext(), LoginActivity.class));
        } else {
            view.getContext().startActivity(new Intent(view.getContext(), MessageSetActivity.class));
        }
    }

    /**
     * 跳转至关于页面
     *
     * @param view
     */
    public void jumpToAbout(View view) {
        view.getContext().startActivity(new Intent(view.getContext(), VersionExplainActivity.class));
    }


    public void clearCache(View view) {
        CacheUnits.getInstance().clearMessage();
        ViewUnits.getInstance().showToast("清理完毕");
        CacheUnits.getInstance().clearMessageNum();
    }

    /**
     * 跳转至修改密码页面
     *
     * @param view
     */
    public void jumpToResetPwd(View view) {
        if (FormatUtils.getInstance().isEmpty(UserUnits.getInstance().getToken())) {
            view.getContext().startActivity(new Intent(view.getContext(), LoginActivity.class));
        } else {
            view.getContext().startActivity(new Intent(view.getContext(), ResetPwdActivity.class));
        }
    }

    /**
     * 跳转至换绑手机页面
     *
     * @param view
     */
    public void jumpToResetPhone(View view) {
        if (FormatUtils.getInstance().isEmpty(UserUnits.getInstance().getToken())) {
            view.getContext().startActivity(new Intent(view.getContext(), LoginActivity.class));
        } else {
            view.getContext().startActivity(new Intent(view.getContext(), ReBindingPhone1Activity.class));
        }
    }

    /**
     * 跳转至文件管理
     *
     * @param view
     */
    public void jumpToFile(View view) {
        view.getContext().startActivity(new Intent(view.getContext(), FileManager1Activity.class));
    }


    /**
     * 检查更新
     *
     * @param view
     */
    public void checkUpdate(View view) {
        Beta.checkUpgrade();
    }
}
