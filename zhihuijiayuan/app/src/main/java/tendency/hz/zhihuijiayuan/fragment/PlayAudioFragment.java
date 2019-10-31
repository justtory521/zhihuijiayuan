package tendency.hz.zhihuijiayuan.fragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.databinding.adapters.SeekBarBindingAdapter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;


import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.units.LogUtils;


/**
 * Author：Libin on 2019/5/7 16:12
 * Email：1993911441@qq.com
 * Describe：播放音频
 */
public class PlayAudioFragment extends DialogFragment {


    @BindView(R.id.tv_current_time)
    TextView tvCurrentTime;
    @BindView(R.id.tv_total_time)
    TextView tvTotalTime;
    @BindView(R.id.fab_play_audio)
    FloatingActionButton fabPlayAudio;
    @BindView(R.id.tv_cancel_play)
    TextView tvCancelPlay;
    Unbinder unbinder;
    @BindView(R.id.sb_play_audio)
    SeekBar sbPlayAudio;


    //音频文件路径
    private String audioPath;
    private boolean isPlay;
    private MediaPlayer mediaPlayer = null;
    //播放进度秒数
    private int time;
    //音频总时间
    private int totalTime;

    private Handler mHandler = new Handler();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
    }

    public String getTime(int time) {

        int minutes = time / 60;

        int seconds = time % 60;
        return String.format("%02d:%02d", minutes, seconds);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_audio, container, false);

        unbinder = ButterKnife.bind(this, view);
        getData();
        prepareAudio();
        return view;
    }

    /**
     * 加载音频
     */
    private void prepareAudio() {

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioPath);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }


        sbPlayAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                time = seekBar.getProgress();
                mediaPlayer.seekTo(time * 1000);
                tvCurrentTime.setText(getTime(time));
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                totalTime = mediaPlayer.getDuration() / 1000;
                sbPlayAudio.setMax(totalTime);
                tvTotalTime.setText(getTime(totalTime));


                onPlay(false);
                isPlay = true;
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                dismiss();
            }
        });


    }

    private void getData() {
        Bundle bundle = getArguments();
        audioPath = bundle.getString("audio");
    }

    // Play start/stop
    private void onPlay(boolean isPlaying) {
        if (!isPlaying) {
            //currently MediaPlayer is not playing audio
            if (mediaPlayer == null) {
                startPlaying(); //start from beginning
            } else {
                resumePlaying(); //resume the currently paused MediaPlayer
            }
        } else {
            pausePlaying();
        }
    }

    private void pausePlaying() {
        fabPlayAudio.setImageResource(R.mipmap.ic_media_play);
        mHandler.removeCallbacks(mRunnable);
        mediaPlayer.pause();
    }

    private void resumePlaying() {

        fabPlayAudio.setImageResource(R.mipmap.ic_media_pause);
        mHandler.removeCallbacks(mRunnable);
        mediaPlayer.start();
        mHandler.postDelayed(mRunnable, 1000);
    }


    private void startPlaying() {
        mHandler.post(mRunnable);
        mediaPlayer.start();
        //keep screen on while playing audio
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    //updating
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                time++;
                if (tvCurrentTime != null && time <= totalTime) {
                    sbPlayAudio.setProgress(time);
                    tvCurrentTime.setText(getTime(time));
                    mHandler.postDelayed(mRunnable, 1000);
                } else {
                    dismiss();
                }
            }
        }
    };

    private void stopPlaying() {
        fabPlayAudio.setImageResource(R.mipmap.ic_media_play);
        mHandler.removeCallbacks(mRunnable);
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
        isPlay = !isPlay;

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        stopPlaying();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public static PlayAudioFragment newInstance(String audio) {

        Bundle args = new Bundle();
        args.putString("audio", audio);
        PlayAudioFragment fragment = new PlayAudioFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick({R.id.fab_play_audio, R.id.tv_cancel_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_play_audio:
                if (!TextUtils.isEmpty(audioPath)) {
                    onPlay(isPlay);
                    isPlay = !isPlay;
                }
                break;
            case R.id.tv_cancel_play:
                dismiss();
                break;
        }
    }
}
