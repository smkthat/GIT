import java.text.NumberFormat;
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
}
