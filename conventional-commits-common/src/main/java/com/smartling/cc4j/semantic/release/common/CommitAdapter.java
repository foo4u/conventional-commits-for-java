package com.smartling.cc4j.semantic.release.common;

public interface CommitAdapter<T>
{
    String getShortMessage();

    T getCommit();

    String getCommitHash();
}
