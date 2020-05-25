import java.io.IOException;

/**
 * Написать код парсинга страницы Википедии “Список станций Московского метрополитена”, который
 * будет на основе этой страницы создавать JSON-файл со списком станций по линиям и списком линий по
 * формату JSON-файла из проекта SPBMetro (файл map.json, приложен к домашнему заданию)
 *
 * <p>Также пропарсить и вывести в JSON-файл переходы между станциями.
 *
 * <p>Написать код, который прочитает созданный JSON-файл и напечатает количества станций на каждой
 * линии.
 */
public class Loader {

  public static void main(String[] args) throws IOException {
    MetroParser metroParser = new MetroParser();
    metroParser.startParse();
  }
}
