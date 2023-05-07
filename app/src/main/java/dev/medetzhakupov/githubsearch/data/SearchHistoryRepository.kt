package dev.medetzhakupov.githubsearch.data

import dev.medetzhakupov.githubsearch.data.cache.SearchHistory

interface SearchHistoryRepository {
    suspend fun saveSearch(query: String)

    suspend fun deleteAllRecentSearches()

    suspend fun getRecentSearch(query: String): List<SearchHistory>
}