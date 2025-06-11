package com.arbuzerxxl.vibeshot.core.ui.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

private const val CHROME_PACKAGE = "com.android.chrome"

object TabHelper {

    fun openInTab(context: Context, url: String) {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setInstantAppsEnabled(true)
            .build()

        customTabsIntent.intent.setPackage(CHROME_PACKAGE)

        try {
            customTabsIntent.launchUrl(context, url.toUri())
        } catch (e: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}