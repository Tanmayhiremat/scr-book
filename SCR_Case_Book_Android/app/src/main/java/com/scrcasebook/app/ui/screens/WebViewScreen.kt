package com.scrcasebook.app.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.scrcasebook.app.bridge.AndroidWebBridge

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    context: Context,
    onExportRequested: (String) -> Unit,
    onImportRequested: () -> Unit,
    onSwitchToNative: () -> Unit,
    onWebViewReady: (WebView) -> Unit
) {
    var webViewRef: WebView? = remember { null }

    BackHandler(enabled = webViewRef?.canGoBack() == true) {
        webViewRef?.goBack()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                WebView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setBackgroundColor(Color.parseColor("#F4F6F5"))

                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        databaseEnabled = true
                        allowFileAccess = true
                        allowContentAccess = true
                        cacheMode = WebSettings.LOAD_DEFAULT
                        useWideViewPort = true
                        loadWithOverviewMode = true
                        builtInZoomControls = false
                        displayZoomControls = false
                    }

                    val bridge = AndroidWebBridge(
                        context = ctx,
                        onExportRequested = onExportRequested,
                        onImportRequested = onImportRequested,
                        onSwitchToNative = onSwitchToNative
                    )
                    addJavascriptInterface(bridge, "AndroidBridge")

                    webViewClient = object : WebViewClient() {}
                    webChromeClient = object : WebChromeClient() {}

                    loadUrl("file:///android_asset/www/index.html")
                    webViewRef = this
                    onWebViewReady(this)
                }
            },
            update = {
                webViewRef = it
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            webViewRef?.destroy()
        }
    }
}
