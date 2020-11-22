package Core;

import java.util.*;

public class MetroInfo {
    private LinkedHashMap<String, ArrayList<String>> stations;
    private ArrayList<Lines> lines;
    private HashSet<HashSet<Station>> connections;

    public MetroInfo(){
    }

    public LinkedHashMap<String, ArrayList<String>> getStations() {
        return stations;
    }

    public void setStations(LinkedHashMap<String, ArrayList<String>> stations) {
        this.stations = stations;
    }

    public ArrayList<Lines> getLines() {
        return lines;
    }

    public void setLines(ArrayList<Lines> lines) {
        this.lines = lines;
    }

    public HashSet<HashSet<Station>> getConnections() {
        return connections;
    }

    public void setConnections(HashSet<HashSet<Station>> connections) {
        this.connections = connections;
    }
}