package dev.medetzhakupov.githubsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.medetzhakupov.githubsearch.data.remote.GithubSearchRepository
import dev.medetzhakupov.githubsearch.data.remote.apiService
import dev.medetzhakupov.githubsearch.ui.search.SearchScreen
import dev.medetzhakupov.githubsearch.ui.search.SearchViewModel
import dev.medetzhakupov.githubsearch.ui.theme.GithubSearchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel by viewModels<SearchViewModel> {
                viewModelFactory {
                    initializer {
                        SearchViewModel(
                            GithubSearchRepository(apiService)
                        )
                    }
                }
            }

            GithubSearchTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SearchScreen(
                        viewState = viewModel.viewState.collectAsState().value,
                        onSearch = { viewModel.searchUser(it) })
                }
            }
        }
    }
}
