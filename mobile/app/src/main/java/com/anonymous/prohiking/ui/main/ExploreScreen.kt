package com.anonymous.prohiking.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anonymous.prohiking.ui.theme.best_color
import com.anonymous.prohiking.ui.theme.md_theme_light_primaryContainer

@Composable
fun ExploreScreen(navController: NavController) {
    val viewModel = viewModel<ExploreViewModel>(factory = ExploreViewModel.Factory)
    val searchText by viewModel.searchText.collectAsState()
    val tracks by viewModel.tracks.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(best_color)
            .padding(16.dp)
    ){
        TextField(
            value = searchText,
            onValueChange =  viewModel::onSearchTextChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Search") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = md_theme_light_primaryContainer,
                backgroundColor = best_color,
                cursorColor = md_theme_light_primaryContainer,
                unfocusedIndicatorColor = md_theme_light_primaryContainer
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (searchText == "") {
            Text("Click to search")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(best_color)
                    .weight(1f)
            ) {
                items(tracks) {track ->
                    Text(
                        text = track.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    )
                }
            }
        }
    }
}