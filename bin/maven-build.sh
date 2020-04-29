#!/usr/bin/env bash

set -e

(
cd cyfhir
mvn clean
mvn install -U
mvn -o dependency:tree | grep ":.*:.*:.*" | cut -d] -f2- | sed 's/:[a-z]*$//g' | sed '$d' > dependencies.txt
)
