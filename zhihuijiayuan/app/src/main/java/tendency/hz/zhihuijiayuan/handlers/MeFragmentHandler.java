package tendency.hz.zhihuijiayuan.handlers;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.tencent.mm.opensdk.modelbiz.SubscribeMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import tendency.hz.zhihuijiayuan.bean.base.App;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.UserUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.set.FeedBackActivity;
import tendency.hz.zhihuijiayuan.view.set.IndentityValidatedActivity;
import tendency.hz.zhihuijiayuan.view.set.SetActivity;
import tendency.hz.zhihuijiayuan.view.set.ValidateActivity;
import tendency.hz.zhihuijiayuan.view.user.LoginActivity;
import tendency.hz.zhihuijiayuan.view.user.PersonalProfileActivity;
import tendency.hz.zhihuijiayuan.view.user.ResetPwdActivity;
import tendency.hz.zhihuijiayuan.view.user.UserCreditActivity;

/**
 * 我的页面，相关点击跳转事件
 * Created by JasonYao on 2018/11/16.
 */
public class MeFragmentHandler {
    private static final String TAG = "MeFragmentHandler---";

    /**
     * 跳转至登录页面
     *
     * @param view
     */
    public void jumpToLogin(View view) {
        Intent intent = new Intent(view.getContext(), LoginActivity.class);
        view.getContext().startActivity(intent);
    }

    /**
     * 跳转至个人页面
     *
     * @param view
     */
    public void jumpToPerson(View view) {
        if (FormatUtils.getInstance().isEmpty(UserUnits.getInstance().getToken())) {
            Intent intent = new Intent(view.getContext(), LoginActivity.class);
            view.getContext().startActivity(intent);
            return;
        }
        Intent intent = new Intent(view.getContext(), PersonalProfileActivity.class);
        view.getContext().startActivity(intent);
    }

    /**
     * 跳转至身份页面
     *
     * @param view
     */
    public void jumpToIndentity(View view) {
        if (FormatUtils.getInstance().isEmpty(UserUnits.getInstance().getToken())) {
            Intent intent = new Intent(view.getContext(), LoginActivity.class);
            view.getContext().startActivity(intent);
            return;
        }

        if (FormatUtils.getInstance().isEmpty(UserUnits.getInstance().getStatus())) {  //没有审核状态
            view.getContext().startActivity(new Intent(view.getContext(), ValidateActivity.class));
            return;
        }

        if (UserUnits.getInstance().getStatus().equals("1") || UserUnits.getInstance().getStatus().equals("4")) {  //未审核、审核未通过
            view.getContext().startActivity(new Intent(view.getContext(), ValidateActivity.class));
            return;
        }

        if (UserUnits.getInstance().getStatus().equals("2")) {  //审核中
            ViewUnits.getInstance().showToast("审核中，请耐心等待");
            return;
        }

        if (UserUnits.getInstance().getStatus().equals("3")) { //审核通过
            view.getContext().startActivity(new Intent(view.getContext(), IndentityValidatedActivity.class));
            return;
        }

        view.getContext().startActivity(new Intent(view.getContext(), ValidateActivity.class));
    }

    /**
     * 跳转至智慧积分页面
     *
     * @param view
     */
    public void jumpToUserCredit(View view) {
        if (FormatUtils.getInstance().isEmpty(UserUnits.getInstance().getToken())) {
            Intent intent = new Intent(view.getContext(), LoginActivity.class);
            view.getContext().startActivity(intent);
            return;
        }
        Intent intent = new Intent(view.getContext(), UserCreditActivity.class);
        view.getContext().startActivity(intent);
    }

    /**
     * 跳转至修改密码页面
     *
     * @param view
     */
    public void jumpToResetPwd(View view) {
        if (FormatUtils.getInstance().isEmpty(UserUnits.getInstance().getToken())) {
            Intent intent = new Intent(view.getContext(), LoginActivity.class);
            view.getContext().startActivity(intent);
            return;
        }
        Intent intent = new Intent(view.getContext(), ResetPwdActivity.class);
        view.getContext().startActivity(intent);
    }

    /**
     * 跳转至意见反馈页面
     *
     * @param view
     */
    public void jumpToFeedBack(View view) {
        Intent intent = new Intent(view.getContext(), FeedBackActivity.class);
        view.getContext().startActivity(intent);
    }

    /**
     * 跳转至设置页面
     *
     * @param view
     */
    public void jumpToSet(View view) {
        Intent intent = new Intent(view.getContext(), SetActivity.class);
        view.getContext().startActivity(intent);
    }

    public void jumpToGZH(View view) {
        if (FormatUtils.getInstance().isEmpty(UserUnits.getInstance().getToken())) {
            Intent intent = new Intent(view.getContext(), LoginActivity.class);
            view.getContext().startActivity(intent);
            return;
        }

        if (!UMShareAPI.get(view.getContext()).isInstall((Activity) view.getContext(), SHARE_MEDIA.WEIXIN)) {
            ViewUnits.getInstance().showToast("您未安装微信客户端");
            return;
        }

        Log.e(TAG, "jumpToGZH");
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(view.getContext(), App.Pay.WX_APP_ID);
        iwxapi.registerApp(App.Pay.WX_APP_ID);
        SubscribeMessage.Req req = new SubscribeMessage.Req();
        req.scene = 999;
        req.templateID = "5SwMB1T-YD1xPxii3-MJ-UhGwa1urczBzUvSMxTgI2w";
        req.reserved = "zhihuijiayuan";
        iwxapi.sendReq(req);

    }
}
