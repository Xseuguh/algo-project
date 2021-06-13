package Graph;

import java.util.*;

public class Clustering {
    private Map<List<Vertex>,List<Edge>> shortestPaths;
    private List<List<Edge>> graphEdges;
    private Map<List<Edge>,Double> edgeBetweennessMap;
    private Combination combination;
    
    private Map<Vertex, Set<Edge>> adjacencyList;

    public Clustering(Graph g) {
        this.adjacencyList = g.getAdjacencyList();
        this.graphEdges = g.getGraphEdges();
        this.combination = new Combination(this.adjacencyList);
        setShortestPaths();
        setEdgeBetweennessMap();
    }

    public Map<Vertex, Set<Edge>> getAdjacencyList() {
        return this.adjacencyList;
    }

    public void removeEdge(List<Edge> edges) {
        for(Edge e : edges) {
            Vertex source = e.getSource();
            Vertex destination = e.getDestination();
            this.adjacencyList.get(source).remove(e);
            this.adjacencyList.get(destination).remove(e);
        }
    }

    public List<Edge> getEdgeCentrality(Map<List<Edge>,Double> edgeBetweennessMap) {
        List<Edge> maxEdge = graphEdges.get(0);
        for (List<Edge> e : edgeBetweennessMap.keySet()) {
            if (edgeBetweennessMap.get(e) >= edgeBetweennessMap.get(maxEdge)) {
                maxEdge = e;
            }
        }
        return maxEdge;
    }

    public Map<Vertex, Set<Edge>> graphClustering(int clusters) {
        int currentClustersAmount = clusterAmount(this.adjacencyList);

        while((currentClustersAmount < clusters) && (!edgeBetweennessMap.isEmpty())){
            List<Edge> edgeCentrality = getEdgeCentrality(edgeBetweennessMap);
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

    public void setShortestPaths() {
        Map<List<Vertex>,List<Edge>> shortestPathsTemp = new HashMap<List<Vertex>,List<Edge>>();
        List<List<Vertex>> verticesCombinations = combination.getNodePairs();
        Dijkstra dijkstra = new Dijkstra(this.adjacencyList);

        for (List<Vertex> vertexList : verticesCombinations) {
            shortestPathsTemp.put(vertexList, vertexPathToEdgePath(dijkstra.DijkstraPath(vertexList.get(0), vertexList.get(1))));
        }
        this.shortestPaths = shortestPathsTemp;
    }

    public void setEdgeBetweennessMap() {
        
        // for (List<Vertex> verticesList : this.shortestPaths.keySet()) {
        //     if (hasEdge(verticesList.get(0), verticesList.get(1))) {
        //         this.graphEdges.add(new Edge(verticesList.get(0), verticesList.get(1)));
        //     }
        // }

        this.edgeBetweennessMap = new HashMap<List<Edge>,Double>();
        for (List<Edge> listEdge : this.graphEdges) {
            this.edgeBetweennessMap.put(listEdge, 0.);
        }

        for (List<Edge> listEdge : this.graphEdges) {
            for (List<Vertex> vertexPair : this.shortestPaths.keySet()) {
                for (Edge e : listEdge) {
                    if(this.shortestPaths.get(vertexPair).contains(e)) {
                        this.edgeBetweennessMap.put(listEdge, this.edgeBetweennessMap.get(listEdge) + 1);
                        break;
                    }
                }
                
            }
        }
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
