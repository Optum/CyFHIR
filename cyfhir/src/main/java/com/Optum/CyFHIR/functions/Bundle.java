package com.Optum.CyFHIR.functions;

import com.Optum.CyFHIR.procedures.Convert;
import org.neo4j.graphdb.Path;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class Bundle {

    @UserFunction("cyfhir.bundle.format")
    @Description("cyfhir.bundle.format(List<Path>)) parses nodes into FHIR bundle given a collected Tree of connected entries")
    public Map format(@Name("paths") List<Path> paths) {

        Convert tree = new Convert();
        List<Map<String, Object>> mapResponse = tree.toTree(paths, true, new HashMap<String, Object>())
                .map(x -> x.value).collect(Collectors.toList());

        // build empty bundle
        Map bundle = new HashMap();
        bundle.put("resourceType", "Bundle");
        bundle.put("type", "transaction");
        List<Map> entriesList = new ArrayList<Map>();
        if (!mapResponse.get(0).isEmpty()) {
            // traverse each entry in the mapResponse and map with parseResource
            for (Map entry : mapResponse) {
                Resource entryFormatter = new Resource();
                Map entryMap = new HashMap();
                // map the resource
                entryMap.put("resource",
                        entryFormatter.parseResources((Map) ((ArrayList) entry.get("resource")).get(0)));
                // map the request
                entryMap.put("request", entryFormatter.parseResources((Map) ((ArrayList) entry.get("request")).get(0)));
                // map the fullUrl
                entryMap.put("fullUrl", entry.get("fullUrl"));
                // add parsed entry to list of entries
                entriesList.add(entryMap);
            }
        }
        bundle.put("entry", entriesList);
        return bundle;
    }
}
