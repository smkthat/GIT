import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Report {

  private Integer id;
  private static Integer idCounter = 0;
  private String name;
  private List<String> header;
  private List<List<String>> dataList;

  Report(String name, List<String> header) {
    idCounter++;
    this.name = name + "REPORT";
    this.header = header;
    id = idCounter;
  }

  Report(String name, List<String> header, List<List<String>> dataList) {
    idCounter++;
    this.name = name;
    this.header = header;
    this.dataList = dataList;
    id = idCounter;
  }

  static List<List<String>> generateSalaryStaffData(List<Employee> employees) {
    List<List<String>> dataList = new ArrayList<>();
    final AtomicInteger counter = new AtomicInteger(0);
    employees.forEach(e -> dataList.add(Arrays.asList(
        Integer.toString(counter.incrementAndGet()),
        Integer.toString(e.getId()),
        e.toString(),
        Helper.formatToRUB(e.getMonthSalary())
    )));

    return dataList;
  }

  public Integer getId() {
    return id;
  }

  String getName() {
    return name;
  }

  List<String> getHeader() {
    return header;
  }

  List<List<String>> getDataList() {
    return dataList;
  }

  public void addData(List<String> data) {
    if (dataList == null) {
      dataList = new ArrayList<>();
    } else {
      dataList.add(data);
    }
  }
}
