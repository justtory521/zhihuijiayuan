package tendency.hz.zhihuijiayuan.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.DownloadBean;
import tendency.hz.zhihuijiayuan.fragment.PlayAudioFragment;
import tendency.hz.zhihuijiayuan.units.CacheUnits;
import tendency.hz.zhihuijiayuan.units.OpenFileUtils;
import tendency.hz.zhihuijiayuan.units.RVDividerItemDecoration;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;

/**
 * Author：Libin on 2019/7/1 16:07
 * Description：文件管理（不分类）
 */
public class FileManager1Activity extends BaseActivity {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.rv_manager_file)
    RecyclerView rvFile;

    private List<DownloadBean> dataList = new ArrayList<>();
    private BaseQuickAdapter<DownloadBean, BaseViewHolder> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager1);
        ButterKnife.bind(this);
        llTitle.setBackgroundResource(R.color.blue_0d8);
        tvTitleName.setText("文件管理");
        ViewUnits.getInstance().setTitleHeight(viewStatusBar);
        initView();
        initData();
    }

    private void initView() {
        rvFile.setLayoutManager(new LinearLayoutManager(this));
        rvFile.addItemDecoration(new RVDividerItemDecoration(this));
        adapter = new BaseQuickAdapter<DownloadBean, BaseViewHolder>(R.layout.rv_file_manager1_item, dataList) {
            @Override
            protected void convert(BaseViewHolder helper, DownloadBean item) {
                helper.setText(R.id.tv_file_name, item.getFile_name());
            }
        };


        rvFile.setAdapter(adapter);


        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (OpenFileUtils.getInstance().getFileType(dataList.get(position).getFile_type())) {
                    case 1:
                        OpenFileUtils.getInstance().openFile(FileManager1Activity.this,
                                dataList.get(position).getFile_path(), dataList.get(position).getFile_type());
                        break;
                    case 2:
                        PlayAudioFragment playAudioFragment = PlayAudioFragment.newInstance(dataList.get(position).getFile_path());
                        playAudioFragment.show(getFragmentManager(), "audio");
                        break;
                    case 3:
                        Intent intent = new Intent(FileManager1Activity.this, PreviewImgActivity.class);
                        intent.putExtra("file_path", dataList.get(position).getFile_path());
                        startActivity(intent);
                        break;
                    case 4:
                        Intent fileIntent = new Intent(FileManager1Activity.this, PreviewFileActivity.class);
                        fileIntent.putExtra("file_name", dataList.get(position).getFile_name());
                        fileIntent.putExtra("file_path", dataList.get(position).getFile_path());
                        fileIntent.putExtra("file_type", dataList.get(position).getFile_type());
                        startActivity(fileIntent);
                        break;
                }

            }
        });
    }


    private void initData() {
        List<DownloadBean> downloadFile = CacheUnits.getInstance().getDownloadFile();
        if (downloadFile != null && downloadFile.size() > 0) {
            for (DownloadBean downloadBean : downloadFile) {
                File file = new File(downloadBean.getFile_path());
                if (file.exists() && file.isFile()) {
                    dataList.add(downloadBean);
                }
            }
            adapter.notifyDataSetChanged();
        }

    }

    @OnClick(R.id.iv_title_back)
    public void onViewClicked() {
        finish();
    }
}
