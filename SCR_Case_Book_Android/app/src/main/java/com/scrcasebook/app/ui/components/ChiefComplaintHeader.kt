package com.scrcasebook.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChiefComplaintHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFE6EEEB))
            .padding(vertical = 4.dp, horizontal = 2.dp)
    ) {
        Column(modifier = Modifier.weight(0.5f).padding(horizontal = 2.dp)) {
            Text(text = "No.", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF445566))
        }
        Column(modifier = Modifier.weight(2.2f).padding(horizontal = 2.dp)) {
            Text(text = "Location", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF445566))
            Text(text = "area · direction · spread · tissue · organ · system · duration", fontSize = 8.sp, color = Color(0xFF778899), lineHeight = 9.sp)
        }
        Column(modifier = Modifier.weight(1.6f).padding(horizontal = 2.dp)) {
            Text(text = "Sensation & Path.", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF445566))
        }
        Column(modifier = Modifier.weight(1.6f).padding(horizontal = 2.dp)) {
            Text(text = "Modalities", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF445566))
            Text(text = "A.F. · < · >", fontSize = 8.sp, color = Color(0xFF778899), lineHeight = 9.sp)
        }
        Column(modifier = Modifier.weight(1.6f).padding(horizontal = 2.dp)) {
            Text(text = "Accompaniments", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF445566))
            Text(text = "strict time relation · concomitants", fontSize = 8.sp, color = Color(0xFF778899), lineHeight = 9.sp)
        }
    }
}
