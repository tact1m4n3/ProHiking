package com.anonymous.prohiking.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anonymous.prohiking.ui.Screen
import com.anonymous.prohiking.ui.widgets.TrailSymbol

@Composable
fun ExploreScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    exploreViewModel: ExploreViewModel = viewModel(factory = ExploreViewModel.Factory),
) {
    val isLoading by exploreViewModel.isLoading.collectAsState()
    val recommendedTrails by exploreViewModel.recommendedTrails.collectAsState()
    val searchText by exploreViewModel.searchText.collectAsState()
    val isSearching by exploreViewModel.isSearching.collectAsState()
    val searchedTrails by exploreViewModel.searchedTrails.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TextField(
                shape = RoundedCornerShape(10.dp),
                value = searchText,
                onValueChange = {
                    exploreViewModel.updateSearchText(it)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Search trails") },
                maxLines = 1,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (searchText == "") {
                Text(text = "Top Trails Nearby", fontSize = 28.sp, modifier = Modifier.padding(start = 12.dp), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary,textDecoration = TextDecoration.Underline)

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 40.dp)
                        )
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.onPrimaryContainer)
                            .fillMaxWidth(),
                        // border = BorderStroke(1.dp,MaterialTheme.colorScheme.primary),
                        // elevation = CardDefaults.elevatedCardElevation()
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .background(MaterialTheme.colorScheme.onPrimaryContainer)
                        ) {
                            items(recommendedTrails) { trail ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(enabled = true) {
                                            exploreViewModel.onTrailSelect(trail)
                                            navController.navigate(Screen.Main.TrailDetails.route)
                                        }
                                        .padding(all = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TrailSymbol(
                                        trail.symbol,
                                        modifier = Modifier.size(32.dp, 32.dp)
                                    )

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth(0.9f)
                                            .padding(start = 8.dp)
                                    ) {
                                        Text(
                                            text = trail.name,
                                            color = MaterialTheme.colorScheme.tertiary,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold ,
                                            overflow = TextOverflow.Ellipsis,
                                            softWrap = true,
                                            maxLines = 1
                                        )

                                        Text(
                                            text = "${trail.length} km",
                                            fontSize = 16.sp,
                                            overflow = TextOverflow.Ellipsis,
                                            fontWeight = FontWeight.SemiBold,
                                            softWrap = true,
                                            maxLines = 1,
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            fontStyle = FontStyle.Italic
                                        )
                                    }

                                    Icon(
                                        imageVector = Icons.Outlined.ChevronRight,
                                        contentDescription = null,
                                        tint = Color.Black.copy(alpha = 0.70f),
                                        modifier = Modifier
                                            .weight(weight = 1f, fill = false)
                                    )
                                }
                            }
                        }
                    }
                }


            } else {
                if (isSearching) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 40.dp)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(searchedTrails) { trail ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(enabled = true) {
                                        exploreViewModel.onTrailSelect(trail)
                                        navController.navigate(Screen.Main.TrailDetails.route)
                                    }
                                    .padding(all = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TrailSymbol(trail.symbol, modifier = Modifier.size(32.dp, 32.dp))

                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(0.9f)
                                        .padding(start = 8.dp)
                                ) {
                                    Text(
                                        text = trail.name,
                                        fontSize = 18.sp,
                                        overflow = TextOverflow.Ellipsis,
                                        softWrap = true,
                                        maxLines = 1
                                    )

                                    Text(
                                        text = "${trail.length} km",
                                        fontSize = 16.sp,
                                        overflow = TextOverflow.Ellipsis,
                                        fontWeight = FontWeight.SemiBold,
                                        softWrap = true,
                                        maxLines = 1,
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        fontStyle = FontStyle.Italic
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Outlined.ChevronRight,
                                    contentDescription = null,
                                    tint = Color.Black.copy(alpha = 0.70f),
                                    modifier = Modifier
                                        .weight(weight = 1f, fill = false)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
