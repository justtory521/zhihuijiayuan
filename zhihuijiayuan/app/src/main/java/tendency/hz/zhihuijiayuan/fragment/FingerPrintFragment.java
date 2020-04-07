package tendency.hz.zhihuijiayuan.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.Cipher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import tendency.hz.zhihuijiayuan.R;

import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.card.CardContentActivity;

/**
 * Author：Libin on 2019/5/6 16:02
 * Email：1993911441@qq.com
 * Describe：指纹验证弹窗
 */
public class FingerPrintFragment extends DialogFragment {
    @BindView(R.id.tv_fingerprint_status)
    TextView tvFingerprintStatus;
    @BindView(R.id.tv_cancel_fingerprint)
    TextView tvCancelFingerprint;
    @BindView(R.id.view_fingerprint)
    View viewFingerprint;
    @BindView(R.id.tv_input_pwd)
    TextView tvInputPwd;
    Unbinder unbinder;

    private FingerprintManagerCompat mFingerprintManagerCompat;
    private CancellationSignal mCancellationSignal;
    private CardContentActivity mActivity;
    private Cipher mCipher;
    private String mCallback;

    //标识是否是用户主动取消的认证
    private boolean isSelfCancelled;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFingerprintManagerCompat = FingerprintManagerCompat.from(getContext());

        setStyle(android.app.DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (CardContentActivity) getActivity();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fingerprint, container, false);
        unbinder = ButterKnife.bind(this, view);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 开始指纹认证监听
        startListening(mCipher);
    }

    public void setCipher(Cipher cipher) {
        mCipher = cipher;
    }

    public void setCallback(String callback) {
        mCallback = callback;
    }


    @Override
    public void onPause() {
        super.onPause();
        stopListening();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        stopListening();
    }

    private void startListening(Cipher cipher) {
        isSelfCancelled = false;
        mCancellationSignal = new CancellationSignal();
        mFingerprintManagerCompat.authenticate(new FingerprintManagerCompat.CryptoObject(cipher),
                0, mCancellationSignal, new FingerprintManagerCompat.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errMsgId, CharSequence errString) {
                        if (!isSelfCancelled) {
                            tvFingerprintStatus.setText(errString);
                            if (errMsgId == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) {
                                viewFingerprint.setVisibility(View.VISIBLE);
                                tvInputPwd.setVisibility(View.VISIBLE);
                                sendCallBack(mCallback, "501", "fail", "多次验证指纹失败");
                            }
                        }
                    }

                    @Override
                    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                        tvFingerprintStatus.setText(helpString);
                    }

                    @Override
                    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                        ViewUnits.getInstance().showToast("指纹验证成功");
                        dismiss();
                        stopListening();
                        sendCallBack(mCallback, "200", "success", "指纹验证成功");
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        tvFingerprintStatus.setText("指纹验证失败，请再试一次");
                        sendCallBack(mCallback, "505", "fail", "指纹验证失败");
                    }
                }, null);
    }


    public void sendCallBack(String callBack, String status, String msg, String value) {
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("value", value);
            jsonObject.put("status", status);
            jsonObject.put("msg", msg);
            jsonObject.put("data", data);
            mActivity.callBackResult(callBack, jsonObject.toString());
        } catch (JSONException e) {
            mActivity.callBackResult(callBack, "未知错误，联系管理员");
            e.printStackTrace();
        }
    }


    private void stopListening() {
        if (mCancellationSignal != null) {
            mCancellationSignal.cancel();
            mCancellationSignal = null;
            isSelfCancelled = true;
        }
    }


    @OnClick({R.id.tv_cancel_fingerprint, R.id.tv_input_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel_fingerprint:
                dismiss();
                stopListening();
                sendCallBack(mCallback, "500", "success", "取消指纹验证");
                break;
            case R.id.tv_input_pwd:
//                sendCallBack(mCallback,"500","success","取消指纹验证");
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mFingerprintManagerCompat = null;
    }
}
