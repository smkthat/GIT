public class Main
{
    public static void main(String[] args)
    {
        /*for(int i = 612; i > 212; i--)
        {
            System.out.println("Ticket number: " + i);
        }*/

        /**Цикл "doWhile" является оператором постусловия (сначала выполнит, потом проверит).
        То есть, даже если условие не выполняется никогда, всё равно действие будет выполнено хотябы один раз.**/

        int i = 200000;
        do System.out.println("Ticket number: " + (i++)); while (i <= 210000);
        i = 220000;
        do System.out.println("Ticket number: " + (i++)); while (i <= 235000);
    }
}
