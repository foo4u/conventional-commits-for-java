package com.smartling.cc4j.semantic.plugin.maven;

import com.smartling.cc4j.semantic.plugin.maven.context.NoVersionChangeException;
import com.smartling.cc4j.semantic.release.common.ConventionalVersioning;
import com.smartling.cc4j.semantic.release.common.SemanticVersionChange;
import com.smartling.cc4j.semantic.release.common.scm.ScmApiException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.IOException;

@Mojo(name = "validate", aggregator = true, defaultPhase = LifecyclePhase.VALIDATE)
public class ConventionalVersionChangeValidatorMojo extends AbstractVersioningMojo
{
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        try
        {
            ConventionalVersioning versioning = this.getConventionalVersioning();
            SemanticVersionChange change = versioning.getNextVersionChangeType();

            if (SemanticVersionChange.NONE.equals(change))
            {
                throw new NoVersionChangeException("No conventional commit version change to release");
            }
        }
        catch (IOException | ScmApiException e)
        {
            throw new MojoExecutionException("Unable to access repository", e);
        }
    }
}
