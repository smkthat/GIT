import java.util.*;
import java.util.stream.Collectors;

public class MovementStats {

  private List<String> header;
  private List<Movement> movementList = new ArrayList<>();

  public MovementStats(List<String> header) {
    this.header = header;
  }

  public void addMovement(Movement movement) {
    movementList.add(movement);
  }

  public String getPartnersSummaryTable() {
    List<List<String>> data = new ArrayList<>();

    movementList.stream()
        .collect(
            Collectors.groupingBy(
                Movement::getPartner,
                Collectors.mapping(
                    p -> new Summary(p.getIncome(), p.getOutcome()),
                    Collectors.reducing(
                        new Summary(0, 0),
                        Summary::merge))))
        .forEach(
            (partner, summary) ->
                data.add(
                    Arrays.asList(
                        partner,
                        Double.toString(summary.income),
                        Double.toString(summary.outcome))));

    return getTableName("Partners summary report")
        + new TableGenerator(Arrays.asList("Partner", "Total income", "Total outcome"), data)
        .getResult();
  }

  public String getPartnersIncomeTable() {
    List<List<String>> data = new ArrayList<>();

    movementList.stream()
        .collect(
            Collectors.groupingBy(
                Movement::getPartner, Collectors.summingDouble(Movement::getIncome)))
        .forEach(
            (partner, income) -> {
              if (income != 0.0) {
                data.add(Arrays.asList(partner, income.toString()));
              }
            });

    return getTableName("Partners total income report")
        + new TableGenerator(Arrays.asList("Partner", "Total income"), data).getResult();
  }

  public String getPartnersOutcomeTable() {
    List<List<String>> data = new ArrayList<>();

    movementList.stream()
        .collect(
            Collectors.groupingBy(
                Movement::getPartner, Collectors.summingDouble(Movement::getOutcome)))
        .forEach(
            (partner, outcome) -> {
              if (outcome != 0.0) {
                data.add(Arrays.asList(partner, outcome.toString()));
              }
            });

    return getTableName("Partners total outcome report")
        + new TableGenerator(Arrays.asList("Partner", "Total outcome"), data).getResult();
  }

  public List<Movement> getAllMovementsByPartner(String partner) {
    return movementList.stream()
        .filter(m -> m.getPartner().equals(partner))
        .collect(Collectors.toList());
  }

  public List<Movement> getParsedMovementList() {
    return movementList;
  }

  public String getTableFromMovementList(String tableName, List<Movement> list) {
    List<List<String>> data = new ArrayList<>();
    list.forEach(
        p ->
            data.add(
                Arrays.asList(
                    p.getType(),
                    p.getAccount(),
                    p.getCurrency(),
                    p.getDate(),
                    p.getReference(),
                    p.getDescription(),
                    p.getIncome().toString(),
                    p.getOutcome().toString())));

    return getTableName(tableName + " -> " + "movements report")
        + new TableGenerator(header, data).getResult();
  }

  private String getTableName(String name) {
    return "\t".repeat(2) + name.toUpperCase() + "\n";
  }

  private static class Summary {

    double income;
    double outcome;

    Summary(double income, double outcome) {
      this.income = income;
      this.outcome = outcome;
    }

    static Summary merge(Summary m1, Summary m2) {
      return new Summary(m1.income + m2.income, m1.outcome + m2.outcome);
    }

    static Summary fromMovement(Movement m) {
      return new Summary(m.getIncome(), m.getOutcome());
    }
  }
}
