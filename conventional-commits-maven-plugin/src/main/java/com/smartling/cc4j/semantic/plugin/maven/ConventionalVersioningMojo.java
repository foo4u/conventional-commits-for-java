package com.smartling.cc4j.semantic.plugin.maven;

import com.smartling.cc4j.semantic.release.common.scm.ScmApiException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
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
            final Properties props = this.createReleaseProperties();

            session.getUserProperties().putAll(props);
            writeVersionFile(props);

            getLog().info(String.format(Locale.US, "Set release properties: %s", props));
        }
        catch (final IOException | ScmApiException e)
        {
            throw new MojoExecutionException("SCM error: " + e.getMessage(), e);
        }
    }

    private void writeVersionFile(final Properties props) throws MojoExecutionException
    {
        final File f = outputDirectory;

        if (!f.exists())
        {
            f.mkdirs();
        }

        File touch = new File(f, "version.props");

        try (OutputStream out = Files.newOutputStream(touch.toPath()))
        {
            props.store(out, "");
        }
        catch (final IOException e)
        {
            throw new MojoExecutionException("Error creating file " + touch, e);
        }
    }
}
