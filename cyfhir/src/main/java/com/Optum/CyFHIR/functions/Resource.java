package com.Optum.CyFHIR.functions;

import com.Optum.CyFHIR.procedures.Convert;
import org.neo4j.graphdb.Path;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.UserFunction;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class Resource {

    @UserFunction("cyfhir.resource.format")
    @Description("cyfhir.resource.format(List<Path>)) parses nodes into FHIR Resource given a collected Tree of connected entries")
    public Map format(@Name("paths") List<Path> paths) {
        Convert tree = new Convert();
        List<Map<String, Object>> mapResponse = tree.toTree(paths, true, new HashMap<String, Object>())
                .map(x -> x.value).collect(Collectors.toList());

        Map resource = new HashMap();
        if (mapResponse.size() == 1) {
            Map resourceMap = mapResponse.get(0);
            resource = parseResources(resourceMap);
        }
        return resource;
    }

    // Convert tree response to FHIR via this algorithm using the previously injected metadata
    public Map parseResources(Map resource) {
        resource = removeMetadata(resource);
        Map resourceMap = new HashMap();
        // traverse resource keys and map values accordingly
        for (Object key : resource.keySet()) {
            Object keyValue = resource.get(key);
            if (keyValue instanceof ArrayList) {
                ArrayList<Map> mapList = new ArrayList<>();
                Map nestedData = ((ArrayList<Map>) keyValue).get(0);
                String type = nestedData.get("_type").toString();

                Boolean isArray = (Boolean) nestedData.get("_isArray");
                // map flat keys if resource is not a true array
                if (!isArray) {
                    resourceMap.put(type, parseResources(nestedData));
                } else {
                    // if true array map each sub resource
                    for (Map subResource : (ArrayList<Map>) keyValue) {
                        subResource = removeMetadata(subResource);
                        Map subResourceParsed = parseResources(subResource);
                        mapList.add(subResourceParsed);
                    }
                    resourceMap.put(type, mapList);
                }
            }
            // if a flat key, then map value
            else {
                // to convert arrays stored as Strings in neo4j properties
                if (keyValue.toString().matches("\\[.+(, .+)*]")) {
                    List<String> strings = Arrays
                            .asList(keyValue.toString().substring(1, keyValue.toString().length() - 1).split(","));
                    List<Serializable> serializedArray = strings.stream().map(item -> parseArrayString(item))
                            .collect(Collectors.toList());
                    resourceMap.put(key, serializedArray);
                } else {
                    resourceMap.put(key, keyValue);
                }
            }
        }
        return resourceMap;
    }

    // remove metadata fields/data cleanup
    public Map removeMetadata(Map resource) {
        resource.remove("_resourceId");
        resource.remove("_id");
        resource.remove("_type");
        resource.remove("_isArray");
        return resource;
    }

    // handle arrays of numerical and textual types
    public Serializable parseArrayString(String item) {
        String trimmed = item.trim();
        if (NumberUtils.isCreatable(trimmed)) {
            return NumberUtils.createNumber(trimmed);
        }
        return trimmed;
    }
}
