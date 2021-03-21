package com.smartling.cc4j.semantic.release.common.changelog;

import com.smartling.cc4j.semantic.release.common.Commit;
import com.smartling.cc4j.semantic.release.common.ConventionalCommitType;
import com.smartling.cc4j.semantic.release.common.scm.ScmApiException;

import java.util.Map;
import java.util.Set;

public interface ChangelogExtractor {
    /**
     * Extracts and groups commits by their commit types
     * @return - commits grouped by commit type
     */
    Map<ConventionalCommitType, Set<Commit>> getGroupedCommitsByCommitTypes() throws ScmApiException;
}
