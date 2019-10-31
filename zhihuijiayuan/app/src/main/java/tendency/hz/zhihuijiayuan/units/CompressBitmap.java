package tendency.hz.zhihuijiayuan.units;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import tendency.hz.zhihuijiayuan.application.MyApplication;

/**
 * 定义各种压缩bitmap对象的方法
 * Created by Administrator on 2016/10/19.
 */
public class CompressBitmap {

    public static String TAG = "CompressBitmap---";

    /**
     * 通过文件路径进行压缩，以固定宽高比进行压缩
     *
     * @param filePath
     * @return
     */
    public static Bitmap compressPath(String filePath, int width, int height) {

        //2、获取图片的参数信息
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, newOpts);//此时返回bm为空
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

//        Log.e(TAG,"w->" + w + ";h->" + h + ";width->" + width + ";height->" + height);

        //3、获取缩放比例
        int be = 1;//be=1表示不缩放
        if (w > h && w > width) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / width);
        } else if (w < h && h > height) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / height);
        }
        if (be <= 0)
            be = 1;

//        Log.e(TAG,"压缩比例" + be);

        //4、获取缩放之后的图片
        newOpts.inJustDecodeBounds = false;
        newOpts.inSampleSize = be;//设置缩放比例
        bitmap = BitmapFactory.decodeFile(filePath, newOpts);

        return bitmap;
    }


    /**
     * 根据路径压缩图片并返回路径
     *
     * @param uri 图片原路径
     * @return 图片压缩后保存路径
     */
    public static String compressImage(String uri, String fileName) {
        if (FormatUtils.getInstance().isEmpty(uri)){
            ViewUnits.getInstance().showToast("照片不能为空");
            return "";
        }
        String imagePath = "";  //压缩图片后保存在本地的图片路径
        Uri uriPath = Uri.parse(uri);
        //构造图片压缩工具类
        ImageCompressUtils compress = new ImageCompressUtils();
        ImageCompressUtils.CompressOptions options = new ImageCompressUtils.CompressOptions();
        options.uri = uriPath;

        Bitmap bitmap = compress.compressFromUri(options);

        //将压缩后的bitmap保存到新的文件夹下
        File file = new File(tendency.hz.zhihuijiayuan.bean.base.Uri.imagePath);

        if (file.exists()) { //图片缓存路径存在，先清除缓存，防止垃圾文件堆积
            file.delete();
        }

        file.mkdirs(); //创建缓存路径

        File file_ = new File(tendency.hz.zhihuijiayuan.bean.base.Uri.imagePath + "/" + BaseUnits.getInstance().getSerilNumByLength(10));
        file_.mkdirs();
        File dir = new File(file_, fileName);   //将文件保存目录和保存文件名分开写

        //已经有该张图片，先删除
        if (dir.exists()) {
            dir.delete();
        }

        if (!dir.exists()) {
            try {
                dir.createNewFile();

                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dir));

                if (bitmap != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos);
                    bos.flush();
                    bos.close();
                    imagePath = dir.getAbsolutePath();
                }

            } catch (FileNotFoundException e) {
                Log.e(TAG, "FileNotFound=" + e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(TAG, "IOException" + e.toString());
                e.printStackTrace();
            }
        }

        return imagePath;
    }

    /**
     * 根据尺寸压缩图片
     *
     * @param uri
     * @param fileName
     * @return
     */
    public static String compressImageBySize(String uri, String fileName) {
        File file = new File(uri);
        Uri uri1 = Uri.fromFile(file);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(MyApplication.getInstance().getContentResolver(), uri1);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            int options = 90;
            while (baos.toByteArray().length / 1024 > 300) {
                baos.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
                options -= 10;
                if (options <= 0) break;
            }

            Log.e(TAG, "压缩后的图片大小为：" + getBitmapSize(bitmap));
            return bitmapToBase64(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 得到bitmap的大小
     */
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

    /**
     * bitmap转Base64
     *
     * @param bitmap
     * @return
     */
    private static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
