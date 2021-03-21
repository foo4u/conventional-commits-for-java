package com.smartling.cc4j.semantic.release.common.changelog;

import com.smartling.cc4j.semantic.release.common.*;
import com.smartling.cc4j.semantic.release.common.scm.ScmApiException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.util.*;

public class GitChangelogExtractor implements ChangelogExtractor {
    private ConventionalVersioning conventionalVersioning;

    public GitChangelogExtractor(ConventionalVersioning conventionalVersioning) {
        this.conventionalVersioning = conventionalVersioning;
    }

    @Override
    public Map<ConventionalCommitType, Set<Commit>> getGroupedCommitsByCommitTypes() throws ScmApiException {

        LogHandler logHandler = conventionalVersioning.logHandler();

        try {
            Iterable<RevCommit> commits = logHandler.getCommitsSinceLastTag();
            Map<ConventionalCommitType, Set<Commit>> res = new HashMap<>();

            if (commits == null) {
                return res;
            }

            List<Commit> commitList = new ArrayList<>();
            commits.iterator().forEachRemaining(c -> commitList.add(new Commit(new GitCommitAdapter(c))));

            for (Commit c : commitList) {
                if (c.isConventional() && c.getCommitType().isPresent()) {
                    Optional<ConventionalCommitType> commitType = c.getCommitType();
                    res.compute(commitType.get(), (type, commitsForType) -> {
                        if (commitsForType == null) {
                            return new HashSet<>(Collections.singletonList(c));
                        }
                        commitsForType.add(c);
                        return commitsForType;
                    });
                }
            }

            return res;
        } catch (GitAPIException | IOException e) {
            throw new ScmApiException("Git operation failed", e);
        }
    }
}
