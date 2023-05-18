package com.anonymous.prohiking.ui.widgets

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Emergency
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
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
    val sosNumber = stringResource(id = R.string.emergency_number)

    Box(modifier = modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = {
                val intent = Intent(
                    Intent.ACTION_CALL, Uri.parse(
                        "tel:$sosNumber"
                    ))
                context.startActivity(intent)
            },
            containerColor = Color.Red,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Emergency,
                contentDescription = "SOS",
                tint = Color.White,
            )
        }
    }
}