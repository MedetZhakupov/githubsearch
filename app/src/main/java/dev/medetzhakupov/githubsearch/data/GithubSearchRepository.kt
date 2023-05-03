package dev.medetzhakupov.githubsearch.data

import dev.medetzhakupov.githubsearch.data.remote.model.GithubUser
import kotlinx.coroutines.flow.Flow

interface GithubSearchRepository {
    fun search(username: String): Flow<Result<List<GithubUser>>>
}