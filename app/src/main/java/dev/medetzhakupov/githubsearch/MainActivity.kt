package dev.medetzhakupov.githubsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.medetzhakupov.githubsearch.data.GithubSearchRepositoryImpl
import dev.medetzhakupov.githubsearch.data.SearchHistoryRepositoryImpl
import dev.medetzhakupov.githubsearch.data.cache.SearchHistoryRealm
import dev.medetzhakupov.githubsearch.data.remote.apiService
import dev.medetzhakupov.githubsearch.ui.search.SearchScreen
import dev.medetzhakupov.githubsearch.ui.search.SearchViewModel
import dev.medetzhakupov.githubsearch.ui.theme.GithubSearchTheme
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel by viewModels<SearchViewModel> {
                viewModelFactory {
                    initializer {
                        SearchViewModel(
                            GithubSearchRepositoryImpl(apiService),
                            SearchHistoryRepositoryImpl(
                                Realm.open(
                                    RealmConfiguration.create(
                                        schema = setOf(
                                            SearchHistoryRealm::class
                                        )
                                    )
                                )
                            )
                        )
                    }
                }
            }

            GithubSearchTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SearchScreen(
                        viewState = viewModel.viewState.collectAsStateWithLifecycle().value,
                        onQueryChange = { viewModel.onSearchTextChange(it) },
                        onClearRecentSearches = { viewModel.onClearRecentSearches() },
                        onSearch = { viewModel.searchUser(it) })
                }
            }
        }
    }
}
