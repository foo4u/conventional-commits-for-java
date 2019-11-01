package com.smartling.ci.semantic.release.common;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revplot.PlotWalk;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LogHandler
{
    private final Logger logger = LoggerFactory.getLogger(LogHandler.class);

    private final Repository repository;
    private final Git git;

    public LogHandler(Repository repository)
    {
        Objects.requireNonNull(repository, "repository cannot be null");
        this.repository = repository;
        this.git = Git.wrap(repository);
    }

    RevCommit getLastTaggedCommit() throws IOException, GitAPIException
    {
        List<Ref> tags = git.tagList().call();
        List<ObjectId> peeledTags = tags.stream().map(t -> repository.peel(t).getPeeledObjectId()).collect(Collectors.toList());
        PlotWalk walk = new PlotWalk(repository);
        RevCommit start = walk.parseCommit(repository.resolve("HEAD"));

        walk.markStart(start);

        RevCommit revCommit;
        while ((revCommit = walk.next()) != null)
        {
            if (peeledTags.contains(revCommit.getId()))
            {
                logger.debug("Found commit matching last tag: {}", revCommit);
                break;
            }
        }

        walk.close();

        return revCommit;
    }

    public Iterable<RevCommit> getCommitsSinceLastTag() throws IOException, GitAPIException
    {
        ObjectId start = repository.resolve("HEAD");
        RevCommit lastCommit = this.getLastTaggedCommit();

        if (lastCommit == null)
        {
            logger.warn("No annotated tags found matching any commits on branch");
            return git.log().call();
        }

        logger.info("Listing commits in range {}...{}", start.getName(), lastCommit.getId().getName());

        return git.log().addRange(lastCommit.getId(), start).call();
    }

}

