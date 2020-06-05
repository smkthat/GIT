package Main.Java;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * КЛАСС ГЕНЕРИРОВАНИЯ БЛАТНЫХ НОМЕРОВ
 */
class RogueNumberGenerator {

  private static List<String> numbers;

  RogueNumberGenerator() {
    generateNumbersDB();
  }

  List<String> getRogueNumbersDB() {
    return numbers;
  }

  /**
   * ГЕНЕРАТОР БУКВ. На автомобильных номерных знаках в России используются только те буквы, которые
   * присутствуют и в кириллице, и в латинице. Таких букв только 12 — А, В, Е, К, М, Н, О, Р, С, Т,
   * У, Х. Подобное ограничение регулируется венской Конвенцией о дорожном движении 1968 года.
   **/
  private static List<String> generateABC() {
    out.print("Генерация букв...");
    return Arrays.asList("АВЕКМНОРСТУХ".split(""));
  }

  /**
   * ГЕНЕРАТОР НОМЕРОВ. На мой взгляд, блатной номер должен быть с нулями, зеркальным или состоять
   * из 3х одинаковых цифор. Например: 001, 010, 111
   */
  private static List<String> generateNumbers() {
    out.print("Генерация номеров...");
    ArrayList<String> num = new ArrayList<>();
    IntStream.range(1, 10).forEach(n ->
        num.add(String.format("%03d", n))
    );

    IntStream.range(1, 10).forEach(n -> {
      final int i = 100 * n;
      num.add(Integer.toString(i));
    });

    IntStream.range(1, 10).forEach(n -> {
      final int i = 111 * n;
      num.add(Integer.toString(i));
    });

    return num;
  }

  /**
   * ГЕНЕРАТОР РЕГИОНОВ. Т.к. если использовать только регионы России, то база номеров будет слишком
   * мала. Поэтому, генерируем регионы от 0 до 1000
   */
  private static List<String> generateRegion() {
    out.print("Генерация регионов...");
    ArrayList<String> region = new ArrayList<>();
    IntStream.range(1, 10).forEach(k -> region.add(String.format("%03d", k)));
    IntStream.range(10, 100).forEach(k -> region.add(String.format("%03d", k)));
    IntStream.range(100, 1000).forEach(k -> region.add(Integer.toString(k)));

    return region;
  }

  /**
   * ГЕНЕРАЦИЯ БАЗЫ БЛАТНЫХ НОМЕРОВ
   */
  private static void generateNumbersDB() {
    out.println("Генерация базы блатных номеров");
    List<String> abc = generateABC();
    out.println(Helpers.tab(4) + "ꓳꓗ");
    List<String> num = generateNumbers();
    out.println(Helpers.tab(3) + "ꓳꓗ");
    List<String> region = generateRegion();
    out.println(Helpers.tab(3) + "ꓳꓗ");

    /* Формирование блатных номеров из сгенерированных значений */
    out.print("Формирование номеров...");
    numbers = new ArrayList<>();
    List<String> temp = new ArrayList<>();
    abc.forEach(a -> num.forEach(n -> temp.add(a + n + a + a)));
    temp.forEach(t -> region.forEach(value -> numbers.add(t + value)));

    out.println(Helpers.tab(3) + "ꓳꓗ");
    Collections.sort(numbers);
    out.println("Сформированно номеров :" + Helpers.tab(3) + numbers.size());
  }
}
