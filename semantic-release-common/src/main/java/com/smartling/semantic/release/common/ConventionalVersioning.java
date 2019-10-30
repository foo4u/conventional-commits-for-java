package com.smartling.semantic.release.common;

import com.smartling.semantic.release.common.scm.ScmApiException;

import java.io.IOException;

public interface ConventionalVersioning
{
    LogHandler logHandler();

    SemanticVersionChangeResolver semanticVersionChangeResolver();

    SemanticVersion getNextVersion(SemanticVersion currentVersion) throws IOException, ScmApiException;
}
