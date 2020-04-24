function loadBundleCypher (_bundle) {
  const _bundleFormatted = _bundle.replace(/"/g, '\\"');
  const cypher = `CALL cyfhir.loadBundle("${_bundleFormatted}")`;
  return cypher;
}

function deleteAllNodesCypher () {
  const cypher = 'MATCH (n) DETACH DELETE n';
  return cypher;
}

function buildBundleAroundIDCypher (_id) {
  const cypher = `MATCH (m:entry)-[*]->(n:resource)
                  OPTIONAL MATCH (n:resource)-[*2]->()-[r:reference]->(o:entry)
                  WHERE (n.id = "${_id}")
                  WITH collect(m)+collect(o) AS entryList
                  UNWIND entryList AS entry
                  CALL apoc.path.expand(entry, ">|relationship", "-entry", 0, 999) YIELD path
                  WITH collect(path) AS paths
                  CALL apoc.convert.toTree(paths) YIELD value
                  RETURN cyfhir.buildBundle(COLLECT(value))`;
  return cypher;
}

function buildBundleAroundIDWithFilterCypher (_id, _filter) {
  const cypher = `WITH "${_id}" AS _id
                  MATCH (m:entry)-[*]->(n:resource)
                  OPTIONAL MATCH (n:resource)-[*2]->()-[r:reference]->(o:entry)
                  WHERE (n.id = _id)
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

module.exports = {
  loadBundle: (_bundle) => {
    return loadBundleCypher(JSON.stringify(_bundle));
  },
  deleteAll: () => {
    return deleteAllNodesCypher();
  },
  buildBundleAroundID: (_id) => {
    return buildBundleAroundIDCypher(_id);
  },
  buildBundleAroundIDWithFilter: (_id, _filter) => {
    return buildBundleAroundIDWithFilterCypher(_id, _filter);
  }
};
