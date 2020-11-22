import Core.Lines;
import Core.MetroInfo;
import Core.Station;
import com.google.gson.*;
import json.Deserializer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.File;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    private static final String URL = "https://www.moscowmap.ru/metro.html#lines";
    private static final String DATA_FILE = "src/main/resources/map.json";

    public static void main(String[] args) {

        Gson gson = new GsonBuilder()
                .setPrettyPrinting() // *.json prints text on a separate lines (not in one line)
                .registerTypeAdapter(String.class, new Deserializer()) // for deserialize
                .create();

        try {
            Files.createDirectories(Paths.get(DATA_FILE).getParent()); // create directory *.json
        } catch (IOException e) {
            System.err.println("Can not create directories for '*.json' - " + e);
        }

        try {
            Document doc = Jsoup.connect(URL).maxBodySize(0).get();
            PrintWriter pw = new PrintWriter(new File(DATA_FILE));
            pw.write(gson.toJson(createElements(doc.getElementById("metrodata"))));
            pw.flush();
            pw.close();

            StringBuilder sb = new StringBuilder();
            Files.readAllLines(new File(DATA_FILE).toPath()).forEach(sb::append);
            System.out.println(gson.fromJson(sb.toString(), String.class));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static MetroInfo createElements(Element element) {
        MetroInfo metroInfo = new MetroInfo();
        metroInfo.setLines(parseLines(element));
        metroInfo.setStations(parseStations(element));
        metroInfo.setConnections(parseConnections(element));
        return metroInfo;
    }

    private static HashSet<HashSet<Station>> parseConnections(Element metroInfo) {
        HashSet<HashSet<Station>> connections = new HashSet<>();

        metroInfo.select("div.t-metrostation-list-table").forEach(line -> {
            line.select("p").forEach(station -> {
                HashSet<Station> connectedStations = new HashSet<>();

                if (!station.select("span.t-icon-metroln").isEmpty()) {
                    Station from = new Station(line.attr("data-line"), station.select("span.name").text());
                    Station to = new Station(station.select("span.t-icon-metroln").attr("class").substring(18),
                            //18 symbols before lineNumber in HTML
                            getNameOfConnectedStation(station));
                    connectedStations.add(from);
                    connectedStations.add(to);
                    connections.add(connectedStations);
                }
            });
        });
        return connections;
    }

    private static LinkedHashMap<String, ArrayList<String>> parseStations(Element element) {
        LinkedHashMap<String, ArrayList<String>> stations = new LinkedHashMap<>();
        element.select("div.t-metrostation-list-table").forEach(el -> {
            String lineNumber = el.attr("data-line");

            ArrayList<String> stationsNames = new ArrayList<>();
            el.select("p").forEach(e -> {
                stationsNames.add(e.select("span.name").text());
            });

            stations.put(lineNumber, stationsNames);
        });
        return stations;
    }

    private static String getNameOfConnectedStation(Element element) {
        String description = element.toString();
        description = description.substring(description.indexOf("\u00ab") + 1); // \u00ab - «
        description = description.substring(0, description.indexOf("\u00bb")); // \u00bb - »
        return description; // text from '«' to '»'
    }

    private static ArrayList<Lines> parseLines(Element metrodata) {
        ArrayList<Lines> lines = new ArrayList<>();
        metrodata.select("div.js-toggle-depend").select("span").forEach(element -> {
            lines.add(new Lines(element.attr("data-line"), element.text()));
        });
        return lines;
    }
}