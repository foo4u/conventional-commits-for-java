package com.smartling.cc4j.semantic.plugin.maven;

import com.smartling.cc4j.semantic.plugin.maven.context.MavenConventionalVersioning;
import com.smartling.cc4j.semantic.release.common.ConventionalVersioning;
import com.smartling.cc4j.semantic.release.common.SemanticVersion;
import com.smartling.cc4j.semantic.release.common.SemanticVersionChange;
import com.smartling.cc4j.semantic.release.common.scm.ScmApiException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

@Mojo(name = "version", aggregator = true, defaultPhase = LifecyclePhase.VALIDATE)
public class ConventionalVersioningMojo extends AbstractVersioningMojo
{
    @Parameter(defaultValue = "${session}", readonly = true, required = true)
    private MavenSession session;

    public void execute() throws MojoExecutionException
    {
        try
        {
            Properties props = this.createReleaseProperties();

            session.getExecutionProperties().putAll(props);
            writeVersionFile(props);
            getLog().warn(String.format(Locale.US, "Set release properties: %s", props));
        }
        catch (IOException | ScmApiException e)
        {
            throw new MojoExecutionException("SCM error: " + e.getMessage(), e);
        }
    }

    private void writeVersionFile(Properties props) throws MojoExecutionException
    {
        File f = outputDirectory;

        if (!f.exists())
        {
            f.mkdirs();
        }

        File touch = new File(f, "version.props");

        try (OutputStream out = new FileOutputStream(touch))
        {
            props.store(out, "");
        }
        catch (IOException e)
        {
            throw new MojoExecutionException("Error creating file " + touch, e);
        }
    }
}
