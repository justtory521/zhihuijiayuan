package tendency.hz.zhihuijiayuan.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JasonYao on 2018/3/16.
 */

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mListFragment = new ArrayList<>();

    public MainFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);

        mListFragment = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mListFragment.get(position);
    }

    @Override
    public int getCount() {
        return mListFragment.size();
    }
}
