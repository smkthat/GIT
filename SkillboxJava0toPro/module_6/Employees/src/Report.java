import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Report {
  private Integer id;
  private static Integer idCounter = 0;
  private String name;
  private List<String> header;
  private List<List<String>> dataList;
  private List<Employee> employeeData;
  private List<Company> companyData;

  Report(String name,List<String> header) {
    idCounter++;
    this.name = name;
    this.header = header;
    id = idCounter;
  }

  Report(String name,List<String> header, List<List<String>> dataList) {
    idCounter++;
    this.name = name;
    this.header = header;
    this.dataList = dataList;
    id = idCounter;
  }

  static List<List<String>> generateSalaryStaffData(List<Employee> employees) {
    List<List<String>> dataList = new ArrayList<>();
    employees.forEach(e -> dataList.add(Arrays.asList(
        Integer.toString(e.getId()),
        e.toString(),
        Double.toString(e.getMonthSalary())
    )));

    return dataList;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public static Integer getIdCounter() {
    return idCounter;
  }

  public static void setIdCounter(Integer idCounter) {
    Report.idCounter = idCounter;
  }

  String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  List<String> getHeader() {
    return header;
  }

  public void setHeader(List<String> header) {
    this.header = header;
  }

  List<List<String>> getDataList() {
    return dataList;
  }

  public void setDataList(List<List<String>> dataList) {
    this.dataList = dataList;
  }

  public void addData(List<String> data) {
    dataList.add(data);
  }
}
