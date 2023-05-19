package com.anonymous.prohiking.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConfirmDialog(title: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = title, fontSize = 20.sp)
        },
        containerColor = MaterialTheme.colorScheme.onPrimary,
        dismissButton = {
            Button(
                onClick = { onDismiss() },
                shape = RoundedCornerShape(30.0f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                ),
                border = BorderStroke(3.dp, Color.Red)
            ) {
                Text("No", color = Color.Red)
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm() },
                shape = RoundedCornerShape(30.0f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                ),
                border = BorderStroke(3.dp, Color(0.0f, 0.7f, 0.0f, 1.0f))
            ) {
                Text("Yes", color = Color(0.0f, 0.7f, 0.0f, 1.0f))
            }
        }
    )
}