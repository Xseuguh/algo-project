package tests;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Graph.Edge;
import Graph.Graph;
import Graph.Vertex;
import kShortestPaths.KShortestPaths;
import kShortestPaths.Path;

public class KShortestPathsTest {
    private Graph graph;
    private Vertex vertex1 = new Vertex("Source 2", 2.0, 0.0, "1");
    private Vertex vertex2 = new Vertex("2", 2.0, 0.0, "2");
    private Vertex vertex3 = new Vertex("Source 1", 2.0, 0.0, "3");
    private Vertex vertex4 = new Vertex("Sink 1", 2.0, 0.0, "4");
    private Vertex vertex5 = new Vertex("5", 2.0, 0.0, "5");
    private Vertex vertex6 = new Vertex("Sink 2", 2.0, 0.0, "6");

    @BeforeEach
    public void initGraph() {
        List<Vertex> vertices = new ArrayList<>();

        vertices.add(vertex1);
        vertices.add(vertex2);
        vertices.add(vertex3);
        vertices.add(vertex4);
        vertices.add(vertex5);
        vertices.add(vertex6);

        graph = new Graph(vertices);
        Map<Vertex, Set<Edge>> adj = graph.getAdjacencyList();
        adj.get(vertex1).add(new Edge(1, vertex1, vertex2));
        adj.get(vertex2).add(new Edge(1, vertex2, vertex3));
        adj.get(vertex3).add(new Edge(3, vertex3, vertex4));
        adj.get(vertex4).add(new Edge(1, vertex4, vertex5));
        adj.get(vertex1).add(new Edge(1, vertex1, vertex5));
        adj.get(vertex2).add(new Edge(2, vertex2, vertex5));
        adj.get(vertex3).add(new Edge(1, vertex3, vertex5));
        adj.get(vertex5).add(new Edge(1, vertex5, vertex4));
        adj.get(vertex5).add(new Edge(1, vertex5, vertex6));
        adj.get(vertex6).add(new Edge(3, vertex6, vertex1));
    }

    @Test
    public void twoShortestWithSource1AndSink1() {
        KShortestPaths kShortestPaths = new KShortestPaths(graph, vertex3, vertex4, 2);

        List<Path> kPaths = kShortestPaths.getKShortestPaths();
        System.out.println(kPaths);

        List<Path> expected = new ArrayList<>();
        List<Vertex> path1 = new ArrayList<>();
        path1.add(vertex3);
        path1.add(vertex5);
        path1.add(vertex4);

        List<Vertex> path2 = new ArrayList<>();
        path2.add(vertex3);
        path2.add(vertex4);

        expected.add(new Path(path1));
        expected.add(new Path(path2));
        Assertions.assertEquals(expected, kPaths);
    }

    @Test
    public void twoShortestWithSource2AndSink2() {

        KShortestPaths kShortestPaths = new KShortestPaths(graph, vertex1, vertex6, 2);

        List<Path> kPaths = kShortestPaths.getKShortestPaths();
        System.out.println(kPaths);

        List<Path> expected = new ArrayList<>();
        List<Vertex> path1 = new ArrayList<>();
        path1.add(vertex1);
        path1.add(vertex5);
        path1.add(vertex6);

        List<Vertex> path2 = new ArrayList<>();
        path2.add(vertex1);
        path2.add(vertex2);
        path2.add(vertex5);
        path2.add(vertex6);

        expected.add(new Path(path1));
        expected.add(new Path(path2));
        Assertions.assertEquals(expected, kPaths);
    }
}
