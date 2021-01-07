package com.smartling.cc4j.semantic.release.common

import org.eclipse.jgit.revwalk.RevCommit
import com.smartling.cc4j.semantic.release.common.CommitAdapter
import java.util.*
import java.util.regex.Pattern

class GitCommitAdapter(private val commit: RevCommit) : CommitAdapter<RevCommit> {
    private fun getMessageForComparison(): String = this.commit.getShortMessage().toLowerCase()

    private fun isBreaking(): Boolean {
        val msg = this.getMessageForComparison()
        return Commit.BREAKING_REGEX.matcher(msg).matches()
    }

    override fun getShortMessage(): String = this.commit.shortMessage
    override fun getCommit(): RevCommit = this.commit
    override fun getCommitType(): ConventionalCommitType? {
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
}
