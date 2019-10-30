package com.smartling.conventional.plugin.maven.context;

import com.smartling.semantic.release.common.ConventionalVersioning;
import com.smartling.semantic.release.common.GitConventionalVersioning;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;

import java.io.IOException;

public class MavenConventionalVersioning
{
    private final Repository repository;

    public MavenConventionalVersioning() throws IOException
    {
        repository = new RepositoryBuilder().findGitDir().build();
    }

    public ConventionalVersioning getConventionalVersioning()
    {
        return new GitConventionalVersioning(repository);
    }
}
