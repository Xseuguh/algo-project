package Graph;

public class Edge {
    private int weight;
    private Vertex source;
    private Vertex destination;

    public Edge(int weight, Vertex destination) {
        this.weight = weight;
        this.destination = destination;
    }

    public Edge(Vertex source, Vertex destination) {
        this.source = source;
        this.destination = destination;
        this.weight = weightBetweenVertices(source, destination);
    }

    public int getWeight() {
        return this.weight;
    }

    public Vertex getDestination() {
        return this.destination;
    }

    public Vertex getSource()  {
        return this.source;
    }

    public void setSource(Vertex source) {
        this.source = source;
    }

    private int weightBetweenVertices(Vertex source, Vertex destination) {
        double sLatDeg = source.getLatitude();
        double sLongDeg = source.getLongitude();

        double dLatDeg = destination.getLatitude();
        double dLongDeg = destination.getLongitude();


        //Approximation by Pythagore
        double x = (sLongDeg - dLongDeg) * Math.cos(Math.toRadians((sLatDeg + dLatDeg) / 2));
        double y = sLatDeg - dLatDeg;

        double pythagoreDistance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) * 1852 * 60;

        return (int) Math.round(pythagoreDistance);
    }
}
