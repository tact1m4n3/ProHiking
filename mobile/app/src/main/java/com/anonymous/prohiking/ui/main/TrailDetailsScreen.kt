package com.anonymous.prohiking.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anonymous.prohiking.ui.Screen
import com.anonymous.prohiking.ui.widgets.TrailPreview
import com.anonymous.prohiking.ui.widgets.TrailSymbol

@Composable
fun TrailDetailsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    exploreViewModel: ExploreViewModel = viewModel(factory = ExploreViewModel.Factory)
) {
    val selectedTrail by exploreViewModel.selectedTrail.collectAsState()
    val selectedTrailPath by exploreViewModel.selectedTrailPath.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimaryContainer)
        ) {
            TopAppBar(backgroundColor = MaterialTheme.colorScheme.primary) {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Text(
                    text = selectedTrail?.name ?: "Trail",
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold,
                    softWrap = true,
                    maxLines = 1,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                selectedTrail?.let { trail ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        TrailSymbol(text = trail.symbol, modifier = Modifier.size(100.dp))

                        Card(
                            elevation = CardDefaults.elevatedCardElevation(),
                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
                            modifier = Modifier.size(100.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Length",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        letterSpacing = (0.8).sp,
                                        fontFamily = FontFamily.Default,
                                        color = Color.Gray
                                    )
                                )

                                Text(
                                    text = "${trail.length} km",
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    style = TextStyle(
                                        fontSize = 24.sp,
                                        fontFamily = FontFamily.Default
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    TextButton(onClick = {
                        exploreViewModel.onStartTrailButtonPressed(trail)
                        navController.navigate(Screen.Main.Navigate.route)
                    }) {
                        Text("Start Trail")
                    }

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
