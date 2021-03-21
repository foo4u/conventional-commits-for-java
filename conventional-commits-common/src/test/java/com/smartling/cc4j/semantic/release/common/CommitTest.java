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
        assertTrue(create("refactor: code cleanup").isConventional());
    }

    @Test
    public void getCommitTypeBreakingChange()
    {
        assertEquals(ConventionalCommitType.BREAKING_CHANGE, create("breaking change: new version").getCommitType().get());
        assertEquals(ConventionalCommitType.BREAKING_CHANGE, create("Breaking Change: add foo").getCommitType().get());
        assertEquals(ConventionalCommitType.BREAKING_CHANGE, create("BREAKING CHANGE(scope): add foo").getCommitType().get());
    }

    @Test
    public void getCommitTypeBreakingChangeExclamation()
    {
        assertEquals(ConventionalCommitType.BREAKING_CHANGE, create("fix!: new version").getCommitType().get());
        assertEquals(ConventionalCommitType.BREAKING_CHANGE, create("feat!: new version").getCommitType().get());
        assertEquals(ConventionalCommitType.TEST, create("test!: new version").getCommitType().get());
    }

    @Test
    public void getCommitTypeFeat()
    {
        assertEquals(ConventionalCommitType.FEAT, create("feat: add foo").getCommitType().get());
        assertEquals(ConventionalCommitType.FEAT, create("Feat: add foo").getCommitType().get());
        assertEquals(ConventionalCommitType.FEAT, create("feat(scope): add foo").getCommitType().get());
    }

    @Test
    public void getCommitTypeFix()
    {
        assertEquals(ConventionalCommitType.FIX, create("fix: foo").getCommitType().get());
        assertEquals(ConventionalCommitType.FIX, create("Fix: foo").getCommitType().get());
        assertEquals(ConventionalCommitType.FIX, create("fix(scope): foo").getCommitType().get());
    }

    @Test
    public void getMessage() {
        assertEquals("commit message", create("fix: commit message").getCommitMessageDescription().get());
        assertEquals("commit message", create("fix: [22] commit message").getCommitMessageDescription().get());
        assertFalse( create("fix commit message").getCommitMessageDescription().isPresent());
    }

    @Test
    public void getTrackingSystemId() {
        assertEquals("22", create("fix: [22] commit message").getTrackingSystemId().get());
        assertEquals("22", create("fix: [22 ] commit message").getTrackingSystemId().get());
        assertEquals("22", create("fix:[ 22 ] commit message").getTrackingSystemId().get());
        assertFalse(create("fix [22] commit message").getTrackingSystemId().isPresent());
        assertFalse(create("fix: commit message").getTrackingSystemId().isPresent());
    }

    static Commit create(String shortMessage)
    {
        return new Commit(new DummyCommitAdapter(shortMessage));
    }
}
