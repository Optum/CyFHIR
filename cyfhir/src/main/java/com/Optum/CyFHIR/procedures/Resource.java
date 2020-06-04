package com.Optum.CyFHIR.procedures;



import apoc.path.LabelSequenceEvaluator;
import apoc.path.NodeEvaluators;
import apoc.path.RelationshipSequenceExpander;
import apoc.result.PathResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.traversal.*;
import org.neo4j.internal.helpers.collection.Iterables;
import org.neo4j.kernel.impl.traversal.MonoDirectionalTraversalDescription;
import org.neo4j.procedure.*;

import java.util.*;
import java.util.stream.Stream;

public class Resource {
    public static final Uniqueness UNIQUENESS = Uniqueness.RELATIONSHIP_PATH;
    public static final boolean BFS = true;
    @Context
    public GraphDatabaseService db;

    @Procedure(name = "cyfhir.resource.expand", mode = Mode.READ)
    @Description("cyfhir.resource.expand(startNode Node) yield path - expand from entry node following the given relationships from min to max-levelof the FHIR resource, stopping at references")
    public Stream<PathResult> expand(@Name("start") Object start) throws Exception {
        Transaction tx = db.beginTx();
        List<Node> nodes = startToNodes(start, tx);
        Stream<PathResult> entry = explorePathPrivate(nodes, ">|relationship", "-entry", 0, 999, BFS, UNIQUENESS, false, -1, null, null, true, tx).map(PathResult::new);

        tx.commit();
        tx.close();
        return entry;
    }


    @SuppressWarnings("unchecked")
    private List<Node> startToNodes(Object start, Transaction tx) throws Exception {
        if (start == null) return Collections.emptyList();
        if (start instanceof Node) {
            return Collections.singletonList((Node) start);
        }
        if (start instanceof Number) {
            return Collections.singletonList(tx.getNodeById(((Number) start).longValue()));
        }
        if (start instanceof List) {
            List list = (List) start;
            if (list.isEmpty()) return Collections.emptyList();

            Object first = list.get(0);
            if (first instanceof Node) return (List<Node>)list;
            if (first instanceof Number) {
                List<Node> nodes = new ArrayList<>();
                for (Number n : ((List<Number>)list)) nodes.add(tx.getNodeById(n.longValue()));
                return nodes;
            }
        }
        throw new Exception("Unsupported data type for start parameter a Node or an Identifier (long) of a Node must be given!");
    }

    private Stream<Path> explorePathPrivate(Iterable<Node> startNodes,
                                            String pathFilter,
                                            String labelFilter,
                                            long minLevel,
                                            long maxLevel,
                                            boolean bfs,
                                            Uniqueness uniqueness,
                                            boolean filterStartNode,
                                            long limit,
                                            EnumMap<NodeFilter, List<Node>> nodeFilter,
                                            String sequence,
                                            boolean beginSequenceAtStart,
                                            Transaction tx) {

        Traverser traverser = traverse(tx.traversalDescription(), startNodes, pathFilter, labelFilter, minLevel, maxLevel, uniqueness, bfs, filterStartNode, nodeFilter, sequence, beginSequenceAtStart);

        if (limit == -1) {
            return Iterables.stream(traverser);
        } else {
            return Iterables.stream(traverser).limit(limit);
        }
    }

    public Traverser traverse(TraversalDescription traversalDescription,
                                     Iterable<Node> startNodes,
                                     String pathFilter,
                                     String labelFilter,
                                     long minLevel,
                                     long maxLevel,
                                     Uniqueness uniqueness,
                                     boolean bfs,
                                     boolean filterStartNode,
                                     EnumMap<NodeFilter, List<Node>> nodeFilter,
                                     String sequence,
                                     boolean beginSequenceAtStart) {
        TraversalDescription td = traversalDescription;
        // based on the pathFilter definition now the possible relationships and directions must be shown

        td = bfs ? td.breadthFirst() : td.depthFirst();

        // if `sequence` is present, it overrides `labelFilter` and `relationshipFilter`
        if (sequence != null && !sequence.trim().isEmpty())	{
            String[] sequenceSteps = sequence.split(",");
            List<String> labelSequenceList = new ArrayList<>();
            List<String> relSequenceList = new ArrayList<>();

            for (int index = 0; index < sequenceSteps.length; index++) {
                List<String> seq = (beginSequenceAtStart ? index : index - 1) % 2 == 0 ? labelSequenceList : relSequenceList;
                seq.add(sequenceSteps[index]);
            }

            td = td.expand(new RelationshipSequenceExpander(relSequenceList, beginSequenceAtStart));
            td = td.evaluator(new LabelSequenceEvaluator(labelSequenceList, filterStartNode, beginSequenceAtStart, (int) minLevel));
        } else {
            if (pathFilter != null && !pathFilter.trim().isEmpty()) {
                td = td.expand(new RelationshipSequenceExpander(pathFilter.trim(), beginSequenceAtStart));
            }

            if (labelFilter != null && sequence == null && !labelFilter.trim().isEmpty()) {
                td = td.evaluator(new LabelSequenceEvaluator(labelFilter.trim(), filterStartNode, beginSequenceAtStart, (int) minLevel));
            }
        }

        if (minLevel != -1) td = td.evaluator(Evaluators.fromDepth((int) minLevel));
        if (maxLevel != -1) td = td.evaluator(Evaluators.toDepth((int) maxLevel));


        if (nodeFilter != null && !nodeFilter.isEmpty()) {
            List<Node> endNodes = nodeFilter.getOrDefault(NodeFilter.END_NODES, Collections.EMPTY_LIST);
            List<Node> terminatorNodes = nodeFilter.getOrDefault(NodeFilter.TERMINATOR_NODES, Collections.EMPTY_LIST);
            List<Node> blacklistNodes = nodeFilter.getOrDefault(NodeFilter.BLACKLIST_NODES, Collections.EMPTY_LIST);
            List<Node> whitelistNodes;

            if (nodeFilter.containsKey(NodeFilter.WHITELIST_NODES)) {
                // need to add to new list since we may need to add to it later
                // encounter "can't add to abstractList" error if we don't do this
                whitelistNodes = new ArrayList<>(nodeFilter.get(NodeFilter.WHITELIST_NODES));
            } else {
                whitelistNodes = Collections.EMPTY_LIST;
            }

            if (!blacklistNodes.isEmpty()) {
                td = td.evaluator(NodeEvaluators.blacklistNodeEvaluator(filterStartNode, (int) minLevel, blacklistNodes));
            }

            Evaluator endAndTerminatorNodeEvaluator = NodeEvaluators.endAndTerminatorNodeEvaluator(filterStartNode, (int) minLevel, endNodes, terminatorNodes);
            if (endAndTerminatorNodeEvaluator != null) {
                td = td.evaluator(endAndTerminatorNodeEvaluator);
            }

            if (!whitelistNodes.isEmpty()) {
                // ensure endNodes and terminatorNodes are whitelisted
                whitelistNodes.addAll(endNodes);
                whitelistNodes.addAll(terminatorNodes);
                td = td.evaluator(NodeEvaluators.whitelistNodeEvaluator(filterStartNode, (int) minLevel, whitelistNodes));
            }
        }

        td = td.uniqueness(uniqueness); // this is how Cypher works !! Uniqueness.RELATIONSHIP_PATH
        // uniqueness should be set as last on the TraversalDescription
        return td.traverse(startNodes);
    }

    // keys to node filter map
    enum NodeFilter {
        WHITELIST_NODES,
        BLACKLIST_NODES,
        END_NODES,
        TERMINATOR_NODES
    }

}