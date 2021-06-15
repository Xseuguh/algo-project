package Graph;

public class Edge {
    private int weight;
    private Vertex source;
    private Vertex destination;

    public Edge(int weight, Vertex source, Vertex destination) {
        this.source = source;
        this.weight = weight;
        this.destination = destination;
    }

    public int getWeight() {
        return this.weight;
    }

    public Vertex getDestination() {
        return this.destination;
    }

    public Vertex getSource() {
        return this.source;
    }

    public void setSource(Vertex source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "{" + ", source='" + getSource() + "'" + ", destination='" + getDestination() + "'" + ", weight='"
                + getWeight() + "'" + "}";
    }
}
