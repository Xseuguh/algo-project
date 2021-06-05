package Graph;

import java.util.*;

public class Graph {
    //Factor to convert in meter:  1 nautical mile * 60 (to pass in degree)
    private static final double CONST_CONVERTING_FACTOR = 1852 * 60;

    private int numberOfVertices;
    private Map<Vertex, Set<Edge>> adjacencyList;

    private static final Comparator<Edge> EDGE_COMPARATOR = Comparator.comparing(e -> e.getDestination().getId());

    public Graph(List<Vertex> vertices) {
        this.numberOfVertices = vertices.size();
        adjacencyList = new HashMap<>();
        fillAdjacencyListWithEmptyList(vertices);
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


        //Approximation by Pythagore
        double x = (sLongDeg - dLongDeg) * Math.cos(Math.toRadians((sLatDeg + dLatDeg) / 2));
        double y = sLatDeg - dLatDeg;

        double pythagoreDistance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) * CONST_CONVERTING_FACTOR;

        return (int) Math.round(pythagoreDistance);
    }

    public Map<Vertex, Set<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    public void addEdge(Vertex source, Vertex destination) {
        int weight = weightBetweenVertices(source, destination);

        Edge edgeFromSourceToDestination = new Edge(weight, destination);
        this.adjacencyList.get(source).add(edgeFromSourceToDestination);

        //As our graph is undirected, we can add the reversed edge
        Edge edgeFromDestinationToSource = new Edge(weight, source);
        this.adjacencyList.get(destination).add(edgeFromDestinationToSource);
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
