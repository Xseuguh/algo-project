package kShortestPaths;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Map.Entry;

import Graph.Edge;
import Graph.Graph;
import Graph.Vertex;

public class UCS {
    private Map<Vertex, Set<Edge>> graph;
    private Vertex source;
    private Vertex sink;

    public UCS() {
    }

    public UCS(Map<Vertex, Set<Edge>> graph, Vertex source, Vertex sink) {
        this.graph = graph;
        this.source = source;
        this.sink = sink;
    }

    public Map<Vertex, Set<Edge>> getGraph() {
        return this.graph;
    }

    public void setGraph(Map<Vertex, Set<Edge>> graph) {
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

    public Optional<Path> getShortestPath() {
        List<Vertex> visitedVertex = new ArrayList();
        PriorityQueue<Path> queue = new PriorityQueue<>(Comparator.comparing(Path::getTotalWeight));

        while (!visitedVertex.isEmpty()) {
            Path state = queue.poll();

            Vertex currentVertex = state.getLastVertex();

            if (currentVertex == this.sink)
                return Optional.of(state);

            Set<Edge> neighbors = this.graph.get(currentVertex);

            neighbors.forEach(edge -> {
                Vertex edgeDestination = edge.getDestination();
                int edgeWeight = edge.getWeight();

                if (!visitedVertex.contains(edgeDestination)) {
                    List<Vertex> newVertexPath = new ArrayList<>(state.getPath());
                    newVertexPath.add(edgeDestination);

                    Path newPath = new Path(newVertexPath, state.getTotalWeight() + edgeWeight);
                    queue.add(newPath);
                }
            });
        }

        return null;
    }
}
