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
import org.testcontainers.utility.DockerImageName;

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
class BundleTest {

    @Container
    private static final GenericContainer<?> neo4j = new GenericContainer<>(DockerImageName.parse("neo4j:4.2.7"))
            .withExposedPorts(7687).withEnv("NEO4J_AUTH", "neo4j/password")
            .withEnv("NEO4J_dbms_security_procedures_unrestricted", "cyfhir.*,apoc.*")
            .withFileSystemBind("./plugins", "/var/lib/neo4j/plugins", BindMode.READ_ONLY);

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
    void loadBundleWithOneResourceFindEntry() throws IOException {
        Map bundle = loadJsonFromFile("src/test/resources/PatientOnlyBundle.json");
        String bundleString = toJsonString(bundle);

        String fullUrl = (((ArrayList<Map>) bundle.get("entry")).get(0)).get("fullUrl").toString();
        Driver driver = getSessionDriver();

        try (Session session = driver.session()) {

            Result load = session.run("CALL cyfhir.bundle.load(" + bundleString + ")");
            Result result = session.run("MATCH (n:entry) RETURN n");

            List<Record> records = result.stream().collect(Collectors.toList());
            if (records.size() > 1) {
                fail("Too many nodes matched the query");
            } else {
                Map resultNode = records.get(0).get("n").asNode().asMap();

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

            List<Record> records = result.stream().collect(Collectors.toList());
            if (records.size() > 1) {
                fail("Too many nodes matched the query");
            } else {
                Map resultNode = records.get(0).get("n").asNode().asMap();

                /******************************/
                assertThat(resultNode).isNotNull();

                assertThat(resultNode.get("resourceType")).isNotNull();
                assertThat(resultNode.get("resourceType").toString()).isEqualTo(resourceType);

                assertThat(resultNode.get("id")).isNotNull();
                assertThat(resultNode.get("id").toString()).isEqualTo(id);
            }
        }
    }

    @Test
    void loadBundleWithManyResourcesFindPatient() throws IOException {
        Map bundle = loadJsonFromFile("src/test/resources/R4Bundle.json");
        String bundleString = toJsonString(bundle);

        Map patientEntry = ((ArrayList<Map>) bundle.get("entry")).stream().filter(entry -> {
            Map resource = (Map) entry.get("resource");
            return resource.get("resourceType").equals("Patient");
        }).collect(Collectors.toList()).get(0);

        Map patient = (Map) patientEntry.get("resource");
        String resourceType = patient.get("resourceType").toString();
        String id = patient.get("id").toString();

        Driver driver = getSessionDriver();

        try (Session session = driver.session()) {

            Result load = session.run("CALL cyfhir.bundle.load(" + bundleString + ")");
            Result result = session.run("MATCH (n:resource) WHERE n.resourceType = \"" + resourceType + "\" RETURN n");

            List<Record> records = result.stream().collect(Collectors.toList());
            if (records.size() > 1) {
                fail("Too many nodes matched the query");
            } else {
                Map resultNode = records.get(0).get("n").asNode().asMap();

                /******************************/
                assertThat(resultNode).isNotNull();

                assertThat(resultNode.get("resourceType")).isNotNull();
                assertThat(resultNode.get("resourceType").toString()).isEqualTo(resourceType);

                assertThat(resultNode.get("id")).isNotNull();
                assertThat(resultNode.get("id").toString()).isEqualTo(id);
            }
        }
    }

    @Test
    void loadBundleWithManyResourcesFindAllEntries() throws IOException {
        Map bundle = loadJsonFromFile("src/test/resources/R4Bundle.json");
        String bundleString = toJsonString(bundle);

        List<String> fullUrls = ((ArrayList<Map>) bundle.get("entry")).stream().map(entry -> {
            return (String) entry.get("fullUrl");
        }).collect(Collectors.toList());
        Collections.sort(fullUrls);

        Driver driver = getSessionDriver();

        try (Session session = driver.session()) {

            Result load = session.run("CALL cyfhir.bundle.load(" + bundleString + ")");
            Result result = session.run("MATCH (n:entry) RETURN n");

            List<Record> records = result.stream().collect(Collectors.toList());
            if (records.size() != fullUrls.size()) {
                fail("Too many nodes matched the query");
            } else {

                List<String> fullUrlsResult = records.stream().map(record -> {
                    Map<String, Object> entries = record.get("n").asNode().asMap();
                    return (String) entries.get("fullUrl");
                }).collect(Collectors.toList());
                Collections.sort(fullUrlsResult);

                /******************************/
                for (int i = 0; i < fullUrls.size(); i++) {
                    assertThat(fullUrls.get(i)).isNotNull();
                    assertThat(fullUrlsResult.get(i)).isNotNull();

                    assertThat(fullUrls.get(i)).isEqualTo(fullUrlsResult.get(i));
                }
            }
        }
    }
}