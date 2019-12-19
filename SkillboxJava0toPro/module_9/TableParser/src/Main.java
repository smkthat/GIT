/**
 * Написать код парсинга банковской выписки (файл movementsList.csv).
 * Код должен выводить сводную информацию по этой выписке:
 * общий приход,
 * общий расход,
 * а также разбивку расходов. */

public class Main {

  public static void main(String[] args) {
    TableParser parser = new TableParser("resources\\movementList.csv");
    parser.startParse();
    MovementStats stats = parser.getStats();
    System.out.println("\nВся выпеска:");
    System.out.println(stats.getTableFromMovementList(stats.getMovementList()));
    System.out.println("Всего доход: " + stats.getTotalIncome() + "\nВсего расход: " + stats.getTotalOutcome());
    System.out.println("\nТолько расходы:");
    System.out.println(stats.getTableFromMovementList(stats.getOutcomeList()));
  }
}
