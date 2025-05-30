package com.arbuzerxxl.vibeshot.data.network.model.request.tokens

import okhttp3.internal.toHexString
import java.util.Base64
import java.util.UUID
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private const val ALGORITHM = "HmacSHA1"
private const val OAUTH_VERSION = "1.0"
private const val ENCRYPTION_METHOD = "HMAC-SHA1"

data class AccessTokenRequest(
    val baseUrl: String,
    val oAuthStep: String,
    val token: String,
    val verifier: String,
    val nonce: String = UUID.randomUUID().toString(),
    val timestamp: String = System.currentTimeMillis().toString(),
    val consumerKey: String,
    val method: String = ENCRYPTION_METHOD,
    val oAuthVersion: String = OAUTH_VERSION,
    val secret: String,
    val secretToken: String
) {
    val signature: String = generateSignature()

    private fun generateSignature(): String {

        val baseString = this.generateBaseString()

        val key = "$secret&$secretToken"
        val hMacSHA256 = Mac.getInstance(ALGORITHM)
        val secretKey = SecretKeySpec(key.toByteArray(), ALGORITHM)
        hMacSHA256.init(secretKey)
        val hmacString = hMacSHA256.doFinal(baseString.toByteArray())

        return Base64.getEncoder().encodeToString(hmacString)
    }

    private fun String.convertToBaseString(): String {

        val oAuthRegex = listOf<Char>('~', '.', '_', '-')
        val baseString = StringBuilder()
        this.forEach { symbol ->
            if (symbol.isLetterOrDigit() || oAuthRegex.contains(symbol)) baseString.append(symbol)
            else baseString.append("%${symbol.code.toHexString().uppercase()}")
        }
        return baseString.toString()
    }

    // The base string is constructed by concatenating the HTTP verb,
    // the request URL, and all request parameters sorted by name,
    // using lexicographical byte value ordering, separated by an '&'
    private fun generateBaseString(): String = "GET&" +
            "${(baseUrl + oAuthStep).convertToBaseString()}&" +
            "oauth_consumer_key=$consumerKey&".convertToBaseString() +
            "oauth_nonce=$nonce&".convertToBaseString() +
            "oauth_signature_method=$method&".convertToBaseString() +
            "oauth_timestamp=$timestamp&".convertToBaseString() +
            "oauth_token=$token&".convertToBaseString() +
            "oauth_verifier=$verifier&".convertToBaseString() +
            "oauth_version=$oAuthVersion".convertToBaseString()
}