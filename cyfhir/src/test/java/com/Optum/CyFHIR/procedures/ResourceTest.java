package com.Optum.CyFHIR.procedures;

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
            .withFileSystemBind("./plugins", "/var/lib/neo4j/plugins", BindMode.READ_ONLY).withExposedPorts(7687);

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
    void loadPatientResourceOnly() throws IOException {
        Map resource = loadJsonFromFile("src/test/resources/Patient.json");
        String resourceString = toJsonString(resource);

        String id = (String) resource.get("id");
        Driver driver = getSessionDriver();

        try (Session session = driver.session()) {

            Result load = session.run("CALL cyfhir.resource.load(" + resourceString + ")");
            Result result = session.run("MATCH (n:resource) RETURN n");

            List<Record> records = result.stream().collect(Collectors.toList());
            if (records.size() != 1) {
                fail("Wrong number of nodes matched the query");
            } else {
                Map resultNode = records.get(0).get("n").asNode().asMap();

                /******************************/
                assertThat(resultNode).isNotNull();

                assertThat(resultNode.get("id")).isNotNull();
                assertThat(resultNode.get("id").toString()).isEqualTo(id);
            }
        }
    }

    @Test
    void testLoadSequence() throws IOException {
        Map patient = loadJsonFromFile("src/test/resources/Patient.json");
        String patientString = toJsonString(patient);

        Map encounter = loadJsonFromFile("src/test/resources/Encounter.json");
        String encounterString = toJsonString(encounter);

        Map condition = loadJsonFromFile("src/test/resources/Condition.json");
        String conditionString = toJsonString(condition);

        Driver driver = getSessionDriver();
        try (Session session = driver.session()) {
            // Run in order that automatically links references due to nature of the order
            session.run("CALL cyfhir.resource.load(" + patientString + ")");
            session.run("CALL cyfhir.resource.load(" + encounterString + ")");
            session.run("CALL cyfhir.resource.load(" + conditionString + ")");

            String metricQuery = "MATCH (n) \n" + " WITH COUNT(n) as node_count\n" + " MATCH ()-[r]->()\n"
                    + " WITH node_count, COUNT(r) as relationship_count\n" + " RETURN node_count, relationship_count";

            Result result1 = session.run(metricQuery);

            List<Record> records1 = result1.stream().collect(Collectors.toList());
            Record record1 = records1.get(0);
            Integer node_count1 = record1.get("node_count").asInt();
            Integer relationship_count1 = record1.get("relationship_count").asInt();

            // Clean up from first part of the test
            session.run("MATCH (n) DETACH DELETE n");

            // Run in order that normally wouldn't link references due to reverse order, but now should
            session.run("CALL cyfhir.resource.load(" + conditionString + ")");
            session.run("CALL cyfhir.resource.load(" + encounterString + ")");
            session.run("CALL cyfhir.resource.load(" + patientString + ")");

            String metricQuery2 = "MATCH (n) \n" + " WITH COUNT(n) as node_count\n" + " MATCH ()-[r]->()\n"
                    + " WITH node_count, COUNT(r) as relationship_count\n" + " RETURN node_count, relationship_count";

            Result result2 = session.run(metricQuery);

            List<Record> records2 = result2.stream().collect(Collectors.toList());
            Record record2 = records2.get(0);
            Integer node_count2 = record2.get("node_count").asInt();
            Integer relationship_count2 = record2.get("relationship_count").asInt();

            assertThat(node_count1).isEqualTo(relationship_count1);
            assertThat(node_count1).isEqualTo(node_count2);
            assertThat(relationship_count1).isEqualTo(relationship_count2);
        }
    }
}
