package com.anonymous.prohiking.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GoogleMapsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable (modifier: Modifier, color: Color) -> Unit
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .background(Color(0xAAFFFFFF), RoundedCornerShape(4.dp))
    ) {
        IconButton(onClick = onClick, modifier = Modifier.matchParentSize()) {
            content(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.Center),
                color = Color(0xFF444444)
            )
        }
    }
}
