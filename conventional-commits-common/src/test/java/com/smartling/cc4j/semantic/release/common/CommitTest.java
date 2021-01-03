package com.smartling.cc4j.semantic.release.common

import com.smartling.cc4j.semantic.release.common.Commit.isConventional
import com.smartling.cc4j.semantic.release.common.Commit.commitType
import com.smartling.cc4j.semantic.release.common.CommitTest
import com.smartling.cc4j.semantic.release.common.ConventionalCommitType
import com.smartling.cc4j.semantic.release.common.DummyCommitAdapter
import org.junit.Assert

class CommitTest {
    @get:Test
    val isConventional: Unit
        get() {
            Assert.assertTrue(create("fix!: new version").isConventional)
            Assert.assertTrue(create("fix: fix foo").isConventional)
            Assert.assertTrue(create("feat!: add foo").isConventional)
            Assert.assertTrue(create("feat: add foo").isConventional)
            Assert.assertFalse(create("Add foo").isConventional)
        }

    @get:Test
    val commitTypeBreakingChange: Unit
        get() {
            Assert.assertEquals(
                ConventionalCommitType.BREAKING_CHANGE,
                create("breaking change: new version").commitType.get()
            )
            Assert.assertEquals(
                ConventionalCommitType.BREAKING_CHANGE,
                create("Breaking Change: add foo").commitType.get()
            )
            Assert.assertEquals(
                ConventionalCommitType.BREAKING_CHANGE,
                create("BREAKING CHANGE(scope): add foo").commitType.get()
            )
        }

    @get:Test
    val commitTypeBreakingChangeExclamation: Unit
        get() {
            Assert.assertEquals(ConventionalCommitType.BREAKING_CHANGE, create("fix!: new version").commitType.get())
            Assert.assertEquals(ConventionalCommitType.BREAKING_CHANGE, create("feat!: new version").commitType.get())
            Assert.assertEquals(ConventionalCommitType.TEST, create("test!: new version").commitType.get())
        }

    @get:Test
    val commitTypeFeat: Unit
        get() {
            Assert.assertEquals(ConventionalCommitType.FEAT, create("feat: add foo").commitType.get())
            Assert.assertEquals(ConventionalCommitType.FEAT, create("Feat: add foo").commitType.get())
            Assert.assertEquals(ConventionalCommitType.FEAT, create("feat(scope): add foo").commitType.get())
        }

    @get:Test
    val commitTypeFix: Unit
        get() {
            Assert.assertEquals(ConventionalCommitType.FIX, create("fix: foo").commitType.get())
            Assert.assertEquals(ConventionalCommitType.FIX, create("Fix: foo").commitType.get())
            Assert.assertEquals(ConventionalCommitType.FIX, create("fix(scope): foo").commitType.get())
        }

    companion object {
        fun create(shortMessage: String?): Commit {
            return Commit(DummyCommitAdapter(shortMessage))
        }
    }
}
