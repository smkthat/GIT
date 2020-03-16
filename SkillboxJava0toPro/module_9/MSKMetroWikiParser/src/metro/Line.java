package metro;

import java.util.HashSet;
import java.util.Objects;

public class Line implements Comparable<Line>{
  private String number;
  private String name;
  private String color;
  private HashSet<Station> stations;

  public Line(String number, String name) {
    this.number = number;
    this.name = name;
    stations = new HashSet<>();
  }

  public String getNumber() {
    return number;
  }

  public String getName() {
    return name;
  }

  public String getColor() {
    return color;
  }

  public HashSet<Station> getStations() {
    return stations;
  }

  public void addStation(Station station) {
    stations.add(station);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Line)) return false;
    Line line = (Line) o;
    return number.equals(line.number) &&
            name.equals(line.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(number, name);
  }

  @Override
  public int compareTo(Line line) {
    return this.name.compareTo(line.getName());
  }

  @Override
  public String toString() {
    return "Line{" +
            "number='" + number + '\'' +
            ", name='" + name + '\'' +
            '}';
  }
}
