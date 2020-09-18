package com.Optum.CyFHIR.models;

// Don't Optimize org.neo4j.driver imports, prevents type ambiguity

import com.Optum.CyFHIR.procedures.Resource;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.neo4j.procedure.Name;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    void testWorkingDSTU3Bundle() throws Exception {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("validation", true);
        configMap.put("version", "DSTU3");

        Map bundle = loadJsonFromFile("src/test/resources/DSTU3Bundle.json");
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
        assertThat(parsedResult).isInstanceOf(org.hl7.fhir.dstu3.model.Bundle.class);

    }

    @Test
    void testFailingDSTU3BundleToR4() throws Exception {

        Map<String, Object> configMap = new HashMap<>();
        configMap.put("validation", true);
        configMap.put("version", "R4");

        Map bundle = loadJsonFromFile("src/test/resources/DSTU3Bundle.json");
        String bundleString = toJsonString(bundle);

        String resourceType = (String) bundle.get("resourceType");
        Resource resourceClass = new Resource();
        Assertions.assertThrows(Exception.class,
                () -> resourceClass.validateFHIR(bundleString, resourceType, configMap));

    }

    @Test
    void testFailingDSTU3BundleToR5() throws Exception {

        Map<String, Object> configMap = new HashMap<>();
        configMap.put("validation", true);
        configMap.put("version", "R5");

        Map bundle = loadJsonFromFile("src/test/resources/DSTU3Bundle.json");
        String bundleString = toJsonString(bundle);

        String resourceType = (String) bundle.get("resourceType");
        Resource resourceClass = new Resource();
        Assertions.assertThrows(Exception.class,
                () -> resourceClass.validateFHIR(bundleString, resourceType, configMap));

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
    void testFailingR4BundleToDSTU3() throws Exception {

        Map<String, Object> configMap = new HashMap<>();
        configMap.put("validation", true);
        configMap.put("version", "DSTU3");

        Map bundle = loadJsonFromFile("src/test/resources/R4Bundle.json");
        String bundleString = toJsonString(bundle);

        String resourceType = (String) bundle.get("resourceType");
        Resource resourceClass = new Resource();
        Assertions.assertThrows(Exception.class,
                () -> resourceClass.validateFHIR(bundleString, resourceType, configMap));

    }

    @Test
    void testFailingR4BundleToR5() throws Exception {

        Map<String, Object> configMap = new HashMap<>();
        configMap.put("validation", true);
        configMap.put("version", "R5");

        Map bundle = loadJsonFromFile("src/test/resources/R4Bundle.json");
        String bundleString = toJsonString(bundle);

        String resourceType = (String) bundle.get("resourceType");
        Resource resourceClass = new Resource();
        Assertions.assertThrows(Exception.class,
                () -> resourceClass.validateFHIR(bundleString, resourceType, configMap));

    }

    @Test
    void testWorkingR5Bundle() throws Exception {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("validation", true);
        configMap.put("version", "R5");

        Map bundle = loadJsonFromFile("src/test/resources/R5Bundle.json");
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
        assertThat(parsedResult).isInstanceOf(org.hl7.fhir.r5.model.Bundle.class);

    }

    @Test
    void testFailingR5BundleToDSTU3() throws Exception {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("validation", true);
        configMap.put("version", "DSTU3");

        Map bundle = loadJsonFromFile("src/test/resources/R5Bundle.json");
        String bundleString = toJsonString(bundle);

        String resourceType = (String) bundle.get("resourceType");
        Resource resourceClass = new Resource();
        Assertions.assertThrows(Exception.class,
                () -> resourceClass.validateFHIR(bundleString, resourceType, configMap));

    }

    @Test
    void testFailingR5BundleToR4() throws Exception {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("validation", true);
        configMap.put("version", "R4");

        Map bundle = loadJsonFromFile("src/test/resources/R5Bundle.json");
        String bundleString = toJsonString(bundle);

        String resourceType = (String) bundle.get("resourceType");
        Resource resourceClass = new Resource();
        Assertions.assertThrows(Exception.class,
                () -> resourceClass.validateFHIR(bundleString, resourceType, configMap));

    }

}
