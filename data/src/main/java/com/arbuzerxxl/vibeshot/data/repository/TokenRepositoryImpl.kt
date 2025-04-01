package com.arbuzerxxl.vibeshot.data.repository

import com.arbuzerxxl.vibeshot.data.network.model.OAuthRequestToken

import com.arbuzerxxl.vibeshot.data.network.api.FlickrAuthApi
import com.arbuzerxxl.vibeshot.data.network.model.OAuthAccessToken
import com.arbuzerxxl.vibeshot.domain.models.auth.tokens.RequestToken
import com.arbuzerxxl.vibeshot.domain.models.responses.AccessTokenResponse
import com.arbuzerxxl.vibeshot.domain.models.responses.RequestTokenResponse
import com.arbuzerxxl.vibeshot.domain.repository.TokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

enum class OAuthFlow(val step: String) {
    REQUEST_TOKEN("oauth/request_token"),
    ACCESS_TOKEN("oauth/access_token"),
    AUTHORIZE("oauth/authorize"),
}

class TokenRepositoryImpl(
    private val api: FlickrAuthApi,
    private val apiKey: String,
    private val apiToken: String,
    private val apiBaseUrl: String,
    private val apiCallback: String,
) : TokenRepository {

    private lateinit var requestToken: RequestToken

    private suspend fun getRequestToken(): RequestToken {

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
            val tokenResponse = parseRequestTokenResponse(requestTokenApi)
            tokenResponse.oAuthToken.let { token ->
                tokenResponse.oAuthTokenSecret.let { secret ->
                    return RequestToken(
                        token = token,
                        secret = secret
                    )
                }
            }
        } catch (e: Exception) {
            throw IOException("Failed to fetch request token", e)
        }
    }

    override suspend fun getAuthorizeUrl(): String {

        requestToken = getRequestToken()
        return "${apiBaseUrl}${OAuthFlow.AUTHORIZE.step}?oauth_token=${requestToken.token}"
    }

    override suspend fun getAccessToken(verifier: String): AccessTokenResponse {

        val oAuthRequest = OAuthAccessToken(
            baseUrl = apiBaseUrl,
            oAuthStep = OAuthFlow.ACCESS_TOKEN.step,
            consumerKey = apiKey,
            secret = apiToken,
            token = requestToken.token,
            verifier = verifier,
            secretToken = requestToken.secret
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
            val accessTokenResponse = parseAccessTokenResponse(accessTokenApi)
            return accessTokenResponse
        } catch (e: Exception) {
            throw IOException("Failed to fetch request token", e)
        }
    }

    private fun parseAccessTokenResponse(response: String): AccessTokenResponse {

        val params = parseResponse(response)

        return try {
            AccessTokenResponse(
                oAuthToken = params.get("oauth_token")!!,
                oAuthTokenSecret = params.get("oauth_token_secret")!!,
                fullname = params.get("fullname")!!,
                username = params.get("username")!!,
                userId = params.get("user_nsid")!!,
            )
        } catch (e: NullPointerException) {
            throw NullPointerException("Params can't be null")
        }
    }

    private fun parseRequestTokenResponse(response: String): RequestTokenResponse {

        val params = parseResponse(response)

        return try {
            RequestTokenResponse(
                oAuthToken = params.get("oauth_token")!!,
                oAuthTokenSecret = params.get("oauth_token_secret")!!,
            )
        } catch (e: NullPointerException) {
            throw NullPointerException("Params can't be null")
        }
    }

    private fun parseResponse(response: String): Map<String, String> {
        return response.split("&").associate {
            val parts = it.split("=")
            if (parts.size == 2) parts[0] to parts[1] else parts[0] to ""
        }
    }
}