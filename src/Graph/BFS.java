package Graph;

import java.util.*;

public class BFS {
    private Map<Vertex, Set<Edge>> adjacencyList;

    public BFS(Map<Vertex, Set<Edge>> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public List<Vertex> BFSPath(Vertex src, Vertex dest) {
        // pred stores predecessor of each vertex
        // dist stores distance of each vertex from src
        Map<Vertex, Vertex> pred = new HashMap<>();
        Map<Vertex, Integer> dist = new HashMap<>();
 
        if (BFSCheck(src, dest, pred, dist) == false) {
            System.out.println("Given source and destination" + "are not connected");
            return null;
        }

        LinkedList<Vertex> path = new LinkedList<>();
        Vertex crawl = dest;
        path.add(crawl);
        while (!crawl.equals(src)) {
            path.add(pred.get(crawl));
            crawl = pred.get(crawl);
        }

        return path;
    }
 
    public boolean BFSCheck(Vertex src, Vertex dest, Map<Vertex, Vertex> pred, Map<Vertex, Integer> dist) {
        LinkedList<Vertex> queue = new LinkedList<>();
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
}
