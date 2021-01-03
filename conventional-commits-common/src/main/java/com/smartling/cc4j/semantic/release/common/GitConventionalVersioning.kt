package com.smartling.cc4j.semantic.release.common

import kotlin.Throws
import com.smartling.cc4j.semantic.release.common.scm.ScmApiException
import java.io.IOException
import org.eclipse.jgit.api.errors.GitAPIException
import com.smartling.cc4j.semantic.release.common.SemanticVersion
import org.eclipse.jgit.lib.Repository
import java.util.*

class GitConventionalVersioning(private val repository: Repository) : ConventionalVersioning {
    private val semanticVersionChangeResolver = SimpleSemanticVersionChangeResolver()

    override fun logHandler(): LogHandler {
        return LogHandler(this.repository)
    }

    @Throws(ScmApiException::class, IOException::class)
    override fun getNextVersionChangeType(): SemanticVersionChange {
        return try {
            val commits = logHandler().getCommitsSinceLastTag()
            this.semanticVersionChangeResolver.resolveChange(commits)
        } catch (e: GitAPIException) {
            throw ScmApiException("Git operation failed", e)
        }
    }

    @Throws(IOException::class, ScmApiException::class)
    override fun getNextVersion(currentVersion: SemanticVersion): SemanticVersion {
        val change = this.getNextVersionChangeType()
        return currentVersion.nextVersion(change)
    }
}
