package metro;


public class Line {
  private String number;
  private String name;
  private String color;
  private int stationsCount;

  public Line(String number, String name, String color, int stationsCount) {
    this.number = number;
    this.name = name;
    this.color = color;
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

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public int getStationsCount() {
    return stationsCount;
  }

  public void setStationsCount(int stationsCount) {
    this.stationsCount = stationsCount;
  }
}
