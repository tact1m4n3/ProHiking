package com.anonymous.prohiking.ui.widgets

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Emergency
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.anonymous.prohiking.R

@Composable
fun EmergencyButton(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val emergencyNumber = stringResource(id = R.string.emergency_number)

    var showConfirmDialog by remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = {
                showConfirmDialog = true
            },
            containerColor = Color(0.9f, 0.0f, 0.0f, 1.0f),
            shape = RoundedCornerShape(16.dp),
            modifier = modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Rounded.Emergency,
                contentDescription = "SOS",
                tint = Color.White,
            )
        }
    }
    
    if (showConfirmDialog) {
        ConfirmDialog(
            title = "Do you want to call $emergencyNumber?",
            onDismiss = {
                showConfirmDialog = false
            },
            onConfirm = {
                showConfirmDialog = false

                val intent = Intent(
                    Intent.ACTION_CALL, Uri.parse(
                        "tel:$emergencyNumber"
                    ))
                context.startActivity(intent)
            }
        )
    }
}