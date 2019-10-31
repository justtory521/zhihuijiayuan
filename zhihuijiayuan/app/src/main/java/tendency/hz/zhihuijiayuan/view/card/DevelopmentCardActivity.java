package tendency.hz.zhihuijiayuan.view.card;

import android.os.Bundle;
import android.support.annotation.Nullable;

import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/4/17.
 */

public class DevelopmentCardActivity extends BaseActivity implements AllViewInter {
    public static DevelopmentCardActivity mInstances = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInstances = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true);
        }

//        loadUrl(Uri.DEVELOPMENTURL);
    }

    public static DevelopmentCardActivity getInstance() {
        return mInstances;
    }

    @Override
    public void onSuccessed(int what, Object object) {

    }

    @Override
    public void onFailed(int what, Object object) {

    }
}
