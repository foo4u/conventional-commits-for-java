package com.smartling.cc4j.semantic.release.common

import org.eclipse.jgit.revwalk.RevCommit

class SimpleSemanticVersionChangeResolver : SemanticVersionChangeResolver {
    override fun resolveChange(commits: Iterable<RevCommit>): SemanticVersionChange {
        val commitList: MutableList<Commit> = ArrayList()
        commits.iterator().forEachRemaining { c: RevCommit -> commitList.add(Commit(GitCommitAdapter(c))) }
        var change = SemanticVersionChange.NONE

        // FIXME: this needs to be optimized
        for (c in commitList) {
            val commitType = c.getCommitType()
            if (commitType != null) {
                if (SemanticVersionChange.MAJOR == commitType.changeType) {
                    return SemanticVersionChange.MAJOR
                } else if (SemanticVersionChange.MINOR == commitType.changeType) {
                    change = SemanticVersionChange.MINOR
                } else if (SemanticVersionChange.PATCH == commitType.changeType) {
                    if (SemanticVersionChange.MINOR != change) {
                        change = SemanticVersionChange.PATCH
                    }
                }
            }
        }
        return change
    }

//    private fun
}
