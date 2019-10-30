package com.smartling.conventional.plugin.maven;

import com.smartling.conventional.plugin.maven.context.MavenConventionalVersioning;
import com.smartling.semantic.release.common.ConventionalVersioning;
import com.smartling.semantic.release.common.SemanticVersion;
import com.smartling.semantic.release.common.SemanticVersionChange;
import com.smartling.semantic.release.common.scm.ScmApiException;
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
public class SemanticVersioningMojo extends AbstractMojo
{
    private final static String MVN_RELEASE_VERSION_PROPERTY = "releaseVersion";
    private final static String MVN_DEVELOPMENT_VERSION_PROPERTY = "developmentVersion";
    private final static String MVN_TAG_NAME_PROPERTY = "tag";

    @Parameter(defaultValue = "${project.build.directory}", property = "outputDir", required = true)
    private File outputDirectory;

    @Parameter(defaultValue = "${project.version}", required = true)
    private String versionString;

    @Parameter(defaultValue = "${session}", readonly = true, required = true)
    private MavenSession session;

    @Parameter(defaultValue = "${reactorProjects}", readonly = true, required = true)
    private List<MavenProject> reactorProjects;

    public void execute() throws MojoExecutionException
    {
        MavenConventionalVersioning mvnConventionalVersioning;
        Properties props = new Properties();

        try
        {
            mvnConventionalVersioning = new MavenConventionalVersioning();
            ConventionalVersioning versioning = mvnConventionalVersioning.getConventionalVersioning();
            SemanticVersion nextVersion = versioning.getNextVersion(SemanticVersion.parse(versionString.replace("-SNAPSHOT", "")));
            SemanticVersion nextDevelopmentVersion = nextVersion.nextVersion(SemanticVersionChange.PATCH);

            // set properties for release plugin
            props.setProperty(MVN_RELEASE_VERSION_PROPERTY, nextVersion.toString());
            props.setProperty(MVN_DEVELOPMENT_VERSION_PROPERTY, nextDevelopmentVersion.toString() + "-SNAPSHOT");

            for (MavenProject project : reactorProjects)
            {
                String projectKey = project.getGroupId() + ":" + project.getArtifactId();
                props.setProperty("project.rel." + projectKey, nextVersion.toString());
                props.setProperty("project.dev." + projectKey, nextDevelopmentVersion.toString());
            }

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

        File touch = new File(f, "version.txt");

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
