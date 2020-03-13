import java.io.IOException;

/**
 * Написать код парсинга страницы Википедии “Список станций Московского метрополитена”, который
 * будет на основе этой страницы создавать JSON-файл со списком станций по линиям и списком линий по
 * формату JSON-файла из проекта SPBMetro (файл map.json, приложен к домашнему заданию)
 *
 * Также пропарсить и вывести в JSON-файл переходы между станциями.
 *
 * Написать код, который прочитает созданный JSON-файл и напечатает количества станций на каждой
 * линии.
 */
public class Main {

  private static final String URL_STR =
      "https://ru.wikipedia.org/wiki/"
          + "%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA"
          + "_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9"
          + "_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE"
          + "_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";

  public static void main(String[] args) throws IOException {
    MetroParser metroParser = new MetroParser(URL_STR);
  }
}
