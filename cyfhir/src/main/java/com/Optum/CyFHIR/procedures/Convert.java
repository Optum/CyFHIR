package com.Optum.CyFHIR.procedures;

import apoc.util.Util;
import apoc.result.MapResult;
import apoc.convert.ConvertConfig;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Entity;
import org.neo4j.graphdb.Relationship;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;
import org.neo4j.procedure.Description;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Convert {

    @Procedure(name = "cyfhir.convert.toTree")
    @Description("cyfhir.convert.toTree([paths],[lowerCaseRels=true], [config]) creates a stream of nested documents representing the at least one root of these paths")
    public Stream<MapResult> toTree(@Name("paths") List<Path> paths,
            @Name(value = "lowerCaseRels", defaultValue = "true") boolean lowerCaseRels,
            @Name(value = "config", defaultValue = "{}") Map<String, Object> config) {
        if (paths.isEmpty())
            return Stream.of(new MapResult(Collections.emptyMap()));
        ConvertConfig conf = new ConvertConfig(config);
        Map<String, List<String>> nodes = conf.getNodes();
        Map<String, List<String>> rels = conf.getRels();

        Map<Long, Map<String, Object>> maps = new HashMap<>(paths.size() * 100);
        for (Path path : paths) {
            Iterator<Entity> it = path.iterator();
            while (it.hasNext()) {
                Node n = (Node) it.next();
                Map<String, Object> nMap = maps.computeIfAbsent(n.getId(), (id) -> toMap(n, nodes));
                if (it.hasNext()) {
                    Relationship r = (Relationship) it.next();
                    Node m = r.getOtherNode(n);
                    String typeName = lowerCaseRels ? r.getType().name().toLowerCase() : r.getType().name();
                    // parent-[:HAS_CHILD]->(child) vs. (parent)<-[:PARENT_OF]-(child)
                    if (!nMap.containsKey(typeName))
                        nMap.put(typeName, new ArrayList<>(16));
                    List<Map<String, Object>> list = (List) nMap.get(typeName);
                    Optional<Map<String, Object>> optMap = list.stream()
                            .filter(elem -> elem.get("_id").equals(m.getId())).findFirst();
                    if (!optMap.isPresent()) {
                        Map<String, Object> mMap = toMap(m, nodes);
                        mMap = addRelProperties(mMap, typeName, r, rels);
                        maps.put(m.getId(), mMap);
                        list.add(maps.get(m.getId()));
                    }
                }
            }
        }
        return paths.stream().map(Path::startNode).distinct().map(n -> maps.remove(n.getId()))
                .map(m -> m == null ? Collections.<String, Object> emptyMap() : m).map(MapResult::new);
    }

    private Map<String, Object> addRelProperties(Map<String, Object> mMap, String typeName, Relationship r,
            Map<String, List<String>> relFilters) {
        Map<String, Object> rProps = r.getAllProperties();
        if (rProps.isEmpty())
            return mMap;
        String prefix = typeName + ".";
        if (relFilters.containsKey(typeName)) {
            rProps = filterProperties(rProps, relFilters.get(typeName));
        }
        rProps.forEach((k, v) -> mMap.put(prefix + k, v));
        return mMap;
    }

    private Map<String, Object> filterProperties(Map<String, Object> props, List<String> filters) {
        boolean isExclude = filters.get(0).startsWith("-");

        return props.entrySet().stream()
                .filter(e -> isExclude ? !filters.contains("-" + e.getKey()) : filters.contains(e.getKey()))
                .collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue()));
    }

    private Map<String, Object> toMap(Node n, Map<String, List<String>> nodeFilters) {
        Map<String, Object> props = n.getAllProperties();
        Map<String, Object> result = new LinkedHashMap<>(props.size() + 2);
        String type = Util.labelString(n);
        result.put("_id", n.getId());
        result.put("_type", type);
        if (nodeFilters.containsKey(type)) { // Check if list contains LABEL
            props = filterProperties(props, nodeFilters.get(type));
        }
        result.putAll(props);
        return result;
    }

}
