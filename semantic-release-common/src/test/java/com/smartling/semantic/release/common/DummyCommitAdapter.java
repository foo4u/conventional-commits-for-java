package com.smartling.semantic.release.common;

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
}
