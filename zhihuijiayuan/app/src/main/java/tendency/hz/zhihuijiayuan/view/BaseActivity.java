package tendency.hz.zhihuijiayuan.view;


import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.tencent.bugly.beta.Beta;
import com.umeng.analytics.MobclickAgent;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.StatusBarUtils;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;
import tendency.hz.zhihuijiayuan.widget.NoInternetDialog;

/**
 * Created by JasonYao on 2018/3/19.
 */

public class BaseActivity extends AppCompatActivity implements AllViewInter {
    private static final String TAG = "libin";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //keep screen on while playing audio
        StatusBarUtils.getInstance().setStatusBarFontDark(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (!BaseUnits.getInstance().isLinkNet()) {
            new NoInternetDialog(this).show();
        }
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }



    public void back(View view) {
        finish();
    }



    /**
     * @return 字体大小固定
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    public void onSuccessed(int what, Object object) {

    }

    @Override
    public void onFailed(int what, Object object) {

    }
}


