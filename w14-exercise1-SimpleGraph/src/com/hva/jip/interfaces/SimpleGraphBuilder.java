package com.hva.jip.interfaces;

public interface SimpleGraphBuilder<V extends Identifiable<ID>, ID> { // add multiple vertices to the graph under construction SimpleGraphBuilder<V,ID> addVertices(V ...vertices);
    // connect multiple existing vertices as new neighbours
    // to an existing vertex in the graph under construction SimpleGraphBuilder<V,ID> addEdges(V vertex, V ...neighbours); SimpleGraphBuilder<V,ID> addEdges(ID vertexId, ID ...neighbourIds);
    // complete and deliver the groph under construction
    SimpleGraph<V, ID> build();
}