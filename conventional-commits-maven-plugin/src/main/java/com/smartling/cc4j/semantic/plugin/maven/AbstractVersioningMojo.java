package com.smartling.cc4j.semantic.plugin.maven;

import com.smartling.cc4j.semantic.plugin.maven.context.MavenConventionalVersioning;
import com.smartling.cc4j.semantic.release.common.ConventionalVersioning;
import com.smartling.cc4j.semantic.release.common.SemanticVersion;
import com.smartling.cc4j.semantic.release.common.SemanticVersionChange;
import com.smartling.cc4j.semantic.release.common.scm.ScmApiException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

abstract class AbstractVersioningMojo extends AbstractMojo
{
    private final static String MVN_RELEASE_VERSION_PROPERTY = "releaseVersion";
    private final static String MVN_DEVELOPMENT_VERSION_PROPERTY = "developmentVersion";

    @Parameter(defaultValue = "${project.build.directory}", property = "outputDir", required = true)
    File outputDirectory;

    @Parameter(defaultValue = "${project.version}", required = true)
    String versionString;

    @Parameter(defaultValue = "${reactorProjects}", readonly = true, required = true)
    List<MavenProject> reactorProjects;

    Properties createReleaseProperties() throws IOException, ScmApiException
    {
        MavenConventionalVersioning mvnConventionalVersioning;
        Properties props = new Properties();

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

        return props;
    }
}
