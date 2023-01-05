package com.smartling.cc4j.semantic.release.common;

import org.junit.Test;

import java.util.Optional;

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
        assertFalse(create("breaking change: new version").getCommitType().isPresent());
        assertFalse(create("Breaking Change: add foo").getCommitType().isPresent());
        assertFalse(create("BREAKING CHANGE(scope): add foo").getCommitType().isPresent());
    }

    @Test
    public void getCommitTypeBreakingChangeExclamation()
    {
        final Optional<ConventionalCommitType> ctFix = create("fix!: new version").getCommitType();
        assertTrue(ctFix.isPresent());
        assertEquals(ConventionalCommitType.BREAKING_CHANGE, ctFix.get());

        final Optional<ConventionalCommitType> ctFeatBreaking = create("feat!: new version").getCommitType();
        assertTrue(ctFeatBreaking.isPresent());
        assertEquals(ConventionalCommitType.BREAKING_CHANGE, ctFeatBreaking.get());

        final Optional<ConventionalCommitType> ctTestBreaking = create("test!: new version").getCommitType();
        assertTrue(ctTestBreaking.isPresent());
        assertEquals(ConventionalCommitType.BREAKING_CHANGE, ctTestBreaking.get());
    }

    @Test
    public void getCommitTypeFeat()
    {
        final Optional<ConventionalCommitType> ctFeat = create("feat: add foo").getCommitType();
        assertTrue(ctFeat.isPresent());
        assertEquals(ConventionalCommitType.FEAT, ctFeat.get());

        final Optional<ConventionalCommitType> ctFeat2 = create("Feat: add foo").getCommitType();
        assertTrue(ctFeat2.isPresent());
        assertEquals(ConventionalCommitType.FEAT, ctFeat2.get());

        final Optional<ConventionalCommitType> ctFeat3 = create("feat(scope): add foo").getCommitType();
        assertTrue(ctFeat3.isPresent());
        assertEquals(ConventionalCommitType.FEAT, ctFeat3.get());
    }

    @Test
    public void getCommitTypeFix()
    {
        final Optional<ConventionalCommitType> ctFix = create("fix: foo").getCommitType();
        assertTrue(ctFix.isPresent());
        assertEquals(ConventionalCommitType.FIX, ctFix.get());

        final Optional<ConventionalCommitType> ctFix2 = create("Fix: foo").getCommitType();
        assertTrue(ctFix2.isPresent());
        assertEquals(ConventionalCommitType.FIX, ctFix2.get());

        final Optional<ConventionalCommitType> ctFix3 = create("fix(scope): foo").getCommitType();
        assertTrue(ctFix3.isPresent());
        assertEquals(ConventionalCommitType.FIX, ctFix3.get());
    }

    static Commit create(final String shortMessage)
    {
        return new Commit(new DummyCommitAdapter(shortMessage));
    }
}
