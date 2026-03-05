package com.paraspatil.readstack.ui.library.components

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent

object BrowserUtils {
    fun launchBrowser(context: Context,url:String) {
    val customTabsIntent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .setInstantAppsEnabled(true)
        .build()
        customTabsIntent.launchUrl(context, android.net.Uri.parse(url))
    }
}