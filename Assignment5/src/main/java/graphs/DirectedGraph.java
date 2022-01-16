package graphs;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DirectedGraph<V extends Identifiable, E> {

    private final Map<String, V> vertices = new HashMap<>();
    private final Map<V, Map<V, E>> edges = new HashMap<>();

    /**
     * representation invariants:
     * 1.  the vertices map stores all vertices by their identifying id (which prevents duplicates)
     * 2.  the edges map stores all directed outgoing edges by their from-vertex and then in the nested map by their to-vertex
     * 3.  there can only be two directed edges between any two given vertices v1 and v2:
     * one from v1 to v2 in edges.get(v1).get(v2)
     * one from v2 to v1 in edges.get(v2).get(v1)
     * 4.  every vertex instance in the key-sets of edges shall also occur in the vertices map and visa versa
     **/

    public DirectedGraph() {
    }

    public Collection<V> getVertices() {
        return vertices.values();
    }

    /**
     * finds the vertex in the graph identified by the given id
     *
     * @param id the String that the vertex is identified by
     * @return the vertex that matches the given id
     * null if none of the vertices matches the id
     */
    public V getVertexById(String id) {
        return vertices.get(id);
    }

    /**
     * retrieves the collection of neighbour vertices that can be reached directly
     * via an out-going directed edge from 'fromVertex'
     *
     * @param fromVertex the vertex of which the neighbours should be found
     * @return null if fromVertex cannot be found in the graph
     * an empty collection if fromVertex has no neighbours
     */
    public Collection<V> getNeighbours(V fromVertex) {
        if (fromVertex == null || !this.edges.containsKey(fromVertex)) return null;

        // get the keys in the hashmap and return them
        return this.edges.getOrDefault(fromVertex, new HashMap<>()).keySet();
    }

    public Collection<V> getNeighbours(String fromVertexId) {
        return this.getNeighbours(this.getVertexById(fromVertexId));
    }

    /**
     * retrieves the collection of edges
     * which connects the 'fromVertex' with its neighbours
     * (only the out-going edges directed from 'fromVertex' towards a neighbour shall be included
     *
     * @param fromVertex the vertex whose edges should be found
     * @return null if fromVertex cannot be found in the graph
     * an empty collection if fromVertex has no out-going edges
     */
    public Collection<E> getEdges(V fromVertex) {
        // check if fromVertex cannot be found in the graph
        if (!this.edges.containsKey(fromVertex)) return null;

        // retrieve the collection of out-going edges which connect fromVertex with a neighbour in the edges data structure
        return this.edges.getOrDefault(fromVertex, new HashMap<>()).values();
    }

    public Collection<E> getEdges(String fromId) {
        return this.getEdges(this.getVertexById(fromId));
    }

    /**
     * Adds newVertex to the graph, if not yet present and in a way that maintains the representation invariants.
     * If a duplicate of newVertex (with the same id) already exists in the graph,
     * nothing will be added, and the existing duplicate will be kept and returned.
     *
     * @param newVertex the vertex to be added if it's id is unique
     * @return the duplicate of newVertex with the same id that already exists in the graph,
     * or newVertex itself if it has been added.
     */
    public V addOrGetVertex(V newVertex) {
        // get the vertex that is/was in this spot
        V currentVertex = this.vertices.putIfAbsent(newVertex.getId(), newVertex);

        // If currentVertex is null, then that means that there was no vertex with the same id as newVertex
        if (currentVertex == null) {
            this.edges.put(newVertex, new HashMap<>());
            return newVertex;
        }

        // a proper vertex shall be returned at all times
        return currentVertex;
    }


    /**
     * Adds a new, directed edge 'newEdge'
     * from vertex 'fromVertex' to vertex 'toVertex'
     * No change shall be made if a directed edge already exists between these vertices
     *
     * @param fromVertex the start vertex of the directed edge
     * @param toVertex   the target vertex of the directed edge
     * @param newEdge    the instance with edge information
     * @return whether the edge has been added successfully
     */
    public boolean addEdge(V fromVertex, V toVertex, E newEdge) {
        if (fromVertex == null || toVertex == null) return false;

        // make sure the fromVertex and toVertex will be in the graphs
        fromVertex = this.addOrGetVertex(fromVertex);
        toVertex = this.addOrGetVertex(toVertex);

        /*
        there can only be one directed edge from fromVertex to toVertex,
        so the edge can only be added if there was no edge before it
        */
        return this.edges.get(fromVertex).putIfAbsent(toVertex, newEdge) == null;
    }

    /**
     * Adds a new, directed edge 'newEdge'
     * from vertex with id=fromId to vertex with id=toId
     * No change shall be made if a directed edge already exists between these vertices
     *
     * @param fromId  the id of the start vertex of the outgoing edge
     * @param toId    the id of the target vertex of the directed edge
     * @param newEdge the instance with edge information
     * @return whether the edge has been added successfully
     */
    public boolean addEdge(String fromId, String toId, E newEdge) {
        V fromVertex = this.getVertexById(fromId);
        V toVertex = this.getVertexById(toId);

        // make sure the fromVertex and toVertex are in the graphs
        if (fromVertex == null || toVertex == null) return false;

        // add (directed) newEdge to the graph between fromId and toId
        return this.addEdge(fromVertex, toVertex, newEdge);
    }

    /**
     * Adds two directed edges: one from v1 to v2 and one from v2 to v1
     * both with the same edge information
     *
     * @param v1      the first vertex
     * @param v2      the second vertex
     * @param newEdge the edge to add between the first and the second and between de second and the first
     * @return whether both edges have been added
     */
    public boolean addConnection(V v1, V v2, E newEdge) {
        return this.addEdge(v1, v2, newEdge) && this.addEdge(v2, v1, newEdge);
    }

    /**
     * Adds two directed edges: one from id1 to id2 and one from id2 to id1
     * both with the same edge information
     *
     * @param id1     the id of the first vertex
     * @param id2     the id of the second vertex
     * @param newEdge the edge to add between the first and the second and between de second and the first
     * @return whether both edges have been added
     */
    public boolean addConnection(String id1, String id2, E newEdge) {
        return this.addEdge(id1, id2, newEdge) && this.addEdge(id2, id1, newEdge);
    }

    /**
     * retrieves the directed edge between 'fromVertex' and 'toVertex' from the graph, if any
     *
     * @param fromVertex the start vertex of the designated edge
     * @param toVertex   the end vertex of the designated edge
     * @return the designated directed edge that has been registered in the graph
     * returns null if no connection has been set up between these vertices in the specified direction
     */
    public E getEdge(V fromVertex, V toVertex) {
        if (fromVertex == null || toVertex == null) return null;

        // retrieve the directed edge between vertices fromVertex and toVertex from the graph, if any
        return this.edges.getOrDefault(fromVertex, new HashMap<>()).get(toVertex);
    }

    /**
     * retrieves the directed edge between 'fromVertex' and 'toVertex' from the graph, if any
     *
     * @param fromId the id of the start vertex of the designated edge
     * @param toId   the id of the end vertex of the designated edge
     * @return null if no connection has been set up between these vertices in the specified direction
     */
    public E getEdge(String fromId, String toId) {
        return this.getEdge(this.vertices.get(fromId), this.vertices.get(toId));
    }

    /**
     * @return the total number of vertices in the graph
     */
    public int getNumVertices() {
        return vertices.size();
    }

    /**
     * calculates and returns the total number of directed edges in the graph data structure
     *
     * @return the total number of edges in the graph
     */
    public int getNumEdges() {
        // calculate and return the total number of directed edges in the graph
        return this.edges.values()
                .stream()
                .mapToInt(Map::size)
                .sum();
    }

    /**
     * Remove vertices without any connection from the graph
     */
    public void removeUnconnectedVertices() {
        this.edges.entrySet().removeIf(e -> e.getValue().size() == 0);
        this.vertices.entrySet().removeIf(e -> !this.edges.containsKey(e.getValue()));
    }

    /**
     * represents a path of connected vertices and edges in the graph
     */
    public class DGPath {
        private Deque<V> vertices = new LinkedList<>();
        private double totalWeight = 0.0;
        private Set<V> visited = new HashSet<>();

        /**
         * representation invariants:
         * 1. vertices contains a sequence of vertices that are connected in the graph by a directed edge,
         * i.e. FOR ALL i: 0 < i < vertices.length: this.getEdge(vertices[i-1],vertices[i]) will provide edge information of the connection
         * 2. a path with one vertex has no edges
         * 3. a path without vertices is empty
         * totalWeight is a helper attribute to capture additional info from searches, not a fundamental property of a path
         * visited is a helper set to be able to track visited vertices in searches, not a fundamental property of a path
         **/

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(
                    String.format("Weight=%f Length=%d visited=%d (",
                            this.totalWeight, this.vertices.size(), this.visited.size()));
            String separator = "";
            for (V v : this.vertices) {
                sb.append(separator + v.getId());
                separator = ", ";
            }
            sb.append(")");
            return sb.toString();
        }

        public Queue<V> getVertices() {
            return this.vertices;
        }

        public double getTotalWeight() {
            return this.totalWeight;
        }

        public Set<V> getVisited() {
            return this.visited;
        }
    }

    /**
     * Uses a depth-first search algorithm to find a path from the start vertex to the target vertex in the graph
     * All vertices that are being visited by the search should also be registered in path.visited
     *
     * @param startId  the id of the vertex to start the depth first search from
     * @param targetId the id of the vertex to be found by the depth first search algorithm
     * @return the path from start to target
     * returns null if either start or target cannot be matched with a vertex in the graph
     * or no path can be found from start to target
     */
    public DGPath depthFirstSearch(String startId, String targetId) {

        V start = getVertexById(startId);
        V target = getVertexById(targetId);
        if (start == null || target == null) return null;

        DGPath path = new DGPath();

        // calculate the path from start to target by recursive depth-first-search
        return this.depthFirstSearch(start, target, path);
    }

    /**
     * Uses a depth-first search algorithm to find a path to the target vertex in the graph
     * All vertices that are being visited by the search should also be registered in path.visited
     *
     * @param current the vertex that is currently being visited
     * @param target  the vertex to be found
     * @param path    the DGPath that can eventually hold the path and the visited vertices
     * @return the path from start to target
     * returns null if either start or target cannot be matched with a vertex in the graph
     * or no path can be found from start to target
     */
    private DGPath depthFirstSearch(V current, V target, DGPath path) {
        // the current vertex should not be visited, because the
        if (path.visited.contains(current)) return null;
        path.visited.add(current);

        // we found a path if the target has been found
        if (current.equals(target)) {
            path.vertices.addLast(current);
            return path;
        }

        // the target has not been found yet, so we look further
        for (V neighbour : this.getNeighbours(current)) {
            if (depthFirstSearch(neighbour, target, path) != null) {
                path.vertices.addFirst(current);
                return path;
            }
        }

        // this return-statement can be reached if no path was found
        return null;
    }


    /**
     * Uses a breadth-first search algorithm to find a path from the start vertex to the target vertex in the graph
     * All vertices that are being visited by the search should also be registered in path.visited
     *
     * @param startId  the id of the vertex to start the breadth first search from
     * @param targetId the id of the vertex to find with breadth first search
     * @return the path from start to target
     * returns null if either start or target cannot be matched with a vertex in the graph
     * or no path can be found from start to target
     */
    public DGPath breadthFirstSearch(String startId, String targetId) {

        V start = getVertexById(startId);
        V target = getVertexById(targetId);
        if (start == null || target == null) return null;

        // initialise the result path of the search
        DGPath path = new DGPath();
        path.visited.add(start);
        path.vertices.addLast(target);

        // easy target
        if (start.equals(target)) {
            return path;
        }

        // calculate the path from start to target by breadth-first-search

        Queue<V> queue = new LinkedList<>();
        Map<V, V> visitedFromTo = new HashMap<>();

        queue.offer(start);
        visitedFromTo.put(start, null);

        V current = queue.poll();

        // go through the layers of the graph to try and find the target
        while (current != null) {
            for (V neighbour : this.getNeighbours(current)) {
                if (neighbour.equals(target)) {
                    while (current != null) {
                        path.vertices.addFirst(current);
                        current = visitedFromTo.get(current);
                    }
                    return path;
                } else if (!path.visited.contains(neighbour)) {
                    visitedFromTo.put(neighbour, current);
                    path.visited.add(neighbour);
                    queue.offer(neighbour);
                }
            }
            current = queue.poll();
        }

        return null;
    }

    // helper class to register the state of a vertex in dijkstra shortest path algorithm
    // your may change this class or delete it altogether follow a different approach in your implementation
    private class DSPNode implements Comparable<DSPNode> {
        protected V vertex;                // the graph vertex that is concerned with this DSPNode
        protected V fromVertex = null;     // the parent's node vertex that has an edge towards this node's vertex
        protected boolean marked = false;  // indicates DSP processing has been marked complete for this vertex
        protected double weightSumTo = Double.MAX_VALUE;   // sum of weights of current shortest path to this node's vertex

        private DSPNode(V vertex) {
            this.vertex = vertex;
        }

        // comparable interface helps to find a node with the shortest current path, sofar
        @Override
        public int compareTo(DSPNode dspv) {
            return Double.compare(weightSumTo, dspv.weightSumTo);
        }
    }

    /**
     * Calculates the edge-weighted shortest path from start to target
     * according to Dijkstra's algorithm of a minimum spanning tree
     *
     * @param startId      id of the start vertex of the search
     * @param targetId     id of the target vertex of the search
     * @param weightMapper provides a function, by which the weight of an edge can be retrieved or calculated
     * @return the shortest path from start to target
     * returns null if either start or target cannot be matched with a vertex in the graph
     * or no path can be found from start to target
     */
    public DGPath dijkstraShortestPath(String startId, String targetId,
                                       Function<E, Double> weightMapper) {

        V start = getVertexById(startId);
        V target = getVertexById(targetId);
        if (start == null || target == null) return null;

        // initialise the result path of the search
        DGPath path = new DGPath();
        path.visited.add(start);

        // easy target
        if (start.equals(target)) {
            path.vertices.add(start);
            return path;
        }

        // keep track of the DSP status of all visited nodes
        // you may choose a different approach of tracking progress of the algorithm, if you wish
        Map<V, DSPNode> progressData = new HashMap<>();

        // initialise the progress of the start node
        DSPNode nextDspNode = new DSPNode(start);
        nextDspNode.weightSumTo = 0.0;
        progressData.put(start, nextDspNode);

        while (nextDspNode != null) {
            // mark the dspNode to show that the weightSumTo of this node is the smallest possible
            nextDspNode.marked = true;

            // build and return the path when the target is marked
            if (nextDspNode.vertex.equals(target)) {
                // the path from the start to the target vertex is the same as the weightSumTo of the target vertex
                path.totalWeight = nextDspNode.weightSumTo;

                /*
                build the path by getting the fromVertex, the fromVertex of the first nextDspNode (where the
                vertex is start) is null, so the path is found when the fromVertex is null
                */
                while (nextDspNode.vertex != null) {
                    path.vertices.addFirst(nextDspNode.vertex);
                    nextDspNode.vertex = progressData.get(nextDspNode.vertex).fromVertex;
                }

                return path;
            }

            // relaxing the neighbours of the vertex in nextDspNode
            for (V neighbour : this.getNeighbours(nextDspNode.vertex)) {
                // create a new neighbourDspNode instance of the current neighbour
                DSPNode neighbourDspNode = new DSPNode(neighbour);

                /*
                set the weightSumTo to the weight of the neighbour dspNode (neighbourDspNode)
                plus the weightSumTo of the next dspNode (nextDspNode)
                 */
                neighbourDspNode.weightSumTo = nextDspNode.weightSumTo
                        + weightMapper.apply(this.getEdge(nextDspNode.vertex, neighbour));
                neighbourDspNode.fromVertex = nextDspNode.vertex;

//                progressData.merge(neighbour, neighbourDspNode, DSPNode::compareTo); // try to find a BiFunction to set the minimum;

                /*
                if the neighbour has already been added,
                then check if the current weightSumTo is smaller than the one in the map
                */
                if (progressData.getOrDefault(neighbour, neighbourDspNode).weightSumTo >= neighbourDspNode.weightSumTo)
                    progressData.put(neighbour, neighbourDspNode);
                path.visited.add(neighbour);
            }

            /*
            find the nearest node that is not marked yet and has the minimum value
            according to the compareTo of the DSPNode class
             */
            nextDspNode = progressData.values()
                    .stream()
                    .filter(dspNode -> !dspNode.marked)
                    .min(DSPNode::compareTo)
                    .orElse(null);
        }

        // no path found
        return null;
    }


    @Override
    public String toString() {
        return this.getVertices().stream()
                .map(v -> v.toString() + ": " +
                        this.edges.get(v).entrySet().stream()
                                .map(e -> e.getKey().toString() + "(" + e.getValue().toString() + ")")
                                .collect(Collectors.joining(",", "[", "]"))
                )
                .collect(Collectors.joining(",\n  ", "{ ", "\n}"));
    }
}
