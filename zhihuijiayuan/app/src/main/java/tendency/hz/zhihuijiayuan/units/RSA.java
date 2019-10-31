package tendency.hz.zhihuijiayuan.units;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by JasonYao on 2018/8/1.
 */
public class RSA {
    private static final String TAG = "RSA--";
    private static String RSAC = "RSA/ECB/PKCS1Padding";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    public static int MAX_LEN = 40;

    //公钥加密
    public static String encryptByPublicKey(String data, String publicKey) {
        try {
            byte[] dataBytes = data.getBytes();
            byte[] keyBytes = Base64.decode(publicKey.getBytes(), Base64.DEFAULT);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            // 对数据加密
            Cipher cipher = Cipher.getInstance(RSAC);
            cipher.init(Cipher.ENCRYPT_MODE, publicK);
            byte[] cache = cipher.doFinal(dataBytes);
            byte[] encode = Base64.encode(cache, Base64.DEFAULT);
            return new String(encode, "utf-8").replaceAll("[\\s*\t\n\r]", "");
        } catch (InvalidKeyException e) {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String encryptedData, String privateKey) {
        try {
            byte[] keyBytes = Base64.decode(privateKey, Base64.DEFAULT);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(RSAC);
            cipher.init(Cipher.DECRYPT_MODE, privateK);
            byte[] encryp = Base64.decode(encryptedData.getBytes(), Base64.DEFAULT);
            byte[] decryptedData = cipher.doFinal(encryp);
            return new String(decryptedData, "utf-8");
        } catch (InvalidKeyException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.toString());

            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            Log.e(TAG, e.toString());

            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            Log.e(TAG, e.toString());

            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            Log.e(TAG, e.toString());

            e.printStackTrace();
        } catch (BadPaddingException e) {
            Log.e(TAG, e.toString());

            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.toString());

            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, e.toString());

            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将base64编码后的公钥字符串转成PublicKey实例
     *
     * @param publicKey 公钥字符
     * @return publicKEY
     * @throws Exception exception
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        byte[] keyBytes = Base64.decode(publicKey, Base64.NO_WRAP);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 将base64编码后的私钥字符串转成PrivateKey实例
     *
     * @param privateKey 私钥字符串
     * @return 私钥对象
     * @throws Exception exception
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        byte[] keyBytes = Base64.decode(privateKey, Base64.NO_WRAP);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * RSA加密
     *
     * @param content   待加密文本
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception exception
     */
    public static String encrypt(String content, String publicKey) {
        StringBuffer encryptedData = new StringBuffer();
        StringBuffer contentSb = new StringBuffer(content);
        while (contentSb.length() > 0) {
            if (contentSb.length() > MAX_LEN) {
                encryptedData.append(encryptByPublicKey(contentSb.substring(0, MAX_LEN), publicKey));
                encryptedData.append(" ");
                contentSb.delete(0, MAX_LEN);
            } else {
                encryptedData.append(encryptByPublicKey(contentSb.substring(0, contentSb.length()), publicKey));
                contentSb.delete(0, contentSb.length());
            }
        }

        return encryptedData.toString();


        /*Cipher cipher = null;//java默认"RSA"="RSA/ECB/PKCS1Padding"
        try {
            cipher = Cipher.getInstance(RSAC);
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
            byte[] data = content.getBytes();
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return new String(Base64Utils.encode(encryptedData, Base64Utils.DEFAULT), "utf-8");
        } catch (InvalidKeyException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.toString());

            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            Log.e(TAG, e.toString());

            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            Log.e(TAG, e.toString());

            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            Log.e(TAG, e.toString());

            e.printStackTrace();
        } catch (BadPaddingException e) {
            Log.e(TAG, e.toString());

            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.toString());

            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, e.toString());

            e.printStackTrace();
        }
        return null;*/

    }

    /**
     * RSA解密
     *
     * @param content    密文
     * @param privateKey 私钥
     * @return 明文
     * @throws Exception exception
     */
    public static String decrypt(String content, String privateKey) {
        String[] contents = content.split(" ");
        StringBuffer stringBuffer = new StringBuffer();
        for (String temp : contents) {
            Log.e(TAG, "原字符：" + temp);
            stringBuffer.append(decryptByPrivateKey(temp, privateKey));
            Log.e(TAG, "解密字符：" + decryptByPrivateKey(temp, privateKey));
        }

        return stringBuffer.toString();

        /*Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(RSAC);
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
            byte[] encryptedData = Base64Utils.decode(content, Base64Utils.DEFAULT);
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return new String(decryptedData, "utf-8");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            Log.e(TAG, e.toString());

            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            Log.e(TAG, e.toString());

            e.printStackTrace();
        } catch (BadPaddingException e) {
            Log.e(TAG, e.toString());

            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.toString());

            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, e.toString());

            e.printStackTrace();
        }
        return null;*/
    }
}
