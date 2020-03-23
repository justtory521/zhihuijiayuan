package tendency.hz.zhihuijiayuan.fragment;


import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zlw.main.recorderlib.BuildConfig;
import com.zlw.main.recorderlib.RecordManager;
import com.zlw.main.recorderlib.recorder.RecordConfig;
import com.zlw.main.recorderlib.recorder.RecordHelper;
import com.zlw.main.recorderlib.recorder.listener.RecordResultListener;
import com.zlw.main.recorderlib.recorder.listener.RecordStateListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.application.MyApplication;
import tendency.hz.zhihuijiayuan.bean.base.Uri;
import tendency.hz.zhihuijiayuan.units.Base64Utils;
import tendency.hz.zhihuijiayuan.units.FileUtils;
import tendency.hz.zhihuijiayuan.units.LogUtils;
import tendency.hz.zhihuijiayuan.view.card.CardContentActivity;

import static com.zlw.main.recorderlib.recorder.RecordHelper.RecordState.RECORDING;

/**
 * Author：Libin on 2019/5/7 15:11
 * Email：1993911441@qq.com
 * Describe：录音
 */
public class RecordAudioFragment extends DialogFragment {

    @BindView(R.id.tv_record_limit_time)
    TextView tvRecordLimitTime;
    @BindView(R.id.fab_record)
    FloatingActionButton fabRecord;
    Unbinder unbinder;
    @BindView(R.id.tv_record_cancel)
    TextView tvCancelRecord;

    private String callback;
    private int time;
    private boolean startRecord = false;

    RecordManager recordManager = RecordManager.getInstance();

    private CountDownTimer countDownTimer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_audio, container, false);
        unbinder = ButterKnife.bind(this, view);
        getData();
        return view;
    }

    private void getData() {
        Bundle bundle = getArguments();
        callback = bundle.getString("callback");
        time = bundle.getInt("time");
        tvRecordLimitTime.setText("点击按钮录音，最长" + time + "秒");


        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initRecord();

    }

    private void initRecord() {

        recordManager.init(MyApplication.getInstance(), BuildConfig.DEBUG);
        recordManager.changeFormat(RecordConfig.RecordFormat.MP3);
        recordManager.changeRecordDir(Uri.audioPath + "/");

        recordManager.setRecordStateListener(new RecordStateListener() {
            @Override
            public void onStateChange(RecordHelper.RecordState state) {
                if (state == RECORDING) {
                    countDownTimer = new CountDownTimer(time * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (tvRecordLimitTime != null) {

                                String s = new BigDecimal(millisUntilFinished * 0.001).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
                                tvRecordLimitTime.setText("正在录音，还剩" + s + "秒");
                            }
                        }

                        @Override
                        public void onFinish() {
                            recordManager.stop();
                        }
                    };
                    countDownTimer.start();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
        recordManager.setRecordResultListener(new RecordResultListener() {
            @Override
            public void onResult(File result) {
                sendCallBack(result.getAbsolutePath());
                dismiss();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        recordManager = null;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

    }

    @OnClick({R.id.tv_record_cancel, R.id.fab_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_record_cancel:
                dismiss();
                break;
            case R.id.fab_record:
                if (startRecord) {
                    startRecord = false;
                    recordManager.stop();
                } else {
                    startRecord = true;
                    fabRecord.setImageResource(R.mipmap.ic_stop_record);
                    recordManager.start();
                }
                break;
        }
    }

    public void sendCallBack(String filePath) {

        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            String s = Base64Utils.fileToBase64(filePath);
            FileUtils.getInstance().clearDirectory(Uri.audioPath);
            data.put("value", s);
            jsonObject.put("status", "200");
            jsonObject.put("msg", "success");
            jsonObject.put("data", data);
            ((CardContentActivity) getActivity()).callBackResult(callback, jsonObject.toString());

        } catch (JSONException e) {
            ((CardContentActivity) getActivity()).callBackResult(callback, "未知错误，联系管理员");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            ((CardContentActivity) getActivity()).callBackResult(callback, "未知错误，联系管理员");
        }

    }


    public static RecordAudioFragment newInstance(String callback, int time) {

        Bundle args = new Bundle();
        args.putString("callback", callback);
        args.putInt("time", time);
        RecordAudioFragment fragment = new RecordAudioFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
