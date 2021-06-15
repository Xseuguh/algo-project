package kShortestPaths;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Graph.Dijkstra;
import Graph.Edge;
import Graph.Graph;
import Graph.Vertex;

public class KShortestPaths {
    private Graph graph;
    private Vertex sink;
    private Vertex source;
    private int K;

    public KShortestPaths(Graph graph, Vertex source, Vertex sink, int K) {
        this.graph = graph;
        this.sink = sink;
        this.source = source;
        this.K = K;
    };

    public List<Path> getKShortestPaths() {
        List<Path> shortestPaths = new ArrayList<>();
        List<Path> possiblePaths = new ArrayList<>();
        Dijkstra dijkstra = new Dijkstra(this.cloneGraph().getAdjacencyList());

        shortestPaths.add(dijkstra.DijkstraPathWithDist(source, sink));

        for (int k = 1; k < this.K; k++) {
            Path lastPath = shortestPaths.get(k - 1);
            for (int i = 0; i < lastPath.size() - 2; i++) {
                Graph clonedGraph = this.cloneGraph();

                Vertex junctionNode = lastPath.getNode(i);
                Path lastPathRoot = lastPath.getSubPath(0, i + 1);

                for (Path path : shortestPaths) {
                    if (lastPathRoot.equals(path.getSubPath(0, i + 1)))
                        clonedGraph.removeEdge(path.getNode(i), path.getNode(i + 1));
                }

                for (int j = 0; j < lastPathRoot.size() - 1; j++) {
                    clonedGraph.removeVertex(lastPathRoot.getNode(j));
                }

                dijkstra = new Dijkstra(clonedGraph.getAdjacencyList());
                Path nextPath = dijkstra.DijkstraPathWithDist(junctionNode, sink);

                Path newPath = lastPathRoot.concat(nextPath);
                if (!possiblePaths.contains(newPath) && newPath.getLastVertex() == sink)
                    possiblePaths.add(newPath);

            }
            if (possiblePaths.isEmpty())
                break;

            possiblePaths.sort(Comparator.comparing(path -> path.getDistance(this.graph)));
            shortestPaths.add(possiblePaths.remove(0));
        }

        return shortestPaths;
    }

    public Graph cloneGraph() {
        List<Vertex> vertices = new ArrayList(this.graph.getAdjacencyList().keySet());
        Graph newGraph = new Graph(vertices);

        vertices.forEach(vertex -> {
            vertex.setMinDistance(Integer.MAX_VALUE);
            Set<Edge> edges = this.graph.getAdjacencyList().get(vertex);
            newGraph.getAdjacencyList().get(vertex).addAll(edges);
        });

        return newGraph;
    }

}
