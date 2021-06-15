package Graph;

import java.util.*;

public class Graph {
    // Factor to convert in meter: 1 nautical mile * 60 (to pass in degree)
    private static final double CONST_CONVERTING_FACTOR = 1852 * 60;

    private int numberOfVertices;
    private Map<Vertex, Set<Edge>> adjacencyList;
    private Set<Set<Edge>> graphEdges;

    private static final Comparator<Edge> EDGE_COMPARATOR = Comparator.comparing(e -> e.getDestination().getId());

    public Graph(List<Vertex> vertices) {
        this.numberOfVertices = vertices.size();
        this.graphEdges = new TreeSet<Set<Edge>>();
        adjacencyList = new HashMap<>();
        fillAdjacencyListWithEmptyList(vertices);
    }

    public Graph(Map<Vertex, Set<Edge>> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public Optional<Edge> findEdge(Vertex source, Vertex destination) {
        return adjacencyList.get(source).stream().filter(edge -> edge.getDestination() == destination).findFirst();
    }

    public void removeEdge(Vertex source, Vertex destination) {
        Set<Edge> edges = this.adjacencyList.get(source);

        Optional<Edge> edge = findEdge(source, destination);
        if (edge.isPresent())
            edges.remove(edge.get());
    }

    public void removeVertex(Vertex removedVertex) {
        Map<Set<Edge>, Edge> deletedEdges = new HashMap<>();
        this.adjacencyList.remove(removedVertex);
        this.adjacencyList.forEach((vertex, edges) -> {
            edges.forEach(edge -> {
                if (edge.getDestination() == removedVertex)
                    deletedEdges.put(edges, edge);
            });
        });

        for (Set<Edge> edges : deletedEdges.keySet()) {
            edges.remove(deletedEdges.get(edges));
        }
    }

    private void fillAdjacencyListWithEmptyList(List<Vertex> vertices) {
        for (Vertex vertex : vertices) {
            adjacencyList.put(vertex, new TreeSet<>(EDGE_COMPARATOR));
        }
    }

    private int weightBetweenVertices(Vertex source, Vertex destination) {
        double sLatDeg = source.getLatitude();
        double sLongDeg = source.getLongitude();

        double dLatDeg = destination.getLatitude();
        double dLongDeg = destination.getLongitude();

        // Approximation by Pythagore
        double x = (sLongDeg - dLongDeg) * Math.cos(Math.toRadians((sLatDeg + dLatDeg) / 2));
        double y = sLatDeg - dLatDeg;

        double pythagoreDistance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) * CONST_CONVERTING_FACTOR;

        return (int) Math.round(pythagoreDistance);
    }

    public Map<Vertex, Set<Edge>> getAdjacencyList() {
        return this.adjacencyList;
    }

    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    public Set<Set<Edge>> getGraphEdges() {
        return this.graphEdges;
    }

    public void addEdge(Vertex source, Vertex destination) {
        int weight = weightBetweenVertices(source, destination);

        addEdgeWithWeight(source, destination, weight);
    }

    public void addEdgeWithWeight(Vertex source, Vertex destination, int weight) {
        Edge edgeFromSourceToDestination = new Edge(weight, source, destination);
        this.adjacencyList.get(source).add(edgeFromSourceToDestination);

        // As our graph is undirected, we can add the reversed edge
        Edge edgeFromDestinationToSource = new Edge(weight, destination, source);
        this.adjacencyList.get(destination).add(edgeFromDestinationToSource);

        // this.graphEdges.add(List.of(edgeFromSourceToDestination,edgeFromDestinationToSource));
    }

    public void setGraphEdges() {
        Set<Set<Edge>> graphEdgesTemp = new HashSet<Set<Edge>>();
        for (Vertex v : adjacencyList.keySet()) {
            for (Edge e : adjacencyList.get(v)) {
                Set<Edge> pair = new HashSet<Edge>();
                pair.add(e);
                for (Edge edge : adjacencyList.get(e.getDestination())) {
                    if (edge.getDestination().equals(e.getSource())) {
                        pair.add(edge);
                        break;
                    }
                }
                graphEdgesTemp.add(pair);
            }
        }
        this.graphEdges = graphEdgesTemp;
    }

    public void displayAdjacencyList() {
        for (Vertex station : adjacencyList.keySet()) {
            Set<Edge> neighbors = adjacencyList.get(station);
            System.out.println("The station " + station.getName() + " has " + neighbors.size() + " neighbors:");
            for (Edge edge : neighbors) {
                Vertex neighbor = edge.getDestination();
                System.out.println("\t- " + neighbor.getName());
            }
        }
    }
}
