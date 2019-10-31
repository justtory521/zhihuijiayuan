package tendency.hz.zhihuijiayuan.units;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.util.Locale;

import tendency.hz.zhihuijiayuan.application.MyApplication;

/**
 * Author：Libin on 2019/6/26 15:38
 * Description：打开文件
 */
public class OpenFileUtils {

    /**
     * 声明各种类型文件的dataType
     **/
    public static final String DATA_TYPE_VIDEO = "video/*";
    public static final String DATA_TYPE_AUDIO = "audio/*";
    public static final String DATA_TYPE_IMAGE = "image/*";
    public static final String DATA_TYPE_PPT = "application/vnd.ms-powerpoint";
    public static final String DATA_TYPE_XLS = "application/vnd.ms-excel";
    public static final String DATA_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String DATA_TYPE_DOC = "application/msword";
    public static final String DATA_TYPE_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    public static final String DATA_TYPE_TXT = "text/plain";
    public static final String DATA_TYPE_PDF = "application/pdf";

    private static OpenFileUtils mInstance;

    private OpenFileUtils() {
    }

    public static OpenFileUtils getInstance() {
        if (mInstance == null) {
            synchronized (OpenFileUtils.class) {
                if (mInstance == null) {
                    mInstance = new OpenFileUtils();
                }
            }
        }
        return mInstance;
    }


    /**
     * 打开文件
     *
     * @param activity
     * @param filePath
     */
    public void openFile(Activity activity, String filePath, String fileType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        File file = new File(filePath);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = BuglyFileProvider.getUriForFile(MyApplication.getInstance(),
                    MyApplication.getInstance().getPackageName() + ".fileProvider", file);
            intent.setDataAndType(contentUri, fileType);
        } else {
            Uri uri = Uri.fromFile(file);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(uri, fileType);
        }
        activity.startActivity(intent);
    }


    /**
     * 文件类型
     */
    public int getFileType(String fileType) {
        if (fileType.startsWith("video/")) {
            return 1;
        } else if (fileType.startsWith("audio/")) {
            return 2;
        } else if (fileType.startsWith("image/")) {
            return 3;
        } else if (fileType.equals(DATA_TYPE_XLS)
                || fileType.equals(DATA_TYPE_XLSX)
                || fileType.equals(DATA_TYPE_DOC)
                || fileType.equals(DATA_TYPE_DOCX)
                || fileType.equals(DATA_TYPE_TXT)
                || fileType.equals(DATA_TYPE_PDF)) {
            return 4;
        }

        return 0;
    }
}
