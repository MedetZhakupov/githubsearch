package dev.medetzhakupov.githubsearch.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.medetzhakupov.githubsearch.ui.search.SearchScreen
import dev.medetzhakupov.githubsearch.ui.search.SearchViewModel
import dev.medetzhakupov.githubsearch.ui.theme.GithubSearchTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: SearchViewModel = hiltViewModel()

            GithubSearchTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SearchScreen(
                        viewState = viewModel.viewState.collectAsStateWithLifecycle().value,
                        onQueryChange = { viewModel.onSearchTextChange(it) },
                        onClearRecentSearches = { viewModel.onClearRecentSearches() },
                        onSearch = { viewModel.searchUser(it) }
                    )
                }
            }
        }
    }
}
