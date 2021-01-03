package com.smartling.cc4j.semantic.release.common

import org.eclipse.jgit.revwalk.RevCommit

class SimpleSemanticVersionChangeResolver : SemanticVersionChangeResolver {
    override fun resolveChange(commits: Iterable<RevCommit>): SemanticVersionChange {
        val commitList: MutableList<Commit> = ArrayList()
        commits.iterator().forEachRemaining { c: RevCommit -> commitList.add(Commit(GitCommitAdapter(c))) }
        var change = SemanticVersionChange.NONE

        for (c in commitList) {
            val commitType = c.commitType
            if (commitType.isPresent) {
                if (SemanticVersionChange.MAJOR == commitType.get().changeType) {
                    return SemanticVersionChange.MAJOR
                } else if (SemanticVersionChange.MINOR == commitType.get().changeType) {
                    change = SemanticVersionChange.MINOR
                } else if (SemanticVersionChange.PATCH == commitType.get().changeType) {
                    if (SemanticVersionChange.MINOR != change) {
                        change = SemanticVersionChange.PATCH
                    }
                }
            }
        }
        return change
    }
}
