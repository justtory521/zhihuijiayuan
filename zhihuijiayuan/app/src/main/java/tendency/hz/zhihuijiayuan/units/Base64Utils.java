package tendency.hz.zhihuijiayuan.units;

import android.util.Base64;
import android.util.Log;

import com.cjt2325.cameralibrary.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import tendency.hz.zhihuijiayuan.bean.base.Uri;

/**
 * @author: XieminQuan
 * @time : 2007-11-20 下午04:10:22
 * <p>
 * DNAPAY
 */

public class Base64Utils {

    private static char[] codec_table = {'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', '+', '/'};

    public Base64Utils() {

    }


    /**
     * @param data
     * @return 解码
     */
    public static byte[] decode(String data) {
        return Base64.decode(data.getBytes(), Base64.NO_WRAP);
    }

    /**
     * @param
     * @return
     */
    @SuppressWarnings("restriction")
//    public static byte[] decode(String data) {
//
//        if (data == null || "".equals(data))
//            return null;
//
//        Class classes = null;
//        try {
//            classes = Class.forName("Decoder.BASE64Decoder");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        Method method = null;
//        try {
//            method = classes.getMethod("decode", String.class);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//        method.setAccessible(true);
//        try {
//            method.invoke(data);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        try {
//            return (byte[]) method.invoke(data);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    /**
     * @param data
     * @return 编码去空格  js交互
     */
    public static String encode(byte[] data) {

        return Base64.encodeToString(data, Base64.NO_WRAP);
    }

    /**
     * @param data
     * @return 编码
     */
    public static String encodeDefault(byte[] data) {

        return Base64.encodeToString(data, Base64.DEFAULT);
    }


    public static String encodePicture(byte[] a) {

        int totalBits = a.length * 8;
        int nn = totalBits % 6;
        int curPos = 0;// process bits
        StringBuffer toReturn = new StringBuffer();
        while (curPos < totalBits) {
            int bytePos = curPos / 8;
            switch (curPos % 8) {
                case 0:
                    toReturn.append(codec_table[(a[bytePos] & 0xfc) >> 2]);
                    break;
                case 2:

                    toReturn.append(codec_table[(a[bytePos] & 0x3f)]);
                    break;
                case 4:
                    if (bytePos == a.length - 1) {
                        toReturn.append(codec_table[((a[bytePos] & 0x0f) << 2) & 0x3f]);
                    } else {
                        int pos = (((a[bytePos] & 0x0f) << 2) | ((a[bytePos + 1] & 0xc0) >> 6)) & 0x3f;
                        toReturn.append(codec_table[pos]);
                    }
                    break;
                case 6:
                    if (bytePos == a.length - 1) {
                        toReturn.append(codec_table[((a[bytePos] & 0x03) << 4) & 0x3f]);
                    } else {
                        int pos = (((a[bytePos] & 0x03) << 4) | ((a[bytePos + 1] & 0xf0) >> 4)) & 0x3f;
                        toReturn.append(codec_table[pos]);
                    }
                    break;
                default:
                    //never hanppen
                    break;
            }
            curPos += 6;
        }
        if (nn == 2) {
            toReturn.append("==");
        } else if (nn == 4) {
            toReturn.append("=");
        }
        return toReturn.toString();

    }


    public static String fileToBase64(String path) {
        byte[] buffer = null;
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encode(buffer);
    }

}
