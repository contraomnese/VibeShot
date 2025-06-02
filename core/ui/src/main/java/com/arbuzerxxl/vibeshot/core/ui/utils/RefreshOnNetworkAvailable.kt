package com.arbuzerxxl.vibeshot.core.ui.utils

//@Composable
//fun RefreshOnNetworkAvailable(
//    refreshAction: () -> Unit
//) {
//    val networkMonitor: NetworkMonitor = koinInject<NetworkMonitor>()
//    val isConnected by networkMonitor.isConnected.collectAsState()
//
//    LaunchedEffect(isConnected) {
//        if (isConnected) {
//            refreshAction()
//        }
//    }
//}