public class Main {

  private static final String URL_STR =
      "https://ru.wikipedia.org/wiki/"
          + "%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA"
          + "_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9"
          + "_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE"
          + "_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";

  public static void main(String[] args) {
    HTMLParser htmlParser = new HTMLParser(URL_STR);

  }
}
