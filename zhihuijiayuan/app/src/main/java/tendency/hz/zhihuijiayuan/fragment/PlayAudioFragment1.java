package tendency.hz.zhihuijiayuan.fragment;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRouting;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cjt2325.cameralibrary.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.units.LogUtils;

/**
 * Author：Libin on 2020-03-13 11:22
 * Email：1993911441@qq.com
 * Describe：
 */
public class PlayAudioFragment1 extends DialogFragment {


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
    //    private MediaPlayer mediaPlayer = null;
    //播放进度秒数
    private int time;
    //音频总时间
    private int totalTime;

    private Handler mHandler = new Handler();


    private byte[] audioData;
    private AudioTrack audioTrack;


    // 采样率
    // 44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    // 采样频率一般共分为22.05KHz、44.1KHz、48KHz三个等级
    private final static int AUDIO_SAMPLE_RATE = 16000;

    // 音频通道 单声道
    private final static int AUDIO_CHANNEL = AudioFormat.CHANNEL_OUT_MONO;

    // 音频格式：PCM编码
    private final static int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    // 音频格式：PCM编码
    private final static int AUDIO_TRACK_TYPE_STREAM = AudioTrack.MODE_STREAM;



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

        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });


        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, AUDIO_SAMPLE_RATE,
                AUDIO_CHANNEL, AUDIO_ENCODING, audioData.length, AUDIO_TRACK_TYPE_STREAM);
        audioTrack.write(audioData, 0, audioData.length);
        audioTrack.play();


    }

    private void getData() {

        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });



        Bundle bundle = getArguments();
        audioData = bundle.getByteArray("audio");

        LogUtils.log(audioData.length+"aaaa");


    }


    private void releaseAudioTrack() {
        if (this.audioTrack != null) {
            audioTrack.stop();
            audioTrack.release();

        }
    }


//    // Play start/stop
//    private void onPlay(boolean isPlaying) {
//        if (!isPlaying) {
//            //currently MediaPlayer is not playing audio
//            if (mediaPlayer == null) {
//                startPlaying(); //start from beginning
//            } else {
//                resumePlaying(); //resume the currently paused MediaPlayer
//            }
//        } else {
//            pausePlaying();
//        }
//    }
//
//    private void pausePlaying() {
//        fabPlayAudio.setImageResource(R.mipmap.ic_media_play);
//        mHandler.removeCallbacks(mRunnable);
//        mediaPlayer.pause();
//    }
//
//    private void resumePlaying() {
//
//        fabPlayAudio.setImageResource(R.mipmap.ic_media_pause);
//        mHandler.removeCallbacks(mRunnable);
//        mediaPlayer.start();
//        mHandler.postDelayed(mRunnable, 1000);
//    }
//
//
//    private void startPlaying() {
//        mHandler.post(mRunnable);
//        mediaPlayer.start();
//        //keep screen on while playing audio
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//    }


//    //updating
//    private Runnable mRunnable = new Runnable() {
//        @Override
//        public void run() {
//            if (mediaPlayer != null) {
//                time++;
//                if (tvCurrentTime != null && time <= totalTime) {
//                    sbPlayAudio.setProgress(time);
//                    tvCurrentTime.setText(getTime(time));
//                    mHandler.postDelayed(mRunnable, 1000);
//                } else {
//                    stopPlaying();
//                    dismiss();
//                }
//            }
//        }
//    };

//    private void stopPlaying() {
//        fabPlayAudio.setImageResource(R.mipmap.ic_media_play);
//        mHandler.removeCallbacks(mRunnable);
//        mediaPlayer.stop();
//        mediaPlayer.reset();
//        mediaPlayer.release();
//        mediaPlayer = null;
//        isPlay = !isPlay;
//
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public static PlayAudioFragment1 newInstance(byte[] audio) {

        Bundle args = new Bundle();
        args.putByteArray("audio", audio);
        PlayAudioFragment1 fragment = new PlayAudioFragment1();
        fragment.setArguments(args);
        return fragment;
    }


    @OnClick({R.id.fab_play_audio, R.id.tv_cancel_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_play_audio:
                if (!TextUtils.isEmpty(audioPath)) {
//                    onPlay(isPlay);
                    isPlay = !isPlay;
                }
                break;
            case R.id.tv_cancel_play:
//                stopPlaying();
                dismiss();
                break;
        }
    }
}
