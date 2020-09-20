package com.Optum.CyFHIR.procedures;

import apoc.create.Create;
import apoc.path.LabelSequenceEvaluator;
import apoc.path.NodeEvaluators;
import apoc.path.RelationshipSequenceExpander;
import apoc.result.MapResult;
import apoc.result.PathResult;
import apoc.result.RelationshipResult;
import com.Optum.CyFHIR.models.Entry;
import com.Optum.CyFHIR.models.FhirRecursiveObj;
import com.Optum.CyFHIR.models.FhirRelationship;
import com.Optum.CyFHIR.models.Validator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.*;
import org.neo4j.internal.helpers.collection.Iterables;
import org.neo4j.procedure.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Resource {
    public static final Uniqueness UNIQUENESS = Uniqueness.RELATIONSHIP_PATH;
    public static final boolean BFS = true;
    public static Validator validator;

    @Context
    public GraphDatabaseService db;

    public Resource() throws Exception {
        validator = new Validator();
    }

    @Procedure(name = "cyfhir.resource.load", mode = Mode.WRITE)
    @Description("cyfhir.resource.load(resource, config: { validation: Boolean, version: String }) loads a FHIR resource into Neo4j, must be a stringified JSON"
            + "The config allows you to turn on FHIR validation and pick a version with choices being DSTU3, R4, and R5. If validation == true, the default for fhir version is R4")
    public Stream<MapResult> load(@Name("json") String json,
            @Name(value = "config", defaultValue = "{}") Map<String, Object> configMap) throws Exception {
        Transaction tx = db.beginTx();
        // Generate JSON object from string of json
        Map<String, Object> resourceMap = stringToMap(json);
        // Validate
        String resourceType = (String) resourceMap.get("resourceType");
        validateFHIR(json, resourceType, configMap);

        Entry entry = new Entry();
        entry.setResource(resourceMap);
        // Relationship Array
        ArrayList<FhirRelationship> relationships = addToDatabase(entry, tx);
        createRelationships(relationships, tx);
        // Array for FullUrl
        ArrayList<String> fullUrls = new ArrayList<>();
        fullUrls.add((String) entry.get("fullUrl"));

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("fullUrls", fullUrls);
        Stream<MapResult> stream = Stream.of(new MapResult(responseMap));

        // This function attaches references to a resource being loaded from previously loaded resources.
        // It runs a Cypher Query due to there being >700 possible node labels that can have a reference as a property
        attachLooseReferences(fullUrls, tx);

        tx.commit();
        tx.close();
        return stream;
    }

    public void attachLooseReferences(ArrayList<String> fullUrls, Transaction tx) {
        String collect = fullUrls.stream().collect(Collectors.joining("\", \"", "[\"", "\"]"));
        String cypher = "MATCH (a)\n" + "MATCH (b:entry {fullUrl: a.reference}) \n" + "WHERE NOT (a)-[:reference]->()\n"
                + "AND b.fullUrl IN " + collect + " \n CREATE (a)-[:reference]->(b)";
        tx.execute(cypher);
    }

    public IAnyResource validateFHIR(String json, String resourceType, Map<String, Object> configMap) throws Exception {
        if (!configMap.isEmpty()) {
            if (configMap.containsKey("validation")) {
                Boolean validation = (Boolean) configMap.get("validation");
                if (validation) {
                    IAnyResource bundle;
                    if (configMap.containsKey("version")) {
                        validator = new Validator((String) configMap.get("version"));
                    }
                    return validator.validate(json, resourceType);
                }
            }
        }
        return null;
    }

    public ArrayList<FhirRelationship> addToDatabase(Entry entry, Transaction tx) {
        String resourceType = "entry";
        ResourceIterator<Node> nodes = tx.findNodes(Label.label(resourceType), "fullUrl", entry.get("fullUrl"));
        ArrayList<FhirRelationship> rels = new ArrayList<>();
        try {
            System.out.println("Duplicate Resource With ID: " + nodes.next().getProperty("_resourceId").toString());
        } catch (Exception e) {
            // Get resource ID
            Map<String, Object> resourceMap = (Map<String, Object>) entry.get("resource");
            String resourceId = (String) resourceMap.get("id");
            // Start json recursion
            FhirRecursiveObj recursionObject = nodeRecursion(tx, entry, resourceType, true, resourceId);
            // Get relationships
            rels = recursionObject.getRelationships();
        }
        return rels;
    }

    public void createRelationships(ArrayList<FhirRelationship> relationships, Transaction tx) {
        // Add class for relationship creation
        Create create = new Create();
        // Create map object for props
        Map<String, Object> props = new HashMap<String, Object>();
        // Iterate through relationships

        relationships.forEach((o) -> {
            Label label = Label.label("entry");
            String childID = o.getChildUUID();
            // Retrieve child node
            Node childNode = tx.findNode(label, "fullUrl", childID);
            // Get parent node
            Node parentNode = o.getParentNode();
            // Get relationship string
            String relationship = o.getRelationType();
            // Create relationship
            if (parentNode != null && childNode != null) {
                create.relationship(parentNode, relationship, props, childNode);
            }
        });
    }

    public Map<String, Object> stringToMap(@Name("jsonString") String jsonString) throws IOException {
        // Create JSON mapper
        ObjectMapper mapper = new ObjectMapper();
        // Convert JSON string to Map
        Map<String, Object> map = mapper.readValue(jsonString, Map.class);

        return map;
    }

    public ArrayList<FhirRelationship> getRelationshipsFromMap(Transaction tx, Map<String, Object> json, String key,
            Node parentNode, Boolean isArray, String resourceId) {
        // Add class for relationship creation
        Create create = new Create();
        // Create Properties List
        Map<String, Object> props = new HashMap<String, Object>();
        // Create relationship array
        ArrayList<FhirRelationship> relationships = new ArrayList<FhirRelationship>();
        // Recursively create node and retrieve map
        FhirRecursiveObj nodeMap = nodeRecursion(tx, json, key, isArray, resourceId);
        // Retrieve node from map
        Node nNode = nodeMap.getNode();
        // Create relationship between parent node and node from recursion
        Stream<RelationshipResult> relationshipResult = create.relationship(parentNode, key, props, nNode);
        // get child relationships
        ArrayList<FhirRelationship> childRels = nodeMap.getRelationships();
        // Add relationships to parent array
        if (childRels != null) {
            relationships.addAll(childRels);
        }
        // Return relationships
        return relationships;
    }

    public FhirRecursiveObj nodeRecursion(Transaction tx, Map<String, Object> json, String objectType, Boolean isArray,
            String resourceId) {
        // Create map that will be returned
        FhirRecursiveObj recursionObject = new FhirRecursiveObj();
        // Create relationship array
        ArrayList<FhirRelationship> relationships = new ArrayList<FhirRelationship>();
        // Create node for returnable object
        Node n = tx.createNode(Label.label(objectType));
        // Set _isArray Property
        n.setProperty("_isArray", isArray);
        // Set _resourceId Property
        n.setProperty("_resourceId", resourceId);
        // Add node properties
        json.forEach((key, o) -> {
            if (o instanceof ArrayList) {
                ArrayList a = (ArrayList) o;
                if (a.get(0) instanceof LinkedHashMap) {
                    // Iterate over map instances
                    a.forEach((t) -> {
                        // Cast object as map
                        Map<String, Object> map = (Map<String, Object>) t;
                        // Get relationships
                        ArrayList<FhirRelationship> relations = getRelationshipsFromMap(tx, map, key, n, true,
                                resourceId);
                        // Add relationships to parent array
                        relationships.addAll(relations);
                    });
                } else {
                    n.setProperty(key, a.toString());
                }
            } else if (o instanceof LinkedHashMap) {
                // Cast object as map
                Map<String, Object> map = (Map<String, Object>) o;
                // Get relationships
                ArrayList<FhirRelationship> relations = getRelationshipsFromMap(tx, map, key, n, false, resourceId);
                // Add relationships to parent array
                relationships.addAll(relations);
            } else {
                // Double Boolean String Integer
                n.setProperty(key, o);
            }
        });

        // Create relationship if we have a reference
        if (json.containsKey("reference")) {
            // Get child node reference
            String referenceID = json.get("reference").toString();
            // Create relationship object
            FhirRelationship ref = new FhirRelationship();
            // Set parent id
            ref.setParentNode(n);
            // Add relationship to relationship object
            ref.setChildRelationship(referenceID, "reference");
            // Add relationship to array
            relationships.add(ref);
        }

        // Add entries into recursion object
        recursionObject.setNode(n);
        recursionObject.setRelationships(relationships);
        // Return object
        return recursionObject;
    }

    @Procedure(name = "cyfhir.resource.expand", mode = Mode.READ)
    @Description("cyfhir.resource.expand(startNode Node) yield path - expand from entry node following the given relationships from min to max-levelof the FHIR resource, stopping at references")
    public Stream<PathResult> expand(@Name("start") Object start) throws Exception {
        Transaction tx = db.beginTx();
        List<Node> nodes = startToNodes(start, tx);
        Stream<PathResult> expandedResource = explorePathPrivate(nodes, ">|relationship", "-entry", 0, 999, BFS,
                UNIQUENESS, false, -1, null, null, true, tx).map(PathResult::new);

        tx.commit();
        tx.close();
        return expandedResource;
    }

    @SuppressWarnings("unchecked")
    private List<Node> startToNodes(Object start, Transaction tx) throws Exception {
        if (start == null)
            return Collections.emptyList();
        if (start instanceof Node) {
            return Collections.singletonList((Node) start);
        }
        if (start instanceof Number) {
            return Collections.singletonList(tx.getNodeById(((Number) start).longValue()));
        }
        if (start instanceof List) {
            List list = (List) start;
            if (list.isEmpty())
                return Collections.emptyList();

            Object first = list.get(0);
            if (first instanceof Node)
                return (List<Node>) list;
            if (first instanceof Number) {
                List<Node> nodes = new ArrayList<>();
                for (Number n : ((List<Number>) list))
                    nodes.add(tx.getNodeById(n.longValue()));
                return nodes;
            }
        }
        throw new Exception(
                "Unsupported data type for start parameter a Node or an Identifier (long) of a Node must be given!");
    }

    private Stream<Path> explorePathPrivate(Iterable<Node> startNodes, String pathFilter, String labelFilter,
            long minLevel, long maxLevel, boolean bfs, Uniqueness uniqueness, boolean filterStartNode, long limit,
            EnumMap<NodeFilter, List<Node>> nodeFilter, String sequence, boolean beginSequenceAtStart, Transaction tx) {

        Traverser traverser = traverse(tx.traversalDescription(), startNodes, pathFilter, labelFilter, minLevel,
                maxLevel, uniqueness, bfs, filterStartNode, nodeFilter, sequence, beginSequenceAtStart);

        if (limit == -1) {
            return Iterables.stream(traverser);
        } else {
            return Iterables.stream(traverser).limit(limit);
        }
    }

    public Traverser traverse(TraversalDescription traversalDescription, Iterable<Node> startNodes, String pathFilter,
            String labelFilter, long minLevel, long maxLevel, Uniqueness uniqueness, boolean bfs,
            boolean filterStartNode, EnumMap<NodeFilter, List<Node>> nodeFilter, String sequence,
            boolean beginSequenceAtStart) {
        TraversalDescription td = traversalDescription;
        // based on the pathFilter definition now the possible relationships and directions must be shown

        td = bfs ? td.breadthFirst() : td.depthFirst();

        // if `sequence` is present, it overrides `labelFilter` and `relationshipFilter`
        if (sequence != null && !sequence.trim().isEmpty()) {
            String[] sequenceSteps = sequence.split(",");
            List<String> labelSequenceList = new ArrayList<>();
            List<String> relSequenceList = new ArrayList<>();

            for (int index = 0; index < sequenceSteps.length; index++) {
                List<String> seq = (beginSequenceAtStart ? index : index - 1) % 2 == 0 ? labelSequenceList
                        : relSequenceList;
                seq.add(sequenceSteps[index]);
            }

            td = td.expand(new RelationshipSequenceExpander(relSequenceList, beginSequenceAtStart));
            td = td.evaluator(new LabelSequenceEvaluator(labelSequenceList, filterStartNode, beginSequenceAtStart,
                    (int) minLevel));
        } else {
            if (pathFilter != null && !pathFilter.trim().isEmpty()) {
                td = td.expand(new RelationshipSequenceExpander(pathFilter.trim(), beginSequenceAtStart));
            }

            if (labelFilter != null && sequence == null && !labelFilter.trim().isEmpty()) {
                td = td.evaluator(new LabelSequenceEvaluator(labelFilter.trim(), filterStartNode, beginSequenceAtStart,
                        (int) minLevel));
            }
        }

        if (minLevel != -1)
            td = td.evaluator(Evaluators.fromDepth((int) minLevel));
        if (maxLevel != -1)
            td = td.evaluator(Evaluators.toDepth((int) maxLevel));

        if (nodeFilter != null && !nodeFilter.isEmpty()) {
            List<Node> endNodes = nodeFilter.getOrDefault(NodeFilter.END_NODES, Collections.EMPTY_LIST);
            List<Node> terminatorNodes = nodeFilter.getOrDefault(NodeFilter.TERMINATOR_NODES, Collections.EMPTY_LIST);
            List<Node> blacklistNodes = nodeFilter.getOrDefault(NodeFilter.BLACKLIST_NODES, Collections.EMPTY_LIST);
            List<Node> whitelistNodes;

            if (nodeFilter.containsKey(NodeFilter.WHITELIST_NODES)) {
                // need to add to new list since we may need to add to it later
                // encounter "can't add to abstractList" error if we don't do this
                whitelistNodes = new ArrayList<>(nodeFilter.get(NodeFilter.WHITELIST_NODES));
            } else {
                whitelistNodes = Collections.EMPTY_LIST;
            }

            if (!blacklistNodes.isEmpty()) {
                td = td.evaluator(
                        NodeEvaluators.blacklistNodeEvaluator(filterStartNode, (int) minLevel, blacklistNodes));
            }

            Evaluator endAndTerminatorNodeEvaluator = NodeEvaluators.endAndTerminatorNodeEvaluator(filterStartNode,
                    (int) minLevel, endNodes, terminatorNodes);
            if (endAndTerminatorNodeEvaluator != null) {
                td = td.evaluator(endAndTerminatorNodeEvaluator);
            }

            if (!whitelistNodes.isEmpty()) {
                // ensure endNodes and terminatorNodes are whitelisted
                whitelistNodes.addAll(endNodes);
                whitelistNodes.addAll(terminatorNodes);
                td = td.evaluator(
                        NodeEvaluators.whitelistNodeEvaluator(filterStartNode, (int) minLevel, whitelistNodes));
            }
        }

        td = td.uniqueness(uniqueness); // this is how Cypher works !! Uniqueness.RELATIONSHIP_PATH
        // uniqueness should be set as last on the TraversalDescription
        return td.traverse(startNodes);
    }

    // keys to node filter map
    enum NodeFilter {
        WHITELIST_NODES, BLACKLIST_NODES, END_NODES, TERMINATOR_NODES
    }

}
