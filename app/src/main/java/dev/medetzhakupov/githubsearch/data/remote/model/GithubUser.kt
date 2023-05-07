package dev.medetzhakupov.githubsearch.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GithubUser(
    val login: String,
    @Json(name = "avatar_url")
    val avatarUrl: String,
    @Json(name = "followers_url")
    val followersUrl: String,
    @Json(name = "repos_url")
    val reposUrl: String
)
