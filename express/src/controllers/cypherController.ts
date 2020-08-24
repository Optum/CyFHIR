// Cypher that loads a FHIR Bundle
function loadBundleCypher(_bundle: string): string {
  const _bundleFormatted = _bundle.replace(/"/g, '\\"');
  const cypher = `CALL cyfhir.bundle.load("${_bundleFormatted}")`;
  return cypher;
}

// Cypher that deletes all Nodes and Relationships in Neo4J DB
function deleteAllNodesCypher(): string {
  const cypher = 'MATCH (n) DETACH DELETE n';
  return cypher;
}

// Cypher that builds a FHIR Bundle based off of a ResourceId, every resource that at max depth
// points to that resource is included in the Bundle
function buildBundleAroundIDCypher(_id: string): string {
  const cypher = `WITH "${_id}" AS _id
                  MATCH (_entry:entry {_resourceId: _id})
                  WITH [_entry] + [(a:entry)-[*1..3]->(_entry) | a] + [(_entry)-[*1..3]->(a:entry) | a] as entries
                  WITH DISTINCT(entries) AS entries
                  UNWIND entries AS entry
                  CALL cyfhir.resource.expand(entry) YIELD path
                  RETURN cyfhir.bundle.format(collect(path))`;
  return cypher;
}

// Cypher that builds a FHIR Bundle based off of a ResourceId, every resource that at max depth
// points to that resource is included in the Bundle IF it matches the ResourceType filter
function buildBundleAroundIDWithFilterCypher(_id: string, _filter: string): string {
  const cypher = `WITH "${_id}" AS _id
                  MATCH (_entry:entry {_resourceId: _id})
                  WITH [_entry] + [(a:entry)-[*1..3]->(_entry) | a] + [(_entry)-[*1..3]->(a:entry) | a] as entries
                  WITH DISTINCT(entries) AS entries, ${_filter} AS filter
                  MATCH (m:entry)-[*1]->(n:resource)
                  WHERE (m in entries)
                  AND (n.resourceType in filter)
                  WITH collect(m) AS entries
                  UNWIND entries AS entry
                  CALL cyfhir.resource.expand(entry) YIELD path
                  RETURN cyfhir.bundle.format(collect(path))`;
  return cypher;
}

// Cypher that loads a FHIR Resource
function loadResourceCypher(_resource: string): string {
  const _resourceFormatted = _resource.replace(/"/g, '\\"');
  const cypher = `CALL cyfhir.resource.load("${_resourceFormatted}")`;
  return cypher;
}

// Cypher that builds a FHIR Resource based off of a ResourceId
function getResourceCypher(_id: string): string {
  const cypher = `WITH "${_id}" AS _id
                  MATCH (r:resource {id: _id})
                  CALL cyfhir.resource.expand(r) YIELD path
                  WITH cyfhir.resource.format(collect(path)) AS resource
                  RETURN resource`;
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
  },
  loadResource: (_resource) => {
    return loadResourceCypher(JSON.stringify(_resource));
  },
  getResource: (_id: string) => {
    return getResourceCypher(_id);
  },
};
