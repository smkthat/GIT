/**
 * Написать код парсинга банковской выписки (файл movementsList.csv). Код должен выводить сводную
 * информацию по этой выписке: общий приход, общий расход, а также разбивку расходов.
 */
public class Main {

  public static void main(String[] args) {
    TableParser parser = new TableParser("resources/movementList.csv");
    parser.startParse();
    MovementStats stats = parser.getStats();

    System.out.println(
        stats.getTableFromMovementList(
            "resources/movementList.csv", stats.getParsedMovementList()));

    System.out.println(stats.getPartnersSummaryTable());

    System.out.println(stats.getPartnersIncomeTable());

    System.out.println(stats.getPartnersOutcomeTable());

    System.out.println(
        stats.getTableFromMovementList(
            "ALFA_MOBILE>MOSCOW", stats.getAllMovementsByPartner("ALFA_MOBILE>MOSCOW")));
  }
}
