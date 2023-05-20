package com.anonymous.prohiking.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.style.TextDecoration
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
                    .verticalScroll(rememberScrollState())
            ) {
                selectedTrail?.let { trail ->
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceEvenly
//                    ) {
//                        Button(
//                            shape = CircleShape,
//                            onClick = {
//                                exploreViewModel.onStartTrailButtonPressed(trail)
//                                navController.navigate(Screen.Main.Navigate.route)
//                            }
//                        ) {
//                            Icon(
//                                modifier = Modifier
//                                    .weight(weight = 1f, fill = false),
//                                imageVector = Icons.Outlined.Navigation,
//                                contentDescription = "Navigate",
//                                tint = MaterialTheme.colorScheme.onPrimaryContainer
//                            )
//                        }
//                        Button(
//                            shape = CircleShape,
//                            onClick = {
//                            }
//                        ) {
//                            Icon(
//                                modifier = Modifier
//                                    .weight(weight = 1f, fill = false),
//                                imageVector = Icons.Outlined.FavoriteBorder,
//                                contentDescription = "Save",
//                                tint = MaterialTheme.colorScheme.onPrimaryContainer
//                            )
//                        }
//
//                        Button(
//                            shape = CircleShape,
//                            onClick = {
//
//                            }
//                        ) {
//                            Icon(
//                                modifier = Modifier
//                                    .weight(weight = 1f, fill = false),
//                                imageVector = Icons.Outlined.Download,
//                                contentDescription = "Download",
//                                tint = MaterialTheme.colorScheme.onPrimaryContainer
//                            )
//                        }
//                    }

//                    Spacer(modifier = Modifier.height(20.dp))

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Description",
                            style = TextStyle(
                                fontSize = 25.sp,
                                letterSpacing = (0.8).sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.tertiary,
                                textDecoration = TextDecoration.Underline
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Card(
                            elevation = CardDefaults.elevatedCardElevation(),
                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(15.dp),
                                ) {
                                    Text(
                                        text = "Trail",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            letterSpacing = (0.8).sp,
                                            fontFamily = FontFamily.Default,
                                            color = Color.LightGray
                                        )
                                    )
                                    Text(
                                        text = trail.name,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = TextStyle(
                                            fontSize = 20.sp,
                                            fontFamily = FontFamily.SansSerif
                                        )
                                    )

                                    Spacer(modifier = Modifier.size(15.dp))

                                    Text(
                                        text = "Length",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            letterSpacing = (0.8).sp,
                                            fontFamily = FontFamily.Default,
                                            color = Color.LightGray
                                        )
                                    )
                                    Text(
                                        text = "${trail.length} km",
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = TextStyle(
                                            fontSize = 20.sp,
                                            fontFamily = FontFamily.Default
                                        )
                                    )

                                    Spacer(modifier = Modifier.size(15.dp))

                                    Text(
                                        text = "Estimated Time",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            letterSpacing = (0.8).sp,
                                            fontFamily = FontFamily.Default,
                                            color = Color.LightGray
                                        )
                                    )
                                    Text(
                                        text = getFormattedTime(trail.length * 25),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = TextStyle(
                                            fontSize = 20.sp,
                                            fontFamily = FontFamily.Default
                                        )
                                    )
                                }

                                TrailSymbol(
                                    text = trail.symbol,
                                    modifier = Modifier
                                        .padding(20.dp)
                                        .size(80.dp)
                                        .align(Alignment.BottomEnd)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    Column {
                        Text(
                            text = "Preview",
                            style = TextStyle(
                                fontSize = 25.sp,
                                letterSpacing = (0.8).sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.tertiary,
                                textDecoration = TextDecoration.Underline
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        selectedTrailPath?.let { trailPath ->
                            TrailPreview(
                                trail = trail,
                                trailPath = trailPath,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            )
                            {
                                Button(
                                    shape = CircleShape,
                                    onClick = {
                                        exploreViewModel.onStartTrailButtonPressed()
                                        navController.navigate(Screen.Main.Navigate.route)
                                    }) {
                                    Text(
                                        "Start Trail",
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(15.dp))
                            Column( modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Weather Forecast",
                                    style = TextStyle(
                                        fontSize = 25.sp,
                                        letterSpacing = (0.8).sp,
                                        fontFamily = FontFamily.Default,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.tertiary,
                                        textDecoration = TextDecoration.Underline
                                    )
                                )
                                Row( modifier = Modifier
                                    .fillMaxWidth()
                                    //.horizontalScroll(rememberScrollState())
                                    .padding(10.dp)){


                                }



                            }

                        }
                    }
                }
            }
        }
    }
}

private fun getFormattedTime(time: Double): String {
    val hours = (time / 60.0).toInt()
    val minutes = (time % 60.0).toInt()
    return if (hours == 0)
        "${minutes}m"
    else
        "${hours}h ${minutes}m"
}
