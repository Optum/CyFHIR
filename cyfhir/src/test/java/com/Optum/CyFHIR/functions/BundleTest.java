package com.Optum.CyFHIR.functions;

// Don't Optimize org.neo4j.driver imports, prevents type ambiguity

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@Testcontainers
class BundleTest {

    @Container
    private static final GenericContainer neo4j = new GenericContainer<>("neo4j:4.1.0")
            .withEnv("NEO4J_AUTH", "neo4j/password")
            .withEnv("NEO4J_dbms_security_procedures_unrestricted", "cyfhir.*,apoc.*")
            .withFileSystemBind("./plugins", "/var/lib/neo4j/plugins", BindMode.READ_ONLY)
            .withExposedPorts(7687);

    Map loadJsonFromFile(String location) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<?, ?> map = mapper.readValue(Paths.get(location).toFile(), Map.class);
        return map;
    }

    String toJsonString(Map map) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(map);
        return mapper.writeValueAsString(json);
    }


    Driver getSessionDriver() {
        String uri = "bolt://" + neo4j.getContainerIpAddress() + ":" + neo4j.getMappedPort(7687);
        Driver driver = GraphDatabase.driver(uri, AuthTokens.basic("neo4j", "password"));
        return driver;
    }

    @AfterEach
    void cleanUp() {
        Driver driver = getSessionDriver();
        try (Session session = driver.session()) {
            Result load = session.run("MATCH (n) DETACH DELETE n");
        }
    }

    @Test
    void returnFullBundleCenteredAroundPatient() throws IOException {
        Map bundle = loadJsonFromFile("src/test/resources/ThreeResourceBundle.json");
        String bundleString = toJsonString(bundle);

        Map<String, String> hash = new HashMap();
        ((ArrayList<Map>) bundle.get("entry")).stream().forEach(entry -> {
            Map resource = (Map) entry.get("resource");
            String resourceType = (String) resource.get("resourceType");
            String resourceId = (String) resource.get("id");
            hash.put(resourceId, resourceType);
        });

        Map patientEntry = ((ArrayList<Map>) bundle.get("entry")).stream().filter(entry -> {
            Map resource = (Map) entry.get("resource");
            return resource.get("resourceType").equals("Patient");
        }).collect(Collectors.toList()).get(0);

        Map patient = (Map) patientEntry.get("resource");
        String patientId = (String) patient.get("id");

        Driver driver = getSessionDriver();

        try (Session session = driver.session()) {

            Result load = session.run("CALL cyfhir.bundle.load(" + bundleString + ")");
            String query = "" +
                    "CALL { \n" +
                    "WITH \"" + patientId + "\" as _id \n" +
                    "MATCH path=(a:entry)-[*1..3]->()-[r:reference]->(b:entry) \n" +
                    "WHERE (b._resourceId = _id) \n" +
                    "RETURN a,b \n" +
                    "UNION \n" +
                    "WITH \"" + patientId + "\" as _id \n" +
                    "MATCH path=(a:entry)-[*1..3]->()-[r:reference]->(b:entry) \n" +
                    "WHERE (a._resourceId = _id) \n" +
                    "RETURN a,b \n" +
                    "} \n" +
                    "WITH collect(a)+collect(b) AS entries \n" +
                    "UNWIND entries AS entry \n" +
                    "CALL cyfhir.resource.expand(entry) YIELD path \n" +
                    "WITH cyfhir.bundle.format(collect(path)) AS bundle " +
                    "RETURN bundle";

            Result result = session.run(query);

            List<Record> records = result.stream().collect(Collectors.toList());
            if (records.size() > 1) {
                fail("Too many nodes matched the query");
            } else {
                Map resultBundle = records.get(0).get("bundle").asMap();
                List<Map> entries = (List<Map>) resultBundle.get("entry");
                if (entries.size() != 3) {
                    fail("Wrong number of entries in bundle");
                } else {
                    entries.stream().forEach(entry -> {
                        Map resource = (Map) entry.get("resource");
                        String resourceType = (String) resource.get("resourceType");
                        String resourceId = (String) resource.get("id");

                        /******************************/
                        assertThat(entry).isNotNull();
                        assertThat(resourceType).isEqualTo(hash.get(resourceId));

                        if (resourceType.equals("Patient")) {
                            List<Map> address = (List<Map>) resource.get("address");
                            assertThat(address.size()).isEqualTo(1);
                            String city = (String) address.get(0).get("city");
                            String state = (String) address.get(0).get("state");
                            assertThat(city).isEqualTo("Archdale");
                            assertThat(state).isEqualTo("North Carolina");

                        } else if (resourceType.equals("Encounter")) {
                            Map serviceProvider = (Map) resource.get("serviceProvider");
                            String display = (String) serviceProvider.get("display");
                            String reference = (String) serviceProvider.get("reference");
                            assertThat(display).isEqualTo("HIGH POINT REGIONAL HOSPITAL");
                            assertThat(reference).isEqualTo("urn:uuid:2864a135-87f9-316b-a6b7-a272636846b6");

                        } else if (resourceType.equals("Condition")) {
                            Map code = (Map) resource.get("code");
                            List<Map> coding = (List<Map>) code.get("coding");
                            assertThat(coding.size()).isEqualTo(1);
                            String display = (String) coding.get(0).get("display");
                            String text = (String) code.get("text");
                            assertThat(display).isEqualTo(text);
                            assertThat(text).isEqualTo("Osteoarthritis of knee");
                        }
                    });
                }
            }
        }
    }
}
