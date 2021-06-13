package Graph;

public class Vertex implements Comparable<Vertex>{
    private String name;
    private double longitude,latitude;
    private String id;
    private int minDistance = Integer.MAX_VALUE;

    public Vertex(String name, double latitude, double longitude, String id){
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getId() {
        return id;
    }

    public int getMinDistance() {
        return this.minDistance;
    }

    public void setMinDistance(int minDistance) {
        this.minDistance = minDistance;
    }

    public int compareTo(Vertex vertex) {
        return Integer.compare(minDistance, vertex.minDistance);
    }


}
