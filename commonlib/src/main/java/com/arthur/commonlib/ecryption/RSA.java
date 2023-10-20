package com.arthur.commonlib.ecryption;

import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * RSA加解密工具
 */
public class RSA {
    private static String RSA_PUBLIC = "";
    private static final String ALGORITHM = "RSA";

    /**
     * 得到公钥
     *
     * @param algorithm
     * @param bysKey
     * @return
     */
    private static PublicKey getPublicKeyFromX509(String algorithm, String bysKey) {
        byte[] decodedKey = Base64.decode(bysKey, Base64.DEFAULT);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance(algorithm);
            return keyFactory.generatePublic(x509);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("获取公约异常");

        }
        return null;
    }

    /**
     * 设置加密的key
     *
     * @param secretKey
     */
    public static void setRsaPublicKey(String secretKey) {
        RSA_PUBLIC = secretKey;
    }

    /**
     * 使用公钥加密
     *
     * @param content
     * @return
     */
    public static String encryptByPublic(String content) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, RSA_PUBLIC);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);
            byte[] data = content.getBytes(StandardCharsets.UTF_8);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > 117) {
                    cache = cipher.doFinal(data, offSet, 117);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * 117;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return new String(Base64.encode(encryptedData, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("RSA", "RSA加密失败：" + e.getMessage());
            return null;
        }
    }


    /**
     * 使用公钥解密
     *
     * @param content 密文
     * @return 解密后的字符串
     */
    public static String decryptByPublic(String content) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, RSA_PUBLIC);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, pubkey);
            //InputStream ins = new ByteArrayInputStream(Base64.getDecoder().decode(content));
            InputStream ins = new ByteArrayInputStream(Base64.decode(content, Base64.DEFAULT));
            ByteArrayOutputStream writer = new ByteArrayOutputStream();
            byte[] buf = new byte[128];
            int bufl;
            while ((bufl = ins.read(buf)) != -1) {
                byte[] block = null;
                if (buf.length == bufl) {
                    block = buf;
                } else {
                    block = new byte[bufl];
                    if (bufl >= 0) System.arraycopy(buf, 0, block, 0, bufl);
                }
                writer.write(cipher.doFinal(block));
            }
            return new String(writer.toByteArray(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 使用公钥加密
     *
     * @param content   内容
     * @param publicKey 公钥
     * @return String
     */
    public static String encryptByPublic(String content, String publicKey) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, publicKey);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);
            byte[] data = content.getBytes(StandardCharsets.UTF_8);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > 117) {
                    cache = cipher.doFinal(data, offSet, 117);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * 117;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return new String(Base64.encode(encryptedData, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("RSA", "RSA加密失败：" + e.getMessage());
            return null;
        }
    }


    /**
     * 使用公钥解密
     *
     * @param content 密文
     * @return 解密后的字符串
     */
    public static String decryptByPublic(String content, String publicKey) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, publicKey);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, pubkey);
            //InputStream ins = new ByteArrayInputStream(Base64.getDecoder().decode(content));
            InputStream ins = new ByteArrayInputStream(Base64.decode(content, Base64.DEFAULT));
            ByteArrayOutputStream writer = new ByteArrayOutputStream();
            byte[] buf = new byte[128];
            int bufl;
            while ((bufl = ins.read(buf)) != -1) {
                byte[] block = null;
                if (buf.length == bufl) {
                    block = buf;
                } else {
                    block = new byte[bufl];
                    if (bufl >= 0) System.arraycopy(buf, 0, block, 0, bufl);
                }
                writer.write(cipher.doFinal(block));
            }
            return new String(writer.toByteArray(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

}