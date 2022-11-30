package com.smartling.cc4j.semantic.release.common.changelog;

import com.smartling.cc4j.semantic.release.common.Commit;
import com.smartling.cc4j.semantic.release.common.ConventionalCommitType;
import com.smartling.cc4j.semantic.release.common.LogHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ChangelogGenerator {
    public static final int COMMIT_HASH_DISPLAYED_LENGTH = 10;
    private final Logger logger = LoggerFactory.getLogger(LogHandler.class);

    private static final String CHANGELOG_FORMAT = "## %s (%s)" +
        "%s";
    private static final String MD_LINK_FORMAT = "[%s](%s)";
    private static final String BUG_FIXES_HEADER = "Bug fixes";
    private static final String FEATURE_HEADER = "Feature";
    private static final String BREAKING_HEADER = "Breaking changes";
    private static final String DOCS_HEADER = "Docs";
    private static final String CI_HEADER = "CI";
    private static final String BUILD_HEADER = "Build";

    private final String repoUrlFormat;
    private final String trackingSystemUrlFormat;

    public ChangelogGenerator(String repoUrlFormat, String trackingSystemUrlFormat) {
        this.repoUrlFormat = repoUrlFormat;
        this.trackingSystemUrlFormat = trackingSystemUrlFormat;
    }

    public String generate(String nextVersion, Map<ConventionalCommitType, Set<Commit>> commitsByCommitType) {
        Objects.requireNonNull(nextVersion, "next version can not be null");
        Objects.requireNonNull(commitsByCommitType, "commits by type can not be null");

        List<String> sections = new ArrayList<>();

        if (commitsByCommitType.get(ConventionalCommitType.BREAKING_CHANGE) != null) {
            getSection(BREAKING_HEADER, commitsByCommitType.get(ConventionalCommitType.BREAKING_CHANGE)).ifPresent(sections::add);
        }

        if (commitsByCommitType.get(ConventionalCommitType.FIX) != null) {
            getSection(BUG_FIXES_HEADER, commitsByCommitType.get(ConventionalCommitType.FIX)).ifPresent(sections::add);
        }

        if (commitsByCommitType.get(ConventionalCommitType.FEAT) != null) {
            getSection(FEATURE_HEADER, commitsByCommitType.get(ConventionalCommitType.FEAT)).ifPresent(sections::add);
        }

        if (commitsByCommitType.get(ConventionalCommitType.DOCS) != null) {
            getSection(DOCS_HEADER, commitsByCommitType.get(ConventionalCommitType.DOCS)).ifPresent(sections::add);
        }

        if (commitsByCommitType.get(ConventionalCommitType.CI) != null) {
            getSection(CI_HEADER, commitsByCommitType.get(ConventionalCommitType.CI)).ifPresent(sections::add);
        }

        if (commitsByCommitType.get(ConventionalCommitType.BUILD) != null) {
            getSection(BUILD_HEADER, commitsByCommitType.get(ConventionalCommitType.BUILD)).ifPresent(sections::add);
        }

        sections = sections.stream().filter(Objects::nonNull).collect(Collectors.toList());

        return String.format(CHANGELOG_FORMAT, nextVersion, LocalDate.now(), "\n" + String.join("\n", sections));
    }

    private Optional<String> getSection(String header, Set<Commit> commits) {
        String sectionEntries = getSectionEntries(commits);
        if (sectionEntries != null && !sectionEntries.trim().equals("")) {
            return Optional.of("###" + header + "\n" + sectionEntries);
        }

        return Optional.empty();
    }

    private String getSectionEntries(Set<Commit> commits) {
        Set<String> uniqueMessages = new HashSet<>();
        return commits.stream()
            .filter(commit -> commit.getCommitMessageDescription().isPresent() && uniqueMessages.add(commit.getCommitMessageDescription().get()))
            .map(this::getChangeLogEntry)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .sorted()
            .collect(Collectors.joining("\n"));
    }

    private Optional<String> getChangeLogEntry(Commit commit) {
        if (!commit.getCommitMessageDescription().isPresent()) {
            logger.warn("Skipping message for commit: {}", commit.getCommitHash());
        }
        return commit.getCommitMessageDescription().map(message -> {
            if (commit.getCommitMessageDescription().get().trim().equals("")) {
                logger.warn("Skipping message for commit: {}", commit.getCommitHash());
                return null;
            }
            return "* " + commit.getCommitMessageDescription().get() + getTrackingSystemLink(commit) + getCommitHashLink(commit);
        });
    }

    private String getCommitHashLink(Commit commit) {
        if (this.repoUrlFormat == null) {
            return " (" + commit.getCommitHash().substring(0, COMMIT_HASH_DISPLAYED_LENGTH) + ")";
        } else {
            return " " + String.format(MD_LINK_FORMAT, "(" + commit.getCommitHash().substring(0, COMMIT_HASH_DISPLAYED_LENGTH) + ")", String.format(this.repoUrlFormat, commit.getCommitHash()));
        }
    }

    private String getTrackingSystemLink(Commit commit) {
        if (this.trackingSystemUrlFormat == null || !commit.getTrackingSystemId().isPresent()) {
            return commit.getTrackingSystemId().map(s -> " (" + s + ")").orElse("");
        } else {
            return " " + String.format(MD_LINK_FORMAT, "(" + commit.getTrackingSystemId().get() + ")", String.format(this.trackingSystemUrlFormat, commit.getTrackingSystemId().get()));
        }
    }
}
