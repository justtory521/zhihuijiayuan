package tendency.hz.zhihuijiayuan.view.card;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.databinding.ActivityBannerDetailsBinding;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/9/28.
 */
public class BannerDetailsActivity extends BaseActivity implements AllViewInter {
    private ActivityBannerDetailsBinding mBinding;
    private String mPostion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_banner_details);

        initData();

        initView();
    }

    private void initData() {
        mPostion = super.getIntent().getStringExtra("img");
    }

    private void initView() {
        mBinding.webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        WebSettings webSettings = mBinding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);  //设置与JS交互权限
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //设置运行JS弹窗
        webSettings.setUserAgentString(webSettings.getUserAgentString() + "-Android");  //设置用户代理
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //启用地理定位
        webSettings.setGeolocationEnabled(true);
        mBinding.webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, true);
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
        mBinding.webView.loadUrl(mPostion);
    }

    @Override
    public void onSuccessed(int what, Object object) {

    }

    @Override
    public void onFailed(int what, Object object) {

    }
}
