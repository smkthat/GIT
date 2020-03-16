public class Station {
  private Line line;
  private String name;

  public Station(String name, Line line) {
    this.name = name;
    this.line = line;
  }

  public Line getLine() {
    return line;
  }

  public String getName() {
    return name;
  }
}
