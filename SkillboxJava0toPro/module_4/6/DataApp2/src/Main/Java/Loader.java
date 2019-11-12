package Main.Java;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Scanner;


public class Loader {

  private static final DateTimeFormatter RU_DATE_FORMATTER = DateTimeFormatter
      .ofPattern("dd.MM.yyyy - EEE")
      .localizedBy(new Locale("ru", "RU"));


  public static void main(String[] args) {
    System.out.println(
        "\nВВЕДИТЕ ДАТУ РОЖДЕНИЯ\n"
            + "Доступные форматы:\n"
            + "\t- день.месяц.год\n");

    LocalDate localBirthDate = null;
    while (localBirthDate == null) {
      System.out.print("\nВвод : ");
      String inputDate = new Scanner(System.in).nextLine();
      localBirthDate = validationInputDate(inputDate);
    }

    System.out.println("\nВывод:");
    LocalDate todayDate = LocalDate.now();
    int n = 0;
    for (int i = localBirthDate.getYear(); i <= todayDate.getYear(); i++) {
      LocalDate tmpDate = localBirthDate.plusYears(n);
      if (tmpDate.isBefore(todayDate)) {
        System.out.println(n + "\t-\t" + tmpDate.format(RU_DATE_FORMATTER));
      }
      n++;
    }
  }

  private static LocalDate validationInputDate(String inputDate) {
    LocalDate validInputDate = null;
    try {
      validInputDate = LocalDate.parse(inputDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    } catch (DateTimeParseException e) {
      System.err.println("\tДата введена неверно! Повторите ввод!");
    }
    return validInputDate;
  }
}
