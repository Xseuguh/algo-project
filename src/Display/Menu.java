package Display;

import Graph.*;
import loadData.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.*;
import java.util.List;

public class Menu extends JPanel {

    private ScrollingMenu startingStationName, endingStationName;
    private GridLayout layout;

    private DrawSubway drawSubway;

    private JButton bfs, dijkstra, kShortestPath;

    private JComboBox<String> kShortestPathList;
    private Map<String, List<Vertex>> kShortestPathMap;

    private static final String DEFAULT_START_STATION = "--Select the departure station";
    private static final String DEFAULT_END_STATION = "--Select the ending station";
    private static final String DEFAULT_SELECTED_PATH = "--Select the path to display";

    public Menu(Data data, DrawSubway drawSubway) {
        super();
        this.drawSubway = drawSubway;


        this.kShortestPathList = new JComboBox<>();

        this.startingStationName = new ScrollingMenu(data, v -> drawSubway.setStartStation(v), DEFAULT_START_STATION, "Start from:");

        this.endingStationName = new ScrollingMenu(data, v -> drawSubway.setEndStation(v), DEFAULT_END_STATION, "End to:");


        this.bfs = new JButton("Show the shortest path via BFS");
        this.bfs.addActionListener(e -> {
            setSelectedPathToDefault();
            System.out.println("bfs");
            //TODO Get path from bfs algorithm

            //TEMP
            List<Vertex> path = new ArrayList<>();
            path.add(data.getVertexFromStationName("Innsbrucker Platz"));
            path.add(data.getVertexFromStationName("Rathaus Schöneberg"));
            path.add(data.getVertexFromStationName("Bayerischer Platz"));
            path.add(data.getVertexFromStationName("Viktoria-Luise-Platz"));
            path.add(data.getVertexFromStationName("Nollendorfplatz"));
            //END TEMP

            drawSubway.setPath(path);
        });

        this.dijkstra = new JButton("Show the shortest path via Dijkstra");
        this.dijkstra.addActionListener(e -> {
            setSelectedPathToDefault();
            System.out.println("dijkstra");
            //TODO Get path from dijkstra algorithm
            drawSubway.setPath(new ArrayList<>());
        });

        this.kShortestPath = new JButton("Calculate the k shortest paths");
        this.kShortestPath.addActionListener(e -> {
            //TODO Get k shortest paths
            this.kShortestPathMap = new TreeMap<>();

            //TEMP
            List<Vertex> path = new ArrayList<>();
            path.add(data.getVertexFromStationName("Innsbrucker Platz"));
            path.add(data.getVertexFromStationName("Rathaus Schöneberg"));
            path.add(data.getVertexFromStationName("Bayerischer Platz"));
            path.add(data.getVertexFromStationName("Viktoria-Luise-Platz"));
            path.add(data.getVertexFromStationName("Nollendorfplatz"));
            this.kShortestPathMap.put("Path 1 : 505m", path);
            path = new ArrayList<>();
            path.add(data.getVertexFromStationName("Innsbrucker Platz"));
            path.add(data.getVertexFromStationName("Rathaus Schöneberg"));
            path.add(data.getVertexFromStationName("Bayerischer Platz"));
            path.add(data.getVertexFromStationName("Viktoria-Luise-Platz"));
            path.add(data.getVertexFromStationName("Nollendorfplatz"));
            path.add(data.getVertexFromStationName("Bülowstr."));
            path.add(data.getVertexFromStationName("Gleisdreieck"));
            this.kShortestPathMap.put("Path 2 : 610m", path);
            //END TEMP
            kShortestPathList.addItem(DEFAULT_SELECTED_PATH);
            for (String key : kShortestPathMap.keySet()) {
                this.kShortestPathList.addItem(key);
            }
            this.kShortestPathList.setVisible(true);
            System.out.println("k shortest paths");
            this.kShortestPath.setEnabled(false);
        });
        this.kShortestPathList.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String path = (String) e.getItem();
                this.drawSubway.setPath(this.kShortestPathMap.get(path));
            }
        });


        layout = new GridLayout(2, 3);

        this.setLayout(layout);

        this.add(this.startingStationName);
        this.add(this.bfs);
        this.add(this.dijkstra);
        this.add(this.endingStationName);
        this.add(this.kShortestPath);
        this.add(this.kShortestPathList);

        enableButtons(false);
        resetShortestPathList();

        this.setBackground(Color.WHITE);
    }

    public void setDepartureStation(String stationName) {
        this.startingStationName.setSelectedItem(stationName);
    }

    public void setEndingStation(String stationName) {
        this.endingStationName.setSelectedItem(stationName);
    }

    public void enableButtons(boolean isDisable) {
        this.bfs.setEnabled(isDisable);
        this.dijkstra.setEnabled(isDisable);
        this.kShortestPath.setEnabled(isDisable);
    }

    public void resetShortestPathList() {
        this.kShortestPathList.removeAllItems();
        this.kShortestPathList.setVisible(false);
    }

    private void setSelectedPathToDefault(){
        this.kShortestPathList.setSelectedItem(DEFAULT_SELECTED_PATH);
    }
}
