import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class Parser {

  private ExecutorService executor;
  private String webLink;
  int lvl = 0;

  private Map<String, Link> siteMapLinks;

  public Parser(String webLink) {
    this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    this.webLink = webLink;
    siteMapLinks = new TreeMap<>();
  }

  private String checkAndFixLink(String link) {
    if (!link.startsWith("http")) {
      link = "https://" + link;
    }

    if (link.endsWith("/")) {
      link = link.substring(0, link.lastIndexOf("/"));
    }

    return link;
  }

  public void startParse() {
    parseLink(webLink);
    //siteMapLinks.values().forEach(System.out::println);
  }

  private String getTabs(int count) {
    return "\t".repeat(Math.max(0, count));
  }

  private void parseLink(String link) {
    link = checkAndFixLink(link);
    System.out.println(link);
    Elements elements = getElements(link);
    if (elements == null) {
      return;
    }

    Set<String> children = gettingLinksFromElements(link, elements);
    siteMapLinks.put(link, new Link(link, children, lvl));
    children.forEach(this::parseLink);
  }

  private Set<String> gettingLinksFromElements(String parentLink, Elements elements) {
    Set<String> linksSet = new TreeSet<>();

    for (Element element : elements) {
      //получаем значение атрибута по ключу
      String attrKey = element.attr("href").replaceAll("[#?].*", "");
      //фильтруем полученные значения
      String childLink = null;
      if (attrKey.length() > 2) {
        if (attrKey.length() > 19 && attrKey.substring(0, 19).equals(parentLink)) {
          childLink = attrKey;
        } else if (attrKey.startsWith("/")) {
          attrKey = attrKey.replaceAll("/","");
          if (!parentLink.endsWith(attrKey)) {
            childLink = parentLink + attrKey;
          }
        }
      }

      if (childLink != null) {
        linksSet.add(childLink);
      }
    }

    return linksSet;
  }

  private Elements getElements(String url) {
    Document doc = null;
    try {
      doc = Jsoup.connect(url).get();
    } catch (HttpStatusException | UnsupportedMimeTypeException ignored) {
      return null;
    } catch (IOException e) {
      e.printStackTrace();
    }

    assert (doc != null) : "-> \tWarning! Connection error";
    return doc.select("a[href]");
  }
}