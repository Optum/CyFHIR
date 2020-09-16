package com.Optum.CyFHIR.models;

// Don't Optimize org.neo4j.driver imports, prevents type ambiguity

import com.Optum.CyFHIR.procedures.Resource;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.procedure.Name;
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

public class ValidatorTest {

    Map loadJsonFromFile(String location) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<?, ?> map = mapper.readValue(Paths.get(location).toFile(), Map.class);
        return map;
    }

    String toJsonString(Map map) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(map);
    }

    public Map<String, Object> stringToMap(@Name("jsonString") String jsonString) throws IOException {
        // Create JSON mapper
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        // Convert JSON string to Map
        Map<String, Object> map = mapper.readValue(jsonString, Map.class);

        return map;
    }

    @Test
    void testWorkingDSTU3Bundle() throws Exception {

    }

    @Test
    void testFailingDSTU3Bundle() throws Exception {

    }

    @Test
    void testWorkingR4Bundle() throws Exception {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("validation", true);
        configMap.put("version", "R4");

        Map bundle = loadJsonFromFile("src/test/resources/R4Bundle.json");
        String bundleString = toJsonString(bundle);

        String resourceType = (String) bundle.get("resourceType");
        Resource resourceClass = new Resource();
        IAnyResource parsedResult = resourceClass.validateFHIR(bundleString, resourceType, configMap);

        String className = parsedResult.getClass().getName();
        String parsedResourceType = className.substring(className.lastIndexOf('.') + 1);
        /******************************/
        assertThat(parsedResourceType).isNotNull();
        assertThat(resourceType).isNotNull();

        assertThat(parsedResourceType).isEqualTo(resourceType);
        assertThat(parsedResult).isInstanceOf(org.hl7.fhir.r4.model.Bundle.class);

    }

    @Test
    void testFailingR4Bundle() throws Exception {

        Map<String, Object> configMap = new HashMap<>();
        configMap.put("validation", true);
        configMap.put("version", "R4");

        Map bundle = loadJsonFromFile("src/test/resources/R4Bundle.json");
        String bundleString = toJsonString(bundle);

        String resourceType = (String) bundle.get("resourceType");
        Resource resourceClass = new Resource();
        IAnyResource parsedResult = resourceClass.validateFHIR(bundleString, resourceType, configMap);

        String className = parsedResult.getClass().getName();
        String parsedResourceType = className.substring(className.lastIndexOf('.') + 1);
        /******************************/
        assertThat(parsedResourceType).isNotNull();
        assertThat(resourceType).isNotNull();

        assertThat(parsedResourceType).isEqualTo(resourceType);
        assertThat(parsedResult).isInstanceOf(org.hl7.fhir.r4.model.Bundle.class);

    }

    @Test
    void testWorkingR5Bundle() throws Exception {

    }

    @Test
    void testFailingR5Bundle() throws Exception {

    }

}
