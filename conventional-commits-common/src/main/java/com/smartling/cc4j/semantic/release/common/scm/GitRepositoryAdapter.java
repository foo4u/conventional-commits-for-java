package com.smartling.cc4j.semantic.release.common.scm;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;

import java.util.Objects;

public class GitRepositoryAdapter implements RepositoryAdapter {

    private final Repository repository;
    private final Git git;

    public GitRepositoryAdapter(Repository repository) {
        this.repository = repository;
        this.git = Git.wrap(repository);
    }

    @Override
    public void addFile(String pattern) throws ScmApiException {
        try {
            this.git.add().addFilepattern(pattern).call();
        } catch (GitAPIException e) {
            throw new ScmApiException("Failed to add file: " + pattern, e);
        }
    }

    @Override
    public void commit(String message) throws ScmApiException {
        Objects.requireNonNull(message, "commit message can not be null");

        try {
            this.git.commit().setMessage(message).call();
        } catch (GitAPIException e) {
            throw new ScmApiException("Failed to perform commit", e);
        }
    }
}
