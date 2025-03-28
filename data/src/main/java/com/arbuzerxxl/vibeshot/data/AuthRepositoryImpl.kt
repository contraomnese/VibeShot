package com.arbuzerxxl.vibeshot.data

import com.arbuzerxxl.vibeshot.data.network.model.OAuthRequestToken

import com.arbuzerxxl.vibeshot.data.network.api.FlickrAuthApi
import com.arbuzerxxl.vibeshot.data.network.model.OAuthAccessToken
import com.arbuzerxxl.vibeshot.domain.models.AccessTokenDomain
import com.arbuzerxxl.vibeshot.domain.models.RequestTokenDomain
import com.arbuzerxxl.vibeshot.domain.models.TokenResponse
import com.arbuzerxxl.vibeshot.domain.models.User
import com.arbuzerxxl.vibeshot.domain.repository.AuthRepository
import java.io.IOException

enum class OAuthFlow(val step: String) {
    REQUEST_TOKEN("oauth/request_token"),
    ACCESS_TOKEN("oauth/access_token"),
    AUTHORIZE("oauth/authorize"),
}

class AuthRepositoryImpl(
    private val api: FlickrAuthApi,
    private val apiKey: String,
    private val apiToken: String,
    private val apiBaseUrl: String,
    private val apiCallback: String,
) : AuthRepository {

    override suspend fun getRequestToken(): RequestTokenDomain {

        val oAuthRequest = OAuthRequestToken(
            baseUrl = apiBaseUrl,
            oAuthStep = OAuthFlow.REQUEST_TOKEN.step,
            consumerKey = apiKey,
            callback = apiCallback,
            secret = apiToken
        )

        try {
            val requestTokenApi = api.getRequestToken(
                nonce = oAuthRequest.nonce,
                timestamp = oAuthRequest.timestamp,
                consumerKey = oAuthRequest.consumerKey,
                signature = oAuthRequest.signature,
                method = oAuthRequest.method,
                oauthVersion = oAuthRequest.oAuthVersion,
                callback = oAuthRequest.callback
            )
            val tokenResponse = parseResponse(requestTokenApi)
            tokenResponse.oAuthToken?.let { token ->
                tokenResponse.oAuthTokenSecret?.let { secret ->
                    return RequestTokenDomain(
                        requestToken = token,
                        requestTokenSecret = secret
                    )
                } ?: throw IllegalStateException("Invalid secret token response")
            } ?: throw IllegalStateException("Invalid token response")
        } catch (e: Exception) {
            throw IOException("Failed to fetch request token", e)
        }
    }

    override fun getAuthorizeUrl(requestToken: String): String {
        return "${apiBaseUrl}${OAuthFlow.AUTHORIZE.step}?oauth_token=${requestToken}"
    }

    override suspend fun getAccessToken(requestToken: String, verifier: String, secretToken: String): AccessTokenDomain {

        val oAuthRequest = OAuthAccessToken(
            baseUrl = apiBaseUrl,
            oAuthStep = OAuthFlow.ACCESS_TOKEN.step,
            consumerKey = apiKey,
            secret = apiToken,
            token = requestToken,
            verifier = verifier,
            secretToken = secretToken
        )

        try {
            val accessTokenApi = api.getAccessToken(
                nonce = oAuthRequest.nonce,
                timestamp = oAuthRequest.timestamp,
                consumerKey = oAuthRequest.consumerKey,
                signature = oAuthRequest.signature,
                method = oAuthRequest.method,
                oauthVersion = oAuthRequest.oAuthVersion,
                token = oAuthRequest.token,
                verifier = oAuthRequest.verifier
            )
            val tokenResponse = parseResponse(accessTokenApi)
            tokenResponse.oAuthToken?.let { token ->
                tokenResponse.oAuthTokenSecret?.let { secret ->
                    return AccessTokenDomain(
                        accessToken = token,
                        accessTokenSecret = secret,
                        user = User(
                            id = tokenResponse.userId!!,
                            username = tokenResponse.userName!!,
                            fullname = tokenResponse.fullName!!
                        )

                    )
                } ?: throw IllegalStateException("Invalid secret token response")
            } ?: throw IllegalStateException("Invalid token response")
        } catch (e: Exception) {
            throw IOException("Failed to fetch request token", e)
        }
    }

    private fun parseResponse(response: String): TokenResponse {

        val params = response.split("&").associate {
            val parts = it.split("=")
            if (parts.size == 2) parts[0] to parts[1] else parts[0] to ""
        }

        return TokenResponse(
            oAuthToken = params.getOrDefault("oauth_token", null),
            oAuthTokenSecret = params.getOrDefault("oauth_token_secret", null),
            fullName = params.getOrDefault("fullname", null),
            userName = params.getOrDefault("username", null),
            userId = params.getOrDefault("user_nsid", null),
        )
    }
}