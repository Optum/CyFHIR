FROM neo4j:4.0.3

# APOC Plugin for Neo4j
ENV APOC_VERSION 4.0.0.9
ENV APOC_URL https://github.com/neo4j-contrib/neo4j-apoc-procedures/releases/download/${APOC_VERSION}/apoc-${APOC_VERSION}-all.jar
RUN (cd $NEO4J_HOME/plugins && wget --quiet $APOC_URL)

# CyFHIR Plugin for Neo4j
ENV CyFHIR_URL ./cyfhir/target/CyFHIR.jar
COPY $CyFHIR_URL $NEO4J_HOME/plugins/

# Ports for neo4j: http, https, and bolt
EXPOSE 7474 7473 7687

USER neo4j

ENTRYPOINT ["/sbin/tini", "-g", "--", "/docker-entrypoint.sh"]
CMD ["neo4j"]
