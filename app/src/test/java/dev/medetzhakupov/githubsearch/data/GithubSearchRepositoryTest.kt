package dev.medetzhakupov.githubsearch.data

import dev.medetzhakupov.githubsearch.CoroutinesTestRule
import dev.medetzhakupov.githubsearch.data.remote.GithubApiService
import dev.medetzhakupov.githubsearch.data.remote.model.GithubUser
import dev.medetzhakupov.githubsearch.data.remote.model.SearchResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
internal class GithubSearchRepositoryTest {

    @JvmField
    @Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val apiService: GithubApiService = mockk()
    private val repository: GithubSearchRepository = GithubSearchRepositoryImpl(apiService)

    @Test
    fun `search user`() = runTest {
        val githubUser = GithubUser("login", "avatarUrl", "htmlUrl")
        val searchResult = SearchResult(listOf(githubUser))
        coEvery { apiService.searchUsers(any()) } returns searchResult

        assertEquals(Result.success(searchResult.items), repository.search("user").first())
    }


    @Test
    fun `search user failure`() = runTest {
        val response: Response<SearchResult> =
            Response.error(404, "".toResponseBody())
        val error = HttpException(response)
        coEvery { apiService.searchUsers(any()) } throws error

        assertTrue(repository.search("user").first().isFailure)
    }
}