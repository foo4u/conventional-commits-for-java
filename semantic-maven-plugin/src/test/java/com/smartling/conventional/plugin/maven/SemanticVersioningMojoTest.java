package com.smartling.conventional.plugin.maven;

import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.WithoutMojo;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SemanticVersioningMojoTest
{
    @Rule public MojoRule rule = new MojoRule()
    {
        @Override
        protected void before() throws Throwable
        {
        }

        @Override
        protected void after()
        {
        }
    };

    /**
     * @throws Exception if any
     */
    @Test
    public void testExecute() throws Exception
    {
        File pom = new File("target/test-classes/project-to-test/");
        assertNotNull(pom);
        assertTrue(pom.exists());

        SemanticVersioningMojo myMojo = (SemanticVersioningMojo) rule.lookupConfiguredMojo(pom, "version");
        assertNotNull(myMojo);
        myMojo.execute();

        File outputDirectory = (File) rule.getVariableValueFromObject(myMojo, "outputDirectory");
        assertNotNull(outputDirectory);
        assertTrue(outputDirectory.exists());

        File touch = new File(outputDirectory, "version.txt");
        assertTrue(touch.exists());

    }

    /**
     * Do not need the MojoRule.
     */
    @WithoutMojo
    @Test
    public void testSomethingWhichDoesNotNeedTheMojoAndProbablyShouldBeExtractedIntoANewClassOfItsOwn()
    {
        assertTrue(true);
    }

}

