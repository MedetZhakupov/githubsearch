package dev.medetzhakupov.githubsearch.ui.search

import dev.medetzhakupov.githubsearch.CoroutinesTestRule
import dev.medetzhakupov.githubsearch.data.remote.GithubSearchRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

internal class SearchViewModelTest {

    @JvmField
    @Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val repository: GithubSearchRepository = mockk()
    val viewModel = SearchViewModel(repository)

    @Test
    fun `check initial view state is empty`() {
        assertEquals(viewModel.viewState.value, SearchViewState.Empty)
    }

    @Test
    fun `should search github users`() {
        coEvery { repository.search(any()) } returns flowOf(Result.success(emptyList()))

        viewModel.searchUser("username")

        assertEquals(viewModel.viewState.value, SearchViewState.Loaded(emptyList()))
    }

    @Test
    fun `searching github users failure`() {
        coEvery { repository.search(any()) } returns flowOf(Result.failure(Exception()))

        viewModel.searchUser("username")

        assertEquals(viewModel.viewState.value, SearchViewState.Error)
    }
}