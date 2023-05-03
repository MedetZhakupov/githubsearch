package dev.medetzhakupov.githubsearch.data

import dev.medetzhakupov.githubsearch.data.remote.GithubApiService
import dev.medetzhakupov.githubsearch.data.remote.model.GithubUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GithubSearchRepositoryImpl(private val apiService: GithubApiService) :
    GithubSearchRepository {

    override fun search(username: String): Flow<Result<List<GithubUser>>> = flow {
        val result = apiService.searchUsers(username)
        emit(Result.success(result.items))
    }.catch { error ->
        emit(Result.failure(error))
    }
}