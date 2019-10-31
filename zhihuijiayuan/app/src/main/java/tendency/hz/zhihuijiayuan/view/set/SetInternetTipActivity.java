package tendency.hz.zhihuijiayuan.view.set;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.databinding.ActivitySetInternetTipBinding;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;

/**
 * Created by JasonYao on 2019/2/22.
 */
public class SetInternetTipActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySetInternetTipBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_set_internet_tip);
        ViewUnits.getInstance().setTitleHeight(mBinding.layoutTitle);
    }


    public void goBack(View view) {
        finish();
    }
}
