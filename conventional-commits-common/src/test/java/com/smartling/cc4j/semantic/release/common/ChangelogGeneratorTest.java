package com.smartling.cc4j.semantic.release.common;

import com.smartling.cc4j.semantic.release.common.changelog.ChangelogGenerator;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class ChangelogGeneratorTest {
    private static final String COMMIT_HASH = "2717635691";
    private static final String TRACKING_SYSTEM_URL_FORMAT = "http://test.com?id=%s";
    private static final String REPO_URL_FORMAT = "http://repo.com?id=%s";

    private ChangelogGenerator changelogGenerator;

    private static final String EXPECTED_CHANGELOG =
        "###Breaking changes\n" +
        "* breaking change test [(2717635691)](http://repo.com?id=2717635691)\n" +
        "###Bug fixes\n" +
        "* fix test 2 [(2717635691)](http://repo.com?id=2717635691)\n" +
        "* fix test [(2717635691)](http://repo.com?id=2717635691)\n" +
        "* fix ui test [(ticket-id)](http://test.com?id=ticket-id) [(2717635691)](http://repo.com?id=2717635691)\n" +
        "###Feature\n" +
        "* fix test [(2717635691)](http://repo.com?id=2717635691)\n" +
        "###Docs\n" +
        "* docs test [(2717635691)](http://repo.com?id=2717635691)\n" +
        "###CI\n" +
        "* ci test [(2717635691)](http://repo.com?id=2717635691)\n" +
        "###Build\n" +
        "* build test [(2717635691)](http://repo.com?id=2717635691)";

    private static final String EXPECTED_CHANGELOG_NO_URLS = "###Breaking changes\n" +
        "* breaking change test (2717635691)\n" +
        "###Bug fixes\n" +
        "* fix test (2717635691)\n" +
        "* fix test 2 (2717635691)\n" +
        "* fix ui test (ticket-id) (2717635691)\n" +
        "###Feature\n" +
        "* fix test (2717635691)\n" +
        "###Docs\n" +
        "* docs test (2717635691)\n" +
        "###CI\n" +
        "* ci test (2717635691)\n" +
        "###Build\n" +
        "* build test (2717635691)";

    @Before
    public void setUp() {
        changelogGenerator = new ChangelogGenerator(REPO_URL_FORMAT, TRACKING_SYSTEM_URL_FORMAT);
    }

    @Test
    public void testOnlyHeaderGeneratedOnEmptyChanges() {
        Map<ConventionalCommitType, Set<Commit>> commits = new HashMap<>();
        assertEquals(getExpectedChangelogHeader("0.0.1"), changelogGenerator.generate("0.0.1", commits));

        commits.put(ConventionalCommitType.FEAT, new HashSet<>(Collections.singletonList(
            new Commit(
                new DummyCommitAdapter("ci this message will not be included to changelog as there is no colon", COMMIT_HASH)))));
        assertEquals(getExpectedChangelogHeader("0.0.1"), changelogGenerator.generate("0.0.1", commits));
    }

    @Test(expected = NullPointerException.class)
    public void testVersionIsMandatory() {
        changelogGenerator.generate(null, Collections.emptyMap());
    }

    @Test
    public void testChangelogGenerated() {
        Map<ConventionalCommitType, Set<Commit>> commitsByCommitType = getCommitsByCommitType();
        String changelog = changelogGenerator.generate("0.2.7", commitsByCommitType);
        assertEquals(EXPECTED_CHANGELOG, changelog.substring(changelog.indexOf("\n") + 1));
    }

    @Test
    public void testChangelogGeneratedNoUrlFormatsProvided() {
        Map<ConventionalCommitType, Set<Commit>> commitsByCommitType = getCommitsByCommitType();
        String changelog = new ChangelogGenerator(null, null).generate("0.2.7", commitsByCommitType);
        assertEquals(EXPECTED_CHANGELOG_NO_URLS, changelog.substring(changelog.indexOf("\n") + 1));
    }

    private Map<ConventionalCommitType, Set<Commit>> getCommitsByCommitType() {
        Map<ConventionalCommitType, Set<Commit>> res = new HashMap<>();
        res.put(ConventionalCommitType.BREAKING_CHANGE,
            new HashSet<>(Collections.singletonList(new Commit(new DummyCommitAdapter("breaking change: breaking change test", COMMIT_HASH)))));
        res.put(ConventionalCommitType.FEAT,
            new HashSet<>(Collections.singletonList(new Commit(new DummyCommitAdapter("feat(ui): fix test", COMMIT_HASH)))));
        res.put(ConventionalCommitType.FIX,
            new HashSet<>(Arrays.asList(new Commit(new DummyCommitAdapter("fix(ui): [TICKET-ID] fix ui test", COMMIT_HASH)),
                new Commit(new DummyCommitAdapter("fix(ui): fix test", COMMIT_HASH)),
                new Commit(new DummyCommitAdapter("fix(ui): fix test 2", COMMIT_HASH)))));
        res.put(ConventionalCommitType.CI,
            new HashSet<>(Arrays.asList(new Commit(new DummyCommitAdapter("ci: ci test", COMMIT_HASH)),
                new Commit(new DummyCommitAdapter("ci this message will not me included to changelog as there is no colon", COMMIT_HASH)))));
        res.put(ConventionalCommitType.BUILD,
            new HashSet<>(Collections.singletonList(new Commit(new DummyCommitAdapter("build: build test", COMMIT_HASH)))));
        res.put(ConventionalCommitType.DOCS,
            new HashSet<>(Collections.singletonList(new Commit(new DummyCommitAdapter("docs: docs test", COMMIT_HASH)))));
        return res;
    }

    private String getExpectedChangelogHeader(String version) {
        return "## " + version + " (" + LocalDate.now() + ")\n";
    }
}
