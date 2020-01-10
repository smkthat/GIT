import java.sql.*;
import java.util.*;
import java.util.regex.*;

class JDBC {
  // Resources
  private Statement statement;
  private Connection connection;
  private ResultSet resultSet;

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
        query = matcher.group().replaceAll("[\",]", "").substring(2);
        list.add(query);
      }

    } else {
      pattern = "(\\w+,|\\w+$|\".+\",|\".+\"$)";
      matcher = Pattern.compile(pattern).matcher(query);

      while (matcher.find()) {
        query = matcher.group().replaceAll("[\",]", "");
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
        row.add(set.getString(s));
      }
      data.add(row);
    }

    return data;
  }

  public void printTableFromQuery(String query) {
    try {
      if (gettingResources()) {
        consoleMessage("Execute query...\n");
        System.err.println(query);

        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        List<String> headList = null;

        if (query.contains("SELECT *")) {
          System.out.println("Please set the table columns to search in the query");
        } else if (query.contains("SHOW")) {
          headList = Collections.singletonList("Tables_in_" + DB_NAME);
        } else if (query.contains("SELECT")) {
          headList = getHeadFromSELECT(query);
        }

        if (headList != null && !headList.isEmpty()) {
          resultSet = statement.executeQuery(query);
          System.out.println(new TableGenerator(headList, getDataFromResultSet(resultSet, headList)).getResult());
        } else {
          System.out.println("Nothing to print :(");
        }
      }

      closeResources();
    } catch (SQLException se) {
      se.printStackTrace();
    } // Handle errors for Class.forName
    finally {
      // finally block used to close resources
      try {
        if (statement != null) {
          connection.close();
        }
      } catch (SQLException ignored) {
      }
      try {
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
  }

  private void closeResources() throws SQLException {
    System.out.println();
    consoleMessage("Closing connection, statement and result set...");

    resultSet.close();
    statement.close();
    connection.close();
    consoleMessage("Closing successfully...\n\n");
  }

  private boolean gettingResources() {
    connection = null;
    statement = null;
    try {
      consoleMessage("Connecting to a \"" + DB_NAME + "\" database...");
      connection = DriverManager.getConnection(DB_URL_STRING);
      consoleMessage("Connected database successfully...");

      consoleMessage("Getting statement in given database...");
      statement = connection.createStatement();
    } catch (Exception se) {
      se.printStackTrace();
      return false;
    }

    return true;
  }
}
