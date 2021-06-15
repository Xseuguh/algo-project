package kShortestPaths;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import Graph.Vertex;
import Graph.Edge;
import Graph.Graph;

public class Path {
    private List<Vertex> path;

    public Path(List<Vertex> path) {
        this.path = path;
    }

    public List<Vertex> getPath() {
        return this.path;
    }

    public Vertex getLastVertex() {
        return this.path.get(this.path.size() - 1);
    }

    public int size() {
        return this.path.size();
    }

    public Vertex getNode(int i) {
        return this.path.get(i);
    }

    public Path getSubPath(int start, int end) {
        if (end <= size())
            return new Path(this.path.subList(start, end));
        return new Path(this.path.subList(start, size()));
    }

    public int getDistance(Graph graph) {
        int sum = 0;
        if (this.size() == 0)
            return 0;

        Vertex actualNode = this.getNode(0);

        for (int i = 1; i < this.size(); i++) {
            Vertex nextNode = this.getNode(i);
            try {
                Optional<Edge> correspondingEdge = graph.findEdge(actualNode, nextNode);
                if (correspondingEdge.isPresent())
                    sum += correspondingEdge.get().getWeight();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                actualNode = nextNode;
            }
        }

        return sum;
    }

    public Path concat(Path other) {
        List<Vertex> newPath = new ArrayList<>();

        newPath.addAll(this.path);
        newPath.addAll(other.path.subList(1, other.path.size()));

        return new Path(newPath);
    }

    @Override
    public String toString() {
        return "{" + " path='" + getPath() + "'" + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Path)) {
            return false;
        }
        Path path = (Path) o;
        return Objects.equals(this.path, path.path);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(path);
    }

}
