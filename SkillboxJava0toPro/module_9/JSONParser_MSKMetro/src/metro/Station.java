package metro;

import java.util.Objects;

public class Station implements Comparable<Station> {
  private String name;
  private Line line;

  public Station(String name, Line line) {
    this.name = name;
    this.line = line;
  }

  public String getName() {
    return name;
  }

  public Line getLine() {
    return line;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Station)) return false;
    Station station = (Station) o;
    return name.equals(station.name) &&
            line.equals(station.line);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, line);
  }

  @Override
  public int compareTo(Station station) {
    return this.name.compareTo(station.getName());
  }

  @Override
  public String toString() {
    return "Station{" +
            "name='" + name + '\'' +
            ", line=" + line +
            '}';
  }
}
