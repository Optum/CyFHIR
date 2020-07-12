package com.Optum.CyFHIR.models;

import org.neo4j.graphdb.Node;

public class FhirRelationship {
    Node parentNode;
    String uuidChild;
    String relationType;

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node node) {
        parentNode = node;
    }

    public String getChildUUID() {
        return uuidChild;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setChildRelationship(String uuid, String relation) {
        uuidChild = uuid;
        relationType = relation;
    }
}
