package com.arbuzerxxl.vibeshot.data.generators

import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private const val ALGORITHM = "HmacSHA1"

enum class AuthSteps(val value: String) {
    REQUEST_TOKEN("request_token"),
    ACCESS_TOKEN("access_token"),
    AUTHORIZE("authorize"),
}

fun generateSignature(
    step: AuthSteps,
    nonce: String,
    timestamp: String,
    consumerKey: String,
    method: String,
    oauthVersion: String,
    callback: String,
    secret: String
): String {
    val baseString =  "GET&https%3A%2F%2Fwww.flickr.com%2Fservices%2Foauth%2F${step.value}&" +
            "oauth_callback%3Dhttp%253A%252F%252Fwww.wackylabs.net%252Foauth%252Ftest%26" +
            "oauth_consumer_key%3D${consumerKey}%26" +
            "oauth_nonce%3D${nonce}%26" +
            "oauth_signature_method%3D${method}%26" +
            "oauth_timestamp%3D${timestamp}%26" +
            "oauth_version%3D${oauthVersion}"

    println(baseString)

    val key = "$secret&"

    val hMacSHA256 = Mac.getInstance(ALGORITHM)

    val secretKey = SecretKeySpec(key.toByteArray(), ALGORITHM)
    hMacSHA256.init(secretKey)
    val hmacString = hMacSHA256.doFinal(baseString.toByteArray())
    return Base64.getEncoder().encodeToString(hmacString)
}