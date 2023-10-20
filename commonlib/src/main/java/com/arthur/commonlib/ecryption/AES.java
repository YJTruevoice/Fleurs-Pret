package com.arthur.commonlib.ecryption;

import android.text.TextUtils;
import android.util.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    private final static String CODE_MODE = "AES";
    private final static String CIPHERMODEPADDING = "AES/CBC/PKCS7Padding";

    private final static byte[] iv = {0xA, 1, 0xB, 5, 4, 0xF, 7, 9, 0x17, 3,
            1, 6, 8, 0xC, 0xD, 91};

    private static final IvParameterSpec IV = new IvParameterSpec(iv);

    private static byte[] md5GeneralKey(byte[] password) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(password);

            return md5.digest();
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

    private static SecretKey generalSeed(byte[] password) {
        SecretKeySpec sk = null;
        byte[] key = md5GeneralKey(password);
        if (key != null) {
            sk = new SecretKeySpec(key, CODE_MODE);
        }
        return sk;
    }

    /**
     * 加密
     *
     * @param plaintext 要加密的数组
     * @param password  加密的key
     * @return 加密后的结果
     */

    public static byte[] bytesEncrypt(byte[] plaintext, String password) {
        SecretKey sk = generalSeed(password.getBytes());

        return encrypt(CIPHERMODEPADDING, sk, IV, plaintext);
    }

    /**
     * 使用原始密钥进行加密，不对密钥进行md5
     *
     * @param plaintext         要加密的内容
     * @param password          密钥
     * @param cipherModePadding 加密填充方式
     * @param iv                如果为空使用默认iv
     * @return 加密后的byte数组
     */
    public static byte[] bytesEncryptWithRawPassWord(byte[] plaintext, String password, String cipherModePadding, String iv) {
        SecretKey sk = new SecretKeySpec(password.getBytes(), "AES");
        IvParameterSpec mIV = IV;
        if (!TextUtils.isEmpty(iv)) {
            mIV = new IvParameterSpec(iv.getBytes());
        }
        return encrypt(cipherModePadding, sk, mIV, plaintext);
    }


    /**
     * 使用原始密钥进行解密，不对密钥进行md5
     *
     * @param plaintext         要解密的内容
     * @param password          密钥
     * @param cipherModePadding 加密填充方式
     * @param iv                如果为空使用默认iv
     * @return 加密后的byte数组
     */
    public static byte[] bytesDecryptWithRawPassWord(byte[] plaintext, String password, String cipherModePadding, String iv) {
        SecretKey sk = new SecretKeySpec(password.getBytes(), "AES");
        IvParameterSpec mIV = IV;
        if (!TextUtils.isEmpty(iv)) {
            mIV = new IvParameterSpec(iv.getBytes());
        }
        return decrypt(cipherModePadding, sk, mIV, plaintext, 0, plaintext.length);
    }

    /**
     * 解密
     *
     * @param plaintext 要解密的数组
     * @param password  解密的key
     * @param offset    解密的起始点
     * @param length    需要解密的字符长度
     * @return 解密后的结果
     */
    public static byte[] bytesDecrypt(byte[] plaintext, int offset, int length,
                                      String password) {
        SecretKey sk = generalSeed(password.getBytes());
        return decrypt(CIPHERMODEPADDING, sk, IV, plaintext, offset, length);
    }

    /**
     * 解密
     *
     * @param plaintext 要解密的数组
     * @param password  解密的key
     * @return 解密后的结果
     */
    public static byte[] bytesDecrypt(byte[] plaintext, String password) {
        return bytesDecrypt(plaintext, 0, plaintext.length, password);
    }

    private static byte[] encrypt(String cmp, SecretKey sk, IvParameterSpec IV,
                                  byte[] msg) {
        try {
            Cipher c = Cipher.getInstance(cmp);
            c.init(Cipher.ENCRYPT_MODE, sk, IV);
            return c.doFinal(msg);
        } catch (NoSuchAlgorithmException nsae) {
        } catch (NoSuchPaddingException nspe) {
        } catch (InvalidKeyException e) {
        } catch (InvalidAlgorithmParameterException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (BadPaddingException e) {
        }
        return null;
    }

    private static byte[] decrypt(String cmp, SecretKey sk, IvParameterSpec IV,
                                  byte[] ciphertext, int offset, int length) {
        try {
            Cipher c = Cipher.getInstance(cmp);
            c.init(Cipher.DECRYPT_MODE, sk, IV);
            return c.doFinal(ciphertext, offset, length);
        } catch (NoSuchAlgorithmException nsae) {
        } catch (NoSuchPaddingException nspe) {
        } catch (InvalidKeyException e) {
        } catch (InvalidAlgorithmParameterException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (BadPaddingException e) {
        }
        return null;
    }


    /**
     * 加密
     *
     * @param plaintext 要加密的字符串
     * @param password  加密的key
     * @return 加密后的String
     */
    public static String encrypt(String plaintext, String password) {
        return encrypt(plaintext.getBytes(), password);
    }

    /**
     * 加密
     *
     * @param plaintext 要加密的数组
     * @param password  加密的key
     * @return 加密后的String
     */
    public static String encrypt(byte[] plaintext, String password) {
        byte[] ciphertext = bytesEncrypt(plaintext, password);
        byte[] base64_ciphertext = Base64.encode(ciphertext,
                Base64.DEFAULT);

        String decrypted = new String(base64_ciphertext);
        return decrypted;
    }

    /**
     * 解密
     *
     * @param string   要解密的字符串
     * @param password 解密的key
     * @return 解密的结果
     */
    public static String decrypt(String string, String password) {
        try {
            if (TextUtils.isEmpty(string)) {
                return "";
            } else {
                byte[] s = Base64.decode(string, Base64.DEFAULT);
                String decrypted = new String(bytesDecrypt(s, password));
                return decrypted;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 加密：使用原始秘钥，不MD5
     *
     * @param string   要解密的字符串
     * @param password 解密的key
     * @param iv       iv
     * @return 加密的结果
     */
    public static String encrypt(String string, String password, String iv) {
        try {
            if (TextUtils.isEmpty(string)) {
                return "";
            } else {
                byte[] s = Base64.decode(string, Base64.DEFAULT);
                return new String(bytesEncryptWithRawPassWord(s, password, CIPHERMODEPADDING, iv));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 解密：使用原始秘钥，不MD5
     *
     * @param string   要解密的字符串
     * @param password 解密的key
     * @param iv       iv
     * @return 解密的结果
     */
    public static String decrypt(String string, String password, String iv) {
        try {
            if (TextUtils.isEmpty(string)) {
                return "";
            } else {
                byte[] s = Base64.decode(string, Base64.DEFAULT);
                return new String(bytesDecryptWithRawPassWord(s, password, CIPHERMODEPADDING, iv));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
