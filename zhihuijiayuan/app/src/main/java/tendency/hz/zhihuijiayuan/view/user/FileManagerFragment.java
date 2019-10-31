package tendency.hz.zhihuijiayuan.view.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.DownloadBean;
import tendency.hz.zhihuijiayuan.fragment.PlayAudioFragment;
import tendency.hz.zhihuijiayuan.units.CacheUnits;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.OpenFileUtils;
import tendency.hz.zhihuijiayuan.units.RVDividerItemDecoration;
import tendency.hz.zhihuijiayuan.view.BaseFragment;

/**
 * Author：Libin on 2019/6/27 14:37
 * Description：文件管理
 */
public class FileManagerFragment extends BaseFragment {
    @BindView(R.id.rv_file)
    RecyclerView rvFile;
    private Unbinder unbinder;

    private List<DownloadBean> dataList = new ArrayList<>();
    private BaseQuickAdapter<DownloadBean, BaseViewHolder> adapter;
    private int type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_file_manager, container, false);
        unbinder = ButterKnife.bind(this, view);
        getData();
        initView();
        initData();
        return view;
    }

    private void getData() {
        type = getArguments().getInt("type", 0);
    }


    private void initData() {
        List<DownloadBean> downloadFile = null;
        switch (type) {
            case 1:
                downloadFile = CacheUnits.getInstance().getDownloadVideo();
                break;
            case 2:
                downloadFile = CacheUnits.getInstance().getDownloadAudio();
                break;
            case 3:
                downloadFile = CacheUnits.getInstance().getDownloadPhoto();
                break;
            case 4:
                downloadFile = CacheUnits.getInstance().getDownloadDoc();
                break;
        }
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

    private void initView() {
        rvFile.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFile.addItemDecoration(new RVDividerItemDecoration(getActivity()));
        adapter = new BaseQuickAdapter<DownloadBean, BaseViewHolder>(R.layout.rv_file_manager_item, dataList) {
            @Override
            protected void convert(BaseViewHolder helper, DownloadBean item) {
                ImageView img = helper.getView(R.id.iv_download_photo);
                if (type == 3) {
                    Glide.with(FileManagerFragment.this)
                            .load(Uri.fromFile(new File(item.getFile_path())))
                            .into(img);
                } else {
                    img.setImageResource(R.mipmap.logo);
                }
                helper.setText(R.id.tv_photo_name, item.getFile_name());

                TextView tvSize = helper.getView(R.id.tv_photo_size);
                long fileSize = Long.parseLong(item.getFile_size());

                if (fileSize < 1024) {
                    tvSize.setText(fileSize + "B");
                } else if (fileSize < 1024 * 1024) {
                    tvSize.setText(FormatUtils.getInstance().intFormat(Double.parseDouble(item.getFile_size()) / 1024) + "K");
                } else if (fileSize < 1024 * 1024 * 1024) {
                    tvSize.setText(FormatUtils.getInstance().intFormat(Double.parseDouble(item.getFile_size()) / (1024 * 1024)) + "M");
                } else {
                    tvSize.setText(FormatUtils.getInstance().intFormat(Double.parseDouble(item.getFile_size()) / (1024 * 1024 * 1024)) + "G");
                }

            }
        };


        rvFile.setAdapter(adapter);


        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (type) {
                    case 1:
                        OpenFileUtils.getInstance().openFile(getActivity(),
                                dataList.get(position).getFile_path(), dataList.get(position).getFile_type());
                        break;
                    case 2:
                        PlayAudioFragment playAudioFragment = PlayAudioFragment.newInstance(dataList.get(position).getFile_path());
                        playAudioFragment.show(getActivity().getFragmentManager(), "audio");
                        break;
                    case 3:
                        Intent intent = new Intent(getActivity(), PreviewImgActivity.class);
                        intent.putExtra("file_path", dataList.get(position).getFile_path());
                        startActivity(intent);
                        break;
                    case 4:
                        Intent fileIntent = new Intent(getActivity(), PreviewFileActivity.class);
                        fileIntent.putExtra("file_name", dataList.get(position).getFile_name());
                        fileIntent.putExtra("file_path", dataList.get(position).getFile_path());
                        fileIntent.putExtra("file_type", dataList.get(position).getFile_type());
                        startActivity(fileIntent);
                        break;
                }

            }
        });
    }

    public static FileManagerFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        FileManagerFragment fragment = new FileManagerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
