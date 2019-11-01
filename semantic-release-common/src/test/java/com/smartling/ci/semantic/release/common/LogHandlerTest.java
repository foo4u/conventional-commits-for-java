package com.smartling.ci.semantic.release.common;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class LogHandlerTest {

    private Git git;
    private LogHandler logHandler;
    private Repository repository;
    private Path tempDir;

    @Before
    public void setUp() throws Exception {
        tempDir = Files.createTempDirectory("git-repo-test");
        System.err.println(tempDir.toString());
        repository = new RepositoryBuilder().setWorkTree(tempDir.toFile()).build();

        repository.create();
        git = Git.wrap(repository);

        // disable GPG signing
        repository.getConfig().setBoolean("commit", null, "gpgsign", false);

        BufferedWriter writer = new BufferedWriter(new FileWriter(tempDir.resolve("hello.txt").toFile()));
        writer.write("Hello world");
        writer.newLine();
        writer.close();

        git.add().addFilepattern("*.txt");
        git.commit().setMessage("First commit").call();

        logHandler = new LogHandler(repository);
    }

    @After
    public void tearDown() throws Exception
    {
        //Files.delete(tempDir);
    }

    @Test(expected = NullPointerException.class)
    public void contructor()
    {
        new LogHandler(null);
    }

    @Test
    public void getLastTaggedCommit() throws IOException, GitAPIException {
        assertNull(logHandler.getLastTaggedCommit());
    }

    @Test
    public void getLastTaggedCommitTagFound() throws IOException, GitAPIException {
        git.tag().setAnnotated(true).setName("v1.0.0").setMessage("Release 1.0.0").call();
        assertNotNull(logHandler.getLastTaggedCommit());
    }

    @Test
    public void getCommitsSinceLastTag() throws IOException, GitAPIException {
        assertNotNull(logHandler.getCommitsSinceLastTag());
    }
}
