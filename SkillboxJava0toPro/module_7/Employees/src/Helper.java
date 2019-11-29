import java.text.NumberFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Random;

class Helper {
  static String formatToRUB(double value) {
    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("ru", "RU"));
    return formatter.format(value);
  }

  static int randInt(int min, int max) {
    Random rand = new Random();
    return rand.nextInt((max - min) + 1) + min;
  }

  static String getTAB(int count) {
    return "\t".repeat(Math.max(0, count));
  }

  static String formatDate(LocalDate date) {
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
        .ofPattern("dd.MM.yyyy");
    return date.format(DATE_TIME_FORMATTER);
  }

  static LocalDate generateRandomDate(int yearFrom,int yearTo) {
    LocalDate date = null;
    while (date == null) {
      int randomYear = Helper.randInt(yearFrom, yearTo);
      int randomMonth = Helper.randInt(1, 12);
      int randomDay = Helper.randInt(1, 31);
      try {
        date = LocalDate.of(randomYear, randomMonth, randomDay);
      } catch (DateTimeException e) {
        date = null;
      }
    }

    return date;
  }
}
