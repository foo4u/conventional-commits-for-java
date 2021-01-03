package com.smartling.cc4j.semantic.release.common

import org.eclipse.jgit.revwalk.RevCommit
import com.smartling.cc4j.semantic.release.common.CommitAdapter
import java.util.*

class GitCommitAdapter(commit: RevCommit) : CommitAdapter<RevCommit> {
    override val shortMessage: String?
        get() = this.commit.shortMessage
    override val commit: RevCommit
        get() = this.commit
}
