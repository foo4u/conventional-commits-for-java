#!/usr/bin/env bash

set -e

echo "Checking if we should release"
if ! ./mvnw -Prelease conventional-commits:validate; then
    echo "No changes to release"
    exit 0
fi

./mvnw -e -X -B -Prelease -DskipTests -Darguments='-DskipTests' -s .circleci/settings.xml conventional-commits:version release:prepare
