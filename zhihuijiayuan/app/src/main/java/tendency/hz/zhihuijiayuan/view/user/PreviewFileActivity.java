package tendency.hz.zhihuijiayuan.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.base.Uri;
import tendency.hz.zhihuijiayuan.units.OpenFileUtils;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;

/**
 * Author：Libin on 2019/6/28 12:32
 * Description：文件浏览
 */
public class PreviewFileActivity extends BaseActivity implements TbsReaderView.ReaderCallback {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.rl_file)
    RelativeLayout rlFile;

    TbsReaderView mTbsReaderView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_file);
        ButterKnife.bind(this);
        ViewUnits.getInstance().setTitleHeight(viewStatusBar);
        llTitle.setBackgroundResource(R.color.blue_0d8);
        mTbsReaderView = new TbsReaderView(this, this);
        rlFile.addView(mTbsReaderView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        Intent intent = getIntent();
        String fileName = intent.getStringExtra("file_name");
        String filePath = intent.getStringExtra("file_path");
        String fileType = intent.getStringExtra("file_type");

        tvTitleName.setText(fileName);


        String tempPath = Uri.tempPath;
        File bsReaderTempFile = new File(tempPath);
        if (!bsReaderTempFile.exists()) {
            bsReaderTempFile.mkdir();
        }
        Bundle bundle = new Bundle();
        bundle.putString("filePath", filePath);
        bundle.putString("tempPath", tempPath);
        boolean result = mTbsReaderView.preOpen(fileName.substring(fileName.lastIndexOf(".") + 1), false);
        if (result) {
            mTbsReaderView.openFile(bundle);
        } else {
            OpenFileUtils.getInstance().openFile(this, filePath, fileType);
        }

    }

    @OnClick(R.id.iv_title_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }

    @Override
    protected void onDestroy() {
        mTbsReaderView.onStop();
        super.onDestroy();
    }
}
