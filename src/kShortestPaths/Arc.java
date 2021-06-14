package kShortestPaths;

import Graph.Vertex;

public class Arc {
    private Vertex departure;
    private Vertex destination;
    private int capacity;

    public Arc() {
    }

    public Arc(Vertex departure, Vertex destination, int capacity) {
        this.departure = departure;
        this.destination = destination;
        this.capacity = capacity;
    }

    public Vertex getDeparture() {
        return this.departure;
    }

    public void setDeparture(Vertex departure) {
        this.departure = departure;
    }

    public Vertex getDestination() {
        return this.destination;
    }

    public void setDestination(Vertex destination) {
        this.destination = destination;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

}
