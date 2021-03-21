package com.smartling.cc4j.semantic.plugin.maven;

import com.smartling.cc4j.semantic.release.common.Commit;
import com.smartling.cc4j.semantic.release.common.ConventionalCommitType;
import com.smartling.cc4j.semantic.release.common.changelog.ChangelogGenerator;
import com.smartling.cc4j.semantic.release.common.scm.RepositoryAdapter;
import com.smartling.cc4j.semantic.release.common.scm.ScmApiException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Mojo(name = "changelog", aggregator = true, defaultPhase = LifecyclePhase.VALIDATE)
public class ConventionalChangelogMojo extends AbstractVersioningMojo {

    private static final String CHANGELOG_FILE_NAME = "CHANGELOG.MD";

    @Parameter( property = "conventional-commits-maven-plugin.repoUrlFormat")
    private String repoUrlFormat;

    @Parameter( property = "conventional-commits-maven-plugin.trackingSystemUrlFormat")
    private String trackingSystemUrlFormat;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            Map<ConventionalCommitType, Set<Commit>> commitsByCommitTypes = this
                .getChangelogExtractor()
                .getGroupedCommitsByCommitTypes();

            ChangelogGenerator changelogGenerator = new ChangelogGenerator(repoUrlFormat, trackingSystemUrlFormat);
            String changeLogs = changelogGenerator.generate(this.getNextVersion().toString(), commitsByCommitTypes);
            appendChangeLogs(changeLogs);
            commitChanges();
        } catch (IOException | ScmApiException e) {
            throw new MojoExecutionException("SCM error: " + e.getMessage(), e);
        }
    }

    private void appendChangeLogs(String changeLogs) throws IOException {
        Path changelogPath = Paths.get(this.baseDir.getAbsolutePath(), CHANGELOG_FILE_NAME);
        if(!Files.exists(changelogPath)) {
            Files.createFile(changelogPath);
        }

        List<String> resultChangeLogs = new ArrayList<>();
        resultChangeLogs.add(changeLogs);
        List<String> prevChangeLogs = Files.readAllLines(changelogPath);
        resultChangeLogs.addAll(prevChangeLogs);
        Files.write(changelogPath, resultChangeLogs);
    }

    private void commitChanges() throws IOException, ScmApiException {
        RepositoryAdapter repositoryAdapter = getRepositoryAdapter();
        repositoryAdapter.addFile(CHANGELOG_FILE_NAME);
        repositoryAdapter.commit("ci: update changelog");
    }
}
