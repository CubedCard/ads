package com.hva.jip.interfaces;

import java.util.Collection;

public interface SimpleGraph<V extends Identifiable<ID>, ID> {
    // vertices can be any class that can be identified by a value of any type ID // the Identifiable interface requires implementation of a getId() method
// adds the vertex if not already present; returns whether at least one new vertex was added
    boolean addVertex(V vertex);

    // retrieves the vertex specified by identifier id
    V getVertex(ID id);

    // adds an edge if not already present; returns whether a new edge was added
    boolean addEdge(V vertex1, V vertex2);

    boolean addEdge(ID vertexId1, ID vertexId2);

    // returns all vertices in the graph
    Collection<V> getVertices();

    // returns all neighbours connected to the given vertex
    Collection<V> getNeighbours(V vertex);

    Collection<V> getNeighbours(ID vertexId);

    // returns the total number of vertices in the graph
    int getNumVertices();

    // returns the total number of edges in the graph
    int getNumEdges();

    // produces a formatted string representation of the complete graph
    String getAdjacencyReport();

    // calculates whether the graph is connected
    boolean isConnected();
}