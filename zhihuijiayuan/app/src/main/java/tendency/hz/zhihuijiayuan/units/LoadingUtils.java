package tendency.hz.zhihuijiayuan.units;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.text.TextUtils;

import java.util.Timer;
import java.util.TimerTask;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.ToastBean;
import tendency.hz.zhihuijiayuan.widget.ToastDialog;

/**
 * Author：Libin on 2020/1/3 0003 11:39
 * Email：1993911441@qq.com
 * Describe：
 */
public class LoadingUtils {
    private static LoadingUtils mInstance;

    private ToastDialog.Builder mLoadBuilder;
    private ToastDialog mDialog;


    private LoadingUtils() {
    }

    public static LoadingUtils getInstance() {
        if (mInstance == null) {
            synchronized (LoadingUtils.class) {
                if (mInstance == null) {
                    mInstance = new LoadingUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 显示toast
     */
    public void showToast(Context context, ToastBean toastBean) {
        dismiss();

        mLoadBuilder = new ToastDialog.Builder(context);

        mLoadBuilder.setMessage(toastBean.getMessage());
        //显示位置
        if (TextUtils.isEmpty(toastBean.getPosition())) {
            mLoadBuilder.setPosition(0);
        } else {
            switch (toastBean.getPosition()) {
                case "top":
                    mLoadBuilder.setPosition(1);
                    break;
                case "bottom":
                    mLoadBuilder.setPosition(2);
                    break;
                default:
                    mLoadBuilder.setPosition(0);
                    break;
            }
        }

        //显示时间
        int duration = 2000;
        if (!TextUtils.isEmpty(toastBean.getDuration())) {
            duration = Integer.parseInt(toastBean.getDuration());
        }

        //显示logo
        int resource = FormatUtils.getInstance().getResource("mipmap", toastBean.getIconType());
        mLoadBuilder.setResource(resource);
        mLoadBuilder.setType(1);

        //文本颜色
        if (!TextUtils.isEmpty(toastBean.getMsgColor())) {
            mLoadBuilder.setTextColor(FormatUtils.getInstance().colorTo6Color(toastBean.getMsgColor()));
        }

        //背景颜色
        if (!TextUtils.isEmpty(toastBean.getBgColor())) {
            mLoadBuilder.setBgColor(FormatUtils.getInstance().colorTo6Color(toastBean.getBgColor()));
        }

        //外部是否可点击
        mLoadBuilder.setCancelOutside(toastBean.isOutsideTouchable());
        mLoadBuilder.setCancelable(true);

        mDialog = mLoadBuilder.create();
        mDialog.show();

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        }, duration);

        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                t.cancel();
            }
        });
    }


    /**
     * 显示等待框
     */
    public void showLoading(Context context, ToastBean toastBean) {
        dismiss();

        mLoadBuilder = new ToastDialog.Builder(context);


        //显示logo
        mLoadBuilder.setType(2);
        //文本
        mLoadBuilder.setMessage(toastBean.getMessage());

        //文本颜色
        if (!TextUtils.isEmpty(toastBean.getMsgColor())) {
            mLoadBuilder.setTextColor(FormatUtils.getInstance().colorTo6Color(toastBean.getMsgColor()));
        }

        //背景颜色
        if (!TextUtils.isEmpty(toastBean.getBgColor())) {
            mLoadBuilder.setBgColor(FormatUtils.getInstance().colorTo6Color(toastBean.getBgColor()));
        }

        mLoadBuilder.setCancelable(false);
        mLoadBuilder.setCancelOutside(false);
        mDialog = mLoadBuilder.create();
        mDialog.show();


    }


    /**
     * 隐藏等待框
     */
    public void dismiss() {
        if (mDialog == null || !mDialog.isShowing()) {
            return;
        }

        Context context = ((ContextWrapper) mDialog.getContext()).getBaseContext();

        if (context instanceof Activity) {
            if (((Activity) context).isFinishing())  //如果当前Activity已被销毁，跳出
                return;
        }

        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
