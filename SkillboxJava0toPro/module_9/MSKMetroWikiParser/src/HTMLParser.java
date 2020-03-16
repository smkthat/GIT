import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

public class HTMLParser {
  private String URL_STR;
  private String PATH = "dest";
  private static final String PATH_SEPARATOR = "\\";
  private List<List<Element>> tableDataList;

  private List<Line> lines;
  private List<String[]> connections;

  public HTMLParser(String url) {
    URL_STR = url;
    startParse();
  }

  private boolean checkPathTail() {
    return PATH.endsWith(PATH_SEPARATOR) || PATH.endsWith("/");
  }

  private void startParse() {
    System.out.println("Getting page data\nfrom " + URL_STR);
    if (!checkPathTail()) {
      PATH = PATH + PATH_SEPARATOR;
    }

    Document doc = getDocument();
    if (doc == null) {
      System.exit(-1);
    }

    Element table = getFirstTable(doc);
    Elements rows = getTableRows(table);
    createTableLists(rows);
    prepareDataForJASON();

    System.out.println(lines);
  }

  private boolean isFoundedLine;
  private boolean isFoundedStation;
  private void prepareDataForJASON() {
    lines = new ArrayList<>();
    connections = new ArrayList<>();
    for (List<Element> cell : tableDataList) {
      isFoundedLine = true;
      Line line = searchOrCreateLine(cell.get(0));
      Station station = searchOrCreateStation(cell.get(1),line);
      line.addStation(station);
      if (!isFoundedLine) {
        lines.add(line);
      }
      Line connectedLine = searchConnectedLines(cell.get(3));
    }
  }

  private Station searchOrCreateStation(Element element, Line line) {
      String name = element.select("span").select("a").text();
      for (Station s : line.getStations()) {
          if (s.getName().equals(name)) {
              isFoundedStation = true;
              return s;
          }
      }
      return new Station(name,line);
  }

    private Line searchConnectedLines(Element element) {
        Line line;
        String name = element.select("span").attr("title");
        String number = element.attr("data-sort-value");
        line = searchLine(name);
        if (line == null) {
            isFoundedLine = false;
            line = createLine(number, name);
        }

        return line;
    }

  private Line searchOrCreateLine(Element element) {
    Line line;
    String name = element.select("span").attr("title");
    String number = element.attr("data-sort-value");
    line = searchLine(name);
    if (line == null) {
      isFoundedLine = false;
      line = createLine(number, name);
    }

    return line;
  }

  private Line searchLine(String name) {
    for (Line l : lines) {
      if (l.getName().equals(name)) {
        return l;
      }
    }
    return null;
  }

  private Line createLine(String number, String name) {
    return new Line(number, name);
  }

  private void createTableLists(Elements rows) {
    tableDataList = new ArrayList<>();
    for (int i = 1; i < rows.size(); i++) {
      Element row = rows.get(i);
      Elements cols = row.select("td");
      List<Element> col = new ArrayList<>(cols);
      tableDataList.add(col);
    }
  }

  private Element getFirstTable(Document doc) {
    String tag1 = "table.standard.sortable";
    String tag2 = "tbody";
    System.out.println("\t* getting table by tags: " + tag1 + " + " + tag2);
    return doc.select(tag1).first().getElementsByTag(tag2).first();
  }

  private Elements getTableRows(Element table) {
    System.out.println("\t* getting children elements from table");
    return table.children();
  }

  private Document getDocument() {
    System.out.println("\t* getting document");
    Document doc;
    try {
      doc = Jsoup.connect(URL_STR).get();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    return doc;
  }
}
