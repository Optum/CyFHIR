package com.Optum.CyFHIR.procedures;

import apoc.create.Create;
import apoc.result.MapResult;
import apoc.result.RelationshipResult;
import com.Optum.CyFHIR.models.FhirRecursiveObj;
import com.Optum.CyFHIR.models.FhirRelationship;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.neo4j.graphdb.*;
import org.neo4j.procedure.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class loadBundle {

    @Context
    public GraphDatabaseService db;

    @Procedure(name = "cyfhir.loadBundle", mode = Mode.WRITE)
    @Description("cyfhir.loadBundle(bundle) loads a FHIR bundle into the db, must be a stringified JSON")
    public Stream<MapResult> loadBundle(@Name("json") String json) throws IOException {
        Transaction tx = db.beginTx();
        // Generate JSON object from string of json
        Map<String, Object> jsonMap = stringToMap(json);
        /* Get entries from bundle */
        ArrayList<Map<String, Object>> entries = (ArrayList<Map<String, Object>>) jsonMap.get("entry");
        // Relationship Array
        ArrayList<FhirRelationship> relationships = new ArrayList<FhirRelationship>();
        // Iterate over entries
        entries.forEach((o) -> {
            // Set resource type
            String resourceType = "entry";
            Object fullUrl = o.get("fullUrl");
            ResourceIterator<Node> nodes = tx.findNodes(Label.label(resourceType), "fullUrl", fullUrl);
            try {
                Node duplicate = nodes.next();
                System.out.println("Duplicate Node: " + duplicate.toString());
            } catch (Exception e) {
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
        Map<String, Object> props = new HashMap<String, Object>();
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
            if (parentNode != null && childNode != null) {
                create.relationship(parentNode, relationship, props, childNode);
            }
        });

        // Create paths list
        List<Path> response = new ArrayList<Path>();
        // Create map for config
        Map<String, Object> responseMap = new HashMap<String, Object>();
        // Return apoc method results
        Stream<MapResult> stream = toStream.toStream(response, true, responseMap);

        tx.commit();
        tx.close();
        return stream;
    }

    public Map<String, Object> stringToMap(@Name("jsonString") String jsonString) throws IOException {
        // todo make sure to change jsonTest to jsonString
        // Create JSON mapper
        ObjectMapper mapper = new ObjectMapper();
        // Convert JSON string to Map
        Map<String, Object> map = mapper.readValue(jsonString, Map.class);

        return map;
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

    public FhirRecursiveObj nodeRecursion(Transaction tx, Map<String, Object> json, String objectType, Boolean isArray, String resourceId) {
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

}
