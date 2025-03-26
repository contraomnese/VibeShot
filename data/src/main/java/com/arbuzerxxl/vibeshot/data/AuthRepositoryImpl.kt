package com.arbuzerxxl.vibeshot.data

import com.arbuzerxxl.vibeshot.data.generators.AuthSteps
import com.arbuzerxxl.vibeshot.data.generators.generateSignature
import com.arbuzerxxl.vibeshot.data.network.api.FlickrAuthApi
import com.arbuzerxxl.vibeshot.domain.models.AccessToken
import com.arbuzerxxl.vibeshot.domain.models.RequestTokenDomain
import com.arbuzerxxl.vibeshot.domain.models.User
import com.arbuzerxxl.vibeshot.domain.models.Verifier
import com.arbuzerxxl.vibeshot.domain.repository.AuthRepository
import java.io.IOException
import java.util.UUID

private const val CRYPTO_METHOD = "HMAC-SHA1"
private const val OAUTH_VERSION = "1.0"
private const val CALLBACK = "http://www.wackylabs.net/oauth/test"

class AuthRepositoryImpl(private val api: FlickrAuthApi, private val apiKey: String, private val apiToken: String) :
    AuthRepository {
    override suspend fun getRequestToken(): RequestTokenDomain {

        val nonce = UUID.randomUUID().toString()
        val timeStamp = System.currentTimeMillis().toString()

        val signature = generateSignature(
            step = AuthSteps.REQUEST_TOKEN,
            nonce = nonce,
            timestamp = timeStamp,
            consumerKey = apiKey,
            method = CRYPTO_METHOD,
            oauthVersion = OAUTH_VERSION,
            callback = CALLBACK,
            secret = apiToken
        )

        try {
            val requestTokenApi = api.getRequestToken(
                nonce = nonce,
                timestamp = timeStamp,
                consumerKey = apiKey,
                signature = signature,
                method = CRYPTO_METHOD,
                oauthVersion = OAUTH_VERSION,
                callback = CALLBACK
            )
            return parseTokenResponse(requestTokenApi) ?: throw IllegalStateException("Invalid token response")
        } catch (e: Exception) {
            throw IOException("Failed to fetch token", e)
        }
    }

    override suspend fun authorize(): Verifier {
        return Verifier(verifier = "equidem")
    }

    override suspend fun getAccessToken(): AccessToken {
        return AccessToken(
            accessToken = "necessitatibus",
            accessTokenSecret = "atomorum",
            user = User(id = "malesuada", username = "Derek Mayo")
        )
    }

    private fun parseTokenResponse(response: String): RequestTokenDomain? {

        val params = response.split("&").associate {
            val parts = it.split("=")
            if (parts.size == 2) parts[0] to parts[1] else parts[0] to ""
        }

        return if (params.containsKey("oauth_token") && params.containsKey("oauth_token_secret")) {
            RequestTokenDomain(
                requestToken = params["oauth_token"]!!,
                requestTokenSecret = params["oauth_token_secret"]!!
            )
        } else {
            null
        }
    }
}