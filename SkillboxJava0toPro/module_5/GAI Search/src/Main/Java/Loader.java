package Main.Java;

import java.util.*;

public class Loader {

  private static final String EXIT = "выход";
  private static ArrayList<String> numbersArrayList;
  private static List<List<String>> tableResultData;
  private static long start;
  private static long finish;
  private static int searchCounter;
  private static boolean isFound;

  public static void main(String[] args) {
    generateNumbersDB();
    boolean isWorking = true;
    searchCounter = 0;
    tableResultData = new ArrayList<>();

    while (isWorking) {
      isFound = false;
      System.out.println(
          "\nВведите номер автомобиля в формате Х000ХХ000 с русскими буквами: "
              + "\n( наберите \""
              + EXIT
              + "\" чтобы закрыть программу )");
      String inputNumber = new Scanner(System.in).nextLine();

      if (inputNumber.equals(EXIT)) {
        break;
      }

      if (checkInputNumber(inputNumber)) {

        startSearch(inputNumber);
        showSearchResult();
        isWorking = repeatSearch();
      } else {
        System.err.println("Неправельный ввод!");
      }
    }
  }

  /* Показывает результаты поиска */
  private static void showSearchResult() {
    printTable(getHeader(), tableResultData);
  }

  /*  ПОИСК  */
  private static void startSearch(String inputNumber) {
    searchCounter++;
    tableResultData.add(creatingSearchResult(inputNumber));
  }

  /*  формирование данных для таблицы результатов поиска  */
  private static ArrayList<String> creatingSearchResult(String inputNumber) {
    System.out.println("Поиск начат");
    ArrayList<String> searchResultData = new ArrayList<>();
    searchResultData.add(Integer.toString(searchCounter));
    searchResultData.add(inputNumber);
    searchResultData.add(enumeration(inputNumber));
    searchResultData.add(binarySearch(inputNumber));
    searchResultData.add(treeSetSearch(inputNumber));
    searchResultData.add(hashSetSearch(inputNumber));
    searchResultData.add(Boolean.toString(isFound));
    System.out.println("Результат:");
    return searchResultData;
  }

  /*  получить шапку таблицы  */
  private static ArrayList<String> getHeader() {
    ArrayList<String> header = new ArrayList<>();
    header.add("N");
    header.add("Номер");
    header.add("Перебор");
    header.add("HashSet");
    header.add("TreeSet");
    header.add("binarySearch");
    header.add("Найден");
    return header;
  }

  /*  получить дно таблицы таблицы  */
  private static void showFinalResult() {
    ArrayList<String> header = getFinalHeader();

    TreeSet<Long> sortedEnumerationResults = new TreeSet<>();
    TreeSet<Long> sortedHashSetResults = new TreeSet<>();
    TreeSet<Long> sortedTreeSetResults = new TreeSet<>();
    TreeSet<Long> sortedBinarySearchResults = new TreeSet<>();

    for (List<String> data : tableResultData) {
      sortedEnumerationResults.add(Long.parseLong(data.get(2)));
      sortedHashSetResults.add(Long.parseLong(data.get(3)));
      sortedTreeSetResults.add(Long.parseLong(data.get(4)));
      sortedBinarySearchResults.add(Long.parseLong(data.get(5)));
    }

    List<List<String>> finalTableResultData = getFinalTableData(
        sortedEnumerationResults,
        sortedHashSetResults,
        sortedTreeSetResults,
        sortedBinarySearchResults
    );

    printTable(header, finalTableResultData);
  }

  /*  Печатает таблицу */
  private static void printTable(ArrayList<String> header,
      List<List<String>> finalTableResultData) {
    TableGenerator table = new TableGenerator();
    String finalResult = table.generateTable(header, finalTableResultData);
    System.out.println(finalResult);
  }

  /*  Получаем данные финальной таблицы результатов */
  private static List<List<String>> getFinalTableData(TreeSet<Long> sortedEnumerationResults,
      TreeSet<Long> sortedHashSetResults, TreeSet<Long> sortedTreeSetResults,
      TreeSet<Long> sortedBinarySearchResults) {
    List<List<String>> finalTableResultData = new ArrayList<>();

    ArrayList<String> data = new ArrayList<>();
    data.add("Перебор");
    data.add(sortedEnumerationResults.first().toString());
    data.add(
        "Линейный поиск можно использовать для малого, несортированного набора данных, который не увеличивается в размерах.");
    finalTableResultData.add(data);

    data = new ArrayList<>();
    data.add("HashSet");
    data.add(sortedHashSetResults.first().toString());
    data.add(
        "Если много элементов, не сортирует элементы. Может хранить null. Не синхронизирован.");
    finalTableResultData.add(data);

    data = new ArrayList<>();
    data.add("TreeSet");
    data.add(sortedTreeSetResults.first().toString());
    data.add("Если много элементов, сортирует элементы. Неможет хранить null. Не синхронизирован.");
    finalTableResultData.add(data);

    data = new ArrayList<>();
    data.add("binarySearch");
    data.add(sortedBinarySearchResults.first().toString());
    data.add(
        "Двоичный поиск может использоваться для быстрого доступа к упорядоченным данным, когда пространство памяти ограничено.");
    finalTableResultData.add(data);
    return finalTableResultData;
  }

  /*  Получаем шапку финальной таблицы */
  private static ArrayList<String> getFinalHeader() {
    ArrayList<String> header = new ArrayList<>();
    header.add("Метод");
    header.add("Наилучший результат");
    header.add("В каких случаях хорошо использовать");
    return header;
  }

  /* Спрашивает нужно ли повторит поиск */
  private static boolean repeatSearch() {
    System.out.println("Повторить поиск? - да/нет");
    while (true) {
      String answer = new Scanner(System.in).nextLine();
      if (answer.equals("да")) {
        return true;
      } else if (answer.equals("нет")) {
        showFinalResult();
        return false;
      }
      System.err.println("Повторите ввод! - да/нет");
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
      num.add(String.format("%03d", k));
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

  private static String binarySearch(String inputNumber) {
    /* БИНАРНЫЙ ПОИСК */
    start = System.nanoTime();
    int index = Collections.binarySearch(numbersArrayList, inputNumber);
    finish = System.nanoTime() - start;
    if (index > 0) {
      isFound = true;
    }
    return Long.toString(finish);
  }

  private static String treeSetSearch(String inputNumber) {
    /* ПОИСК В TREESET */
    TreeSet<String> numbersTreeSet = new TreeSet<>(numbersArrayList);
    start = System.nanoTime();
    isFound = numbersTreeSet.contains(inputNumber);
    finish = System.nanoTime() - start;
    numbersTreeSet.clear();
    return Long.toString(finish);
  }

  private static String hashSetSearch(String inputNumber) {
    /* ПОИСК В HASHSET */
    HashSet<String> numbersHashSet = new HashSet<>(numbersArrayList);
    start = System.nanoTime();
    isFound = numbersHashSet.contains(inputNumber);
    finish = System.nanoTime() - start;
    numbersHashSet.clear();
    return Long.toString(finish);
  }

  private static String enumeration(String inputNumber) {
    /* ПОИСК ПЕРЕБОРОМ */
    start = System.nanoTime();
    for (String number : numbersArrayList) {
      if (number.equals(inputNumber)) {
        isFound = true;
        break;
      }
    }
    finish = System.nanoTime() - start;
    return Long.toString(finish);
  }

  /*  ПОМОШНИКИ */
  private static boolean checkInputNumber(String number) {
    /* Проверка корректности ввода номера */
    return number.matches("\\D\\d{3}\\D{2}\\d{3}");
  }

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
