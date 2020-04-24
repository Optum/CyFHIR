import apoc.convert.ConvertConfig;
import apoc.create.Create;
import apoc.result.MapResult;
import apoc.result.RelationshipResult;
import apoc.util.Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Entity;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Label;

import org.neo4j.procedure.Context;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;
import org.neo4j.procedure.UserFunction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Java Utils
// StringUtils
// Neo4Jø
// Apoc
// JSON Util

public class CyFhir {

    @Context
    public GraphDatabaseService db;

    public static void main(String[] args) {}


    private Map<String, Object> addRelProperties(Map<String, Object> mMap, String typeName, Relationship r, Map<String, List<String>> relFilters) {
        Map<String, Object> rProps = r.getAllProperties();
        if (rProps.isEmpty()) return mMap;
        String prefix = typeName + ".";
        if (relFilters.containsKey(typeName)) {
            rProps = filterProperties(rProps, relFilters.get(typeName));
        }
        rProps.forEach((k, v) -> mMap.put(prefix + k, v));
        return mMap;
    }

    private Map<String, Object> filterProperties(Map<String, Object> props, List<String> filters) {
        boolean isExclude = filters.get(0).startsWith("-");

        return props.entrySet().stream().filter(e -> isExclude ? !filters.contains("-" + e.getKey()) : filters.contains(e.getKey())).collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue()));
    }

    private Map<String, Object> toMap(Node n, Map<String, List<String>> nodeFilters) {
        Map<String, Object> props = n.getAllProperties();
        Map<String, Object> result = new LinkedHashMap<>(props.size() + 2);
        String type = Util.labelString(n);
        result.put("_id", n.getId());
        result.put("_type", type);
        if (nodeFilters.containsKey(type)) { //Check if list contains LABEL
            props = filterProperties(props, nodeFilters.get(type));
        }
        result.putAll(props);
        return result;
    }

    @Procedure("cyfhir.toStream")
    @Description("cyfhir.toStream([paths],[lowerCaseRels=true], [config]) creates a stream of nested documents representing the at least one root of these paths")
    public Stream<MapResult> toStream(@Name("paths") List<Path> paths, @Name(value = "lowerCaseRels", defaultValue = "true") boolean lowerCaseRels, @Name(value = "config", defaultValue = "{}") Map<String, Object> config) {
        if (paths.isEmpty()) return Stream.of(new MapResult(Collections.emptyMap()));
        ConvertConfig conf = new ConvertConfig(config);
        Map<String, List<String>> nodes = conf.getNodes();
        Map<String, List<String>> rels = conf.getRels();

        Map<Long, Map<String, Object>> maps = new HashMap<>(paths.size() * 100);
        for (Path path : paths) {
            Iterator<Entity> it = path.iterator();
            while (it.hasNext()) {
                Node n = (Node) it.next();
                Map<String, Object> nMap = maps.computeIfAbsent(n.getId(), (id) -> toMap(n, nodes));
                if (it.hasNext()) {
                    Relationship r = (Relationship) it.next();
                    Node m = r.getOtherNode(n);
                    String typeName = lowerCaseRels ? r.getType().name().toLowerCase() : r.getType().name();
                    // parent-[:HAS_CHILD]->(child) vs. (parent)<-[:PARENT_OF]-(child)
                    if (!nMap.containsKey(typeName)) nMap.put(typeName, new ArrayList<>(16));
                    List<Map<String, Object>> list = (List) nMap.get(typeName);
                    Optional<Map<String, Object>> optMap = list.stream()
                            .filter(elem -> elem.get("_id").equals(m.getId()))
                            .findFirst();
                    if (!optMap.isPresent()) {
                        Map<String, Object> mMap = toMap(m, nodes);
                        mMap = addRelProperties(mMap, typeName, r, rels);
                        maps.put(m.getId(), mMap);
                        list.add(maps.get(m.getId()));
                    }
                }
            }
        }
        return paths.stream()
                .map(Path::startNode)
                .distinct()
                .map(n -> maps.remove(n.getId()))
                .map(m -> m == null ? Collections.<String, Object>emptyMap() : m)
                .map(MapResult::new);
    }


    public Map<String, Object> stringToMap(@Name("jsonString") String jsonString) throws IOException {
        // todo make sure to change jsonTest to jsonString
        // Create JSON mapper
        ObjectMapper mapper = new ObjectMapper();
        // Convert JSON string to Map
        Map<String, Object> map = mapper.readValue(jsonString, Map.class);

        return map;
    }

    public class FhirRelationship {
        Node parentNode;
        String uuidChild;
        String relationType;

        Node getParentNode() { return parentNode; }
        String getChildUUID() { return uuidChild; }
        String getRelationType() { return relationType; }

        void setParentNode(Node node) { parentNode = node; }
        void setChildRelationship(String uuid, String relation) {
            uuidChild = uuid;
            relationType = relation;
        }
    }

    public class FhirRecursiveObj {
        Node node;
        ArrayList<FhirRelationship> relationships;
        Node getNode() {return node; }
        ArrayList<FhirRelationship> getRelationships() { return relationships; }
        void setNode(Node node) { this.node = node; }
        void setRelationships(ArrayList<FhirRelationship> relationships) { this.relationships = relationships; }
    }


    public ArrayList<FhirRelationship> getRelationshipsFromMap(Transaction tx, Map<String, Object> json, String key, Node parentNode, Boolean isArray, String resourceId) {
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

    public String getResourceId(Map<String, Object> json, String objectType) {
        if (objectType == "resource") {
            return (String) json.get("id");
        } else {
            return "";
        }
    }

    public FhirRecursiveObj nodeRecursion(Transaction tx, Map<String, Object>  json, String objectType, Boolean isArray, String resourceId) {
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
                        ArrayList<FhirRelationship> relations = getRelationshipsFromMap(tx, map, key, n, true, resourceId);
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
            String referenceID = json.get("reference").toString().substring(9);
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


    // Procedure that should be called.
    @Procedure(name = "cyfhir.loadBundle", mode = Mode.WRITE)
    @Description("cyfhir.loadBundle([paths],[lowerCaseRels=true], [config]) creates a stream of nested documents representing the at least one root of these paths")
    public Stream<MapResult> loadBundle(@Name("json") String json) throws IOException {
        Transaction tx = db.beginTx();
        // Generate JSON object from string of json
        Map<String, Object> jsonMap = stringToMap(json);
        // Get entries from bundle
        ArrayList<Map<String, Object>> entries = (ArrayList<Map<String, Object>>) jsonMap.get("entry");
        // Relationship Array
        ArrayList<FhirRelationship> relationships = new ArrayList<FhirRelationship>();
        // Iterate over entries
        entries.forEach((o) -> {
            // Set resource type
            String resourceType = "entry";
            Object fullUrl = o.get("fullUrl");
            ResourceIterator<Node> nodes = tx.findNodes(Label.label(resourceType), "fullUrl", fullUrl );
            try {
              Node duplicate = nodes.next();
              System.out.println("Duplicate Node: " + duplicate.toString());
            } catch (Exception e){
              // Get resource ID
              Map<String, Object> resourceMap = (Map<String, Object>) o.get("resource");
              String resourceId = (String) resourceMap.get("id");
              // Start json recursion
              FhirRecursiveObj recursionObject = nodeRecursion(tx, o, resourceType, true, resourceId);
              // Get relationships
              ArrayList<FhirRelationship> rels = recursionObject.getRelationships();
              // Add relationships to total array
              relationships.addAll(rels);
            }
        });

        // Add class for relationship creation
        Create create = new Create();
        // Create map object for props
        Map<String, Object> props = new HashMap<String, java.lang.Object>();
        // Iterate through relationships

        relationships.forEach((o) -> {
            Label label = Label.label("entry");
            String childID = o.getChildUUID();
            String childURN = "urn:uuid:" + childID;
            // Retrieve child node
            Node childNode = tx.findNode(label, "fullUrl", childURN);
            // Get parent node
            Node parentNode = o.getParentNode();
            // Get relationship string
            String relationship = o.getRelationType();
            // Create relationship
            if(parentNode != null && childNode != null){
              create.relationship(parentNode, relationship, props, childNode);
            }
        });

        // Create paths list
        List<Path> response = new ArrayList<Path>();
        // Create map for config
        Map<String, Object> responseMap = new HashMap<String, Object>();
        // Return apoc method results
        Stream<MapResult> stream = toStream(response, true, responseMap);

        tx.commit();
        tx.close();
        return stream;
    }

    // User function to map
        //template chypher query used to invoke this function
        // WITH ["Patient", "Condition"] AS resources
        // MATCH path1=((l:entry)-[*1]->(m:resource)-[*]->(n))
        // MATCH path2=((l:entry)-[*1]->(k:request))
        // WHERE (m.resourceType in resources or k.url in resources)
        // AND NONE(n IN nodes(path1) WHERE EXISTS(n.reference))
        // WITH apoc.coll.union(collect(path1), collect(path2)) as paths
        // CALL apoc.convert.toTree(paths) YIELD value
        // RETURN cyfhir.buildBundle(COLLECT(value))
    @UserFunction("cyfhir.buildBundle")
    @Description("cyfhir.buildBundle(collect(paths) parses nodes into FHIR bundle given a collected Tree of connected entries")
    public Map buildBundle(@Name("mapResponse") List<Map> mapResponse) throws IOException {

        //build empty bundle
        Map bundle = new HashMap();
        if(!mapResponse.get(0).isEmpty()){
          bundle.put("resourceType", "Bundle");
          bundle.put("type", "transaction");

          //create empty list of entries
          List<Map> entries = new ArrayList<Map>();

          //traverse each entry in the mapResponse and map with parseResource
          for(Map entry : mapResponse){
              Map entryMap  = new HashMap();
              //map the resource
              entryMap.put("resource", parseResources((Map) ((ArrayList) entry.get("resource")).get(0)));
              //map the request
              entryMap.put("request", parseResources((Map) ((ArrayList) entry.get("request")).get(0)));
              //map the fullUrl
              entryMap.put("fullUrl", entry.get("fullUrl"));
              //add parsed entry to list of entries
              entries.add(entryMap);
          }
          bundle.put("entry", entries);
        }
        return bundle;
    }

    public Map parseResources(Map resource) {
        //remove custom fields/data cleanup
        resource.remove("_resourceId"); resource.remove("_id"); resource.remove("_type"); resource.remove("_isArray");
        Map resourceMap = new HashMap();
        //traverse resource keys and map values accordingly
            for(Object key : resource.keySet()){
                //map each nested resources if within an array - although
               if (resource.get(key) instanceof ArrayList) {
                    ArrayList<Map> mapList = new ArrayList<Map>();
                    //extract type field to set as the key
                    String type = ((ArrayList<Map>)resource.get(key)).get(0).get("_type").toString();
                    //find if array is true array
                    Boolean isArray = (Boolean)((ArrayList<Map>)resource.get(key)).get(0).get("_isArray");
                    //map flat keys if resource is not a true array
                    if(!isArray){
                        resourceMap.put(type, parseResources(((ArrayList<Map>)resource.get(key)).get(0)));
                    }
                    else {
                        //if true array map each sub resource
                        for(Map subResource : (ArrayList<Map>)resource.get(key)){
                            //remove custom fields/data cleanup
                            subResource.remove("_type"); subResource.remove("_id"); subResource.remove("_isArray"); subResource.remove("_resourceId");
                            mapList.add(parseResources(subResource));
                        }
                        resourceMap.put(type, mapList);
                    }
                }
               //if a flat key, then map value
                else {
                    //to convert arrays stored as Strings in neo4j properties
                    if(resource.get(key).toString().matches("\\[.+(, .+)*]")){
                        resourceMap.put(key, Arrays.asList(resource.get(key).toString().substring(1, resource.get(key).toString().length() - 1).split(",")).stream().map(item ->{return item.trim();}));
                    }
                    //map string, long, double, and boolean values
                    else {
                        resourceMap.put(key, resource.get(key));
                    }
                }
            }
        return resourceMap;
    }
}
