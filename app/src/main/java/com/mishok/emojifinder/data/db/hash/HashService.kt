package com.mishok.emojifinder.data.db.hash

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.util.encoders.Base64
import java.security.Security
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


object HashService {

    private const val transformation = "AES/ECB/PKCS7Padding"
    private const val keyValue = "662ede816988e58fb6d057d9d85605e0"

    fun encrypt(strToEncrypt: String, secret_key: String = keyValue): String {
        Security.addProvider(BouncyCastleProvider())
        val keyBytes: ByteArray
        try {
            keyBytes = secret_key.toByteArray(charset("UTF8"))
            val skey = SecretKeySpec(keyBytes, "AES")
            val input = strToEncrypt.toByteArray(charset("UTF8"))

            synchronized(Cipher::class.java) {
                val cipher = Cipher.getInstance(transformation)
                cipher.init(Cipher.ENCRYPT_MODE, skey)

                val cipherText = ByteArray(cipher.getOutputSize(input.size))
                var ctLength = cipher.update(
                    input, 0, input.size,
                    cipherText, 0
                )
                ctLength += cipher.doFinal(cipherText, ctLength)
                return String(
                    Base64.encode(cipherText)
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun decrypt(key: String = keyValue, strToDecrypt: String?): String? {
        Security.addProvider(BouncyCastleProvider())
        val keyBytes: ByteArray
        try {
            keyBytes = key.toByteArray(charset("UTF8"))
            val skey = SecretKeySpec(keyBytes, "AES")
            val input = Base64.decode(strToDecrypt?.trim { it <= ' ' }?.toByteArray(charset("UTF8")))
            synchronized(Cipher::class.java) {
                val cipher = Cipher.getInstance(transformation)
                cipher.init(Cipher.DECRYPT_MODE, skey)

                val plainText = ByteArray(cipher.getOutputSize(input.size))
                var ptLength = cipher.update(input, 0, input.size, plainText, 0)
                ptLength += cipher.doFinal(plainText, ptLength)
                val decryptedString = String(plainText)
                return decryptedString.trim { it <= ' ' }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}