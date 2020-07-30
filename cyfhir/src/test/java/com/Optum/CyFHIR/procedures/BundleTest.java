package com.Optum.CyFHIR.procedures;

import org.junit.jupiter.api.Test;
import org.neo4j.driver.*;
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
class BundleTest {

    @Container
    public GenericContainer neo4j = new GenericContainer<>("neo4j:4.1.0")
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

    @Test
    void loadBundleWithOneResourceFindEntry() throws IOException {
        Map bundle = loadJsonFromFile("src/test/resources/PatientOnlyBundle.json");
        String bundleString = toJsonString(bundle);

        String fullUrl = (((ArrayList<Map>) bundle.get("entry")).get(0)).get("fullUrl").toString();
        Driver driver = getSessionDriver();

        try (Session session = driver.session()) {

            Result load = session.run("CALL cyfhir.bundle.load(" + bundleString + ")");
            Result result = session.run("MATCH (n:entry) RETURN n");

            List<Record> Records = result.stream().collect(Collectors.toList());
            if (Records.size() > 1) {
                fail("Too many nodes matched the query");
            } else {
                Map resultNode = Records.get(0).get("n").asNode().asMap();

                /******************************/
                assertThat(resultNode).isNotNull();

                assertThat(resultNode.get("fullUrl")).isNotNull();
                assertThat(resultNode.get("fullUrl").toString()).isEqualTo(fullUrl);
            }
        }
    }

    @Test
    void loadBundleWithOneResourceFindResource() throws IOException {
        Map bundle = loadJsonFromFile("src/test/resources/PatientOnlyBundle.json");
        String bundleString = toJsonString(bundle);

        Map resource = (Map) ((ArrayList<Map>) bundle.get("entry")).get(0).get("resource");
        String resourceType = resource.get("resourceType").toString();
        String id = resource.get("id").toString();

        Driver driver = getSessionDriver();

        try (Session session = driver.session()) {

            Result load = session.run("CALL cyfhir.bundle.load(" + bundleString + ")");
            Result result = session.run("MATCH (n:resource) RETURN n");

            List<Record> Records = result.stream().collect(Collectors.toList());
            if (Records.size() > 1) {
                fail("Too many nodes matched the query");
            } else {
                Map resultNode = Records.get(0).get("n").asNode().asMap();

                /******************************/
                assertThat(resultNode).isNotNull();

                assertThat(resultNode.get("resourceType")).isNotNull();
                assertThat(resultNode.get("resourceType").toString()).isEqualTo(resourceType);

                assertThat(resultNode.get("id")).isNotNull();
                assertThat(resultNode.get("id").toString()).isEqualTo(id);
            }
        }
    }
}