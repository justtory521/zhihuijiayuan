package tendency.hz.zhihuijiayuan.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import tendency.hz.zhihuijiayuan.MainActivity;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.databinding.ActivityAdBinding;
import tendency.hz.zhihuijiayuan.units.ConfigUnits;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/11/1.
 */
public class AdActivity extends BaseActivity implements AllViewInter {
    private ActivityAdBinding mBinding;

    /**
     * 记时对象
     */
    private CountDownTimer mTimer = new CountDownTimer(3 * 1000, 1000) {
        @Override
        public void onTick(long l) {
            if (mBinding.textTime !=null){
                mBinding.textTime.setText("跳过广告 " + l / 1000 + "S");
            }

        }

        @Override
        public void onFinish() {
            startActivity(new Intent(AdActivity.this, MainActivity.class));
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_ad);
        mBinding.imgAd.setImageURI(Uri.parse(tendency.hz.zhihuijiayuan.bean.base.Uri.tdrPath + "/index.png"));
        mTimer.start();
    }

    public void goMain(View view) {
        mTimer.cancel();
        mTimer.onFinish();
        Intent intent = new Intent(AdActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

    public void goAd(View view) {
        if (FormatUtils.getInstance().isEmpty(ConfigUnits.getInstance().getAdUrl())) {
            return;
        }

        mTimer.cancel();
        mTimer.onFinish();
        startActivity(new Intent(AdActivity.this, AdDetailActivity.class));
        finish();
    }

    @Override
    public void onSuccessed(int what, Object object) {

    }

    @Override
    public void onFailed(int what, Object object) {

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            //do something.
            return true;
        }else {
            return super.dispatchKeyEvent(event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding=null;
        mTimer=null;
    }
}
