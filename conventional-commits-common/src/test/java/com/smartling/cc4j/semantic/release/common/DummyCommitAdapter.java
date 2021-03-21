package com.smartling.cc4j.semantic.release.common;

class DummyCommitAdapter implements CommitAdapter<DummyCommitAdapter>
{
    private final String shortMessage;
    private final String hash;

    DummyCommitAdapter(String shortMessage)
    {
        this.shortMessage = shortMessage;
        this.hash = null;
    }

    DummyCommitAdapter(String shortMessage, String hash)
    {
        this.shortMessage = shortMessage;
        this.hash = hash;
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

    @Override
    public String getCommitHash() {
        return this.hash;
    }
}
