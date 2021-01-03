package com.smartling.cc4j.semantic.release.common

import java.util.*

class SemanticVersion(major: Int, minor: Int, patch: Int) {
    private val major: Int
    private val minor: Int
    private val patch: Int

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as SemanticVersion
        return major == that.major && minor == that.minor && patch == that.patch
    }

    override fun hashCode(): Int {
        return Objects.hash(major, minor, patch)
    }

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
            Objects.requireNonNull(version, "version required")
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
        this.major = major
        this.minor = minor
        this.patch = patch
    }
}
