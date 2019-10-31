package tendency.hz.zhihuijiayuan.units;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.application.MyApplication;
import tendency.hz.zhihuijiayuan.inter.PopWindowOnClickInter;

/**
 * 页面工具类
 * Created by JasonYao on 2018/3/1.
 */

public class ViewUnits {
    private static final String TAG = "ViewUnits--";
    public static ViewUnits mInstance = null;

    //缓存等待框
    private LoadingDialog.Builder mLoadBuilder;
    private static LoadingDialog mDialog;

    //弹出的选项框
    public PopupWindow mPopupWindow; //返回消息框
    public TextView mTextViewLeft, mTextViewRight; //消息框左右按钮
    public TextView mTextViewMsg; //消息文本
    private View mPopBackView;

    private Toast mToast;

    private boolean isReflectedHandler = false;

    private ViewUnits() {

    }

    public static ViewUnits getInstance() {
        if (mInstance == null) {
            synchronized (ViewUnits.class) {
                if (mInstance == null) {
                    mInstance = new ViewUnits();
                }
            }
        }

        return mInstance;
    }

    /**
     * 设置标题栏高度（适配各种异性全面屏、各种状态栏高度）
     *
     * @param view
     */
    public void setTitleHeight(View view) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.height = BaseUnits.getInstance().getStatusBarHeight();
        view.setLayoutParams(layoutParams);
    }

    /**
     * 显示弹吐司
     *
     * @param message
     */
    public void showToast(String message) {

        if (TextUtils.isEmpty(message)) {
            return;
        }

        try {
            if (mToast == null) {
                mToast = Toast.makeText(MyApplication.getInstance().getApplicationContext(), message, Toast.LENGTH_SHORT);
                mToast.setGravity(Gravity.CENTER, 0, 0);
            } else {
                mToast.setText(message);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && Build.VERSION.SDK_INT < Build.VERSION_CODES.O && !isReflectedHandler) {
                reflectTNHandler(mToast);
                //这里为了避免多次反射，使用一个标识来限制
                isReflectedHandler = true;
                return;
            }

            mToast.show();

        } catch (Exception e) {
        }

    }

    /**
     * 显示弹吐司
     *
     * @param message
     */
    public void showToast(String message, Application application) {
        mToast = Toast.makeText(application, message, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);

        if (FormatUtils.getInstance().isEmpty(message)) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && Build.VERSION.SDK_INT < Build.VERSION_CODES.O && !isReflectedHandler) {
            reflectTNHandler(mToast);
            //这里为了避免多次反射，使用一个标识来限制
            isReflectedHandler = true;

            return;
        }

        mToast.show();


    }

    /**
     * 在屏幕中央弹吐司
     *
     * @param message
     */
    public void showMiddleToast(String message) {
        if (FormatUtils.getInstance().isEmpty(message)) {
            return;
        }
        Toast toast = Toast.makeText(MyApplication.getInstance().getBaseContext(),
                message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return
     */
    public int dp2px(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * px转换dp
     */
    public int px2dip(int px) {
        final float scale = MyApplication.getInstance().getResources().getDisplayMetrics().density;
        Log.e(TAG, "密实系数：" + scale);
        return (int) (px / scale + 0.5f);
    }

    /**
     * JS px 转 Android px
     *
     * @param jsPx
     * @return
     */
    public float jsPX2AndroidPX(int jsPx) {
        return jsPx * MyApplication.getInstance().getResources().getDisplayMetrics().density;
    }

    /**
     * 显示等待框
     */
    public void showLoading(Context context, String message) {
        if (((Activity) context).isFinishing()) {
            return;
        }
        if (mLoadBuilder != null) {
            mLoadBuilder = null;
        }

        if (mDialog != null) {
            mDialog = null;
        }

        mLoadBuilder = new LoadingDialog.Builder(context)
                .setMessage(message)
                .setCancelable(true)
                .setCancelOutside(false);
        mDialog = mLoadBuilder.create();
        mDialog.show();
    }

    /**
     * 隐藏等待框
     */
    public void missLoading() {
        if (mDialog == null) {
            return;
        }

        Context context = ((ContextWrapper) mDialog.getContext()).getBaseContext();

        if (context instanceof Activity) {
            if (((Activity) context).isFinishing() || ((Activity) context).isDestroyed())  //如果当前Activity已被销毁，跳出
                return;
        }

        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }

    }

    /**
     * 显示弹出信息框
     *
     * @param context
     * @param view
     * @param message
     */
    public void showPopWindow(Context context, View view, String message, PopWindowOnClickInter onClickInter) {
        if (((Activity) context).isFinishing()) {
            return;
        }
        mPopupWindow = new PopupWindow();
        mPopBackView = LayoutInflater.from(context).inflate(R.layout.popup_msg, null);

        //设置Popup具体控件
        mTextViewLeft = mPopBackView.findViewById(R.id.text_left_msg);
        mTextViewRight = mPopBackView.findViewById(R.id.text_right_msg);
        mTextViewMsg = mPopBackView.findViewById(R.id.text_msg);

        mTextViewMsg.setText(message);

        //设置Popup具体参数
        mPopupWindow.setContentView(mPopBackView);
        mPopupWindow.setWidth(LinearLayoutCompat.LayoutParams.MATCH_PARENT);//设置宽
        mPopupWindow.setHeight(LinearLayoutCompat.LayoutParams.MATCH_PARENT);//设置高
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));
        mPopupWindow.setFocusable(false);//点击空白，popup不自动消失
        mPopupWindow.setTouchable(true);//popup区域可触摸
        mPopupWindow.setOutsideTouchable(true);//非popup区域可触摸

        mTextViewLeft.setOnClickListener(view1 -> onClickInter.leftBtnOnClick());
        mTextViewRight.setOnClickListener(view1 -> onClickInter.rightBtnOnClick());

        if (!mPopupWindow.isShowing()) {
            mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        } else {
            ///如果正在显示等待框，，，则结束掉等待框然后重新显示
            mPopupWindow.dismiss();
            mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        }
    }

    /**
     * 隐藏弹出信息框
     */
    public void missPopView() {
        if (mPopupWindow == null) {
            return;
        }

        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    /**
     * 弹出二维码框
     *
     * @param context
     * @param view
     */
    public void showQrCodePopView(Context context, View view, String uri) {
        if (((Activity) context).isFinishing()) return;
        mPopupWindow = new PopupWindow();
        mPopBackView = LayoutInflater.from(context).inflate(R.layout.layout_pop_img, null);

        //设置Popup具体参数
        mPopupWindow.setContentView(mPopBackView);
        mPopupWindow.setWidth(LinearLayoutCompat.LayoutParams.MATCH_PARENT);//设置宽
        mPopupWindow.setHeight(LinearLayoutCompat.LayoutParams.MATCH_PARENT);//设置高
        mPopupWindow.setFocusable(false);//点击空白，popup不自动消失
        mPopupWindow.setTouchable(true);//popup区域可触摸
        mPopupWindow.setOutsideTouchable(true);//非popup区域可触摸
        ((ImageView) mPopBackView.findViewById(R.id.img_qrcode)).setImageBitmap(QrCodeUnits.createQRCodeWithLogo(uri, 600,
                BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo)));
        mPopBackView.findViewById(R.id.layout_popup).setOnClickListener(view1 -> missQrCodePopView());
        if (!mPopupWindow.isShowing()) {
            mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        } else {
            ///如果正在显示等待框，，，则结束掉等待框然后重新显示
            mPopupWindow.dismiss();
            mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        }
    }

    /**
     * 隐藏二维码框
     */
    public void missQrCodePopView() {
        if (mPopupWindow == null) {
            return;
        }
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    /**
     * dp转换成px
     */
    public int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 防止在android7.x系统会偶发Toast显示异常报BadTokenException
     *
     * @param toast
     */
    private static void reflectTNHandler(Toast toast) {
        try {
            Field tNField = toast.getClass().getDeclaredField("mTN");
            if (tNField == null) {
                return;
            }
            tNField.setAccessible(true);
            Object TN = tNField.get(toast);
            if (TN == null) {
                return;
            }
            Field handlerField = TN.getClass().getDeclaredField("mHandler");
            if (handlerField == null) {
                return;
            }
            handlerField.setAccessible(true);
            handlerField.set(TN, new ProxyTNHandler(TN));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static class ProxyTNHandler extends Handler {
        private Object tnObject;
        private Method handleShowMethod;
        private Method handleHideMethod;

        ProxyTNHandler(Object tnObject) {
            this.tnObject = tnObject;
            try {
                this.handleShowMethod = tnObject.getClass().getDeclaredMethod("handleShow", IBinder.class);
                this.handleShowMethod.setAccessible(true);
                this.handleHideMethod = tnObject.getClass().getDeclaredMethod("handleHide");
                this.handleHideMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    //SHOW
                    IBinder token = (IBinder) msg.obj;
                    if (handleShowMethod != null) {
                        try {
                            handleShowMethod.invoke(tnObject, token);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (WindowManager.BadTokenException e) {
                            //显示Toast时添加BadTokenException异常捕获
                            e.printStackTrace();
                        }
                    }
                    break;
                }

                case 1:
                case 2: {
                    //HIDE
                    if (handleHideMethod != null) {
                        try {
                            handleHideMethod.invoke(tnObject);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }//CANCEL

            }
            super.handleMessage(msg);
        }
    }
}
