package Main.Java;

class Helper {
  static String tab(int n) {
    /* Добавляет знаки табуляции */
    return "\t".repeat(Math.max(0, n));
  }

  /*  ПОМОШНИКИ */
  static boolean checkInputNumber(String number) {
    /* Проверка корректности ввода номера */
    return number.matches("\\D\\d{3}\\D{2}\\d{3}");
  }
}
