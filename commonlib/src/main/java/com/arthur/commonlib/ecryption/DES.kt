package com.arthur.commonlib.ecryption

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

/**
 * 加解密工具类
 */
class DES {

    companion object {

        /**
         * des加密
         */
        fun desEncrypt(source: String, password: String): String? {
            if (source.isBlank()) {
                return null
            }
            val dataSource = source.toByteArray()
            try {
                val random = SecureRandom()
                val desKey = DESKeySpec(password.toByteArray())

                // 创建一个密匙工厂，然后用它把DESKeySpec转换成
                val keyFactory = SecretKeyFactory.getInstance("DES")
                val secureKey = keyFactory.generateSecret(desKey)

                // Cipher对象实际完成加密操作
                val cipher = Cipher.getInstance("DES")

                // 用密匙初始化Cipher对象
                cipher.init(Cipher.ENCRYPT_MODE, secureKey, random)

                // 现在，获取数据并加密
                // 正式执行加密操作
                val result = cipher.doFinal(dataSource)
                return String(Base64.encode(result, Base64.DEFAULT))
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return null
        }

        /**
         * des解密
         */
        @kotlin.jvm.Throws(Exception::class)
        fun desDecrypt(des: String, password: String): String {

            // DES算法要求有一个可信任的随机数源
            val random = SecureRandom()

            // 创建一个DESKeySpec对象
            val desKey = DESKeySpec(password.toByteArray())

            // 创建一个密匙工厂
            val keyFactory = SecretKeyFactory.getInstance("DES")

            // 将DESKeySpec对象转换成SecretKey对象
            val securekey = keyFactory.generateSecret(desKey)

            // Cipher对象实际完成解密操作
            val cipher = Cipher.getInstance("DES")

            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, random)

            // 真正开始解密操作
            val result = cipher.doFinal(Base64.decode(des, Base64.DEFAULT))
            return String(result)
        }

        private fun toByte(hexString: String): ByteArray {
            val len = hexString.length / 2
            val result = ByteArray(len)
            for (i in 0 until len) {
                result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).toByte()
            }
            return result
        }

        private fun toHex(buf: ByteArray?): String? {
            if (buf == null) {
                return ""
            }
            val result = StringBuffer(2 * buf.size)
            for (i in buf.indices) {
                appendHex(result, buf[i])
            }
            return result.toString()
        }

        private const val HEX = "0123456789ABCDEF"
        private fun appendHex(sb: StringBuffer, b: Byte) {
            sb.append(HEX[b.toInt() shr 4 and 0x0f])
                .append(HEX[b.toInt() and 0x0f])
        }

    }

}