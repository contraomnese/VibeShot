package com.arbuzerxxl.vibeshot.data.utils


import java.net.URLEncoder
import java.util.Base64
import java.util.TreeMap
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class PostNetworkParams(
    private val baseUrl: String,
    private val token: String,
    private val nonce: String,
    private val timestamp: String,
    private val consumerKey: String,
    private val consumerSecret: String,
    private val tokenSecret: String,
    private val title: String,
    private val description: String,
    private val tags: String,
) {
    companion object {
        const val oauthVersion = "1.0"
        const val encryptionMethod = "HMAC-SHA1"
        private const val algorithm = "HmacSHA1"
    }

    val signature: String
    val authorizationHeader: String

    init {
        signature = generateSignature()
        authorizationHeader = generateAuthorization()
    }

    private fun generateSignature(): String {
        val baseString = generateSignatureBaseString()
        val signingKey = "${consumerSecret.rfc3986Encode()}&${tokenSecret.rfc3986Encode()}"
        val mac = Mac.getInstance(algorithm)
        val keySpec = SecretKeySpec(signingKey.toByteArray(Charsets.UTF_8), algorithm)
        mac.init(keySpec)
        val rawHmac = mac.doFinal(baseString.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(rawHmac)
    }

    private fun generateSignatureBaseString(): String {
        val params = TreeMap<String, String>()
        params["oauth_consumer_key"] = consumerKey
        params["oauth_nonce"] = nonce
        params["oauth_signature_method"] = encryptionMethod
        params["oauth_timestamp"] = timestamp
        params["oauth_token"] = token
        params["oauth_version"] = oauthVersion
        params["title"] = title
        params["description"] = description
        params["tags"] = tags

        val paramString = params.entries.joinToString("&") { (key, value) ->
            "${key.rfc3986Encode()}=${value.rfc3986Encode()}"
        }

        val encodedUrl = baseUrl.rfc3986Encode()
        val encodedParams = paramString.rfc3986Encode()
        return "POST&$encodedUrl&$encodedParams"
    }

    private fun generateAuthorization(): String {
        return buildString {
            append("OAuth ")
            append("oauth_consumer_key=\"${consumerKey.rfc3986Encode()}\", ")
            append("oauth_token=\"${token.rfc3986Encode()}\", ")
            append("oauth_signature_method=\"${encryptionMethod.rfc3986Encode()}\", ")
            append("oauth_timestamp=\"${timestamp.rfc3986Encode()}\", ")
            append("oauth_nonce=\"${nonce.rfc3986Encode()}\", ")
            append("oauth_version=\"${oauthVersion.rfc3986Encode()}\", ")
            append("oauth_signature=\"${signature.rfc3986Encode()}\"")
        }
    }

    private fun String.rfc3986Encode(): String =
        URLEncoder.encode(this, "UTF-8")
            .replace("+", "%20")
            .replace("*", "%2A")
            .replace("%7E", "~")
}
