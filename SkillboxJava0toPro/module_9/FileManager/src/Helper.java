import java.text.DecimalFormat;

public class Helper
{
    /** Получаем точки */
    public static String getDots(int start, int max) {
        int count = max - start;
        return ".".repeat(count);
    }

    /** Создаем отступы в зависимости от уровня папки в которой находится File */
    public static String getTab(int level) {
        return "\t".repeat(Math.max(0, level));
    }

    /** Переводит размер файлов к читабельному виду */
    public static String getReadableSize(long size) {
        String[] units = new String[] {"B", "KB", "MB", "GB", "TB"};
        if (size == 0) {
            return "0 " + units[0];
        }

        int unitIndex = (int) (Math.log10(size) / 3);
        double unitValue = 1 << (unitIndex * 10);

        return new DecimalFormat("#,##0.##").format(size / unitValue) + " " + units[unitIndex];
    }
}
