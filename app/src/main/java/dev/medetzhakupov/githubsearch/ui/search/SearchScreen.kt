package dev.medetzhakupov.githubsearch.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isContainer
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun SearchScreen(viewState: SearchViewState, onSearch: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Search(onSearch)
        when (viewState) {
            SearchViewState.Empty -> Text(
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center,
                text = "Search github users"
            )

            SearchViewState.Error -> Text(
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center,
                text = "Something went wrong"
            )

            is SearchViewState.Loaded -> {
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        top = 72.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
                ) {
                    items(viewState.users.size) { idx ->
                        ListItem(
                            headlineContent = { Text(text = viewState.users[idx].login) }
                        )
                    }
                }
            }

            SearchViewState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(onSearch: (String) -> Unit) {
    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    Box(
        Modifier
            .semantics { isContainer = true }
            .zIndex(1.0f)
            .fillMaxWidth()) {
        SearchBar(
            modifier = Modifier.align(Alignment.TopCenter),
            query = text,
            onQueryChange = { text = it },
            onSearch = {
                active = false
                onSearch(it)
            },
            active = active,
            onActiveChange = { active = it },
            placeholder = { Text(text = "Search by username") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        ) {

        }
    }
}