// Cypher that loads a FHIR Bundle
function loadBundleCypher (_bundle: string): string {
  const _bundleFormatted = _bundle.replace(/"/g, '\\"');
  const cypher = `CALL cyfhir.bundle.load("${_bundleFormatted}")`;
  return cypher;
}

// Cypher that deletes all Nodes and Relationships in Neo4J DB
function deleteAllNodesCypher (): string {
  const cypher = 'MATCH (n) DETACH DELETE n';
  return cypher;
}

// Cypher that builds a FHIR Bundle based off of a ResourceId, every resource that at max depth
// points to that resource is included in the Bundle
function buildBundleAroundIDCypher (_id: string): string {
  const cypher = `CALL {
                    WITH "${_id}" as _id
                    MATCH path=(a:entry)-[*1..3]->()-[r:reference]->(b:entry)
                    WHERE (b._resourceId = _id)
                    RETURN a,b
                    UNION
                    WITH "${_id}" as _id
                    MATCH path=(a:entry)-[*1..3]->()-[r:reference]->(b:entry)
                    WHERE (a._resourceId = _id)
                    RETURN a,b
                  }
                  WITH collect(a)+collect(b) AS entries
                  UNWIND entries AS entry
                  CALL cyfhir.resource.expand(entry) YIELD path
                  RETURN cyfhir.bundle.build(collect(path))`;
  return cypher;
}

// Cypher that builds a FHIR Bundle based off of a ResourceId, every resource that at max depth
// points to that resource is included in the Bundle IF it matches the ResourceType filter
function buildBundleAroundIDWithFilterCypher (_id: string, _filter: string): string {
  const cypher = `CALL {
                    WITH "${_id}" as _id
                    MATCH path=(a:entry)-[*1..3]->()-[r:reference]->(b:entry)
                    WHERE (b._resourceId = _id)
                    RETURN a,b
                    UNION
                    WITH "${_id}" as _id
                    MATCH path=(a:entry)-[*1..3]->()-[r:reference]->(b:entry)
                    WHERE (a._resourceId = _id)
                    RETURN a,b
                  }
                  WITH collect(a)+collect(b) AS entryList, ${_filter} AS filter
                  MATCH (m:entry)-[*1]->(n:resource)
                  WHERE (m in entryList)
                  AND (n.resourceType in filter)
                  WITH collect(m) AS entries
                  UNWIND entries AS entry
                  CALL cyfhir.resource.expand(entry) YIELD path
                  RETURN cyfhir.bundle.build(collect(path))`;
  return cypher;
}

export = {
  loadBundle: (_bundle) => {
    return loadBundleCypher(JSON.stringify(_bundle));
  },
  deleteAll: () => {
    return deleteAllNodesCypher();
  },
  buildBundleAroundID: (_id: string) => {
    return buildBundleAroundIDCypher(_id);
  },
  buildBundleAroundIDWithFilter: (_id: string, _filter: string) => {
    return buildBundleAroundIDWithFilterCypher(_id, _filter);
  }
};
