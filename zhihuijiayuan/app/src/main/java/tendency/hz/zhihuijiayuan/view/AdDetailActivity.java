package tendency.hz.zhihuijiayuan.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import tendency.hz.zhihuijiayuan.MainActivity;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.databinding.ActivityBannerDetailsBinding;
import tendency.hz.zhihuijiayuan.units.ConfigUnits;

/**
 * Created by JasonYao on 2018/11/1.
 */
public class AdDetailActivity extends BaseActivity {
    private ActivityBannerDetailsBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_banner_details);

        initView();
    }

    private void initView() {
        mBinding.webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        WebSettings webSettings = mBinding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);  //设置与JS交互权限
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //设置运行JS弹窗
        webSettings.setUserAgentString("-Android");  //设置用户代理
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //启用地理定位
        webSettings.setGeolocationEnabled(true);
        mBinding.webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin,true,true);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });
        mBinding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mBinding.imgLoading.setVisibility(View.GONE);
            }
        });
        mBinding.webView.loadUrl(ConfigUnits.getInstance().getAdUrl());
    }

    public void back(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }
}
