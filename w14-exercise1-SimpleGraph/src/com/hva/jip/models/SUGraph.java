package com.hva.jip.models;

import com.hva.jip.interfaces.Identifiable;
import com.hva.jip.interfaces.SimpleGraph;
import com.hva.jip.interfaces.SimpleGraphBuilder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SUGraph<V extends Identifiable<ID>, ID> implements SimpleGraph<V, ID> {
    List<V> list = new LinkedList<>();

    @Override
    public boolean addVertex(V vertex) {
        return list.add(vertex);
    }

    @Override
    public V getVertex(ID id) {
        for (V vertex : list) {
            if (vertex.getId() == id) return vertex;
        }
        return null;
    }

    @Override
    public boolean addEdge(V vertex1, V vertex2) {
        return false;
    }

    @Override
    public boolean addEdge(ID vertexId1, ID vertexId2) {
        return false;
    }

    @Override
    public Collection<V> getVertices() {
        return list;
    }

    @Override
    public Collection<V> getNeighbours(V vertex) {
        return null;
    }

    @Override
    public Collection<V> getNeighbours(ID vertexId) {
        return null;
    }

    @Override
    public int getNumVertices() {
        return list.size();
    }

    @Override
    public int getNumEdges() {
        return 0;
    }

    @Override
    public String getAdjacencyReport() {
        return null;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    // TODO select appropriate data structures to store vertices and connections

    public static class Builder<V extends Identifiable<ID>, ID> implements SimpleGraphBuilder<V, ID> {
        // create a num graph under construction
        private SUGraph<V, ID> suGraph = new SUGraph();

        @Override
        public SimpleGraph<V, ID> build() {
            return suGraph;
        }

        public Builder<V, ID> addVertices(V... vertices) {
            for (V vertex : vertices) {
                suGraph.addVertex(vertex);
            }
            return this;
        }

        public Builder<V, ID> addEdges(ID... ids) {
            for (int i = 0; i < ids.length - 1; i++) {
                suGraph.addEdge(ids[i], ids[i + 1]);
            }
            return this;
        }

        // TODO implement builder methods
    }

    @Override
    public String toString() {
        return String.format("%s", list);
    }
}