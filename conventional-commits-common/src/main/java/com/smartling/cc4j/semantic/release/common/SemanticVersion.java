package com.smartling.cc4j.semantic.release.common;

import java.util.Locale;
import java.util.Objects;

public final class SemanticVersion
{
    public static final String VERSION_SUFFIX_SNAPSHOT = "-SNAPSHOT";

    private final Integer major;
    private final Integer minor;
    private final Integer patch;

    public SemanticVersion(final int major, final int minor, final int patch)
    {
        if (major < 0 || minor < 0 || patch < 0)
            throw new IllegalArgumentException("versions can only contain positive integers");

        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        SemanticVersion that = (SemanticVersion) o;
        return major.equals(that.major) && minor.equals(that.minor) && patch.equals(that.patch);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(major, minor, patch);
    }

    public int getMajor()
    {
        return major;
    }

    public int getMinor()
    {
        return minor;
    }

    public int getPatch()
    {
        return patch;
    }

    public SemanticVersion nextVersion(final SemanticVersionChange change)
    {
        final SemanticVersion nextVersion;

        switch (change)
        {
        case MAJOR:
            nextVersion = new SemanticVersion(major + 1, 0, 0);
            break;
        case MINOR:
            nextVersion = new SemanticVersion(major, minor + 1, 0);
            break;
        case PATCH:
        case NONE:
            nextVersion = new SemanticVersion(major, minor, patch);
            break;
        default:
            throw new IllegalStateException("Invalid semantic version change");
        }

        return nextVersion;
    }

    public String toString()
    {
        return toString(major, minor, patch);
    }

    public static SemanticVersion parse(final String version)
    {
        Objects.requireNonNull(version, "version required");
        final String[] parts = version.replace(VERSION_SUFFIX_SNAPSHOT, "").split("\\.");

        if (parts.length != 3)
            throw new IllegalArgumentException("Invalid semantic version: " + version);

        return new SemanticVersion(
            Integer.parseInt(parts[0]),
            Integer.parseInt(parts[1]),
            Integer.parseInt(parts[2])
        );
    }

    private static String toString(final int major, final int minor, final int patch)
    {
        return String.format(Locale.US, "%d.%d.%d", major, minor, patch);
    }

    public String getDevelopmentVersionString()
    {
        return this.toString() + VERSION_SUFFIX_SNAPSHOT;
    }
}
