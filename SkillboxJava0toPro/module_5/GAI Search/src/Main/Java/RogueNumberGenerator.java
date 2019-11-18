package Main.Java;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class RogueNumberGenerator {

  private static ArrayList<String> numbersArrayList;

  RogueNumberGenerator() {
    generateNumbersDB();
  }

  List<String> getRogueNumbersDB() {
    return numbersArrayList;
  }

  /*  ГЕНЕРАТОР БУКВ. На автомобильных номерных знаках в России используются только те буквы,
  которые присутствуют и в кириллице, и в латинице. Таких букв только 12 — А, В, Е, К, М, Н, О,
  Р, С, Т, У, Х. Подобное ограничение регулируется венской Конвенцией о дорожном движении 1968
  года.  */
  private static ArrayList<String> generateABC() {
    out.print("Генерация букв...");
    return new ArrayList<>(Arrays.asList("АВЕКМНОРСТУХ".split("")));
  }

  /*  ГЕНЕРАТОР НОМЕРОВ. На мой взгляд, блатной номер должен быть с нулями, зеркальным или состоять
  из 3х одинаковых цифор. Например: 001, 010, 111  */
  private static ArrayList<String> generateNumbers() {
    out.print("Генерация номеров...");
    ArrayList<String> num = new ArrayList<>();
    int count;
    for (int k = 0; k < 10; k++) {
      num.add(String.format("%03d", k));
    }

    for (int k = 1; k < 10; k++) {
      count = 100 * k;
      num.add(Integer.toString(count));
    }

    for (int k = 1; k < 10; k++) {
      count = 111 * k;
      num.add(Integer.toString(count));
    }

    return num;
  }

  /*  ГЕНЕРАТОР РЕГИОНОВ. Т.к. если использовать только регионы России, то база номеров будет
  слишком маленька. Поэтому, генерируем регионы от 0 до 1000 */
  private static ArrayList<String> generateRegion() {
    out.print("Генерация регионов...");
    ArrayList<String> region = new ArrayList<>();
    for (int k = 1; k < 10; k++) {
      region.add(String.format("%03d", k));
    }

    for (int k = 10; k < 100; k++) {
      region.add(String.format("%03d", k));
    }

    for (int k = 100; k < 10000; k++) { // изменить на k < 200
      region.add(Integer.toString(k));
    }

    return region;
  }

  /*  ГЕНЕРАЦИЯ БАЗЫ БЛАТНЫХ НОМЕРОВ  */
  private static void generateNumbersDB() {
    out.println("Генерация базы блатных номеров");

    ArrayList<String> abc = generateABC();
    out.println(Helper.tab(4) + "ꓳꓗ");
    ArrayList<String> num = generateNumbers();
    out.println(Helper.tab(3) + "ꓳꓗ");
    ArrayList<String> region = generateRegion();
    out.println(Helper.tab(3) + "ꓳꓗ");

    /* Формирование блатных номеров из сгенерированных значений */
    out.print("Формирование номеров...");
    numbersArrayList = new ArrayList<>();
    ArrayList<String> temp = new ArrayList<>();
    abc.forEach(s -> num.forEach(value -> temp.add(s + value + s + s)));
    temp.forEach(s -> region.forEach(value -> numbersArrayList.add(s + value)));

    out.println(Helper.tab(3) + "ꓳꓗ");
    Collections.sort(numbersArrayList);
    out.println("Сформированно номеров :" + Helper.tab(3) + numbersArrayList.size());
  }
}
