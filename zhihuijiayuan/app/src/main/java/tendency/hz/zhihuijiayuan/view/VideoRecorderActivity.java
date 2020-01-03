package tendency.hz.zhihuijiayuan.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.JCameraListener;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.VideoRecorderBean;
import tendency.hz.zhihuijiayuan.bean.base.Uri;
import tendency.hz.zhihuijiayuan.inter.OnVideoRecorderListener;
import tendency.hz.zhihuijiayuan.units.LogUtils;

/**
 * Author：Libin on 2019/6/21 12:59
 * Description：录制视频
 */
public class VideoRecorderActivity extends BaseActivity {
    @BindView(R.id.camera_view)
    JCameraView cameraView;
    private String callback;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_recorder);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        callback = intent.getStringExtra("callback");
        int time = intent.getIntExtra("time", 10);


        cameraView.setDurtation(time * 1000);
        //设置视频保存路径
        cameraView.setSaveVideoPath(Uri.videoPath);

        //JCameraView监听
        cameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {


            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                LogUtils.log(url);
                VideoRecorderActivity.this.finish();
                EventBus.getDefault().post(new VideoRecorderBean(callback,url));
            }

            @Override
            public void quit() {
                VideoRecorderActivity.this.finish();
            }

        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.onPause();
    }

    @Override
    protected void onDestroy() {
        cameraView.onDestroy();
        super.onDestroy();
    }
}
