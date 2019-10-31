package tendency.hz.zhihuijiayuan.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.view.set.SetInternetTipActivity;

/**
 * Created by JasonYao on 2018/5/8.
 */

public class NoInternetDialog extends Dialog implements View.OnClickListener {
    private Context mContext;

    public NoInternetDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public NoInternetDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
    }

    public NoInternetDialog(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected NoInternetDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pop_no_net);
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = WindowManager.LayoutParams.WRAP_CONTENT; //设置dialog的宽度为当前手机屏幕的宽度
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(p);

        setCanceledOnTouchOutside(true);
        initView();

        findViewById(R.id.btn_go_set).setOnClickListener(view -> {
            Intent intent=new Intent(view.getContext(), SetInternetTipActivity.class);
            view.getContext().startActivity(intent);
        });
    }

    private void initView() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, boolean confirm);
    }
}
