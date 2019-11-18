package Main.Java;

import java.util.*;

import static java.lang.System.out;

public class Loader {

  private static final String EXIT = "выход";
  private static List<String> numbersArrayList;
  private static List<List<String>> tableResultData;
  private static long start;
  private static long finish;
  private static int searchCounter;
  private static boolean isFound;

  public static void main(String[] args) {
    RogueNumberGenerator numberGenerator = new RogueNumberGenerator();
    numbersArrayList = numberGenerator.getRogueNumbersDB();

    boolean isWorking = true;
    searchCounter = 0;
    tableResultData = new ArrayList<>();

    while (isWorking) {
      isFound = false;
      out.println(
          "\nВведите номер автомобиля в формате Х000ХХ000 с русскими буквами: "
              + "\n( наберите \""
              + EXIT
              + "\" чтобы закрыть программу )");
      String inputNumber = new Scanner(System.in).nextLine();

      if (inputNumber.equals(EXIT)) {
        break;
      }

      if (Helpers.checkInputNumber(inputNumber)) {
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
    out.println("Результат:");
  }

  /*  формирование данных для таблицы результатов поиска  */
  private static List<String> creatingSearchResult(String inputNumber) {
    out.println("Поиск начат");
    return Arrays.asList(
        Integer.toString(searchCounter),
        inputNumber,
        enumeration(inputNumber),
        binarySearch(inputNumber),
        treeSetSearch(inputNumber),
        hashSetSearch(inputNumber),
        Boolean.toString(isFound)
    );
  }

  /*  получить шапку таблицы  */
  private static List<String> getHeader() {
    return Arrays.asList("N", "Номер", "Перебор", "TreeSet", "HashSet", "binarySearch", "Найден");
  }

  /*  получить финальную таблицу результатов  */
  private static void showFinalResult() {
    List<String> header = getFinalHeader();

    TreeSet<Long> sortedEnumerationResults = new TreeSet<>();
    TreeSet<Long> sortedTreeSetResults = new TreeSet<>();
    TreeSet<Long> sortedHashSetResults = new TreeSet<>();
    TreeSet<Long> sortedBinarySearchResults = new TreeSet<>();

    tableResultData.forEach(f -> {
      sortedEnumerationResults.add(Long.parseLong(f.get(2)));
      sortedTreeSetResults.add(Long.parseLong(f.get(4)));
      sortedHashSetResults.add(Long.parseLong(f.get(3)));
      sortedBinarySearchResults.add(Long.parseLong(f.get(5)));
    });

    printTable(header, getFinalTableData(
        sortedEnumerationResults,
        sortedTreeSetResults,
        sortedHashSetResults,
        sortedBinarySearchResults
    ));
  }

  /*  Печатает таблицу */
  private static void printTable(List<String> header,
      List<List<String>> finalTableResultData) {
    TableGenerator table = new TableGenerator();
    String finalResult = table.generateTable(header, finalTableResultData);
    out.println(finalResult);
  }

  /*  Получаем данные финальной таблицы результатов */
  private static List<List<String>> getFinalTableData(TreeSet<Long> sortedEnumerationResults,
      TreeSet<Long> sortedHashSetResults, TreeSet<Long> sortedTreeSetResults,
      TreeSet<Long> sortedBinarySearchResults) {

    List<List<String>> finalTableResultData = new ArrayList<>();

    finalTableResultData.add(Arrays.asList(
        "Перебор",
        sortedEnumerationResults.first().toString(),
        "Линейный поиск можно использовать для малого, несортированного набора данных, "
            + "который не увеличивается в размерах."
    ));

    finalTableResultData.add(Arrays.asList(
        "TreeSet",
        sortedTreeSetResults.first().toString(),
        "Если много элементов, сортирует элементы. Неможет хранить null. Не синхронизирован."
    ));

    finalTableResultData.add(Arrays.asList(
        "HashSet",
        sortedHashSetResults.first().toString(),
        "Если много элементов, не сортирует элементы. Может хранить null. Не синхронизирован."
    ));

    finalTableResultData.add(Arrays.asList(
        "binarySearch",
        sortedBinarySearchResults.first().toString(),
        "Двоичный поиск может использоваться для быстрого доступа к "
            + "упорядоченным данным, когда пространство памяти ограничено."
    ));

    return finalTableResultData;
  }

  /*  Получаем шапку финальной таблицы */
  private static List<String> getFinalHeader() {
    return Arrays.asList("Метод", "Наилучший результат", "В каких случаях хорошо использовать");
  }

  /* Спрашивает нужно ли повторит поиск */
  private static boolean repeatSearch() {
    out.println("Повторить поиск? - да/нет");
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

  private static String binarySearch(String inputNumber) {
    /* БИНАРНЫЙ ПОИСК */
    start = System.nanoTime();
    int index = Collections.binarySearch(numbersArrayList, inputNumber);
    finish = System.nanoTime() - start;
    if (index >= 0) {
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
}
