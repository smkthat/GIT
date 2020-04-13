import java.util.Set;

public class Link {
  int depthLvl;
  String link;
  Set<String> children;

  public Link(String link, Set<String> children, int depthLvl) {
    this.link = link;
    this.depthLvl = depthLvl;
    this.children = children;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder
        .append(link)
        .append(":\n");

    children.forEach(child -> stringBuilder.append("\t").append(child).append("\n"));

    return stringBuilder.toString();
  }

  public Set<String> getChildren() {
    return children;
  }
}
