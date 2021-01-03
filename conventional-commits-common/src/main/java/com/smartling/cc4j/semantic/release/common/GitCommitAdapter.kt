package com.smartling.cc4j.semantic.release.common

import org.eclipse.jgit.revwalk.RevCommit
import com.smartling.cc4j.semantic.release.common.CommitAdapter
import java.util.*

class GitCommitAdapter(private val commit: RevCommit) : CommitAdapter<RevCommit> {
    override fun getShortMessage(): String = this.commit.shortMessage
    override fun getCommit(): RevCommit = this.commit
}
