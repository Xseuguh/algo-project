package Graph;

public class Edge {
    private int weight;
    private Vertex destination;

    public Edge(int weight, Vertex destination) {
        this.weight = weight;
        this.destination = destination;
    }

    public int getWeight() {
        return this.weight;
    }

    public Vertex getDestination() {
        return this.destination;
    }
}
