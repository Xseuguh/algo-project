package Graph;

import java.util.*;

public class Graph {
    //Factor to convert in meter:  1 nautical mile * 60 (to pass in degree)
    private static final double CONST_CONVERTING_FACTOR = 1852 * 60;

    private int numberOfVertices;
    private Map<Vertex, Set<Edge>> adjacencyList;
    private Map<Vertex, Set<Edge>> subAdjacencyList;

    private List<int[]> combinations;
    private List<List<Vertex>> verticesCombinations;
    private Map<Integer,Vertex> verticesIDMap;
    private Map<List<Vertex>,List<Edge>> shortestPaths;
    private List<Edge> graphEdges;
    private Map<Edge,Double> edgeBetweennessMap;

    private static final Comparator<Edge> EDGE_COMPARATOR = Comparator.comparing(e -> e.getDestination().getId());

    public Graph(List<Vertex> vertices) {
        this.numberOfVertices = vertices.size();
        adjacencyList = new HashMap<>();
        fillAdjacencyListWithEmptyList(vertices);
        this.verticesIDMap = graphVertexIDs();
        this.graphEdges = new ArrayList<Edge>();
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
        return this.adjacencyList;
    }

    public Map<Vertex, Set<Edge>> getSubAdjacencyList() {
        return this.subAdjacencyList;
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

    public void removeEdge(Vertex source, Vertex destination) {
        Set<Edge> sourceEdgesNew = new HashSet<Edge>();
        for (Edge e : this.subAdjacencyList.get(source)) {
            if (!e.getDestination().equals(destination)) {
                sourceEdgesNew.add(e);
            }
        }
        this.subAdjacencyList.put(source,sourceEdgesNew);
        
        //As our graph is undirected, we can add the reversed edge
        Set<Edge> destEdgesNew = new HashSet<Edge>();
        for (Edge e : this.subAdjacencyList.get(destination)) {
            if (!e.getDestination().equals(source)) {
                destEdgesNew.add(e);
            }
        }
        this.subAdjacencyList.put(destination,destEdgesNew);
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

    public Edge getEdgeCentrality(Map<Edge,Double> edgeBetweennessMap) {
        Edge maxEdge = graphEdges.get(0);
        for (Edge e : edgeBetweennessMap.keySet()) {
            if (edgeBetweennessMap.get(e) >= edgeBetweennessMap.get(maxEdge)) {
                maxEdge = e;
            }
        }
        return maxEdge;
    }

    public void graphClustering(int clusters) {
        this.subAdjacencyList = this.adjacencyList;
        setEdgeBetweennessMap();
        Map<Edge,Double> edgeBetweennessMapCopy = this.edgeBetweennessMap;
        int iterator = clusterAmount(this.subAdjacencyList);

        while((iterator < clusters) && (!edgeBetweennessMapCopy.isEmpty())){
            Edge edgeCentrality = getEdgeCentrality(edgeBetweennessMapCopy);
            removeEdge(edgeCentrality.getSource(), edgeCentrality.getDestination());
            edgeBetweennessMapCopy.remove(edgeCentrality);
            graphEdges.remove(edgeCentrality);
            iterator = clusterAmount(this.subAdjacencyList);
        }
    }

    public int clusterAmount(Map<Vertex, Set<Edge>> adjacencyList) {
        Vertex s = verticesIDMap.get(0);
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

    public List<Vertex> Dijkstra(Vertex source, Vertex dest) {
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

    public boolean hasEdge(Vertex u, Vertex v) {
        Edge edgeToV = new Edge(weightBetweenVertices(u, v), v);
        return adjacencyList.get(u).contains(edgeToV);
    }

    public List<List<Vertex>> getNodePairs() {
        this.combinations = generate(adjacencyList.size(), 2);
        List<List<Vertex>> verticesCombinationsTemp = new ArrayList<List<Vertex>>();
        for (int[] intList : combinations) {
            List<Vertex> vertexPair = new ArrayList<Vertex>();
            vertexPair.add(verticesIDMap.get(intList[0]));
            vertexPair.add(verticesIDMap.get(intList[1]));
            if (!vertexPair.get(0).equals(vertexPair.get(1))) {
                verticesCombinationsTemp.add(vertexPair);
            }
        }
        return verticesCombinationsTemp;
        
    }

    public void setShortestPaths() {
        Map<List<Vertex>,List<Edge>> shortestPathsTemp = new HashMap<List<Vertex>,List<Edge>>();
        this.verticesCombinations = getNodePairs();
        for (List<Vertex> vertexList : verticesCombinations) {
            shortestPathsTemp.put(vertexList, vertexPathToEdgePath(Dijkstra(vertexList.get(0), vertexList.get(1))));
        }
        this.shortestPaths = shortestPathsTemp;
    }

    public void setEdgeBetweennessMap() {
        setShortestPaths();
        for (List<Vertex> verticesList : this.shortestPaths.keySet()) {
            if (hasEdge(verticesList.get(0), verticesList.get(1))) {
                this.graphEdges.add(new Edge(verticesList.get(0), verticesList.get(1)));
            }
        }

        this.edgeBetweennessMap = new HashMap<Edge,Double>();
        for (Edge e : this.graphEdges) {
            this.edgeBetweennessMap.put(e, 0.);
        }

        for (Edge e : this.graphEdges) {
            for (List<Vertex> vertexPair : this.shortestPaths.keySet()) {
                if(this.shortestPaths.get(vertexPair).contains(e) || this.shortestPaths.get(vertexPair).contains(new Edge(e.getDestination(), e.getSource()))){
                    this.edgeBetweennessMap.put(e, this.edgeBetweennessMap.get(e) + 1);
                }
            }
        }
    }


    public void helper(List<int[]> combinationsT, int data[], int start, int end, int index) {
        if (index == data.length) {
            int[] combination = data.clone();
            combinationsT.add(combination);
        } else {
            int max = Math.min(end, end + 1 - data.length + index);
            for (int i = start; i <= max; i++) {
                data[index] = i;
                helper(combinationsT, data, i + 1, end, index + 1);
            }
        }
    }

    public List<int[]> generate(int n, int r) {
        List<int[]> combinationsTemp = new ArrayList<>();
        helper(combinationsTemp, new int[r], 0, n - 1, 0);
        return combinationsTemp;
    }

    public Map<Integer,Vertex> graphVertexIDs() {
        Map<Integer,Vertex> res = new HashMap<Integer,Vertex>();
        int i = 0;
        for (Vertex v : adjacencyList.keySet()) {
            res.put(i,v);
            i++;
        }
        return res;
    }

    public List<Edge> vertexPathToEdgePath(List<Vertex> path) {
        List<Edge> edgePath = new ArrayList<Edge>();
        int N = path.size();
        for (int i = 0 ; i < N - 1 ; i++) {
            edgePath.add(new Edge(path.get(i), path.get(i + 1)));
        }
        return edgePath;
    }
}
