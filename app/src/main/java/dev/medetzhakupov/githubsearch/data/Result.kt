package dev.medetzhakupov.githubsearch.data

sealed class Result<out T> {
    data class Success<out R>(val value: R) : Result<R>()
    data class Failure(val error: Throwable?) : Result<Nothing>()
}

fun Result<*>.isSuccess() = this is Result.Success