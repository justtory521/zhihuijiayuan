package tendency.hz.zhihuijiayuan.units;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.widget.BadgeRadioButton;

/**
 * Author：Libin on 2019/7/6 17:43
 * Description：
 */
public class FragmentTabUtils implements RadioGroup.OnCheckedChangeListener {
    private List<Fragment> fragmentList; // 一个tab页面对应一个Fragment
    private RadioGroup rgs; // 用于切换tab
    private FragmentManager fragmentManager; // Fragment所属的Activity
    private int flContainerId; // Activity中当前fragment的区域的id

    /**
     * @param fragmentManager
     * @param fragmentList
     * @param flContainerId
     * @param rgs
     */
    public FragmentTabUtils(FragmentManager fragmentManager, List<Fragment> fragmentList,
                            int flContainerId, RadioGroup rgs) {
        this.fragmentList = fragmentList;
        this.rgs = rgs;
        this.fragmentManager = fragmentManager;
        this.flContainerId = flContainerId;

        rgs.setOnCheckedChangeListener(this);

        ((RadioButton) rgs.getChildAt(0)).setChecked(true);

    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

        for (int i = 0; i < rgs.getChildCount(); i++) {
            BadgeRadioButton rBtn = ((BadgeRadioButton) rgs.getChildAt(i));
            if (rBtn.getId() == checkedId) {
                switchFragment(i);
            }
            if (checkedId == R.id.bottom_rb_message){
                rBtn.setBadgeNumber(-1);  //去掉角标
                CacheUnits.getInstance().clearMessageNum();
            }
        }
    }

    /**
     * 切换fragment
     *
     * @param position
     */
    private void switchFragment(int position) {
        //开启事务
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //遍历集合
        for (int i = 0; i < fragmentList.size(); i++) {
            Fragment fragment = fragmentList.get(i);
            if (i == position) {
                //显示fragment
                if (fragment.isAdded()) {
                    //如果这个fragment已经被事务添加,显示
                    ft.show(fragment);
                } else {
                    //如果这个fragment没有被事务添加过,添加
                    ft.add(flContainerId, fragment);
                }
            } else {
                //隐藏fragment
                if (fragment.isAdded()) {
                    ft.hide(fragment);
                }
            }
        }
        //提交事务
        ft.commitAllowingStateLoss();
    }


    /**
     * @param position 设置当前fragment
     */
    public void setCurrentFragment(int position) {
        ((RadioButton) rgs.getChildAt(position)).setChecked(true);
    }

}
