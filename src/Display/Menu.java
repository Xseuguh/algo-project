package Display;

import Graph.*;
import loadData.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Menu extends JPanel {

    private ScrollingMenu startingStationName, endingStationName;
    private GridLayout layout;

    private DrawSubway drawSubway;

    public Menu(Data data, DrawSubway drawSubway) {
        super();
        this.drawSubway = drawSubway;

        this.startingStationName = new ScrollingMenu(data, v -> drawSubway.setStartStation(v), "--Select the departure station","Start from:");

        this.endingStationName = new ScrollingMenu(data, v -> drawSubway.setEndStation(v), "--Select the ending station","End to:");



        JButton bfs = new JButton("Show the shortest path via BFS");
        bfs.addActionListener(e->{
            System.out.println("bfs");
            //TODO Get path from bfs algorithm
            List<Vertex> path = new ArrayList<>();
            path.add(data.getVertexFromStationName("Innsbrucker Platz"));
            path.add(data.getVertexFromStationName("Rathaus Schöneberg"));
            path.add(data.getVertexFromStationName("Bayerischer Platz"));
            path.add(data.getVertexFromStationName("Viktoria-Luise-Platz"));
            path.add(data.getVertexFromStationName("Nollendorfplatz"));

            drawSubway.setPath(path);
        });
        JButton dijkstra = new JButton("Show the shortest path via Dijkstra");
        dijkstra.addActionListener(e->{
            System.out.println("dijkstra");
            drawSubway.setPath(new ArrayList<>());
        });

        JButton kShortestPath = new JButton("Show the k shortest paths");
        kShortestPath.addActionListener(e->{
            System.out.println("k shortest paths");
        });


        layout = new GridLayout(2, 3);

        this.setLayout(layout);
        this.add(startingStationName);
        this.add(bfs);
        this.add(dijkstra);
        this.add(endingStationName);
        this.add(kShortestPath);

        this.setBackground(Color.WHITE);
    }

    public void setDepartureStation(String stationName) {
        this.startingStationName.setSelectedItem(stationName);
    }

    public void setEndingStation(String stationName) {
        this.endingStationName.setSelectedItem(stationName);
    }
}
