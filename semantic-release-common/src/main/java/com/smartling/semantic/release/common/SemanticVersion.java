package com.smartling.semantic.release.common;

import java.util.Locale;
import java.util.Objects;

public final class SemanticVersion
{
    private final Integer major;
    private final Integer minor;
    private final Integer patch;

    public SemanticVersion(int major, int minor, int patch)
    {
        if (major < 0 || minor < 0 || patch < 0)
            throw new IllegalArgumentException("versions can only contain positive integers");

        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    @Override
    public boolean equals(Object o)
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

    public SemanticVersion nextVersion(SemanticVersionChange change)
    {
        SemanticVersion nextVersion;

        switch (change)
        {
        case MAJOR:
            nextVersion = new SemanticVersion(major + 1, 0, 0);
            break;
        case MINOR:
            nextVersion = new SemanticVersion(major, minor + 1, 0);
            break;
        case PATCH:
            nextVersion = new SemanticVersion(major, minor, patch + 1);
            break;
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

    public static SemanticVersion parse(String version)
    {
        Objects.requireNonNull(version, "version required");
        String[] parts = version.split("\\.");

        if (parts.length != 3)
            throw new IllegalArgumentException("Invalid semantic version: " + version);

        return new SemanticVersion(
            Integer.parseInt(parts[0]),
            Integer.parseInt(parts[1]),
            Integer.parseInt(parts[2])
        );
    }

    private static String toString(int major, int minor, int patch)
    {
        return String.format(Locale.US, "%d.%d.%d", major, minor, patch);
    }
}
