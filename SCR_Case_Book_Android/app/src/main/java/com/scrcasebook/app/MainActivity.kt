package com.scrcasebook.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.scrcasebook.app.data.db.FollowUpEntity
import com.scrcasebook.app.data.db.PatientEntity
import com.scrcasebook.app.data.model.ScrDefinitions
import com.scrcasebook.app.data.model.ScrSection
import com.scrcasebook.app.ui.screens.HomeScreen
import com.scrcasebook.app.ui.screens.PatientDetailScreen
import com.scrcasebook.app.ui.screens.StylusSheetScreen
import com.scrcasebook.app.ui.screens.WebViewScreen
import com.scrcasebook.app.ui.theme.SCRCaseBookTheme
import com.scrcasebook.app.util.BackupManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class AppMode {
    NATIVE_COMPOSE,
    WEBVIEW_CONTAINER
}

sealed class Screen {
    object Home : Screen()
    data class PatientDetail(val patient: PatientEntity) : Screen()
    data class StylusSheet(
        val patient: PatientEntity,
        val sectionKey: String,
        val sectionTitle: String,
        val sectionHint: String,
        val sectionIndex: Int?,
        val followUp: FollowUpEntity?
    ) : Screen()
}

class MainActivity : ComponentActivity() {
    private val database by lazy { (application as ScrApplication).database }
    private val backupManager by lazy { BackupManager(database) }

    private var pendingBackupJson: String? = null
    private var webViewInstance: WebView? = null

    // Android 16 Storage Access Framework (SAF) Launchers
    private val exportLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("application/json")
    ) { uri: Uri? ->
        uri?.let { destUri ->
            val json = pendingBackupJson ?: return@let
            val success = BackupManager.writeTextToUri(this, destUri, json)
            if (success) {
                Toast.makeText(this, "Backup exported successfully!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Failed to write backup file.", Toast.LENGTH_SHORT).show()
            }
            pendingBackupJson = null
        }
    }

    private val importLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { sourceUri ->
            val jsonString = BackupManager.readTextFromUri(this, sourceUri)
            if (jsonString.isNotBlank()) {
                CoroutineScope(Dispatchers.Main).launch {
                    val success = backupManager.importBackupFromJson(jsonString)
                    if (success) {
                        Toast.makeText(this@MainActivity, "Backup imported & merged!", Toast.LENGTH_LONG).show()
                        // If WebView is open, sync it as well
                        webViewInstance?.evaluateJavascript(
                            "window.onBackupImported && window.onBackupImported(${org.json.JSONObject.quote(jsonString)});",
                            null
                        )
                    } else {
                        Toast.makeText(this@MainActivity, "Could not parse backup file.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enforce Android 16 Edge-to-Edge display
        enableEdgeToEdge()

        setContent {
            SCRCaseBookTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation()
                }
            }
        }
    }

    @Composable
    private fun MainNavigation() {
        var appMode by remember { mutableStateOf(AppMode.NATIVE_COMPOSE) }
        var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
        val coroutineScope = rememberCoroutineScope()

        fun triggerExport(jsonContent: String) {
            pendingBackupJson = jsonContent
            exportLauncher.launch(BackupManager.getDefaultBackupFileName())
        }

        fun triggerImport() {
            importLauncher.launch(arrayOf("application/json", "text/plain"))
        }

        when (appMode) {
            AppMode.WEBVIEW_CONTAINER -> {
                WebViewScreen(
                    context = this,
                    onExportRequested = { json -> triggerExport(json) },
                    onImportRequested = { triggerImport() },
                    onSwitchToNative = { appMode = AppMode.NATIVE_COMPOSE },
                    onWebViewReady = { webViewInstance = it }
                )
            }
            AppMode.NATIVE_COMPOSE -> {
                when (val screen = currentScreen) {
                    is Screen.Home -> {
                        HomeScreen(
                            database = database,
                            onOpenPatient = { patient ->
                                currentScreen = Screen.PatientDetail(patient)
                            },
                            onSwitchToWebView = {
                                appMode = AppMode.WEBVIEW_CONTAINER
                            },
                            onExportBackup = {
                                coroutineScope.launch {
                                    val json = backupManager.generateBackupJson()
                                    triggerExport(json)
                                }
                            },
                            onImportBackup = {
                                triggerImport()
                            }
                        )
                    }
                    is Screen.PatientDetail -> {
                        PatientDetailScreen(
                            database = database,
                            patient = screen.patient,
                            onNavigateBack = {
                                currentScreen = Screen.Home
                            },
                            onOpenSection = { sec ->
                                currentScreen = Screen.StylusSheet(
                                    patient = screen.patient,
                                    sectionKey = sec.key,
                                    sectionTitle = "${sec.index + 1}. ${sec.title}",
                                    sectionHint = sec.hint,
                                    sectionIndex = sec.index,
                                    followUp = null
                                )
                            },
                            onOpenFollowUp = { fu ->
                                currentScreen = Screen.StylusSheet(
                                    patient = screen.patient,
                                    sectionKey = "f${fu.id}",
                                    sectionTitle = "Follow-up · ${fu.date}",
                                    sectionHint = "",
                                    sectionIndex = null,
                                    followUp = fu
                                )
                            }
                        )
                    }
                    is Screen.StylusSheet -> {
                        StylusSheetScreen(
                            database = database,
                            patientId = screen.patient.id,
                            patientName = screen.patient.name,
                            sectionKey = screen.sectionKey,
                            sectionTitle = screen.sectionTitle,
                            sectionHint = screen.sectionHint,
                            sectionIndex = screen.sectionIndex,
                            followUp = screen.followUp,
                            onNavigateBack = {
                                currentScreen = Screen.PatientDetail(screen.patient)
                            },
                            onNavigateSection = { newIdx ->
                                val sec = ScrDefinitions.SECTIONS[newIdx]
                                currentScreen = Screen.StylusSheet(
                                    patient = screen.patient,
                                    sectionKey = sec.key,
                                    sectionTitle = "${sec.index + 1}. ${sec.title}",
                                    sectionHint = sec.hint,
                                    sectionIndex = sec.index,
                                    followUp = null
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
