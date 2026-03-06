package com.paraspatil.readstack.ui.library.components

import android.content.Context
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent

object BrowserUtils {
    fun launchBrowser(context: Context,url:String,toolbarColor: Int) {
        try {
            val cleanUrl = if (url.startsWith("http://")) {
                url.replaceFirst("http://", "https://")
            } else {
                url
            }
            val customTabsIntent = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .setDefaultColorSchemeParams(
                  CustomTabColorSchemeParams.Builder()
                        .setToolbarColor(toolbarColor)
                        .build()
                    )
                    .build()
                customTabsIntent.launchUrl(context, android.net.Uri.parse(cleanUrl))

            }catch (e : Exception){
                //fallback is custom tabs fails
                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
                context.startActivity(intent)
            e.printStackTrace()
            }

    }
}