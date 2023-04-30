package dev.medetzhakupov.githubsearch.data.remote

import dev.medetzhakupov.githubsearch.data.remote.model.GithubUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GithubSearchRepository(private val apiService: GithubApiService) {

    fun search(username: String): Flow<Result<List<GithubUser>>> = flow {
        val result = apiService.searchUsers(username)
        emit(Result.success(result.items))
    }.catch { error ->
        emit(Result.failure(error))
    }
}