package metro;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Line {
  private String number;
  private String name;
  private int stationsCount;

  public Line(String number, String name, int stationsCount) {
    this.number = number;
    this.name = name;
    this.stationsCount = stationsCount;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getStationsCount() {
    return stationsCount;
  }

  public void setStationsCount(int stationsCount) {
    this.stationsCount = stationsCount;
  }
}
