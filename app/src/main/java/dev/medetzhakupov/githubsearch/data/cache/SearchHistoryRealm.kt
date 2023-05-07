package dev.medetzhakupov.githubsearch.data.cache

import io.realm.kotlin.types.RealmObject

class SearchHistoryRealm : RealmObject {
    var username: String = ""
}

data class SearchHistory(val username: String)