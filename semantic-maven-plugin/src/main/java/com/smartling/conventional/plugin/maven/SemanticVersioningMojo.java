package com.smartling.conventional.plugin.maven;

import com.smartling.conventional.plugin.maven.context.MavenConventionalVersioning;
import com.smartling.semantic.release.common.ConventionalVersioning;
import com.smartling.semantic.release.common.SemanticVersion;
import com.smartling.semantic.release.common.SemanticVersionChange;
import com.smartling.semantic.release.common.scm.ScmApiException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Mojo(name = "version", defaultPhase = LifecyclePhase.VALIDATE)
public class SemanticVersioningMojo extends AbstractMojo
{
    @Parameter(defaultValue = "${project.build.directory}", property = "outputDir", required = true) private File outputDirectory;
    @Parameter(defaultValue = "${project.version}", required = true) private String versionString;

    public void execute() throws MojoExecutionException
    {
        MavenConventionalVersioning mvnConventionalVersioning;

        try
        {
            mvnConventionalVersioning = new MavenConventionalVersioning();
            ConventionalVersioning versioning = mvnConventionalVersioning.getConventionalVersioning();
            SemanticVersion nextVersion = versioning.getNextVersion(SemanticVersion.parse(versionString.replace("-SNAPSHOT", "")));
            SemanticVersion nextDevelopmentVersion = nextVersion.nextVersion(SemanticVersionChange.PATCH);

            writeVersionFile(nextVersion.toString(), nextDevelopmentVersion.toString() + "-SNAPSHOT");
        }
        catch (IOException | ScmApiException e)
        {
            throw new MojoExecutionException("SCM error: " + e.getMessage(), e);
        }

    }

    private void writeVersionFile(String nextVersion, String nextDevelopmentVersion) throws MojoExecutionException
    {
        File f = outputDirectory;

        if (!f.exists())
        {
            f.mkdirs();
        }

        File touch = new File(f, "version.txt");

        try (FileWriter w = new FileWriter(touch))
        {
            w.write(String.format("Current version:  %s%n", versionString));
            w.write(String.format("Next version:     %s%n", nextVersion));
            w.write(String.format("Next dev version: %s%n", nextDevelopmentVersion));
        }
        catch (IOException e)
        {
            throw new MojoExecutionException("Error creating file " + touch, e);
        }
    }
}
