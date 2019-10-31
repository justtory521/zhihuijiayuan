package tendency.hz.zhihuijiayuan.view.set;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.base.Config;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;

/**
 * Created by JasonYao on 2018/4/1.
 */

public class VersionExplainActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_explain);
        ViewUnits.getInstance().setTitleHeight(findViewById(R.id.layout_title));
        String verName = BaseUnits.getInstance().getVerName(this);
        if (Config.IS_BETA) {
            verName += "-1";
        }
        ((TextView) findViewById(R.id.text_version)).setText("版本号：" + verName);
        findViewById(R.id.img_btn_back).setOnClickListener(view -> finish());
    }
}
