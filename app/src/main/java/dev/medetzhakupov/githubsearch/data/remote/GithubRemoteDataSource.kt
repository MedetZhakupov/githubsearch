package dev.medetzhakupov.githubsearch.data.remote

import dev.medetzhakupov.githubsearch.data.remote.model.GithubUser

class GithubRemoteDataSource(private val apiService: GithubApiService) {

    suspend fun search(username: String): Result<List<GithubUser>> {
        return try {
            val response = apiService.searchUsers(username)
            if (response.isSuccessful) {
                Result.success(response.body()!!.items)
            } else {
                Result.failure(Exception("Server error"))
            }
        } catch (error: Exception) {
            Result.failure(Exception("Network error"))
        }
    }
}