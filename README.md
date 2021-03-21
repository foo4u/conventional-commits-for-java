# Conventional Commits for Java

Provides a Java implementation of [Conventional Commits] for projects built
with Java 1.8+ using Git for version control.

## Maven Plugin

### Usage

This plugin works together with the [Maven Release Plugin] to create
a conventional commit compliant releases for your Maven projects

#### Install the Plugin

In your main `pom.xml` file add the plugin:

    <plugins>
        <plugin>
            <groupId>com.smartling.cc4j</groupId>
            <artifactId>conventional-commits-maven-plugin</artifactId>
            <version>${version}</version>
        </plugin>
    </plugins>

You can provide the link to you tracking system as parameter in configuration. In generated change log there will be
 the link to the ticket.

    <trackingSystemUrlFormat>http://example.com/%s</trackingSystemUrlFormat>

`%s` - will be replaced by ticket id provided at the begging of message in square brackets.
For example:

`fix: [ticket-id] message`

Also, you can provide the pattern for repository URL. In the generated change log
there will be a commit hash with URL to the commit in the remote repository.

    <repoUrlFormat>http://example.com/%s</repoUrlFormat>

#### Release a Version

    mvn conventional-commits:version release:prepare
    mvn release:perform

#### With generated change logs

     mvn conventional-commits:version conventional-commits:changelog release:prepare
     mvn release:perform

#### Changelog example

##### Commit messages:
breaking change: [ticket-23] change public API

ci: add build step

##### Generated change log (CHANGELOG.MD):
## 1.0.0 (2020-11-14)
### Breaking changes
* change public API [(ticket-23)](http://example.com/ticket-23) [(23b1e004c4)](http://example.com/23b1e004c45b56b633f09656a05875a5a5ff7e86)
### CI
* add build step

**Note**: changelog goal performs a commit that includes updated CHANGELOG.MD
this commit will not be rolled back on release:clean - this is because of well-known
maven limitation - release plugin does not allow committing additional files on release:prepare
stage

## Gradle Plugin

A [Gradle] plugin is planned for future release.











[Conventional Commits]: https://www.conventionalcommits.org/en/v1.0.0/
[Maven Release Plugin]: https://maven.apache.org/maven-release/maven-release-plugin/
