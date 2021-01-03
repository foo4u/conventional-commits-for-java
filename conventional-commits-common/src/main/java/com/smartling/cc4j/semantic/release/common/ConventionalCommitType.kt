package com.smartling.cc4j.semantic.release.common

import com.smartling.cc4j.semantic.release.common.SemanticVersionChange
import com.smartling.cc4j.semantic.release.common.ConventionalCommitType
import java.util.*

enum class ConventionalCommitType(change: SemanticVersionChange, vararg commitTypes: String) :
    Comparable<ConventionalCommitType> {
    BREAKING_CHANGE(SemanticVersionChange.MAJOR, "breaking change", "!"),
    BUILD(SemanticVersionChange.NONE, "build"),
    CHORE(SemanticVersionChange.MINOR, "chore"),
    CI(SemanticVersionChange.NONE, "ci"),
    DOCS(SemanticVersionChange.PATCH, "docs"),
    FIX(SemanticVersionChange.PATCH, "fix"),
    FEAT(SemanticVersionChange.MINOR, "feat"),
    TEST(SemanticVersionChange.NONE, "test");

    val commitTypes: List<String> = listOf(*commitTypes)
    val changeType: SemanticVersionChange = change
}
