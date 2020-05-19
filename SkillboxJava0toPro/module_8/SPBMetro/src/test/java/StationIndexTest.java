import core.Line;
import core.Station;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

public class StationIndexTest extends TestCase {
    private StationIndex index;
    private Line testLine;
    private Station station;

    @Override
    protected void setUp() {
        index = new StationIndex();

        testLine = new Line(1, "Test Line");

        testLine.addStation(new Station("I-1", testLine)); // index 0
        testLine.addStation(new Station("I-2", testLine)); // index 1
        station = new Station("Test Station", testLine); // index 2

        index.addStation(station);
        index.addLine(testLine);
    }

    @Test
    public void testGetStation() {
        setUp();

        Station actual = index.getStation("Test Station");
        Station expected = station;
        assertEquals(expected,actual);

        actual = index.getStation("Test Station",1);
        assertEquals(expected,actual);
    }
}
