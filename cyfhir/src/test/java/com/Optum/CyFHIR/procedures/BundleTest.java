package com.Optum.CyFHIR.procedures;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.*;
import org.neo4j.driver.types.Node;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class BundleTest {

    @Container
    public GenericContainer neo4j = new GenericContainer<>("neo4j:4.1.0")
            .withEnv("NEO4J_AUTH", "neo4j/password")
            .withEnv("NEO4J_dbms_security_procedures_unrestricted", "cyfhir.*,apoc.*")
            .withFileSystemBind("./plugins", "/var/lib/neo4j/plugins", BindMode.READ_ONLY)
            .withExposedPorts(7687);

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void load() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<?, ?> map = mapper.readValue(Paths.get("src/test/resources/PatientOnlyBundle.json").toFile(), Map.class);
        String json = mapper.writeValueAsString(map);
        String stringifiedJson = mapper.writeValueAsString(json);

        String uri = "bolt://" + neo4j.getContainerIpAddress() + ":" + neo4j.getMappedPort(7687);
        Driver driver = GraphDatabase.driver(uri, AuthTokens.basic("neo4j", "password"));

        try (Session session = driver.session()) {
            Result load = session.run("CALL cyfhir.bundle.load(" + stringifiedJson + ")");
            Result result = session.run("MATCH (n) RETURN n");

            result.stream().forEach(res -> {
                Node node = res.get("n").asNode();
                System.out.println(node.asMap().toString());
            });
            assertThat(result).isNotNull();
        }
    }

    @Test
    void stringToMap() {
    }

    @Test
    void getRelationshipsFromMap() {
    }

    @Test
    void nodeRecursion() {
    }
}