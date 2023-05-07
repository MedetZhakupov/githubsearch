package dev.medetzhakupov.githubsearch.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.medetzhakupov.githubsearch.data.GithubSearchRepository
import dev.medetzhakupov.githubsearch.data.Result
import dev.medetzhakupov.githubsearch.data.SearchHistoryRepository
import dev.medetzhakupov.githubsearch.data.remote.model.GithubUser
import dev.medetzhakupov.githubsearch.ui.search.SearchViewState.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val githubSearchRepository: GithubSearchRepository,
    private val searchHistoryRepository: SearchHistoryRepository,
) : ViewModel() {

    private val _viewState = MutableStateFlow<SearchViewState>(ScreenState.Empty)
    val viewState = _viewState.asStateFlow()


    fun searchUser(query: String) {
        viewModelScope.launch {
            _viewState.value = ScreenState.Loading
            githubSearchRepository.search(query).collect { result ->
                when (result) {
                    is Result.Success -> {
                        saveSearch(query)
                        _viewState.value = ScreenState.Loaded(result.value)
                    }

                    is Result.Failure -> _viewState.value = ScreenState.Error
                }
            }
        }
    }

    fun onSearchTextChange(query: String) {
        viewModelScope.launch {
            val result = searchHistoryRepository.getRecentSearch(query)
            _viewState.value = SearchViewState.RecentSearches(result.map { it.username })
        }
    }

    fun onClearRecentSearches() {
        viewModelScope.launch {
            searchHistoryRepository.deleteAllRecentSearches()
            _viewState.value = SearchViewState.RecentSearches(emptyList())
        }
    }

    private suspend fun saveSearch(query: String) {
        searchHistoryRepository.saveSearch(query)
    }
}

sealed class SearchViewState {

    sealed class ScreenState : SearchViewState() {
        object Empty : ScreenState()

        object Loading : ScreenState()

        data class Loaded(val users: List<GithubUser>) : ScreenState()

        object Error : ScreenState()
    }

    data class RecentSearches(val searches: List<String>) : SearchViewState()
}