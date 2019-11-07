package com.smartling.cc4j.semantic.plugin.maven;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.junit.Before;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.UUID;

public class AbstractScmMojoTest
{
    Path projectPath;
    private Repository repository;
    private Git git;

    @Before
    public void setUp() throws Exception
    {
        Path tempDir = Files.createTempDirectory("convention-commits-version-change-mojo-test");
        Path sourceProjectPath = Paths.get("target/test-classes/project-to-test/pom.xml");
        Files.copy(sourceProjectPath, tempDir.resolve("pom.xml"));

        this.repository = new RepositoryBuilder().setWorkTree(tempDir.toFile()).build();
        this.git = Git.wrap(repository);
        this.projectPath = tempDir;

        repository.create();

        // disable GPG signing
        repository.getConfig().setBoolean("commit", null, "gpgsign", false);

        createCommit("Initial commit");
    }

    void createCommit(String commitMessage) throws IOException, GitAPIException
    {
        String filename = String.format(Locale.US, "%s.txt", UUID.randomUUID().toString());
        BufferedWriter writer = new BufferedWriter(new FileWriter(projectPath.resolve(filename).toFile()));
        writer.write("Hello world");
        writer.newLine();
        writer.close();

        git.add().addFilepattern("*.txt");
        git.commit().setMessage(commitMessage).call();
    }
}
