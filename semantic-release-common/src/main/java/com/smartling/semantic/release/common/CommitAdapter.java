package com.smartling.semantic.release.common;

public interface CommitAdapter<T>
{
    String getShortMessage();

    T getCommit();
}
