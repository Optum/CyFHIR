package com.Optum.CyFHIR.functions;

import com.Optum.CyFHIR.procedures.Convert;
import org.apache.commons.lang3.math.NumberUtils;
import org.neo4j.graphdb.Path;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Bundle {

    @UserFunction("cyfhir.bundle.build")
    @Description("cyfhir.bundle.build(List<Path>)) parses nodes into FHIR bundle given a collected Tree of connected entries")
    public Map build(@Name("mapResponse") List<Path> paths) throws IOException {

        Convert tree = new Convert();
        List<Map<String, Object>> mapResponse = tree.toTree(paths, true, new HashMap<String, Object>())
                .map(x -> x.value)
                .collect(Collectors.toList());

        //build empty bundle
        Map bundle = new HashMap();
        if (!mapResponse.get(0).isEmpty()) {
            bundle.put("resourceType", "Bundle");
            bundle.put("type", "transaction");

            //create empty list of entries
            List<Map> entriesList = new ArrayList<Map>();

            //traverse each entry in the mapResponse and map with parseResource
            for (Map entry : mapResponse) {
                Map entryMap = new HashMap();
                //map the resource
                entryMap.put("resource", parseResources((Map) ((ArrayList) entry.get("resource")).get(0)));
                //map the request
                entryMap.put("request", parseResources((Map) ((ArrayList) entry.get("request")).get(0)));
                //map the fullUrl
                entryMap.put("fullUrl", entry.get("fullUrl"));
                //add parsed entry to list of entries
                entriesList.add(entryMap);
            }
            bundle.put("entry", entriesList);
        }
        return bundle;
    }

    public Map parseResources(Map resource) {
        //remove custom fields/data cleanup
        resource.remove("_resourceId");
        resource.remove("_id");
        resource.remove("_type");
        resource.remove("_isArray");
        Map resourceMap = new HashMap();
        //traverse resource keys and map values accordingly
        for (Object key : resource.keySet()) {
            //map each nested resources if within an array - although
            if (resource.get(key) instanceof ArrayList) {
                ArrayList<Map> mapList = new ArrayList<Map>();
                //extract type field to set as the key
                Map nestedData = ((ArrayList<Map>) resource.get(key)).get(0);
                String type = nestedData.get("_type").toString();
                //find if array is true array
                Boolean isArray = (Boolean) nestedData.get("_isArray");
                //map flat keys if resource is not a true array
                if (!isArray) {
                    resourceMap.put(type, parseResources(nestedData));
                } else {
                    //if true array map each sub resource
                    for (Map subResource : (ArrayList<Map>) resource.get(key)) {
                        //remove custom fields/data cleanup
                        subResource.remove("_type");
                        subResource.remove("_id");
                        subResource.remove("_isArray");
                        subResource.remove("_resourceId");
                        Map subResourceParsed = parseResources(subResource);
                        mapList.add(subResourceParsed);
                    }
                    resourceMap.put(type, mapList);
                }
            }
            //if a flat key, then map value
            else {
                //to convert arrays stored as Strings in neo4j properties
                if (resource.get(key).toString().matches("\\[.+(, .+)*]")) {
                    List<String> strings = Arrays.asList(resource.get(key).toString().substring(1, resource.get(key).toString().length() - 1).split(","));
                    List<Serializable> stringStream = strings.stream().map(item -> {
                        String trimmed = item.trim();
                        if (NumberUtils.isCreatable(trimmed)) {
                            return NumberUtils.createNumber(trimmed);
                        }
                        return trimmed;
                    }).collect(Collectors.toList());
                    resourceMap.put(key, stringStream);
                }
                //map string, long, double, and boolean values
                else {
                    resourceMap.put(key, resource.get(key));
                }
            }
        }
        return resourceMap;
    }
}
