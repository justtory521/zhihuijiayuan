package tendency.hz.zhihuijiayuan.units;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.application.MyApplication;

/**
 * Author：Li Bin on 2019/7/15 17:25
 * Description：推动播放自定义音频
 */
public class SoundPoolUtils {
    private static SoundPoolUtils mInstance;
    private SoundPool soundPool;


    private SoundPoolUtils() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().setMaxStreams(1).build();// 创建SoundPool对象
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
        }
    }

    public static SoundPoolUtils getInstance() {
        if (mInstance == null) {
            synchronized (SoundPoolUtils.class) {
                if (mInstance == null) {
                    mInstance = new SoundPoolUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 播放提示音
     *
     * @param rid
     */
    public void playAudio(int rid) {
        soundPool.load(MyApplication.getInstance(), rid, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                soundPool.play(i, 1, 1, 0, 0, 1);
            }
        });
    }

}
