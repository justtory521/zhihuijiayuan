package tendency.hz.zhihuijiayuan;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import tendency.hz.zhihuijiayuan.adapter.MainFragmentPagerAdapter;
import tendency.hz.zhihuijiayuan.bean.Message;
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
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.card.SearchCardActivity;
import tendency.hz.zhihuijiayuan.view.index.ChoiceFragment;
import tendency.hz.zhihuijiayuan.view.index.HomeFragment;
import tendency.hz.zhihuijiayuan.view.index.MeFragment;
import tendency.hz.zhihuijiayuan.view.index.MessageFragment;
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


        new Handler().postDelayed(() -> BaseUnits.getInstance().checkOldPackage(MainActivity.this), 500);
        mBinding.bottomRbMessage.setBadgeOffX(-10);
        mBinding.bottomRbMessage.setBadgeOffY(10);


        if (getIntent().getIntExtra("type", 0) == Request.StartActivityRspCode.PUSH_TOMESSAGELIST_JUMP) {
            new Handler().postDelayed(() -> mBinding.bottomRbMessage.setChecked(true), 200);
        }

        //绑定蓝牙服务
        BluetoothUtils.getInstance().startSerVice(this);
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
    }


    private void initView() {
        mChoiceFragment = new ChoiceFragment();
        mListFragments.add(new HomeFragment());
        mListFragments.add(mChoiceFragment);
        mListFragments.add(new MessageFragment());
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
                List<Message> list = (List<Message>) object;
                CacheUnits.getInstance().setMessageNum(list.size());
                mBinding.bottomRbMessage.setBadgeNumber(CacheUnits.getInstance().getDisplayMessageNum());
                break;
        }
    }

    @Override
    public void onFailed(int what, Object object) {
        switch (what) {
            case NetCode.Personal.getMessage:
                mBinding.bottomRbMessage.setBadgeNumber(CacheUnits.getInstance().getDisplayMessageNum());
                break;
        }
    }

    @Override
    public void process() {
        mChoiceFragment.updateData();
    }

    @Override
    public void clearMessage() {
        mBinding.bottomRbMessage.setBadgeNumber(-1);
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
