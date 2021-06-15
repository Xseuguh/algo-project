package Graph;

import java.util.*;

import kShortestPaths.Path;

public class Dijkstra {
    private Map<Vertex, Set<Edge>> adjacencyList;

    public Dijkstra(Map<Vertex, Set<Edge>> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public Path DijkstraPathWithDist(Vertex source, Vertex dest) {
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
            for (Edge e : adjacencyList.get(u)) {
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
        return new Path(path);
    }

    public List<Vertex> DijkstraPath(Vertex source, Vertex dest) {
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
            for (Edge e : adjacencyList.get(u)) {
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
