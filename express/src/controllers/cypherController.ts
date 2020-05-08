// Cypher that loads a FHIR Bundle
function loadBundleCypher (_bundle: string): string {
  const _bundleFormatted = _bundle.replace(/"/g, '\\"');
  const cypher = `CALL cyfhir.loadBundle("${_bundleFormatted}")`;
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
  const cypher = `WITH "${_id}" as _id
                    MATCH (m:entry)-[*]->(n:resource)
                    MATCH (q:resource)-[*2]->()-[r:reference]->(o:entry)
                    WHERE (n.id = _id) AND (o._resourceId = _id)
                    WITH collect(m)+collect(o) AS entryList
                    UNWIND entryList AS entry
                    CALL apoc.path.expand(entry, ">|relationship", "-entry", 0, 999) YIELD path
                    WITH collect(path) AS paths
                    CALL apoc.convert.toTree(paths) YIELD value
                    RETURN cyfhir.buildBundle(COLLECT(value))`;
  return cypher;
}

// Cypher that builds a FHIR Bundle based off of a ResourceId, every resource that at max depth
// points to that resource is included in the Bundle IF it matches the ResourceType filter
function buildBundleAroundIDWithFilterCypher (_id: string, _filter: string): string {
  const cypher = `WITH "${_id}" AS _id
                    MATCH (m:entry)-[*]->(n:resource)
                    MATCH (q:resource)-[*2]->()-[r:reference]->(o:entry)
                    WHERE (n.id = _id) AND (o._resourceId = _id)
                    WITH collect(m)+collect(o) AS entryList, ${_filter} AS filter
                    MATCH (m:entry)-[*1]->(n:resource)
                    WHERE (m in entryList)
                    AND (n.resourceType in filter)
                    WITH collect(m) AS entryList
                    UNWIND entryList AS entry
                    CALL apoc.path.expand(entry, ">|relationship", "-entry", 0, 999) YIELD path
                    WITH collect(path) AS paths
                    CALL apoc.convert.toTree(paths) YIELD value
                    RETURN cyfhir.buildBundle(COLLECT(value))`;
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
