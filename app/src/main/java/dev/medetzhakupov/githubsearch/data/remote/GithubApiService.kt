package dev.medetzhakupov.githubsearch.data.remote

import dev.medetzhakupov.githubsearch.data.remote.model.GithubUser
import dev.medetzhakupov.githubsearch.data.remote.model.SearchResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {
    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): GithubUser

    @GET("search/users")
    suspend fun searchUsers(@Query("q") query: String): SearchResult
}
