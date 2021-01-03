package com.smartling.cc4j.semantic.release.common

import kotlin.Throws
import com.smartling.cc4j.semantic.release.common.scm.ScmApiException
import java.io.IOException

interface ConventionalVersioning {
    fun logHandler(): LogHandler?

    @Throws(ScmApiException::class, IOException::class)
    fun getNextVersionChangeType(): SemanticVersionChange?

    @Throws(IOException::class, ScmApiException::class)
    fun getNextVersion(currentVersion: SemanticVersion): SemanticVersion
}
