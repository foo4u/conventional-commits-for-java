package com.smartling.cc4j.semantic.release.common;

import org.junit.Test;

import static org.junit.Assert.*;

public class SemanticVersionTest
{
    @Test
    public void nextVersionMajor()
    {
        SemanticVersion next = SemanticVersion.parse("9.10.11").nextVersion(SemanticVersionChange.MAJOR);
        assertEquals(new SemanticVersion(10, 0, 0), next);
    }

    @Test
    public void nextVersionMinor()
    {
        SemanticVersion next = SemanticVersion.parse("9.10.11").nextVersion(SemanticVersionChange.MINOR);
        assertEquals(new SemanticVersion(9, 11, 0), next);
    }

    @Test
    public void nextVersionPatch()
    {
        SemanticVersion next = SemanticVersion.parse("9.10.11-SNAPSHOT").nextVersion(SemanticVersionChange.PATCH);
        assertEquals(new SemanticVersion(9, 10, 11), next);
    }

    @Test
    public void nextVersionNoChange()
    {
        SemanticVersion next = SemanticVersion.parse("9.10.11").nextVersion(SemanticVersionChange.NONE);
        assertEquals(new SemanticVersion(9, 10, 11), next);
    }

    @Test
    public void testToString()
    {
        assertEquals("1.2.3", new SemanticVersion(1,2, 3).toString());
    }

    @Test
    public void parseValid()
    {
        assertEquals(new SemanticVersion(1, 2, 3), SemanticVersion.parse("1.2.3"));
    }

    @Test(expected = NullPointerException.class)
    public void parseNull()
    {
        SemanticVersion.parse(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseNonSemVer()
    {
        SemanticVersion.parse("1.2.3.4");
    }

    @Test(expected = NumberFormatException.class)
    public void parseNonNumeric()
    {
        SemanticVersion.parse("1.2.0FOO");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseNegative()
    {
        SemanticVersion.parse("-1.0.0");
    }
}
