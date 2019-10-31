package tendency.hz.zhihuijiayuan.units;

import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import tendency.hz.zhihuijiayuan.application.MyApplication;
import tendency.hz.zhihuijiayuan.bean.base.App;

/**
 * Created by JasonYao on 2018/10/11.
 */
public class SpeechUnits {
    private static final String TAG = "SpeechUnits---";
    public static SpeechUnits mInstance = null;
    private SpeechRecognizer mIat;

    private SendSpeechResult mSendSpeechResult;

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = code -> {
    };

    /**
     * 识别监听器
     */
    private RecognizerListener mRecogListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            Log.d(TAG, "返回音频数据：" + bytes.length);
        }

        @Override
        public void onBeginOfSpeech() {
            Log.e(TAG, "开始说话");
        }

        @Override
        public void onEndOfSpeech() {
            Log.e(TAG, "结束说话");
        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            if (!FormatUtils.getInstance().isEmpty(parseGrammarResult(recognizerResult.getResultString()))) {
                mSendSpeechResult.sendResult(parseGrammarResult(recognizerResult.getResultString()));
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            ViewUnits.getInstance().showToast("我没有听到你说什么");
            Log.e(TAG, "onError Code：" + speechError.getErrorCode());
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    private SpeechUnits() {
        mIat = SpeechRecognizer.createRecognizer(MyApplication.getInstance(), mInitListener);
        //设置语法ID和 SUBJECT 为空，以免因之前有语法调用而设置了此参数；或直接清空所有参数，具体可参考 DEMO 的示例。
        mIat.setParameter(SpeechConstant.CLOUD_GRAMMAR, null);
        mIat.setParameter(SpeechConstant.SUBJECT, null);
        //设置返回结果格式，目前支持json,xml以及plain 三种格式，其中plain为纯听写文本内容
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        //此处engineType为“cloud”
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        //设置语音输入语言，zh_cn为简体中文
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        //设置结果返回语言
        mIat.setParameter(SpeechConstant.ACCENT, "zh_cn");
        // 设置语音前端点:静音超时时间，单位ms，即用户多长时间不说话则当做超时处理
        //取值范围{1000～10000}
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        //设置语音后端点:后端点静音检测时间，单位ms，即用户停止说话多长时间内即认为不再输入，
        //自动停止录音，范围{0~10000}
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
        //设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
    }

    public static SpeechUnits getInstance() {
        if (mInstance == null) {
            synchronized (SpeechUnits.class) {
                if (mInstance == null) {
                    mInstance = new SpeechUnits();
                }
            }
        }

        return mInstance;
    }

    /**
     * 开始识别
     */
    public void startListening(SendSpeechResult sendSpeechResult) {
        Log.e(TAG, "开始识别");
        mSendSpeechResult = sendSpeechResult;
        mIat.startListening(mRecogListener);
    }

    /**
     * 停止识别
     *
     * @return
     */
    public void stopListening() {
        Log.e(TAG, "停停止说话");
        mIat.stopListening();
    }

    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
//				如果需要多候选结果，解析数组其他字段
//				for(int j = 0; j < items.length(); j++)
//				{
//					JSONObject obj = items.getJSONObject(j);
//					ret.append(obj.getString("w"));
//				}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    public static String parseGrammarResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                for (int j = 0; j < items.length(); j++) {
                    JSONObject obj = items.getJSONObject(j);
                    if (obj.getString("w").contains("nomatch")) {
                        ret.append("没有匹配结果.");
                        return ret.toString();
                    }
                    ret.append(obj.getString("w"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret.append("没有匹配结果.");
        }
        return ret.toString();
    }

    public interface SendSpeechResult {
        void sendResult(String result);
    }
}
