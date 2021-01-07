package com.smartling.cc4j.semantic.release.common

import org.eclipse.jgit.revwalk.RevCommit

class SimpleSemanticVersionChangeResolver : SemanticVersionChangeResolver {
    override fun resolveChange(commits: Iterable<RevCommit>): SemanticVersionChange {
        return commits
            .map { c -> Commit(GitCommitAdapter(c)) }
            .fold(SemanticVersionChange.NONE) { type, commit ->
                val changeType = this.resolveChange(commit)
                return when {
                    SemanticVersionChange.MAJOR == changeType -> SemanticVersionChange.MAJOR
                    SemanticVersionChange.MINOR == changeType && SemanticVersionChange.MAJOR != type -> SemanticVersionChange.MINOR
                    SemanticVersionChange.PATCH == changeType && SemanticVersionChange.MINOR != type -> SemanticVersionChange.PATCH
                    else -> type
                }
            }
    }

    fun resolveChange(commit: Commit): SemanticVersionChange {
        val changeType = commit.getCommitType()?.changeType
        return when {
            SemanticVersionChange.MAJOR == changeType -> SemanticVersionChange.MAJOR
            SemanticVersionChange.MINOR == changeType -> SemanticVersionChange.MINOR
            SemanticVersionChange.PATCH == changeType -> SemanticVersionChange.PATCH
            else -> SemanticVersionChange.NONE
        }
    }
}
