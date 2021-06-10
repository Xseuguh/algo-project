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

    public List<Vertex> BFSPath(Vertex src, Vertex dest) {
        // pred stores predecessor of each vertex
        // dist stores distance of each vertex from src
        Map<Vertex, Vertex> pred = new HashMap<>();
        Map<Vertex, Integer> dist = new HashMap<>();
 
        if (BFS(src, dest, pred, dist) == false) {
            System.out.println("Given source and destination" + "are not connected");
            return null;
        }

        LinkedList<Vertex> path = new LinkedList<Vertex>();
        Vertex crawl = dest;
        path.add(crawl);
        while (pred.containsKey(crawl)) {
            path.add(pred.get(crawl));
            crawl = pred.get(crawl);
        }

        return path;
    }
 
    public boolean BFS(Vertex src, Vertex dest, Map<Vertex, Vertex> pred, Map<Vertex, Integer> dist) {
        LinkedList<Vertex> queue = new LinkedList<Vertex>();
        Map<Vertex, Boolean> visited = new HashMap<>();
        
        for (Vertex v : adjacencyList.keySet()) {
            visited.put(v, false);
            dist.put(v, Integer.MAX_VALUE);
            pred.put(v, null);
        }
 
        visited.put(src, true);
        dist.put(src, 0);
        queue.add(src);
 
        // bfs Algorithm
        while (!queue.isEmpty()) {
            Vertex u = queue.remove();
            for (Edge e : adjacencyList.get(u)) {
                if (!visited.get(e.getDestination())) {
                    visited.put(e.getDestination(), true);
                    dist.put(e.getDestination(), dist.get(u) + 1);
                    pred.put(e.getDestination(), u);
                    queue.add(e.getDestination());
 
                    if (e.getDestination().getName().equals(dest.getName()))
                        return true;
                }
            }
        }
        return false;
    }

    public List<Vertex> Dijkstra(Vertex source, Vertex dest)
    {
        Map<Vertex, Vertex> pred = new HashMap<>();
        for (Vertex v : adjacencyList.keySet()) {
            pred.put(v, null);
            v.setMinDistance(Integer.MAX_VALUE);
        }
        source.setMinDistance(0);
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
        vertexQueue.add(source);

        while (!vertexQueue.isEmpty()) {
            Vertex u = vertexQueue.poll();

            // Visit each edge exiting u
            for (Edge e : adjacencyList.get(u))
            {
                Vertex v = e.getDestination();
                int weight = e.getWeight();
                int distanceThroughU = u.getMinDistance() + weight;
                if (distanceThroughU < v.getMinDistance()) {
                    vertexQueue.remove(v);
                    v.setMinDistance(distanceThroughU);
                    pred.put(v, u);
                    vertexQueue.add(v);
                }
            }
        }
        
        List<Vertex> path = new ArrayList<Vertex>();
        for (Vertex vertex = dest; vertex != null; vertex = pred.get(vertex))
            path.add(vertex);
        Collections.reverse(path);
        return path;
    }
}
