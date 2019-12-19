import java.util.*;
import java.util.stream.*;

public class MovementStats {
  private List<String> header;
  private List<Movement> movementList = new ArrayList<>();

  public MovementStats(List<String> header) {
    this.header = header;
  }

  public void addMovement(Movement movement) {
    movementList.add(movement);
  }

  public Double getTotalIncome() {
    Double total = 0.0;
    for (Movement m : movementList) {
      total += m.getIncome();
    }

    return total;
  }

  public Double getTotalOutcome() {
    Double total = 0.0;
    for (Movement m : movementList) {
      total += m.getOutcome();
    }

    return total;
  }

  public List<Movement> getIncomeList() {
    return movementList.stream().filter(m -> m.getIncome() != 0).collect(Collectors.toList());
  }

  public List<Movement> getOutcomeList() {
    return movementList.stream().filter(m -> m.getOutcome() != 0).collect(Collectors.toList());
  }

  public List<Movement> getMovementList() {
    return movementList;
  }

  public String getTableFromMovementList(List<Movement> list) {
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

    return new TableGenerator(header, data).getResult();
  }
}
