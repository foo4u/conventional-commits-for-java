package com.smartling.cc4j.semantic.release.common

import com.smartling.cc4j.semantic.release.common.CommitAdapter
import com.smartling.cc4j.semantic.release.common.ConventionalCommitType
import java.util.*
import java.util.regex.Pattern

class Commit(private val commit: CommitAdapter<*>) {

    private fun getMessageForComparison(): String = this.commit.getShortMessage().toLowerCase()

    /**
     * Returns true if this commit is written in conventional commit style;
     * false otherwise.
     */
    fun isConventional(): Boolean = this.getCommitType() != null

    private fun isBreaking(): Boolean {
        val msg = this.getMessageForComparison()
        return BREAKING_REGEX.matcher(msg).matches()
    }

    fun getCommitType(): ConventionalCommitType? {
        val msg = this.getMessageForComparison()
        var type: ConventionalCommitType? = null

        if (this.isBreaking())
            return ConventionalCommitType.BREAKING_CHANGE

        for (cc in ConventionalCommitType.values()) {
            for (t in cc.commitTypes) {
                if (msg.startsWith(t)) {
                    type = cc
                    break
                }
            }
        }

    return type
}

    companion object {
        private val BREAKING_REGEX = Pattern.compile("^(fix|feat)!.+", Pattern.CASE_INSENSITIVE)
    }
}
