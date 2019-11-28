package Main.Java;

/**
 * ПОМОШНИКИ
 * */
class Helpers {

  /**
   * Добавляет знаки табуляции
   * */
  static String tab(int n) {
    return "\t".repeat(Math.max(0, n));
  }

  /**
   * Проверка корректности ввода номера
   * */
  static boolean checkInputNumber(String number) {
    return number.matches("\\D\\d{3}\\D{2}\\d{3}");
  }
}
