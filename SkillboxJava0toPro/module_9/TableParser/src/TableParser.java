import java.nio.file.*;
import java.util.*;
import java.io.*;

public class TableParser {
  private Path path;
  private MovementStats stats;
  private int lineLength;

  public TableParser(String path) {
    this.path = getAndCheckPathToFile(path);
  }

  public void startParse() {
    if (path == null) {
      System.err.println("Please, check path to file!");
    } else {
      loadDataFromFile();
    }
  }

  private void loadDataFromFile() {
    List<String> lines = new ArrayList<>();
    try {
      lines = Files.readAllLines(path);
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (lines.size() > 1) {
      cutHeadAndCreateStats(lines)
          .forEach(
              line -> {
                try {
                  List<String> dataLine = CSVHelper.parseLine(new StringReader(line));
                  if (dataLine != null && dataLine.size() == lineLength) {
                    parseDataLine(dataLine);
                  } else {
                    throw new IOException("Wrong line: " + line);
                  }
                } catch (IOException e) {
                  System.err.println(e.getMessage());
                }
              });
    } else {
      System.err.println("Empty file data!");
    }
  }

  private List<String> cutHeadAndCreateStats(List<String> lines) {
    try {
      if (!lines.isEmpty()) {
        List<String> head = CSVHelper.parseLine(new StringReader(lines.get(0)));
        if (head != null) {
          lineLength = head.size();
          stats = new MovementStats(head);
          lines.remove(0);
        }
      }
    } catch (IOException e) {
      System.err.println("File is empty!");
    }

    return lines;
  }

  private void parseDataLine(List<String> dataLine) {
    if (dataLine == null) {
      return;
    }

    stats.addMovement(
        new Movement(
            dataLine.get(0),
            dataLine.get(1),
            dataLine.get(2),
            dataLine.get(3),
            dataLine.get(4),
            dataLine.get(5),
            Double.parseDouble(dataLine.get(6).replace(",", ".")),
            Double.parseDouble(dataLine.get(7).replace(",", "."))));
  }

  public String parsePartner(String data) {
    return null;
  }

  public MovementStats getStats() {
    return stats;
  }

  private Path getAndCheckPathToFile(String path) {
    Path filePath = Paths.get(path);
    if (Files.exists(filePath)) {
      return filePath;
    } else {
      System.err.println("File not found!");
    }

    return null;
  }
}
