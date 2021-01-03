package com.smartling.cc4j.semantic.release.common

import java.util.*

data class SemanticVersion(private val major: Int, private val minor: Int, private val patch: Int) {

    fun nextVersion(change: SemanticVersionChange): SemanticVersion {
        return when (change) {
            SemanticVersionChange.MAJOR -> SemanticVersion(major + 1, 0, 0)
            SemanticVersionChange.MINOR -> SemanticVersion(major, minor + 1, 0)
            SemanticVersionChange.PATCH -> SemanticVersion(major, minor, patch + 1)
            SemanticVersionChange.NONE -> SemanticVersion(major, minor, patch)
        }
    }

    override fun toString(): String {
        return toString(major, minor, patch)
    }

    companion object {
        @JvmStatic
        fun parse(version: String): SemanticVersion {
            val parts = version.split("\\.".toRegex()).toTypedArray()
            require(parts.size == 3) { "Invalid semantic version: $version" }
            return SemanticVersion(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
        }

        private fun toString(major: Int, minor: Int, patch: Int): String {
            return String.format(Locale.US, "%d.%d.%d", major, minor, patch)
        }
    }

    init {
        require(major >= 0 && minor >= 0 && patch >= 0) { "versions can only contain positive integers" }
    }
}
