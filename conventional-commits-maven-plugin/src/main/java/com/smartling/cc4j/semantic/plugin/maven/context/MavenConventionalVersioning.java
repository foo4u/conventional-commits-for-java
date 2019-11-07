package com.smartling.cc4j.semantic.plugin.maven.context;

import com.smartling.cc4j.semantic.release.common.ConventionalVersioning;
import com.smartling.cc4j.semantic.release.common.GitConventionalVersioning;
import org.eclipse.jgit.lib.Repository;

import java.util.Objects;

public class MavenConventionalVersioning
{
    private final Repository repository;

    public MavenConventionalVersioning(Repository repository)
    {
        Objects.requireNonNull(repository, "repository required");
        this.repository = repository;
    }

    public ConventionalVersioning getConventionalVersioning()
    {
        return new GitConventionalVersioning(repository);
    }
}
