package com.scrcasebook.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.scrcasebook.app.data.db.FollowUpEntity
import com.scrcasebook.app.data.db.ScrDatabase
import com.scrcasebook.app.data.db.SectionPageEntity
import com.scrcasebook.app.data.model.PalmRejectionMode
import com.scrcasebook.app.data.model.ScrDefinitions
import com.scrcasebook.app.data.model.ScrSection
import com.scrcasebook.app.data.model.Stroke
import com.scrcasebook.app.ui.components.ChiefComplaintHeader
import com.scrcasebook.app.ui.components.StylusDrawingCanvas
import com.scrcasebook.app.ui.theme.ScrPrimary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun StylusSheetScreen(
    database: ScrDatabase,
    patientId: String,
    patientName: String,
    sectionKey: String,
    sectionTitle: String,
    sectionHint: String,
    sectionIndex: Int?, // null if follow-up
    followUp: FollowUpEntity?,
    onNavigateBack: () -> Unit,
    onNavigateSection: ((Int) -> Unit)? = null
) {
    val coroutineScope = rememberCoroutineScope()
    val gson = remember { Gson() }

    // Tool state
    var isEraser by remember { mutableStateOf(false) }
    var activeColor by remember { mutableStateOf("#111111") }
    var activeWidth by remember { mutableStateOf(2.5f) }
    var palmMode by remember { mutableStateOf(PalmRejectionMode.PEN_ONLY) }

    // Follow-up editing state
    var fuDate by remember { mutableStateOf(followUp?.date ?: "") }
    var fuRx by remember { mutableStateOf(followUp?.rx ?: "") }

    // Pages state: list of stroke lists
    val pages = remember { mutableStateListOf<List<Stroke>>() }
    var activePageForActions by remember { mutableIntStateOf(0) }
    var isLoaded by remember { mutableStateOf(false) }

    val secDef = remember(sectionKey) { ScrDefinitions.SECTIONS.find { it.key == sectionKey } }
    val defaultPageCount = remember(sectionKey, followUp) {
        if (followUp != null) 3 else (secDef?.pages?.size ?: 1)
    }
    val isParagraphSection = sectionKey in listOf("s1", "s11", "s24", "s25")

    // Load initial pages from Room DB
    LaunchedEffect(patientId, sectionKey) {
        val existingPages = database.sectionPageDao().getPagesList(patientId, sectionKey)
        pages.clear()
        if (existingPages.isEmpty()) {
            for (p in 0 until defaultPageCount) {
                pages.add(emptyList())
            }
        } else {
            val maxIndex = maxOf(existingPages.maxOf { it.pageIndex }, defaultPageCount - 1)
            for (i in 0..maxIndex) {
                val pageEntity = existingPages.find { it.pageIndex == i }
                if (pageEntity != null && pageEntity.strokesJson.isNotEmpty()) {
                    val strokeListType = object : TypeToken<List<Stroke>>() {}.type
                    val list: List<Stroke> = try {
                        gson.fromJson(pageEntity.strokesJson, strokeListType) ?: emptyList()
                    } catch (e: Exception) {
                        emptyList()
                    }
                    pages.add(list)
                } else {
                    pages.add(emptyList())
                }
            }
        }
        isLoaded = true
    }

    // Save page helper
    fun savePage(pageIndex: Int, strokes: List<Stroke>) {
        coroutineScope.launch {
            val json = gson.toJson(strokes)
            database.sectionPageDao().insertPage(
                SectionPageEntity(
                    patientId = patientId,
                    sectionKey = sectionKey,
                    pageIndex = pageIndex,
                    strokesJson = json,
                    updated = System.currentTimeMillis()
                )
            )
            // Update follow up if applicable
            if (followUp != null) {
                database.patientDao().updateFollowUp(
                    followUp.copy(
                        date = fuDate,
                        rx = fuRx,
                        pageCount = pages.size,
                        hasInk = pages.any { it.isNotEmpty() },
                        updated = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = sectionTitle,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = patientName,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (sectionIndex != null && onNavigateSection != null) {
                        IconButton(onClick = {
                            val prev = (sectionIndex - 1 + ScrDefinitions.SECTIONS.size) % ScrDefinitions.SECTIONS.size
                            onNavigateSection(prev)
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Section")
                        }
                        IconButton(onClick = {
                            val next = (sectionIndex + 1) % ScrDefinitions.SECTIONS.size
                            onNavigateSection(next)
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Section")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // TOOLBAR
            Surface(
                tonalElevation = 2.dp,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Pen Tool
                        Button(
                            onClick = { isEraser = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!isEraser) ScrPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (!isEraser) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Pen", fontSize = 13.sp)
                        }

                        // Eraser Tool
                        Button(
                            onClick = { isEraser = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isEraser) ScrPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (isEraser) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.AutoFixHigh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Eraser", fontSize = 13.sp)
                        }

                        // Colors Palette
                        ScrDefinitions.PALETTE.forEach { colorHex ->
                            val parsed = parseHex(colorHex)
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(parsed)
                                    .border(
                                        width = if (activeColor == colorHex && !isEraser) 3.dp else 1.dp,
                                        color = if (activeColor == colorHex && !isEraser) ScrPrimary else Color.Gray,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        activeColor = colorHex
                                        isEraser = false
                                    }
                            )
                        }

                        // Thickness
                        listOf(
                            1.5f to "Thin",
                            2.5f to "Med",
                            5.0f to "Thick"
                        ).forEach { (w, label) ->
                            FilterChip(
                                selected = activeWidth == w,
                                onClick = { activeWidth = w },
                                label = { Text(label, fontSize = 12.sp) }
                            )
                        }

                        // Palm Rejection Mode
                        OutlinedButton(
                            onClick = {
                                palmMode = when (palmMode) {
                                    PalmRejectionMode.PEN_ONLY -> PalmRejectionMode.PEN_AND_FINGER
                                    PalmRejectionMode.PEN_AND_FINGER -> PalmRejectionMode.SCROLL
                                    PalmRejectionMode.SCROLL -> PalmRejectionMode.PEN_ONLY
                                }
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(palmMode.label, fontSize = 12.sp)
                        }

                        // Undo
                        OutlinedButton(
                            onClick = {
                                if (pages.isNotEmpty() && activePageForActions in pages.indices) {
                                    val currentStrokes = pages[activePageForActions]
                                    if (currentStrokes.isNotEmpty()) {
                                        val updated = currentStrokes.dropLast(1)
                                        pages[activePageForActions] = updated
                                        savePage(activePageForActions, updated)
                                    }
                                }
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Undo, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Undo", fontSize = 12.sp)
                        }

                        // Clear Page
                        OutlinedButton(
                            onClick = {
                                if (pages.isNotEmpty() && activePageForActions in pages.indices) {
                                    pages[activePageForActions] = emptyList()
                                    savePage(activePageForActions, emptyList())
                                }
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.DeleteSweep, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Clear", fontSize = 12.sp)
                        }

                        // Add Page
                        Button(
                            onClick = {
                                val newIndex = pages.size
                                pages.add(emptyList())
                                activePageForActions = newIndex
                                savePage(newIndex, emptyList())
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("+ Page", fontSize = 12.sp)
                        }

                        // Delete Added Page (only enabled when extra pages exist)
                        val canDeletePage = pages.size > defaultPageCount
                        OutlinedButton(
                            onClick = {
                                if (canDeletePage) {
                                    val lastIndex = pages.size - 1
                                    coroutineScope.launch {
                                        database.sectionPageDao().deletePage(patientId, sectionKey, lastIndex)
                                    }
                                    pages.removeAt(lastIndex)
                                    if (activePageForActions >= pages.size) {
                                        activePageForActions = maxOf(0, pages.size - 1)
                                    }
                                }
                            },
                            enabled = canDeletePage,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("- Page", fontSize = 12.sp)
                        }
                    }

                    // Follow up inputs if applicable
                    if (followUp != null) {
                        Spacer(Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = fuDate,
                                onValueChange = {
                                    fuDate = it
                                    coroutineScope.launch {
                                        database.patientDao().updateFollowUp(followUp.copy(date = it))
                                    }
                                },
                                label = { Text("Date (YYYY-MM-DD)", fontSize = 11.sp) },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = fuRx,
                                onValueChange = {
                                    fuRx = it
                                    coroutineScope.launch {
                                        database.patientDao().updateFollowUp(followUp.copy(rx = it))
                                    }
                                },
                                label = { Text("Remedy / Potency (Rx)", fontSize = 11.sp) },
                                modifier = Modifier.weight(2f),
                                singleLine = true
                            )
                        }
                    }
                }
            }

            // Clinical Section Hint
            if (sectionHint.isNotEmpty()) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = sectionHint,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            // Scrollable Sheets (Pages)
            if (isLoaded) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(pages) { index, strokes ->
                        Column(modifier = Modifier.fillMaxWidth()) {
                            val isContinuation = index >= defaultPageCount
                            val pageNumber = if (isContinuation) null else if (followUp != null) (104 + index) else secDef?.pages?.getOrNull(index)
                            val isPara = isParagraphSection || isContinuation

                            // If Chief complaints and page 0, show column header
                            if (sectionKey == "s2" && index == 0 && !isContinuation) {
                                ChiefComplaintHeader()
                            }

                            StylusDrawingCanvas(
                                pageIndex = index,
                                pageNumber = pageNumber,
                                isContinuationPage = isContinuation,
                                isParagraphPage = isPara,
                                strokes = strokes,
                                onStrokesChanged = { updatedStrokes ->
                                    activePageForActions = index
                                    pages[index] = updatedStrokes
                                    savePage(index, updatedStrokes)
                                },
                                activeColor = activeColor,
                                activeWidth = activeWidth,
                                isEraser = isEraser,
                                palmMode = palmMode,
                                hasChiefComplaintColumns = sectionKey == "s2" && index == 0 && !isContinuation
                            )
                        }
                    }
                    item {
                        Spacer(Modifier.height(48.dp))
                    }
                }
            }
        }
    }
}

private fun parseHex(hex: String): Color {
    val clean = hex.removePrefix("#")
    val colorInt = clean.toLong(16)
    return Color(colorInt or 0x00000000FF000000)
}
