package com.smartling.cc4j.semantic.release.common

import com.smartling.cc4j.semantic.release.common.CommitAdapter
import com.smartling.cc4j.semantic.release.common.ConventionalCommitType
import java.util.*
import java.util.regex.Pattern

class Commit(private val commit: CommitAdapter<*>) {

    /**
     * Returns true if this commit is written in conventional commit style;
     * false otherwise.
     */
    fun isConventional(): Boolean = this.getCommitType() != null

    fun getCommitType(): ConventionalCommitType? = this.commit.getCommitType()

    companion object {
        val BREAKING_REGEX = Pattern.compile("^(fix|feat)!.+", Pattern.CASE_INSENSITIVE)
    }
}
