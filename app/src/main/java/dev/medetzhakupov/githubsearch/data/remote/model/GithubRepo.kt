package dev.medetzhakupov.githubsearch.data.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GithubRepo(val name: String)