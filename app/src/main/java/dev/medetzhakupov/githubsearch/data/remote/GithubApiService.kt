package dev.medetzhakupov.githubsearch.data.remote

import dev.medetzhakupov.githubsearch.data.remote.model.GithubRepo
import dev.medetzhakupov.githubsearch.data.remote.model.GithubUser
import dev.medetzhakupov.githubsearch.data.remote.model.SearchResult
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface GithubApiService {
    @GET("search/users")
    suspend fun searchUsers(@Query("q") query: String): SearchResult

    @GET
    suspend fun getFollowersFromUrl(@Url url: String): List<GithubUser>

    @GET
    suspend fun getReposFromUrl(@Url url: String): List<GithubRepo>

}
