package com.scrcasebook.app.bridge

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.webkit.JavascriptInterface

class AndroidWebBridge(
    private val context: Context,
    private val onExportRequested: (String) -> Unit,
    private val onImportRequested: () -> Unit,
    private val onSwitchToNative: () -> Unit
) {
    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    @JavascriptInterface
    fun exportBackup(jsonContent: String) {
        onExportRequested(jsonContent)
    }

    @JavascriptInterface
    fun requestImportBackup() {
        onImportRequested()
    }

    @JavascriptInterface
    fun switchToNative() {
        onSwitchToNative()
    }

    @JavascriptInterface
    fun vibrate(milliseconds: Long) {
        try {
            val duration = milliseconds.coerceIn(10L, 500L)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(
                    VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(duration)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
