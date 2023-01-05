package com.smartling.cc4j.semantic.release.common;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

public class Commit
{
    private static final Pattern BREAKING_REGEX = Pattern.compile(
        "^((build|chore|ci|docs|fix|feat|refactor|style|test)[a-z0-9\\(\\)]*)((\\!([\\s]*:(.|\\n)*))|" +
            "([\\s]*:(.|\\n)*(BREAKING(\\s|-)CHANGE)(.|\\n)*))", Pattern.CASE_INSENSITIVE);

    private static final Pattern CONVENTIONAL_COMMIT_REGEX = Pattern.compile(
        "^((build|chore|ci|docs|fix|feat|refactor|style|test)[a-z0-9\\(\\)]*)((\\!([\\s]*:" +
            "(.|\\n)*))|([\\s]*:(.|\\n)*))", Pattern.CASE_INSENSITIVE);
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

        if (CONVENTIONAL_COMMIT_REGEX.matcher(msg).matches())
        {
            for (ConventionalCommitType cc : ConventionalCommitType.values()) {
                if (ConventionalCommitType.BREAKING_CHANGE.equals(cc))
                {
                    continue;
                }

                for (String t : cc.getCommitTypes()) {
                    if (msg.startsWith(t)) {
                        type = cc;
                        break;
                    }
                }
            }
        }

        return Optional.ofNullable(type);
    }

    private String getMessageForComparison()
    {
        String msg = commit.getShortMessage();
        return msg != null ? msg.toLowerCase() : "";
    }
}
