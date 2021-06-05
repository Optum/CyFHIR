#!/usr/bin/env bash

set -e

(
  cd cyfhir/plugins

  APOC_VERSION=4.2.0.4
  APOC_URL=https://github.com/neo4j-contrib/neo4j-apoc-procedures/releases/download/${APOC_VERSION}/apoc-${APOC_VERSION}-all.jar
  wget -N $APOC_URL

  GDS_VERSION=1.6.0
  GDS_URL=https://github.com/neo4j/graph-data-science/releases/download/${GDS_VERSION}/neo4j-graph-data-science-${GDS_VERSION}.jar
  wget -N $GDS_URL
)
