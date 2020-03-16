import metro.Line;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.TreeSet;

public class JSOUPParser {
  private String URL_STR;
  private String PATH = "dest";
  private static final String PATH_SEPARATOR = "\\";

  public JSOUPParser(String url) {
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

    Element table = getTable(doc);
    Elements rows = getTableRows(table);

    rows.remove(0); // removed head of table
    rows.forEach(
        (row) -> {
          Elements columns = row.select("td");
          String stationName = columns.get(1).child(0).text();
          String lineName = columns.get(0).child(1).attr("title");
          List<String> lineNumbers = columns.get(0).children().eachText();
          List<String> connectionNumbers = columns.get(3).children().eachText();

          Parser.parseLineAndStation(stationName,lineName,lineNumbers);
        });

    TreeSet<Line> lines = Parser.getAllLines();
    System.out.println();
  }

  private Element getTable(Document doc) {
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
