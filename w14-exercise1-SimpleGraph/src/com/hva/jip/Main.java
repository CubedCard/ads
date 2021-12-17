package com.hva.jip;

import com.hva.jip.interfaces.SimpleGraph;
import com.hva.jip.models.SUGraph;
import com.hva.jip.models.Vertex;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to the Simple Undirected Graph demo program\n");
        // Build a graph with vertices that are uniquely identified by a String identifier
        SimpleGraph<Vertex, String> graph = new SUGraph.Builder<Vertex, String>().addVertices(new Vertex("A"), new Vertex("B"), new Vertex("C"),
                        new Vertex("D"), new Vertex("E")).addEdges("A", "B", "C")
                .addEdges("B", "C", "D", "E").addEdges("C", "D", "E").addEdges("D", "E").build();
        System.out.println(graph);
        System.out.println(graph.getAdjacencyReport());
        System.out.println("isConnected = " + graph.isConnected() + "\n");
        graph.addVertex(new Vertex("E"));
        graph.addVertex(new Vertex("F"));
        graph.addVertex(new Vertex("G"));
        graph.addVertex(new Vertex("H"));
        graph.addEdge("F", "G");
        graph.addEdge("G", "H");
        graph.addEdge("H", "I"); // non-existing vertex test
        System.out.println(graph);
        System.out.println(graph.getAdjacencyReport());
        System.out.println("isConnected = " + graph.isConnected() + "\n");
    }
}
