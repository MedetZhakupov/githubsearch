package dev.medetzhakupov.githubsearch.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private val retrofit = Retrofit.Builder()
    .baseUrl("https://api.github.com/")
    .addConverterFactory(MoshiConverterFactory.create())
    .client(OkHttpClient())
    .build()

val apiService = retrofit.create(GithubApiService::class.java)