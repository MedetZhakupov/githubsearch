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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.medetzhakupov.githubsearch.ui.details.DetailsScreen
import dev.medetzhakupov.githubsearch.ui.details.DetailsViewModel
import dev.medetzhakupov.githubsearch.ui.search.SearchScreen
import dev.medetzhakupov.githubsearch.ui.search.SearchViewModel
import dev.medetzhakupov.githubsearch.ui.theme.GithubSearchTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            GithubSearchTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Route.SEARCH,
                    ) {
                        composable(Route.SEARCH) {
                            val viewModel: SearchViewModel = hiltViewModel()

                            SearchScreen(
                                viewState = viewModel.viewState.collectAsStateWithLifecycle().value,
                                onQueryChange = { viewModel.onSearchTextChange(it) },
                                onClearRecentSearches = { viewModel.onClearRecentSearches() },
                                onSearch = { viewModel.searchUser(it) },
                                navigateToDetail = { followersUrl, reposUrl ->
                                    navController.navigate("${Route.DETAILS}/$followersUrl/$reposUrl")
                                },
                            )
                        }
                        composable("${Route.DETAILS}/{followersUrl}/{reposUrl}") {
                            val viewModel: DetailsViewModel = hiltViewModel()

                            DetailsScreen(
                                viewState = viewModel.viewState.collectAsStateWithLifecycle().value,
                                modifier = Modifier,
                                onBackClick = { navController.navigateUp() }
                            )
                        }
                    }
                }
            }
        }
    }
}

object Route {
    const val SEARCH = "Search"
    const val DETAILS = "Details"
}
