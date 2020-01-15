import java.sql.*;
import java.util.*;
import java.util.regex.*;

class JDBC implements AutoCloseable {

  private Connection connection;

  @Override
  public void close() throws Exception {
    if (connection != null) {
      connection.close();
    }
  }

  // Database credentials
  private static final String USER = "root";
  private static final String PASS = "root";

  // JDBC database URL
  private static final String DB_HOST = "jdbc:mysql://localhost";
  private static final String DB_PORT = "3333";
  private static final String DB_NAME = "courses";
  private static final String SRV_TIMEZONE = "serverTimezone=UTC";
  private static final String DB_URL_STRING =
      DB_HOST
          + ":"
          + DB_PORT
          + "/"
          + DB_NAME
          + "?user="
          + USER
          + "&password="
          + PASS
          + "&"
          + SRV_TIMEZONE;

  // Console helpers
  private static final String CNSL = ">\t";

  public JDBC() {
    consoleMessage("Connecting to a \"" + DB_NAME + "\" database...");
    try {
      connection = DriverManager.getConnection(DB_URL_STRING);
      consoleMessage("Connected to database successfully...");
    } catch (SQLException e) {
      System.err.println("Connected to database unsuccessfully!");
      throw new RuntimeException(e);
    }
  }

  public void printTableFromQuery(String query) {
    consoleMessage("Execute query...\n");
    System.err.println(query);

    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    List<String> headList = null;

    if (query.toUpperCase().contains("SELECT *")) {
      System.out.println("Please set the table columns to search in the query");
    } else if (query.toUpperCase().contains("SHOW")) {
      headList = Collections.singletonList("Tables_in_" + DB_NAME);
    } else if (query.toUpperCase().contains("SELECT")) {
      headList = getHeadFromSELECT(query);
    } else if (query.toUpperCase().contains("DESCRIBE")) {
      headList = Arrays.asList("Field", "Type", "Null", "Key", "Default", "Extra");
    }

    if (headList != null && !headList.isEmpty()) {
      try (Statement statement = connection.createStatement();
          ResultSet resultSet = statement.executeQuery(query)) {
        List<List<String>> data = getDataFromResultSet(resultSet, headList);
        System.out.println(new TableGenerator(headList, data).getResult());
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("Nothing to print :(");
    }
  }

  private void consoleMessage(String message) {
    System.out.println(CNSL + message);
  }

  private List<String> getHeadFromSELECT(String query) {
    List<String> list = new ArrayList<>();
    query = query.substring(6, query.indexOf("FROM")).trim();
    String pattern;
    Matcher matcher;

    if (query.contains("(")) {
      pattern = "[^(.,:;\"'/|]\\s(\\w+,|\\w+$|\".+\",|\".+\"$)";
      matcher = Pattern.compile(pattern).matcher(query);

      while (matcher.find()) {
        query = matcher.group().replaceAll("[\"',]", "").substring(2);
        list.add(query);
      }

    } else {
      pattern = "(\\w+,|\\w+$|\".+\",|\".+\"$)";
      matcher = Pattern.compile(pattern).matcher(query);

      while (matcher.find()) {
        query = matcher.group().replaceAll("[\"',]", "");
        list.add(query);
      }
    }

    return list;
  }

  private List<List<String>> getDataFromResultSet(ResultSet set, List<String> head)
      throws SQLException {
    List<List<String>> data = new ArrayList<>();
    while (set.next()) {
      List<String> row = new ArrayList<>();
      for (String s : head) {
        String str = set.getString(s);
        row.add(Objects.requireNonNullElse(str, ""));
      }
      data.add(row);
    }

    return data;
  }
}
