package kShortestPaths;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import Graph.Edge;
import Graph.Graph;
import Graph.Vertex;

public class KShortestPaths {
    private Graph graph;
    private Vertex source;
    private Vertex sink;
    private int K;

    public KShortestPaths() {
    }

    public KShortestPaths(Graph graph, Vertex source, Vertex sink, int K) {
        this.graph = graph;
        this.source = source;
        this.sink = sink;
        this.K = K;
    }

    public Graph getGraph() {
        return this.graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public Vertex getSource() {
        return this.source;
    }

    public void setSource(Vertex source) {
        this.source = source;
    }

    public Vertex getSink() {
        return this.sink;
    }

    public void setSink(Vertex sink) {
        this.sink = sink;
    }

    public List<List<Edge>> getKShortestPaths() {
        // Initialization

        List<Edge> orderedCapacities = new ArrayList<>();
        List<Optional<Path>> shortestPath = new ArrayList<>();

        Map<Vertex, Set<Edge>> adjList = this.graph.getAdjacencyList();

        for (Vertex vertex : adjList.keySet()) {
            adjList.get(vertex).forEach(edge -> orderedCapacities.add(edge));
        }

        // Order are capacities : c1, ..., cr
        orderedCapacities.sort(Comparator.comparing(Edge::getWeight));

        // A<-A'
        Map<Vertex, Set<Edge>> subAdjList = new HashMap<>(adjList);

        for (int l = 0; l < orderedCapacities.size(); l++) {
            int capacity = orderedCapacities.get(l).getWeight();

            // A' <- {(i, j) in A'| cij >= cl}
            for (Vertex vertex : subAdjList.keySet()) {
                Set<Edge> edges = adjList.get(vertex);
                edges.forEach(edge -> {
                    if (edge.getWeight() < capacity) {
                        edges.remove(edge);
                    }
                });
            }

            // L[l] <- the o-d shortest loopless path in D' = (V,A'), null otherwise
            UCS ucs = new UCS(subAdjList, source, sink);
            shortestPath.add(ucs.getShortestPath());
        }

        // Find K-Quickest Paths

        return new ArrayList<>();
    }
}
