package com.arbuzerxxl.vibeshot.data.repository

import com.arbuzerxxl.vibeshot.data.exceptions.AccessTokenFetchException
import com.arbuzerxxl.vibeshot.data.exceptions.AccessTokenInvalidResponseException
import com.arbuzerxxl.vibeshot.data.exceptions.RequestTokenFetchException
import com.arbuzerxxl.vibeshot.data.exceptions.RequestTokenInitializeException
import com.arbuzerxxl.vibeshot.data.exceptions.RequestTokenInvalidResponseException
import com.arbuzerxxl.vibeshot.data.network.api.FlickrAuthApi
import com.arbuzerxxl.vibeshot.data.network.model.tokens.AccessTokenRequest
import com.arbuzerxxl.vibeshot.data.network.model.tokens.RequestTokenRequest
import com.arbuzerxxl.vibeshot.domain.models.auth.User
import com.arbuzerxxl.vibeshot.domain.models.auth.tokens.AccessToken
import com.arbuzerxxl.vibeshot.domain.models.auth.tokens.RequestToken
import com.arbuzerxxl.vibeshot.domain.repository.TokenRepository
import kotlinx.coroutines.CancellationException

private const val FLICKR_PARAMS_ID = "user_nsid"
private const val FLICKR_PARAMS_USERNAME = "username"
private const val FLICKR_PARAMS_FULLNAME = "fullname"
private const val FLICKR_PARAMS_TOKEN = "oauth_token"
private const val FLICKR_PARAMS_SECRET = "oauth_token_secret"

private enum class OAuthFlow(val step: String) {
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

    private var requestToken: RequestToken? = null

    override suspend fun getAuthorizeUrlAndSaveRequestToken(): String =
        try {
            requestToken = getRequestToken()
            "${apiBaseUrl}${OAuthFlow.AUTHORIZE.step}?oauth_token=${requestToken!!.token}"
        } catch (cause: Throwable) {
            throw RequestTokenInitializeException(cause)
        }

    private suspend fun getRequestToken(): RequestToken {
        val request = generateRequestTokenRequest()
        val response = getRequestTokenResponse(request)
        return parseRequestTokenResponse(response)
    }

    private fun generateRequestTokenRequest(): RequestTokenRequest = RequestTokenRequest(
        baseUrl = apiBaseUrl,
        oAuthStep = OAuthFlow.REQUEST_TOKEN.step,
        consumerKey = apiKey,
        callback = apiCallback,
        secret = apiToken
    )

    private suspend fun getRequestTokenResponse(request: RequestTokenRequest): String =
        try {
            api.getRequestToken(
                nonce = request.nonce,
                timestamp = request.timestamp,
                consumerKey = request.consumerKey,
                signature = request.signature,
                method = request.method,
                oauthVersion = request.oAuthVersion,
                callback = request.callback
            )
        } catch (cause: Throwable) {
            if (cause is CancellationException) throw cause
            throw RequestTokenFetchException(cause)
        }

    override suspend fun getUserBy(verifier: String): User {

        val request = generateAccessTokenRequest(verifier)
        val response = getAccessTokenResponse(request)
        return parseAccessTokenResponse(response)
    }

    private fun generateAccessTokenRequest(verifier: String): AccessTokenRequest =
        try {
            AccessTokenRequest(
                baseUrl = apiBaseUrl,
                oAuthStep = OAuthFlow.ACCESS_TOKEN.step,
                consumerKey = apiKey,
                secret = apiToken,
                token = requestToken!!.token,
                verifier = verifier,
                secretToken = requestToken!!.secret
            )
        } catch (cause: Throwable) {
            throw RequestTokenInitializeException(cause)
        }


    private suspend fun getAccessTokenResponse(request: AccessTokenRequest): String =
        try {
            api.getAccessToken(
                nonce = request.nonce,
                timestamp = request.timestamp,
                consumerKey = request.consumerKey,
                signature = request.signature,
                method = request.method,
                oauthVersion = request.oAuthVersion,
                token = request.token,
                verifier = request.verifier
            )
        } catch (cause: Throwable) {
            if (cause is CancellationException) throw cause
            throw AccessTokenFetchException(cause)
        }

    private fun parseAccessTokenResponse(response: String): User {

        val params = parseResponse(response)

        return try {
            User(
                id = params.getValue(FLICKR_PARAMS_ID),
                username = params.getValue(FLICKR_PARAMS_USERNAME),
                fullname = params.getValue(FLICKR_PARAMS_FULLNAME),
                token = AccessToken(
                    accessToken = params.getValue(FLICKR_PARAMS_TOKEN),
                    accessTokenSecret = params.getValue(FLICKR_PARAMS_SECRET)
                )
            )
        } catch (cause: Throwable) {
            throw AccessTokenInvalidResponseException(cause)
        }
    }


    private fun parseRequestTokenResponse(response: String): RequestToken {

        val params = parseResponse(response)

        return try {
            RequestToken(
                token = params.getValue(FLICKR_PARAMS_TOKEN),
                secret = params.getValue(FLICKR_PARAMS_SECRET)
            )
        } catch (cause: Throwable) {
            throw RequestTokenInvalidResponseException(cause)
        }
    }

    private fun parseResponse(response: String): Map<String, String> {
        return response.split("&").associate {
            val parts = it.split("=")
            if (parts.size == 2) parts[0] to parts[1] else parts[0] to ""
        }
    }
}