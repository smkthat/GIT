import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import metro.Connection;
import metro.Line;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MetroParser {
  private String URL_STR;

  public MetroParser(String url) throws IOException {
    URL_STR = url;
    startParse();
  }

  private void startParse() throws IOException {
    System.out.println("Getting page data\nfrom " + URL_STR);

    Document doc = getDocument();
    if (doc == null) {
      throw new RuntimeException("Can't get document!");
    }

    Elements tablesRows = getTablesRows(doc);

    System.out.println("\t* getting unique line numbers");
    Set<String> lineNumbers = new LinkedHashSet<>();
    tablesRows.stream()
        .filter(tr -> !tr.select("td:nth-child(1) span:nth-child(1)").text().isEmpty())
        .forEach(d -> {
          String number = d.select("td:nth-child(1) span:nth-child(1)").text();
          lineNumbers.add(number);
        });

    System.out.println("\t* getting lines and stations");
    Set<Line> linesSet = new LinkedHashSet<>();
    Map<String, Set<String>> lineStations = new LinkedHashMap<>();
    lineNumbers.forEach(line -> {
      Set<String> stationsList = new LinkedHashSet<>();
      tablesRows.parallelStream()
          .filter(tr -> tr.select("td:nth-child(1) span:nth-child(1)").text().equals(line))
          .forEach(d -> {
            d.select("td:nth-child(2) small").remove();
            String stationName = d.select("td:nth-child(2)").text();
            if (!stationName.isEmpty()) {
              stationsList.add(stationName);
            }
          });

      lineStations.put(line, stationsList);

      tablesRows.parallelStream().filter(d ->
          d.select("td:nth-child(1) span:nth-child(1)").text().equals(line))
          .findFirst()
          .ifPresent(x -> {
            String lineName = x.select("td:nth-child(1) span:nth-child(2)").attr("title");
            linesSet.add(new Line(
                line,
                lineName,
                stationsList.size()));
          });
    });


    System.out.println("\t* getting connections");
    List<List<Connection>> connections = new ArrayList<>();
    tablesRows.parallelStream().filter(tr -> !tr.select("td:nth-child(4)").text().isEmpty())
        .forEach(tr -> {
          List<Connection> connList = new ArrayList<>();
          List<Element> elements = new ArrayList<>(tr.select("td:nth-child(4) span"));
          for (int i = 0; i < elements.size() - 1; i += 2) {
            String fullConnStationName = elements.get(i + 1).attr("title");
            connList.add(new Connection(
                elements.get(i).text(),
                getSimpleName(fullConnStationName))
            );
          }
          connections.add(connList);
        });

    System.out.println("\t* fill metro data");
    Map<String, Object> metro = new LinkedHashMap<>();
    metro.put("lines", linesSet);
    metro.put("stations", lineStations);
    metro.put("connections", connections);

    System.out.println("\t* writing metro.json file");
    ObjectMapper mapper = new ObjectMapper();
    mapper.writerWithDefaultPrettyPrinter().writeValue(new File("metro.json"), metro);
    System.out.println("\nComplete!");
  }

  private static String getSimpleName(String spanTitleAttribute) {
    return spanTitleAttribute.isEmpty() ? " - " : spanTitleAttribute.substring(
        spanTitleAttribute.indexOf("ю ") + 1);
  }

  private Elements getTablesRows(Document doc) {
    System.out.println("\t* getting data elements from tables");
    return doc.select(".standard.sortable tr");
  }

  private Document getDocument() {
    System.out.println("\t* getting document");
    Document doc;
    try {
      doc = Jsoup.connect(URL_STR).maxBodySize(0).get();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    return doc;
  }
}
