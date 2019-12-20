import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class Main {
  private static final String URL_STR = "https://lenta.ru/";
  private static String PATH = "d:\\test";
  private static final String PATH_SEPARATOR = "\\";

  public static void main(String[] args) {
    startParse();
  }

  private static boolean checkPathTail() {
    return PATH.endsWith(PATH_SEPARATOR) || PATH.endsWith("/");
  }

  private static void startParse() {
    System.out.println("Start getting images\nfrom " + URL_STR + "\n  to " + PATH);
    if (!checkPathTail()) {
      PATH = PATH + PATH_SEPARATOR;
    }

    Document doc = getDocument();
    if (doc == null) {
      System.exit(-1);
    }

    Elements elements = getSelected(doc, ".g-picture");
    List<String> urls = getElementsAttrToList(elements, "src");
    if (downloadPics(urls)) {
      System.out.println("\nDownloading complete without errors! :)");
    } else {
      System.out.println(
          "\nDownloading complete with some errors! :("
              + "\nСheck the console for detailed error information");
    }
  }

  private static boolean downloadPics(List<String> urls) {
    boolean withoutErrors = true;
    System.out.println("Downloading:");
    for (String u : urls) {
      if (download(u)) {
        System.out.println("\t* file\t" + u + " downloaded");
      } else {
        withoutErrors = false;
        System.err.println("\t* error\t" + u);
      }
    }

    return withoutErrors;
  }

  private static boolean download(String strUrl) {
    boolean isWrited = false;
    Path pathToFile = Paths.get(PATH + getNameFromURL(strUrl));
    BufferedInputStream bis = getBISFromURL(strUrl);
    if (bis != null) {
      isWrited = writeFile(bis, pathToFile);
    }

    return isWrited;
  }

  private static BufferedInputStream getBISFromURL(String strUrl) {
    URL url;
    URLConnection connection;
    BufferedInputStream bis;

    try {
      url = new URL(strUrl);
      connection = url.openConnection();
      connection.setDoOutput(true);
      connection.setDoInput(true);

      bis = new BufferedInputStream(connection.getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }

    return bis;
  }

  private static boolean writeFile(BufferedInputStream bis, Path path) {
    try {
      if (Files.exists(path)) {
        System.out.println("\t* file \"" + path + "\" exists, will be overwritten");
      } else {
        Files.createDirectories(Paths.get(PATH));
        Files.createFile(path);
      }

      Files.write(path, bis.readAllBytes());
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  private static String getNameFromURL(String url) {
    return url.substring(url.lastIndexOf("/") + 1);
  }

  private static Elements getSelected(Document doc, String tag) {
    System.out.println("\t* getting elements from document by tag \"" + tag + "\"");
    return doc.select(tag);
  }

  private static List<String> getElementsAttrToList(Elements elements, String attr) {
    System.out.println("\t* getting img urls by attr \"" + attr + "\"");
    List<String> attrs = new ArrayList<>();
    for (Element e : elements) {
      attrs.add(e.attr(attr));
    }
    return attrs;
  }

  private static Document getDocument() {
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
