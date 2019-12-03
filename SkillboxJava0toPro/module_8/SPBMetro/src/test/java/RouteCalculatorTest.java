import core.Line;
import core.Station;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
 *          - ↓ , ↑, ←→, -→ направления
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * }</pre>
 */
class RouteCalculatorTest extends TestCase {
  private RouteCalculator calculator;
  private Line line1, line2, line3, line4;

  @Override
  protected void setUp() {
    StationIndex index = new StationIndex();

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

    // Связи I и II линии
    index.addConnection(Arrays.asList(line1.getStations().get(0), line2.getStations().get(0)));
    index.addConnection(Arrays.asList(line1.getStations().get(4), line2.getStations().get(2)));
    // Связь I и III линии
    index.addConnection(Arrays.asList(line1.getStations().get(0), line3.getStations().get(3)));
    // Связь II и III линии
    index.addConnection(Arrays.asList(line2.getStations().get(0), line3.getStations().get(3)));
    // Связь I и IV линии
    index.addConnection(Arrays.asList(line1.getStations().get(3), line4.getStations().get(2)));

    index.addLine(line1);
    index.addLine(line2);
    index.addLine(line3);
    index.addLine(line4);

    calculator = new RouteCalculator(index);
  }

  @Test
  public void testGetShortestRoute() {
    setUp();

    List<Station> actual;
    List<Station> expected;
    Station from;
    Station to;

    // тест без пересадок
    from = line3.getStations().get(0); // III-1
    to = line3.getStations().get(3); // III-4
    actual = calculator.getShortestRoute(from, to);
    expected =
        Arrays.asList(
            line3.getStations().get(0),
            line3.getStations().get(1),
            line3.getStations().get(2),
            line3.getStations().get(3));
    assertEquals(expected, actual);

    // тест с 1 пересадкой
    from = line3.getStations().get(0); // III-1
    to = line1.getStations().get(4); // I-5
    actual = calculator.getShortestRoute(from, to);
    expected =
        Arrays.asList(
            line3.getStations().get(0),
            line3.getStations().get(1),
            line3.getStations().get(2),
            line3.getStations().get(3),
            line1.getStations().get(0),
            line1.getStations().get(1),
            line1.getStations().get(2),
            line1.getStations().get(3),
            line1.getStations().get(4));
    assertEquals(expected, actual);

    // тест с 2 пересадками
    from = line3.getStations().get(0); // III-1
    to = line4.getStations().get(0); // IV-1
    actual = calculator.getShortestRoute(from, to);
    expected =
        Arrays.asList(
            line3.getStations().get(0),
            line3.getStations().get(1),
            line3.getStations().get(2),
            line3.getStations().get(3),
            line1.getStations().get(0),
            line1.getStations().get(1),
            line1.getStations().get(2),
            line1.getStations().get(3),
            line4.getStations().get(2),
            line4.getStations().get(1),
            line4.getStations().get(0));
    assertEquals(expected, actual);
  }

  @Test
  public void testCalculateDuration() {
    setUp();

    List<Station> route = new ArrayList<>();

    route.add(line3.getStations().get(0));
    //2.5
    route.add(line3.getStations().get(1));
    //2.5
    route.add(line3.getStations().get(2));
    //2.5
    route.add(line3.getStations().get(3));
    //3.5
    route.add(line1.getStations().get(0));
    //2.5
    route.add(line1.getStations().get(1));
    //2.5
    route.add(line1.getStations().get(2));
    //2.5
    route.add(line1.getStations().get(3));
    //3.5
    route.add(line4.getStations().get(2));
    //2.5
    route.add(line4.getStations().get(1));
    //2.5
    route.add(line4.getStations().get(0));

    double actual = RouteCalculator.calculateDuration(route);
    double expected = 27.0;
    assertEquals(expected, actual);
  }
}
