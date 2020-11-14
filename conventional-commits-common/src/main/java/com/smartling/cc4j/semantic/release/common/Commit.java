package com.smartling.cc4j.semantic.release.common;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Commit
{
    private static final Pattern BREAKING_REGEX = Pattern.compile("^(fix|feat)!.+", Pattern.CASE_INSENSITIVE);
    private static final String  TRACKING_SYSTEM_REGEX_STRING = "^\\s*\\[\\s*(.*)\\s*\\]\\s*";

    private final CommitAdapter commit;

    public Commit(CommitAdapter commit)
    {
        Objects.requireNonNull(commit, "commit may not be null");
        this.commit = commit;
    }

    /**
     * Returns true if this commit is written in conventional commit style;
     * false otherwise.
     */
    public boolean isConventional()
    {
        return this.getCommitType().isPresent();
    }

    public Optional<ConventionalCommitType> getCommitType()
    {
        String msg = this.getMessageForComparison();
        ConventionalCommitType type = null;

        if (msg.startsWith("!"))
        {
            return Optional.empty();
        }

        if (BREAKING_REGEX.matcher(msg).matches())
        {
            return Optional.of(ConventionalCommitType.BREAKING_CHANGE);
        }

        for (ConventionalCommitType cc : ConventionalCommitType.values())
        {
            for (String t : cc.getCommitTypes())
            {
                if (msg.startsWith(t))
                {
                    type = cc;
                    break;
                }
            }
        }

        // FIXME: check for breaking change in footer
/*
        for (FooterLine footerLine : commit.getFooterLines())
        {
            if (footerLine.getKey().equalsIgnoreCase(ConventionalCommitType.BREAKING_CHANGE.getCommitTypes().get(0))) {
                type = ConventionalCommitType.BREAKING_CHANGE;
                break;
            }
        }

 */

        return Optional.ofNullable(type);
    }

    public Optional<String> getCommitMessageDescription() {
        return getCommitMessageFullDescription()
            .map(fullCommitMessage -> fullCommitMessage.replaceFirst(TRACKING_SYSTEM_REGEX_STRING, ""));
     }

    public Optional<String> getTrackingSystemId() {
        return getCommitMessageFullDescription().map(commitMessage -> {
            if("".equals(commitMessage.trim())) {
                return null;
            }

            Matcher matcher = Pattern.compile(TRACKING_SYSTEM_REGEX_STRING + ".*", Pattern.CASE_INSENSITIVE).matcher(commitMessage);
            return matcher.matches() ? matcher.group(1).trim() : null;
        });
    }

    public String getCommitHash() {
        return this.commit.getCommitHash();
    }

    private Optional<String> getCommitMessageFullDescription() {
        String message = getMessageForComparison();
        String[] split = message.split(":");
        if(split.length <= 1) {
            return Optional.empty();
        }

        return Optional.of(split[1].trim());
    }

    private String getMessageForComparison()
    {
        String msg = commit.getShortMessage();
        return msg != null ? msg.toLowerCase() : "";
    }
}
