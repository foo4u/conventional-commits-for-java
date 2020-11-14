# The fork of Conventional Commits for Java

Provides a Java implementation of [Conventional Commits] for projects built
with Java 1.8+ using Git for version control.
The fork include additional goal that enables to generate changelog files automatically
## Maven Plugin

### Usage

This plugin works together with the [Maven Release Plugin] to create
conventional commit compliant releases for your Maven projects

#### Install the Plugin

In your main `pom.xml` file add the plugin:

    <plugins>
        <plugin>
            <groupId>com.smartling.cc4j</groupId>
            <artifactId>conventional-commits-maven-plugin</artifactId>
            <version>${version}</version>
        </plugin>
    </plugins>

#### Release a Version

    mvn conventional-commits:version release:prepare
    mvn release:perform

#### Generate change logs
     mvn conventional-commits:version conventional-commits:changelog release:prepare
     mvn release:perform
Note: changelog goal performs a commit that includes updated CHANGELOG.MD
this commit will not be rolled back on release:clean - this is because of well known
maven limitation - release plugin does not allow to commit additional files on release:prepare
stage

## Gradle Plugin

A [Gradle] plugin is planned for future release.











[Conventional Commits]: https://www.conventionalcommits.org/en/v1.0.0/
[Maven Release Plugin]: https://maven.apache.org/maven-release/maven-release-plugin/
