package com.Optum.CyFHIR.functions;

import apoc.result.MapResult;
import com.Optum.CyFHIR.procedures.toTree;
import org.neo4j.graphdb.Path;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class buildBundle {

    @UserFunction("cyfhir.buildBundle")
    @Description("cyfhir.buildBundle(List<Path>)) parses nodes into FHIR bundle given a collected Tree of connected entries")
    public Map buildBundle(@Name("mapResponse") List<Path> paths) throws IOException {

        final List<Map<String, Object>> mapResponse = toTree.toTree(paths, true, new HashMap<String, Object>())
                .map(x -> x.value)
                .collect(Collectors.toList());
        //build empty bundle
        Map bundle = new HashMap();
        if (!mapResponse.get(0).isEmpty()) {
            bundle.put("resourceType", "Bundle");
            bundle.put("type", "transaction");

            //create empty list of entries
            List<Map> entries = new ArrayList<Map>();

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
                entries.add(entryMap);
            }
            bundle.put("entry", entries);
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
                String type = ((ArrayList<Map>) resource.get(key)).get(0).get("_type").toString();
                //find if array is true array
                Boolean isArray = (Boolean) ((ArrayList<Map>) resource.get(key)).get(0).get("_isArray");
                //map flat keys if resource is not a true array
                if (!isArray) {
                    resourceMap.put(type, parseResources(((ArrayList<Map>) resource.get(key)).get(0)));
                } else {
                    //if true array map each sub resource
                    for (Map subResource : (ArrayList<Map>) resource.get(key)) {
                        //remove custom fields/data cleanup
                        subResource.remove("_type");
                        subResource.remove("_id");
                        subResource.remove("_isArray");
                        subResource.remove("_resourceId");
                        mapList.add(parseResources(subResource));
                    }
                    resourceMap.put(type, mapList);
                }
            }
            //if a flat key, then map value
            else {
                //to convert arrays stored as Strings in neo4j properties
                if (resource.get(key).toString().matches("\\[.+(, .+)*]")) {
                    resourceMap.put(key, Arrays.asList(resource.get(key).toString().substring(1, resource.get(key).toString().length() - 1).split(",")).stream().map(item -> {
                        return item.trim();
                    }));
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
