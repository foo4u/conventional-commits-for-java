package com.smartling.cc4j.semantic.release.common;

import org.eclipse.jgit.revwalk.RevCommit;

import java.util.Objects;

public class GitCommitAdapter implements CommitAdapter<RevCommit>
{
    private final RevCommit commit;

    public GitCommitAdapter(RevCommit commit)
    {
        Objects.requireNonNull(commit, "commit cannot be null");
        this.commit = commit;
    }

    @Override
    public String getShortMessage()
    {
        return commit.getShortMessage();
    }

    @Override
    public RevCommit getCommit()
    {
        return commit;
    }

    @Override
    public String getCommitHash()
    {
        return commit.getName();
    }
}
