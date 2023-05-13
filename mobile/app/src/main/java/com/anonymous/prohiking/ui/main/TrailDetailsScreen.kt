package com.anonymous.prohiking.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anonymous.prohiking.ui.Screen
import com.anonymous.prohiking.ui.widgets.TrailPreview

@Composable
fun TrailDetailsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    exploreViewModel: ExploreViewModel = viewModel(factory = ExploreViewModel.Factory)
) {
    val selectedTrail by exploreViewModel.selectedTrail.collectAsState()
    val selectedTrailPath by exploreViewModel.selectedTrailPath.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar {
                IconButton(onClick = {
                    navController.navigate(Screen.Main.Explore.route)
                }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                }

                Text(
                    text = selectedTrail?.name ?: "Trail",
                    fontSize = 24.sp,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold,
                    softWrap = true,
                    maxLines = 1,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                selectedTrail?.let { trail ->
                    selectedTrailPath?.let { trailPath ->
                        TrailPreview(
                            trail = trail,
                            trailPath = trailPath,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.5f),
                        )
                    }
                }
            }
        }
    }
}
