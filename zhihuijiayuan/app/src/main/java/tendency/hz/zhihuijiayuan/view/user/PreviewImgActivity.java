package tendency.hz.zhihuijiayuan.view.user;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.view.BaseActivity;

/**
 * Author：Libin on 2019/6/27 16:48
 * Description：预览图片
 */
public class PreviewImgActivity extends BaseActivity {
    @BindView(R.id.iv_preview)
    ImageView ivPreview;
    @BindView(R.id.iv_preview_back)
    ImageView ivPreviewBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_photo);
        ButterKnife.bind(this);

        String filePath = getIntent().getStringExtra("file_path");
        Glide.with(this)
                .load(Uri.fromFile(new File(filePath)))
                .into(ivPreview);

    }

    @OnClick(R.id.iv_preview_back)
    public void onViewClicked() {
        finish();
    }
}
