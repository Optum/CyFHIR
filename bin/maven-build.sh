#!/usr/bin/env bash

set -e

(
cd cyfhir
mvn clean
mvn install -U
mvn -o dependency:tree | grep ":.*:.*:.*" | cut -d] -f2- | sed 's/:[a-z]*$//g' | sed -n '1!p' | sed '$d' > dependencies.txt
(
cd target
mv original-cyfhir-WIP.jar ../plugins/CyFHIR.jar
rm cyfhir-WIP.jar
)
)
