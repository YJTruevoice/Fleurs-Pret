package com.facile.immediate.electronique.fleurs.pret.common.json.tools

import android.os.Build
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.zip.GZIPOutputStream
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


object EncryptTool {
    private const val KEY_ALGORITHM = "AES"
    private const val UNICODE_FORMAT = "UTF-8"
    private const val CIPHER_ALGORITHM = "AES/ECB/PKCS7Padding"


    @Throws(Exception::class)
    private fun toKey(key: ByteArray): Key {
        return SecretKeySpec(key, KEY_ALGORITHM)
    }

    fun AESEncrypt(data: String, key: String): String? {
        try {
            // 还原密钥
            val k: Key = toKey(key.toByteArray(charset(UNICODE_FORMAT)))

            // Security.addProvider(new BouncyCastleProvider());
            val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
            // 初始化，设置为加密模式
            cipher.init(Cipher.ENCRYPT_MODE, k)
            // 执行操作
            return bytes2String(cipher.doFinal(data.toByteArray(charset(UNICODE_FORMAT))))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun bytes2String(buf: ByteArray): String {
        val sb = StringBuffer()
        for (i in buf.indices) {
            var hex = Integer.toHexString(buf[i].toInt() and 0xFF)
            if (hex.length == 1) {
                hex = "0$hex"
            }
            sb.append(hex)
        }
        return sb.toString()
    }

    /**
     * zip
     */
    @Throws(IOException::class)
    fun compress(source: String?): String? {
        if (source.isNullOrEmpty()) {
            return source
        }
        var byteOut: ByteArrayOutputStream? = null
        val gzipOut: GZIPOutputStream
        try {
            byteOut = ByteArrayOutputStream()
            gzipOut = GZIPOutputStream(byteOut)
            gzipOut.write(source.toByteArray(StandardCharsets.UTF_8))
            gzipOut.finish()
            gzipOut.close()
            byteOut.flush()
            byteOut.close()
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                byteOut.toString(StandardCharsets.ISO_8859_1)
            } else {
                byteOut.toString("ISO-8859-1")
            }
        } finally {
            byteOut?.close()
        }
    }
}