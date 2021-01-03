package com.smartling.cc4j.semantic.release.common

import com.smartling.cc4j.semantic.release.common.CommitAdapter
import com.smartling.cc4j.semantic.release.common.ConventionalCommitType
import java.util.*
import java.util.regex.Pattern

class Commit(commit: CommitAdapter<*>) {
    private val commit: CommitAdapter<*>

    /**
     * Returns true if this commit is written in conventional commit style;
     * false otherwise.
     */
    val isConventional: Boolean
        get() = commitType.isPresent

    // FIXME: check for breaking change in footer
/*
        for (FooterLine footerLine : commit.getFooterLines())
        {
            if (footerLine.getKey().equalsIgnoreCase(ConventionalCommitType.BREAKING_CHANGE.getCommitTypes().get(0))) {
                type = ConventionalCommitType.BREAKING_CHANGE;
                break;
            }
        }

 */
    val commitType: Optional<ConventionalCommitType>
        get() {
            val msg = messageForComparison
            var type: ConventionalCommitType? = null
            if (msg.startsWith("!")) {
                return Optional.empty()
            }
            if (BREAKING_REGEX.matcher(msg).matches()) {
                return Optional.of(ConventionalCommitType.BREAKING_CHANGE)
            }
            for (cc in ConventionalCommitType.values()) {
                for (t in cc.commitTypes) {
                    if (msg.startsWith(t!!)) {
                        type = cc
                        break
                    }
                }
            }

            // FIXME: check for breaking change in footer
/*
        for (FooterLine footerLine : commit.getFooterLines())
        {
            if (footerLine.getKey().equalsIgnoreCase(ConventionalCommitType.BREAKING_CHANGE.getCommitTypes().get(0))) {
                type = ConventionalCommitType.BREAKING_CHANGE;
                break;
            }
        }

 */return Optional.ofNullable(type)
        }
    private val messageForComparison: String
        private get() {
            val msg = commit.shortMessage
            return msg?.toLowerCase() ?: ""
        }

    companion object {
        private val BREAKING_REGEX = Pattern.compile("^(fix|feat)!.+", Pattern.CASE_INSENSITIVE)
    }

    init {
        Objects.requireNonNull(commit, "commit may not be null")
        this.commit = commit
    }
}
