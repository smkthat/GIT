import metro.Connection;
import metro.Line;
import metro.Station;

import java.util.*;

public class Parser {
  private static TreeSet<Line> allLines = new TreeSet<>();
  private static TreeSet<Station> allStations = new TreeSet<>();
  private static Map<String, List<Station>> lineStations = new TreeMap<>();
  private static List<Connection> connections = new ArrayList<>();

  static void parseLineAndStation(String stationName, String lineName, List<String> lineNumbers) {
    for (String lineNumber : lineNumbers) {
      Line line = searchLine(lineNumber);
      if (line == null) {
        line = new Line(lineNumber, lineName);
        allLines.add(line);
      }

      Station station = searchStation(stationName, lineNumber);
      if (station == null) {
        station = new Station(stationName, line);
        allStations.add(station);
      }

      line.addStation(station);

      System.out.println(line.getName() + "\t" + station.getName());
    }
  }

  private static Station searchStation(String name, String lineNumber) {
    for (Station s : allStations) {
      if (s.getName().equals(name) && s.getLine().getNumber().equals(lineNumber)) {
        return s;
      }
    }

    return null;
  }

  private static Line searchLine(String number) {
    for (Line l : allLines) {
      if (l.getNumber().equals(number)) {
        return l;
      }
    }

    return null;
  }

  public static TreeSet<Line> getAllLines() {
    return allLines;
  }

  public static TreeSet<Station> getAllStations() {
    return allStations;
  }

  public static Map<String, List<Station>> getLineStations() {
    return lineStations;
  }

  public static List<Connection> getConnections() {
    return connections;
  }
}
