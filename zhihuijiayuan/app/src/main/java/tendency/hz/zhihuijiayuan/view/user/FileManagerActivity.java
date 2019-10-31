package tendency.hz.zhihuijiayuan.view.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.tablayout.SlidingTabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.adapter.MyFragmentPagerAdapter;
import tendency.hz.zhihuijiayuan.bean.DownloadBean;
import tendency.hz.zhihuijiayuan.bean.UserRightsBean;
import tendency.hz.zhihuijiayuan.fragment.UserRightsFragment;
import tendency.hz.zhihuijiayuan.units.CacheUnits;
import tendency.hz.zhihuijiayuan.units.OpenFileUtils;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;

/**
 * Author：Libin on 2019/6/26 14:02
 * Description：文件管理
 */
public class FileManagerActivity extends BaseActivity {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;

    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.stl_file_manager)
    SlidingTabLayout stlFileManager;
    @BindView(R.id.vp_file_manager)
    ViewPager vpFileManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);
        ButterKnife.bind(this);
        llTitle.setBackgroundResource(R.color.blue_0d8);
        tvTitleName.setText("文件管理");
        ViewUnits.getInstance().setTitleHeight(viewStatusBar);
        initView();
    }


    private void initView() {


        List<Fragment> fragmentList = new ArrayList<>();

        List<String> titleList = new ArrayList<>();

        titleList.add("视频");
        titleList.add("音频");
        titleList.add("图片");
        titleList.add("文档");

        for (int i = 1; i < 5; i++) {
            fragmentList.add(FileManagerFragment.newInstance(i));
        }

        stlFileManager.setTabSpaceEqual(true);

        stlFileManager.setTabSpaceEqual(true);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),
                fragmentList, titleList);
        vpFileManager.setOffscreenPageLimit(titleList.size());

        vpFileManager.setAdapter(adapter);
        stlFileManager.setViewPager(vpFileManager);
        stlFileManager.setCurrentTab(0);


    }

    @OnClick(R.id.iv_title_back)
    public void onViewClicked() {
        finish();
    }
}
