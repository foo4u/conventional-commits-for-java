package com.smartling.cc4j.semantic.release.common

import org.eclipse.jgit.revwalk.RevCommit

interface SemanticVersionChangeResolver {
    fun resolveChange(commits: Iterable<RevCommit>): SemanticVersionChange
}
