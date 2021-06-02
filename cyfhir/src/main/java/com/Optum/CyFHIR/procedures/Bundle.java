package com.Optum.CyFHIR.procedures;

import com.Optum.CyFHIR.models.Entry;
import com.Optum.CyFHIR.models.Validator;
import com.Optum.CyFHIR.models.FhirRelationship;
import apoc.result.MapResult;
import org.neo4j.graphdb.*;
import org.neo4j.procedure.*;

import java.util.*;
import java.util.stream.Stream;

public class Bundle {
    public static Validator validator;

    @Context
    public GraphDatabaseService db;

    public Bundle() throws Exception {
        validator = new Validator();
    }

    @Procedure(name = "cyfhir.bundle.load", mode = Mode.WRITE)
    @Description("cyfhir.bundle.load(String bundle, config: { validation: Boolean, version: String }) loads a FHIR bundle into the neo4j, must be a stringified JSON. "
            + "The config allows you to turn on FHIR validation and pick a version with choices being DSTU3, R4, and R5. If validation == true, the default for fhir version is R4")
    public Stream<MapResult> load(@Name("json") String json,
            @Name(value = "config", defaultValue = "{}") Map<String, Object> configMap) throws Exception {

        Transaction tx = db.beginTx();
        Resource resourceClass = new Resource();
        // Generate JSON object from string of json
        Map<String, Object> bundleMap = resourceClass.stringToMap(json);
        String resourceType = (String) bundleMap.get("resourceType");
        resourceClass.validateFHIR(json, resourceType, configMap);

        /* Get entries from bundle */
        ArrayList<Map<String, Object>> entries = (ArrayList<Map<String, Object>>) bundleMap.get("entry");
        // Relationship Array
        ArrayList<FhirRelationship> relationships = new ArrayList<FhirRelationship>();
        ArrayList<String> fullUrls = new ArrayList<>();
        // Iterate over entries
        entries.forEach((entry) -> {
            Map<String, Object> resource = (Map<String, Object>) entry.get("resource");
            Entry entryObj = new Entry();
            entryObj.setResource(resource);
            relationships.addAll(resourceClass.addToDatabase(entryObj, tx));
            fullUrls.add((String) entry.get("fullUrl"));
        });
        resourceClass.createRelationships(relationships, tx);
        // Create map for response
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("fullUrls", fullUrls);
        Stream<MapResult> stream = Stream.of(new MapResult(responseMap));

        // This function attaches references to all of the resources currently being loaded from previously loaded
        // resources.
        // It runs a Cypher Query due to there being >700 possible node labels that can have a reference as a property
        resourceClass.attachLooseReferences(fullUrls, tx);

        tx.commit();
        tx.close();
        return stream;
    }

}
