package dev.medetzhakupov.githubsearch.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.medetzhakupov.githubsearch.data.GithubSearchRepository
import dev.medetzhakupov.githubsearch.data.remote.model.GithubUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val githubSearchRepository: GithubSearchRepository
) : ViewModel() {

    private val followersUrl: String = checkNotNull(savedStateHandle["followersUrl"])
    private val reposUrl: String = checkNotNull(savedStateHandle["reposUrl"])

    private val _viewState = MutableStateFlow<DetailsViewState>(DetailsViewState.Empty)
    val viewState = _viewState.asStateFlow()

    init {

    }
}

sealed class DetailsViewState {
    object Empty : DetailsViewState()

    object Loading : DetailsViewState()

    data class Loaded(val users: List<GithubUser>) : DetailsViewState()

    object Error : DetailsViewState()
}