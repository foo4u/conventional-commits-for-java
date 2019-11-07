package com.smartling.cc4j.semantic.plugin.maven.context;

import org.apache.maven.plugin.MojoFailureException;

public class NoVersionChangeException extends MojoFailureException
{
    public NoVersionChangeException(String message)
    {
        super(message);
    }
}
