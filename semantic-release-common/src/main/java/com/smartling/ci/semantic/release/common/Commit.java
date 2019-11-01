package com.smartling.ci.semantic.release.common;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

public class Commit
{
    private static final Pattern BREAKING_REGEX = Pattern.compile("^(fix|feat)!.+", Pattern.CASE_INSENSITIVE);

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

    private String getMessageForComparison()
    {
        String msg = commit.getShortMessage();
        return msg != null ? msg.toLowerCase() : "";
    }
}
