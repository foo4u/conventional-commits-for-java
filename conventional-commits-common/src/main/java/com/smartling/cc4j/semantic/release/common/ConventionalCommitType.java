package com.smartling.cc4j.semantic.release.common;

import java.util.Arrays;
import java.util.List;

public enum ConventionalCommitType implements Comparable<ConventionalCommitType>
{
    BREAKING_CHANGE(SemanticVersionChange.MAJOR, "breaking change", "!"),
    BUILD(SemanticVersionChange.NONE, "build"),
    CHORE(SemanticVersionChange.MINOR, "chore"),
    CI(SemanticVersionChange.NONE, "ci"),
    DOCS(SemanticVersionChange.PATCH, "docs"),
    FIX(SemanticVersionChange.PATCH, "fix"),
    FEAT(SemanticVersionChange.MINOR, "feat"),
    TEST(SemanticVersionChange.NONE, "test");

    private final List<String> commitTypes;
    private final SemanticVersionChange changeType;

    ConventionalCommitType(SemanticVersionChange change, String... commitTypes)
    {
        this.commitTypes = Arrays.asList(commitTypes);
        this.changeType = change;
    }

    public SemanticVersionChange getChangeType()
    {
        return changeType;
    }

    public List<String> getCommitTypes()
    {
        return commitTypes;
    }
}
