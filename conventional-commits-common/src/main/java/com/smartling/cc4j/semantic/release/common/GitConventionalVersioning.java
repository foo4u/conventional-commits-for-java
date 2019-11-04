package com.smartling.cc4j.semantic.release.common;

import com.smartling.cc4j.semantic.release.common.scm.ScmApiException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.util.Objects;

public class GitConventionalVersioning implements ConventionalVersioning
{
    private final Repository repository;

    public GitConventionalVersioning(Repository repository)
    {
        Objects.requireNonNull(repository, "repository required");
        this.repository = repository;
    }

    @Override
    public LogHandler logHandler()
    {
        return new LogHandler(repository);
    }

    @Override
    public SemanticVersionChangeResolver semanticVersionChangeResolver()
    {
        return new SimpleSemanticVersionChangeResolver();
    }

    @Override
    public SemanticVersion getNextVersion(SemanticVersion currentVersion) throws IOException, ScmApiException
    {
        try {
            Iterable<RevCommit> commits = logHandler().getCommitsSinceLastTag();
            SemanticVersionChangeResolver resolver = semanticVersionChangeResolver();
            SemanticVersionChange change = resolver.resolveChange(commits);

            return currentVersion.nextVersion(change);
        }
        catch (GitAPIException e)
        {
            throw new ScmApiException("Git operation failed", e);
        }
    }
}
