import core.Line;
import core.Station;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  private static final Logger LOGGER = LogManager.getLogger(Main.class);
  private static final String dataFile = "src/main/resources/map.json";
  private static Scanner scanner;

  private static StationIndex stationIndex;

  public static void main(String[] args) {
    try {
      LOGGER.trace("Старт программы");
      LOGGER.error("Тест вывода ошибки");
      RouteCalculator calculator = getRouteCalculator();
      System.out.println("Программа расчёта маршрутов метрополитена Санкт-Петербурга\n");
      scanner = new Scanner(System.in);
      for (; ; ) {
        Station from = takeStation("Введите станцию отправления:");
        Station to = takeStation("Введите станцию назначения:");

        List<Station> route = calculator.getShortestRoute(from, to);
        System.out.println("Маршрут:");
        printRoute(route);
        double durationTime = RouteCalculator.calculateDuration(route);
        System.out.println("Длительность: " + durationTime + " минут");

        LOGGER.info("Действительный маршрут: {} -> {}\tВремя: {}", from, to, durationTime);
      }
    } catch (Exception ex) {
      LOGGER.error(ex);
    }
  }

  private static RouteCalculator getRouteCalculator() {
    LOGGER.trace("Индексация станций");
    createStationIndex();
    return new RouteCalculator(stationIndex);
  }

  private static void printRoute(List<Station> route) {
    LOGGER.trace("Печать маршрута");

    Station previousStation = null;
    for (Station station : route) {
      if (previousStation != null) {
        Line prevLine = previousStation.getLine();
        Line nextLine = station.getLine();
        if (!prevLine.equals(nextLine)) {
          System.out.println(
              "\tПереход на станцию " + station.getName() + " (" + nextLine.getName() + " линия)");
        }
      }
      System.out.println("\t" + station.getName());
      previousStation = station;
    }
  }

  private static Station takeStation(String message) {
    for (; ; ) {
      System.out.println(message);
      String line = scanner.nextLine().trim();
      Station station = stationIndex.getStation(line);
      if (station != null) {
        LOGGER.info("Поиск станции: {}\tРезультат: {}", line, true);
        return station;
      }
      LOGGER.warn("Поиск станции: {}\tРезультат: {}", line, false);
      System.out.println("Станция не найдена :(");
    }
  }

  private static void createStationIndex() {
    stationIndex = new StationIndex();
    try {
      JSONParser parser = new JSONParser();
      JSONObject jsonData = (JSONObject) parser.parse(getJsonFile());

      JSONArray linesArray = (JSONArray) jsonData.get("lines");
      parseLines(linesArray);

      JSONObject stationsObject = (JSONObject) jsonData.get("stations");
      parseStations(stationsObject);

      JSONArray connectionsArray = (JSONArray) jsonData.get("connections");
      parseConnections(connectionsArray);
    } catch (Exception ex) {
      LOGGER.error("Ошибка парсинга map.json файла", ex);
      ex.printStackTrace();
    }
  }

  private static void parseConnections(JSONArray connectionsArray) {
    LOGGER.trace("Парсинг связей станций");
    connectionsArray.forEach(
        connectionObject -> {
          JSONArray connection = (JSONArray) connectionObject;
          List<Station> connectionStations = new ArrayList<>();
          connection.forEach(
              item -> {
                JSONObject itemObject = (JSONObject) item;
                int lineNumber = ((Long) itemObject.get("line")).intValue();
                String stationName = (String) itemObject.get("station");

                Station station = stationIndex.getStation(stationName, lineNumber);
                if (station == null) {
                  throw new IllegalArgumentException(
                      "core.Station " + stationName + " on line " + lineNumber + " not found");
                }
                connectionStations.add(station);
              });
          stationIndex.addConnection(connectionStations);
        });
  }

  private static void parseStations(JSONObject stationsObject) {
    LOGGER.trace("Парсинг станций");
    stationsObject
        .keySet()
        .forEach(
            lineNumberObject -> {
              int lineNumber = Integer.parseInt((String) lineNumberObject);
              Line line = stationIndex.getLine(lineNumber);
              JSONArray stationsArray = (JSONArray) stationsObject.get(lineNumberObject);
              stationsArray.forEach(
                  stationObject -> {
                    Station station = new Station((String) stationObject, line);
                    stationIndex.addStation(station);
                    line.addStation(station);
                  });
            });
  }

  private static void parseLines(JSONArray linesArray) {
    LOGGER.trace("Парсинг линий");
    linesArray.forEach(
        lineObject -> {
          JSONObject lineJsonObject = (JSONObject) lineObject;
          Line line =
              new Line(
                  ((Long) lineJsonObject.get("number")).intValue(),
                  (String) lineJsonObject.get("name"));
          stationIndex.addLine(line);
        });
  }

  private static String getJsonFile() {
    LOGGER.trace("Загрузка {}", dataFile);
    StringBuilder builder = new StringBuilder();
    try {
      List<String> lines = Files.readAllLines(Paths.get(dataFile));
      lines.forEach(builder::append);
    } catch (Exception ex) {
      LOGGER.error("Ошибка чтения json файла", ex);
    }
    return builder.toString();
  }
}
