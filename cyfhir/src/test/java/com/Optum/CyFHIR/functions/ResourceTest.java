package com.Optum.CyFHIR.functions;

// Don't Optimize org.neo4j.driver imports, prevents type ambiguity

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@Testcontainers
class ResourceTest {

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
    void returnPatientResourceFromBundle() throws IOException {
        Map bundle = loadJsonFromFile("src/test/resources/ThreeResourceBundle.json");
        String bundleString = toJsonString(bundle);

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
                    "WITH \"" + patientId + "\" as _id \n" +
                    "MATCH (r:resource)\n" +
                    "WHERE (r.id = _id)\n" +
                    "CALL cyfhir.resource.expand(r) YIELD path\n" +
                    "WITH cyfhir.resource.format(collect(path)) AS patient\n" +
                    "RETURN patient";

            Result result = session.run(query);

            List<Record> records = result.stream().collect(Collectors.toList());
            if (records.size() != 1) {
                fail("Wrong number of nodes matched query");
            } else {
                Map patientResult = records.get(0).get("patient").asMap();
                List<Map> address = (List<Map>) patientResult.get("address");
                String city = (String) address.get(0).get("city");
                String state = (String) address.get(0).get("state");

                assertThat((String) patientResult.get("id")).isEqualTo(patientId);
                assertThat((String) patientResult.get("resourceType")).isEqualTo("Patient");
                assertThat(address.size()).isEqualTo(1);
                assertThat(city).isEqualTo("Archdale");
                assertThat(state).isEqualTo("North Carolina");
            }
        }
    }

    @Test
    void returnPatientResourceFromOnlyPatientResource() throws IOException {
        Map resource = loadJsonFromFile("src/test/resources/Patient.json");
        String resourceString = toJsonString(resource);

        String patientId = (String) resource.get("id");
        Driver driver = getSessionDriver();

        try (Session session = driver.session()) {

            Result load = session.run("CALL cyfhir.resource.load(" + resourceString + ")");
            String query = "" +
                    "WITH \"" + patientId + "\" as _id \n" +
                    "MATCH (r:resource)\n" +
                    "WHERE (r.id = _id)\n" +
                    "CALL cyfhir.resource.expand(r) YIELD path\n" +
                    "WITH cyfhir.resource.format(collect(path)) AS patient\n" +
                    "RETURN patient";

            Result result = session.run(query);

            List<Record> records = result.stream().collect(Collectors.toList());
            if (records.size() != 1) {
                fail("Wrong number of nodes matched query");
            } else {
                Map patientResult = records.get(0).get("patient").asMap();
                List<Map> address = (List<Map>) patientResult.get("address");
                String city = (String) address.get(0).get("city");
                String state = (String) address.get(0).get("state");

                assertThat((String) patientResult.get("id")).isEqualTo(patientId);
                assertThat((String) patientResult.get("resourceType")).isEqualTo("Patient");
                assertThat(address.size()).isEqualTo(1);
                assertThat(city).isEqualTo("Archdale");
                assertThat(state).isEqualTo("North Carolina");
            }
        }
    }
}
