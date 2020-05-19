import core.Line;
import core.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// import static junit.framework.TestCase.assertEquals;

/**
 * Metro scheme that used in tests:
 *
 * <pre>{@code
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *     (III)
 *     III-1
 *       ↓
 *     III-2
 *       ↓
 *     III-3
 *       ↓      (II)  (I)
 *   [ III-4 / II-1 / I-1 ]  →  I-2
 *               ↓               ↓
 *             II-2             I-3
 *               ↓               ↓
 *           [ II-3 / I-5 ] ← [ I-4 / IV-3 ]  →  IV-2  →  IV-1 (IV)
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  Where:  - [ X-x / X-x ] связь линий/станций
 *          - X-x станция
 *          - (X) линия
 *          - ↓ , ↑, ←→, → направления
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * }</pre>
 */
class RouteCalculatorTest {
  private RouteCalculator calculator;
  private StationIndex index;
  private Line line1, line2, line3, line4;

  @BeforeEach
  public void setUp() {
    index = new StationIndex();

    line1 = new Line(1, "I");
    line2 = new Line(2, "II");
    line3 = new Line(3, "III");
    line4 = new Line(4, "IV");

    // Линия I
    line1.addStation(new Station("I-1", line1)); // index 0
    line1.addStation(new Station("I-2", line1)); // index 1
    line1.addStation(new Station("I-3", line1)); // index 2
    line1.addStation(new Station("I-4", line1)); // index 3
    line1.addStation(new Station("I-5", line1)); // index 4
    // Линия II
    line2.addStation(new Station("II-1", line2)); // index 0
    line2.addStation(new Station("II-2", line2)); // index 1
    line2.addStation(new Station("II-3", line2)); // index 2
    // Линия III
    line3.addStation(new Station("III-1", line3)); // index 0
    line3.addStation(new Station("III-2", line3)); // index 1
    line3.addStation(new Station("III-3", line3)); // index 2
    line3.addStation(new Station("III-4", line3)); // index 3
    // Линия IV
    line4.addStation(new Station("IV-1", line4)); // index 0
    line4.addStation(new Station("IV-2", line4)); // index 1
    line4.addStation(new Station("IV-3", line4)); // index 2

    index.addLine(line1);
    index.addLine(line2);
    index.addLine(line3);
    index.addLine(line4);

    // Связи I и II линии
    index.addConnection(Arrays.asList(getStation("I-1"), getStation("II-1")));
    index.addConnection(Arrays.asList(getStation("I-5"), getStation("II-3")));
    // Связь I и III линии
    index.addConnection(Arrays.asList(getStation("I-1"), getStation("III-4")));
    // Связь II и III линии
    index.addConnection(Arrays.asList(getStation("II-1"), getStation("III-4")));
    // Связь I и IV линии
    index.addConnection(Arrays.asList(getStation("I-4"), getStation("IV-3")));

    calculator = new RouteCalculator(index);
  }

  private List<Station> getRoutWithTwoConnection() {
    return Arrays.asList(
        getStation("III-1"),
        getStation("III-2"),
        getStation("III-3"),
        getStation("III-4"),
        getStation("I-1"),
        getStation("I-2"),
        getStation("I-3"),
        getStation("I-4"),
        getStation("IV-3"),
        getStation("IV-2"),
        getStation("IV-1"));
  }

  private List<Station> getRouteWithOneConnection() {
    return Arrays.asList(
        getStation("III-1"),
        getStation("III-2"),
        getStation("III-3"),
        getStation("III-4"),
        getStation("I-1"),
        getStation("I-2"),
        getStation("I-3"),
        getStation("I-4"),
        getStation("I-5"));
  }

  private List<Station> getRouteWithoutConnection() {
    return Arrays.asList(
        getStation("III-1"), getStation("III-2"), getStation("III-3"), getStation("III-4"));
  }

  private Station getStation(String name) {
    String[] data = name.split("-");
    Line line = null;
    List<Station> stations;
    switch (data[0]) {
      case "I":
        {
          line = index.getLine(1);
          break;
        }
      case "II":
        {
          line = index.getLine(2);
          break;
        }
      case "III":
        {
          line = index.getLine(3);
          break;
        }
      case "IV":
        {
          line = index.getLine(4);
          break;
        }
    }

    if (line != null) {
      stations = line.getStations();
      int stationIndex = Integer.parseInt(data[1]) - 1;
      return stations.get(stationIndex);
    }
    return null;
  }

  @Test
  public void getShortestRouteWithOneConnections() {
    // тест с 1 пересадкой
    Station from = getStation("III-1");
    Station to = getStation("I-5");
    List<Station> actual = calculator.getShortestRoute(from, to);
    List<Station> expected = getRouteWithOneConnection();
    assertEquals(expected, actual);
  }

  @Test
  public void getShortestRouteWithTwoConnections() {
    // тест с 2 пересадками
    Station from = getStation("III-1");
    Station to = getStation("IV-1");
    List<Station> actual = calculator.getShortestRoute(from, to);
    List<Station> expected = getRoutWithTwoConnection();
    assertEquals(expected, actual);
  }

  @Test
  public void getShortestRouteAtOneStation() {
    // тест совпадения
    Station from = getStation("I-1");
    Station to = getStation("I-1");
    List<Station> actual = calculator.getShortestRoute(from, to);
    List<Station> expected = Collections.singletonList(from);
    assertEquals(expected, actual);
  }

  @Test
  public void getShortestRouteWithoutConnections() {
    // тест без пересадок
    Station from = getStation("III-1");
    Station to = getStation("III-4");
    List<Station> actual = calculator.getShortestRoute(from, to);
    List<Station> expected = getRouteWithoutConnection();
    assertEquals(expected, actual);
  }

  @Test
  public void getShortestRouteWithFromNULL() {
    Station from = null;
    Station to = getStation("III-4");
    List<Station> actual = calculator.getShortestRoute(from, to);
    List<Station> expected = null;
    assertEquals(expected, actual);
  }

  @Test
  public void getShortestRouteWithToNULL() {
    Station from = getStation("II-3");
    Station to = null;
    List<Station> actual = calculator.getShortestRoute(from, to);
    List<Station> expected = null;
    assertEquals(expected, actual);
  }

  private List<Station> getCalculateDurationTestRoute() {
    return Arrays.asList(
        getStation("III-1"), // 2.5
        getStation("III-2"), // 2.5
        getStation("III-3"), // 2.5
        getStation("III-4"), // 3.5
        getStation("I-1"), // 2.5
        getStation("I-2"), // 2.5
        getStation("I-3"), // 2.5
        getStation("I-4"), // 3.5
        getStation("IV-3"), // 2.5
        getStation("IV-2"), // 2.5
        getStation("IV-1"));
  }

  @Test
  public void calculateDuration() {
    List<Station> route = getCalculateDurationTestRoute();

    double stationTime = 3.5;
    double transitTime = 2.5;

    double actual = RouteCalculator.calculateDuration(route);
    double expected = stationTime * 2 + transitTime * 8;
    assertEquals(expected, actual);
  }
}
