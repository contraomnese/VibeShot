package com.arbuzerxxl.vibeshot.core.ui.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

@SuppressLint("MissingPermission")
//class NetworkMonitor(context: Context) : DefaultLifecycleObserver {
//
//    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//    private val _isConnected = MutableStateFlow(checkInternet())
//    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()
//
//    private val callback = object : ConnectivityManager.NetworkCallback() {
//        override fun onAvailable(network: Network) {
//            _isConnected.value = true
//        }
//
//        override fun onLost(network: Network) {
//            _isConnected.value = checkInternet()
//        }
//    }
//
//    init {
//        val request = NetworkRequest.Builder()
//            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//            .build()
//
//        connectivityManager.registerNetworkCallback(request, callback)
//    }
//
//    private fun checkInternet(): Boolean {
//        val activeNetwork = connectivityManager.activeNetwork
//        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
//        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
//    }
//
//    fun stop() {
//        connectivityManager.unregisterNetworkCallback(callback)
//    }
//}

class NetworkMonitor(context: Context) {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val isConnected: Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)
            }
        }

        val request = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request, callback)

        trySend(checkInternet())

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()

    private fun checkInternet(): Boolean {
        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}