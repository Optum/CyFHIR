package com.Optum.CyFHIR.models;

import org.neo4j.graphdb.Node;

import java.util.ArrayList;

public class FhirRecursiveObj {
    Node node;
    ArrayList<FhirRelationship> relationships;

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public ArrayList<FhirRelationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(ArrayList<FhirRelationship> relationships) {
        this.relationships = relationships;
    }
}
