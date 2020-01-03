package tendency.hz.zhihuijiayuan;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.ui.UILifecycleListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.jpush.android.api.JPushInterface;
import tendency.hz.zhihuijiayuan.application.MyApplication;
import tendency.hz.zhihuijiayuan.bean.base.Config;
import tendency.hz.zhihuijiayuan.bean.base.NetCode;
import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.databinding.ActivityMain2Binding;
import tendency.hz.zhihuijiayuan.inter.FragmentInteraction;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.presenter.prenInter.PersonalPrenInter;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.BluetoothUtils;
import tendency.hz.zhihuijiayuan.units.CacheUnits;
import tendency.hz.zhihuijiayuan.units.FragmentTabUtils;
import tendency.hz.zhihuijiayuan.units.LogUtils;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.card.SearchCardActivity;
import tendency.hz.zhihuijiayuan.view.index.ChoiceFragment;
import tendency.hz.zhihuijiayuan.view.index.HomeFragment;
import tendency.hz.zhihuijiayuan.view.index.MeFragment;
import tendency.hz.zhihuijiayuan.view.index.NewMessageFragment;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class MainActivity extends BaseActivity implements AllViewInter, FragmentInteraction {
    private static final String TAG = "MainActivity---";
    private ActivityMain2Binding mBinding;

    //碎片list
    private List<Fragment> mListFragments = new ArrayList<>();


    private boolean isQuit = false; //标记两次点击返回键退出APP

    private PersonalPrenInter mPersonalPreInter;
    //微信订阅返回
    private boolean isWechatReap;
    private ChoiceFragment mChoiceFragment;
    private FragmentTabUtils mFragmentTabUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main2);
        EventBus.getDefault().register(this);
        mPersonalPreInter = new PersonalPrenImpl(this);

        //初始化默认显示页面
        initView();

//        new Handler().postDelayed(() -> BaseUnits.getInstance().checkOldPackage(MainActivity.this), 500);
        mBinding.bottomRbMessage.setBadgeOffX(-10);
        mBinding.bottomRbMessage.setBadgeOffY(10);


        if (getIntent().getIntExtra("type", 0) == Request.StartActivityRspCode.PUSH_TOMESSAGELIST_JUMP) {
            new Handler().postDelayed(() -> mBinding.bottomRbMessage.setChecked(true), 200);
        }

        //绑定蓝牙服务
        BluetoothUtils.getInstance().startSerVice(this);

        checkUpdate();

    }


    /**
     * 检测更新
     */
    private void checkUpdate() {
        Beta.upgradeDialogLayoutId = R.layout.layout_update_popup;
        Beta.tipsDialogLayoutId = R.layout.dialog_tips;
        Beta.strNetworkTipsCancelBtn = "";
        Beta.strUpgradeDialogCancelBtn = "     ";
        Beta.initDelay = 2 * 1000;
        Beta.canShowUpgradeActs.add(MainActivity.class);

        Beta.upgradeDialogLifecycleListener = new UILifecycleListener<UpgradeInfo>() {
            @Override
            public void onCreate(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d("libin", "onResume111"+upgradeInfo.upgradeType);
                // 注：可通过这个回调方式获取布局的控件，如果设置了id，可通过findViewById方式获取，如果设置了tag，可以通过findViewWithTag，具体参考下面例子:

                // 通过id方式获取控件

                ImageView ivCancel = (ImageView) view.findViewById(R.id.iv_cancel_update);

                // 更多的操作：比如设置控件的点击事件
                ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (upgradeInfo.upgradeType == 2){
                            LogUtils.log("退出应用");
                            finish();
                            System.exit(0);
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    }
                });
            }

            @Override
            public void onStart(Context context, View view, UpgradeInfo upgradeInfo) {

            }

            @Override
            public void onResume(Context context, View view, UpgradeInfo upgradeInfo) {



            }

            @Override
            public void onPause(Context context, View view, UpgradeInfo upgradeInfo) {

            }

            @Override
            public void onStop(Context context, View view, UpgradeInfo upgradeInfo) {

            }

            @Override
            public void onDestroy(Context context, View view, UpgradeInfo upgradeInfo) {

            }

        };

        Bugly.init(MyApplication.getInstance(), Config.BUGLY_APPID, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPersonalPreInter.getMessage(NetCode.Personal.getMessage);

        if (isWechatReap) {
            isWechatReap = false;

            new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                    .setTitle("提示")
                    .setMessage("是否打开微信，接受消息通知")
                    .setIcon(R.mipmap.logo)
                    .setCancelable(true)
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (BaseUnits.getInstance().isApkInstalled(MainActivity.this, "com.tencent.mm")) {
                                Intent intent = new Intent();
                                ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                                intent.setAction(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setComponent(cmp);
                                startActivity(intent);
                            } else {
                                ViewUnits.getInstance().showToast("当前手机未安装微信");
                            }
                        }
                    }).create().show();
        }

        mBinding.bottomRbMessage.setBadgeNumber(CacheUnits.getInstance().getUnreadCounts());

        //每次打开APP都重新设置一下推送别名
        JPushInterface.setAlias(this, new Random().nextInt(900) + 100, BaseUnits.getInstance().getPhoneKey());
    }


    private void initView() {
        mChoiceFragment = new ChoiceFragment();
        mListFragments.add(new HomeFragment());
        mListFragments.add(mChoiceFragment);
        mListFragments.add(new NewMessageFragment());
        mListFragments.add(new MeFragment());
        mFragmentTabUtils =  new FragmentTabUtils(getSupportFragmentManager(), mListFragments, R.id.fl_main, mBinding.bottomRgMenu);

    }


    public void goAddCard(View view) {
        Intent intent = new Intent(this, SearchCardActivity.class);
        intent.putExtra("ThemeVal", "");
        startActivity(intent);
    }

    public void goMessageIndex(View view) {
        mFragmentTabUtils.setCurrentFragment(2);
    }

    /**
     * 双击退出
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isQuit) {
                finish();
                System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());
                return true;
            } else {
                ViewUnits.getInstance().showToast("再按一次退出应用程序");
                isQuit = true;

                new Handler().postDelayed(() -> isQuit = false, 2000);
                return false;
            }
        }

        return false;
    }

    @Override
    public void onSuccessed(int what, Object object) {
        switch (what) {
            case NetCode.Personal.getMessage:
                CacheUnits.getInstance().setMessageNum((Integer) object);
                mBinding.bottomRbMessage.setBadgeNumber(CacheUnits.getInstance().getUnreadCounts());
                break;
        }
    }

    @Override
    public void onFailed(int what, Object object) {
        switch (what) {
            case NetCode.Personal.getMessage:
                mBinding.bottomRbMessage.setBadgeNumber(CacheUnits.getInstance().getUnreadCounts());
                break;
        }
    }

    @Override
    public void process() {
        mChoiceFragment.updateData();
    }

    @Override
    public void clearMessage() {
        mBinding.bottomRbMessage.setBadgeNumber(CacheUnits.getInstance().getUnreadCounts());
    }



    /**
     * 微信订阅回调
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void wechatResp(String msg) {
        if (msg.equals("wechat_resp")) {
            isWechatReap = true;
        }
    }

    /**
     * @param outState fragment重叠
     */
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }



    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        BluetoothUtils.getInstance().unbindService(this);
        super.onDestroy();
    }
}
