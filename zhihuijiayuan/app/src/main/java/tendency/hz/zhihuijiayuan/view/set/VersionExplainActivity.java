package tendency.hz.zhihuijiayuan.view.set;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.base.Config;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.user.AgreementActivity;
import tendency.hz.zhihuijiayuan.view.user.LoginActivity;
import tendency.hz.zhihuijiayuan.view.user.PrivacyStatementActivity;

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

        ((TextView) findViewById(R.id.text_version)).setText("版本号：" + verName+Config.VERSION);
        findViewById(R.id.img_btn_back).setOnClickListener(view -> finish());

        findViewById(R.id.tv_agreement_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VersionExplainActivity.this, AgreementActivity.class));
            }
        });

        findViewById(R.id.tv_privacy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VersionExplainActivity.this, PrivacyStatementActivity.class));
            }
        });
    }
}
