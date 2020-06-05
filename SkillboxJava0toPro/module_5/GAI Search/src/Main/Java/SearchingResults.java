package Main.Java;

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

  void addToDataList(List<String> rows) {
    dataList.add(rows);
  }

  List<String> getHeaderList() {
    return headerList;
  }
}
