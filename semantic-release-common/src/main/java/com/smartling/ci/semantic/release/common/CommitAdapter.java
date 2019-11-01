package com.smartling.ci.semantic.release.common;

public interface CommitAdapter<T>
{
    String getShortMessage();

    T getCommit();
}
