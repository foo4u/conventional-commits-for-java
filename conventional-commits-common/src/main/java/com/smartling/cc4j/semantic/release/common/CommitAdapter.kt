package com.smartling.cc4j.semantic.release.common

interface CommitAdapter<T> {
    fun getShortMessage(): String
    fun getCommit(): T
}
