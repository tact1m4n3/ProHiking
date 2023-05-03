package com.anonymous.prohiking.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anonymous.prohiking.ExploreViewModel

@Composable
fun ExploreScreen() {
    val viewModel = viewModel<ExploreViewModel>()
    val searchText by viewModel.searchText.collectAsState()
    val tracks by viewModel.tracks.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    Column(
        modifier= Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        TextField(
            value = searchText,
            onValueChange =  viewModel::onSearchTextChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Search") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if(isSearching) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        LazyColumn(
            modifier= Modifier
                .fillMaxWidth()
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
