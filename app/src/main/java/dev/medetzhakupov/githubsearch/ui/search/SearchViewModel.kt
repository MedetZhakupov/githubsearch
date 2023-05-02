package dev.medetzhakupov.githubsearch.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.medetzhakupov.githubsearch.data.remote.GithubSearchRepository
import dev.medetzhakupov.githubsearch.data.remote.model.GithubUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val githubSearchRepository: GithubSearchRepository) : ViewModel() {

    private val _viewState = MutableStateFlow<SearchViewState>(SearchViewState.Empty)
    val viewState = _viewState.asStateFlow()

    fun searchUser(query: String) {
        viewModelScope.launch {
            _viewState.value = SearchViewState.Loading
            githubSearchRepository.search(query).collect { result ->
                if (result.isSuccess) {
                    _viewState.value = SearchViewState.Loaded(result.getOrThrow())
                } else {
                    _viewState.value = SearchViewState.Error
                }
            }
        }
    }
}

sealed class SearchViewState {
    object Empty : SearchViewState()
    object Loading : SearchViewState()
    data class Loaded(val users: List<GithubUser>) : SearchViewState()
    object Error : SearchViewState()
}