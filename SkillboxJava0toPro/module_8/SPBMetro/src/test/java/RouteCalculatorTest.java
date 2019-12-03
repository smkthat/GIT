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
 *   (III)
 *   III-1
 *     ↓
 *   III-2
 *     ↓
 *   III-3
 *     ↓       (II)  (I)
 * [ III-4 / II-1 / I-1 ]  →  I-2
 *             ↓               ↓
 *           II-2             I-3
 *             ↓               ↓
 *         [ II-3 / I-5 ]  ←  I-4
 *
 * }</pre>
 */

class RouteCalculatorTest extends TestCase {

  @Test
  public void testGetShortestRoute() {
    StationIndex index = new StationIndex();

    Line line1 = new Line(1, "I");
    Line line2 = new Line(2, "II");
    Line line3 = new Line(3, "III");

    Station from = new Station("III-1", line3);
    Station to = new Station("I-5", line1);

    // Линия I
    line1.addStation(new Station("I-1", line1)); // index 0
    line1.addStation(new Station("I-2", line1)); // index 1
    line1.addStation(new Station("I-3", line1)); // index 2
    line1.addStation(new Station("I-4", line1)); // index 3
    line1.addStation(to); // index 4
    // Линия II
    line2.addStation(new Station("II-1", line2)); // index 0
    line2.addStation(new Station("II-2", line2)); // index 1
    line2.addStation(new Station("II-3", line2)); // index 2
    // Линия III
    line3.addStation(from); // index 0
    line3.addStation(new Station("III-2", line3)); // index 1
    line3.addStation(new Station("III-3", line3)); // index 2
    line3.addStation(new Station("III-4", line3)); // index 3

    // Связи I и II линии
    index.addConnection(Arrays.asList(line1.getStations().get(0), line2.getStations().get(0)));
    index.addConnection(Arrays.asList(line1.getStations().get(4), line2.getStations().get(2)));
    // Связь I и III линии
    index.addConnection(Arrays.asList(line1.getStations().get(0), line3.getStations().get(3)));
    // Связь II и III линии
    index.addConnection(Arrays.asList(line2.getStations().get(0), line3.getStations().get(3)));

    index.addLine(line1);
    index.addLine(line2);

    RouteCalculator calculator = new RouteCalculator(index);
    List<Station> actual = calculator.getShortestRoute(from, to);
    List<Station> expected =
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
  }

  @Test
  public void testCalculateDuration() {
    List<Station> route = new ArrayList<>();

    Line line1 = new Line(1, "I");
    Line line2 = new Line(2, "II");
    Line line3 = new Line(3, "III");

    route.add(new Station("1-1", line1));
    // 2.5
    route.add(new Station("1-2", line1));
    // 3.5
    route.add(new Station("2-1", line2));
    // 2.5
    route.add(new Station("2-2", line2));
    // 2.5
    route.add(new Station("2-3", line2));
    // 3.5
    route.add(new Station("3-1", line3));
    // 2.5
    route.add(new Station("3-2", line3));

    double actual = RouteCalculator.calculateDuration(route);
    double expected = 17.0;
    assertEquals(expected, actual);
  }
}
