package dev.medetzhakupov.githubsearch.data

import dev.medetzhakupov.githubsearch.data.cache.SearchHistory
import dev.medetzhakupov.githubsearch.data.cache.SearchHistoryRealm
import io.realm.kotlin.Realm

class SearchHistoryRepositoryImpl(
    private val realm: Realm,
) : SearchHistoryRepository {

    override suspend fun saveSearch(query: String) {
        val searchHistoryRealm = SearchHistoryRealm().apply {
            username = query
        }
        val queryResult = realm.query(SearchHistoryRealm::class, "username = $0", query).find()
        if (queryResult.isEmpty()) {
            realm.write {
                copyToRealm(searchHistoryRealm)
            }
        }
    }

    override suspend fun deleteAllRecentSearches() {
        realm.write {
            val queryToDelete = this.query(SearchHistoryRealm::class)
            delete(queryToDelete)
        }
    }

    override suspend fun getRecentSearch(query: String): List<SearchHistory> {
        return realm.query(SearchHistoryRealm::class, "username CONTAINS $0", query)
            .find().map { item -> SearchHistory(item.username) }
    }
}