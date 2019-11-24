package Main.Java;

import java.util.ArrayList;
import java.util.List;

class SearchingResults {
  private List<String> headerList;
  private List<List<String>> dataList;

  SearchingResults(List<String> headerList, List<List<String>> dataList) {
    this.headerList = headerList;
    this.dataList = dataList;
  }

  List<List<String>> getDataList() {
    return dataList;
  }

  List<List<String>> getDataListById(int id) {
    List<List<String>> dataById = new ArrayList<>();
    dataById.add(dataList.get(id - 1));
    return dataById;
  }

  void addToDataList(List<String> rows) {
    dataList.add(rows);
  }

  void setHeaderList(List<String> headerList) {
    this.headerList = headerList;
  }

  void setDataList(List<List<String>> dataList) {
    this.dataList = dataList;
  }

  List<String> getHeaderList() {
    return headerList;
  }
}
