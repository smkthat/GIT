import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import metro.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import utils.ColorUtils;

public class MetroParser {
  private Map<String, Set<String>> lineStations;
  private Set<Line> linesSet;
  private Set<String> uniquelineNumbers;
  private List<Set<Connection>> connections;

  private final String URL =
      "https://ru.wikipedia.org/wiki/"
          + "%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA"
          + "_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9"
          + "_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE"
          + "_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";

  public MetroParser() {
    // init
  }

  public void startParse() {
    Document doc = getDocument();
    if (doc == null) {
      throw new RuntimeException("Can't get document!");
    }

    System.out.println("Getting page data from\n\"" + doc.title() + "\"");
    Elements tablesRows = getRowsFromTables(doc);

    System.out.println("\t* getting unique line numbers");
    parseUniqueLineNumbers(tablesRows);

    System.out.println("\t* getting lines and stations");
    parseLinesAndStations(tablesRows);

    System.out.println("\t* getting connections");
    parseConnections(tablesRows);

    System.out.println("\t* fill metro data");
    Map<String, Object> metro = new LinkedHashMap<>();
    metro.put("lines", linesSet);
    metro.put("stations", lineStations);
    metro.put("connections", connections);

    System.out.println("\t* writing metro.json file");
    writeMetroJson(metro);

    System.out.println("\nComplete!");
  }

  private void parseUniqueLineNumbers(Elements tablesRows) {
    uniquelineNumbers = new LinkedHashSet<>();
    tablesRows.forEach(
        row -> {
          Elements sortKeys = row.select("td:nth-child(1) span.sortkey");
          sortKeys.remove(sortKeys.last());
          sortKeys.forEach(
              sortKey -> {
                if (!sortKey.text().isEmpty()) {
                  uniquelineNumbers.add(sortKey.text());
                }
              });
        });
  }

  private void parseLinesAndStations(Elements tablesRows) {
    linesSet = new LinkedHashSet<>();
    lineStations = new HashMap<>();

    uniquelineNumbers.forEach(
        line -> {
          tablesRows
              .parallelStream()
              .filter(row -> row.select("td:nth-child(1) span:nth-child(1)").text().equals(line))
              .forEach(
                  row -> {
                    row.select("td:nth-child(2) small").remove();
                    String stationName = row.select("td:nth-child(2)").text();
                    Elements sortKeys = row.select("td:nth-child(1) span.sortkey");
                    sortKeys.remove(sortKeys.last());

                    sortKeys.forEach(
                        sortKey -> {
                          String lineName = sortKey.text();
                          lineStations
                              .computeIfAbsent(lineName, key -> new LinkedHashSet<>())
                              .add(stationName);
                        });
                  });

          tablesRows
              .parallelStream()
              .filter(row -> row.select("td:nth-child(1) span:nth-child(1)").text().equals(line))
              .findFirst()
              .ifPresent(
                  x -> {
                    String lineName = x.select("td:nth-child(1) span:nth-child(2)").attr("title");
                    String lineColorName;
                    String colorHex =
                        x.select("td:nth-child(1)").attr("style").replace("background:", "");

                    if (colorHex.isEmpty()) {
                      lineColorName = "no color";
                    } else {
                      lineColorName =
                          new ColorUtils().getColorNameFromColor(Color.decode(colorHex));
                    }

                    linesSet.add(
                        new Line(line, lineName, lineColorName, lineStations.get(line).size()));
                  });
        });
  }

  private void parseConnections(Elements tablesRows) {
    connections = new ArrayList<>();

    tablesRows
        .parallelStream()
        .filter(tr -> !tr.select("td:nth-child(4)").text().isEmpty())
        .forEach(
            row -> {
              Set<Connection> connList = new LinkedHashSet<>();
              String line = row.select("td:nth-child(1) span.sortkey").first().text();
              String lineName = row.select("td:nth-child(2)").text();
              connList.add(new Connection(line, lineName));

              Elements sortKeys = row.select("td:nth-child(4) span.sortkey");
              List<String> connStationNames =
                  row.select("td:nth-child(4) span[title]").stream()
                      .map(t -> t.attr("title"))
                      .collect(Collectors.toList());

              AtomicInteger counter = new AtomicInteger();
              sortKeys.forEach(
                  sortKey -> {
                    String connLine = sortKey.text();
                    String fullConnStationName = connStationNames.get(counter.get());
                    String connStation =
                        getConnStationName(
                            lineStations.get(connLine), getSimpleName(fullConnStationName));
                    connList.add(new Connection(connLine, connStation));
                    counter.getAndIncrement();
                  });

              connections.add(connList);
            });
  }

  private void writeMetroJson(Map<String, Object> metro) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.writerWithDefaultPrettyPrinter().writeValue(new File("metro.json"), metro);
    } catch (IOException e) {
      throw new RuntimeException("Can't write json file!\n\n" + Arrays.toString(e.getStackTrace()));
    }
  }

  private static String getConnStationName(Set<String> stations, String simpleName) {
    for (String s : stations) {
      if (s.startsWith(simpleName)) {
        return s;
      }
    }
    return "???";
  }

  private static String getSimpleName(String fullConnStationName) {
    String name = "???";
    if (!fullConnStationName.isEmpty()) {
      name = fullConnStationName.substring(fullConnStationName.indexOf("ю ") + 1).trim();
      name = name.substring(0, name.indexOf(' '));
    }

    return name;
  }

  private Elements getRowsFromTables(Document doc) {
    System.out.println("\t* getting data elements from tables");
    Elements tRows = doc.select(".standard.sortable tr");
    tRows.remove(0);
    return tRows;
  }

  private Document getDocument() {
    System.out.println("\t* getting document");
    Document doc;
    try {
      doc = Jsoup.connect(URL).maxBodySize(0).get();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    return doc;
  }
}
