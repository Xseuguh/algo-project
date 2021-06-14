package kShortestPaths;

import java.util.List;

import Graph.Vertex;

public class Path {
    private List<Vertex> path;
    private int totalWeight;

    public Path() {
    }

    public Path(List<Vertex> path, int totalWeight) {
        this.path = path;
        this.totalWeight = totalWeight;
    }

    public List<Vertex> getPath() {
        return this.path;
    }

    public void setPath(List<Vertex> path) {
        this.path = path;
    }

    public int getTotalWeight() {
        return this.totalWeight;
    }

    public void setTotalWeight(int totalWeight) {
        this.totalWeight = totalWeight;
    }

    public void addVertex(Vertex vertex) {
        this.path.add(vertex);
    }

    public Vertex getLastVertex() {
        return this.path.get(this.path.size() - 1);
    }
}
