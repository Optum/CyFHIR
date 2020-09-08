package com.Optum.CyFHIR.procedures;

import apoc.result.MapResult;
import com.Optum.CyFHIR.models.Entry;
import com.Optum.CyFHIR.models.FhirRelationship;
import com.Optum.CyFHIR.models.Validator;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.neo4j.graphdb.*;
import org.neo4j.procedure.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class Bundle {

    public static Validator validator;
    @Context
    public GraphDatabaseService db;

    public Bundle() {
        validator = new Validator();
    }

    @Procedure(name = "cyfhir.bundle.load", mode = Mode.WRITE)
    @Description("cyfhir.bundle.load(bundle) loads a FHIR bundle into the db, must be a stringified JSON")
    public Stream<MapResult> load(@Name("json") String json) throws IOException {

        Transaction tx = db.beginTx();
        Resource resourceClass = new Resource();
        // Generate JSON object from string of json
        Map<String, Object> jsonMap = resourceClass.stringToMap(json);
        IAnyResource bundle = validator.validate(json, (String) jsonMap.get("resourceType"));
        System.out.println(bundle);
        /* Get entries from bundle */
        ArrayList<Map<String, Object>> entries = (ArrayList<Map<String, Object>>) jsonMap.get("entry");
        // Relationship Array
        ArrayList<FhirRelationship> relationships = new ArrayList<FhirRelationship>();
        // Iterate over entries
        entries.forEach((entry) -> {
            Map<String, Object> resource = (Map<String, Object>) entry.get("resource");
            Entry entryObj = new Entry();
            entryObj.setResource(resource);
            relationships.addAll(resourceClass.addToDatabase(entryObj, tx));
        });
        resourceClass.createRelationships(relationships, tx);
        // Create paths list
        List<Path> response = new ArrayList<Path>();
        // Create map for config
        Map<String, Object> responseMap = new HashMap<String, Object>();
        // Return apoc method results
        Convert convert = new Convert();
        Stream<MapResult> stream = convert.toTree(response, true, responseMap);

        tx.commit();
        tx.close();
        return stream;
    }

}
