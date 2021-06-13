package Graph;

import java.util.*;

public class Clustering {
    private Map<List<Vertex>,List<Edge>> shortestPaths;
    private Combination combination;

    private Map<Set<Edge>,Double> edgeBetweennessMap;
    private Map<Vertex, Set<Edge>> adjacencyList;
    private Set<Set<Edge>> graphEdges;

    public Clustering(Graph g) {
        this.adjacencyList = g.getAdjacencyList();
        this.graphEdges = g.getGraphEdges();
        this.combination = new Combination(this.adjacencyList);
        this.shortestPaths = setShortestPaths();
        this.edgeBetweennessMap = setEdgeBetweennessMap();
    }

    public Map<Vertex, Set<Edge>> getAdjacencyList() {
        return this.adjacencyList;
    }

    private void removeEdge(Set<Edge> edges) {
        for(Edge e : edges) {
            Vertex source = e.getSource();
            Vertex destination = e.getDestination();
            this.adjacencyList.get(source).remove(e);
            this.adjacencyList.get(destination).remove(e);
        }
    }

    public Set<Edge> getEdgePair() {
        Set<Edge> edgePair = new HashSet<Edge>();
        for (Vertex v : adjacencyList.keySet()) {
            for (Edge e : adjacencyList.get(v)) {
                Set<Edge> pair = new HashSet<Edge>();
                pair.add(e);
                for (Edge edge : adjacencyList.get(e.getDestination())) {
                    if (edge.getDestination().equals(e.getSource())){
                        pair.add(edge);
                        break;
                    }
                }
                edgePair = pair;
                break;                    
            }
            break;
        }
        return edgePair;
    }

    private Set<Edge> getEdgeCentrality(Map<Set<Edge>,Double> edgeBetweennessMap) {
        Set<Edge> maxEdge = getEdgePair();
        for (Set<Edge> e : edgeBetweennessMap.keySet()) {
            if (edgeBetweennessMap.get(e) >= edgeBetweennessMap.get(maxEdge)) {
                maxEdge = e;
            }
        }
        return maxEdge;
    }

    public Map<Vertex, Set<Edge>> graphClustering(int clusters) {
        int currentClustersAmount = clusterAmount(this.adjacencyList);

        while((currentClustersAmount < clusters) && (!edgeBetweennessMap.isEmpty())){
            Set<Edge> edgeCentrality = getEdgeCentrality(edgeBetweennessMap);
            removeEdge(edgeCentrality);
            edgeBetweennessMap.remove(edgeCentrality);
            graphEdges.remove(edgeCentrality);
            currentClustersAmount = clusterAmount(this.adjacencyList);
        }
        return this.adjacencyList;
    }

    public int clusterAmount(Map<Vertex, Set<Edge>> adjacencyList) {
        Vertex s = combination.getVerticesIDMap().get(0);
        int clusterCount = 1;

        Map<Vertex, Integer> explored = new HashMap<>();
        for (Vertex v : adjacencyList.keySet()) {
            explored.put(v, 0);
        }

        LinkedList<Vertex> queue = new LinkedList<Vertex>();
        explored.put(s, 1);
        queue.add(s);

        int remainingVertex = 0;
        boolean stop = true;

        while (stop){
            // BFS
            while (!queue.isEmpty()) {
                Vertex u = queue.remove();
                explored.put(u, clusterCount);
                for (Edge e : adjacencyList.get(u)) {
                    if (explored.get(e.getDestination()) == 0) {
                        explored.put(e.getDestination(), clusterCount);
                        queue.add(e.getDestination());
                    }
                }
            }
            // END BFS

            remainingVertex = 0;
            for (Vertex v : explored.keySet()) {
                if (explored.get(v) == 0) {
                    remainingVertex++;
                }
            }

            if (remainingVertex == 0) {
                stop = false;
            } else {
                for (Vertex v : explored.keySet()) {
                    if (explored.get(v) == 0) {
                        queue.add(v);
                        break;
                    }
                }
                clusterCount++;
            }
        }

        return clusterCount;
    }
    
    // public boolean hasEdge(Vertex u, Vertex v) {
    //     Edge edgeToV = new Edge(weightBetweenVertices(u, v), v);
    //     return adjacencyList.get(u).contains(edgeToV);
    // }

    public Map<List<Vertex>,List<Edge>> setShortestPaths() {
        Map<List<Vertex>,List<Edge>> shortestPathsTemp = new HashMap<List<Vertex>,List<Edge>>();
        List<List<Vertex>> verticesCombinations = combination.getNodePairs();
        Dijkstra dijkstra = new Dijkstra(this.adjacencyList);

        for (List<Vertex> vertexList : verticesCombinations) {
            shortestPathsTemp.put(vertexList, vertexPathToEdgePath(dijkstra.DijkstraPath(vertexList.get(0), vertexList.get(1))));
        }
        return shortestPathsTemp;
    }

    public Map<Set<Edge>,Double> setEdgeBetweennessMap() {
        Map<Set<Edge>,Double> edgeBetweennessMapTemp = new HashMap<Set<Edge>,Double>();
        for (Set<Edge> listEdge : this.graphEdges) {
            edgeBetweennessMapTemp.put(listEdge, 0.);
        }

        for (Set<Edge> listEdge : this.graphEdges) {
            for (List<Vertex> vertexPair : this.shortestPaths.keySet()) {
                for (Edge e : listEdge) {
                    if(this.shortestPaths.get(vertexPair).contains(e)) {
                        edgeBetweennessMapTemp.put(listEdge, edgeBetweennessMapTemp.get(listEdge) + 1);
                        break;
                    }
                }
            }
        }
        return edgeBetweennessMapTemp;
    }

    public List<Edge> vertexPathToEdgePath(List<Vertex> path) {
        List<Edge> edgePath = new ArrayList<Edge>();
        int N = path.size();
        for (int i = 0 ; i < N - 1 ; i++) {
            for(Edge e : adjacencyList.get(path.get(i))) {
                if (e.getDestination().equals(path.get(i+1))) {
                    edgePath.add(e);
                    break;
                }
            }
        }
        return edgePath;
    }
}
