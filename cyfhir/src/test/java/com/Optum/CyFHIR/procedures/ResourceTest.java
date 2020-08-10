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
import java.util.ArrayList;
import java.util.Collections;
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
}