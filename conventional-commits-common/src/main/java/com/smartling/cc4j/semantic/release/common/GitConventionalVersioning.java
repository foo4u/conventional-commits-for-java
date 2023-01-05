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

    private SemanticVersionChangeResolver semanticVersionChangeResolver()
    {
        return new SimpleSemanticVersionChangeResolver();
    }

    @Override
    public SemanticVersionChange getNextVersionChangeType() throws ScmApiException, IOException
    {
        try
        {
            final Iterable<RevCommit> commits = logHandler().getCommitsSinceLastTag();
            final SemanticVersionChangeResolver resolver = semanticVersionChangeResolver();

            return resolver.resolveChange(commits);
        }
        catch (final GitAPIException e)
        {
            throw new ScmApiException("Git operation failed", e);
        }
    }

    @Override
    public SemanticVersion getNextVersion(SemanticVersion currentVersion) throws IOException, ScmApiException
    {
        final SemanticVersionChange change = this.getNextVersionChangeType();
        return currentVersion.nextVersion(change);
    }
}
