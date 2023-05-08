package dev.medetzhakupov.githubsearch.data

import dev.medetzhakupov.githubsearch.data.remote.GithubApiService
import dev.medetzhakupov.githubsearch.data.remote.model.GithubRepo
import dev.medetzhakupov.githubsearch.data.remote.model.GithubUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GithubSearchRepositoryImpl(private val apiService: GithubApiService) :
    GithubSearchRepository {

    override fun search(username: String): Flow<Result<List<GithubUser>>> = flow {
        val result = apiService.searchUsers(username)
        emit(Result.Success(result.items) as Result<List<GithubUser>>)
    }.catch { error ->
        emit(Result.Failure(error))
    }

    override fun getFollowers(url: String): Flow<Result<List<GithubUser>>> = flow {
        val result = apiService.getFollowersFromUrl(url)
        emit(Result.Success(result) as Result<List<GithubUser>>)
    }.catch { error ->
        emit(Result.Failure(error))
    }

    override fun getRepos(url: String): Flow<Result<List<GithubRepo>>> = flow {
        val result = apiService.getReposFromUrl(url)
        emit(Result.Success(result) as Result<List<GithubRepo>>)
    }.catch { error ->
        emit(Result.Failure(error))
    }
}