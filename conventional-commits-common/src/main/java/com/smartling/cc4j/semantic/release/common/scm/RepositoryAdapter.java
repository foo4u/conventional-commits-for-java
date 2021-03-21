package com.smartling.cc4j.semantic.release.common.scm;

public interface RepositoryAdapter {
    void addFile(String pattern) throws ScmApiException;
    void commit(String message) throws ScmApiException;
}
