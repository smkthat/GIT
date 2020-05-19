import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class Main {

  private static final String URL_STR = "https://lenta.ru/";
  private static final String DOWNLOAD_PATH = "./downloads";
  private static Path PATH;

  public static void main(String[] args) {
    startParse();
  }

  private static void startParse() {
    System.out.println("Getting path from string");
    PATH = Paths.get(DOWNLOAD_PATH);

    System.out.println("Start getting images from " + URL_STR + " to " + PATH + " :");

    Document doc;
    try {
      doc = getDocument();
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    String tag = ".g-picture";
    System.out.println("\t* getting elements from document by tag \"" + tag + "\"");
    Elements elements = doc.select(tag);

    List<String> urls = getElementsAttrToList(elements, "src");
    if (downloadPics(urls)) {
      System.out.println("\nDownloading complete without errors! :)");
    } else {
      System.out.println(
          "\nDownloading complete with some errors! :("
              + "\nСheсk the console for detailed error information");
    }
  }

  private static boolean downloadPics(List<String> urls) {
    boolean withoutErrors = true;
    System.out.println("Downloading:");
    for (String u : urls) {
      try {
        download(u);
        System.out.println("\t* file\t" + u + " downloaded");
      } catch (IOException e) {
        withoutErrors = false;
        System.err.println("\t* error\t" + u);
      }
    }

    return withoutErrors;
  }

  private static void download(String strUrl) throws IOException {
    Path pathToFile = Paths.get(PATH.toString(), getNameFromURL(strUrl));
    BufferedInputStream bis = getBISFromURL(strUrl);
    if (bis != null) {
      writeFile(bis, pathToFile);
      bis.close();
    }
  }

  private static BufferedInputStream getBISFromURL(String strUrl) {
    try {
      URL url = new URL(strUrl);
      URLConnection connection = url.openConnection();
      connection.setDoOutput(true);
      connection.setDoInput(true);

      return new BufferedInputStream(connection.getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private static void writeFile(BufferedInputStream bis, Path path) {
    try {
      if (Files.exists(path)) {
        System.out.println("\t* file \"" + path + "\" exists, will be overwritten");
      } else {
        Files.createDirectories(PATH);
        Files.createFile(path);
      }

      Files.write(path, bis.readAllBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static String getNameFromURL(String url) {
    return url.substring(url.lastIndexOf("/") + 1);
  }

  private static List<String> getElementsAttrToList(Elements elements, String attr) {
    System.out.println("\t* getting img urls by attr \"" + attr + "\"");
    List<String> attrs = new ArrayList<>();
    for (Element e : elements) {
      attrs.add(e.attr(attr));
    }
    return attrs;
  }

  private static Document getDocument() throws IOException {
    System.out.println("\t* getting document");
    return Jsoup.connect(URL_STR).maxBodySize(0).get();
  }
}
