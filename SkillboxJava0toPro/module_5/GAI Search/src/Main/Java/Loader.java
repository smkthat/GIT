package Main.Java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Loader {
  private static final String EXIT = "выход";
  private static ArrayList<String> numbersArrayList;
  private static long start;
  private static long finish;

  public static void main(String[] args) throws IOException {
    generateNumbersDB();

    for (; ; ) {
      System.out.println(
          "\nВведите номер автомобиля в формате Х000ХХ000 с русскими буквами: "
              + "\n( наберите \""
              + EXIT
              + "\" чтобы закрыть программу )");
      String inputNumber = (new BufferedReader(new InputStreamReader(System.in))).readLine();

      if (inputNumber.equals(EXIT)) {
        break;
      }

      if (checkInputNumber(inputNumber)) {
        startSearch(inputNumber);
      } else {
        System.err.println("Неправельный ввод!");
      }
    }
  }

  /*  ГЕНЕРАТОР БУКВ. На автомобильных номерных знаках в России используются только те буквы,
  которые присутствуют и в кириллице, и в латинице. Таких букв только 12 — А, В, Е, К, М, Н, О,
  Р, С, Т, У, Х. Подобное ограничение регулируется венской Конвенцией о дорожном движении 1968
  года.  */
  private static ArrayList<String> generateABC() {
    print("Генерация букв...");
    ArrayList<String> abc = new ArrayList<>(Arrays.asList("АВЕКМНОРСТУХ".split("")));
    println(tab(4) + "ꓳꓗ");

    return abc;
  }

  /*  ГЕНЕРАТОР НОМЕРОВ. На мой взгляд, блатной номер должен быть с нулями, зеркальным или состоять
  из 3х одинаковых цифор. Например: 001, 010, 111  */
  private static ArrayList<String> generateNumbers() {
    System.out.print("Генерация номеров...");
    ArrayList<String> num = new ArrayList<>();
    int count;
    for (int k = 0; k < 10; k++) {
      String temp = Integer.toString(k).trim();
      num.add("00" + temp);
    }

    for (int k = 1; k < 10; k++) {
      count = 100 * k;
      String temp = Integer.toString(count).trim();
      num.add(temp);
    }

    for (int k = 1; k < 10; k++) {
      count = 111 * k;
      String temp = Integer.toString(count).trim();
      num.add(temp);
    }
    System.out.println(tab(3) + "ꓳꓗ");

    return num;
  }

  /*  ГЕНЕРАТОР РЕГИОНОВ. Т.к. если использовать только регионы России, то база номеров будет
  слишком маленька. Поэтому, генерируем регионы от 0 до 1000 */
  private static ArrayList<String> generateRegion() {
    System.out.print("Генерация регионов...");
    ArrayList<String> region = new ArrayList<>();
    for (int k = 1; k < 10; k++) {
      String temp = Integer.toString(k).trim();
      region.add("00" + temp);
    }

    for (int k = 10; k < 100; k++) {
      String temp = Integer.toString(k).trim();
      region.add("0" + temp);
    }

    for (int k = 100; k < 10000; k++) { // изменить на k < 200
      String temp = Integer.toString(k).trim();
      region.add(temp);
    }
    System.out.println(tab(3) + "ꓳꓗ");

    return region;
  }

  /*  ГЕНЕРАЦИЯ БАЗЫ БЛАТНЫХ НОМЕРОВ  */
  private static void generateNumbersDB() {
    println("Генерация базы блатных номеров");

    ArrayList<String> abc = generateABC();
    ArrayList<String> num = generateNumbers();
    ArrayList<String> region = generateRegion();

    /* Формирование блатных номеров из сгенерированных значений */
    print("Формирование номеров...");
    numbersArrayList = new ArrayList<>();
    ArrayList<String> temp = new ArrayList<>();
    abc.forEach(s -> num.forEach(value -> temp.add(s.trim() + value.trim() + s.trim() + s.trim())));

    temp.forEach(s -> region.forEach(value -> numbersArrayList.add(s.trim() + value.trim())));

    println(tab(3) + "ꓳꓗ");

    Collections.sort(numbersArrayList);
    temp.clear();
    println("Сформированно номеров :" + tab(3) + numbersArrayList.size());
  }

  /*  ПОИСК  */
  private static void startSearch(String inputNumber) {
    print("Поиск перебором (ArrayList):" + tab(1));
    println(enumeration(inputNumber));
    print("Бинарный  поиск:" + tab(4));
    println(binarySearch(inputNumber));
    print("Поиск по TreeSet:" + tab(4));
    println(treeSetSearch(inputNumber));
    print("Поиск по HashSet:" + tab(4));
    println(hashSetSearch(inputNumber));
  }

  private static String binarySearch(String inputNumber) {
    /* БИНАРНЫЙ ПОИСК */
    boolean numberIsFound = false;
    start = System.nanoTime();
    int index = Collections.binarySearch(numbersArrayList, inputNumber);
    finish = System.nanoTime() - start;
    if (index > 0) {
      numberIsFound = true;
    }

    return numberIsFound + "(" + finish + " ns)";
  }

  private static String treeSetSearch(String inputNumber) {
    /* ПОИСК В TREESET */
    TreeSet<String> numbersTreeSet = new TreeSet<>(numbersArrayList);
    start = System.nanoTime();
    boolean numberIsFound = numbersTreeSet.contains(inputNumber);
    finish = System.nanoTime() - start;
    numbersTreeSet.clear();

    return numberIsFound + "(" + finish + " ns)";
  }

  private static String hashSetSearch(String inputNumber) {
    /* ПОИСК В HASHSET */
    HashSet<String> numbersHashSet = new HashSet<>(numbersArrayList);
    start = System.nanoTime();
    boolean numberIsFound = numbersHashSet.contains(inputNumber);
    finish = System.nanoTime() - start;
    numbersHashSet.clear();

    return numberIsFound + "(" + finish + " ns)";
  }

  private static String enumeration(String inputNumber) {
    /* ПОИСК ПЕРЕБОРОМ */
    start = System.nanoTime();
    boolean numberIsFound = false;
    for (String number : numbersArrayList) {
      if (number.equals(inputNumber)) {
        numberIsFound = true;
        break;
      }
    }
    finish = System.nanoTime() - start;

    return numberIsFound + "(" + finish + " ns)";
  }

  /*  ПОМОШНИКИ */
  private static boolean checkInputNumber(String number) {
    /* Проверка корректности ввода номера */
    return number.matches("\\D\\d{3}\\D{2}\\d{3}");
  }

//  private static long getMilliFromNano(long nanoSeconds) {
//    /* Перевод наносекунд в миллисекунды */
//    return nanoSeconds / 1000000;
//  }

  private static String tab(int n) {
    /* Добавляет знаки табуляции */
    return "\t".repeat(Math.max(0, n));
  }

  private static void print(String string) {
    System.out.print(string);
  }

  private static void println(String string) {
    System.out.println(string);
  }
}
