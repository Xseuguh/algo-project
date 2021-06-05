package Display;

import Graph.*;
import loadData.Data;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class DrawSubway extends JPanel implements MouseListener, MouseMotionListener {

    private Graph graph;
    private Map<Vertex, int[]> cartesianCoordinatesFromVertex;
    private Data data;

    private static final int PADDING_TOP = 10;
    private static final int PADDING_LEFT = 10;
    private static final int HEADER_HEIGHT = 50 + Display.MENU_HEIGHT;
    private static final int TEXT_OFFSET = 5;
    private static final int MARKER_SIZE = 6;
    private static final int TRIGGER_CLICK = MARKER_SIZE + 5;

    private static final Map<String, Color> lineColor = new TreeMap<>();

    private int[] minimalCoord, maximalCoord;
    private int[] size;

    private Vertex startStation, endStation, hoverStation;

    public DrawSubway(Graph graph, Data data, int[] initialSize) {
        super();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        this.graph = graph;
        this.size = initialSize;
        this.data = data;

        initOffset();
        refreshCoordinates();


    }

    private int[] getXYCoordinates(Vertex v) {
        double sLatDeg = v.getLatitude();
        double sLongDeg = v.getLongitude();

        int x = (int) (sLongDeg * Math.cos(Math.toRadians(sLatDeg)) * 5000);
        int y = (int) (sLatDeg * 5000);

        return new int[]{x, y};
    }

    private void initOffset() {
        this.minimalCoord = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE};
        this.maximalCoord = new int[]{0, 0};

        for (Vertex v : graph.getAdjacencyList().keySet()) {
            int[] coordinates = getXYCoordinates(v);

            if (coordinates[0] < minimalCoord[0]) {
                minimalCoord[0] = coordinates[0];
            }
            if (coordinates[0] > maximalCoord[0]) {
                maximalCoord[0] = coordinates[0];
            }
            if (coordinates[1] > maximalCoord[1]) {
                maximalCoord[1] = coordinates[1];
            }
            if (coordinates[1] < minimalCoord[1]) {
                minimalCoord[1] = coordinates[1];
            }
        }
    }

    private void refreshCoordinates() {
        this.cartesianCoordinatesFromVertex = new HashMap<>();
        for (Vertex v : graph.getAdjacencyList().keySet()) {
            int[] coord = getXYCoordinates(v);
            int rawX = coord[0];
            int rawY = coord[1];
            //We project our raw coordinates on our JPanel
            double x = (this.size[0]) * (double) (rawX - (minimalCoord[0] - PADDING_LEFT)) / (double) (maximalCoord[0] - minimalCoord[0] + 2 * PADDING_LEFT);
            double y = (this.size[1] - HEADER_HEIGHT) * (double) (maximalCoord[1] + PADDING_TOP - rawY) / (double) (maximalCoord[1] - minimalCoord[1] + PADDING_TOP);
            this.cartesianCoordinatesFromVertex.put(v, new int[]{(int) x, (int) y});
        }
    }

    public void setSize(int[] newSize) {
        this.size = newSize;
    }

    public void refresh() {
        this.refreshCoordinates();
        this.repaint();
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.black);

        for (Vertex v : graph.getAdjacencyList().keySet()) {

            int[] coordinates = cartesianCoordinatesFromVertex.get(v);
            int x = coordinates[0];
            int y = coordinates[1];

            g.fillOval(x - (MARKER_SIZE / 2), y - (MARKER_SIZE / 2), MARKER_SIZE, MARKER_SIZE);

            for (Edge edge : graph.getAdjacencyList().get(v)) {
                Vertex n = edge.getDestination();
                int[] neigbhorCoordinates = cartesianCoordinatesFromVertex.get(n);
                int neighborX = neigbhorCoordinates[0];
                int neighborY = neigbhorCoordinates[1];

                g.drawLine(x, y, neighborX, neighborY);
            }
        }
        //We color the start and end station

        colorVertexAndDisplayName(hoverStation, Color.GRAY, g);
        colorVertexAndDisplayName(startStation, Color.CYAN, g);
        colorVertexAndDisplayName(endStation, Color.RED, g);
    }

    private void colorVertexAndDisplayName(Vertex v, Color color, Graphics g) {
        if (v != null) {
            g.setColor(color);
            int[] coordinates = cartesianCoordinatesFromVertex.get(v);
            int x = coordinates[0];
            int y = coordinates[1];
            g.fillOval(x - (MARKER_SIZE / 2), y - (MARKER_SIZE / 2), MARKER_SIZE, MARKER_SIZE);
            g.drawString(v.getName(), x + TEXT_OFFSET, y + TEXT_OFFSET);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mX = e.getX();
        int mY = e.getY();
        for (Vertex v : graph.getAdjacencyList().keySet()) {
            int[] c = cartesianCoordinatesFromVertex.get(v);
            if (Math.abs((c[0] - mX)) < TRIGGER_CLICK && Math.abs((c[1] - mY)) < TRIGGER_CLICK) {

                if (e.getButton() == MouseEvent.BUTTON1) { //Left click = selection starting station
                    this.startStation = v;
                } else if (e.getButton() == MouseEvent.BUTTON3) {  //Right click = selection starting station

                    this.endStation = v;
                }
                this.repaint();
                break;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int mX = e.getX();
        int mY = e.getY();
        boolean vertexFound = false;
        for (Vertex v : graph.getAdjacencyList().keySet()) {
            int[] c = cartesianCoordinatesFromVertex.get(v);
            if (Math.abs((c[0] - mX)) < TRIGGER_CLICK && Math.abs((c[1] - mY)) < TRIGGER_CLICK) {
                this.hoverStation = v;
                this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                vertexFound = true;
                break;
            }
        }
        if (!vertexFound) {
            this.hoverStation = null;
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        this.repaint();
    }
}
