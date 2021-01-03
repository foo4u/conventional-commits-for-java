package com.smartling.cc4j.semantic.release.common;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommitTest
{
    @Test
    public void isConventional()
    {
        assertTrue(create("fix!: new version").isConventional());
        assertTrue(create("fix: fix foo").isConventional());
        assertTrue(create("feat!: add foo").isConventional());
        assertTrue(create("feat: add foo").isConventional());
        assertFalse(create("Add foo").isConventional());
    }

    @Test
    public void getCommitTypeBreakingChange()
    {
        assertEquals(ConventionalCommitType.BREAKING_CHANGE, create("breaking change: new version").getCommitType());
        assertEquals(ConventionalCommitType.BREAKING_CHANGE, create("Breaking Change: add foo").getCommitType());
        assertEquals(ConventionalCommitType.BREAKING_CHANGE, create("BREAKING CHANGE(scope): add foo").getCommitType());
    }

    @Test
    public void getCommitTypeBreakingChangeExclamation()
    {
        assertEquals(ConventionalCommitType.BREAKING_CHANGE, create("fix!: new version").getCommitType());
        assertEquals(ConventionalCommitType.BREAKING_CHANGE, create("feat!: new version").getCommitType());
        assertEquals(ConventionalCommitType.TEST, create("test!: new version").getCommitType());
    }

    @Test
    public void getCommitTypeFeat()
    {
        assertEquals(ConventionalCommitType.FEAT, create("feat: add foo").getCommitType());
        assertEquals(ConventionalCommitType.FEAT, create("Feat: add foo").getCommitType());
        assertEquals(ConventionalCommitType.FEAT, create("feat(scope): add foo").getCommitType());
    }

    @Test
    public void getCommitTypeFix()
    {
        assertEquals(ConventionalCommitType.FIX, create("fix: foo").getCommitType());
        assertEquals(ConventionalCommitType.FIX, create("Fix: foo").getCommitType());
        assertEquals(ConventionalCommitType.FIX, create("fix(scope): foo").getCommitType());
    }

    static Commit create(String shortMessage)
    {
        return new Commit(new DummyCommitAdapter(shortMessage));
    }
}
