package dev.medetzhakupov.githubsearch.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isContainer
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import dev.medetzhakupov.githubsearch.R
import dev.medetzhakupov.githubsearch.ui.search.SearchViewState.ScreenState

@Composable
fun SearchScreen(
    viewState: SearchViewState,
    onQueryChange: (String) -> Unit,
    onClearRecentSearches: () -> Unit,
    onSearch: (String) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Search(
            recentSearches = (viewState as? SearchViewState.RecentSearches)?.searches
                ?: emptyList(),
            onQueryChange,
            onClearRecentSearches,
            onSearch
        )

        if (viewState !is ScreenState) return@Box

        when (viewState) {
            ScreenState.Empty -> Text(
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.search_github_users)
            )

            ScreenState.Error -> Text(
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.error)
            )

            is ScreenState.Loaded -> {
                if (viewState.users.isEmpty()) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center,
                        text = stringResource(id = R.string.not_found)
                    )
                    return
                }
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        top = 72.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
                ) {
                    items(viewState.users.size) { idx ->
                        viewState.users[idx].run {
                            ListItem(
                                modifier = Modifier.height(100.dp),
                                leadingContent = {
                                    AsyncImage(
                                        modifier = Modifier.clip(CircleShape),
                                        model = avatarUrl,
                                        contentDescription = null
                                    )
                                },
                                headlineContent = {
                                    Text(
                                        text = login,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            )
                        }
                    }
                }
            }

            ScreenState.Loading -> CircularProgressIndicator(
                modifier = Modifier.align(
                    Alignment.Center
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(
    recentSearches: List<String>,
    onQueryChange: (String) -> Unit,
    onClearRecentSearches: () -> Unit,
    onSearch: (String) -> Unit
) {
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
            onQueryChange = {
                text = it
                onQueryChange(text)
            },
            onSearch = {
                active = false
                onSearch(it)
            },
            active = active,
            onActiveChange = {
                active = it
                if (active) {
                    onQueryChange(text)
                }
            },
            placeholder = { Text(text = stringResource(R.string.searche_by_username)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        ) {
            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                items(recentSearches.size.plus(1)) { idx ->
                    if (idx == 0) {
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = stringResource(R.string.recent_searches),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            },
                            trailingContent = {
                                Text(
                                    text = stringResource(R.string.clear),
                                    modifier = Modifier.clickable { onClearRecentSearches() },
                                    color = Color(65, 127, 250),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                        )
                        return@items
                    }

                    val index = idx.minus(1)
                    ListItem(
                        modifier = Modifier.clickable {
                            active = false
                            text = recentSearches[index]
                            onSearch(recentSearches[index])
                        },
                        leadingContent = { Icon(Icons.Default.History, contentDescription = null) },
                        headlineContent = {
                            Text(
                                text = buildAnnotatedString {
                                    append(recentSearches[index])
                                    val startIdx = recentSearches[index].indexOf(text)
                                    val endIdx = startIdx.plus(text.length)
                                    addStyle(
                                        style = SpanStyle(fontWeight = FontWeight.Bold),
                                        start = startIdx,
                                        end = endIdx
                                    )
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}