package dev.medetzhakupov.githubsearch.ui.search

import dev.medetzhakupov.githubsearch.CoroutinesTestRule
import dev.medetzhakupov.githubsearch.data.GithubSearchRepository
import dev.medetzhakupov.githubsearch.data.Result
import dev.medetzhakupov.githubsearch.data.SearchHistoryRepository
import dev.medetzhakupov.githubsearch.ui.search.SearchViewState.ScreenState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

internal class SearchViewModelTest {

    @JvmField
    @Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val githubSearchRepository: GithubSearchRepository = mockk()
    private val searchHistoryRepository: SearchHistoryRepository = mockk {
        coEvery { saveSearch(any()) } returns Unit
    }
    private val viewModel = SearchViewModel(githubSearchRepository, searchHistoryRepository)

    @Test
    fun `check initial view state is empty`() {
        assertEquals(viewModel.viewState.value, ScreenState.Empty)
    }

    @Test
    fun `should search github users`() {
        coEvery { githubSearchRepository.search(any()) } returns flowOf(Result.Success(emptyList()))

        viewModel.searchUser("username")

        coVerify { searchHistoryRepository.saveSearch("username") }
        assertEquals(viewModel.viewState.value, ScreenState.Loaded(emptyList()))
    }

    @Test
    fun `searching github users failure`() {
        coEvery { githubSearchRepository.search(any()) } returns flowOf(Result.Failure(Exception()))

        viewModel.searchUser("username")

        coVerify(exactly = 0) { searchHistoryRepository.saveSearch("username") }
        assertEquals(viewModel.viewState.value, ScreenState.Error)
    }
}