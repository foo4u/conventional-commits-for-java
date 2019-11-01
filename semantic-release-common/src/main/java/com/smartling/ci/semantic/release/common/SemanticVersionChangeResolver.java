package com.smartling.ci.semantic.release.common;

import org.eclipse.jgit.revwalk.RevCommit;

public interface SemanticVersionChangeResolver
{
    SemanticVersionChange resolveChange(Iterable<RevCommit> commits);
}
