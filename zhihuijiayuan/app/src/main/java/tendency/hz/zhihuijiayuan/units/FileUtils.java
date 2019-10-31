package tendency.hz.zhihuijiayuan.units;

import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import tendency.hz.zhihuijiayuan.bean.base.Uri;

/**
 * Author：Libin on 2019/5/8 18:01
 * Email：1993911441@qq.com
 * Describe：
 */
public class FileUtils {
    private static FileUtils mInstance;

    private FileUtils() {
    }

    public static FileUtils getInstance() {
        if (mInstance == null) {
            synchronized (FileUtils.class) {
                if (mInstance == null) {
                    mInstance = new FileUtils();
                }
            }
        }
        return mInstance;
    }


    /**
     * @param path
     */
    public void delete(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }


    /**
     * 清空文件夹
     *
     * @param path
     */
    public void clearDirectory(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delete(f.getAbsolutePath());
            }
        } else if (file.exists()) {
            file.delete();
        }
    }


    /**
     * 保存音频到文件
     *
     * @param data
     * @return
     */

    public String saveAudioToFile(byte[] data) {

        File file = new File(Uri.audioPath, System.currentTimeMillis() + ".mp3");

        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data, 0, data.length);
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 保存mp4文件
     *
     * @param data
     * @return
     */

    public String saveVideoFile(byte[] data) {
        // 首先保存图片
        File file = new File(Uri.videoPath, System.currentTimeMillis() + ".mp4");

        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        try {
            FileOutputStream imageOutput = new FileOutputStream(file);
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * @param file
     * @return 保存文件
     */
    public String saveFile(File file, String data) {


        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * oldPath 和 newPath必须是新旧文件的绝对路径
     */
    public void renameFile(String oldPath, String newPath) {
        File file = new File(oldPath);
        file.renameTo(new File(newPath));
    }
}
