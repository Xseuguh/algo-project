package Graph;

public class Vertex {
    private String name;
    private double longitude,latitude;
    private String id;

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
}
