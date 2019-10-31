# Convention Commits for Java

Provides a Java implementation of [Conventional Commits] for projects built
with Java 1.8+.

## Maven Plugin

### Usage

This plugin works together with the [Maven Release Plugin] to create 
conventional commit compliant releases for your Maven projects

#### Install the Plugin

In your main `pom.xml` file add the plugin:

    <plugins>
        <plugin>
            <groupId>com.smartling.conventional.plugin.maven</groupId>
            <artifactId>semantic-maven-plugin</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </plugin>
    </plugins>
    
#### Release a Version

    mvn semantic:version release:prepare
    mvn release:perform

## Gradle Plugin

A [Gradle] plugin is planned for future release.











[Conventional Commits]: https://www.conventionalcommits.org/en/v1.0.0/
