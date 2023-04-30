package dev.medetzhakupov.githubsearch.data.remote

import dev.medetzhakupov.githubsearch.data.remote.model.GithubUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GithubSearchRepository(private val remoteDataSource: GithubRemoteDataSource) {

    fun search(username: String): Flow<List<GithubUser>> = flow {
        val result = remoteDataSource.search(username)
        emit(result.getOrThrow())
    }
}