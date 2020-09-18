#!/usr/bin/env bash

set -e

(
  cd cyfhir/plugins

  APOC_VERSION=4.1.0.0
  APOC_URL=https://github.com/neo4j-contrib/neo4j-apoc-procedures/releases/download/${APOC_VERSION}/apoc-${APOC_VERSION}-all.jar
  wget -N $APOC_URL

  GDS_VERSION=1.3.1
  GDS_URL=https://github.com/neo4j/graph-data-science/releases/download/${GDS_VERSION}/neo4j-graph-data-science-${GDS_VERSION}-standalone.jar
  wget -N $GDS_URL
)
