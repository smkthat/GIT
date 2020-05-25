package metro;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class Connection {
  private String line;
  private String station;

  public Connection(String line, String station) {
    this.line = line;
    this.station = station;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Connection)) return false;
    Connection that = (Connection) o;
    return line.equals(that.line) &&
            Objects.equals(station, that.station);
  }

  @Override
  public int hashCode() {
    return Objects.hash(line, station);
  }

  public String getLine() {
    return line;
  }

  public void setLine(String line) {
    this.line = line;
  }

  public String getStation() {
    return station;
  }

  public void setStation(String station) {
    this.station = station;
  }
}
