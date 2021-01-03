package com.smartling.cc4j.semantic.release.common

interface CommitAdapter<T> {
    val shortMessage: String?
    val commit: T
}
