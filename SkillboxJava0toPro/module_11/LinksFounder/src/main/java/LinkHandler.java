public interface LinkHandler {
  int size();

  boolean isVisited(String link);

  void addVisited(String link);
}
