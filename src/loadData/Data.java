package loadData;

import Graph.Vertex;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Data {

    //We only want metro lines
    private static final String METRO_ROUTE_TYPE = "400";

    private Set<String> lineNames;

    //lineWithStations is a map to link a line name to its stations id
    //we create this be taking the trip with the higher stops number in sortedStopsForEachTrip
    private Map<String, List<String>> lineWithStations;

    private Set<String> stationNames;

    //Map to convert a station name to the vertex object
    private Map<String, Vertex> vertexFromStationName;

    public Data() throws IOException {
        String line;
        BufferedReader br;

        /*
            We get lines informations from routes.txt
         */
        String routesPath = "src/loadData/data/routes.txt";

        lineNames = new TreeSet<>();
        //We save all the lines (U1, U2, ...) with a dictionary id => name of the line
        Map<String, String> lineNameFromLineId = new TreeMap<>();
        br = new BufferedReader(new FileReader(routesPath));
        while ((line = br.readLine()) != null) {
            String[] dataSplit = parseData(line);

            String routeType = dataSplit[4];
            if (routeType.equals(METRO_ROUTE_TYPE)) {
                String lineId = dataSplit[0];
                String lineName = dataSplit[2];
                lineNameFromLineId.put(lineId, lineName);
                lineNames.add(lineName);
            }
        }
        System.out.println("There are " + lineNameFromLineId.size() + " lines in the Berlin subway: " + lineNameFromLineId.values());

        /*
             We get trips id for each lines
         */
        String tripsPath = "src/loadData/data/trips.txt";
        //We save for each line, the trips associated
        Map<String, Set<String>> tripsIdFromLineId = new TreeMap<>();
        //We save all the trips id to filter the useful stops
        Set<String> tripsIdSet = new HashSet<>();

        br = new BufferedReader(new FileReader(tripsPath));
        while ((line = br.readLine()) != null) {
            String[] dataSplit = parseData(line);

            String routeID = dataSplit[0];
            if (lineNameFromLineId.containsKey(routeID)) {
                String tripID = dataSplit[2];
                tripsIdSet.add(tripID);

                Set<String> tripsId = tripsIdFromLineId.getOrDefault(routeID, new TreeSet<>());
                tripsId.add(tripID);
                tripsIdFromLineId.put(routeID, tripsId);
            }
        }

        /*
            We get stops id for each trips
         */
        String stopTimesPath = "src/loadData/data/stop_times.txt";
        br = new BufferedReader(new FileReader(stopTimesPath));

        //We save for each trips, all the stops
        Map<String, List<String>> stopsIdByTripsId = new TreeMap<>();
        while ((line = br.readLine()) != null) {
            String[] dataSplit = parseData(line);

            String tripID = dataSplit[0];
            if (tripsIdSet.contains(tripID)) {
                String stopId = dataSplit[3];
                int index = Integer.parseInt(dataSplit[4]);

                List<String> stopsId = stopsIdByTripsId.getOrDefault(tripID, new ArrayList<>());
                stopsId.add(index, stopId);
                stopsIdByTripsId.put(tripID, stopsId);

            }
        }

        //We get the trip with the higher number of stops in order to get all the stops of each line
        //lineWithStops is a map to link a line name to its stops
        Map<String, List<String>> lineWithStops = new TreeMap<>();
        for (Map.Entry<String, Set<String>> entry : tripsIdFromLineId.entrySet()) {
            int maxStopsNumber = 0;
            List<String> lineStops = new ArrayList<>();
            for (String tripId : entry.getValue()) {
                List<String> stops = stopsIdByTripsId.get(tripId);
                if (stops.size() > maxStopsNumber) {
                    maxStopsNumber = stops.size();
                    lineStops = stops;
                }
            }
            String lineName = lineNameFromLineId.get(entry.getKey());
            lineWithStops.put(lineName, lineStops);
        }

        /*
            We get the information about each stops
         */
        String stopsPath = "src/loadData/data/stops.txt";
        br = new BufferedReader(new FileReader(stopsPath));

        vertexFromStationName = new TreeMap<>();
        //Map to convert a stop id to its station name
        Map<String, String> stationNameFromStopId = new HashMap<>();
//        //Map to convert a stop id to its station id
//        Map<String, String> stationIdFromStopId = new HashMap<>();
        stationNames = new TreeSet<>();
        while ((line = br.readLine()) != null) {
            String[] dataSplit = parseData(line);

            String stopID = dataSplit[0];
            for (List<String> sortedStopsMap : lineWithStops.values()) {
                if (sortedStopsMap.contains(stopID)) {
//                    String stopName = dataSplit[2];
                    String stopName = removeUnwantedInfoFromStationName(dataSplit[2]);
                    String parentID = dataSplit[7];

//                    stationNameFromStopId.put(stopID, parentID);
                    stationNameFromStopId.put(stopID, stopName);

                    double latitude = Double.parseDouble(dataSplit[4]);
                    double longitude = Double.parseDouble(dataSplit[5]);

//                    vertexFromStationId.put(parentID, new Vertex(stopName, latitude, longitude, parentID));
                    vertexFromStationName.put(stopName, new Vertex(stopName, latitude, longitude, parentID));

                    stationNames.add(stopName);
                }
            }
        }
        br.close();
        //End of files reading

        //We get the id of the stations from to the id of the stops
        lineWithStations = new TreeMap<>();
        for (String lineName : lineWithStops.keySet()) {
            List<String> stationsName = lineWithStops.get(lineName).stream()
                    .map(stationNameFromStopId::get)
                    .collect(Collectors.toList());
            this.lineWithStations.put(lineName, stationsName);
        }
    }

    private String[] parseData(String data) {
        return data.replace("\"", "")
                .replace(", ", " - ")
                .split(",");
    }

    private String removeUnwantedInfoFromStationName(String rawName) {
        String name = rawName.replace(" (Berlin)", "")
                .replace("S+U ", "")
                .replace("U ", "");
        for (String lineName : lineNames) {
            name = name.replace(" [" + lineName + "]", "");
        }
        return name;
    }

    public Vertex getVertexFromStationName(String stationId) {
        return this.vertexFromStationName.get(stationId);
    }

    public List<Vertex> getAllStations() {
        return new ArrayList<>(this.vertexFromStationName.values());
    }

    public Map<String, List<String>> getLineWithStations() {
        return this.lineWithStations;
    }

    public Vertex getStartingStationFromStationName(String stationName) {
        for (String stationId : vertexFromStationName.keySet()) {
            Vertex station = vertexFromStationName.get(stationId);
            if (station.getName().equals(stationName)) {
                return station;

            }
        }
        return null;
    }

    public void displayLinesWithStations() {
        for (String lineName : lineWithStations.keySet()) {
            List<String> stationsId = lineWithStations.get(lineName);
            System.out.println(lineName + " (" + stationsId.size() + " stations):");
            for (String stationId : stationsId) {
                System.out.println("\t- " + vertexFromStationName.get(stationId).getName());
            }
        }
    }

    public Set<String> getStationNames(){
        return this.stationNames;
    }
}
