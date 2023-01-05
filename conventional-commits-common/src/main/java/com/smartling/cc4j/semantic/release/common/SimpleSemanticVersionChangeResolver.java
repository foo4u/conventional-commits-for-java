package com.smartling.cc4j.semantic.release.common;

import org.eclipse.jgit.revwalk.RevCommit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SimpleSemanticVersionChangeResolver implements SemanticVersionChangeResolver
{
    @Override
    public SemanticVersionChange resolveChange(final Iterable<RevCommit> commits)
    {
        Objects.requireNonNull(commits, "commits may not be null");
        final List<Commit> commitList = new ArrayList<>();
        commits.iterator().forEachRemaining(c -> commitList.add(new Commit(new GitCommitAdapter(c))));
        SemanticVersionChange change = SemanticVersionChange.NONE;

        for (final Commit c : commitList)
        {
            final Optional<ConventionalCommitType> commitType = c.getCommitType();
            if (commitType.isPresent())
            {
                if (SemanticVersionChange.MAJOR.equals(commitType.get().getChangeType()))
                {
                    return SemanticVersionChange.MAJOR;
                }
                else if (SemanticVersionChange.MINOR.equals(commitType.get().getChangeType()))
                {
                    change = SemanticVersionChange.MINOR;
                }
                else if (SemanticVersionChange.PATCH.equals(commitType.get().getChangeType()))
                {
                    if (!SemanticVersionChange.MINOR.equals(change))
                    {
                        change = SemanticVersionChange.PATCH;
                    }
                }
            }
        }

        return change;
    }
}
