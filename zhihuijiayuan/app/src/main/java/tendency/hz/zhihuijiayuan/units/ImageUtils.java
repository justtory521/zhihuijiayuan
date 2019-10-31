package tendency.hz.zhihuijiayuan.units;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.currentTimeMillis;

/**
 * Created by JasonYao on 2017/10/25.
 */

public class ImageUtils {
    private static final String TAG = "ImageUtils---";

    public static ImageUtils mImageUtils = null;

    private ImageUtils() {
    }

    /**
     * 获取单例对象
     *
     * @return
     */
    public static ImageUtils getInstance() {
        if (mImageUtils == null) {
            synchronized (ImageUtils.class) {
                if (mImageUtils == null) {
                    mImageUtils = new ImageUtils();
                }
            }
        }

        return mImageUtils;
    }

    /**
     * 根据图片名称生成图片文件
     *
     * @param name
     * @return
     */
    public static File createImageFile(Context context, String name) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "用户未授权");
        } else {
            Log.e(TAG, "用户已授权");
        }

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File image = null;
        try {
            image = File.createTempFile(name, ".png", storageDir);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        return image;
    }

    /**
     * 通过文件获取Uri
     *
     * @param context
     * @param file
     * @return
     */
    public static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }

        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {  //android版本大于N
            uri = BuglyFileProvider.getUriForFile(context.getApplicationContext(), "tendency.hz.zhihuijiayuan.fileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }

        Log.e(TAG, "图片=" + uri.toString());

        return uri;
    }

    /**
     * 生成二维码
     *
     * @param content
     * @param width
     * @param height
     * @return
     */
    public static Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据图片路径，将图片转为二进制
     *
     * @param path
     * @return
     */
    public byte[] image2byte(String path) {
        byte[] data = null;
        FileInputStream input = null;
        try {
            input = new FileInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (FileNotFoundException ex1) {
            Log.e(TAG, "FileNotFoundException:" + ex1.toString());
            ex1.printStackTrace();
        } catch (IOException ex1) {
            Log.e(TAG, "IOException:" + ex1.toString());
            ex1.printStackTrace();
        }

        return data;
    }


    /**
     * 保存文件到系统相册
     *
     * @param data
     * @return
     */

    public static String saveImgToGallery(byte[] data) {
        // 首先保存图片
        File file = new File(tendency.hz.zhihuijiayuan.bean.base.Uri.tdrPath, System.currentTimeMillis() + ".png");

        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        try {
            FileOutputStream imageOutput = new FileOutputStream(file);
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            return file.getName();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
