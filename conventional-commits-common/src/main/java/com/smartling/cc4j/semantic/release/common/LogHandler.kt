package com.smartling.cc4j.semantic.release.common

import org.eclipse.jgit.api.Git
import kotlin.Throws
import java.io.IOException
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.lib.Repository
import java.util.stream.Collectors
import org.eclipse.jgit.revplot.PlotWalk
import org.slf4j.LoggerFactory
import java.util.*

class LogHandler(private val repository: Repository) {
    private val logger = LoggerFactory.getLogger(LogHandler::class.java)
    private val git: Git = Git.wrap(repository)

    @Throws(IOException::class, GitAPIException::class)
    fun findLastTaggedCommit(): RevCommit? {
        val tags = git.tagList().call()
        val peeledTags = tags.map { t -> repository.peel(t).peeledObjectId }
//            tags.stream().map { t: Ref? -> repository.peel(t).peeledObjectId }.collect(Collectors.toList())
        val walk = PlotWalk(repository)
        val start = walk.parseCommit(repository.resolve("HEAD"))

        walk.markStart(start)

        walk.forEach { rev ->
            if (peeledTags.contains(rev.id)) {
                logger.debug("Found commit matching last tag: {}", rev)
                walk.close()
                return rev;
            }
        }

//        while (walk.next().also { revCommit = it } != null) {
//            if (peeledTags.contains(revCommit.id)) {
//                logger.debug("Found commit matching last tag: {}", revCommit)
//                break
//            }
//        }

        walk.close()
        return null
    }

    @Throws(IOException::class, GitAPIException::class)
    fun getCommitsSinceLastTag(): Iterable<RevCommit> {
        val start = repository.resolve("HEAD")
        val lastCommit = this.findLastTaggedCommit()
        if (lastCommit == null) {
            logger.warn("No annotated tags found matching any commits on branch")
            return git.log().call()
        }
        logger.info("Listing commits in range {}...{}", start.name, lastCommit.id.name)
        return git.log().addRange(lastCommit.id, start).call()
    }

}
