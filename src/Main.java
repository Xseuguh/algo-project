import Graph.*;
import loadData.Data;

import java.io.IOException;
import java.util.*;

import Display.Display;

public class Main {

    public static void main(String[] args) throws IOException {
        /*
            Initialization part
        */
        //Get data from GTFS files
        Data data = new Data();

        //Initialization of the graph
        Graph graph = new Graph(data.getAllStations());

        //Initialization of the edges
        for (List<String> stations : data.getLineWithStations().values()) {
            for (int i = 0; i < stations.size() - 1; i++) {
                Vertex sourceVertex = data.getVertexFromStationName(stations.get(i));

                Vertex destinationVertex = data.getVertexFromStationName(stations.get(i + 1));

                graph.addEdge(sourceVertex, destinationVertex);
                graph.setGraphEdges();
            }
        }
        //End of graph initialization

        /*
            Display graph
         */
        new Display("Berlin subway", graph, data);

        /*
            Do something with the graph
         */
//        System.out.println("\nBerlin subway:");
//        data.displayLinesWithStations();
//
//        System.out.println("\nAdjacency list:");
//        graph.displayAdjacencyList();
//
//        Map<Vertex, Set<Edge>> adjacencyList = graph.getAdjacencyList();
//
//        String startName = "Alexanderplatz";
////        Vertex startVertex = data.getStartingStationFromStationName(startName);
//        Vertex startVertex = data.getStartingStationFromStationName(startName);
//
//        System.out.println("\nStarting station: " + startVertex.getName() + " (id: " + startVertex.getId()
//                + ", coordinates: " + startVertex.getLatitude() + "," + startVertex.getLongitude() + ")" +
//                "\nThis stations has " + adjacencyList.get(startVertex).size() + " neighbors:");
//        for (Edge edge : adjacencyList.get(startVertex)) {
//            Vertex destination = edge.getDestination();
//            System.out.println("\t- " + destination.getName() + "(id: " + destination.getId()
//                    + ", coordinates: " + destination.getLatitude() + "," + destination.getLongitude() + ")" +
//                    "\n\tDistance from starting station: " + edge.getWeight() + "m");
//        }
    }
}
