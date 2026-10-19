package com.scrcasebook.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke as CanvasStroke
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.scrcasebook.app.data.model.PalmRejectionMode
import com.scrcasebook.app.data.model.Stroke
import com.scrcasebook.app.ui.theme.PaperLineColor

@Composable
fun StylusDrawingCanvas(
    pageIndex: Int,
    strokes: List<Stroke>,
    onStrokesChanged: (List<Stroke>) -> Unit,
    activeColor: String,
    activeWidth: Float,
    isEraser: Boolean,
    palmMode: PalmRejectionMode,
    hasChiefComplaintColumns: Boolean = false,
    modifier: Modifier = Modifier
) {
    val currentPoints = remember { mutableStateListOf<Float>() }
    var isDrawing by remember { mutableStateOf(false) }
    val latestStrokes by rememberUpdatedState(strokes)
    val latestOnStrokesChanged by rememberUpdatedState(onStrokesChanged)

    val paperBackground = Color(0xFFFFFFFF)
    val ruledLineColor = PaperLineColor

    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(5f / 7f)
            .shadow(6.dp, RoundedCornerShape(4.dp))
            .clip(RoundedCornerShape(4.dp)),
        colors = CardDefaults.cardColors(containerColor = paperBackground)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(palmMode, isEraser, activeColor, activeWidth) {
                        if (palmMode == PalmRejectionMode.SCROLL) return@pointerInput

                        awaitEachGesture {
                            val down = awaitFirstDown(requireUnconsumed = false)
                            val isStylus = down.type == PointerType.Stylus || down.type == PointerType.Eraser

                            // Palm Rejection: If PEN_ONLY mode, ignore touch events
                            if (palmMode == PalmRejectionMode.PEN_ONLY && !isStylus) {
                                return@awaitEachGesture
                            }

                            down.consume()
                            currentPoints.clear()
                            currentPoints.add(down.position.x)
                            currentPoints.add(down.position.y)
                            isDrawing = true

                            while (true) {
                                val event = awaitPointerEvent()
                                val change = event.changes.firstOrNull { it.id == down.id } ?: break

                                if (change.pressed) {
                                    change.consume()
                                    currentPoints.add(change.position.x)
                                    currentPoints.add(change.position.y)
                                } else {
                                    change.consume()
                                    // Gesture finished
                                    if (currentPoints.size >= 2) {
                                        val newStroke = Stroke(
                                            c = activeColor,
                                            w = activeWidth,
                                            e = if (isEraser || down.type == PointerType.Eraser) 1 else 0,
                                            p = currentPoints.toList()
                                        )
                                        val updated = latestStrokes.toMutableList().apply { add(newStroke) }
                                        latestOnStrokesChanged(updated)
                                    }
                                    currentPoints.clear()
                                    isDrawing = false
                                    break
                                }
                            }
                        }
                    }
            ) {
                // 1. Draw Ruled Lines
                val lineSpacing = 48.dp.toPx()
                var y = lineSpacing
                while (y < size.height) {
                    drawLine(
                        color = ruledLineColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1.dp.toPx()
                    )
                    y += lineSpacing
                }

                // 2. Draw Chief Complaint Columns if applicable
                if (hasChiefComplaintColumns && pageIndex == 0) {
                    drawChiefComplaintGrid(size.width, size.height)
                }

                // 3. Draw Persisted Strokes
                strokes.forEach { stroke ->
                    drawStroke(stroke, paperBackground)
                }

                // 4. Draw Active In-Progress Stroke
                if (isDrawing && currentPoints.size >= 2) {
                    val activeStroke = Stroke(
                        c = activeColor,
                        w = activeWidth,
                        e = if (isEraser) 1 else 0,
                        p = currentPoints.toList()
                    )
                    drawStroke(activeStroke, paperBackground)
                }
            }

            // Page Number Indicator
            Text(
                text = "${pageIndex + 1}",
                color = Color(0xFF888899),
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 12.dp, bottom = 8.dp)
            )
        }
    }
}

private fun DrawScope.drawStroke(stroke: Stroke, backgroundPaper: Color) {
    val points = stroke.p
    if (points.size < 2) return

    val strokeColor = if (stroke.e == 1) backgroundPaper else parseHexColor(stroke.c)
    val strokeWidth = if (stroke.e == 1) stroke.w * 8f else stroke.w * 2.5f

    val path = Path().apply {
        moveTo(points[0], points[1])
        if (points.size == 2) {
            lineTo(points[0] + 0.5f, points[1] + 0.5f)
        } else {
            var i = 2
            while (i < points.size) {
                lineTo(points[i], points[i + 1])
                i += 2
            }
        }
    }

    drawPath(
        path = path,
        color = strokeColor,
        style = CanvasStroke(
            width = strokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}

private fun DrawScope.drawChiefComplaintGrid(width: Float, height: Float) {
    val colHeaderHeight = 48.dp.toPx()
    val totalRatio = 0.5f + 2.2f + 1.6f + 1.6f + 1.6f
    val wNo = (0.5f / totalRatio) * width
    val wLoc = (2.2f / totalRatio) * width
    val wSens = (1.6f / totalRatio) * width
    val wMod = (1.6f / totalRatio) * width
    val wAcc = (1.6f / totalRatio) * width

    val dividerColor = Color(0xFFB9C6C1)
    val dividers = listOf(wNo, wNo + wLoc, wNo + wLoc + wSens, wNo + wLoc + wSens + wMod)

    dividers.forEach { xPos ->
        drawLine(
            color = dividerColor,
            start = Offset(xPos, 0f),
            end = Offset(xPos, height),
            strokeWidth = 1.dp.toPx()
        )
    }
}

private fun parseHexColor(hex: String): Color {
    return try {
        val clean = hex.removePrefix("#")
        val colorInt = clean.toLong(16)
        if (clean.length == 6) {
            Color(colorInt or 0x00000000FF000000)
        } else {
            Color(colorInt)
        }
    } catch (e: Exception) {
        Color(0xFF111111)
    }
}
