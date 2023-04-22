package com.isp.restaurantapp.models

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object Encryption {
    fun encryptSHA256(stringToEncrypt: String): String{
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val hashBytes = messageDigest.digest(stringToEncrypt.toByteArray(StandardCharsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}