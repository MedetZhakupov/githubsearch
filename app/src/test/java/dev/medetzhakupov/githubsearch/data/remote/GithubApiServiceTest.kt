package dev.medetzhakupov.githubsearch.data.remote

import com.squareup.moshi.Moshi
import dev.medetzhakupov.githubsearch.CoroutinesTestRule
import dev.medetzhakupov.githubsearch.data.remote.model.GithubUser
import dev.medetzhakupov.githubsearch.data.remote.model.SearchResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

internal class GithubApiServiceTest {

    @JvmField
    @Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: GithubApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GithubApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test successful search`() = runTest {
        val searchResult = SearchResult(listOf(GithubUser("login", "avatarUrl")))
        val adapter = Moshi.Builder().build().adapter(SearchResult::class.java)
        mockWebServer.enqueue(MockResponse().setBody(adapter.toJson(searchResult)))

        val actualResult = apiService.searchUsers("testting")

        assertEquals(searchResult, actualResult)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test failed search`() = runTest {
        val errorCode = 404
        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        try {
            apiService.searchUsers("testting")
            fail("Expected an exception to be thrown")
        } catch (e: HttpException) {
            assertEquals(errorCode, e.code())
        }
    }
}