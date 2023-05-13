package com.anonymous.prohiking.data

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter

class TrailSymbolPainter(private val symbol: String): Painter() {
    override val intrinsicSize: Size
        get() = Size(600.0f, 600.0f)

    override fun DrawScope.onDraw() {
    }
}