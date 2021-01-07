package com.smartling.cc4j.semantic.release.common;

import org.jetbrains.annotations.Nullable;

class DummyCommitAdapter implements CommitAdapter<DummyCommitAdapter>
{
    private final String shortMessage;

    DummyCommitAdapter(String shortMessage)
    {
        this.shortMessage = shortMessage;
    }

    @Override
    public String getShortMessage()
    {
        return shortMessage;
    }

    @Override
    public DummyCommitAdapter getCommit()
    {
        return null;
    }

    @Nullable
    @Override
    public ConventionalCommitType getCommitType()
    {
        return null;
    }
}
