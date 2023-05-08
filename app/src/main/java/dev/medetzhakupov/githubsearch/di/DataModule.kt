package dev.medetzhakupov.githubsearch.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.medetzhakupov.githubsearch.data.GithubSearchRepository
import dev.medetzhakupov.githubsearch.data.GithubSearchRepositoryImpl
import dev.medetzhakupov.githubsearch.data.SearchHistoryRepository
import dev.medetzhakupov.githubsearch.data.SearchHistoryRepositoryImpl
import dev.medetzhakupov.githubsearch.data.cache.SearchHistoryRealm
import dev.medetzhakupov.githubsearch.data.remote.GithubApiService
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideGithubApiService(): GithubApiService =
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(OkHttpClient())
            .build()
            .create(GithubApiService::class.java)

    @Singleton
    @Provides
    fun provideGithubSearchRepository(apiService: GithubApiService): GithubSearchRepository =
        GithubSearchRepositoryImpl(apiService)

    @Singleton
    @Provides
    fun provideSearchHistoryRepository(realm: Realm): SearchHistoryRepository =
        SearchHistoryRepositoryImpl(realm)

    @Singleton
    @Provides
    fun provideRealm() = Realm.open(
        RealmConfiguration.create(
            schema = setOf(
                SearchHistoryRealm::class
            )
        )
    )
}