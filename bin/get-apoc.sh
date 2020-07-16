#!/usr/bin/env bash

set -e

(
cd cyfhir/plugins
APOC_VERSION=4.1.0.0
APOC_URL=https://github.com/neo4j-contrib/neo4j-apoc-procedures/releases/download/${APOC_VERSION}/apoc-${APOC_VERSION}-all.jar
wget -N $APOC_URL
)
