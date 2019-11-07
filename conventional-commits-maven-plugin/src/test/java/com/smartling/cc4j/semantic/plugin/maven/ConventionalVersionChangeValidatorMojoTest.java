package com.smartling.cc4j.semantic.plugin.maven;

import com.smartling.cc4j.semantic.plugin.maven.context.NoVersionChangeException;
import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ConventionalVersionChangeValidatorMojoTest extends AbstractScmMojoTest
{
    @Rule
    public MojoRule rule = new MojoRule()
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

    ConventionalVersionChangeValidatorMojo getMojo() throws Exception
    {
        File pom = projectPath.toFile();
        assertNotNull(pom);
        assertTrue(pom.exists());

        ConventionalVersionChangeValidatorMojo myMojo = (ConventionalVersionChangeValidatorMojo) rule.lookupConfiguredMojo(pom, "validate");
        assertNotNull(myMojo);

        return myMojo;
    }

    @Test(expected = NoVersionChangeException.class)
    public void executeNoConventionalCommits() throws Exception
    {
        ConventionalVersionChangeValidatorMojo myMojo = getMojo();
        myMojo.execute();
    }

    @Test(expected = NoVersionChangeException.class)
    public void executeNoConventionalVersionChangeCommits() throws Exception
    {
        ConventionalVersionChangeValidatorMojo myMojo = getMojo();
        createCommit("ci: release x.y.z");
        myMojo.execute();
    }

    @Test
    public void executeChoreCommit() throws Exception
    {
        ConventionalVersionChangeValidatorMojo myMojo = getMojo();
        createCommit("chore: add something");
        myMojo.execute();
    }

    @Test
    public void executeFeatCommit() throws Exception
    {
        ConventionalVersionChangeValidatorMojo myMojo = getMojo();
        createCommit("feat: add foo");
        myMojo.execute();
    }

    @Test
    public void executeFixCommit() throws Exception
    {
        ConventionalVersionChangeValidatorMojo myMojo = getMojo();
        createCommit("fix: add something");
        myMojo.execute();
    }

    @Test
    public void executeBreakingChangeCommit() throws Exception
    {
        ConventionalVersionChangeValidatorMojo myMojo = getMojo();
        createCommit("fix: add something");
        myMojo.execute();
    }
}
