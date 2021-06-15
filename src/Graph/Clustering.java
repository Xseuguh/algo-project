package Graph;

import java.util.*;

public class Clustering {
    private Map<List<Vertex>, List<Edge>> shortestPaths;
    private Combination combination;

    private Map<Set<Edge>, Double> edgeBetweennessMap;

    private Map<Vertex, Set<Edge>> adjacencyList;
    private Set<Set<Edge>> graphEdges;

    public Clustering(Graph g) {
        fillAdjacencyList(g.getAdjacencyList());
        fillGraphEdges(g.getGraphEdges());

        this.combination = new Combination(this.adjacencyList);
        this.shortestPaths = setShortestPaths();
        this.edgeBetweennessMap = setEdgeBetweennessMap();
    }

    private void fillAdjacencyList(Map<Vertex, Set<Edge>> graphAdjacencyList) {
        this.adjacencyList = new HashMap<>();
        for (Vertex v : graphAdjacencyList.keySet()) {
            for (Edge e : graphAdjacencyList.get(v)) {
                adjacencyList.computeIfAbsent(v, k -> new HashSet<>()).add(e);
            }
        }
    }

    private void fillGraphEdges(Set<Set<Edge>> graphEdges) {
        this.graphEdges = new HashSet<>();
        for (Set<Edge> listEdge : graphEdges) {
            this.graphEdges.add(listEdge);

        }
    }

    private void removeEdge(Set<Edge> edges) {
        for (Edge e : edges) {
            Vertex source = e.getSource();
            Vertex destination = e.getDestination();
            this.adjacencyList.get(source).remove(e);
            this.adjacencyList.get(destination).remove(e);
        }
    }

    private Set<Edge> getEdgeCentrality(Map<Set<Edge>, Double> edgeBetweennessMap) {
        Set<Edge> maxEdge = null;
        for (Set<Edge> e : edgeBetweennessMap.keySet()) {
            if (edgeBetweennessMap.get(e) >= edgeBetweennessMap.getOrDefault(maxEdge, -1.)) {
                maxEdge = e;
            }
        }
        return maxEdge;
    }

    public List<Set<Edge>> graphClustering(int clusters) {
        List<Set<Edge>> edgesToRemove = new ArrayList<>();
        int currentClustersAmount = clusterAmount();

        while (currentClustersAmount < clusters && !edgeBetweennessMap.isEmpty()) {
            Set<Edge> edgeCentrality = getEdgeCentrality(edgeBetweennessMap);
            removeEdge(edgeCentrality);
            edgeBetweennessMap.remove(edgeCentrality);
            graphEdges.remove(edgeCentrality);
            edgesToRemove.add(edgeCentrality);
            currentClustersAmount = clusterAmount();
        }
        return edgesToRemove;
    }

    public int clusterAmount() {
        return vertexByCluster().size();
    }

    public Map<Integer, Set<Vertex>> vertexByCluster() {
        Map<Integer, Set<Vertex>> vertexByCluster = new HashMap<>();

        Set<Vertex> vertexNotVisited = new HashSet<>();
        for (Vertex v : adjacencyList.keySet()) {
            vertexNotVisited.add(v);
        }

        LinkedList<Vertex> queue = new LinkedList<>();

        int clusterCount = 1;
        while (!vertexNotVisited.isEmpty()) {
            queue.add(vertexNotVisited.iterator().next());
            while (!queue.isEmpty()) {
                Vertex u = queue.remove();
                vertexByCluster.computeIfAbsent(clusterCount, k -> new HashSet<>()).add(u);
                vertexNotVisited.remove(u);
                for (Edge e : adjacencyList.get(u)) {
                    if (vertexNotVisited.contains(e.getDestination())) {
                        vertexByCluster.get(clusterCount).add(u);
                        vertexNotVisited.remove(e.getDestination());
                        queue.add(e.getDestination());
                    }
                }
            }
            clusterCount++;
        }

        return vertexByCluster;
    }

    public Map<List<Vertex>, List<Edge>> setShortestPaths() {
        Map<List<Vertex>, List<Edge>> shortestPathsTemp = new HashMap<>();
        List<List<Vertex>> verticesCombinations = combination.getNodePairs();
        Dijkstra dijkstra = new Dijkstra(this.adjacencyList);

        for (List<Vertex> vertexList : verticesCombinations) {
            shortestPathsTemp.put(vertexList, vertexPathToEdgePath(dijkstra.DijkstraPath(vertexList.get(0), vertexList.get(1))));
        }
        return shortestPathsTemp;
    }

    public Map<Set<Edge>, Double> setEdgeBetweennessMap() {
        Map<Set<Edge>, Double> edgeBetweennessMapTemp = new HashMap<>();
        for (Set<Edge> listEdge : this.graphEdges) {
            edgeBetweennessMapTemp.put(listEdge, 0.);
        }

        for (Set<Edge> listEdge : this.graphEdges) {
            for (List<Vertex> vertexPair : this.shortestPaths.keySet()) {
                for (Edge e : listEdge) {
                    if (this.shortestPaths.get(vertexPair).contains(e)) {
                        edgeBetweennessMapTemp.put(listEdge, edgeBetweennessMapTemp.get(listEdge) + 1);
                        break;
                    }
                }
            }
        }
        return edgeBetweennessMapTemp;
    }

    public List<Edge> vertexPathToEdgePath(List<Vertex> path) {
        List<Edge> edgePath = new ArrayList<>();
        int N = path.size();
        for (int i = 0; i < N - 1; i++) {
            for (Edge e : adjacencyList.get(path.get(i))) {
                if (e.getDestination().equals(path.get(i + 1))) {
                    edgePath.add(e);
                    break;
                }
            }
        }
        return edgePath;
    }
}
