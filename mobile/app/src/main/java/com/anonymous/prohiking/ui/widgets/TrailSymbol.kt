package com.anonymous.prohiking.ui.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill

@Composable
fun TrailSymbol(text: String, modifier: Modifier = Modifier) {
    val parts = text.split(':')
    if (parts.size != 3) {
        println("3 parts required, got ${parts.size}")
        return
    }

    val symbolParts = parts[2].split('_')
    if (symbolParts.size != 2) {
        println("2 symbol parts required, got ${parts.size}")
        return
    }

    val backgroundColor = nameToColor(parts[1])
    val symbolColor = nameToColor(symbolParts[0])
    val symbolName = symbolParts[1]
    
    Canvas(modifier = modifier) {
        when (symbolName) {
            "dot" -> {
                drawCircle(Color.Black, radius = size.width / 2.0f)
                drawCircle(backgroundColor, radius = size.width / 2.0f - 15f)
                drawCircle(symbolColor, radius = size.width / 5.0f)
            }
            "stripe" -> {
                drawRect(Color.Black, size = Size(size.width, size.height))
                drawRect(
                    backgroundColor,
                    topLeft = Offset(15f, 15f),
                    size = Size(size.width - 30f, size.height - 30f)
                )
                drawRect(
                    color = symbolColor,
                    topLeft = Offset(2 * size.width / 5f, 15f),
                    size = Size(size.width / 5f, size.height - 30f)
                )
            }
            "cross" -> {
                drawRect(Color.Black, size = Size(size.width, size.height))
                drawRect(
                    backgroundColor,
                    topLeft = Offset(15f, 15f),
                    size = Size(size.width - 30f, size.height - 30f)
                )
                drawRect(
                    color = symbolColor,
                    topLeft = Offset(2 * size.width / 5f, 15f),
                    size = Size(size.width / 5f, size.height - 30f)
                )
                drawRect(
                    color = symbolColor,
                    topLeft = Offset(15f, 2 * size.width / 5f),
                    size = Size(size.height - 30f, size.width / 5f)
                )
            }
            "triangle" -> {
                drawRect(Color.Black, size = Size(size.width, size.height))
                drawRect(
                    color = backgroundColor,
                    topLeft = Offset(15f, 15f),
                    size = Size(size.width - 30f, size.height - 30f)
                )
                drawPath(
                    path = Path().apply {
                        lineTo(size.width / 2f, 15f)
                        lineTo(size.width - 15f, size.height - 15f)
                        lineTo(15f, size.height - 15f)
                        lineTo(size.width / 2f, 15f)
                    },
                    color = symbolColor,
                    style = Fill
                )
            }
        }
    }
}

private fun nameToColor(name: String): Color {
    return when (name) {
        "black" -> Color.Black
        "white" -> Color(0xFFE0E0E0)
        "yellow" -> Color.Yellow
        "blue" -> Color.Blue
        "red" -> Color.Red
        else -> Color.Magenta
    }
}
