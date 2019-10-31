package tendency.hz.zhihuijiayuan.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.DownloadBean;
import tendency.hz.zhihuijiayuan.bean.base.Uri;
import tendency.hz.zhihuijiayuan.units.CacheUnits;
import tendency.hz.zhihuijiayuan.units.FileUtils;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.NoHttpUtil;
import tendency.hz.zhihuijiayuan.units.UserUnits;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.card.CardContentActivity;

/**
 * Author：Libin on 2019/6/25 15:16
 * Description：下载
 */
public class DownLoadFragment extends DialogFragment {
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.iv_download)
    ImageView ivDownload;
    @BindView(R.id.tv_download_name)
    TextView tvFileName;
    @BindView(R.id.pb_download)
    ProgressBar pbDownload;
    @BindView(R.id.tv_cancel_download)
    TextView tvCancelDownload;
    private Unbinder unbinder;
    private String callback;
    //下载链接
    private String url;
    //文件类型
    private String fileType;
    //是否正在下载
    private boolean isDownload;
    //文件类型
    private String fileName;
    //卡片id
    private String cardId;
    //文件大小b
    private long fileSize;
    //取消下载标识
    private String cancelSign;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, container, false);
        unbinder = ButterKnife.bind(this, view);
        getData();
        download();
        return view;
    }

    private void getData() {
        Bundle bundle = getArguments();
        callback = bundle.getString("callback");
        url = bundle.getString("url");
        cardId = bundle.getString("cardId");


        fileName = url.substring(url.lastIndexOf("/") + 1);


        String name = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
        String type = url.substring(url.lastIndexOf("."));

        int index = 0;
        File file = new File(Uri.downloadPath, fileName);

        while (file.exists() && file.isFile()) {
            index++;
            fileName = name + "(" + index + ")" + type;
            file = new File(Uri.downloadPath, fileName);
        }

        cancelSign = fileName;
        tvFileName.setText(fileName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public static DownLoadFragment newInstance(String callback, String url, String cardId) {

        Bundle args = new Bundle();
        args.putString("callback", callback);
        args.putString("url", url);

        args.putString("cardId", cardId);
        DownLoadFragment fragment = new DownLoadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void download() {

        DownloadRequest downloadRequest = NoHttp.createDownloadRequest(url, Uri.downloadPath, fileName, true, true);
        downloadRequest.setCancelSign(cancelSign);
        NoHttpUtil.getNoHttpUtil().mDownloadQueue.add(1, downloadRequest, new DownloadListener() {
            @Override
            public void onDownloadError(int what, Exception exception) {
                if (getActivity() != null) {
                    ViewUnits.getInstance().showToast("下载失败");
                    sendCallBack("500", "fail", exception.getMessage());
                }

                dismiss();
            }

            @Override
            public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {
                if (allCount <= 50 * 1024 * 1024) {
                    fileType = responseHeaders.getContentType();
                    fileSize = allCount;
                    isDownload = true;
                } else {
                    cancelDownload();
                }

            }

            @Override
            public void onProgress(int what, int progress, long fileCount, long speed) {
                if (tvProgress != null) {
                    if (fileSize < 1024) {
                        tvProgress.setText(fileCount + "B / " + fileSize + "B");
                    } else if (fileSize < 1024 * 1024) {
                        tvProgress.setText(FormatUtils.getInstance().intFormat((double) fileCount / 1024)
                                + "K / " + FormatUtils.getInstance().intFormat((double) fileSize / 1024) + "K");
                    } else if (fileSize < 1024 * 1024 * 1024) {
                        tvProgress.setText(FormatUtils.getInstance().intFormat((double) fileCount / (1024 * 1024))
                                + "M / " + FormatUtils.getInstance().intFormat((double) fileSize / (1024 * 1024)) + "M");
                    } else {
                        tvProgress.setText(FormatUtils.getInstance().intFormat((double) fileCount / (1024 * 1024 * 1024))
                                + "G / " + FormatUtils.getInstance().intFormat((double) fileSize / (1024 * 1024 * 1024)) + "G");
                    }

                    pbDownload.setProgress(progress);
                    ivDownload.setImageResource(R.mipmap.ic_download_start);
                }
            }

            @Override
            public void onFinish(int what, String filePath) {
                DownloadBean downloadBean = new DownloadBean();
                downloadBean.setFile_name(fileName);
                downloadBean.setFile_path(filePath);
                downloadBean.setFile_size(String.valueOf(fileSize));
                downloadBean.setFile_type(fileType);
                downloadBean.setFile_url(url);
                downloadBean.setUser_id(UserUnits.getInstance().getAccountId());
                downloadBean.setCard_id(cardId);
                CacheUnits.getInstance().insertDownloadFile(downloadBean);
                ViewUnits.getInstance().showToast("文件已保存至：" + filePath);
                if (getActivity() != null) {
                    sendCallBack("200", "success", filePath);
                }
                dismiss();
            }

            @Override
            public void onCancel(int what) {
                if (tvProgress != null) {
                    ivDownload.setImageResource(R.mipmap.ic_download_pause);
                    isDownload = false;
                }

            }
        });
    }

    @OnClick({R.id.iv_download, R.id.tv_cancel_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_download:
                if (isDownload) {
                    NoHttpUtil.getNoHttpUtil().mDownloadQueue.cancelBySign(cancelSign);
                } else {
                    download();
                }
                break;
            case R.id.tv_cancel_download:
                cancelDownload();
                break;
        }
    }

    /**
     * 取消下载
     */
    private void cancelDownload() {
        NoHttpUtil.getNoHttpUtil().mDownloadQueue.cancelBySign(cancelSign);
        FileUtils.getInstance().delete(Uri.downloadPath + "/" + fileName + ".nohttp");
        dismiss();
    }


    public void sendCallBack(String status, String msg, String value) {

        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("value", value);
            jsonObject.put("status", status);
            jsonObject.put("msg", msg);
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
}
